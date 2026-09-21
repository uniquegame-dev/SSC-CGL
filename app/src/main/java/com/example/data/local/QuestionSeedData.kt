package com.example.data.local

import com.example.data.local.entities.QuestionEntity

/**
 * Seed data for practice questions sourced from standard SSC study references.
 */
object QuestionSeedData {

    val reasoningAnalogyQuestions: List<QuestionEntity> = listOf(
        QuestionEntity(
            questionText = "House : Rent :: Capital : ?",
            optionA = "Interest",
            optionB = "Investment",
            optionC = "Country",
            optionD = "Money",
            correctOption = 1,
            explanation = "house rent कमाता है, capital interest कमाता है.",
            subject = "Reasoning",
            topic = "Analogy",
            subtopic = "SSC KIRAN REASONING pdf",
            difficulty = "Easy",
            year = null,
            examDate = null,
            shift = null,
            examName = "SSC KIRAN REASONING pdf",
            isBookmarked = false
        ),
        QuestionEntity(
            questionText = "NUMBER : UNBMRE :: GHOST : ?",
            optionA = "HOGST",
            optionB = "HOGTS",
            optionC = "HGSOT",
            optionD = "HGOST",
            correctOption = 3,
            explanation = "letters को pairwise swap किया गया है.",
            subject = "Reasoning",
            topic = "Analogy",
            subtopic = "SSC KIRAN REASONING pdf",
            difficulty = "Easy",
            year = null,
            examDate = null,
            shift = null,
            examName = "SSC KIRAN REASONING pdf",
            isBookmarked = false
        ),
        QuestionEntity(
            questionText = "18 : 30 :: 36 : ?",
            optionA = "64",
            optionB = "66",
            optionC = "54",
            optionD = "62",
            correctOption = 2,
            explanation = "multiply by 2, फिर 6 घटाओ.",
            subject = "Reasoning",
            topic = "Analogy",
            subtopic = "SSC KIRAN REASONING pdf",
            difficulty = "Easy",
            year = null,
            examDate = null,
            shift = null,
            examName = "SSC KIRAN REASONING pdf",
            isBookmarked = false
        ),
        QuestionEntity(
            questionText = "Find the set most like (4,10,15).",
            optionA = "(3,6,12)",
            optionB = "(2,8,10)",
            optionC = "(5,12,18)",
            optionD = "(7,10,18)",
            correctOption = 3,
            explanation = "+6 then +5, next +7 then +6.",
            subject = "Reasoning",
            topic = "Analogy",
            subtopic = "SSC KIRAN REASONING pdf",
            difficulty = "Easy",
            year = null,
            examDate = null,
            shift = null,
            examName = "SSC KIRAN REASONING pdf",
            isBookmarked = false
        ),
        QuestionEntity(
            questionText = "set most like (6,36,63).",
            optionA = "(7,49,98)",
            optionB = "(8,64,46)",
            optionC = "(9,84,45)",
            optionD = "(11,111,84)",
            correctOption = 2,
            explanation = "number squared करो, फिर उसके digits को reverse करो.",
            subject = "Reasoning",
            topic = "Analogy",
            subtopic = "SSC KIRAN REASONING pdf",
            difficulty = "Easy",
            year = null,
            examDate = null,
            shift = null,
            examName = "SSC KIRAN REASONING pdf",
            isBookmarked = false
        )
    )
}
