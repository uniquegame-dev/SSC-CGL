package com.example.data.local

import com.example.data.local.entities.ExamEntity
import com.example.data.local.entities.QuestionEntity
import com.example.data.local.entities.SubjectEntity
import com.example.data.local.entities.TopicEntity

/** Stable IDs shared by seed data, importers, repositories, and future UI ViewModels. */
object SscCatalogIds {
    const val EXAM_SSC_CGL = "ssc_cgl"
    const val SUBJECT_REASONING = "reasoning"
    const val SUBJECT_MATHS = "maths"
    const val SUBJECT_ENGLISH = "english"
    const val SUBJECT_GK_GA = "gk_ga"
    const val TOPIC_ANALOGIES = "analogies"
}

/** Minimal bundled foundation. Large question imports can build on the same stable hierarchy. */
object QuestionSeedData {
    val exam = ExamEntity(
        id = SscCatalogIds.EXAM_SSC_CGL,
        code = "SSC_CGL",
        name = "SSC Combined Graduate Level",
        description = "Tier I and Tier II practice, mock tests, and previous-year papers"
    )

    val subjects = listOf(
        SubjectEntity(
            id = SscCatalogIds.SUBJECT_REASONING,
            examId = exam.id,
            name = "Reasoning",
            description = "General Intelligence & Logical Reasoning",
            displayOrder = 1
        ),
        SubjectEntity(
            id = SscCatalogIds.SUBJECT_MATHS,
            examId = exam.id,
            name = "Maths",
            description = "Quantitative Aptitude & Numerical Ability",
            displayOrder = 2
        ),
        SubjectEntity(
            id = SscCatalogIds.SUBJECT_ENGLISH,
            examId = exam.id,
            name = "English",
            description = "Grammar, Vocabulary & Comprehension",
            displayOrder = 3
        ),
        SubjectEntity(
            id = SscCatalogIds.SUBJECT_GK_GA,
            examId = exam.id,
            name = "GK / GA",
            description = "General Knowledge & Current Affairs",
            displayOrder = 4
        )
    )

    val topics: List<TopicEntity> = SubjectCatalogSeedData.topics

    val reasoningAnalogyQuestions = listOf(
        seededQuestion(
            key = "seed_reasoning_analogy_001",
            subtopicId = "sub_sem_ana",
            text = "House : Rent :: Capital : ?",
            options = listOf("Interest", "Investment", "Country", "Money"),
            correct = 1,
            explanation = "house rent कमाता है, capital interest कमाता है."
        ),
        seededQuestion(
            key = "seed_reasoning_analogy_002",
            subtopicId = "sub_sem_ana",
            text = "NUMBER : UNBMRE :: GHOST : ?",
            options = listOf("HOGST", "HOGTS", "HGSOT", "HGOST"),
            correct = 3,
            explanation = "letters को pairwise swap किया गया है."
        ),
        seededQuestion(
            key = "seed_reasoning_analogy_003",
            subtopicId = "sub_sym_ana",
            text = "18 : 30 :: 36 : ?",
            options = listOf("64", "66", "54", "62"),
            correct = 2,
            explanation = "multiply by 2, फिर 6 घटाओ."
        ),
        seededQuestion(
            key = "seed_reasoning_analogy_004",
            subtopicId = "sub_sym_ana",
            text = "Find the set most like (4,10,15).",
            options = listOf("(3,6,12)", "(2,8,10)", "(5,12,18)", "(7,10,18)"),
            correct = 3,
            explanation = "+6 then +5, next +7 then +6."
        ),
        seededQuestion(
            key = "seed_reasoning_analogy_005",
            subtopicId = "sub_sym_ana",
            text = "set most like (6,36,63).",
            options = listOf("(7,49,98)", "(8,64,46)", "(9,84,45)", "(11,111,84)"),
            correct = 2,
            explanation = "number squared करो, फिर उसके digits को reverse करो."
        )
    )

    private fun seededQuestion(
        key: String,
        subtopicId: String,
        text: String,
        options: List<String>,
        correct: Int,
        explanation: String
    ) = QuestionEntity(
        contentKey = key,
        topicId = SscCatalogIds.TOPIC_ANALOGIES,
        subtopicId = subtopicId,
        questionText = text,
        optionA = options[0],
        optionB = options[1],
        optionC = options[2],
        optionD = options[3],
        correctOption = correct,
        explanation = explanation,
        difficulty = "EASY",
        sourceType = "PRACTICE",
        sourceName = "Bundled seed"
    )

    val allQuestions: List<QuestionEntity> by lazy {
        (reasoningAnalogyQuestions + LegacyQuestionBankSeedData.questions)
            .distinctBy { it.questionText.trim().lowercase() }
    }
}
