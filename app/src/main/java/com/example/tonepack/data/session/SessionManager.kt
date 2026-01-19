package com.example.tonepack.data.session

import android.content.Context
import android.content.SharedPreferences

/**
 * 로그인 세션 관리: 로그인한 유저의 ID를 저장하고 관리합니다.
 */
class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("tonepack_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USER_ID = "logged_in_user_id"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    // 로그인 정보 저장
    fun saveLoginSession(userId: String) {
        prefs.edit().apply {
            putString(KEY_USER_ID, userId)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    // 로그인 여부 확인
    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    // 저장된 유저 아이디 가져오기
    fun getUserId(): String? = prefs.getString(KEY_USER_ID, null)

    // 로그아웃 (데이터 초기화)
    fun logout() {
        prefs.edit().clear().apply()
    }
}