package com.example.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.entities.AttemptAnswerEntity
import com.example.data.local.entities.AttemptEntity
import com.example.data.local.entities.TestEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class CentralQuestionBankTest {
    @Test
    fun `one question is reused by test attempt bookmark and progress`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        val repository = SscLocalRepository(database)
        repository.seedFoundationIfNeeded()

        assertEquals(4, repository.getSubjects().first().size)
        val question = repository
            .getQuestionsBySubjectAndTopicDirect("reasoning", "analogies")
            .first()

        val testId = repository.createTestWithQuestions(
            test = TestEntity(
                title = "Central Bank Reuse Test",
                testType = "CUSTOM",
                totalQuestions = 1,
                totalMarks = 2.0
            ),
            questionIds = listOf(question.id)
        )
        assertEquals(question.id, repository.getQuestionsForTest(testId).first().single().id)

        repository.setBookmarked(question.id, true)
        assertEquals(question.id, repository.bookmarkedQuestions.first().single().id)

        repository.recordTestAttempt(
            attempt = AttemptEntity(
                testId = testId,
                testTitle = "Central Bank Reuse Test",
                attemptType = "CUSTOM",
                endTime = 2_000,
                totalQuestions = 1,
                attemptedCount = 1,
                wrongCount = 1,
                score = -0.5,
                maxScore = 2.0,
                accuracy = 0.0,
                isCompleted = true
            ),
            answers = listOf(
                AttemptAnswerEntity(
                    attemptId = 0,
                    questionId = question.id,
                    selectedOption = 2,
                    correctOption = question.correctOption,
                    isCorrect = false,
                    isMarkedForReview = true,
                    timeSpentSeconds = 25,
                    awardedMarks = -0.5
                )
            )
        )

        val progress = repository.getQuestionProgress(question.id).first()!!
        assertEquals(1, progress.attemptCount)
        assertEquals(1, progress.wrongCount)
        assertEquals(25, progress.totalTimeSeconds)
        assertTrue(progress.needsRevision)
        assertEquals(question.id, repository.weakAreaQuestions.first().single().id)
        assertEquals(question.id, repository.revisionQuestions.first().single().id)

        repository.setBookmarked(question.id, false)
        assertFalse(repository.isBookmarked(question.id).first())
        database.close()
    }
}
