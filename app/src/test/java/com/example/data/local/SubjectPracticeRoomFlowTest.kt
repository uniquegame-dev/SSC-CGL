package com.example.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.entities.AttemptAnswerEntity
import com.example.data.local.entities.AttemptEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class SubjectPracticeRoomFlowTest {
    private lateinit var database: AppDatabase
    private lateinit var repository: SscLocalRepository

    @Before
    fun setUp() {
        runBlocking {
            val context = ApplicationProvider.getApplicationContext<Context>()
            database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
            repository = SscLocalRepository(database)
            repository.seedFoundationIfNeeded()
        }
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `subject selection returns only topics belonging to that subject`() = runBlocking {
        val reasoningTopics = repository.getTopics("reasoning").first()
        val mathsTopics = repository.getTopics("maths").first()

        assertTrue(reasoningTopics.isNotEmpty())
        assertTrue(reasoningTopics.all { it.subjectId == "reasoning" })
        assertTrue(reasoningTopics.any { it.id == "analogies" })
        assertFalse(reasoningTopics.any { it.id == "number_system" })
        assertTrue(mathsTopics.all { it.subjectId == "maths" })
        assertTrue(mathsTopics.any { it.id == "number_system" })
    }

    @Test
    fun `topic selection returns only subtopics belonging to that topic`() = runBlocking {
        val subtopics = repository.getSubtopics("analogies").first()

        assertEquals(
            listOf("sub_sem_ana", "sub_sym_ana", "sub_fig_ana", "sub_odd_one"),
            subtopics.map { it.id }
        )
        assertTrue(subtopics.all { it.topicId == "analogies" })
    }

    @Test
    fun `subtopic fetch returns exact central questions without topic leakage`() = runBlocking {
        val semanticQuestions = repository.getQuestionsBySubtopicDirect("sub_sem_ana")
        val numberQuestions = repository.getQuestionsBySubtopicDirect("sub_sym_ana")

        assertTrue(semanticQuestions.size >= 2)
        assertTrue(numberQuestions.size >= 3)
        assertTrue(semanticQuestions.all { it.subtopicId == "sub_sem_ana" })
        assertTrue(numberQuestions.all { it.subtopicId == "sub_sym_ana" })
        assertTrue(semanticQuestions.map { it.id }.intersect(numberQuestions.map { it.id }.toSet()).isEmpty())
    }

    @Test
    fun `topic practice aggregates all questions assigned across its subtopics`() = runBlocking {
        val topicQuestions = repository.getQuestionsByTopicDirect("analogies")
        val subtopicIds = repository.getSubtopics("analogies").first().map { it.id }.toSet()

        assertTrue(topicQuestions.isNotEmpty())
        assertTrue(topicQuestions.all { it.topicId == "analogies" })
        assertTrue(topicQuestions.all { it.subtopicId in subtopicIds })
        assertTrue(topicQuestions.map { it.id }.distinct().size == topicQuestions.size)
    }

    @Test
    fun `practice attempt saves summary and exact answers`() = runBlocking {
        val question = repository.getQuestionsBySubtopicDirect("sub_sem_ana").first()
        val attemptId = repository.recordTestAttempt(
            attempt = practiceAttempt(correct = 1, wrong = 0, accuracy = 100.0),
            answers = listOf(
                AttemptAnswerEntity(
                    attemptId = 0,
                    questionId = question.id,
                    selectedOption = question.correctOption,
                    correctOption = question.correctOption,
                    isCorrect = true,
                    timeSpentSeconds = 12,
                    awardedMarks = 2.0
                )
            )
        )

        val savedAttempt = repository.allAttempts.first().single()
        val savedAnswer = repository.getAttemptAnswers(attemptId).first().single()
        assertEquals(attemptId, savedAttempt.id)
        assertEquals("sub_sem_ana", savedAttempt.subtopicId)
        assertEquals(question.id, savedAnswer.questionId)
        assertEquals(1, savedAnswer.answerOrder)
    }

    @Test
    fun `saved answer updates progress weak area and revision`() = runBlocking {
        val question = repository.getQuestionsBySubtopicDirect("sub_sym_ana").first()
        repository.recordTestAttempt(
            attempt = practiceAttempt(correct = 0, wrong = 1, accuracy = 0.0),
            answers = listOf(
                AttemptAnswerEntity(
                    attemptId = 0,
                    questionId = question.id,
                    selectedOption = if (question.correctOption == 1) 2 else 1,
                    correctOption = question.correctOption,
                    isCorrect = false,
                    isMarkedForReview = true,
                    timeSpentSeconds = 30,
                    awardedMarks = -0.5
                )
            )
        )

        val progress = repository.getQuestionProgress(question.id).first()!!
        assertEquals(1, progress.attemptCount)
        assertEquals(1, progress.wrongCount)
        assertEquals(30, progress.totalTimeSeconds)
        assertEquals(0.0, progress.masteryScore, 0.0)
        assertTrue(progress.needsRevision)
        assertTrue(repository.weakAreaQuestions.first().any { it.id == question.id })
        assertTrue(repository.revisionQuestions.first().any { it.id == question.id })
    }

    private fun practiceAttempt(correct: Int, wrong: Int, accuracy: Double) = AttemptEntity(
        subjectId = "reasoning",
        topicId = "analogies",
        subtopicId = "sub_sem_ana",
        testTitle = "Analogy Practice",
        attemptType = "PRACTICE",
        startTime = 1_000,
        endTime = 2_000,
        timeTakenSeconds = 60,
        totalQuestions = 1,
        attemptedCount = 1,
        correctCount = correct,
        wrongCount = wrong,
        unattemptedCount = 0,
        score = correct * 2.0 - wrong * 0.5,
        maxScore = 2.0,
        accuracy = accuracy,
        isCompleted = true
    )
}
