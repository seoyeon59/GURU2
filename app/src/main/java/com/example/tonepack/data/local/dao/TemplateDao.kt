package com.example.tonepack.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tonepack.data.local.entity.Template
import kotlinx.coroutines.flow.Flow

/**
 * Template 테이블: 템플릿 조회, 필터링, 추천 기능을 관리하는 DAO입니다.
 */
@Dao
interface TemplateDao {

    /**
     * 초기 데이터 주입: SeedData의 리스트를 DB에 저장합니다.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(templates: List<Template>)

    /**
     * 필터링 조회 (추천 수 순 정렬로 수정됨)
     * 1. 아무것도 선택 안 함: situation="%%", target="%%" 전달 -> 전체 리스트 반환
     * 2. 상황(situation)만 선택: target="%%" 전달 -> 해당 상황 내 모든 상대 리스트 반환
     * [수정] 기존 최신순에서 추천(likeCount)이 많은 순으로 정렬 기준 변경
     */
    @Query("""
        SELECT * FROM templates 
        WHERE situation LIKE :situation 
        AND target LIKE :target 
        ORDER BY likeCount DESC, `index` DESC
    """)
    fun getFilteredTemplates(situation: String, target: String): Flow<List<Template>>


    // 전체 조회: 모든 템플릿을 추천(likeCount)이 많은 순으로 가져옵니다. (동점일 경우 최신순)
    @Query("SELECT * FROM templates ORDER BY likeCount DESC, `index` DESC")
    fun getAllTemplates(): Flow<List<Template>>

    // 상세 조회: 리스트에서 특정 아이템을 클릭했을 때 사용합니다.
    @Query("SELECT * FROM templates WHERE `index` = :templateId")
    suspend fun getTemplateById(templateId: Int): Template?

    // 추천 수 증가
    @Query("UPDATE templates SET likeCount = likeCount + 1 WHERE `index` = :templateId")
    suspend fun updateLikeCount(templateId: Int)

    // 비추천 수 증가
    @Query("UPDATE templates SET dislikeCount = dislikeCount + 1 WHERE `index` = :templateId")
    suspend fun updateDislikeCount(templateId: Int)

    // 특정 템플릿 삭제
    @Query("DELETE FROM templates WHERE `index` = :templateId")
    suspend fun deleteTemplate(templateId: Int)

    // authorId 기준으로 내가 쓴 글 목록 가져오기 (내가 쓴 글은 최신순 정렬 유지)
    @Query("SELECT * FROM templates WHERE authorId = :authorId ORDER BY `index` DESC")
    fun getTemplatesByAuthor(authorId: String): Flow<List<Template>>

}