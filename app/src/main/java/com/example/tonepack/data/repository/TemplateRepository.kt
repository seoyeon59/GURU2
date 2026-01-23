package com.example.tonepack.data.repository

import com.example.tonepack.data.local.dao.TemplateDao
import com.example.tonepack.data.local.dao.TemplateLikeDao
import com.example.tonepack.data.local.entity.Template
import com.example.tonepack.data.local.entity.TemplateLike
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * TemplateRepository: 템플릿 조회/저장/삭제/복사데이터조회 + 커뮤니티/추천·비추천 로직 통합 관리
 */
class TemplateRepository(
    private val templateDao: TemplateDao,
    private val likeDao: TemplateLikeDao // [추가] 좋아요 중복 체크를 위한 Dao
) {

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

     // 추천 로직 (1인 1추천 제한 적용)
    suspend fun updateLike(userId: String, templateId: Int): String = withContext(Dispatchers.IO) {
        // 이미 추천 혹은 비추천을 눌렀는지 확인 (중복 방지)
        if (likeDao.hasLiked(userId, templateId)) {
            "이미 참여하신 게시글입니다."
        } else {
            // 좋아요 기록 저장 후 카운트 증가
            likeDao.insertLike(TemplateLike(userId, templateId))
            templateDao.updateLikeCount(templateId)
            "추천되었습니다! 👍"
        }
    }


    // 비추천 로직 (1인 1비추천 제한 적용)
    suspend fun updateDislike(userId: String, templateId: Int): String = withContext(Dispatchers.IO) {
        if (likeDao.hasLiked(userId, templateId)) {
            "이미 참여하신 게시글입니다."
        } else {
            // 기록 저장 후 비추천 카운트 증가
            likeDao.insertLike(TemplateLike(userId, templateId))
            templateDao.updateDislikeCount(templateId)
            "비추천되었습니다."
        }
    }

    // 내가 쓴 글 조회
    fun getTemplatesByAuthor(authorId: String): Flow<List<Template>> {
        return templateDao.getTemplatesByAuthor(authorId)
    }
}