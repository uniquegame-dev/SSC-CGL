package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.Difficulty
import com.example.data.models.PaletteState
import com.example.data.models.PracticeQuestion
import com.example.data.models.PracticeSessionResult
import com.example.data.models.QuestionReviewItem
import com.example.data.models.Subject
import com.example.data.models.Subtopic
import com.example.data.models.Topic
import com.example.data.models.UserQuestionState
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.DifficultyEasy
import com.example.ui.theme.DifficultyEasyBg
import com.example.ui.theme.DifficultyHard
import com.example.ui.theme.DifficultyHardBg
import com.example.ui.theme.DifficultyModerate
import com.example.ui.theme.DifficultyModerateBg
import com.example.ui.theme.LightBackground
import com.example.ui.theme.LightCardBackground
import com.example.ui.theme.LightCardBackgroundVariant
import com.example.ui.theme.LightSurface
import com.example.ui.theme.PaletteAnsweredBg
import com.example.ui.theme.PaletteAnsweredText
import com.example.ui.theme.PaletteMarkedBg
import com.example.ui.theme.PaletteMarkedText
import com.example.ui.theme.PaletteNotAnsweredBg
import com.example.ui.theme.PaletteNotAnsweredText
import com.example.ui.theme.PaletteNotVisitedBg
import com.example.ui.theme.PaletteNotVisitedBorder
import com.example.ui.theme.PaletteNotVisitedText
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueDark
import com.example.ui.theme.TextDarkBody
import com.example.ui.theme.TextDarkHeading
import com.example.ui.theme.TextMediumGray
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeQuestionScreen(
    subject: Subject,
    topic: Topic,
    subtopic: Subtopic? = null,
    questions: List<PracticeQuestion>,
    onFinishPractice: (PracticeSessionResult) -> Unit,
    onBackToSubtopics: () -> Unit = {},
    onBackToTopics: () -> Unit = onBackToSubtopics,
    modifier: Modifier = Modifier
) {
    var currentQuestionIndex by rememberSaveable { mutableIntStateOf(0) }
    var totalElapsedSeconds by rememberSaveable { mutableLongStateOf(0L) }
    var showPaletteSheet by rememberSaveable { mutableStateOf(false) }
    var showSubmitDialog by rememberSaveable { mutableStateOf(false) }
    var showExitConfirmDialog by rememberSaveable { mutableStateOf(false) }

    // User question responses map: key is question index (0 to 19)
    val userResponses = remember {
        mutableStateMapOf<Int, UserQuestionState>().apply {
            questions.indices.forEach { index ->
                put(
                    index,
                    UserQuestionState(
                        questionId = questions[index].id,
                        selectedOptionIndex = null,
                        isMarkedForReview = false,
                        isVisited = (index == 0),
                        timeSpentSeconds = 0L
                    )
                )
            }
        }
    }

    // Keep current selected option in local state for active editing before saving
    val currentQuestion = questions.getOrElse(currentQuestionIndex) { questions.first() }
    val currentState = userResponses[currentQuestionIndex] ?: UserQuestionState(currentQuestion.id, isVisited = true)

    // Ensure the current question is marked visited
    LaunchedEffect(currentQuestionIndex) {
        val existing = userResponses[currentQuestionIndex]
        if (existing == null || !existing.isVisited) {
            userResponses[currentQuestionIndex] = (existing ?: UserQuestionState(currentQuestion.id)).copy(isVisited = true)
        }
    }

    // Timer tracking
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            totalElapsedSeconds += 1L
            val current = userResponses[currentQuestionIndex]
            if (current != null) {
                userResponses[currentQuestionIndex] = current.copy(timeSpentSeconds = current.timeSpentSeconds + 1L)
            }
        }
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Helper functions for action navigation
    fun selectOption(optionIndex: Int) {
        val existing = userResponses[currentQuestionIndex] ?: UserQuestionState(currentQuestion.id, isVisited = true)
        userResponses[currentQuestionIndex] = existing.copy(
            selectedOptionIndex = optionIndex,
            isVisited = true
        )
    }

    fun clearResponse() {
        val existing = userResponses[currentQuestionIndex] ?: UserQuestionState(currentQuestion.id, isVisited = true)
        userResponses[currentQuestionIndex] = existing.copy(
            selectedOptionIndex = null,
            isVisited = true
        )
    }

    fun markForReviewAndNext() {
        val existing = userResponses[currentQuestionIndex] ?: UserQuestionState(currentQuestion.id, isVisited = true)
        userResponses[currentQuestionIndex] = existing.copy(
            isMarkedForReview = true,
            isVisited = true
        )
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex += 1
        } else {
            showSubmitDialog = true
        }
    }

    fun saveAndNext() {
        val existing = userResponses[currentQuestionIndex] ?: UserQuestionState(currentQuestion.id, isVisited = true)
        // If user already had marked for review, clearing review flag upon explicit Save & Next is standard SSC behavior
        userResponses[currentQuestionIndex] = existing.copy(
            isMarkedForReview = false,
            isVisited = true
        )
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex += 1
        } else {
            showSubmitDialog = true
        }
    }

    fun previousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex -= 1
        }
    }

    fun submitExam() {
        var attempted = 0
        var correct = 0
        var wrong = 0
        var unattempted = 0

        val reviewItems = questions.mapIndexed { index, q ->
            val resp = userResponses[index]
            val selected = resp?.selectedOptionIndex
            val isAttempted = selected != null
            val isCorrect = selected == q.correctOptionIndex
            val timeSpent = resp?.timeSpentSeconds ?: 0L

            if (isAttempted) {
                attempted++
                if (isCorrect) correct++ else wrong++
            } else {
                unattempted++
            }

            QuestionReviewItem(
                question = q,
                selectedOptionIndex = selected,
                correctOptionIndex = q.correctOptionIndex,
                isCorrect = isCorrect,
                isAttempted = isAttempted,
                timeSpentSeconds = timeSpent,
                isMarkedForReview = resp?.isMarkedForReview == true
            )
        }

        val accuracy = if (attempted > 0) (correct.toDouble() / attempted.toDouble()) * 100.0 else 0.0
        val avgTime = if (questions.isNotEmpty()) totalElapsedSeconds.toDouble() / questions.size.toDouble() else 0.0

        val result = PracticeSessionResult(
            subjectTitle = subject.title,
            topicTitle = topic.title,
            subtopicTitle = subtopic?.title ?: questions.firstOrNull()?.subtopicTitle ?: topic.title,
            totalQuestions = questions.size,
            attempted = attempted,
            correct = correct,
            wrong = wrong,
            unattempted = unattempted,
            accuracyPercent = accuracy,
            totalTimeSeconds = totalElapsedSeconds,
            averageTimePerQuestionSeconds = avgTime,
            questionReviews = reviewItems
        )

        onFinishPractice(result)
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("practice_question_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Question ${currentQuestionIndex + 1} of ${questions.size}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                ),
                                color = TextDarkHeading,
                                modifier = Modifier.testTag("question_header_title")
                            )
                        }
                        val subtitleText = when {
                            subtopic != null -> "${subject.title} • ${subtopic.title}"
                            currentQuestion.subtopicTitle.isNotBlank() -> "${subject.title} • ${currentQuestion.subtopicTitle}"
                            else -> "${subject.title} • ${topic.title}"
                        }
                        Text(
                            text = subtitleText,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = TextMediumGray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { showExitConfirmDialog = true },
                        modifier = Modifier.testTag("practice_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Exit Practice",
                            tint = TextDarkHeading
                        )
                    }
                },
                actions = {
                    // Timer indicator
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = LightCardBackgroundVariant,
                        border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.3f)),
                        modifier = Modifier.padding(end = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            val minutes = totalElapsedSeconds / 60
                            val seconds = totalElapsedSeconds % 60
                            Text(
                                text = String.format("%02d:%02d", minutes, seconds),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBlue
                                ),
                                modifier = Modifier.testTag("practice_timer_text")
                            )
                        }
                    }

                    // Palette Toggle Button
                    IconButton(
                        onClick = { showPaletteSheet = true },
                        modifier = Modifier.testTag("palette_toggle_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.GridView,
                            contentDescription = "Question Palette",
                            tint = PrimaryBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LightBackground,
                    titleContentColor = TextDarkHeading
                )
            )
        },
        bottomBar = {
            // Action Navigation Buttons (Previous, Clear, Mark for Review & Next, Save & Next / Submit)
            Surface(
                color = LightSurface,
                border = BorderStroke(1.dp, BorderLight),
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    // First row: Clear Response & Mark for Review
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { clearResponse() },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, BorderSubtle),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = TextMediumGray
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("clear_response_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Clear",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }

                        OutlinedButton(
                            onClick = { markForReviewAndNext() },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, PaletteMarkedBg.copy(alpha = 0.6f)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = PaletteMarkedBg
                            ),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                            modifier = Modifier
                                .weight(1.3f)
                                .height(44.dp)
                                .testTag("mark_review_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.BookmarkBorder,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Mark & Next",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Second row: Previous & Save and Next / Submit
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { previousQuestion() },
                            enabled = currentQuestionIndex > 0,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, if (currentQuestionIndex > 0) PrimaryBlue else BorderLight),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = PrimaryBlue,
                                disabledContentColor = TextMediumGray.copy(alpha = 0.4f)
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("previous_question_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Previous",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }

                        val isLastQuestion = currentQuestionIndex == questions.size - 1
                        Button(
                            onClick = {
                                if (isLastQuestion) {
                                    showSubmitDialog = true
                                } else {
                                    saveAndNext()
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isLastQuestion) Color(0xFF059669) else PrimaryBlue,
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            modifier = Modifier
                                .weight(1.3f)
                                .height(46.dp)
                                .testTag(if (isLastQuestion) "submit_practice_button" else "save_next_button")
                        ) {
                            Text(
                                text = if (isLastQuestion) "Submit Practice" else "Save & Next",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = if (isLastQuestion) Icons.Default.Check else Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
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
                    .widthIn(max = 600.dp)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Info Bar: Subtopic Breadcrumb + Difficulty Badge + Palette shortcut
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Difficulty Tag
                    val (diffColor, diffBg) = when (currentQuestion.difficulty) {
                        Difficulty.EASY -> DifficultyEasy to DifficultyEasyBg
                        Difficulty.MODERATE -> DifficultyModerate to DifficultyModerateBg
                        Difficulty.HARD -> DifficultyHard to DifficultyHardBg
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = diffBg,
                        border = BorderStroke(1.dp, diffColor.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = currentQuestion.difficulty.label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = diffColor
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    // Marks indicator (SSC pattern: +2.0, -0.50)
                    Text(
                        text = "+2.0  /  -0.50",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TextMediumGray
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Question Box Container
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = LightSurface),
                    border = BorderStroke(1.dp, BorderLight),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("question_card_${currentQuestion.id}")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        // Question Number and status tag
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Q.${currentQuestionIndex + 1}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = PrimaryBlue
                                )
                            )

                            // Current status indicator pill
                            val currentPalState = currentState.paletteState
                            val (statusBg, statusFg) = when (currentPalState) {
                                PaletteState.ANSWERED -> PaletteAnsweredBg to PaletteAnsweredText
                                PaletteState.MARKED_FOR_REVIEW -> PaletteMarkedBg to PaletteMarkedText
                                PaletteState.NOT_ANSWERED -> PaletteNotAnsweredBg to PaletteNotAnsweredText
                                PaletteState.NOT_VISITED -> PaletteNotVisitedBg to PaletteNotVisitedText
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = statusBg,
                                modifier = Modifier.padding(2.dp)
                            ) {
                                Text(
                                    text = currentPalState.label,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = statusFg
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Question Text
                        Text(
                            text = currentQuestion.questionText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                lineHeight = 24.sp
                            ),
                            color = TextDarkHeading,
                            modifier = Modifier.testTag("question_text")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Options Section Header
                Text(
                    text = "Select one of the following options:",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextMediumGray,
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )

                // 4 Options A, B, C, D
                val optionLabels = listOf("A", "B", "C", "D")
                currentQuestion.options.forEachIndexed { optIndex, optionText ->
                    val isSelected = currentState.selectedOptionIndex == optIndex
                    val optionLabel = optionLabels.getOrElse(optIndex) { "${optIndex + 1}" }

                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) LightCardBackgroundVariant else LightSurface
                        ),
                        border = BorderStroke(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) PrimaryBlue else BorderLight
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (isSelected) 2.dp else 1.dp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { selectOption(optIndex) }
                            .testTag("option_${optIndex}_button")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Option Label Circle (A, B, C, D)
                            Surface(
                                modifier = Modifier.size(32.dp),
                                shape = CircleShape,
                                color = if (isSelected) PrimaryBlue else LightCardBackground,
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) PrimaryBlue else BorderSubtle
                                )
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = optionLabel,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else TextDarkHeading
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // Option Text
                            Text(
                                text = optionText,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 15.sp,
                                    lineHeight = 20.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                ),
                                color = if (isSelected) PrimaryBlueDark else TextDarkBody,
                                modifier = Modifier.weight(1f)
                            )

                            // Radio Button
                            RadioButton(
                                selected = isSelected,
                                onClick = { selectOption(optIndex) },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = PrimaryBlue,
                                    unselectedColor = BorderSubtle
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    // Question Palette Bottom Sheet (1 to 20 Grid)
    if (showPaletteSheet) {
        ModalBottomSheet(
            onDismissRequest = { showPaletteSheet = false },
            sheetState = sheetState,
            containerColor = LightSurface
        ) {
            QuestionPaletteContent(
                questions = questions,
                userResponses = userResponses,
                currentIndex = currentQuestionIndex,
                onSelectQuestion = { index ->
                    currentQuestionIndex = index
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        showPaletteSheet = false
                    }
                },
                onSubmitClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        showPaletteSheet = false
                        showSubmitDialog = true
                    }
                },
                onClose = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        showPaletteSheet = false
                    }
                }
            )
        }
    }

    // Submit Practice Confirmation Dialog
    if (showSubmitDialog) {
        val answeredCount = userResponses.values.count { it.selectedOptionIndex != null }
        val markedCount = userResponses.values.count { it.isMarkedForReview }
        val unattemptedCount = questions.size - answeredCount

        AlertDialog(
            onDismissRequest = { showSubmitDialog = false },
            title = {
                Text(
                    text = "Submit Practice Test?",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextDarkHeading
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Are you sure you want to finish this practice session? Here is your summary:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextDarkBody
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = LightCardBackground),
                        border = BorderStroke(1.dp, BorderLight)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            SummaryRow(label = "Total Questions", value = "${questions.size}")
                            SummaryRow(label = "Answered", value = "$answeredCount", color = PaletteAnsweredBg)
                            SummaryRow(label = "Marked for Review", value = "$markedCount", color = PaletteMarkedBg)
                            SummaryRow(label = "Unattempted", value = "$unattemptedCount", color = PaletteNotAnsweredBg)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSubmitDialog = false
                        submitExam()
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    modifier = Modifier.testTag("confirm_submit_dialog_button")
                ) {
                    Text("Submit Practice", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showSubmitDialog = false },
                    modifier = Modifier.testTag("cancel_submit_dialog_button")
                ) {
                    Text("Resume Practice", color = TextMediumGray)
                }
            },
            containerColor = LightSurface
        )
    }

    // Exit Confirmation Dialog
    if (showExitConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showExitConfirmDialog = false },
            title = {
                Text(
                    text = "Leave Practice?",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextDarkHeading
                )
            },
            text = {
                Text(
                    text = "Your current practice progress will not be saved if you leave now.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextDarkBody
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showExitConfirmDialog = false
                        onBackToTopics()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                ) {
                    Text("Leave", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitConfirmDialog = false }) {
                    Text("Stay", color = TextMediumGray)
                }
            },
            containerColor = LightSurface
        )
    }
}

@Composable
fun QuestionPaletteContent(
    questions: List<PracticeQuestion>,
    userResponses: Map<Int, UserQuestionState>,
    currentIndex: Int,
    onSelectQuestion: (Int) -> Unit,
    onSubmitClick: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val answeredCount = userResponses.values.count { it.selectedOptionIndex != null }
    val markedCount = userResponses.values.count { it.isMarkedForReview }
    val notAnsweredCount = userResponses.values.count { it.isVisited && it.selectedOptionIndex == null && !it.isMarkedForReview }
    val notVisitedCount = questions.size - userResponses.values.count { it.isVisited }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .testTag("question_palette_sheet")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Question Palette (${questions.size})",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = TextDarkHeading
            )
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close palette",
                    tint = TextDarkHeading
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Palette Status Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LegendItem(count = answeredCount, label = "Answered", bg = PaletteAnsweredBg, fg = Color.White)
            LegendItem(count = markedCount, label = "Marked", bg = PaletteMarkedBg, fg = Color.White)
            LegendItem(count = notAnsweredCount, label = "Not Ans.", bg = PaletteNotAnsweredBg, fg = Color.White)
            LegendItem(count = notVisitedCount, label = "Not Visited", bg = PaletteNotVisitedBg, fg = PaletteNotVisitedText, border = PaletteNotVisitedBorder)
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = BorderLight)
        Spacer(modifier = Modifier.height(16.dp))

        // 1 to 20 Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
        ) {
            itemsIndexed(questions) { index, _ ->
                val state = userResponses[index] ?: UserQuestionState(index + 1)
                val paletteState = state.paletteState
                val isCurrent = index == currentIndex

                val (bgColor, textColor, borderColor) = when (paletteState) {
                    PaletteState.ANSWERED -> Triple(PaletteAnsweredBg, PaletteAnsweredText, PaletteAnsweredBg)
                    PaletteState.MARKED_FOR_REVIEW -> Triple(PaletteMarkedBg, PaletteMarkedText, PaletteMarkedBg)
                    PaletteState.NOT_ANSWERED -> Triple(PaletteNotAnsweredBg, PaletteNotAnsweredText, PaletteNotAnsweredBg)
                    PaletteState.NOT_VISITED -> Triple(PaletteNotVisitedBg, PaletteNotVisitedText, PaletteNotVisitedBorder)
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = bgColor,
                    border = BorderStroke(
                        width = if (isCurrent) 2.5.dp else 1.dp,
                        color = if (isCurrent) PrimaryBlue else borderColor
                    ),
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onSelectQuestion(index) }
                        .testTag("palette_item_${index + 1}")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "${index + 1}",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            ),
                            color = textColor
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Submit Button in Palette
        Button(
            onClick = onSubmitClick,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("palette_submit_button")
        ) {
            Text(
                text = "Submit Practice Test",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun LegendItem(
    count: Int,
    label: String,
    bg: Color,
    fg: Color,
    border: Color = bg
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = bg,
            border = BorderStroke(1.dp, border),
            modifier = Modifier.size(26.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "$count",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    ),
                    color = fg
                )
            }
        }
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = TextMediumGray
        )
    }
}

@Composable
private fun SummaryRow(label: String, value: String, color: Color = TextDarkHeading) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = TextDarkBody
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = color
        )
    }
}
