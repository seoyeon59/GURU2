package com.example.tonepack.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tonepack.data.local.dao.TemplateDao
import com.example.tonepack.data.local.dao.UserDao
import com.example.tonepack.data.local.dao.TemplateLikeDao
import com.example.tonepack.data.local.entity.Template
import com.example.tonepack.data.local.entity.User
import com.example.tonepack.data.local.entity.TemplateLike
import com.example.tonepack.data.seed.SeedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// User와 Template 테이블을 포함하는 Room 데이터베이스 설정
@Database(
    entities = [User::class, Template::class, TemplateLike::class],
    version = 2, // seed data 수정 후 버전 업데이트
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun templateDao(): TemplateDao
    abstract fun templateLikeDao(): TemplateLikeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // 싱글톤 패턴으로 데이터베이스 인스턴스 반환
        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tonepack_database"
                )
                    .addCallback(AppDatabaseCallback(scope)) // DB 생성 시 콜백 등록
                    .fallbackToDestructiveMigration()      // 버전 변경 시 기존 데이터 삭제 후 재생성
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }

    // DB가 처음 생성될 때 실행되는 콜백 클래스
    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                // 백그라운드 스레드에서 초기 데이터(SeedData) 주입
                scope.launch(Dispatchers.IO) {
                    try {
                        // 1. 초기 유저 데이터 2명 주입 (gurumi, guru2 등)
                        database.userDao().insertUser(SeedData.user1)
                        database.userDao().insertUser(SeedData.user2)

                        // 2. 초기 템플릿 데이터 리스트 주입
                        // 제목이 비어있지 않은 유효한 데이터만 필터링
                        val validTemplates = SeedData.templates.filter { it.title.isNotBlank() }

                        if (validTemplates.isNotEmpty()) {
                            database.templateDao().insertAll(validTemplates)
                        }

                    } catch (e: Exception) {
                        e.printStackTrace() // 데이터 주입 중 오류 발생 시 로그 출력
                    }
                }
            }
        }
    }
}