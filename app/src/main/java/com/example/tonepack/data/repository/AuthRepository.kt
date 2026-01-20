package com.example.tonepack.data.repository

import com.example.tonepack.data.local.dao.UserDao
import com.example.tonepack.data.local.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


// 인증(로그인, 회원가입) 관련 데이터 로직을 담당하는 레포지토리
class AuthRepository(private val userDao: UserDao) {

    /**
     * 로그인 로직: 아이디와 비밀번호가 일치하는지 확인
     * 결과가 있으면 User 객체 반환, 없으면 null 반환
     */
    suspend fun login(id: String, pw: String): Result<User> = withContext(Dispatchers.IO) {
        val user = userDao.loginCheck(id, pw)
        if (user != null) {
            Result.success(user) // 성공 시 유저 데이터 담아서 보냄
        } else {
            // 실패 시 에러 이유를 담아서 보냄
            Result.failure(Exception("아이디 또는 비밀번호가 일치하지 않습니다."))
        }
    }

    /**
     * 회원가입 로직: 새로운 유저를 DB에 등록
     * @return 성공 시 true, 아이디 중복 등으로 실패 시 false
     */
    suspend fun register(user: User): Boolean = withContext(Dispatchers.IO) {
        val result = userDao.insertUser(user)
        result != -1L // Insert 성공 시 rowId가 양수로 반환됨
    }

     //아이디 중복 체크
     suspend fun isIdDuplicated(id: String): Boolean = withContext(Dispatchers.IO) {
        userDao.findById(id) != null
    }
}