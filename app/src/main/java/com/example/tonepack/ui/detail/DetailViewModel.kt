package com.example.tonepack.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tonepack.data.local.entity.TemplateEntity
import com.example.tonepack.data.repository.TemplateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 상세 페이지의 두뇌 (ViewModel)
 * 민경 담당: 데이터 로드 및 추천 기능 로직 관리
 */
class DetailViewModel(private val repository: TemplateRepository) : ViewModel() {

    // 1. 현재 화면에 보여줄 템플릿 데이터를 보관하는 상자
    // (StateFlow는 데이터가 변하면 화면에 자동으로 알려줘요!)
    private val _template = MutableStateFlow<TemplateEntity?>(null)
    val template: StateFlow<TemplateEntity?> = _template.asStateFlow()

    // 2. [민경 담당] 특정 ID의 템플릿을 불러오는 함수
    // DetailActivity가 켜질 때 이 함수를 호출할 거예요.
    fun loadTemplate(id: Int) {
        viewModelScope.launch {
            // repository야, DB에서 이 ID 가진 데이터 좀 가져와줘!
            repository.getTemplateById(id).collect { data ->
                _template.value = data
            }
        }
    }

    // 3. [민경 담당] 추천 또는 비추천 클릭 시 호출
    fun onLikeClick(id: Int) {
        viewModelScope.launch {
            repository.updateLikeCount(id, true) // true는 추천
        }
    }

    fun onDislikeClick(id: Int) {
        viewModelScope.launch {
            repository.updateLikeCount(id, false) // false는 비추천
        }
    }
}