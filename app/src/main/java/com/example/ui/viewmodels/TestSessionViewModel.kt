package com.example.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.SscLocalRepository
import com.example.data.local.entities.AttemptAnswerEntity
import com.example.data.local.entities.AttemptEntity
import com.example.data.local.entities.QuestionEntity
import com.example.data.local.entities.TestEntity
import com.example.data.models.CustomTestResult
import com.example.data.models.Difficulty
import com.example.data.models.PracticeQuestion
import com.example.data.models.SscDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TestSessionUiState(
    val testId: Long? = null,
    val testType: String = "CUSTOM",
    val questions: List<PracticeQuestion> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null
)

class TestSessionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SscLocalRepository.getInstance(application)
    private val _uiState = MutableStateFlow(TestSessionUiState())
    val uiState: StateFlow<TestSessionUiState> = _uiState.asStateFlow()

    fun prepareCustomTest(subjectIds: List<String>, isTimed: Boolean) {
        prepareSession(
            subjectIds = subjectIds,
            title = "Custom Test",
            type = "CUSTOM",
            durationMinutes = if (isTimed) subjectIds.size * 15 else 0
        )
    }

    fun prepareMockTest() {
        prepareSession(
            subjectIds = MOCK_SUBJECT_ORDER,
            title = "Mock Test",
            type = "MOCK",
            durationMinutes = 60
        )
    }

    private fun prepareSession(
        subjectIds: List<String>,
        title: String,
        type: String,
        durationMinutes: Int
    ) {
        _uiState.value = TestSessionUiState(testType = type, isLoading = true)
        viewModelScope.launch {
            runCatching {
                repository.seedFoundationIfNeeded()
                val entities = repository.getRandomQuestionsForSubjects(
                    subjectIds = subjectIds,
                    perSubject = QUESTIONS_PER_SUBJECT
                )
                val testId = repository.createTestWithQuestions(
                    test = TestEntity(
                        title = title,
                        testType = type,
                        totalQuestions = entities.size,
                        durationMinutes = durationMinutes,
                        totalMarks = entities.size * 2.0
                    ),
                    questionIds = entities.map(QuestionEntity::id)
                )
                testId to entities.mapIndexed { index, entity -> entity.toPracticeQuestion(index + 1) }
            }.onSuccess { (testId, questions) ->
                _uiState.update {
                    it.copy(testId = testId, questions = questions, isLoading = false)
                }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
            }
        }
    }

    fun recordResult(result: CustomTestResult, onSaved: (CustomTestResult) -> Unit) {
        val state = _uiState.value
        val testId = state.testId ?: return
        if (state.isSaving) return
        _uiState.update { it.copy(isSaving = true, errorMessage = null) }

        viewModelScope.launch {
            runCatching {
                val finishedAt = System.currentTimeMillis()
                val attempt = AttemptEntity(
                    testId = testId,
                    testTitle = if (state.testType == "MOCK") "Mock Test" else "Custom Test",
                    attemptType = state.testType,
                    startTime = finishedAt - result.totalTimeSeconds * 1_000,
                    endTime = finishedAt,
                    timeTakenSeconds = result.totalTimeSeconds,
                    totalQuestions = result.totalQuestions,
                    attemptedCount = result.attempted,
                    correctCount = result.correct,
                    wrongCount = result.wrong,
                    unattemptedCount = result.unattempted,
                    score = result.totalScore,
                    maxScore = result.maxScore,
                    accuracy = result.accuracyPercent,
                    isCompleted = true
                )
                val answers = result.questionReviews.map { review ->
                    AttemptAnswerEntity(
                        attemptId = 0,
                        questionId = requireNotNull(review.question.roomQuestionId),
                        selectedOption = review.selectedOptionIndex?.plus(1),
                        correctOption = review.correctOptionIndex + 1,
                        isCorrect = review.isCorrect,
                        isMarkedForReview = review.isMarkedForReview,
                        timeSpentSeconds = review.timeSpentSeconds,
                        awardedMarks = when {
                            review.isCorrect -> 2.0
                            review.isAttempted -> -0.5
                            else -> 0.0
                        }
                    )
                }
                repository.recordTestAttempt(attempt, answers)
            }.onSuccess {
                _uiState.update { it.copy(isSaving = false) }
                onSaved(result)
            }.onFailure { error ->
                _uiState.update { it.copy(isSaving = false, errorMessage = error.message) }
            }
        }
    }

    companion object {
        const val QUESTIONS_PER_SUBJECT = 25
        val MOCK_SUBJECT_ORDER = listOf("reasoning", "gk_ga", "maths", "english")
    }
}

private fun QuestionEntity.toPracticeQuestion(number: Int): PracticeQuestion {
    val subject = SscDataRepository.subjects.firstOrNull { candidate ->
        candidate.topics.any { it.id == topicId }
    }
    val topic = subject?.topics?.firstOrNull { it.id == topicId }
    val subtopic = topic?.subtopics?.firstOrNull { it.id == subtopicId }
    return PracticeQuestion(
        id = number,
        questionNumber = number,
        questionText = questionText,
        options = listOf(optionA, optionB, optionC, optionD),
        correctOptionIndex = (correctOption - 1).coerceIn(0, 3),
        explanation = explanation,
        difficulty = runCatching { Difficulty.valueOf(difficulty.uppercase()) }
            .getOrDefault(Difficulty.MODERATE),
        subjectTitle = subject?.title.orEmpty(),
        topicTitle = topic?.title.orEmpty(),
        subtopicTitle = subtopic?.title.orEmpty(),
        subtopicId = subtopicId.orEmpty(),
        roomQuestionId = id
    )
}
