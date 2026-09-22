package com.example.data.models

enum class Difficulty(val label: String) {
    EASY("Easy"),
    MODERATE("Moderate"),
    HARD("Hard")
}

enum class PaletteState(val label: String) {
    NOT_VISITED("Not Visited"),
    NOT_ANSWERED("Not Answered"),
    ANSWERED("Answered"),
    MARKED_FOR_REVIEW("Marked for Review")
}

data class PracticeQuestion(
    val id: Int,
    val roomQuestionId: Long? = null,
    val questionNumber: Int,
    val questionText: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanation: String,
    val difficulty: Difficulty,
    val subjectTitle: String,
    val topicTitle: String,
    val subtopicTitle: String,
    val subtopicId: String,
    val shortcutMethod: String? = null
)

data class UserQuestionState(
    val questionId: Int,
    val selectedOptionIndex: Int? = null,
    val isMarkedForReview: Boolean = false,
    val isVisited: Boolean = false,
    val timeSpentSeconds: Long = 0L
) {
    val paletteState: PaletteState
        get() {
            return when {
                !isVisited -> PaletteState.NOT_VISITED
                isMarkedForReview -> PaletteState.MARKED_FOR_REVIEW
                selectedOptionIndex != null -> PaletteState.ANSWERED
                else -> PaletteState.NOT_ANSWERED
            }
        }
}

data class QuestionReviewItem(
    val question: PracticeQuestion,
    val selectedOptionIndex: Int?,
    val correctOptionIndex: Int,
    val isCorrect: Boolean,
    val isAttempted: Boolean,
    val timeSpentSeconds: Long,
    val isMarkedForReview: Boolean = false
)

enum class ReviewFilter(val label: String) {
    ALL("All"),
    CORRECT("Correct"),
    WRONG("Wrong"),
    UNATTEMPTED("Unattempted")
}

data class PracticeSessionResult(
    val subjectTitle: String,
    val topicTitle: String,
    val subtopicTitle: String,
    val totalQuestions: Int,
    val attempted: Int,
    val correct: Int,
    val wrong: Int,
    val unattempted: Int,
    val accuracyPercent: Double,
    val totalTimeSeconds: Long,
    val averageTimePerQuestionSeconds: Double,
    val questionReviews: List<QuestionReviewItem>
)

data class SubjectTestPerformance(
    val subjectId: String,
    val subjectTitle: String,
    val totalQuestions: Int,
    val correct: Int,
    val wrong: Int,
    val unattempted: Int,
    val accuracyPercent: Double,
    val score: Double
)

data class CustomTestResult(
    val selectedSubjectTitles: List<String>,
    val totalQuestions: Int,
    val attempted: Int,
    val correct: Int,
    val wrong: Int,
    val unattempted: Int,
    val totalScore: Double,
    val maxScore: Double,
    val accuracyPercent: Double,
    val totalTimeSeconds: Long,
    val isTimed: Boolean,
    val subjectBreakdown: List<SubjectTestPerformance>,
    val negativeMarksLost: Double = 0.0,
    val averageTimePerQuestionSeconds: Double = 0.0,
    val questionReviews: List<QuestionReviewItem> = emptyList()
)
