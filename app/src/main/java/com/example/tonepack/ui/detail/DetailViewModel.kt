package com.example.tonepack.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tonepack.App
import com.example.tonepack.data.local.entity.Template
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as App).templateRepository

    private val _template = MutableLiveData<Template?>()
    val template: LiveData<Template?> = _template

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadTemplate(templateId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
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

    fun onLikeClicked(templateId: Int) {
        viewModelScope.launch {
            try {
                repository.updateLike(templateId)
                loadTemplate(templateId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onDislikeClicked(templateId: Int) {
        viewModelScope.launch {
            try {
                repository.updateDislike(templateId)
                loadTemplate(templateId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}