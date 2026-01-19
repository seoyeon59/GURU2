package com.example.tonepack.ui.editor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tonepack.App
import com.example.tonepack.data.local.entity.Template
import kotlinx.coroutines.launch

/**
 * 글 작성 화면의 ViewModel
 *
 * 역할:
 *  - 사용자가 입력한 제목/내용/상황/상대를 검증
 *  - 검증 통과 시 DB에 새 템플릿 저장
 *  - 저장 성공/실패 결과를 Activity에 전달
 *
 */
class EditorViewModel(application: Application) : AndroidViewModel(application) {

    // App.kt에서 만든 싱글톤들 가져오기
    private val repository = (application as App).templateRepository
    private val sessionManager = (application as App).sessionManager

    // 저장 성공 여부를 Activity에 알려주는 LiveData
    private val _saveSuccess = MutableLiveData<Boolean>()
    val saveSuccess: LiveData<Boolean> = _saveSuccess

    // 에러 메시지를 Activity에 전달하는 LiveData
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    /**
     * 사용자가 입력한 템플릿을 DB에 저장하는 함수
     *
     * 검증 항목:
     *  1. 제목이 비어있지 않은지
     *  2. 내용이 비어있지 않은지
     *  3. 상황이 선택되었는지 ("선택하세요" 제외)
     *  4. 상대가 선택되었는지 ("선택하세요" 제외)
     *  5. 로그인 상태인지 (authorId 필요)
     *
     * @param title 템플릿 제목
     * @param content 템플릿 내용
     * @param situation 상황 (출근, 업무, 퇴사 등)
     * @param target 상대 (상사, 동기, 업체 등)
     */
    fun saveTemplate(
        title: String,
        content: String,
        situation: String,
        target: String
    ) {
        // 1. 입력값 검증 (빈 값 체크)
        if (title.isBlank()) {
            _errorMessage.value = "제목을 입력해주세요."
            return
        }
        if (content.isBlank()) {
            _errorMessage.value = "내용을 입력해주세요."
            return
        }

        // 2. Spinner 선택 검증 ("선택하세요"는 제외)
        if (situation.isBlank() || situation == "선택하세요") {
            _errorMessage.value = "상황을 선택해주세요."
            return
        }
        if (target.isBlank() || target == "선택하세요") {
            _errorMessage.value = "상대를 선택해주세요."
            return
        }

        // 3. 로그인 상태 확인 (authorId 필요)
        val userId = sessionManager.getUserId()
        if (userId == null) {
            _errorMessage.value = "로그인이 필요합니다."
            return
        }

        // 4. Template 객체 생성 (서연님의 Entity 구조 따름)
        val template = Template(
            authorId = userId,          // 현재 로그인한 유저 ID
            title = title,              // 사용자 입력 제목
            content = content,          // 사용자 입력 내용
            situation = situation,      // Spinner에서 선택한 상황
            target = target,            // Spinner에서 선택한 상대
            likeCount = 0,             // 새 글이므로 0
            dislikeCount = 0           // 새 글이므로 0
        )

        // 5. Repository를 통해 DB에 저장
        viewModelScope.launch {
            try {
                // 서연님의 TemplateRepository.insertTemplate() 호출
                repository.insertTemplate(template)
                _saveSuccess.value = true
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "저장에 실패했습니다: ${e.message}"
                _saveSuccess.value = false
            }
        }
    }
}