package com.example.tonepack.ui.editor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tonepack.App
import com.example.tonepack.data.local.entity.Template
import kotlinx.coroutines.launch

class EditorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as App).templateRepository
    private val sessionManager = (application as App).sessionManager

    private val _saveSuccess = MutableLiveData<Boolean>()
    val saveSuccess: LiveData<Boolean> = _saveSuccess

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    // 입력값 검증 후 새 템플릿 저장
    fun saveTemplate(
        title: String,
        content: String,
        situation: String,
        target: String
    ) {
        // 입력값 검증
        if (title.isBlank()) {
            _errorMessage.value = "제목을 입력해주세요."
            return
        }
        if (content.isBlank()) {
            _errorMessage.value = "내용을 입력해주세요."
            return
        }

        if (situation.isBlank() || situation == "선택하세요") {
            _errorMessage.value = "상황을 선택해주세요."
            return
        }
        if (target.isBlank() || target == "선택하세요") {
            _errorMessage.value = "상대를 선택해주세요."
            return
        }

        val userId = sessionManager.getUserId()
        if (userId == null) {
            _errorMessage.value = "로그인이 필요합니다."
            return
        }

        // Template 객체 생성 및 DB 저장
        val template = Template(
            authorId = userId,
            title = title,
            content = content,
            situation = situation,
            target = target,
            likeCount = 0,
            dislikeCount = 0
        )

        viewModelScope.launch {
            try {
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