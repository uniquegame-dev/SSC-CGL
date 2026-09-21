package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.data.local.dao.AttemptAnswerDao
import com.example.data.local.dao.AttemptDao
import com.example.data.local.dao.DailyNoteDao
import com.example.data.local.dao.QuestionDao
import com.example.data.local.dao.TestDao
import com.example.data.local.dao.TestQuestionDao
import com.example.data.local.entities.AttemptAnswerEntity
import com.example.data.local.entities.AttemptEntity
import com.example.data.local.entities.DailyNoteItemEntity
import com.example.data.local.entities.DailyNoteProgressEntity
import com.example.data.local.entities.DailyNoteSetEntity
import com.example.data.local.entities.QuestionEntity
import com.example.data.local.entities.TestEntity
import com.example.data.local.entities.TestQuestionEntity

/**
 * Single Central Room Database for the SSC CGL Offline Practice App.
 *
 * Contains Tables:
 * 1. questions           - QuestionEntity
 * 2. tests               - TestEntity
 * 3. test_questions      - TestQuestionEntity
 * 4. attempts            - AttemptEntity
 * 5. attempt_answers     - AttemptAnswerEntity
 * 6. daily_note_sets     - DailyNoteSetEntity
 * 7. daily_note_items    - DailyNoteItemEntity
 * 8. daily_note_progress - DailyNoteProgressEntity
 */
@Database(
    entities = [
        QuestionEntity::class,
        TestEntity::class,
        TestQuestionEntity::class,
        AttemptEntity::class,
        AttemptAnswerEntity::class,
        DailyNoteSetEntity::class,
        DailyNoteItemEntity::class,
        DailyNoteProgressEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun questionDao(): QuestionDao
    abstract fun testDao(): TestDao
    abstract fun testQuestionDao(): TestQuestionDao
    abstract fun attemptDao(): AttemptDao
    abstract fun attemptAnswerDao(): AttemptAnswerDao
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
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                    try {
                        SscLocalRepository(instance).seedInitialQuestionsIfEmpty()
                    } catch (_: Exception) {}
                }
                instance
            }
        }
    }
}
