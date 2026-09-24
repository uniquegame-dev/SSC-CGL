package com.example.data.local

import com.example.data.PracticeQuestionRepository
import com.example.data.local.entities.QuestionEntity
import com.example.data.models.SscDataRepository

/**
 * Converts the app's existing bundled questions into central-question-bank rows.
 * Screens never read [PracticeQuestionRepository] after this one-way seed step.
 */
object LegacyQuestionBankSeedData {
    val questions: List<QuestionEntity> by lazy {
        SscDataRepository.subjects
            .flatMap { subject ->
                val sourceQuestions = PracticeQuestionRepository.getQuestionsForSubject(subject.id)
                sourceQuestions.mapNotNull { question ->
                    val topic = subject.topics.firstOrNull {
                        it.title.equals(question.topicTitle, ignoreCase = true) ||
                            it.title.contains(question.topicTitle, ignoreCase = true) ||
                            question.topicTitle.contains(it.title, ignoreCase = true)
                    } ?: subject.topics.firstOrNull() ?: return@mapNotNull null
                    val subtopic = topic.subtopics.firstOrNull {
                        it.id == question.subtopicId ||
                            it.title.equals(question.subtopicTitle, ignoreCase = true)
                    } ?: topic.subtopics.firstOrNull()

                    QuestionEntity(
                        contentKey = contentKey(subject.id, question.questionText),
                        topicId = topic.id,
                        subtopicId = subtopic?.id,
                        questionText = question.questionText,
                        optionA = question.options.getOrElse(0) { "" },
                        optionB = question.options.getOrElse(1) { "" },
                        optionC = question.options.getOrElse(2) { "" },
                        optionD = question.options.getOrElse(3) { "" },
                        correctOption = question.correctOptionIndex + 1,
                        explanation = question.explanation,
                        difficulty = question.difficulty.name,
                        sourceType = "BUNDLED"
                    )
                }
            }
            .distinctBy { it.questionText.trim().lowercase() }
    }

    private fun contentKey(subjectId: String, questionText: String): String {
        val normalized = questionText.trim().lowercase().replace(Regex("\\s+"), " ")
        return "bundled_${subjectId}_${normalized.hashCode().toUInt().toString(16)}"
    }
}
