package com.example.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 1. Questions Table
 * Stores individual practice and previous year questions.
 */
@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "question_text")
    val questionText: String,

    @ColumnInfo(name = "option_a")
    val optionA: String,

    @ColumnInfo(name = "option_b")
    val optionB: String,

    @ColumnInfo(name = "option_c")
    val optionC: String,

    @ColumnInfo(name = "option_d")
    val optionD: String,

    @ColumnInfo(name = "correct_option")
    val correctOption: Int, // 1 for A, 2 for B, 3 for C, 4 for D

    @ColumnInfo(name = "explanation")
    val explanation: String = "",

    @ColumnInfo(name = "subject")
    val subject: String, // e.g. "Quantitative Aptitude", "General Intelligence & Reasoning", etc.

    @ColumnInfo(name = "topic")
    val topic: String, // e.g. "Percentage", "Analogy", "Modern History", etc.

    @ColumnInfo(name = "subtopic")
    val subtopic: String = "",

    @ColumnInfo(name = "difficulty")
    val difficulty: String = "Moderate", // Easy, Moderate, Hard

    @ColumnInfo(name = "year")
    val year: Int? = null, // e.g. 2025, 2024, 2023 (or null for general practice)

    @ColumnInfo(name = "exam_date")
    val examDate: String? = null, // e.g. "09 Sep 2024"

    @ColumnInfo(name = "shift")
    val shift: String? = null, // e.g. "Shift 1", "Shift 2", "Shift 3"

    @ColumnInfo(name = "exam_name")
    val examName: String? = null, // e.g. "SSC CGL 2024 Tier I"

    @ColumnInfo(name = "is_bookmarked")
    val isBookmarked: Boolean = false
)

/**
 * 2. Tests Table
 * Stores test blueprints and metadata (PYQ Papers, Mock Tests, Custom Tests, Sectional Tests).
 */
@Entity(tableName = "tests")
data class TestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "title")
    val title: String, // e.g. "SSC CGL 2024 - 09 Sep Shift 1", "All India Live Mock 01"

    @ColumnInfo(name = "test_type")
    val testType: String, // "PYQ", "MOCK", "CUSTOM", "SECTIONAL", "TOPIC"

    @ColumnInfo(name = "subject")
    val subject: String? = null, // null for full tests, or specific subject for sectional tests

    @ColumnInfo(name = "year")
    val year: Int? = null, // e.g. 2024

    @ColumnInfo(name = "exam_date")
    val examDate: String? = null, // e.g. "09 Sep 2024"

    @ColumnInfo(name = "shift")
    val shift: String? = null, // e.g. "Shift 1"

    @ColumnInfo(name = "total_questions")
    val totalQuestions: Int = 100,

    @ColumnInfo(name = "duration_minutes")
    val durationMinutes: Int = 60,

    @ColumnInfo(name = "total_marks")
    val totalMarks: Double = 200.0,

    @ColumnInfo(name = "positive_marks")
    val positiveMarks: Double = 2.0,

    @ColumnInfo(name = "negative_marks")
    val negativeMarks: Double = 0.50,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * 3. TestQuestions Table
 * Maps questions to specific tests in ordered sequence and section structure.
 */
@Entity(
    tableName = "test_questions",
    foreignKeys = [
        ForeignKey(
            entity = TestEntity::class,
            parentColumns = ["id"],
            childColumns = ["test_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = QuestionEntity::class,
            parentColumns = ["id"],
            childColumns = ["question_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["test_id"]),
        Index(value = ["question_id"])
    ]
)
data class TestQuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "test_id")
    val testId: Long,

    @ColumnInfo(name = "question_id")
    val questionId: Long,

    @ColumnInfo(name = "section_order")
    val sectionOrder: Int = 1, // 1: Reasoning, 2: GA, 3: Quant, 4: English

    @ColumnInfo(name = "question_order")
    val questionOrder: Int = 1 // 1..100
)

/**
 * 4. Attempts Table
 * Stores overall results and summary statistics of a user's test attempt.
 */
@Entity(
    tableName = "attempts",
    foreignKeys = [
        ForeignKey(
            entity = TestEntity::class,
            parentColumns = ["id"],
            childColumns = ["test_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["test_id"])
    ]
)
data class AttemptEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "test_id")
    val testId: Long? = null,

    @ColumnInfo(name = "test_title")
    val testTitle: String,

    @ColumnInfo(name = "test_type")
    val testType: String, // "PYQ", "MOCK", "CUSTOM", "PRACTICE"

    @ColumnInfo(name = "start_time")
    val startTime: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "end_time")
    val endTime: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "time_taken_seconds")
    val timeTakenSeconds: Long = 0,

    @ColumnInfo(name = "total_questions")
    val totalQuestions: Int = 0,

    @ColumnInfo(name = "attempted_count")
    val attemptedCount: Int = 0,

    @ColumnInfo(name = "correct_count")
    val correctCount: Int = 0,

    @ColumnInfo(name = "wrong_count")
    val wrongCount: Int = 0,

    @ColumnInfo(name = "unattempted_count")
    val unattemptedCount: Int = 0,

    @ColumnInfo(name = "score")
    val score: Double = 0.0,

    @ColumnInfo(name = "max_score")
    val maxScore: Double = 0.0,

    @ColumnInfo(name = "accuracy")
    val accuracy: Double = 0.0,

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = true
)

/**
 * 5. AttemptAnswers Table
 * Stores the detailed user response for each question in a test attempt.
 */
@Entity(
    tableName = "attempt_answers",
    foreignKeys = [
        ForeignKey(
            entity = AttemptEntity::class,
            parentColumns = ["id"],
            childColumns = ["attempt_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = QuestionEntity::class,
            parentColumns = ["id"],
            childColumns = ["question_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["attempt_id"]),
        Index(value = ["question_id"])
    ]
)
data class AttemptAnswerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "attempt_id")
    val attemptId: Long,

    @ColumnInfo(name = "question_id")
    val questionId: Long,

    @ColumnInfo(name = "selected_option")
    val selectedOption: Int? = null, // 1 for A, 2 for B, 3 for C, 4 for D, or null if skipped

    @ColumnInfo(name = "correct_option")
    val correctOption: Int,

    @ColumnInfo(name = "is_correct")
    val isCorrect: Boolean = false,

    @ColumnInfo(name = "is_marked_for_review")
    val isMarkedForReview: Boolean = false,

    @ColumnInfo(name = "time_spent_seconds")
    val timeSpentSeconds: Long = 0
)

/**
 * 6. Daily Note Sets Table
 * Represents a study set of daily notes (e.g., VOCAB, IDIOM, CURRENT_AFFAIRS) with active status and progress.
 */
@Entity(
    tableName = "daily_note_sets",
    indices = [
        Index(value = ["type", "set_number"], unique = true),
        Index(value = ["type", "is_active"])
    ]
)
data class DailyNoteSetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "type")
    val type: String, // "VOCAB", "IDIOM", "CURRENT_AFFAIRS"

    @ColumnInfo(name = "set_number")
    val setNumber: Int, // 1, 2, 3...

    @ColumnInfo(name = "title")
    val title: String = "Set $setNumber",

    @ColumnInfo(name = "total_items")
    val totalItems: Int = 10,

    @ColumnInfo(name = "is_active")
    val isActive: Boolean = false,

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,

    @ColumnInfo(name = "completed_at")
    val completedAt: Long? = null
)

// Alias for convenience
typealias DailyNoteSet = DailyNoteSetEntity

/**
 * 7. Daily Note Items Table
 * Represents an individual entry (Vocabulary word, Idiom/Phrase, or Current Affairs update).
 */
@Entity(
    tableName = "daily_note_items",
    foreignKeys = [
        ForeignKey(
            entity = DailyNoteSetEntity::class,
            parentColumns = ["id"],
            childColumns = ["set_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["set_id"])
    ]
)
data class DailyNoteItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "set_id")
    val setId: Long,

    @ColumnInfo(name = "item_order")
    val itemOrder: Int = 1,

    @ColumnInfo(name = "word_or_title")
    val wordOrTitle: String,

    @ColumnInfo(name = "phonetic")
    val phonetic: String = "",

    @ColumnInfo(name = "part_of_speech")
    val partOfSpeech: String = "",

    @ColumnInfo(name = "meaning")
    val meaning: String = "",

    @ColumnInfo(name = "synonym")
    val synonym: String = "",

    @ColumnInfo(name = "antonym")
    val antonym: String = "",

    @ColumnInfo(name = "example")
    val example: String = "",

    @ColumnInfo(name = "explanation")
    val explanation: String = "",

    @ColumnInfo(name = "fact_date")
    val factDate: String = "",

    @ColumnInfo(name = "category")
    val category: String = ""
)

// Alias for convenience
typealias DailyNoteItem = DailyNoteItemEntity

/**
 * 8. Daily Note Progress Table
 * Tracks completion and review status for individual items.
 */
@Entity(
    tableName = "daily_note_progress",
    foreignKeys = [
        ForeignKey(
            entity = DailyNoteSetEntity::class,
            parentColumns = ["id"],
            childColumns = ["set_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DailyNoteItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["item_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["set_id"]),
        Index(value = ["item_id"], unique = true)
    ]
)
data class DailyNoteProgressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "set_id")
    val setId: Long,

    @ColumnInfo(name = "item_id")
    val itemId: Long,

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,

    @ColumnInfo(name = "review_later")
    val reviewLater: Boolean = false,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
)

// Alias for convenience
typealias DailyNoteProgress = DailyNoteProgressEntity

