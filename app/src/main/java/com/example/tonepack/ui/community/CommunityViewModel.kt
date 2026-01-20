package com.example.tonepack.ui.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.tonepack.data.repository.TemplateRepository

class CommunityViewModel(private val repository: TemplateRepository) : ViewModel() {

    // 전체 템플릿 리스트를 LiveData 형태로 들고 있습니다.
    // Activity는 이 allTemplates를 관찰(Observe)만 하면 됩니다.
    val allTemplates = repository.getFilteredTemplates(null, null).asLiveData()
}

/**
 * ViewModel에 repository를 주입하기 위해 필요한 Factory 클래스입니다.
 */
class CommunityViewModelFactory(private val repository: TemplateRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommunityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CommunityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}