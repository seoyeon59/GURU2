package com.example.tonepack.ui.mypage

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tonepack.App
import com.example.tonepack.data.local.entity.Template
import com.example.tonepack.data.repository.TemplateRepository
import com.example.tonepack.data.session.SessionManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * MyPageViewModel
 *
 * - 로그인한 유저 ID(SessionManager 기준)를 가져와서
 *   authorId == userId 인 템플릿만 "내 글 목록"으로 제공
 * - 내 글 삭제 기능 제공 (DB에서 실제 delete)
 *
 * 나중에 북마크 컬럼(isBookmarked 등)이 생기면,
 * 같은 구조로 "저장한 글" LiveData도 추가하면 됨.
 */
class MyPageViewModel(
    application: Application
) : AndroidViewModel(application) {

    // App(Application)에서 Repository / SessionManager 꺼내오기
    private val templateRepository: TemplateRepository
    private val sessionManager: SessionManager

    // 1) 내가 쓴 글 리스트
    private val _myPosts = MutableLiveData<List<Template>>()
    val myPosts: LiveData<List<Template>> = _myPosts

    // 2) 에러 메시지 (토스트나 스낵바로 보여줄 용도)
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        val app = application as App
        templateRepository = app.templateRepository
        sessionManager = app.sessionManager

        // ViewModel 생성 시 내 글을 바로 로딩
        loadMyPosts()
    }

    /**
     * 로그인한 유저 기준으로 "내가 쓴 글" 목록 로딩
     */
    fun loadMyPosts() {
        val userId = sessionManager.getUserId()

        if (userId.isNullOrEmpty()) {
            // 로그인 정보가 없으면 리스트 비우고 에러 메시지 세팅
            _myPosts.value = emptyList()
            _errorMessage.value = "로그인 정보가 없습니다. 다시 로그인해 주세요."
            return
        }

        viewModelScope.launch {
            try {
                // Flow<List<Template>> 를 collect 해서 LiveData에 반영
                templateRepository
                    .getTemplatesByAuthor(userId)
                    .collectLatest { templates ->
                        _myPosts.value = templates
                    }
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "내 글을 불러오는 중 오류가 발생했습니다."
            }
        }
    }

    /**
     * 내 글 삭제
     * - Adapter에서 Template 객체를 넘겨주는 방식이 사용하기 편함
     * - DB에서 삭제 → Flow가 자동으로 다시 emit 되므로
     *   별도의 리스트 재로딩 없이도 myPosts가 갱신됨
     */
    fun deletePost(template: Template) {
        viewModelScope.launch {
            try {
                templateRepository.deleteTemplate(template.index)
                // delete 후에는 Room + Flow 조합 덕분에
                // getTemplatesByAuthor Flow가 알아서 변경된 리스트를 다시 흘려보냄
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "글 삭제 중 오류가 발생했습니다."
            }
        }
    }

    /**
     * 에러 메시지 한 번 보여준 뒤 초기화할 때 사용
     * (Activity/Fragment에서 observe 하다가 사용)
     */
    fun clearError() {
        _errorMessage.value = null
    }
}
