package com.example.tonepack.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tonepack.data.local.dao.TemplateDao
import com.example.tonepack.data.local.dao.TemplateLikeDao
import com.example.tonepack.data.local.dao.UserDao
import com.example.tonepack.data.local.entity.Template
import com.example.tonepack.data.local.entity.TemplateLike
import com.example.tonepack.data.local.entity.User
import com.example.tonepack.data.seed.SeedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [User::class, Template::class, TemplateLike::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun templateDao(): TemplateDao
    abstract fun templateLikeDao(): TemplateLikeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tonepack_database"
                )
                    // 버전 바뀌면 기존 DB 삭제 후 재생성 (개발 단계에서 편함)
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                // 여기서 seed주입하기(수민_추가 코드 : 백엔드 개발자 확인 필요)
                scope.launch(Dispatchers.IO) {
                    seedDatabase(instance)
                }

                instance
            }
        }

        private suspend fun seedDatabase(database: AppDatabase) {
            try {
                // 유저 2명 주입
                database.userDao().insertUser(SeedData.user1)
                database.userDao().insertUser(SeedData.user2)

                // 템플릿 주입
                val validTemplates = SeedData.templates.filter { it.title.isNotBlank() }
                if (validTemplates.isNotEmpty()) {
                    database.templateDao().insertAll(validTemplates)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
