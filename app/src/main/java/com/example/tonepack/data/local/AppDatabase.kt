package com.example.tonepack.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tonepack.data.local.dao.TemplateDao
import com.example.tonepack.data.local.dao.UserDao
import com.example.tonepack.data.local.entity.Template
import com.example.tonepack.data.local.entity.User
import com.example.tonepack.data.seed.SeedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 앱의 메인 데이터베이스 클래스
 * entities: 사용할 테이블(User, Template) 등록
 * version: 스키마 버전 (처음이므로 1)
 */
@Database(entities = [User::class, Template::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Dao 연결
    abstract fun userDao(): UserDao
    abstract fun templateDao(): TemplateDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // 싱글톤 패턴: 앱 전체에서 하나의 DB 인스턴스만 공유합니다.
        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tonepack_database"
                )
                    // DB가 처음 생성될 때 Seed 데이터를 넣기 위한 콜백 등록
                    .addCallback(AppDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                    INSTANCE = instance
                    instance
            }
        }
    }

    /**
     * 데이터베이스 초기화 콜백
     * 앱 설치 후 최초 실행 시 SeedData를 DB에 주입합니다.
     */
    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    // 1. 관리자 계정 주입 (SeedData.adminUser)
                    database.userDao().insertUser(SeedData.adminUser)

                    // 2. 초기 템플릿 20개 주입 (SeedData.templates)
                    database.templateDao().insertAll(SeedData.templates)
                }
            }
        }
    }
}