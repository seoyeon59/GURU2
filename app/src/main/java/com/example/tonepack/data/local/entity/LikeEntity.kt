package com.example.tonepack.data.local.entity

import androidx.room.Entity

// 좋아요 중복 방지를 위한 테이블: 1인 1게시글 1좋아요 제한용
@Entity(
    tableName = "template_likes",
    primaryKeys = ["userId", "templateIndex"] // 두 값을 합쳐 고유하게 관리
)
data class TemplateLike(
    val userId: String,
    val templateIndex: Int
)