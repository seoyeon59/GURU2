package com.example.tonepack.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * User 테이블: 사용자의 ID와 비밀번호를 관리합니다.
 */
@Entity(tableName = "user")
data class User(
    @PrimaryKey
    val id: String, // 아이디 (PK)
    val password: String // 비밀번호
)