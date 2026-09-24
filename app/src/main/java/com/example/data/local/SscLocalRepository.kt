package com.example.data.local

import android.content.Context
import androidx.room.withTransaction
import com.example.data.local.entities.AttemptAnswerEntity
import com.example.data.local.entities.AttemptEntity
import com.example.data.local.entities.BookmarkEntity
import com.example.data.local.entities.DailyNoteItemEntity
import com.example.data.local.entities.DailyNoteProgressEntity
import com.example.data.local.entities.DailyNoteSetEntity
import com.example.data.local.entities.QuestionEntity
import com.example.data.local.entities.QuestionProgressEntity
import com.example.data.local.entities.SubjectEntity
import com.example.data.local.entities.SubtopicEntity
import com.example.data.local.entities.TestEntity
import com.example.data.local.entities.TestQuestionEntity
import com.example.data.local.entities.TopicEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Repository boundary for the offline SSC CGL database.
 * UI and ViewModels should depend on this class rather than Room DAOs.
 */
class SscLocalRepository(private val database: AppDatabase) {
    private val hierarchyDao = database.hierarchyDao()
    private val questionDao = database.questionDao()
    private val testDao = database.testDao()
    private val testQuestionDao = database.testQuestionDao()
    private val attemptDao = database.attemptDao()
    private val attemptAnswerDao = database.attemptAnswerDao()
    private val bookmarkDao = database.bookmarkDao()
    private val questionProgressDao = database.questionProgressDao()
    private val dailyNoteDao = database.dailyNoteDao()

    // Exam -> Subject -> Topic -> Subtopic
    val exams = hierarchyDao.getExams()

    fun getSubjects(examId: String = SscCatalogIds.EXAM_SSC_CGL): Flow<List<SubjectEntity>> =
        hierarchyDao.getSubjects(examId)

    fun getTopics(subjectId: String): Flow<List<TopicEntity>> =
        hierarchyDao.getTopics(subjectId)

    fun getSubtopics(topicId: String): Flow<List<SubtopicEntity>> =
        hierarchyDao.getSubtopics(topicId)

    // Central Question Bank
    val allQuestions: Flow<List<QuestionEntity>> = questionDao.getAllQuestions()
    val bookmarkedQuestions: Flow<List<QuestionEntity>> = questionDao.getBookmarkedQuestions()
    val weakAreaQuestions: Flow<List<QuestionEntity>> = questionDao.getWeakAreaQuestions()
    val revisionQuestions: Flow<List<QuestionEntity>> = questionDao.getRevisionQuestions()
    val totalQuestionCount: Flow<Int> = questionDao.getQuestionCount()

    suspend fun insertQuestion(question: QuestionEntity): Long =
        questionDao.insertQuestion(question)

    suspend fun insertQuestions(questions: List<QuestionEntity>): List<Long> =
        questionDao.insertQuestions(questions)

    fun getQuestionsBySubject(subjectId: String): Flow<List<QuestionEntity>> =
        questionDao.getQuestionsBySubject(subjectId)

    fun getQuestionsByTopic(topicId: String): Flow<List<QuestionEntity>> =
        questionDao.getQuestionsByTopic(topicId)

    fun getQuestionsBySubtopic(subtopicId: String): Flow<List<QuestionEntity>> =
        questionDao.getQuestionsBySubtopic(subtopicId)

    suspend fun getQuestionsBySubtopicDirect(subtopicId: String): List<QuestionEntity> =
        questionDao.getQuestionsBySubtopicDirect(subtopicId)

    suspend fun getQuestionsByTopicDirect(topicId: String): List<QuestionEntity> =
        questionDao.getQuestionsByTopicDirect(topicId)

    suspend fun getRandomQuestionsForSubject(subjectId: String, limit: Int): List<QuestionEntity> =
        questionDao.getRandomQuestionsBySubject(subjectId, limit)

    suspend fun getRandomQuestionsForSubjects(
        subjectIds: List<String>,
        perSubject: Int
    ): List<QuestionEntity> = subjectIds
        .distinct()
        .flatMap { getRandomQuestionsForSubject(it, perSubject) }
        .distinctBy(QuestionEntity::id)

    fun getQuestionsBySubjectAndTopic(
        subjectId: String,
        topicId: String
    ): Flow<List<QuestionEntity>> = questionDao.getQuestionsBySubjectAndTopic(subjectId, topicId)

    suspend fun getQuestionsBySubjectAndTopicDirect(
        subjectId: String,
        topicId: String
    ): List<QuestionEntity> = questionDao.getQuestionsBySubjectAndTopicDirect(subjectId, topicId)

    fun getQuestionsByYear(year: Int): Flow<List<QuestionEntity>> =
        questionDao.getQuestionsByYear(year)

    fun getQuestionsByYearAndShift(year: Int, shift: String): Flow<List<QuestionEntity>> =
        questionDao.getQuestionsByYearAndShift(year, shift)

    suspend fun setBookmarked(questionId: Long, bookmarked: Boolean) {
        if (bookmarked) bookmarkDao.addBookmark(BookmarkEntity(questionId))
        else bookmarkDao.removeBookmark(questionId)
    }

    suspend fun toggleBookmark(questionId: Long, isBookmarked: Boolean) =
        setBookmarked(questionId, isBookmarked)

    fun isBookmarked(questionId: Long): Flow<Boolean> = bookmarkDao.isBookmarked(questionId)

    fun getQuestionProgress(questionId: Long): Flow<QuestionProgressEntity?> =
        questionProgressDao.getProgress(questionId)

    // Tests and reusable question mappings
    val allTests: Flow<List<TestEntity>> = testDao.getAllTests()

    suspend fun insertTest(test: TestEntity): Long = testDao.insertTest(test)

    suspend fun insertTests(tests: List<TestEntity>): List<Long> = testDao.insertTests(tests)

    fun getTestsByType(testType: String): Flow<List<TestEntity>> = testDao.getTestsByType(testType)

    fun getTestsByYear(year: Int): Flow<List<TestEntity>> = testDao.getTestsByYear(year)

    fun getTestById(testId: Long): Flow<TestEntity?> = testDao.getTestById(testId)

    suspend fun createTestWithQuestions(
        test: TestEntity,
        questionIds: List<Long>
    ): Long = database.withTransaction {
        require(questionIds.distinct().size == questionIds.size) {
            "A test cannot contain the same central question more than once."
        }
        val testId = testDao.insertTest(test)
        val mappings = questionIds.mapIndexed { index, questionId ->
            TestQuestionEntity(
                testId = testId,
                questionId = questionId,
                questionOrder = index + 1
            )
        }
        testQuestionDao.insertTestQuestions(mappings)
        testId
    }

    suspend fun addQuestionsToTest(testQuestions: List<TestQuestionEntity>): List<Long> =
        testQuestionDao.insertTestQuestions(testQuestions)

    fun getQuestionsForTest(testId: Long): Flow<List<QuestionEntity>> =
        testQuestionDao.getQuestionsForTest(testId)

    // Attempts, results, weak areas, and revision progress
    val allAttempts: Flow<List<AttemptEntity>> = attemptDao.getAllAttempts()

    fun getRecentAttempts(limit: Int = 10): Flow<List<AttemptEntity>> =
        attemptDao.getLatestAttempts(limit)

    fun getAttemptsForTest(testId: Long): Flow<List<AttemptEntity>> =
        attemptDao.getAttemptsByTestId(testId)

    fun getAttemptAnswers(attemptId: Long): Flow<List<AttemptAnswerEntity>> =
        attemptAnswerDao.getAnswersForAttempt(attemptId)

    /** Persists an attempt, its answers, and per-question progress atomically. */
    suspend fun recordTestAttempt(
        attempt: AttemptEntity,
        answers: List<AttemptAnswerEntity>
    ): Long = database.withTransaction {
        require(answers.map { it.questionId }.distinct().size == answers.size) {
            "An attempt can contain only one answer per central question."
        }
        val attemptId = attemptDao.insertAttempt(attempt)
        val orderedAnswers = answers.mapIndexed { index, answer ->
            answer.copy(attemptId = attemptId, answerOrder = index + 1)
        }
        attemptAnswerDao.insertAttemptAnswers(orderedAnswers)
        val attemptedAt = attempt.endTime ?: attempt.startTime
        orderedAnswers.forEach { updateQuestionProgress(it, attemptedAt) }
        attemptId
    }

    private suspend fun updateQuestionProgress(answer: AttemptAnswerEntity, attemptedAt: Long) {
        val old = questionProgressDao.getProgressDirect(answer.questionId)
            ?: QuestionProgressEntity(questionId = answer.questionId)
        val attemptCount = old.attemptCount + 1
        val correctCount = old.correctCount + if (answer.isCorrect) 1 else 0
        val skipped = answer.selectedOption == null
        val wrongCount = old.wrongCount + if (!skipped && !answer.isCorrect) 1 else 0
        val skippedCount = old.skippedCount + if (skipped) 1 else 0
        val masteryScore = correctCount * 100.0 / attemptCount

        questionProgressDao.upsertProgress(
            old.copy(
                attemptCount = attemptCount,
                correctCount = correctCount,
                wrongCount = wrongCount,
                skippedCount = skippedCount,
                totalTimeSeconds = old.totalTimeSeconds + answer.timeSpentSeconds,
                lastAttemptedAt = attemptedAt,
                masteryScore = masteryScore,
                needsRevision = answer.isMarkedForReview || masteryScore < 60.0
            )
        )
    }

    // Daily Notes stay separate from question-bank progress.
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

    suspend fun insertDailyNoteSet(set: DailyNoteSetEntity): Long = dailyNoteDao.insertSet(set)

    suspend fun insertDailyNoteItems(items: List<DailyNoteItemEntity>): List<Long> =
        dailyNoteDao.insertItems(items)

    suspend fun markDailyNoteSetCompleted(type: String, currentSetId: Long): DailyNoteSetEntity? =
        dailyNoteDao.markSetCompletedAndUnlockNext(type, currentSetId)

    suspend fun updateDailyNoteProgress(progress: DailyNoteProgressEntity): Long =
        dailyNoteDao.insertOrUpdateProgress(progress)

    /** Idempotent seed: hierarchy first, then central questions by unique content_key. */
    suspend fun seedFoundationIfNeeded() = database.withTransaction {
        hierarchyDao.insertExam(QuestionSeedData.exam)
        hierarchyDao.insertSubjects(QuestionSeedData.subjects)
        hierarchyDao.insertTopics(QuestionSeedData.topics)
        hierarchyDao.insertSubtopics(SubjectCatalogSeedData.subtopics)
        questionDao.insertQuestions(QuestionSeedData.allQuestions)
        QuestionSeedData.allQuestions.forEach { question ->
            questionDao.updateQuestionClassification(
                contentKey = question.contentKey,
                topicId = question.topicId,
                subtopicId = question.subtopicId
            )
        }
    }

    suspend fun seedInitialQuestionsIfEmpty() = seedFoundationIfNeeded()

    companion object {
        @Volatile private var INSTANCE: SscLocalRepository? = null

        fun getInstance(context: Context): SscLocalRepository {
            return INSTANCE ?: synchronized(this) {
                val repo = SscLocalRepository(AppDatabase.getDatabase(context))
                INSTANCE = repo
                CoroutineScope(Dispatchers.IO).launch { repo.seedFoundationIfNeeded() }
                repo
            }
        }
    }
}
