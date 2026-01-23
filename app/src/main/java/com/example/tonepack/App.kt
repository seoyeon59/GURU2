package com.example.tonepack

import android.app.Application
import com.example.tonepack.data.local.AppDatabase
import com.example.tonepack.data.repository.AuthRepository
import com.example.tonepack.data.repository.TemplateRepository
import com.example.tonepack.data.session.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/**
 * Application 클래스: 앱이 실행될 때 가장 먼저 생성됩니다.
 * DB, Repository, SessionManager를 전역에서 하나씩만 존재하도록(싱글톤) 관리합니다.
 */
class App : Application() {

    // 앱의 생명주기와 함께하는 코루틴 스코프 (DB 초기화 등 백그라운드 작업용)
    private val applicationScope = CoroutineScope(SupervisorJob())

    // 데이터베이스 인스턴스 (lazy: 실제 필요할 때 딱 한 번 생성)
    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }

    // 레포지토리 인스턴스 (DAO를 주입받아 생성)
    val authRepository by lazy { AuthRepository(database.userDao()) }
    val templateRepository by lazy {
        TemplateRepository(database.templateDao(), database.templateLikeDao())
    }

    // 세션 매니저 (로그인 상태 및 유저 ID 저장용)
    val sessionManager by lazy { SessionManager(this) }

    override fun onCreate() {
        super.onCreate()
        // 앱 실행 시 필요한 초기화 로직이 있다면 여기에 추가
    }
}