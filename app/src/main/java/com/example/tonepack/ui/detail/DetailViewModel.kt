package com.example.tonepack.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tonepack.data.local.entity.Template
import com.example.tonepack.data.repository.TemplateRepository
import com.example.tonepack.data.session.SessionManager
import kotlinx.coroutines.launch

// SessionManager를 추가로 주입받아 현재 유저 ID를 가져옵니다.
class DetailViewModel(
    private val repository: TemplateRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _template = MutableLiveData<Template?>()
    val template: LiveData<Template?> = _template

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // 처리 결과(성공/실패 메시지)를 화면에 띄우기 위한 LiveData
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    // 템플릿 정보를 ID로 불러오기
    fun loadTemplate(templateId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val data = repository.getTemplateById(templateId)
                _template.value = data
            } catch (e: Exception) {
                _template.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 좋아요 업데이트: userId를 함께 전달하도록 변경
    fun onLikeClicked(templateId: Int) {
        viewModelScope.launch {
            val userId = sessionManager.getUserId() ?: "guest" // 현재 유저 아이디 가져오기
            val result = repository.updateLike(userId, templateId) // Repository의 새로운 구조 반영
            _toastMessage.value = result // "추천되었습니다" 또는 "이미 참여..." 메시지 저장
            loadTemplate(templateId) // 화면 갱신을 위해 재로드
        }
    }

    // 비추천 업데이트: userId를 함께 전달하도록 변경
    fun onDislikeClicked(templateId: Int) {
        viewModelScope.launch {
            val userId = sessionManager.getUserId() ?: "guest"
            val result = repository.updateDislike(userId, templateId)
            _toastMessage.value = result
            loadTemplate(templateId)
        }
    }
}

/**
 * DetailViewModel을 생성할 때 Repository와 SessionManager를 안전하게 넣어주는 공장(Factory)
 */
class DetailViewModelFactory(
    private val repository: TemplateRepository,
    private val sessionManager: SessionManager // [추가] 세션 매니저 주입
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(repository, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}