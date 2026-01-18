package com.example.tonepack.ui.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tonepack.data.local.entity.TemplateEntity
import com.example.tonepack.data.repository.TemplateRepository
import kotlinx.coroutines.launch

/**
 * 템플릿 추가(에디터) 페이지의 두뇌
 * 민경 담당: 사용자가 입력한 템플릿 정보를 DB로 전달하는 역할
 */
class EditorViewModel(private val repository: TemplateRepository) : ViewModel() {

    // [민경 담당] 사용자가 입력한 제목과 내용을 받아서 저장하는 함수
    fun saveTemplate(title: String, content: String) {
        viewModelScope.launch {
            // 1. 입력된 텍스트들을 TemplateEntity라는 상자에 예쁘게 담기
            val newTemplate = TemplateEntity(
                title = title,
                content = content,
                likeCount = 0 // 새로 만드는 거니까 추천수는 0부터 시작!
            )

            // 2. 창고(Repository)에게 이 상자를 저장해달라고 시키기
            repository.insertTemplate(newTemplate)
        }
    }
}