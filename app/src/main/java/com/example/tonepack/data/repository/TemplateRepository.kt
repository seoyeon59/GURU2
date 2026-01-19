package com.example.tonepack.data.repository

import com.example.tonepack.data.local.dao.TemplateDao
import com.example.tonepack.data.local.entity.Template
import kotlinx.coroutines.flow.Flow

class TemplateRepository(private val templateDao: TemplateDao) {

    // 상황과 상대에 따른 필터링된 템플릿 목록 조회
    fun getFilteredTemplates(situation: String?, target: String?): Flow<List<Template>> {
        val sitQuery = if (situation.isNullOrEmpty()) "%%" else situation
        val targetQuery = if (target.isNullOrEmpty()) "%%" else target

        return templateDao.getFilteredTemplates(sitQuery, targetQuery)
    }

    fun getAllTemplates(): Flow<List<Template>> {
        return templateDao.getAllTemplates()
    }

    suspend fun getTemplateById(id: Int): Template? {
        return templateDao.getTemplateById(id)
    }

    suspend fun insertTemplate(template: Template) {
        templateDao.insertAll(listOf(template))
    }

    suspend fun updateLike(id: Int) {
        templateDao.updateLikeCount(id)
    }

    suspend fun updateDislike(id: Int) {
        templateDao.updateDislikeCount(id)
    }

    suspend fun deleteTemplate(id: Int) {
        templateDao.deleteTemplate(id)
    }
}