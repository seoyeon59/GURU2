package com.example.tonepack.data.local.dao

import androidx.room.*
import com.example.tonepack.data.local.entity.TemplateLike

@Dao
interface TemplateLikeDao {
    // 특정 유저가 특정 게시글에 좋아요를 눌렀는지 확인
    @Query("SELECT EXISTS(SELECT 1 FROM template_likes WHERE userId = :userId AND templateIndex = :templateIndex)")
    suspend fun hasLiked(userId: String, templateIndex: Int): Boolean

    // 좋아요 기록 추가
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLike(like: TemplateLike)

    // 좋아요 기록 삭제 (좋아요 취소 시)
    @Query("DELETE FROM template_likes WHERE userId = :userId AND templateIndex = :templateIndex")
    suspend fun deleteLike(userId: String, templateIndex: Int)
}