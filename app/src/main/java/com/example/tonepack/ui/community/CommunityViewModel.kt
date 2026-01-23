package com.example.tonepack.ui.community

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.example.tonepack.data.repository.TemplateRepository

class CommunityViewModel(private val repository: TemplateRepository) : ViewModel() {

    // 현재 선택된 필터 조건을 저장하는 LiveData
    // Pair(상황, 상대) 형태로 관리
    private val filterCriteria = MutableLiveData<Pair<String?, String?>>(Pair(null, null))

    // switchMap을 사용하여 filterCriteria가 변할 때마다 새로운 쿼리 결과를 가져옴
    val allTemplates = filterCriteria.switchMap { criteria ->
        repository.getFilteredTemplates(criteria.first, criteria.second).asLiveData()
    }

    /**
     * 스피너에서 선택된 상황과 상대를 필터에 적용
     * "선택하세요(기본값)"일 경우에는 검색 조건에서 제외(null) 처리
     */
    fun fetchFilteredTemplates(situation: String, target: String) {
        val sitFilter = if (situation == "선택하세요(기본값)") null else situation
        val tarFilter = if (target == "선택하세요(기본값)") null else target

        filterCriteria.value = Pair(sitFilter, tarFilter)
    }
}


// ViewModel에 repository를 주입하기 위해 필요한 Factory 클래스
class CommunityViewModelFactory(private val repository: TemplateRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommunityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CommunityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}