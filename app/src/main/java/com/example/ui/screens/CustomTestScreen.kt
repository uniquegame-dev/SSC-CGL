package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.CustomTestResult
import com.example.data.models.Difficulty
import com.example.data.models.PaletteState
import com.example.data.models.PracticeQuestion
import com.example.data.models.QuestionReviewItem
import com.example.data.models.SubjectTestPerformance
import com.example.data.models.UserQuestionState
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BorderMedium
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.LightBackground
import com.example.ui.theme.LightCardBackground
import com.example.ui.theme.LightCardBackgroundVariant
import com.example.ui.theme.LightSurface
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueDark
import com.example.ui.theme.PrimaryBlueLight
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextDarkBody
import com.example.ui.theme.TextDarkHeading
import com.example.ui.theme.TextMediumGray
import com.example.ui.theme.WarningAmber
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CustomTestScreen(
    testTitle: String = "Custom Test",
    selectedSubjectIds: List<String>,
    isTimed: Boolean,
    questions: List<PracticeQuestion>,
    onTestFinished: (CustomTestResult) -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No questions available for selected subjects.")
        }
        return
    }

    val totalQuestions = questions.size
    val totalTimeSeconds = remember(selectedSubjectIds, isTimed) {
        if (isTimed) selectedSubjectIds.size * 15 * 60L else 0L
    }

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var timeRemainingSeconds by remember { mutableLongStateOf(totalTimeSeconds) }
    var elapsedSeconds by remember { mutableLongStateOf(0L) }
    var isTimerRunning by remember { mutableStateOf(true) }

    // User answers state
    val userAnswers = remember(questions) {
        mutableStateMapOf<Int, UserQuestionState>().apply {
            questions.forEachIndexed { idx, q ->
                put(q.id, UserQuestionState(questionId = q.id, isVisited = idx == 0))
            }
        }
    }

    var showPaletteSheet by remember { mutableStateOf(false) }
    var showSubmitDialog by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }

    val currentQuestion = questions.getOrNull(currentQuestionIndex) ?: questions[0]
    val currentState = userAnswers[currentQuestion.id] ?: UserQuestionState(currentQuestion.id)

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Helper to calculate result
    fun submitTest() {
        isTimerRunning = false
        val subjectPerformances = mutableListOf<SubjectTestPerformance>()

        for (subjectId in selectedSubjectIds) {
            val subQuestions = questions.filter {
                when (subjectId) {
                    "maths" -> it.subjectTitle.contains("Quantitative", ignoreCase = true) || it.subjectTitle.contains("Math", ignoreCase = true)
                    "reasoning" -> it.subjectTitle.contains("Reasoning", ignoreCase = true) || it.subjectTitle.contains("Intelligence", ignoreCase = true)
                    "english" -> it.subjectTitle.contains("English", ignoreCase = true)
                    "gk_ga" -> it.subjectTitle.contains("Awareness", ignoreCase = true) || it.subjectTitle.contains("Knowledge", ignoreCase = true) || it.subjectTitle.contains("GK", ignoreCase = true)
                    else -> it.subjectTitle == subjectId
                }
            }

            val subTotal = subQuestions.size
            var subCorrect = 0
            var subWrong = 0
            var subUnattempted = 0

            for (q in subQuestions) {
                val state = userAnswers[q.id]
                val selected = state?.selectedOptionIndex
                if (selected == null) {
                    subUnattempted++
                } else if (selected == q.correctOptionIndex) {
                    subCorrect++
                } else {
                    subWrong++
                }
            }

            val subScore = (subCorrect * 2.0) - (subWrong * 0.5)
            val subAttempted = subCorrect + subWrong
            val subAccuracy = if (subAttempted > 0) (subCorrect.toDouble() / subAttempted) * 100.0 else 0.0

            val displayTitle = when (subjectId) {
                "maths" -> "Quantitative Aptitude"
                "reasoning" -> "General Intelligence & Reasoning"
                "english" -> "English Comprehension"
                "gk_ga" -> "General Awareness"
                else -> subjectId.replaceFirstChar { it.uppercase() }
            }

            subjectPerformances.add(
                SubjectTestPerformance(
                    subjectId = subjectId,
                    subjectTitle = displayTitle,
                    totalQuestions = subTotal,
                    correct = subCorrect,
                    wrong = subWrong,
                    unattempted = subUnattempted,
                    accuracyPercent = subAccuracy,
                    score = subScore
                )
            )
        }

        val totalCorrect = subjectPerformances.sumOf { it.correct }
        val totalWrong = subjectPerformances.sumOf { it.wrong }
        val totalUnattempted = subjectPerformances.sumOf { it.unattempted }
        val totalAttempted = totalCorrect + totalWrong
        val totalScore = (totalCorrect * 2.0) - (totalWrong * 0.5)
        val maxScore = (totalQuestions * 2.0)
        val accuracy = if (totalAttempted > 0) (totalCorrect.toDouble() / totalAttempted) * 100.0 else 0.0

        val totalTestTime = if (isTimed) (totalTimeSeconds - timeRemainingSeconds) else elapsedSeconds
        val negativeMarksLost = totalWrong * 0.5
        val avgTimePerQuestion = if (totalQuestions > 0) totalTestTime.toDouble() / totalQuestions else 0.0

        val questionReviews = questions.map { q ->
            val state = userAnswers[q.id]
            val selectedOption = state?.selectedOptionIndex
            val isAttempted = selectedOption != null
            val isCorrect = selectedOption == q.correctOptionIndex
            val timeSpent = state?.timeSpentSeconds ?: if (totalQuestions > 0) (totalTestTime / totalQuestions) else 0L

            QuestionReviewItem(
                question = q,
                selectedOptionIndex = selectedOption,
                correctOptionIndex = q.correctOptionIndex,
                isCorrect = isCorrect,
                isAttempted = isAttempted,
                timeSpentSeconds = timeSpent,
                isMarkedForReview = state?.isMarkedForReview == true
            )
        }

        val result = CustomTestResult(
            selectedSubjectTitles = subjectPerformances.map { it.subjectTitle },
            totalQuestions = totalQuestions,
            attempted = totalAttempted,
            correct = totalCorrect,
            wrong = totalWrong,
            unattempted = totalUnattempted,
            totalScore = totalScore,
            maxScore = maxScore,
            accuracyPercent = accuracy,
            totalTimeSeconds = totalTestTime,
            isTimed = isTimed,
            subjectBreakdown = subjectPerformances,
            negativeMarksLost = negativeMarksLost,
            averageTimePerQuestionSeconds = avgTimePerQuestion,
            questionReviews = questionReviews
        )

        onTestFinished(result)
    }

    // Timer effect
    LaunchedEffect(isTimerRunning, isTimed, currentQuestionIndex) {
        while (isTimerRunning) {
            delay(1000L)
            val currentQ = questions.getOrNull(currentQuestionIndex)
            if (currentQ != null) {
                val state = userAnswers[currentQ.id] ?: UserQuestionState(currentQ.id)
                userAnswers[currentQ.id] = state.copy(timeSpentSeconds = state.timeSpentSeconds + 1L)
            }
            if (isTimed) {
                if (timeRemainingSeconds > 0) {
                    timeRemainingSeconds--
                } else {
                    // Time is up -> Auto Submit
                    submitTest()
                    break
                }
            } else {
                elapsedSeconds++
            }
        }
    }

    // Handle Android system back button
    BackHandler {
        showExitDialog = true
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("custom_test_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = testTitle,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextDarkHeading
                        )
                        Text(
                            text = "${currentQuestion.subjectTitle} • Q${currentQuestionIndex + 1}/$totalQuestions",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = PrimaryBlue
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { showExitDialog = true },
                        modifier = Modifier.testTag("custom_test_exit_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Exit Test",
                            tint = TextDarkHeading
                        )
                    }
                },
                actions = {
                    // Timer Badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isTimed && timeRemainingSeconds < 300) ErrorRed.copy(alpha = 0.15f) else PrimaryBlueLight.copy(alpha = 0.5f),
                        border = BorderStroke(
                            1.dp,
                            if (isTimed && timeRemainingSeconds < 300) ErrorRed.copy(alpha = 0.4f) else PrimaryBlue.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.padding(end = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                                .testTag("custom_test_timer_badge")
                        ) {
                            Icon(
                                imageVector = if (isTimed) Icons.Default.Timer else Icons.Default.Schedule,
                                contentDescription = null,
                                tint = if (isTimed && timeRemainingSeconds < 300) ErrorRed else PrimaryBlueDark,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            val displayTime = if (isTimed) {
                                val m = timeRemainingSeconds / 60
                                val s = timeRemainingSeconds % 60
                                String.format("%02d:%02d", m, s)
                            } else {
                                val m = elapsedSeconds / 60
                                val s = elapsedSeconds % 60
                                String.format("%02d:%02d", m, s)
                            }
                            Text(
                                text = displayTime,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isTimed && timeRemainingSeconds < 300) ErrorRed else PrimaryBlueDark
                            )
                        }
                    }

                    // Palette Icon Button
                    IconButton(
                        onClick = { showPaletteSheet = true },
                        modifier = Modifier.testTag("open_palette_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.GridView,
                            contentDescription = "Question Grid",
                            tint = TextDarkHeading
                        )
                    }

                    // Submit Text Button
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = PrimaryBlue,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { showSubmitDialog = true }
                            .testTag("top_submit_button")
                    ) {
                        Text(
                            text = "Submit",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LightBackground,
                    titleContentColor = TextDarkHeading
                )
            )
        },
        containerColor = LightBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 560.dp)
                    .fillMaxWidth()
                    .fillMaxSize()
            ) {
                // Main Question Body (Scrollable)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    // Question Header Badge Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = PrimaryBlue.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = "Q ${currentQuestionIndex + 1} of $totalQuestions",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = PrimaryBlueDark,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = LightCardBackgroundVariant
                            ) {
                                Text(
                                    text = "+2.0  -0.5",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                                    color = TextMediumGray,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                )
                            }
                        }

                        // Mark for Review toggle
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (currentState.isMarkedForReview) WarningAmber.copy(alpha = 0.15f) else LightCardBackgroundVariant,
                            border = BorderStroke(
                                1.dp,
                                if (currentState.isMarkedForReview) WarningAmber else BorderLight
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    val updated = currentState.copy(
                                        isMarkedForReview = !currentState.isMarkedForReview,
                                        isVisited = true
                                    )
                                    userAnswers[currentQuestion.id] = updated
                                }
                                .testTag("mark_for_review_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = if (currentState.isMarkedForReview) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = null,
                                    tint = if (currentState.isMarkedForReview) WarningAmber else TextMediumGray,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (currentState.isMarkedForReview) "Marked" else "Review",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 11.sp
                                    ),
                                    color = if (currentState.isMarkedForReview) WarningAmber else TextDarkBody
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Question Card
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = LightSurface),
                        border = BorderStroke(1.dp, BorderLight),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = currentQuestion.questionText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                lineHeight = 24.sp
                            ),
                            color = TextDarkHeading,
                            modifier = Modifier.padding(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Option Choices
                    Text(
                        text = "Choose the correct option:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextMediumGray,
                        modifier = Modifier.padding(start = 2.dp, bottom = 8.dp)
                    )

                    val optionLabels = listOf("A", "B", "C", "D")
                    currentQuestion.options.forEachIndexed { optionIndex, optionText ->
                        val isSelected = currentState.selectedOptionIndex == optionIndex

                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) PrimaryBlueLight.copy(alpha = 0.25f) else LightSurface
                            ),
                            border = BorderStroke(
                                1.2.dp,
                                if (isSelected) PrimaryBlue else BorderLight
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 1.dp else 0.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    val updated = currentState.copy(
                                        selectedOptionIndex = optionIndex,
                                        isVisited = true
                                    )
                                    userAnswers[currentQuestion.id] = updated
                                }
                                .testTag("option_${optionLabels.getOrElse(optionIndex) { "$optionIndex" }}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 13.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isSelected) PrimaryBlue else LightCardBackgroundVariant
                                        )
                                        .border(
                                            1.dp,
                                            if (isSelected) PrimaryBlue else BorderMedium,
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = optionLabels.getOrElse(optionIndex) { "?" },
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = if (isSelected) Color.White else TextDarkHeading
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = optionText,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                    ),
                                    color = if (isSelected) PrimaryBlueDark else TextDarkHeading,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                }

                // Bottom Action Bar
                Surface(
                    color = LightSurface,
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Previous Button
                        OutlinedButton(
                            onClick = {
                                if (currentQuestionIndex > 0) {
                                    currentQuestionIndex--
                                    val prevQ = questions[currentQuestionIndex]
                                    val cur = userAnswers[prevQ.id] ?: UserQuestionState(prevQ.id)
                                    userAnswers[prevQ.id] = cur.copy(isVisited = true)
                                }
                            },
                            enabled = currentQuestionIndex > 0,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, BorderLight),
                            modifier = Modifier.testTag("prev_question_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.NavigateBefore,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("Prev", style = MaterialTheme.typography.labelMedium)
                        }

                        // Clear Response Button
                        TextButton(
                            onClick = {
                                val updated = currentState.copy(selectedOptionIndex = null)
                                userAnswers[currentQuestion.id] = updated
                            },
                            enabled = currentState.selectedOptionIndex != null,
                            modifier = Modifier.testTag("clear_response_button")
                        ) {
                            Text(
                                text = "Clear",
                                style = MaterialTheme.typography.labelMedium,
                                color = if (currentState.selectedOptionIndex != null) ErrorRed else TextMediumGray
                            )
                        }

                        // Next / Save & Next Button
                        Button(
                            onClick = {
                                if (currentQuestionIndex < totalQuestions - 1) {
                                    currentQuestionIndex++
                                    val nextQ = questions[currentQuestionIndex]
                                    val cur = userAnswers[nextQ.id] ?: UserQuestionState(nextQ.id)
                                    userAnswers[nextQ.id] = cur.copy(isVisited = true)
                                } else {
                                    showSubmitDialog = true
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryBlue,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.testTag("next_question_button")
                        ) {
                            Text(
                                text = if (currentQuestionIndex < totalQuestions - 1) "Save & Next" else "Submit",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            if (currentQuestionIndex < totalQuestions - 1) {
                                Spacer(modifier = Modifier.width(2.dp))
                                Icon(
                                    imageVector = Icons.Default.NavigateNext,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Bottom Sheet: Question Grid Palette
    if (showPaletteSheet) {
        ModalBottomSheet(
            onDismissRequest = { showPaletteSheet = false },
            sheetState = sheetState,
            containerColor = LightSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Question Palette",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextDarkHeading
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Legend row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val answeredCount = userAnswers.values.count { it.selectedOptionIndex != null }
                    val markedCount = userAnswers.values.count { it.isMarkedForReview }
                    val unattemptedCount = totalQuestions - answeredCount

                    LegendBadge(label = "Answered ($answeredCount)", color = SuccessGreen)
                    LegendBadge(label = "Marked ($markedCount)", color = WarningAmber)
                    LegendBadge(label = "Left ($unattemptedCount)", color = TextMediumGray)
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = BorderLight)
                Spacer(modifier = Modifier.height(14.dp))

                // Grid of question buttons
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 48.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    itemsIndexed(questions) { index, q ->
                        val state = userAnswers[q.id] ?: UserQuestionState(q.id)
                        val isCurrent = index == currentQuestionIndex
                        val isAnswered = state.selectedOptionIndex != null
                        val isMarked = state.isMarkedForReview

                        val bgColor = when {
                            isAnswered -> SuccessGreen
                            isMarked -> WarningAmber
                            state.isVisited -> LightCardBackgroundVariant
                            else -> LightSurface
                        }

                        val textColor = when {
                            isAnswered || isMarked -> Color.White
                            else -> TextDarkHeading
                        }

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(bgColor)
                                .border(
                                    width = if (isCurrent) 2.dp else 1.dp,
                                    color = if (isCurrent) PrimaryBlue else BorderMedium,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    currentQuestionIndex = index
                                    val cur = userAnswers[q.id] ?: UserQuestionState(q.id)
                                    userAnswers[q.id] = cur.copy(isVisited = true)
                                    coroutineScope.launch {
                                        showPaletteSheet = false
                                    }
                                }
                                .testTag("palette_item_${index + 1}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = textColor
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        showPaletteSheet = false
                        showSubmitDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("palette_submit_button")
                ) {
                    Text("Submit Test Now", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    // Submit Confirmation Dialog
    if (showSubmitDialog) {
        val answeredCount = userAnswers.values.count { it.selectedOptionIndex != null }
        val markedCount = userAnswers.values.count { it.isMarkedForReview }
        val unansweredCount = totalQuestions - answeredCount

        AlertDialog(
            onDismissRequest = { showSubmitDialog = false },
            title = {
                Text(
                    text = "Submit Custom Test?",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextDarkHeading
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Are you sure you want to finish and submit your test?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextDarkBody
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = LightCardBackground,
                        border = BorderStroke(1.dp, BorderLight),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Total Questions:", style = MaterialTheme.typography.bodySmall, color = TextMediumGray)
                                Text("$totalQuestions", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Answered:", style = MaterialTheme.typography.bodySmall, color = TextMediumGray)
                                Text("$answeredCount", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = SuccessGreen))
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Unanswered:", style = MaterialTheme.typography.bodySmall, color = TextMediumGray)
                                Text("$unansweredCount", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = ErrorRed))
                            }
                            if (markedCount > 0) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Marked for Review:", style = MaterialTheme.typography.bodySmall, color = TextMediumGray)
                                    Text("$markedCount", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = WarningAmber))
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSubmitDialog = false
                        submitTest()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    modifier = Modifier.testTag("confirm_submit_test_button")
                ) {
                    Text("Submit Test")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showSubmitDialog = false },
                    modifier = Modifier.testTag("cancel_submit_test_button")
                ) {
                    Text("Resume Practice")
                }
            }
        )
    }

    // Exit Confirmation Dialog
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = {
                Text(
                    text = "Exit Test?",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextDarkHeading
                )
            },
            text = {
                Text(
                    text = "If you exit now, your current test progress will be lost. Do you want to leave?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextDarkBody
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showExitDialog = false
                        isTimerRunning = false
                        onExit()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                    modifier = Modifier.testTag("confirm_exit_test_button")
                ) {
                    Text("Exit")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showExitDialog = false },
                    modifier = Modifier.testTag("cancel_exit_test_button")
                ) {
                    Text("Keep Practicing")
                }
            }
        )
    }
}

@Composable
private fun LegendBadge(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
            color = TextDarkHeading
        )
    }
}
