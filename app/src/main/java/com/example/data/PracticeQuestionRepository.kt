package com.example.data

import com.example.data.models.Difficulty
import com.example.data.models.PracticeQuestion
import com.example.data.models.SscDataRepository
import com.example.data.models.Subtopic

object PracticeQuestionRepository {

    fun getQuestionsForTopic(
        subjectId: String,
        subjectTitle: String,
        topicId: String,
        topicTitle: String
    ): List<PracticeQuestion> {
        val topic = SscDataRepository.getTopicById(subjectId, topicId)
        val subtopics: List<Subtopic> = topic?.subtopics ?: emptyList()

        return when {
            subjectId == "maths" && topicId == "number_system" -> getMathNumberSystemQuestions(subjectTitle, topicTitle, subtopics)
            subjectId == "maths" && topicId == "percentage_profit" -> getMathPercentageQuestions(subjectTitle, topicTitle, subtopics)
            subjectId == "maths" -> getGenericTopicQuestions(subjectId, subjectTitle, topicId, topicTitle, subtopics, "maths")
            subjectId == "reasoning" -> getGenericTopicQuestions(subjectId, subjectTitle, topicId, topicTitle, subtopics, "reasoning")
            subjectId == "english" -> getGenericTopicQuestions(subjectId, subjectTitle, topicId, topicTitle, subtopics, "english")
            subjectId == "gk_ga" -> getGenericTopicQuestions(subjectId, subjectTitle, topicId, topicTitle, subtopics, "gk_ga")
            else -> getMathPercentageQuestions(subjectTitle, topicTitle, subtopics)
        }
    }

    fun getQuestionsForSubtopic(
        subjectId: String,
        subjectTitle: String,
        topicId: String,
        topicTitle: String,
        subtopicId: String,
        subtopicTitle: String
    ): List<PracticeQuestion> {
        return getQuestionsForTopic(subjectId, subjectTitle, topicId, topicTitle)
    }

    fun getQuestionsForSubject(subjectId: String): List<PracticeQuestion> {
        val subject = SscDataRepository.getSubjectById(subjectId)
        val subjectTitle = subject?.title ?: when (subjectId) {
            "maths" -> "Quantitative Aptitude"
            "reasoning" -> "General Intelligence & Reasoning"
            "english" -> "English Comprehension"
            "gk_ga" -> "General Awareness"
            else -> "Subject"
        }

        return when (subjectId) {
            "maths" -> {
                val numSys = getMathNumberSystemQuestions(subjectTitle, "Number System", emptyList())
                val perc = getMathPercentageQuestions(subjectTitle, "Percentage & Profit", emptyList())
                (numSys.take(13) + perc.take(12)).mapIndexed { index, q ->
                    q.copy(id = index + 1, questionNumber = index + 1, subjectTitle = subjectTitle)
                }
            }
            "reasoning" -> {
                getReasoningQuestions(subjectTitle, "General Reasoning", "General", "gen_reas").take(25).mapIndexed { index, q ->
                    q.copy(id = index + 1, questionNumber = index + 1, subjectTitle = subjectTitle)
                }
            }
            "english" -> {
                getEnglishQuestions(subjectTitle, "English Language", "General", "gen_eng").take(25).mapIndexed { index, q ->
                    q.copy(id = index + 1, questionNumber = index + 1, subjectTitle = subjectTitle)
                }
            }
            "gk_ga" -> {
                getGkQuestions(subjectTitle, "General Knowledge", "General", "gen_gk").take(25).mapIndexed { index, q ->
                    q.copy(id = index + 1, questionNumber = index + 1, subjectTitle = subjectTitle)
                }
            }
            else -> {
                val numSys = getMathNumberSystemQuestions(subjectTitle, "Maths", emptyList())
                numSys.take(25).mapIndexed { index, q ->
                    q.copy(id = index + 1, questionNumber = index + 1, subjectTitle = subjectTitle)
                }
            }
        }
    }

    fun getQuestionsForCustomTest(subjectIds: List<String>): List<PracticeQuestion> {
        val allQuestions = mutableListOf<PracticeQuestion>()
        var globalIndex = 1
        for (subId in subjectIds) {
            val subQuestions = getQuestionsForSubject(subId)
            for (q in subQuestions) {
                allQuestions.add(
                    q.copy(
                        id = globalIndex,
                        questionNumber = globalIndex
                    )
                )
                globalIndex++
            }
        }
        return allQuestions
    }

    private fun getMathNumberSystemQuestions(
        subjectTitle: String,
        topicTitle: String,
        subtopics: List<com.example.data.models.Subtopic>
    ): List<PracticeQuestion> {
        fun sub(index: Int) = subtopics.getOrNull(index) ?: com.example.data.models.Subtopic("sub_num_gen", "Number System Basics", "")

        return listOf(
            // Subtopic 0: Divisibility Rules
            PracticeQuestion(1, 1, "If the 7-digit number 5432a18 is divisible by 9, what is the value of digit 'a'?", listOf("2", "4", "3", "5"), 1, "For a number to be divisible by 9, the sum of its digits must be a multiple of 9.\n5 + 4 + 3 + 2 + a + 1 + 8 = 23 + a.\nThe next multiple of 9 is 27.\n23 + a = 27 ⇒ a = 4.", Difficulty.EASY, subjectTitle, topicTitle, sub(0).title, sub(0).id, shortcutMethod = "⚡ Divisibility Rule of 9: Sum of digits must be divisible by 9. Sum = 23 + a. Nearest multiple = 27 ⇒ a = 4."),
            PracticeQuestion(2, 2, "If an 8-digit number 789x531y is divisible by 72, find the value of (5x - 3y) for the largest possible value of y.", listOf("23", "27", "29", "25"), 0, "For divisibility by 72, number must be divisible by 8 and 9.\nDivisibility by 8: Last 3 digits '31y' must be divisible by 8.\n312 / 8 = 39 (y = 2). Testing y = 2: 312 is divisible. Next is y = 2 + 8 = none in single digit. So y = 2.\nDivisibility by 9: Sum of digits = 7 + 8 + 9 + x + 5 + 3 + 1 + 2 = 35 + x.\nFor multiple of 9 (36), x = 1.\nThen 5x - 3y = 5(7) - 3(2)...", Difficulty.HARD, subjectTitle, topicTitle, sub(0).title, sub(0).id, shortcutMethod = "⚡ Rule of 72: Check last 3 digits for 8 first (31y → 312, y=2), then sum of digits for 9 (35+x ⇒ x=1)."),
            PracticeQuestion(3, 3, "Which of the following numbers is completely divisible by 11?", listOf("4832718", "4832715", "4832726", "4832719"), 0, "Divisibility by 11: (Sum of odd places) - (Sum of even places) must be 0 or a multiple of 11.\nFor 4832718: (8 + 7 + 3 + 4) - (1 + 2 + 8) = 22 - 11 = 11, which is divisible by 11.", Difficulty.EASY, subjectTitle, topicTitle, sub(0).title, sub(0).id, shortcutMethod = "⚡ Alternating Sum Rule: (Sum of Odd digits) - (Sum of Even digits) = 0 or multiple of 11. Here: 22 - 11 = 11."),
            PracticeQuestion(4, 4, "What is the smallest number that must be added to 1056 so that the sum is completely divisible by 23?", listOf("2", "3", "18", "21"), 0, "Dividing 1056 by 23:\n1056 = 23 × 45 + 21 (Remainder = 21).\nRequired number to add = 23 - 21 = 2.\n1056 + 2 = 1058 (divisible by 23).", Difficulty.EASY, subjectTitle, topicTitle, sub(0).title, sub(0).id, shortcutMethod = "⚡ Remainder Method: 1056 mod 23 = 21. Number to add = Divisor - Remainder = 23 - 21 = 2."),

            // Subtopic 1: Unit Digit & Cyclicity
            PracticeQuestion(5, 5, "Find the unit digit in the product: (2467)¹⁵³ × (341)⁷².", listOf("7", "1", "3", "9"), 0, "Cyclicity of 7 is 4 (7¹=7, 7²=9, 7³=3, 7⁴=1).\n153 mod 4 = 1 ⇒ Unit digit of (2467)¹⁵³ is 7¹ = 7.\nUnit digit of (341)⁷² is 1.\nResulting unit digit = 7 × 1 = 7.", Difficulty.EASY, subjectTitle, topicTitle, sub(1).title, sub(1).id, shortcutMethod = "⚡ Cyclicity Trick: 153 mod 4 = 1 ⇒ 7¹ = 7. Unit digit of 1 is always 1. Product = 7 × 1 = 7."),
            PracticeQuestion(6, 6, "What is the unit digit of the expression (7⁹⁵ - 3⁵⁸)?", listOf("4", "6", "0", "7"), 0, "For 7⁹⁵: 95 mod 4 = 3 ⇒ 7³ = 343 (unit digit = 3).\nFor 3⁵⁸: 58 mod 4 = 2 ⇒ 3² = 9 (unit digit = 9).\nUnit digit = 13 - 9 = 4 (borrowing 10 from tens place).", Difficulty.MODERATE, subjectTitle, topicTitle, sub(1).title, sub(1).id, shortcutMethod = "⚡ Power mod 4: 95 mod 4 = 3 (7³ → 3) and 58 mod 4 = 2 (3² → 9). Borrow 10: 13 - 9 = 4."),
            PracticeQuestion(7, 7, "Determine the unit digit in (137¹³)²⁴.", listOf("1", "7", "9", "3"), 0, "Power is 13 × 24 = 312.\n312 mod 4 = 0 (full cycle of 4).\n7⁴ unit digit is 1.\nTherefore, the unit digit is 1.", Difficulty.MODERATE, subjectTitle, topicTitle, sub(1).title, sub(1).id, shortcutMethod = "⚡ Power product: 13 × 24 is directly a multiple of 4 ⇒ unit digit is 7⁴ = 1."),
            PracticeQuestion(8, 8, "Find the unit digit of 1! + 2! + 3! + 4! + ... + 100!.", listOf("3", "5", "7", "0"), 0, "For n ≥ 5, n! ends in 0 (contains factors 2 and 5).\nUnit digit is determined solely by 1! + 2! + 3! + 4!:\n1 + 2 + 6 + 24 = 33 ⇒ Unit digit is 3.", Difficulty.MODERATE, subjectTitle, topicTitle, sub(1).title, sub(1).id, shortcutMethod = "⚡ Factorial Rule: All n! for n ≥ 5 end in 0. Only calculate 1! + 2! + 3! + 4! = 33 ⇒ 3."),

            // Subtopic 2: Remainder Theorems
            PracticeQuestion(9, 9, "Find the remainder when (67⁶⁷ + 67) is divided by 68.", listOf("66", "67", "1", "0"), 0, "By modular arithmetic: 67 ≡ -1 (mod 68).\n(-1)⁶⁷ + (-1) = -1 - 1 = -2.\nRemainder = 68 - 2 = 66.", Difficulty.MODERATE, subjectTitle, topicTitle, sub(2).title, sub(2).id, shortcutMethod = "⚡ Negative Remainder Trick: 67 ≡ -1 (mod 68). (-1)⁶⁷ + (-1) = -2 ≡ 68 - 2 = 66."),
            PracticeQuestion(10, 10, "A number when divided by 899 gives a remainder 63. If the same number is divided by 29, what will be the remainder?", listOf("5", "4", "2", "3"), 0, "Since 899 is completely divisible by 29 (899 = 29 × 31), we simply divide the remainder 63 by 29.\n63 = 29 × 2 + 5.\nTherefore, remainder is 5.", Difficulty.EASY, subjectTitle, topicTitle, sub(2).title, sub(2).id, shortcutMethod = "⚡ Direct Remainder Division: Since 899 is a multiple of 29, just do 63 mod 29 = 5 directly!"),
            PracticeQuestion(11, 11, "What is the remainder when 2³¹ is divided by 5?", listOf("3", "2", "1", "4"), 0, "2⁴ = 16 ≡ 1 (mod 5).\n2³¹ = (2⁴)⁷ × 2³ = (1)⁷ × 8 = 8 ≡ 3 (mod 5).\nRemainder is 3.", Difficulty.MODERATE, subjectTitle, topicTitle, sub(2).title, sub(2).id, shortcutMethod = "⚡ Fermat/Euler Theorem: 2⁴ ≡ 1 (mod 5). 2³¹ = (2⁴)⁷ × 2³ ≡ 1 × 8 ≡ 3 (mod 5)."),
            PracticeQuestion(12, 12, "Two numbers when divided by a certain divisor leave remainders 43 and 37 respectively. When their sum is divided by the same divisor, the remainder is 13. Find the divisor.", listOf("67", "65", "57", "71"), 0, "Divisor formula = Remainder 1 + Remainder 2 - Remainder 3\n= 43 + 37 - 13 = 80 - 13 = 67.", Difficulty.MODERATE, subjectTitle, topicTitle, sub(2).title, sub(2).id, shortcutMethod = "⚡ Direct Formula: Divisor = R₁ + R₂ - R₃ = 43 + 37 - 13 = 67 in 5 seconds."),

            // Subtopic 3: LCM & HCF
            PracticeQuestion(13, 13, "The LCM of two numbers is 864 and their HCF is 144. If one number is 288, find the other number.", listOf("432", "576", "144", "360"), 0, "Product of two numbers = LCM × HCF\n288 × Other = 864 × 144\nOther = (864 × 144) / 288 = 864 / 2 = 432.", Difficulty.EASY, subjectTitle, topicTitle, sub(3).title, sub(3).id, shortcutMethod = "⚡ Formula: Other number = (LCM × HCF) / Given number = (864 × 144) / 288 = 432."),
            PracticeQuestion(14, 14, "Find the greatest number that will divide 390, 495, and 300 without leaving a remainder.", listOf("15", "5", "25", "30"), 0, "Required number = HCF(390, 495, 300).\n300 = 15 × 20\n390 = 15 × 26\n495 = 15 × 33\nHCF is 15.", Difficulty.EASY, subjectTitle, topicTitle, sub(3).title, sub(3).id, shortcutMethod = "⚡ Difference Method: Difference between 390 & 300 is 90. Difference between 495 & 390 is 105. HCF(90, 105) = 15."),
            PracticeQuestion(15, 15, "Three bells toll together at intervals of 9, 12, 15 minutes respectively. If they start tolling together, after what time will they next toll together?", listOf("3 hours", "2 hours", "4 hours", "6 hours"), 0, "LCM of 9, 12, 15:\n9 = 3²; 12 = 2² × 3; 15 = 3 × 5\nLCM = 2² × 3² × 5 = 4 × 9 × 5 = 180 minutes = 3 hours.", Difficulty.EASY, subjectTitle, topicTitle, sub(3).title, sub(3).id, shortcutMethod = "⚡ Toll Together Shortcut: Always find LCM of given intervals. LCM(9,12,15) = 180 mins = 3 hrs."),
            PracticeQuestion(16, 16, "The ratio of two numbers is 3 : 4 and their HCF is 4. Find their LCM.", listOf("48", "36", "24", "60"), 0, "Numbers are 3 × 4 = 12 and 4 × 4 = 16.\nLCM = 4 × 3 × 4 = 48.", Difficulty.EASY, subjectTitle, topicTitle, sub(3).title, sub(3).id, shortcutMethod = "⚡ Ratio Trick: LCM = HCF × a × b = 4 × 3 × 4 = 48."),

            // Subtopic 4: Surds & Indices
            PracticeQuestion(17, 17, "Which of the following is the greatest: ∛4, √2, ⁶√3, ⁴√5?", listOf("∛4", "⁴√5", "√2", "⁶√3"), 0, "LCM of root powers (3, 2, 6, 4) is 12.\n∛4 = 4^(4/12) = (256)^(1/12)\n√2 = 2^(6/12) = (64)^(1/12)\n⁶√3 = 3^(2/12) = (9)^(1/12)\n⁴√5 = 5^(3/12) = (125)^(1/12)\n256 is the greatest, so ∛4 is largest.", Difficulty.MODERATE, subjectTitle, topicTitle, sub(4).title, sub(4).id, shortcutMethod = "⚡ Equalize Powers: Raise all to LCM power 12: 4⁴=256, 2⁶=64, 3²=9, 5³=125. 256 is largest."),
            PracticeQuestion(18, 18, "Simplify the expression: √(7 + 2√10).", listOf("√5 + √2", "√5 - √2", "√6 + 1", "√7 + √3"), 0, "7 + 2√10 = 5 + 2 + 2√(5×2) = (√5 + √2)².\nTaking square root gives √5 + √2.", Difficulty.EASY, subjectTitle, topicTitle, sub(4).title, sub(4).id, shortcutMethod = "⚡ Split 10 into factors summing to 7: 5 × 2 = 10, 5 + 2 = 7 ⇒ Result is simply √5 + √2."),
            PracticeQuestion(19, 19, "If 2^(x-1) + 2^(x+1) = 320, find the value of x.", listOf("7", "6", "8", "5"), 0, "2^(x-1) × (1 + 2²) = 320\n2^(x-1) × 5 = 320\n2^(x-1) = 64 = 2⁶\nx - 1 = 6 ⇒ x = 7.", Difficulty.MODERATE, subjectTitle, topicTitle, sub(4).title, sub(4).id, shortcutMethod = "⚡ Factor out common power: 2^(x-1) × (1 + 4) = 320 ⇒ 2^(x-1) = 64 = 2⁶ ⇒ x = 7."),
            PracticeQuestion(20, 20, "Evaluate: 1 / (√9 - √8) - 1 / (√8 - √7) + 1 / (√7 - √6) - 1 / (√6 - √5) + 1 / (√5 - √4).", listOf("5", "4", "3", "1"), 0, "Rationalizing each term:\n(√9 + √8) - (√8 + √7) + (√7 + √6) - (√6 + √5) + (√5 + √4)\nTelescoping series simplifies to: √9 + √4 = 3 + 2 = 5.", Difficulty.HARD, subjectTitle, topicTitle, sub(4).title, sub(4).id, shortcutMethod = "⚡ Telescoping Cancellation: In alternating surd series, all middle terms cancel: First + Last = √9 + √4 = 3 + 2 = 5.")
        )
    }

    private fun getMathPercentageQuestions(
        subjectTitle: String,
        topicTitle: String,
        subtopics: List<com.example.data.models.Subtopic>
    ): List<PracticeQuestion> {
        fun sub(index: Int) = subtopics.getOrNull(index) ?: com.example.data.models.Subtopic("sub_perc_gen", "Percentage Basics", "")

        return listOf(
            // Subtopic 0: Fraction to Percentage
            PracticeQuestion(1, 1, "Convert the fraction 3/8 into its percentage equivalent.", listOf("35.5%", "37.5%", "36.5%", "38.0%"), 1, "To convert a fraction into percentage, multiply by 100%:\n(3/8) × 100% = 300 / 8 = 37.5%.\nAlternatively, since 1/8 = 12.5%, 3/8 = 3 × 12.5% = 37.5%.", Difficulty.EASY, subjectTitle, topicTitle, sub(0).title, sub(0).id, shortcutMethod = "⚡ Standard Fraction Table: 1/8 = 12.5% ⇒ 3/8 = 3 × 12.5% = 37.5% in 2 seconds."),
            PracticeQuestion(2, 2, "If 16⅔% of a number is added to itself, the result becomes 4956. Find the original number.", listOf("4248", "4120", "4320", "4050"), 0, "Fraction equivalent of 16⅔% is 1/6.\nLet the original number be 6 units.\nWhen 1 unit is added, it becomes 6 + 1 = 7 units.\n7 units = 4956 ⇒ 1 unit = 708.\nOriginal number = 6 × 708 = 4248.", Difficulty.MODERATE, subjectTitle, topicTitle, sub(0).title, sub(0).id, shortcutMethod = "⚡ Ratio Method: 16⅔% = 1/6. Initial = 6 units, New = 7 units. 7 units = 4956 ⇒ 6 units = 6 × 708 = 4248."),
            PracticeQuestion(3, 3, "What is the simplest fractional equivalent of 83⅓%?", listOf("5/6", "4/5", "7/8", "3/4"), 0, "83⅓% = 250/3%\nIn fraction: (250 / 3) / 100 = 250 / 300 = 5/6.\n(Alternatively: 100% - 16⅔% = 1 - 1/6 = 5/6).", Difficulty.EASY, subjectTitle, topicTitle, sub(0).title, sub(0).id, shortcutMethod = "⚡ Complementary Trick: 83⅓% = 100% - 16⅔% = 1 - 1/6 = 5/6."),
            PracticeQuestion(4, 4, "A candidate scored 32% marks in an examination and failed by 12 marks. Another candidate scored 42% marks and secured 28 marks more than the passing marks. Find the passing percentage.", listOf("35%", "36%", "34%", "38%"), 0, "Difference in percentages = 42% - 32% = 10%.\nDifference in marks = 28 - (-12) = 40 marks.\n10% = 40 marks ⇒ Maximum marks = 400.\nPassing marks = (32% of 400) + 12 = 128 + 12 = 140 marks.\nPassing percentage = (140 / 400) × 100% = 35%.", Difficulty.MODERATE, subjectTitle, topicTitle, sub(0).title, sub(0).id, shortcutMethod = "⚡ Gap Method: % gap = 10%, Marks gap = 40. 10% = 40 marks ⇒ 1% = 4 marks. 12 marks = 3%. Passing % = 32% + 3% = 35%."),

            // Subtopic 1: Successive Percentage
            PracticeQuestion(5, 5, "If the price of sugar is increased by 25%, by what percentage should a household reduce its consumption so that total expenditure remains unchanged?", listOf("25%", "20%", "16.67%", "30%"), 1, "Reduction in consumption = [r / (100 + r)] × 100%\n= [25 / (100 + 25)] × 100% = (25 / 125) × 100% = (1/5) × 100% = 20%.\nTherefore, consumption must decrease by 20%.", Difficulty.EASY, subjectTitle, topicTitle, sub(1).title, sub(1).id, shortcutMethod = "⚡ Golden Formula: [r / (100 + r)] × 100% = [25 / 125] × 100% = 1/5 = 20%."),
            PracticeQuestion(6, 6, "The population of a city increases by 10% in the first year and decreases by 10% in the second year. If the current population is 49,500, what was the initial population 2 years ago?", listOf("50,000", "55,000", "48,000", "52,000"), 0, "Net successive change over 2 years = +10 - 10 - (10 × 10 / 100) = -1%.\nCurrent population = 99% of original population.\n0.99 × P = 49,500 ⇒ P = 50,000.", Difficulty.MODERATE, subjectTitle, topicTitle, sub(1).title, sub(1).id, shortcutMethod = "⚡ Net change for +x% and -x%: Always a loss of x²/100% = 100/100 = 1% loss. 99% = 49,500 ⇒ 100% = 50,000."),
            PracticeQuestion(7, 7, "If A's income is 25% more than B's income, then by what percentage is B's income less than A's income?", listOf("25%", "20%", "16.67%", "33.33%"), 1, "Let B's income = 100.\nA's income = 125.\nPercentage less than A = (25 / 125) × 100% = 20%.", Difficulty.EASY, subjectTitle, topicTitle, sub(1).title, sub(1).id, shortcutMethod = "⚡ Direct Fraction: +25% = +1/4. The corresponding reduction is always 1/(4+1) = 1/5 = 20%."),
            PracticeQuestion(8, 8, "The price of an article was reduced by 20% and its sales volume increased by 80%. What was the net percentage change in total revenue?", listOf("44% increase", "60% increase", "44% decrease", "36% increase"), 0, "Net effect on revenue = a + b + (ab / 100)\n= -20 + 80 + [(-20 × 80) / 100] = 60 - 16 = +44% increase.", Difficulty.MODERATE, subjectTitle, topicTitle, sub(1).title, sub(1).id, shortcutMethod = "⚡ Successive Formula: a + b + (ab/100) = -20 + 80 - 16 = +44% increase."),

            // Subtopic 2: Cost Price, Selling Price & Profit
            PracticeQuestion(9, 9, "By selling an article for ₹840, a trader incurs a loss of 4%. At what price should he sell the article to gain 12%?", listOf("₹960", "₹980", "₹950", "₹1000"), 1, "Cost Price (CP) = 840 / 0.96 = ₹875.\nTarget Selling Price at 12% profit = 875 × 1.12 = ₹980.", Difficulty.MODERATE, subjectTitle, topicTitle, sub(2).title, sub(2).id, shortcutMethod = "⚡ Proportion Shortcut: SP₂ = SP₁ × (100 + g%) / (100 - l%) = 840 × 112 / 96 = ₹980."),
            PracticeQuestion(10, 10, "If the cost price of 15 articles is equal to the selling price of 12 articles, calculate the profit percentage.", listOf("20%", "25%", "30%", "15%"), 1, "15 × CP = 12 × SP ⇒ SP / CP = 5 / 4.\nProfit % = (1 / 4) × 100% = 25%.", Difficulty.EASY, subjectTitle, topicTitle, sub(2).title, sub(2).id, shortcutMethod = "⚡ Ratio Trick: 15 CP = 12 SP ⇒ SP/CP = 5/4. Profit = (5 - 4)/4 = 1/4 = 25%."),
            PracticeQuestion(11, 11, "An article is sold at a 15% profit. If it had been sold for ₹27 more, the profit would have been 20%. What is the cost price of the article?", listOf("₹540", "₹500", "₹600", "₹480"), 0, "5% of CP = ₹27 ⇒ CP = (27 / 5) × 100 = ₹540.", Difficulty.EASY, subjectTitle, topicTitle, sub(2).title, sub(2).id, shortcutMethod = "⚡ % Difference: 20% - 15% = 5%. 5% of CP = ₹27 ⇒ 100% CP = 27 × 20 = ₹540."),
            PracticeQuestion(12, 12, "When a radio is sold for ₹720, the seller incurs a loss of 25%. What should the selling price be to earn a profit of 25%?", listOf("₹1200", "₹1150", "₹1100", "₹1250"), 0, "75% of CP = ₹720 ⇒ CP = ₹960.\nSelling Price for 25% profit = 960 × 1.25 = ₹1200.", Difficulty.EASY, subjectTitle, topicTitle, sub(2).title, sub(2).id, shortcutMethod = "⚡ Ratio method: 75% = 720 ⇒ 125% = (720 / 75) × 125 = (720 / 3) × 5 = ₹1200."),

            // Subtopic 3: Marked Price & Discounts
            PracticeQuestion(13, 13, "Two successive discounts of 20% and 15% on the marked price of an article are equivalent to a single discount of:", listOf("35%", "32%", "30%", "33%"), 1, "Net Discount = 20 + 15 - (20 × 15 / 100) = 35 - 3 = 32%.", Difficulty.EASY, subjectTitle, topicTitle, sub(3).title, sub(3).id, shortcutMethod = "⚡ Single Equivalent Discount: d₁ + d₂ - (d₁d₂/100) = 20 + 15 - 3 = 32%."),
            PracticeQuestion(14, 14, "The marked price of an article is set 40% above its cost price. What percentage discount can be offered on the marked price to still earn a net profit of 12%?", listOf("15%", "20%", "25%", "18%"), 1, "Let CP = 100 ⇒ MP = 140. Required SP = 112.\nDiscount = 140 - 112 = 28.\nDiscount % = (28 / 140) × 100% = 20%.", Difficulty.MODERATE, subjectTitle, topicTitle, sub(3).title, sub(3).id, shortcutMethod = "⚡ CP:MP Formula: (100 - D) / (100 + P) = CP/MP = 100/140 ⇒ 100 - D = 80 ⇒ D = 20%."),
            PracticeQuestion(15, 15, "Find the single discount equivalent to three successive discounts of 10%, 20%, and 30%.", listOf("49.6%", "50.4%", "60.0%", "48.2%"), 0, "Final multiplier = 0.90 × 0.80 × 0.70 = 0.504.\nNet equivalent discount = 1 - 0.504 = 49.6%.", Difficulty.MODERATE, subjectTitle, topicTitle, sub(3).title, sub(3).id, shortcutMethod = "⚡ Multiplier Trick: 0.9 × 0.8 × 0.7 = 0.504. Discount = 1 - 0.504 = 49.6%."),
            PracticeQuestion(16, 16, "An article is marked at ₹500. A customer purchases it for ₹382.50 after two successive discounts, of which the first discount is 15%. Determine the second discount percentage.", listOf("10%", "12%", "8%", "15%"), 0, "Price after 1st discount = 500 - 75 = ₹425.\nSecond discount = 425 - 382.50 = ₹42.50.\nSecond discount % = (42.50 / 425) × 100% = 10%.", Difficulty.HARD, subjectTitle, topicTitle, sub(3).title, sub(3).id, shortcutMethod = "⚡ After 1st discount price is 425. Gap to 382.50 is exactly 42.50, which is 10% of 425."),

            // Subtopic 4: Dishonest Dealer & Faulty Weights
            PracticeQuestion(17, 17, "A dishonest merchant claims to sell goods at cost price, but uses a false weight of 900 grams for a 1 kg (1000g) weight. Find his actual gain percentage.", listOf("10%", "11 1/9%", "12.5%", "9 1/11%"), 1, "Gain % = [Error / (True Weight - Error)] × 100% = (100 / 900) × 100% = 11 1/9%.", Difficulty.MODERATE, subjectTitle, topicTitle, sub(4).title, sub(4).id, shortcutMethod = "⚡ Faulty Weight Shortcut: [Error / Given Weight] × 100% = [100 / 900] × 100% = 1/9 = 11 1/9%."),
            PracticeQuestion(18, 18, "A grocer professes to sell at an 8% profit but uses a 900g weight instead of 1kg. Find his overall profit percentage.", listOf("20%", "22%", "18%", "25%"), 0, "Effective gain multiplier = (108 / 100) × (1000 / 900) = 1.08 × 1.111 = 1.20 ⇒ 20% overall profit.", Difficulty.HARD, subjectTitle, topicTitle, sub(4).title, sub(4).id, shortcutMethod = "⚡ Combined Ratio: (108/100) × (1000/900) = 108/90 = 1.20 ⇒ 20% overall profit."),
            PracticeQuestion(19, 19, "A trader buys 80 kg of rice at ₹13.50 per kg and 120 kg of rice at ₹16.00 per kg. At what rate per kg should he sell the mixture to achieve an overall profit of 20%?", listOf("₹18.00", "₹17.50", "₹18.20", "₹16.80"), 0, "Total CP = (80 × 13.50) + (120 × 16.00) = 1080 + 1920 = ₹3000.\nAverage CP per kg = 3000 / 200 = ₹15.00.\nSP per kg for 20% profit = 15 × 1.20 = ₹18.00.", Difficulty.MODERATE, subjectTitle, topicTitle, sub(4).title, sub(4).id, shortcutMethod = "⚡ Weighted Average: (2 × 13.50 + 3 × 16.00) / 5 = (27 + 48) / 5 = ₹15/kg. SP = 15 × 1.2 = ₹18.00."),
            PracticeQuestion(20, 20, "In a class examination, 65% of students passed in Mathematics, 48% passed in English, and 30% passed in both subjects. What percentage of students failed in both subjects?", listOf("15%", "17%", "12%", "18%"), 1, "Passing at least one = 65% + 48% - 30% = 83%.\nFailing both = 100% - 83% = 17%.", Difficulty.EASY, subjectTitle, topicTitle, sub(4).title, sub(4).id, shortcutMethod = "⚡ Set Formula: Failed in both = 100 - (65 + 48 - 30) = 100 - 83 = 17%.")
        )
    }

    private fun getGenericTopicQuestions(
        subjectId: String,
        subjectTitle: String,
        topicId: String,
        topicTitle: String,
        subtopics: List<com.example.data.models.Subtopic>,
        category: String
    ): List<PracticeQuestion> {
        val baseList = when (category) {
            "reasoning" -> getReasoningQuestions(subjectTitle, topicTitle, "", "")
            "english" -> getEnglishQuestions(subjectTitle, topicTitle, "", "")
            "gk_ga" -> getGkQuestions(subjectTitle, topicTitle, "", "")
            else -> getMathPercentageQuestions(subjectTitle, topicTitle, subtopics)
        }

        return baseList.mapIndexed { index, q ->
            val assignedSubtopic = if (subtopics.isNotEmpty()) {
                subtopics[index % subtopics.size]
            } else {
                com.example.data.models.Subtopic("sub_default", topicTitle, "")
            }
            q.copy(
                id = index + 1,
                questionNumber = index + 1,
                subjectTitle = subjectTitle,
                topicTitle = topicTitle,
                subtopicTitle = assignedSubtopic.title,
                subtopicId = assignedSubtopic.id
            )
        }
    }

    private fun getReasoningQuestions(
        subjectTitle: String,
        topicTitle: String,
        subtopicTitle: String,
        subtopicId: String
    ): List<PracticeQuestion> {
        return listOf(
            PracticeQuestion(1, 1, "Select the related number: 7 : 56 :: 9 : ?", listOf("72", "81", "90", "99"), 2, "Pattern is n : n × (n + 1). 7 × 8 = 56, so 9 × 10 = 90 (or 9² + 9 = 90).", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Pattern Trick: n(n + 1) → 7 × 8 = 56, thus 9 × 10 = 90."),
            PracticeQuestion(2, 2, "If CAT = 24 and DOG = 26, then BIRD = ?", listOf("36", "38", "34", "40"), 0, "Sum of alphabet positions: B(2) + I(9) + R(18) + D(4) = 33 + 3 = 36.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Place Value Sum: B(2) + I(9) + R(18) + D(4) = 33 + 3 = 36."),
            PracticeQuestion(3, 3, "Find the missing number in the series: 4, 9, 19, 39, 79, ?", listOf("159", "169", "149", "158"), 0, "Pattern is ×2 + 1: 4×2+1=9, 9×2+1=19, 19×2+1=39, 39×2+1=79, 79×2+1 = 159.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Series Pattern: 2n + 1 → 79 × 2 + 1 = 158 + 1 = 159."),
            PracticeQuestion(4, 4, "Pointing to a photograph, Rohit said, 'She is the daughter of my grandfather's only son.' How is Rohit related to the girl?", listOf("Sister", "Brother", "Father", "Cousin"), 1, "Grandfather's only son = Rohit's father. Father's daughter = Rohit's sister. So Rohit is her brother.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Blood Relation Tree: Grandfather's only son = Father → Father's daughter = Sister → Rohit is Brother."),
            PracticeQuestion(5, 5, "Find the odd one out: (A) 125, (B) 216, (C) 343, (D) 512, where one is also a square.", listOf("125", "216", "343", "512"), 0, "125 = 5³ (odd cube), 216 = 6³, 343 = 7³, 512 = 8³ = 16² (both square and cube). Or 125 is 5³ while 216, 512 are even.", Difficulty.MODERATE, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Parity & Square Check: 125 is 5³ (pure odd base), while others have even bases or dual powers."),
            PracticeQuestion(6, 6, "A man walks 5 km East, turns right and walks 4 km, then turns left and walks 5 km. In which direction is he now from his starting point?", listOf("South-East", "North-East", "South-West", "East"), 0, "Starting from (0,0): (5,0) -> (5,-4) -> (10,-4). Point (10,-4) is South-East of origin.", Difficulty.MODERATE, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Coordinate Method: (+5, 0) + (0, -4) + (+5, 0) = (+10, -4) → (+x, -y) is South-East."),
            PracticeQuestion(7, 7, "Statements: All Cups are Plates. Some Plates are Spoons. Conclusion: I. Some Cups are Spoons. II. Some Plates are Cups.", listOf("Only I follows", "Only II follows", "Both follow", "Neither follows"), 1, "Since All Cups are Plates, Some Plates are Cups (II is definitely true). Cup and Spoon relationship is only a possibility.", Difficulty.MODERATE, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Syllogism Rule: 'All A are B' directly implies 'Some B are A'. Independent disjoint remains unconfirmed."),
            PracticeQuestion(8, 8, "Complete the sequence: B2D, D4F, F8H, H16J, ?", listOf("J32L", "J30L", "K32L", "J32M"), 0, "Letters increase by +2: B->D->F->H->J and D->F->H->J->L. Numbers double: 2, 4, 8, 16, 32 -> J32L.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Dual Progression: Letters +2 (H→J, J→L), Numbers ×2 (16×2=32) ⇒ J32L."),
            PracticeQuestion(9, 9, "If '+' means '÷', '×' means '+', '÷' means '-', '-' means '×', solve: 36 + 6 - 3 × 5 ÷ 2", listOf("21", "23", "20", "18"), 0, "Replacing operators: 36 ÷ 6 × 3 + 5 - 2 = (6 × 3) + 5 - 2 = 18 + 5 - 2 = 21.", Difficulty.MODERATE, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ BODMAS Substitution: (36 / 6) × 3 + 5 - 2 = 18 + 3 = 21."),
            PracticeQuestion(10, 10, "In a row of 40 students, Aman is 13th from the left. What is his position from the right end?", listOf("27th", "28th", "29th", "26th"), 1, "Position from right = (Total - Position from left) + 1 = (40 - 13) + 1 = 28th.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Ranking Formula: Right = Total - Left + 1 = 40 - 13 + 1 = 28th."),
            PracticeQuestion(11, 11, "Select the related word: Virology : Virus :: Entomology : ?", listOf("Insects", "Birds", "Plants", "Fungi"), 0, "Virology is study of viruses, Entomology is the study of insects.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Root Word: Entomo- (Greek for insect) + logy (study) = Study of insects."),
            PracticeQuestion(12, 12, "If 'ROAD' is coded as 'URDG', how will 'SWAN' be coded?", listOf("VZDG", "VZCQ", "VZDP", "UXDQ"), 1, "Each letter is shifted forward by +3: S+3=V, W+3=Z, A+3=D (or Z if cyclic), N+3=Q -> VZDQ / VZCQ.", Difficulty.MODERATE, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Constant Shift: Add +3 to each letter: S(19)+3=V, W(23)+3=Z, A(1)+3=D/C, N(14)+3=Q."),
            PracticeQuestion(13, 13, "Which number replaces '?' in: 3, 5, 10, 12, 24, 26, ?", listOf("52", "48", "50", "54"), 0, "Pattern is +2, ×2, +2, ×2, +2, ×2: 26 × 2 = 52.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Alternating Operations: +2, ×2, +2, ×2... After 26 (+2), perform ×2: 26 × 2 = 52."),
            PracticeQuestion(14, 14, "If 1st January 2024 was a Monday, what day was 1st January 2025? (Note: 2024 is a leap year)", listOf("Tuesday", "Wednesday", "Thursday", "Sunday"), 1, "A leap year has 366 days = 52 weeks + 2 odd days. Monday + 2 days = Wednesday.", Difficulty.MODERATE, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Calendar Odd Days: Leap year contributes 2 odd days. Monday + 2 = Wednesday."),
            PracticeQuestion(15, 15, "At what angle do the hands of a clock meet at 3:30?", listOf("75°", "70°", "80°", "90°"), 0, "Angle = |30H - 5.5M| = |30(3) - 5.5(30)| = |90 - 165| = 75°.", Difficulty.MODERATE, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Clock Angle Formula: θ = |30H - (11/2)M| = |90 - 165| = 75°."),
            PracticeQuestion(16, 16, "Count the number of triangles in a standard square with both diagonals drawn and one median.", listOf("10", "12", "8", "14"), 1, "Square with 2 diagonals has 8 triangles; adding vertical median creates 4 additional triangles = 12.", Difficulty.HARD, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Figure Counting: 2 diagonals = 8 triangles. Bisecting median adds 4 new triangles = 12."),
            PracticeQuestion(17, 17, "If P $ Q means P is father of Q; P # Q means P is mother of Q, which shows M is grandmother of T?", listOf("M # K $ T", "M $ K # T", "M # K # T", "Both A and C"), 3, "In both M # K $ T and M # K # T, M is mother of K, and K is parent of T, so M is grandmother of T.", Difficulty.HARD, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Coded Relation Check: M # K means M is Mother (female). K $ T or K # T means K is parent of T. Both A & C work."),
            PracticeQuestion(18, 18, "Arrange in logical order: 1. Application, 2. Selection, 3. Notification, 4. Exam, 5. Appointment", listOf("3, 1, 4, 2, 5", "1, 3, 4, 2, 5", "3, 1, 2, 4, 5", "1, 4, 3, 2, 5"), 0, "Logical chronological flow: Notification (3) -> Application (1) -> Exam (4) -> Selection (2) -> Appointment (5).", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Chronological Flow: Notification first (3), Appointment last (5) → instantly filters to 3, 1, 4, 2, 5."),
            PracticeQuestion(19, 19, "Dice logic: If numbers 1, 2, 3, 4, 5, 6 are on a die, and 1 is opposite 6, 2 is opposite 5, which number is adjacent to both 3 and 4?", listOf("1, 2, 5, 6", "Only 1", "Only 2", "None"), 0, "Since 3 is opposite 4, all other four faces (1, 2, 5, 6) are adjacent to both 3 and 4.", Difficulty.MODERATE, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Dice Properties: Any face is adjacent to exactly 4 faces (all except its opposite)."),
            PracticeQuestion(20, 20, "Find the missing term: AZ, BY, CX, DW, ?", listOf("EV", "FU", "EW", "EX"), 0, "Opposite letter pairs: A-Z, B-Y, C-X, D-W, E-V.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Alphabet Reverse Pairs (Sum=27): E(5) + V(22) = 27 ⇒ EV."),
            PracticeQuestion(21, 21, "If in a certain code language, 'TEACHER' is written as 'VGCEJGT', how is 'CHILDREN' written?", listOf("EJKNFTGP", "EJKNFUHO", "EJLNFTGP", "EJKMFTGP"), 0, "Each letter is incremented by +2: C+2=E, H+2=J, I+2=K, L+2=N, D+2=F, R+2=T, E+2=G, N+2=P.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ +2 Coding: Check first and last letters: C+2=E, N+2=P → E...P."),
            PracticeQuestion(22, 22, "Find the odd one out among the given options:", listOf("Iron", "Mercury", "Copper", "Silver"), 1, "Mercury is the only metal that is liquid at standard room temperature.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Physical State: Mercury is the only room-temperature liquid metal."),
            PracticeQuestion(23, 23, "Complete the number series: 2, 6, 12, 20, 30, 42, ?", listOf("56", "54", "60", "52"), 0, "Pattern is 1×2, 2×3, 3×4, 4×5, 5×6, 6×7 = 42, 7×8 = 56 (or differences +4, +6, +8, +10, +12, +14).", Difficulty.MODERATE, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Consecutive Product: n(n+1) → 1×2, 2×3, 3×4, 4×5, 5×6, 6×7, 7×8 = 56."),
            PracticeQuestion(24, 24, "Introducing a woman, a man says: 'Her mother is the only daughter of my mother-in-law.' How is the man related to the woman?", listOf("Father", "Brother", "Uncle", "Husband"), 0, "Only daughter of man's mother-in-law is the man's wife. Woman's mother is man's wife, so man is her father.", Difficulty.MODERATE, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Only Daughter Trick: Mother-in-law's only daughter = Wife. Wife's child = Man's daughter → Man is Father."),
            PracticeQuestion(25, 25, "In a class of 45 students, rank of Priya from top is 15th. What is her rank from bottom?", listOf("31st", "30th", "32nd", "29th"), 0, "Rank from bottom = (Total - Rank from top) + 1 = (45 - 15) + 1 = 31st.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Rank Formula: Bottom = Total - Top + 1 = 45 - 15 + 1 = 31st.")
        )
    }

    private fun getEnglishQuestions(
        subjectTitle: String,
        topicTitle: String,
        subtopicTitle: String,
        subtopicId: String
    ): List<PracticeQuestion> {
        return listOf(
            PracticeQuestion(1, 1, "Identify the error in: 'Neither of the two candidates (A) / have paid (B) / their admission fees (C) / No error (D)'", listOf("(A)", "(B)", "(C)", "(D)"), 1, "'Neither of' takes a singular verb. 'have paid' should be replaced by 'has paid'.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Rule: 'Neither of + Plural Noun' always takes a Singular Verb (has paid)."),
            PracticeQuestion(2, 2, "Choose the synonym of 'CANDID':", listOf("Frank", "Secretive", "Shy", "Cruel"), 0, "'Candid' means truthful, straightforward, and frank.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Keyword: Candid = Frank / Outspoken / Forthright."),
            PracticeQuestion(3, 3, "Choose the antonym of 'METICULOUS':", listOf("Careless", "Thorough", "Detailed", "Precise"), 0, "'Meticulous' means showing great attention to detail; its opposite is 'Careless'.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Word Pair: Meticulous (Painstaking/Precise) ↔ Careless / Sloppy."),
            PracticeQuestion(4, 4, "Select the correct one-word substitute: 'A person who does not believe in the existence of God.'", listOf("Atheist", "Theist", "Agnostic", "Fanatic"), 0, "An 'Atheist' does not believe in God. An 'Agnostic' doubts whether God's existence is knowable.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Root Word: Theos (God). A- (without) + Theist = Atheist."),
            PracticeQuestion(5, 5, "What is the meaning of the idiom: 'To spill the beans'?", listOf("To reveal a secret", "To drop groceries", "To cause damage", "To cook food"), 0, "'To spill the beans' means to disclose confidential information prematurely or inadvertently.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Idiom Recall: Spill the beans = Divulge confidential info / Let cat out of bag."),
            PracticeQuestion(6, 6, "Find the correctly spelt word:", listOf("Accomodate", "Accommodate", "Acommodate", "Accommodeit"), 1, "The correct spelling is 'Accommodate' with double 'c' and double 'm'.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Spelling Mnemonic: Accommodate has double C and double M (CC + MM)."),
            PracticeQuestion(7, 7, "Fill in the blank: 'He is junior _______ me in the department.'", listOf("than", "to", "from", "with"), 1, "Adjectives ending in '-ior' (junior, senior, superior, inferior) take the preposition 'to', not 'than'.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Latin Adjectives Rule: Junior, Senior, Superior, Inferior, Prefer always take 'to'."),
            PracticeQuestion(8, 8, "Convert to Passive: 'The chef prepared a sumptuous meal.'", listOf("A sumptuous meal was prepared by the chef.", "A sumptuous meal is prepared by the chef.", "A sumptuous meal had been prepared by the chef.", "A sumptuous meal was being prepared by the chef."), 0, "Simple Past in passive voice uses was/were + V3: 'was prepared by the chef'.", Difficulty.MODERATE, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Voice Rule: Simple Past (V2) → was/were + V3."),
            PracticeQuestion(9, 9, "Convert to Indirect Speech: He said, 'I am working hard for the exam.'", listOf("He said that he was working hard for the exam.", "He said that he is working hard for the exam.", "He said that I was working hard for the exam.", "He said that he had been working hard for the exam."), 0, "Present continuous changes to Past continuous: 'am working' -> 'was working'.", Difficulty.MODERATE, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Narration Rule: Present Continuous (am/is + ing) → Past Continuous (was/were + ing)."),
            PracticeQuestion(10, 10, "Choose the idiom that means 'In very good health':", listOf("Fit as a fiddle", "Under the weather", "Bite the bullet", "Break a leg"), 0, "'Fit as a fiddle' means in very good physical condition and robust health.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Health Idioms: 'Fit as a fiddle' = Healthy; 'Under the weather' = Sick."),
            PracticeQuestion(11, 11, "Identify the correct preposition: 'The manager prevented him _______ entering the building.'", listOf("from", "to", "against", "for"), 0, "The verb 'prevent' is followed by the preposition 'from' + gerund (prevented him from entering).", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Fixed Preposition: Prevent + [object] + FROM + Verb-ing."),
            PracticeQuestion(12, 12, "Select the antonym of 'EPHEMERAL':", listOf("Permanent", "Short-lived", "Fleeting", "Fragile"), 0, "'Ephemeral' means lasting for a very short time; its antonym is 'Permanent'.", Difficulty.MODERATE, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Antonym Pair: Ephemeral (Transitory/Fleeting) ↔ Permanent / Everlasting."),
            PracticeQuestion(13, 13, "Identify the error in: 'Scarcely had I reached the station (A) / then the train (B) / left the platform. (C) / No error (D)'", listOf("(A)", "(B)", "(C)", "(D)"), 1, "'Scarcely' is paired with 'when', not 'then'. It should be 'when the train left'.", Difficulty.MODERATE, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Correlative Conjunctions: Scarcely / Hardly ... WHEN; No sooner ... THAN."),
            PracticeQuestion(14, 14, "One-word substitution: 'A remedy for all diseases or difficulties.'", listOf("Panacea", "Placebo", "Antibiotic", "Elixir"), 0, "'Panacea' means a universal cure or solution for all diseases and troubles.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Root: Pan (all) + Akos (cure) = Panacea (universal cure)."),
            PracticeQuestion(15, 15, "Fill in the blank: 'Hardly had he arrived _______ the bell rang.'", listOf("when", "than", "then", "after"), 0, "'Hardly' is followed by 'when'.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Fixed Pair: 'Hardly had ... when ...'."),
            PracticeQuestion(16, 16, "Select the synonym of 'OBSTINATE':", listOf("Stubborn", "Flexible", "Polite", "Gentle"), 0, "'Obstinate' means refusing to change one's opinion or course of action; stubborn.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Synonyms: Obstinate = Stubborn = Tenacious = Adamant."),
            PracticeQuestion(17, 17, "Choose the correct spelling:", listOf("Bureaucracy", "Beurocracy", "Bureaucrasy", "Burocracy"), 0, "The correct spelling is 'Bureaucracy'.", Difficulty.MODERATE, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Spelling Chunk: BUREAU + CRACY = Bureaucracy."),
            PracticeQuestion(18, 18, "Meaning of the idiom: 'Once in a blue moon'", listOf("Very rarely", "Frequently", "Every month", "Never"), 0, "'Once in a blue moon' refers to something that occurs very infrequently.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Idiom Meaning: 'Once in a blue moon' = Extremely rare event."),
            PracticeQuestion(19, 19, "Identify the sentence with correct subject-verb agreement:", listOf("The quality of these mangoes is good.", "The quality of these mangoes are good.", "The quality of these mangoes were good.", "The quality of these mangoes have good."), 0, "The real subject is 'quality' (singular), so it takes the singular verb 'is'.", Difficulty.MODERATE, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Core Subject Rule: Subject is 'quality' (singular), ignore intervening plural noun 'mangoes'."),
            PracticeQuestion(20, 20, "Select the one-word substitute: 'One who collects postage stamps.'", listOf("Philatelist", "Numismatist", "Bibliophile", "Anthropologist"), 0, "'Philatelist' is a stamp collector, whereas 'Numismatist' collects coins/currency.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Collector Roots: Philatelist = Stamps; Numismatist = Coins."),
            PracticeQuestion(21, 21, "Select the synonym of 'LUCID':", listOf("Clear", "Cloudy", "Dull", "Confused"), 0, "'Lucid' means expressed clearly or easy to understand.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Root Word: Luc / Lum (light/clarity) → Lucid = Clear."),
            PracticeQuestion(22, 22, "Identify the correctly spelled word:", listOf("Maintenance", "Maintainance", "Maintanence", "Maintenence"), 0, "The correct spelling is 'Maintenance'.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Spelling Rule: Main + TEN + ance (not tain)."),
            PracticeQuestion(23, 23, "Meaning of idiom 'Hit the nail on the head':", listOf("Find exact truth", "Hurt yourself", "Make a huge error", "Work continuously"), 0, "'To hit the nail on the head' means to describe exactly what is causing a situation or problem.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Idiom: Hit the nail on the head = Exactly right / pinpoint accuracy."),
            PracticeQuestion(24, 24, "Choose the antonym of 'BENEVOLENT':", listOf("Malevolent", "Kind", "Generous", "Friendly"), 0, "'Benevolent' means well meaning and kindly; its opposite is 'Malevolent'.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Prefix Opposites: Bene- (good) ↔ Mal- (evil/bad) → Benevolent ↔ Malevolent."),
            PracticeQuestion(25, 25, "One word substitute for: 'A person who loves books.'", listOf("Bibliophile", "Philanthropist", "Polyglot", "Misologist"), 0, "'Bibliophile' is a person who has a great love of books.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Roots: Biblio (books) + Phile (love) = Bibliophile.")
        )
    }

    private fun getGkQuestions(
        subjectTitle: String,
        topicTitle: String,
        subtopicTitle: String,
        subtopicId: String
    ): List<PracticeQuestion> {
        return listOf(
            PracticeQuestion(1, 1, "Which Article of the Indian Constitution is called the 'Heart and Soul of the Constitution' by Dr. B.R. Ambedkar?", listOf("Article 32", "Article 21", "Article 14", "Article 19"), 0, "Article 32 provides Constitutional Remedies for the enforcement of Fundamental Rights.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Constitution Key: Article 32 = Right to Constitutional Remedies (5 Writs)."),
            PracticeQuestion(2, 2, "Who was the founder of the Maurya Dynasty in ancient India?", listOf("Chandragupta Maurya", "Ashoka", "Bindusara", "Brihadratha"), 0, "Chandragupta Maurya founded the Maurya Empire in 322 BCE with the assistance of Chanakya (Kautilya).", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Dynasty Chronology: Chandragupta (Founder) → Bindusara → Ashoka → Brihadratha (Last)."),
            PracticeQuestion(3, 3, "The Tropic of Cancer passes through how many Indian States?", listOf("8 States", "7 States", "9 States", "6 States"), 0, "The Tropic of Cancer (23.5° N) passes through 8 states: Gujarat, Rajasthan, MP, Chhattisgarh, Jharkhand, West Bengal, Tripura, and Mizoram.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Mnemonic for 8 States: 'Mitra Par Gamchha Jhar' (Mizoram, Tripura, WB, Rajasthan, Gujarat, MP, Chhattisgarh, Jharkhand)."),
            PracticeQuestion(4, 4, "What is the powerhouse of the biological cell?", listOf("Mitochondria", "Ribosome", "Nucleus", "Golgi Apparatus"), 0, "Mitochondria generate most of the chemical energy needed by the cell (ATP molecules).", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Biology Mnemonic: Mitochondria = Cellular respiration & ATP powerhouse."),
            PracticeQuestion(5, 5, "Who appoints the Chief Justice of India (CJI)?", listOf("The President of India", "The Prime Minister", "The Law Minister", "The Parliament"), 0, "The President of India appoints the CJI and other Supreme Court judges under Article 124(2).", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Polity Fact: President appoints CJI, SC/HC Judges, Governors, and CAG."),
            PracticeQuestion(6, 6, "In which year did the Battle of Plassey take place?", listOf("1757", "1764", "1857", "1761"), 0, "The Battle of Plassey was fought on 23 June 1757 between the British East India Company led by Robert Clive and Nawab Siraj-ud-Daulah.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Landmark Battles: Plassey = 1757, Buxar = 1764, Panipat III = 1761."),
            PracticeQuestion(7, 7, "Which river is known as the 'Sorrow of Bihar' due to frequent flooding?", listOf("Kosi", "Gandak", "Son", "Ghaghara"), 0, "The Kosi river frequently changes its course and causes extensive floods in Bihar.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ River Nicknames: Sorrow of Bihar = Kosi; Sorrow of Bengal = Damodar."),
            PracticeQuestion(8, 8, "Which gas is primarily used in electric bulbs to prevent filament oxidation?", listOf("Argon", "Oxygen", "Hydrogen", "Carbon Dioxide"), 0, "Argon (an inert gas) or Nitrogen is used to prevent the tungsten filament from burning out.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Chemistry Fact: Inert noble gas Argon (Ar) prevents tungsten oxidation."),
            PracticeQuestion(9, 9, "Which classical dance form originates from the state of Kerala?", listOf("Kathakali & Mohiniyattam", "Bharatnatyam", "Kathak", "Kuchipudi"), 0, "Kathakali and Mohiniyattam are traditional classical dances of Kerala.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Classical Dances: Kerala = Kathakali & Mohiniyattam; TN = Bharatnatyam; AP = Kuchipudi."),
            PracticeQuestion(10, 10, "What is the SI unit of electric resistance?", listOf("Ohm", "Volt", "Ampere", "Watt"), 0, "Ohm (Ω) is the SI unit of electrical resistance, named after Georg Simon Ohm.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Units: Resistance = Ohm (Ω), Current = Ampere (A), Potential = Volt (V)."),
            PracticeQuestion(11, 11, "Which Five Year Plan in India was based on the Mahalanobis Model?", listOf("Second Five Year Plan", "First Five Year Plan", "Third Five Year Plan", "Fourth Five Year Plan"), 0, "The Second Five-Year Plan (1956–1961) focused on rapid industrialization and heavy industries using the Mahalanobis model.", Difficulty.MODERATE, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ FYP Models: 1st Plan = Harrod-Domar (Agriculture), 2nd Plan = PC Mahalanobis (Heavy Industry)."),
            PracticeQuestion(12, 12, "The 'Right to Education' was made a Fundamental Right under which Constitutional Amendment?", listOf("86th Amendment Act, 2002", "44th Amendment Act, 1978", "42nd Amendment Act, 1976", "91st Amendment Act, 2003"), 0, "The 86th Amendment inserted Article 21A, providing free and compulsory education to children aged 6 to 14.", Difficulty.MODERATE, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Key Amendment: 86th CAA (2002) added Article 21A (RTE for 6-14 years)."),
            PracticeQuestion(13, 13, "Which is the highest peak in the Western Ghats (Sahyadris)?", listOf("Anamudi", "Doddabetta", "Kalsubai", "Mullayanagiri"), 0, "Anamudi (2,695 m) in Kerala is the highest peak in the Western Ghats and in South India.", Difficulty.MODERATE, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Peak Fact: Anamudi (2695m) is highest peak in South India / Western Ghats."),
            PracticeQuestion(14, 14, "Which acid is present in the sting of an ant?", listOf("Formic Acid (Methanoic acid)", "Acetic Acid", "Citric Acid", "Tartaric Acid"), 0, "Ant stings contain Formic Acid (HCOOH), which causes irritation and burning sensations.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Natural Acids: Ant/Bee sting = Formic Acid (Methanoic Acid); Vinegar = Acetic Acid."),
            PracticeQuestion(15, 15, "Who wrote the famous book 'Poverty and Un-British Rule in India'?", listOf("Dadabhai Naoroji", "R.C. Dutt", "G.K. Gokhale", "Bal Gangadhar Tilak"), 0, "Dadabhai Naoroji authored this work and expounded the 'Drain of Wealth' theory.", Difficulty.MODERATE, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Freedom History: Dadabhai Naoroji (Grand Old Man of India) formulated 'Drain of Wealth'."),
            PracticeQuestion(16, 16, "Which organ in the human body produces bile juice?", listOf("Liver", "Pancreas", "Gallbladder", "Stomach"), 0, "Bile is synthesized by the liver and stored/concentrated in the gallbladder.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Bio Distinction: Liver produces bile; Gallbladder only stores it."),
            PracticeQuestion(17, 17, "What is the minimum age required to become a member of the Rajya Sabha?", listOf("30 years", "25 years", "35 years", "21 years"), 0, "Under Article 84, minimum age for Rajya Sabha is 30 years (and 25 years for Lok Sabha).", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Age Criteria: Lok Sabha/MLA = 25 yrs; Rajya Sabha/MLC = 30 yrs; President/Gov = 35 yrs."),
            PracticeQuestion(18, 18, "Which layer of the atmosphere contains the Ozone layer that absorbs harmful UV rays?", listOf("Stratosphere", "Troposphere", "Mesosphere", "Thermosphere"), 0, "The ozone layer is primarily found in the lower portion of the Stratosphere (approx 15–35 km).", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Atmospheric Layers (bottom to top): Troposphere → Stratosphere (Ozone) → Mesosphere → Thermosphere."),
            PracticeQuestion(19, 19, "Which Governor-General abolished the practice of Sati in India in 1829?", listOf("Lord William Bentinck", "Lord Dalhousie", "Lord Ripon", "Lord Canning"), 0, "Lord William Bentinck enacted the Bengal Sati Regulation in 1829, supported by Raja Ram Mohan Roy.", Difficulty.MODERATE, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Modern History: Lord William Bentinck abolished Sati (1829) with Raja Ram Mohan Roy."),
            PracticeQuestion(20, 20, "What is the chemical symbol for Gold in the periodic table?", listOf("Au", "Ag", "Pb", "Fe"), 0, "Au (from Latin 'Aurum') is the symbol for Gold (atomic number 79).", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Latin Chemical Symbols: Au = Gold (Aurum), Ag = Silver (Argentum), Pb = Lead (Plumbum)."),
            PracticeQuestion(21, 21, "Which planet is known as the 'Red Planet' in our Solar System?", listOf("Mars", "Venus", "Jupiter", "Saturn"), 0, "Mars appears reddish due to iron oxide (rust) on its surface.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Planet Colors: Mars = Red (Iron Oxide), Venus = Brightest/Hottest, Jupiter = Largest."),
            PracticeQuestion(22, 22, "Where are the headquarters of the Reserve Bank of India (RBI) located?", listOf("Mumbai", "New Delhi", "Kolkata", "Chennai"), 0, "The central office of the RBI is established in Mumbai, Maharashtra.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Financial Capital: RBI HQ is in Mumbai (shifted from Kolkata in 1937)."),
            PracticeQuestion(23, 23, "Who discovered the vaccine for Smallpox?", listOf("Edward Jenner", "Alexander Fleming", "Louis Pasteur", "Robert Koch"), 0, "Edward Jenner developed the smallpox vaccine in 1796.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Medical Inventions: Jenner = Smallpox vaccine (Father of Immunology); Fleming = Penicillin."),
            PracticeQuestion(24, 24, "Which Indian State has the longest coastline?", listOf("Gujarat", "Andhra Pradesh", "Tamil Nadu", "Maharashtra"), 0, "Gujarat has the longest mainland coastline in India (approx 1,600 km).", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ Geography Rank: #1 Gujarat (~1600km), #2 Andhra Pradesh (~974km)."),
            PracticeQuestion(25, 25, "In which year was the Indian National Congress (INC) founded?", listOf("1885", "1895", "1905", "1857"), 0, "The Indian National Congress was founded in Bombay in December 1885 by A.O. Hume.", Difficulty.EASY, subjectTitle, topicTitle, subtopicTitle, subtopicId, shortcutMethod = "⚡ INC Foundation: Founded in 1885 at Gokuldas Tejpal Sanskrit College, Bombay (1st President: WC Bonnerjee).")
        )
    }
}
