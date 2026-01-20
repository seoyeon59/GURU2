package com.example.tonepack.data.repository

import com.example.tonepack.data.local.dao.TemplateDao
import com.example.tonepack.data.local.entity.Template
import kotlinx.coroutines.flow.Flow

/**
 * TemplateRepository: 템플릿 조회/저장/삭제/복사데이터조회 + 커뮤니티/추천·비추천 로직 통합 관리
 */
class TemplateRepository(private val templateDao: TemplateDao) {

    // 카테고리 필터링 및 전체 조회 로직
    // situation이나 target이 없으면 "%%"를 전달하여 전체 데이터 가져옴
    fun getFilteredTemplates(situation: String?, target: String?): Flow<List<Template>> {
        val sitQuery = if (situation.isNullOrEmpty()) "%%" else situation
        val targetQuery = if (target.isNullOrEmpty()) "%%" else target
        return templateDao.getFilteredTemplates(sitQuery, targetQuery)
    }

    // 특정 템플릿 상세 조회 (복사할 데이터를 가져올 때 사용)
    suspend fun getTemplateById(id: Int): Template? = templateDao.getTemplateById(id)

    // 새로운 템플릿 작성 및 저장 (Dao의 insertAll이 List를 받으므로 listOf로 감쌈)
    suspend fun insertTemplate(template: Template) = templateDao.insertAll(listOf(template))

    // 특정 템플릿 삭제
    suspend fun deleteTemplate(id: Int) = templateDao.deleteTemplate(id)

    // 추천 로직 (Dao의 함수명과 일치시킴)
    suspend fun updateLike(id: Int) = templateDao.updateLikeCount(id)

    // 비추천 로직 (Dao의 함수명과 일치시킴)
    suspend fun updateDislike(id: Int) = templateDao.updateDislikeCount(id)

    // 내가 쓴 글 조회
    fun getTemplatesByAuthor(authorId: String): Flow<List<Template>> {
        return templateDao.getTemplatesByAuthor(authorId)
    }
}