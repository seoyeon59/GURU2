package com.example.tonepack.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tonepack.App
import com.example.tonepack.data.local.entity.Template
import kotlinx.coroutines.launch

/**
 * 상세 화면의 ViewModel
 * 역할:
 *  - Repository에서 템플릿 데이터를 가져와서 Activity에 전달
 *  - 복사 기능은 Activity에서 ClipboardUtil로 직접 처리
 *  - 추천/비추천 버튼 클릭 시 DB 업데이트
 *
 */
class DetailViewModel(application: Application) : AndroidViewModel(application) {

    // App.kt에서 만든 싱글톤 Repository 가져오기
    private val repository = (application as App).templateRepository

    // 현재 화면에 표시할 템플릿 데이터 (LiveData로 관찰 가능)
    private val _template = MutableLiveData<Template?>()
    val template: LiveData<Template?> = _template

    // 로딩 상태 (필요하면 ProgressBar 표시용)
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    /**
     * 특정 ID의 템플릿을 DB에서 불러오는 함수
     * DetailActivity가 시작될 때 호출됨
     *
     * @param templateId 조회할 템플릿의 인덱스 번호 (PK)
     */
    fun loadTemplate(templateId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Repository를 통해 DB에서 데이터 가져오기
                val data = repository.getTemplateById(templateId)
                _template.value = data
            } catch (e: Exception) {
                e.printStackTrace()
                _template.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * 추천 버튼 클릭 시 호출되는 함수
     * DB의 likeCount를 +1 증가시킴
     *
     * @param templateId 추천할 템플릿 ID
     */
    fun onLikeClicked(templateId: Int) {
        viewModelScope.launch {
            try {
                // 서연님의 TemplateDao.updateLikeCount() 호출
                repository.updateLike(templateId)
                // 업데이트 후 최신 데이터 다시 불러오기
                loadTemplate(templateId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 비추천 버튼 클릭 시 호출되는 함수
     * DB의 dislikeCount를 +1 증가시킨다
     *
     * @param templateId 비추천할 템플릿 ID
     */
    fun onDislikeClicked(templateId: Int) {
        viewModelScope.launch {
            try {
                // 서연님의 TemplateDao.updateDislikeCount() 호출
                repository.updateDislike(templateId)
                // 업데이트 후 최신 데이터 다시 불러오기
                loadTemplate(templateId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}