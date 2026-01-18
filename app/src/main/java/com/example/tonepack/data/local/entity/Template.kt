package com.example.tonepack.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Template 테이블: 템플릿 정보와 커뮤니티 글을 통합 관리합니다.
 */
@Entity(tableName = "templates",
    // FK 설정
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["authorId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class Template(
    @PrimaryKey(autoGenerate = true) //인덱스 번호 자동생성
    val index: Int = 0, // 인덱스 번호(PK)

    val authorId: String?,  // 글 작성자 아이디 (유저 테이블과 연결용)

    val title: String, // 글 제목
    val content: String, //실제 본문 내용

    val situation: String,         // 필터 1: 상황 (예: 출근, 업무, 퇴사, 실수보고)
    val target: String,            // 필터 2: 상대 (예: 상사, 동기, 업체, 후배)

    val likeCount: Int = 0,        // 추천 수
    val dislikeCount: Int = 0,     // 비추천 수
)