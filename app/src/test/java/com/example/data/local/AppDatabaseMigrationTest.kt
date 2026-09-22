package com.example.data.local

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class AppDatabaseMigrationTest {
    @Test
    fun `migration 3 to 4 preserves core data and creates central relationships`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val databaseName = "room-migration-3-4-test.db"
        context.getDatabasePath(databaseName).parentFile?.mkdirs()
        context.deleteDatabase(databaseName)
        val helper = FrameworkSQLiteOpenHelperFactory().create(
            SupportSQLiteOpenHelper.Configuration.builder(context)
                .name(databaseName)
                .callback(object : SupportSQLiteOpenHelper.Callback(3) {
                    override fun onCreate(db: SupportSQLiteDatabase) = createVersion3Core(db)
                    override fun onUpgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) = Unit
                })
                .build()
        )

        val db = helper.writableDatabase
        insertVersion3Data(db)
        AppDatabaseMigrations.MIGRATION_3_4.migrate(db)

        assertEquals(4, db.singleInt("SELECT COUNT(*) FROM subjects"))
        assertEquals("analogies", db.singleString("SELECT topic_id FROM questions WHERE id = 1"))
        assertEquals(1, db.singleInt("SELECT COUNT(*) FROM bookmarks WHERE question_id = 1"))
        assertEquals(1, db.singleInt("SELECT attempt_count FROM question_progress WHERE question_id = 1"))
        assertEquals(100, db.singleInt("SELECT CAST(mastery_score AS INTEGER) FROM question_progress WHERE question_id = 1"))
        assertEquals(1, db.singleInt("SELECT COUNT(*) FROM test_questions WHERE question_id = 1"))
        assertFalse(db.query("PRAGMA foreign_key_check").use { it.moveToFirst() })

        db.version = 4
        helper.close()
        val migratedDatabase = Room.databaseBuilder(context, AppDatabase::class.java, databaseName)
            .addMigrations(AppDatabaseMigrations.MIGRATION_3_4)
            .allowMainThreadQueries()
            .build()
        migratedDatabase.openHelper.writableDatabase
        migratedDatabase.close()
        context.deleteDatabase(databaseName)
    }

    private fun createVersion3Core(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE questions (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                question_text TEXT NOT NULL, option_a TEXT NOT NULL, option_b TEXT NOT NULL,
                option_c TEXT NOT NULL, option_d TEXT NOT NULL, correct_option INTEGER NOT NULL,
                explanation TEXT NOT NULL, subject TEXT NOT NULL, topic TEXT NOT NULL,
                subtopic TEXT NOT NULL, difficulty TEXT NOT NULL, year INTEGER,
                exam_date TEXT, shift TEXT, exam_name TEXT, is_bookmarked INTEGER NOT NULL
            )
        """.trimIndent())
        db.execSQL("""
            CREATE TABLE tests (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, title TEXT NOT NULL,
                test_type TEXT NOT NULL, subject TEXT, year INTEGER, exam_date TEXT, shift TEXT,
                total_questions INTEGER NOT NULL, duration_minutes INTEGER NOT NULL,
                total_marks REAL NOT NULL, positive_marks REAL NOT NULL,
                negative_marks REAL NOT NULL, created_at INTEGER NOT NULL
            )
        """.trimIndent())
        db.execSQL("""
            CREATE TABLE test_questions (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, test_id INTEGER NOT NULL,
                question_id INTEGER NOT NULL, section_order INTEGER NOT NULL, question_order INTEGER NOT NULL
            )
        """.trimIndent())
        db.execSQL("""
            CREATE TABLE attempts (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, test_id INTEGER,
                test_title TEXT NOT NULL, test_type TEXT NOT NULL, start_time INTEGER NOT NULL,
                end_time INTEGER NOT NULL, time_taken_seconds INTEGER NOT NULL,
                total_questions INTEGER NOT NULL, attempted_count INTEGER NOT NULL,
                correct_count INTEGER NOT NULL, wrong_count INTEGER NOT NULL,
                unattempted_count INTEGER NOT NULL, score REAL NOT NULL, max_score REAL NOT NULL,
                accuracy REAL NOT NULL, is_completed INTEGER NOT NULL
            )
        """.trimIndent())
        db.execSQL("""
            CREATE TABLE attempt_answers (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, attempt_id INTEGER NOT NULL,
                question_id INTEGER NOT NULL, selected_option INTEGER, correct_option INTEGER NOT NULL,
                is_correct INTEGER NOT NULL, is_marked_for_review INTEGER NOT NULL,
                time_spent_seconds INTEGER NOT NULL
            )
        """.trimIndent())
        db.execSQL("""
            CREATE TABLE daily_note_sets (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, type TEXT NOT NULL,
                set_number INTEGER NOT NULL, title TEXT NOT NULL, total_items INTEGER NOT NULL,
                is_active INTEGER NOT NULL, is_completed INTEGER NOT NULL, completed_at INTEGER
            )
        """.trimIndent())
        db.execSQL("CREATE UNIQUE INDEX index_daily_note_sets_type_set_number ON daily_note_sets(type, set_number)")
        db.execSQL("CREATE INDEX index_daily_note_sets_type_is_active ON daily_note_sets(type, is_active)")
        db.execSQL("""
            CREATE TABLE daily_note_items (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, set_id INTEGER NOT NULL,
                item_order INTEGER NOT NULL, word_or_title TEXT NOT NULL, phonetic TEXT NOT NULL,
                part_of_speech TEXT NOT NULL, meaning TEXT NOT NULL, synonym TEXT NOT NULL,
                antonym TEXT NOT NULL, example TEXT NOT NULL, explanation TEXT NOT NULL,
                fact_date TEXT NOT NULL, category TEXT NOT NULL,
                FOREIGN KEY(set_id) REFERENCES daily_note_sets(id) ON UPDATE NO ACTION ON DELETE CASCADE
            )
        """.trimIndent())
        db.execSQL("CREATE INDEX index_daily_note_items_set_id ON daily_note_items(set_id)")
        db.execSQL("""
            CREATE TABLE daily_note_progress (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, set_id INTEGER NOT NULL,
                item_id INTEGER NOT NULL, is_completed INTEGER NOT NULL, review_later INTEGER NOT NULL,
                updated_at INTEGER NOT NULL,
                FOREIGN KEY(set_id) REFERENCES daily_note_sets(id) ON UPDATE NO ACTION ON DELETE CASCADE,
                FOREIGN KEY(item_id) REFERENCES daily_note_items(id) ON UPDATE NO ACTION ON DELETE CASCADE
            )
        """.trimIndent())
        db.execSQL("CREATE INDEX index_daily_note_progress_set_id ON daily_note_progress(set_id)")
        db.execSQL("CREATE UNIQUE INDEX index_daily_note_progress_item_id ON daily_note_progress(item_id)")
    }

    private fun insertVersion3Data(db: SupportSQLiteDatabase) {
        db.execSQL("""
            INSERT INTO questions VALUES(
                1, 'House : Rent :: Capital : ?', 'Interest', 'Investment', 'Country', 'Money', 1,
                'Capital earns interest.', 'Reasoning', 'Analogy', '', 'Easy', NULL,
                NULL, NULL, 'Legacy seed', 1
            )
        """.trimIndent())
        db.execSQL("""
            INSERT INTO tests VALUES(1, 'Analogy Practice', 'TOPIC', 'Reasoning', NULL, NULL, NULL,
                1, 10, 2.0, 2.0, 0.5, 1000)
        """.trimIndent())
        db.execSQL("INSERT INTO test_questions VALUES(1, 1, 1, 1, 1)")
        db.execSQL("""
            INSERT INTO attempts VALUES(1, 1, 'Analogy Practice', 'TOPIC', 1000, 1100, 100,
                1, 1, 1, 0, 0, 2.0, 2.0, 100.0, 1)
        """.trimIndent())
        db.execSQL("INSERT INTO attempt_answers VALUES(1, 1, 1, 1, 1, 1, 0, 20)")
    }

    private fun SupportSQLiteDatabase.singleInt(sql: String): Int =
        query(sql).use { cursor -> cursor.moveToFirst(); cursor.getInt(0) }

    private fun SupportSQLiteDatabase.singleString(sql: String): String =
        query(sql).use { cursor -> cursor.moveToFirst(); cursor.getString(0) }
}
