package com.example.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "exams",
    indices = [Index(value = ["code"], unique = true, name = "index_exams_code")]
)
data class ExamEntity(
    @PrimaryKey val id: String,
    val code: String,
    val name: String,
    val description: String = "",
    @ColumnInfo(name = "is_active") val isActive: Boolean = true
)

@Entity(
    tableName = "subjects",
    foreignKeys = [ForeignKey(
        entity = ExamEntity::class,
        parentColumns = ["id"],
        childColumns = ["exam_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [
        Index(value = ["exam_id"], name = "index_subjects_exam_id"),
        Index(value = ["exam_id", "name"], unique = true, name = "index_subjects_exam_id_name")
    ]
)
data class SubjectEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "exam_id") val examId: String,
    val name: String,
    val description: String = "",
    @ColumnInfo(name = "display_order") val displayOrder: Int = 0,
    @ColumnInfo(name = "is_active") val isActive: Boolean = true
)

@Entity(
    tableName = "topics",
    foreignKeys = [ForeignKey(
        entity = SubjectEntity::class,
        parentColumns = ["id"],
        childColumns = ["subject_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [
        Index(value = ["subject_id"], name = "index_topics_subject_id"),
        Index(value = ["subject_id", "name"], unique = true, name = "index_topics_subject_id_name")
    ]
)
data class TopicEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "subject_id") val subjectId: String,
    val name: String,
    val description: String = "",
    @ColumnInfo(name = "display_order") val displayOrder: Int = 0,
    @ColumnInfo(name = "is_active") val isActive: Boolean = true
)

@Entity(
    tableName = "subtopics",
    foreignKeys = [ForeignKey(
        entity = TopicEntity::class,
        parentColumns = ["id"],
        childColumns = ["topic_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [
        Index(value = ["topic_id"], name = "index_subtopics_topic_id"),
        Index(value = ["topic_id", "name"], unique = true, name = "index_subtopics_topic_id_name")
    ]
)
data class SubtopicEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "topic_id") val topicId: String,
    val name: String,
    val description: String = "",
    @ColumnInfo(name = "display_order") val displayOrder: Int = 0,
    @ColumnInfo(name = "is_active") val isActive: Boolean = true
)

/** A question is stored once here and reused by tests, attempts, bookmarks, and progress. */
@Entity(
    tableName = "questions",
    foreignKeys = [
        ForeignKey(
            entity = TopicEntity::class,
            parentColumns = ["id"],
            childColumns = ["topic_id"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = SubtopicEntity::class,
            parentColumns = ["id"],
            childColumns = ["subtopic_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [
        Index(value = ["content_key"], unique = true, name = "index_questions_content_key"),
        Index(value = ["topic_id"], name = "index_questions_topic_id"),
        Index(value = ["subtopic_id"], name = "index_questions_subtopic_id"),
        Index(value = ["source_type"], name = "index_questions_source_type"),
        Index(value = ["year", "shift"], name = "index_questions_year_shift"),
        Index(value = ["difficulty"], name = "index_questions_difficulty")
    ]
)
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "content_key") val contentKey: String,
    @ColumnInfo(name = "topic_id") val topicId: String,
    @ColumnInfo(name = "subtopic_id") val subtopicId: String? = null,
    @ColumnInfo(name = "question_text") val questionText: String,
    @ColumnInfo(name = "option_a") val optionA: String,
    @ColumnInfo(name = "option_b") val optionB: String,
    @ColumnInfo(name = "option_c") val optionC: String,
    @ColumnInfo(name = "option_d") val optionD: String,
    @ColumnInfo(name = "correct_option") val correctOption: Int,
    val explanation: String = "",
    val difficulty: String = "MODERATE",
    @ColumnInfo(name = "source_type") val sourceType: String = "PRACTICE",
    @ColumnInfo(name = "source_name") val sourceName: String? = null,
    val year: Int? = null,
    @ColumnInfo(name = "exam_date") val examDate: String? = null,
    val shift: String? = null,
    val language: String = "ENGLISH",
    @ColumnInfo(name = "is_active") val isActive: Boolean = true,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "tests",
    foreignKeys = [
        ForeignKey(entity = ExamEntity::class, parentColumns = ["id"], childColumns = ["exam_id"], onDelete = ForeignKey.RESTRICT),
        ForeignKey(entity = SubjectEntity::class, parentColumns = ["id"], childColumns = ["subject_id"], onDelete = ForeignKey.SET_NULL),
        ForeignKey(entity = TopicEntity::class, parentColumns = ["id"], childColumns = ["topic_id"], onDelete = ForeignKey.SET_NULL),
        ForeignKey(entity = SubtopicEntity::class, parentColumns = ["id"], childColumns = ["subtopic_id"], onDelete = ForeignKey.SET_NULL)
    ],
    indices = [
        Index(value = ["exam_id"], name = "index_tests_exam_id"),
        Index(value = ["subject_id"], name = "index_tests_subject_id"),
        Index(value = ["topic_id"], name = "index_tests_topic_id"),
        Index(value = ["subtopic_id"], name = "index_tests_subtopic_id"),
        Index(value = ["test_type"], name = "index_tests_test_type"),
        Index(value = ["year", "shift"], name = "index_tests_year_shift")
    ]
)
data class TestEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "exam_id") val examId: String = "ssc_cgl",
    @ColumnInfo(name = "subject_id") val subjectId: String? = null,
    @ColumnInfo(name = "topic_id") val topicId: String? = null,
    @ColumnInfo(name = "subtopic_id") val subtopicId: String? = null,
    val title: String,
    @ColumnInfo(name = "test_type") val testType: String,
    val year: Int? = null,
    @ColumnInfo(name = "exam_date") val examDate: String? = null,
    val shift: String? = null,
    @ColumnInfo(name = "total_questions") val totalQuestions: Int = 100,
    @ColumnInfo(name = "duration_minutes") val durationMinutes: Int = 60,
    @ColumnInfo(name = "total_marks") val totalMarks: Double = 200.0,
    @ColumnInfo(name = "positive_marks") val positiveMarks: Double = 2.0,
    @ColumnInfo(name = "negative_marks") val negativeMarks: Double = 0.5,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "test_questions",
    primaryKeys = ["test_id", "question_id"],
    foreignKeys = [
        ForeignKey(entity = TestEntity::class, parentColumns = ["id"], childColumns = ["test_id"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = QuestionEntity::class, parentColumns = ["id"], childColumns = ["question_id"], onDelete = ForeignKey.RESTRICT)
    ],
    indices = [
        Index(value = ["question_id"], name = "index_test_questions_question_id"),
        Index(value = ["test_id", "question_order"], unique = true, name = "index_test_questions_test_id_question_order")
    ]
)
data class TestQuestionEntity(
    @ColumnInfo(name = "test_id") val testId: Long,
    @ColumnInfo(name = "question_id") val questionId: Long,
    @ColumnInfo(name = "section_order") val sectionOrder: Int = 1,
    @ColumnInfo(name = "question_order") val questionOrder: Int = 1,
    @ColumnInfo(name = "positive_marks") val positiveMarks: Double? = null,
    @ColumnInfo(name = "negative_marks") val negativeMarks: Double? = null
)

@Entity(
    tableName = "attempts",
    foreignKeys = [
        ForeignKey(entity = TestEntity::class, parentColumns = ["id"], childColumns = ["test_id"], onDelete = ForeignKey.SET_NULL),
        ForeignKey(entity = SubjectEntity::class, parentColumns = ["id"], childColumns = ["subject_id"], onDelete = ForeignKey.SET_NULL),
        ForeignKey(entity = TopicEntity::class, parentColumns = ["id"], childColumns = ["topic_id"], onDelete = ForeignKey.SET_NULL),
        ForeignKey(entity = SubtopicEntity::class, parentColumns = ["id"], childColumns = ["subtopic_id"], onDelete = ForeignKey.SET_NULL)
    ],
    indices = [
        Index(value = ["test_id"], name = "index_attempts_test_id"),
        Index(value = ["subject_id"], name = "index_attempts_subject_id"),
        Index(value = ["topic_id"], name = "index_attempts_topic_id"),
        Index(value = ["subtopic_id"], name = "index_attempts_subtopic_id"),
        Index(value = ["start_time"], name = "index_attempts_start_time")
    ]
)
data class AttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "test_id") val testId: Long? = null,
    @ColumnInfo(name = "subject_id") val subjectId: String? = null,
    @ColumnInfo(name = "topic_id") val topicId: String? = null,
    @ColumnInfo(name = "subtopic_id") val subtopicId: String? = null,
    @ColumnInfo(name = "test_title") val testTitle: String,
    @ColumnInfo(name = "attempt_type") val attemptType: String,
    @ColumnInfo(name = "start_time") val startTime: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "end_time") val endTime: Long? = null,
    @ColumnInfo(name = "time_taken_seconds") val timeTakenSeconds: Long = 0,
    @ColumnInfo(name = "total_questions") val totalQuestions: Int = 0,
    @ColumnInfo(name = "attempted_count") val attemptedCount: Int = 0,
    @ColumnInfo(name = "correct_count") val correctCount: Int = 0,
    @ColumnInfo(name = "wrong_count") val wrongCount: Int = 0,
    @ColumnInfo(name = "unattempted_count") val unattemptedCount: Int = 0,
    val score: Double = 0.0,
    @ColumnInfo(name = "max_score") val maxScore: Double = 0.0,
    val accuracy: Double = 0.0,
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean = false
)

@Entity(
    tableName = "attempt_answers",
    primaryKeys = ["attempt_id", "question_id"],
    foreignKeys = [
        ForeignKey(entity = AttemptEntity::class, parentColumns = ["id"], childColumns = ["attempt_id"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = QuestionEntity::class, parentColumns = ["id"], childColumns = ["question_id"], onDelete = ForeignKey.RESTRICT)
    ],
    indices = [Index(value = ["question_id"], name = "index_attempt_answers_question_id")]
)
data class AttemptAnswerEntity(
    @ColumnInfo(name = "attempt_id") val attemptId: Long,
    @ColumnInfo(name = "question_id") val questionId: Long,
    @ColumnInfo(name = "answer_order") val answerOrder: Int = 0,
    @ColumnInfo(name = "selected_option") val selectedOption: Int? = null,
    @ColumnInfo(name = "correct_option") val correctOption: Int,
    @ColumnInfo(name = "is_correct") val isCorrect: Boolean = false,
    @ColumnInfo(name = "is_marked_for_review") val isMarkedForReview: Boolean = false,
    @ColumnInfo(name = "time_spent_seconds") val timeSpentSeconds: Long = 0,
    @ColumnInfo(name = "awarded_marks") val awardedMarks: Double = 0.0
)

@Entity(
    tableName = "bookmarks",
    foreignKeys = [ForeignKey(
        entity = QuestionEntity::class,
        parentColumns = ["id"],
        childColumns = ["question_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class BookmarkEntity(
    @PrimaryKey @ColumnInfo(name = "question_id") val questionId: Long,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "question_progress",
    foreignKeys = [ForeignKey(
        entity = QuestionEntity::class,
        parentColumns = ["id"],
        childColumns = ["question_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [
        Index(value = ["needs_revision", "mastery_score"], name = "index_question_progress_revision_mastery"),
        Index(value = ["last_attempted_at"], name = "index_question_progress_last_attempted_at")
    ]
)
data class QuestionProgressEntity(
    @PrimaryKey @ColumnInfo(name = "question_id") val questionId: Long,
    @ColumnInfo(name = "attempt_count") val attemptCount: Int = 0,
    @ColumnInfo(name = "correct_count") val correctCount: Int = 0,
    @ColumnInfo(name = "wrong_count") val wrongCount: Int = 0,
    @ColumnInfo(name = "skipped_count") val skippedCount: Int = 0,
    @ColumnInfo(name = "total_time_seconds") val totalTimeSeconds: Long = 0,
    @ColumnInfo(name = "last_attempted_at") val lastAttemptedAt: Long? = null,
    @ColumnInfo(name = "mastery_score") val masteryScore: Double = 0.0,
    @ColumnInfo(name = "needs_revision") val needsRevision: Boolean = false
)

// Daily Notes remain independent from the central question bank.
@Entity(
    tableName = "daily_note_sets",
    indices = [
        Index(value = ["type", "set_number"], unique = true),
        Index(value = ["type", "is_active"])
    ]
)
data class DailyNoteSetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,
    @ColumnInfo(name = "set_number") val setNumber: Int,
    val title: String = "Set $setNumber",
    @ColumnInfo(name = "total_items") val totalItems: Int = 10,
    @ColumnInfo(name = "is_active") val isActive: Boolean = false,
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean = false,
    @ColumnInfo(name = "completed_at") val completedAt: Long? = null
)

typealias DailyNoteSet = DailyNoteSetEntity

@Entity(
    tableName = "daily_note_items",
    foreignKeys = [ForeignKey(
        entity = DailyNoteSetEntity::class,
        parentColumns = ["id"],
        childColumns = ["set_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["set_id"])]
)
data class DailyNoteItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "set_id") val setId: Long,
    @ColumnInfo(name = "item_order") val itemOrder: Int = 1,
    @ColumnInfo(name = "word_or_title") val wordOrTitle: String,
    val phonetic: String = "",
    @ColumnInfo(name = "part_of_speech") val partOfSpeech: String = "",
    val meaning: String = "",
    val synonym: String = "",
    val antonym: String = "",
    val example: String = "",
    val explanation: String = "",
    @ColumnInfo(name = "fact_date") val factDate: String = "",
    val category: String = ""
)

typealias DailyNoteItem = DailyNoteItemEntity

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
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "set_id") val setId: Long,
    @ColumnInfo(name = "item_id") val itemId: Long,
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean = false,
    @ColumnInfo(name = "review_later") val reviewLater: Boolean = false,
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis()
)

typealias DailyNoteProgress = DailyNoteProgressEntity
