package com.example.tonepack.data.repository

import com.example.tonepack.data.local.dao.TemplateDao
import com.example.tonepack.data.local.entity.TemplateEntity
import kotlinx.coroutines.flow.Flow

/**
 * 데이터 창고 관리자 (Repository)
 * 민경 담당: 특정 ID로 템플릿 상세 정보 가져오기
 */
class TemplateRepository(private val templateDao: TemplateDao) {

    // 1. 모든 템플릿 가져오기 (이건 공용)
    fun getAllTemplates(): Flow<List<TemplateEntity>> = templateDao.getAllTemplates()

    // 2. [민경 담당] 특정 ID로 상세 내용 하나만 가져오기
    // 상세 페이지에서 "이 번호 아이템 보여줘!" 할 때 사용합니다.
    fun getTemplateById(id: Int): Flow<TemplateEntity> {
        return templateDao.getTemplateById(id)
    }

    // 3. [민경 담당] 추천/비추천 업데이트 하기
    // 나중에 상세 페이지에서 버튼 눌렀을 때 DB 값을 바꿉니다.
    suspend fun updateLikeCount(id: Int, isLike: Boolean) {
        if (isLike) {
            templateDao.incrementLikeCount(id)
        } else {
            templateDao.incrementDislikeCount(id)
        }
    }
}