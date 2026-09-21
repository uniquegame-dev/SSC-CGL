package com.example.data.local

import android.content.Context
import com.example.data.local.entities.AttemptAnswerEntity
import com.example.data.local.entities.AttemptEntity
import com.example.data.local.entities.DailyNoteItemEntity
import com.example.data.local.entities.DailyNoteProgressEntity
import com.example.data.local.entities.DailyNoteSetEntity
import com.example.data.local.entities.QuestionEntity
import com.example.data.local.entities.TestEntity
import com.example.data.local.entities.TestQuestionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Clean, beginner-friendly repository for accessing and managing local Room data.
 */
class SscLocalRepository(private val database: AppDatabase) {

    private val questionDao = database.questionDao()
    private val testDao = database.testDao()
    private val testQuestionDao = database.testQuestionDao()
    private val attemptDao = database.attemptDao()
    private val attemptAnswerDao = database.attemptAnswerDao()
    private val dailyNoteDao = database.dailyNoteDao()

    // ----------------------------------------------------
    // 1. Questions Operations
    // ----------------------------------------------------
    val allQuestions: Flow<List<QuestionEntity>> = questionDao.getAllQuestions()
    val bookmarkedQuestions: Flow<List<QuestionEntity>> = questionDao.getBookmarkedQuestions()
    val totalQuestionCount: Flow<Int> = questionDao.getQuestionCount()

    suspend fun insertQuestion(question: QuestionEntity): Long =
        questionDao.insertQuestion(question)

    suspend fun insertQuestions(questions: List<QuestionEntity>): List<Long> =
        questionDao.insertQuestions(questions)

    fun getQuestionsBySubject(subject: String): Flow<List<QuestionEntity>> =
        questionDao.getQuestionsBySubject(subject)

    fun getQuestionsBySubjectAndTopic(subject: String, topic: String): Flow<List<QuestionEntity>> =
        questionDao.getQuestionsBySubjectAndTopic(subject, topic)

    suspend fun getQuestionsBySubjectAndTopicDirect(subject: String, topic: String): List<QuestionEntity> =
        questionDao.getQuestionsBySubjectAndTopicDirect(subject, topic)

    fun getQuestionsByYear(year: Int): Flow<List<QuestionEntity>> =
        questionDao.getQuestionsByYear(year)

    fun getQuestionsByYearAndShift(year: Int, shift: String): Flow<List<QuestionEntity>> =
        questionDao.getQuestionsByYearAndShift(year, shift)

    suspend fun toggleBookmark(questionId: Long, isBookmarked: Boolean) =
        questionDao.updateBookmarkStatus(questionId, isBookmarked)

    // ----------------------------------------------------
    // 2. Tests Operations
    // ----------------------------------------------------
    val allTests: Flow<List<TestEntity>> = testDao.getAllTests()

    suspend fun insertTest(test: TestEntity): Long =
        testDao.insertTest(test)

    suspend fun insertTests(tests: List<TestEntity>): List<Long> =
        testDao.insertTests(tests)

    fun getTestsByType(testType: String): Flow<List<TestEntity>> =
        testDao.getTestsByType(testType)

    fun getTestsByYear(year: Int): Flow<List<TestEntity>> =
        testDao.getTestsByYear(year)

    fun getTestById(testId: Long): Flow<TestEntity?> =
        testDao.getTestById(testId)

    // ----------------------------------------------------
    // 3. TestQuestions Mapping Operations
    // ----------------------------------------------------
    suspend fun addQuestionsToTest(testQuestions: List<TestQuestionEntity>): List<Long> =
        testQuestionDao.insertTestQuestions(testQuestions)

    fun getQuestionsForTest(testId: Long): Flow<List<QuestionEntity>> =
        testQuestionDao.getQuestionsForTest(testId)

    // ----------------------------------------------------
    // 4. Attempts & 5. AttemptAnswers Operations
    // ----------------------------------------------------
    val allAttempts: Flow<List<AttemptEntity>> = attemptDao.getAllAttempts()

    fun getRecentAttempts(limit: Int = 10): Flow<List<AttemptEntity>> =
        attemptDao.getLatestAttempts(limit)

    fun getAttemptsForTest(testId: Long): Flow<List<AttemptEntity>> =
        attemptDao.getAttemptsByTestId(testId)

    fun getAttemptAnswers(attemptId: Long): Flow<List<AttemptAnswerEntity>> =
        attemptAnswerDao.getAnswersForAttempt(attemptId)

    /**
     * Saves a complete test attempt along with all its individual question responses.
     */
    suspend fun recordTestAttempt(
        attempt: AttemptEntity,
        answers: List<AttemptAnswerEntity>
    ): Long {
        val attemptId = attemptDao.insertAttempt(attempt)
        val answersWithAttemptId = answers.map { it.copy(attemptId = attemptId) }
        attemptAnswerDao.insertAttemptAnswers(answersWithAttemptId)
        return attemptId
    }

    // ----------------------------------------------------
    // 5. Daily Notes Operations (Room Database)
    // ----------------------------------------------------
    fun getActiveDailyNoteSet(type: String): Flow<DailyNoteSetEntity?> =
        dailyNoteDao.getActiveSetByType(type)

    fun getAllDailyNoteSets(type: String): Flow<List<DailyNoteSetEntity>> =
        dailyNoteDao.getAllSetsByType(type)

    fun getDailyNoteItems(setId: Long): Flow<List<DailyNoteItemEntity>> =
        dailyNoteDao.getItemsForSet(setId)

    fun getDailyNoteProgress(setId: Long): Flow<List<DailyNoteProgressEntity>> =
        dailyNoteDao.getProgressForSet(setId)

    fun getReviewLaterItems(type: String): Flow<List<DailyNoteItemEntity>> =
        dailyNoteDao.getReviewLaterItemsByType(type)

    suspend fun insertDailyNoteSet(set: DailyNoteSetEntity): Long =
        dailyNoteDao.insertSet(set)

    suspend fun insertDailyNoteItems(items: List<DailyNoteItemEntity>): List<Long> =
        dailyNoteDao.insertItems(items)

    suspend fun markDailyNoteSetCompleted(type: String, currentSetId: Long): DailyNoteSetEntity? =
        dailyNoteDao.markSetCompletedAndUnlockNext(type, currentSetId)

    suspend fun updateDailyNoteProgress(progress: DailyNoteProgressEntity): Long =
        dailyNoteDao.insertOrUpdateProgress(progress)

    /**
     * Seeds initial practice questions if not already present in the database.
     * Prevents duplicates on re-initialization.
     */
    suspend fun seedInitialQuestionsIfEmpty() {
        val count = questionDao.getQuestionsCountBySubjectAndTopic("Reasoning", "Analogy")
        if (count == 0) {
            questionDao.insertQuestions(QuestionSeedData.reasoningAnalogyQuestions)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SscLocalRepository? = null

        fun getInstance(context: android.content.Context): SscLocalRepository {
            return INSTANCE ?: synchronized(this) {
                val db = AppDatabase.getDatabase(context)
                val repo = SscLocalRepository(db)
                INSTANCE = repo
                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                    repo.seedInitialQuestionsIfEmpty()
                }
                repo
            }
        }
    }
}

