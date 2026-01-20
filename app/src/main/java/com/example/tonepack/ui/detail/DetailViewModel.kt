package com.example.tonepack.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tonepack.data.local.entity.Template
import com.example.tonepack.data.repository.TemplateRepository
import kotlinx.coroutines.launch

// Factory를 통해 Repository를 직접 받으므로 일반 ViewModel을 사용
class DetailViewModel(private val repository: TemplateRepository) : ViewModel() {

    private val _template = MutableLiveData<Template?>()
    val template: LiveData<Template?> = _template

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

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

    // 좋아요 업데이트
    fun onLikeClicked(templateId: Int) {
        viewModelScope.launch {
            repository.updateLike(templateId) // Repository 함수 이름 확인 필요 (updateLike)
            loadTemplate(templateId)          // 화면 갱신을 위해 재로드
        }
    }

    // 비추천 업데이트
    fun onDislikeClicked(templateId: Int) {
        viewModelScope.launch {
            repository.updateDislike(templateId) // Repository 함수 이름 확인 필요 (updateDislike)
            loadTemplate(templateId)             // 화면 갱신을 위해 재로드
        }
    }
}

/**
 * DetailViewModel을 생성할 때 Repository를 안전하게 넣어주는 공장(Factory)
 */
class DetailViewModelFactory(private val repository: TemplateRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}