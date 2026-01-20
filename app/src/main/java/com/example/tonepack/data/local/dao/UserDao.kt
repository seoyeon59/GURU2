package com.example.tonepack.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tonepack.data.local.entity.User


/**
 * User 테이블에 접근하여 데이터를 조작하는 인터페이스
 */
@Dao
interface UserDao {

    /**
     * 회원가입
     * @OnConflictStrategy.IGNORE: 만약 이미 존재하는 ID로 가입을 시도하면 무시합니다.
     * 리턴값: 저장된 행의 rowId (실패 시 -1)
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User): Long

    /**
     * 로그인 체크
     * 결과가 있으면 유저 객체를 반환하고, 없으면 null을 반환합니다.
     */
    @Query("SELECT * FROM user WHERE id = :userId AND password = :userPw")
    suspend fun loginCheck(userId: String, userPw: String): User?

    /**
     * ID 중복 확인 및 유저 정보 조회
     * 회원가입 시 아이디 중복 체크 버튼 등에 활용할 수 있습니다.
     */
    @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun findById(userId: String): User?

    /**
     * 전체 유저 수 확인: DB가 정상적으로 연결되었는지 테스트
     */
    @Query("SELECT COUNT(*) FROM user")
    suspend fun getUserCount(): Int
}