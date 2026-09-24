package com.example.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.SscCatalogIds
import com.example.data.local.SscLocalRepository
import com.example.data.local.entities.AttemptAnswerEntity
import com.example.data.local.entities.AttemptEntity
import com.example.data.local.entities.QuestionEntity
import com.example.data.local.entities.SubjectEntity
import com.example.data.local.entities.SubtopicEntity
import com.example.data.local.entities.TopicEntity
import com.example.data.models.Difficulty
import com.example.data.models.PracticeQuestion
import com.example.data.models.PracticeSessionResult
import com.example.data.models.SscDataRepository
import com.example.data.models.Subject
import com.example.data.models.Subtopic
import com.example.data.models.Topic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SubjectPracticeUiState(
    val subjects: List<Subject> = emptyList(),
    val selectedSubject: Subject? = null,
    val selectedTopic: Topic? = null,
    val selectedSubtopic: Subtopic? = null,
    val questions: List<PracticeQuestion> = emptyList(),
    val isLoadingCatalog: Boolean = true,
    val isLoadingQuestions: Boolean = false,
    val isSavingAttempt: Boolean = false,
    val errorMessage: String? = null
)

class SubjectPracticeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SscLocalRepository.getInstance(application)
    private val _uiState = MutableStateFlow(SubjectPracticeUiState())
    val uiState: StateFlow<SubjectPracticeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            runCatching {
                repository.seedFoundationIfNeeded()
                repository.getSubjects(SscCatalogIds.EXAM_SSC_CGL).first()
            }.onSuccess { entities ->
                _uiState.update {
                    it.copy(
                        subjects = entities.map(SubjectEntity::toUiModel),
                        isLoadingCatalog = false,
                        errorMessage = null
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isLoadingCatalog = false, errorMessage = error.message)
                }
            }
        }
    }

    fun selectSubject(subject: Subject) {
        _uiState.update {
            it.copy(
                selectedSubject = subject.copy(topics = emptyList()),
                selectedTopic = null,
                selectedSubtopic = null,
                questions = emptyList(),
                isLoadingCatalog = true,
                errorMessage = null
            )
        }
        viewModelScope.launch {
            runCatching { repository.getTopics(subject.id).first() }
                .onSuccess { topicEntities ->
                    val topics = topicEntities.map(TopicEntity::toUiModel)
                    _uiState.update {
                        it.copy(
                            selectedSubject = subject.copy(topics = topics),
                            isLoadingCatalog = false
                        )
                    }
                }
                .onFailure(::setCatalogError)
        }
    }

    fun selectTopic(topic: Topic) {
        _uiState.update {
            it.copy(
                selectedTopic = topic.copy(subtopics = emptyList()),
                selectedSubtopic = null,
                questions = emptyList(),
                isLoadingCatalog = true,
                errorMessage = null
            )
        }
        viewModelScope.launch {
            runCatching { repository.getSubtopics(topic.id).first() }
                .onSuccess { subtopicEntities ->
                    _uiState.update {
                        it.copy(
                            selectedTopic = topic.copy(
                                subtopics = subtopicEntities.map(SubtopicEntity::toUiModel)
                            ),
                            isLoadingCatalog = false
                        )
                    }
                }
                .onFailure(::setCatalogError)
        }
    }

    /** Loads every active central-bank question assigned to this topic, across its subtopics. */
    fun selectTopicForPractice(topic: Topic) {
        _uiState.update {
            it.copy(
                selectedTopic = topic,
                selectedSubtopic = null,
                questions = emptyList(),
                isLoadingQuestions = true,
                errorMessage = null
            )
        }
        viewModelScope.launch {
            runCatching { repository.getQuestionsByTopicDirect(topic.id) }
                .onSuccess { questionEntities ->
                    val state = _uiState.value
                    val questions = questionEntities.mapIndexed { index, entity ->
                        entity.toPracticeQuestion(
                            number = index + 1,
                            subjectTitle = state.selectedSubject?.title.orEmpty(),
                            topicTitle = topic.title,
                            subtopic = topic.subtopics.firstOrNull { it.id == entity.subtopicId }
                                ?: Subtopic(entity.subtopicId.orEmpty(), "All Topic Questions", "")
                        )
                    }
                    _uiState.update {
                        it.copy(questions = questions, isLoadingQuestions = false)
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoadingQuestions = false, errorMessage = error.message)
                    }
                }
        }
    }

    fun selectSubtopic(subtopic: Subtopic) {
        _uiState.update {
            it.copy(
                selectedSubtopic = subtopic,
                questions = emptyList(),
                isLoadingQuestions = true,
                errorMessage = null
            )
        }
        viewModelScope.launch {
            runCatching { repository.getQuestionsBySubtopicDirect(subtopic.id) }
                .onSuccess { questionEntities ->
                    val state = _uiState.value
                    val questions = questionEntities.mapIndexed { index, entity ->
                        entity.toPracticeQuestion(
                            number = index + 1,
                            subjectTitle = state.selectedSubject?.title.orEmpty(),
                            topicTitle = state.selectedTopic?.title.orEmpty(),
                            subtopic = subtopic
                        )
                    }
                    _uiState.update {
                        it.copy(questions = questions, isLoadingQuestions = false)
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoadingQuestions = false, errorMessage = error.message)
                    }
                }
        }
    }

    fun clearSubjectSelection() {
        _uiState.update {
            it.copy(
                selectedSubject = null,
                selectedTopic = null,
                selectedSubtopic = null,
                questions = emptyList(),
                errorMessage = null
            )
        }
    }

    fun clearTopicSelection() {
        _uiState.update {
            it.copy(
                selectedTopic = null,
                selectedSubtopic = null,
                questions = emptyList(),
                errorMessage = null
            )
        }
    }

    fun recordPracticeResult(
        result: PracticeSessionResult,
        onSaved: (PracticeSessionResult) -> Unit
    ) {
        if (_uiState.value.isSavingAttempt) return
        val state = _uiState.value
        val subject = state.selectedSubject ?: return
        val topic = state.selectedTopic ?: return
        _uiState.update { it.copy(isSavingAttempt = true, errorMessage = null) }

        viewModelScope.launch {
            runCatching {
                val finishedAt = System.currentTimeMillis()
                val attempt = AttemptEntity(
                    subjectId = subject.id,
                    topicId = topic.id,
                    subtopicId = null,
                    testTitle = topic.title,
                    attemptType = "PRACTICE",
                    startTime = finishedAt - result.totalTimeSeconds * 1_000,
                    endTime = finishedAt,
                    timeTakenSeconds = result.totalTimeSeconds,
                    totalQuestions = result.totalQuestions,
                    attemptedCount = result.attempted,
                    correctCount = result.correct,
                    wrongCount = result.wrong,
                    unattemptedCount = result.unattempted,
                    score = result.correct * 2.0 - result.wrong * 0.5,
                    maxScore = result.totalQuestions * 2.0,
                    accuracy = result.accuracyPercent,
                    isCompleted = true
                )
                val answers = result.questionReviews.map { review ->
                    val questionId = requireNotNull(review.question.roomQuestionId) {
                        "Practice result contains a question that is not backed by Room."
                    }
                    AttemptAnswerEntity(
                        attemptId = 0,
                        questionId = questionId,
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
                _uiState.update { it.copy(isSavingAttempt = false) }
                onSaved(result)
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isSavingAttempt = false, errorMessage = error.message)
                }
            }
        }
    }

    private fun setCatalogError(error: Throwable) {
        _uiState.update {
            it.copy(isLoadingCatalog = false, errorMessage = error.message)
        }
    }
}

private fun SubjectEntity.toUiModel(): Subject {
    val displayMetadata = SscDataRepository.getSubjectById(id)
        ?: error("Missing display metadata for subject $id")
    return Subject(
        id = id,
        title = name,
        description = description,
        icon = displayMetadata.icon,
        testTag = displayMetadata.testTag,
        topics = emptyList()
    )
}

private fun TopicEntity.toUiModel() = Topic(
    id = id,
    title = name,
    description = description,
    subtopics = emptyList()
)

private fun SubtopicEntity.toUiModel() = Subtopic(
    id = id,
    title = name,
    description = description
)

private fun QuestionEntity.toPracticeQuestion(
    number: Int,
    subjectTitle: String,
    topicTitle: String,
    subtopic: Subtopic
) = PracticeQuestion(
    id = number,
    questionNumber = number,
    questionText = questionText,
    options = listOf(optionA, optionB, optionC, optionD),
    correctOptionIndex = (correctOption - 1).coerceIn(0, 3),
    explanation = explanation,
    difficulty = runCatching { Difficulty.valueOf(difficulty.uppercase()) }
        .getOrDefault(Difficulty.MODERATE),
    subjectTitle = subjectTitle,
    topicTitle = topicTitle,
    subtopicTitle = subtopic.title,
    subtopicId = subtopic.id,
    roomQuestionId = id
)
