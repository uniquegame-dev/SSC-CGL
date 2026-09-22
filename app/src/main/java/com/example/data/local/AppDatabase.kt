package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.launch
import com.example.data.local.dao.AttemptAnswerDao
import com.example.data.local.dao.AttemptDao
import com.example.data.local.dao.BookmarkDao
import com.example.data.local.dao.DailyNoteDao
import com.example.data.local.dao.HierarchyDao
import com.example.data.local.dao.QuestionDao
import com.example.data.local.dao.QuestionProgressDao
import com.example.data.local.dao.TestDao
import com.example.data.local.dao.TestQuestionDao
import com.example.data.local.entities.AttemptAnswerEntity
import com.example.data.local.entities.AttemptEntity
import com.example.data.local.entities.BookmarkEntity
import com.example.data.local.entities.DailyNoteItemEntity
import com.example.data.local.entities.DailyNoteProgressEntity
import com.example.data.local.entities.DailyNoteSetEntity
import com.example.data.local.entities.ExamEntity
import com.example.data.local.entities.QuestionEntity
import com.example.data.local.entities.QuestionProgressEntity
import com.example.data.local.entities.SubjectEntity
import com.example.data.local.entities.SubtopicEntity
import com.example.data.local.entities.TestEntity
import com.example.data.local.entities.TestQuestionEntity
import com.example.data.local.entities.TopicEntity

/**
 * Single Central Room Database for the SSC CGL Offline Practice App.
 *
 * Contains Tables:
 * Exam -> Subject -> Topic -> Subtopic -> Question is the canonical content hierarchy.
 * Tests, attempts, bookmarks, progress, weak areas, and revision all reference QuestionEntity.
 * Daily Notes remain in the same offline database but are intentionally independent.
 */
@Database(
    entities = [
        ExamEntity::class,
        SubjectEntity::class,
        TopicEntity::class,
        SubtopicEntity::class,
        QuestionEntity::class,
        TestEntity::class,
        TestQuestionEntity::class,
        AttemptEntity::class,
        AttemptAnswerEntity::class,
        BookmarkEntity::class,
        QuestionProgressEntity::class,
        DailyNoteSetEntity::class,
        DailyNoteItemEntity::class,
        DailyNoteProgressEntity::class
    ],
    version = 4,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun hierarchyDao(): HierarchyDao
    abstract fun questionDao(): QuestionDao
    abstract fun testDao(): TestDao
    abstract fun testQuestionDao(): TestQuestionDao
    abstract fun attemptDao(): AttemptDao
    abstract fun attemptAnswerDao(): AttemptAnswerDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun questionProgressDao(): QuestionProgressDao
    abstract fun dailyNoteDao(): DailyNoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private const val DATABASE_NAME = "ssc_cgl_offline.db"

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .addMigrations(AppDatabaseMigrations.MIGRATION_3_4)
                    .build()
                INSTANCE = instance
                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                    try {
                        SscLocalRepository(instance).seedFoundationIfNeeded()
                    } catch (_: Exception) {}
                }
                instance
            }
        }
    }
}
