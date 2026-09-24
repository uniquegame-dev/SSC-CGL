package com.example.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.entities.AttemptAnswerEntity
import com.example.data.local.entities.AttemptEntity
import com.example.data.local.entities.TestEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class GeneratedTestRoomFlowTest {
    private lateinit var database: AppDatabase
    private lateinit var repository: SscLocalRepository

    @Before
    fun setUp() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = SscLocalRepository(database)
        repository.seedFoundationIfNeeded()
    }

    @After
    fun tearDown() = database.close()

    @Test
    fun `custom test randomly selects 25 per subject without duplicates`() = runBlocking {
        val selected = listOf("reasoning", "english")
        val questions = repository.getRandomQuestionsForSubjects(selected, 25)

        assertEquals(50, questions.size)
        assertEquals(50, questions.map { it.id }.distinct().size)
        assertEquals(25, countForSubject(questions.map { it.topicId }, "reasoning"))
        assertEquals(25, countForSubject(questions.map { it.topicId }, "english"))
    }

    @Test
    fun `mock test follows four section 25 question Room pattern`() = runBlocking {
        val subjects = listOf("reasoning", "gk_ga", "maths", "english")
        val questions = repository.getRandomQuestionsForSubjects(subjects, 25)
        val testId = repository.createTestWithQuestions(
            TestEntity(
                title = "Mock Test",
                testType = "MOCK",
                totalQuestions = 100,
                durationMinutes = 60,
                totalMarks = 200.0
            ),
            questions.map { it.id }
        )

        val storedQuestions = repository.getQuestionsForTest(testId).first()
        assertEquals(100, storedQuestions.size)
        assertEquals(100, storedQuestions.map { it.id }.distinct().size)
        subjects.forEach { subjectId ->
            assertEquals(25, countForSubject(storedQuestions.map { it.topicId }, subjectId))
        }
    }

    @Test
    fun `generated test persists test attempt answers and progress`() = runBlocking {
        val questions = repository.getRandomQuestionsForSubjects(listOf("maths"), 25)
        val testId = repository.createTestWithQuestions(
            TestEntity(
                title = "Custom Test",
                testType = "CUSTOM",
                totalQuestions = questions.size,
                durationMinutes = 15,
                totalMarks = questions.size * 2.0
            ),
            questions.map { it.id }
        )
        val first = questions.first()
        val attemptId = repository.recordTestAttempt(
            AttemptEntity(
                testId = testId,
                testTitle = "Custom Test",
                attemptType = "CUSTOM",
                startTime = 1_000,
                endTime = 2_000,
                totalQuestions = 1,
                attemptedCount = 1,
                correctCount = 1,
                score = 2.0,
                maxScore = 2.0,
                accuracy = 100.0,
                isCompleted = true
            ),
            listOf(
                AttemptAnswerEntity(
                    attemptId = 0,
                    questionId = first.id,
                    selectedOption = first.correctOption,
                    correctOption = first.correctOption,
                    isCorrect = true,
                    timeSpentSeconds = 20,
                    awardedMarks = 2.0
                )
            )
        )

        assertEquals(testId, repository.allAttempts.first().single().testId)
        assertEquals(first.id, repository.getAttemptAnswers(attemptId).first().single().questionId)
        val progress = repository.getQuestionProgress(first.id).first()!!
        assertEquals(1, progress.attemptCount)
        assertEquals(1, progress.correctCount)
        assertEquals(100.0, progress.masteryScore, 0.0)
        assertTrue(!progress.needsRevision)
    }

    private suspend fun countForSubject(topicIds: List<String>, subjectId: String): Int {
        val subjectTopicIds = repository.getTopics(subjectId).first().map { it.id }.toSet()
        return topicIds.count { it in subjectTopicIds }
    }
}
