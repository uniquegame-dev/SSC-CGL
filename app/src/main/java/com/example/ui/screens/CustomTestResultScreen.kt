package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.CustomTestResult
import com.example.data.models.Difficulty
import com.example.data.models.QuestionReviewItem
import com.example.data.models.ReviewFilter
import com.example.data.models.SubjectTestPerformance
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BorderMedium
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CorrectGreen
import com.example.ui.theme.CorrectGreenBg
import com.example.ui.theme.CorrectGreenBorder
import com.example.ui.theme.DifficultyEasy
import com.example.ui.theme.DifficultyEasyBg
import com.example.ui.theme.DifficultyHard
import com.example.ui.theme.DifficultyHardBg
import com.example.ui.theme.DifficultyModerate
import com.example.ui.theme.DifficultyModerateBg
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.LightBackground
import com.example.ui.theme.LightCardBackground
import com.example.ui.theme.LightCardBackgroundVariant
import com.example.ui.theme.LightSurface
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueDark
import com.example.ui.theme.PrimaryBlueLight
import com.example.ui.theme.ShortcutAmberBg
import com.example.ui.theme.ShortcutAmberBorder
import com.example.ui.theme.ShortcutAmberIcon
import com.example.ui.theme.ShortcutAmberText
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextDarkBody
import com.example.ui.theme.TextDarkHeading
import com.example.ui.theme.TextMediumGray
import com.example.ui.theme.UnattemptedGray
import com.example.ui.theme.UnattemptedGrayBg
import com.example.ui.theme.UnattemptedGrayBorder
import com.example.ui.theme.WarningAmber
import com.example.ui.theme.WrongRed
import com.example.ui.theme.WrongRedBg
import com.example.ui.theme.WrongRedBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTestResultScreen(
    result: CustomTestResult,
    onRetakeTest: () -> Unit,
    onNewCustomTest: () -> Unit,
    onBackToTestMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isReviewMode by rememberSaveable { mutableStateOf(false) }

    if (isReviewMode) {
        CustomTestReviewAnswersView(
            result = result,
            onBackToSummary = { isReviewMode = false },
            modifier = modifier
        )
    } else {
        CustomTestSummaryView(
            result = result,
            onOpenReview = { isReviewMode = true },
            onRetakeTest = onRetakeTest,
            onNewCustomTest = onNewCustomTest,
            onBackToTestMenu = onBackToTestMenu,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomTestSummaryView(
    result: CustomTestResult,
    onOpenReview: () -> Unit,
    onRetakeTest: () -> Unit,
    onNewCustomTest: () -> Unit,
    onBackToTestMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    BackHandler {
        onBackToTestMenu()
    }

    val timeMinutes = result.totalTimeSeconds / 60
    val timeSecs = result.totalTimeSeconds % 60
    val formattedTime = String.format("%02dm %02ds", timeMinutes, timeSecs)

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("custom_test_result_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Test Result",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextDarkHeading
                        )
                        Text(
                            text = if (result.isTimed) "Timed Test • ${result.totalQuestions} Questions" else "Practice Mode • ${result.totalQuestions} Questions",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = TextMediumGray
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackToTestMenu,
                        modifier = Modifier.testTag("result_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextDarkHeading
                        )
                    }
                },
                actions = {
                    OfflineBadge()
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
                    .verticalScroll(scrollState)
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Hero Score & Accuracy Card
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = LightSurface),
                    border = BorderStroke(1.2.dp, BorderLight),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("result_score_hero_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Score & Performance Summary",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextDarkHeading
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Score & Accuracy Side by Side
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Total Score
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("result_total_score")
                            ) {
                                Text(
                                    text = String.format("%.1f", result.totalScore),
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 32.sp
                                    ),
                                    color = if (result.totalScore >= 0) PrimaryBlue else ErrorRed
                                )
                                Text(
                                    text = "Score / ${result.maxScore.toInt()} Marks",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 12.sp
                                    ),
                                    color = TextMediumGray
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .height(48.dp)
                                    .width(1.dp)
                                    .background(BorderLight)
                            )

                            // Accuracy
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("result_accuracy")
                            ) {
                                Text(
                                    text = String.format("%.1f%%", result.accuracyPercent),
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 32.sp
                                    ),
                                    color = if (result.accuracyPercent >= 70) SuccessGreen else if (result.accuracyPercent >= 40) PrimaryBlue else WarningAmber
                                )
                                Text(
                                    text = "Accuracy",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 12.sp
                                    ),
                                    color = TextMediumGray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = BorderLight)
                        Spacer(modifier = Modifier.height(14.dp))

                        // Stats Summary Row 1 (Attempted, Correct, Wrong, Total Time)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StatPill(
                                label = "Attempted",
                                value = "${result.attempted}/${result.totalQuestions}",
                                color = TextDarkHeading
                            )
                            StatPill(
                                label = "Correct (+2.0)",
                                value = "${result.correct}",
                                color = SuccessGreen
                            )
                            StatPill(
                                label = "Wrong (-0.50)",
                                value = "${result.wrong}",
                                color = ErrorRed
                            )
                            StatPill(
                                label = "Total Time",
                                value = formattedTime,
                                color = PrimaryBlueDark
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Metric Row 2: Negative Marks Lost & Avg Time Per Question
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Negative Marks Lost Box
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = WrongRedBg,
                                border = BorderStroke(1.dp, WrongRedBorder),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("negative_marks_lost_card")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.RemoveCircleOutline,
                                        contentDescription = null,
                                        tint = WrongRed,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = String.format("-%.2f", result.negativeMarksLost),
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 14.sp
                                            ),
                                            color = WrongRed
                                        )
                                        Text(
                                            text = "Negative Marks",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Medium
                                            ),
                                            color = TextMediumGray
                                        )
                                    }
                                }
                            }

                            // Average Time per Question Box
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = LightCardBackgroundVariant,
                                border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.25f)),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("avg_time_per_question_card")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = PrimaryBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        val avgSeconds = result.averageTimePerQuestionSeconds
                                        Text(
                                            text = String.format("%.1fs / Q", avgSeconds),
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 14.sp
                                            ),
                                            color = PrimaryBlueDark
                                        )
                                        Text(
                                            text = "Avg Time / Question",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Medium
                                            ),
                                            color = TextMediumGray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Prominent Review Answers Hero Button
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryBlue),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onOpenReview() }
                        .testTag("review_answers_prominent_button")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FactCheck,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "Review Answers (${result.totalQuestions} Questions)",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    ),
                                    color = Color.White
                                )
                                Text(
                                    text = "With detailed explanations & shortcut tricks",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.85f)
                                    )
                                )
                            }
                        }

                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = PrimaryBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                // Section: Subject-wise Performance Breakdown
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Subject-Wise Performance",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextDarkHeading
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Breakdown of questions, correct, and wrong per subject",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMediumGray
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    result.subjectBreakdown.forEach { subjectPerf ->
                        SubjectPerformanceCard(subjectPerf = subjectPerf)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Action Buttons
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Button 1: Retake Test
                    Button(
                        onClick = onRetakeTest,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LightCardBackgroundVariant,
                            contentColor = PrimaryBlue
                        ),
                        border = BorderStroke(1.2.dp, PrimaryBlue),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("retake_test_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Replay,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Retake Test",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    // Button 2: Create New Custom Test
                    OutlinedButton(
                        onClick = onNewCustomTest,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, BorderMedium),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextDarkHeading),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("new_custom_test_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Customize New Test",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    // Button 3: Back to Test Tab
                    TextButton(
                        onClick = onBackToTestMenu,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("back_to_test_tab_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            tint = TextMediumGray,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Back to Tests",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = TextMediumGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomTestReviewAnswersView(
    result: CustomTestResult,
    onBackToSummary: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by rememberSaveable { mutableStateOf(ReviewFilter.ALL) }
    val expandedExplanations = remember { mutableStateMapOf<Int, Boolean>() }

    BackHandler {
        onBackToSummary()
    }

    val filteredQuestions = when (selectedFilter) {
        ReviewFilter.ALL -> result.questionReviews
        ReviewFilter.CORRECT -> result.questionReviews.filter { it.isCorrect }
        ReviewFilter.WRONG -> result.questionReviews.filter { it.isAttempted && !it.isCorrect }
        ReviewFilter.UNATTEMPTED -> result.questionReviews.filter { !it.isAttempted }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("custom_test_review_answers_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Review Answers",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextDarkHeading
                        )
                        Text(
                            text = "${result.totalQuestions} Questions • Score: ${String.format("%.1f", result.totalScore)}",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = PrimaryBlue
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackToSummary,
                        modifier = Modifier.testTag("review_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Summary",
                            tint = TextDarkHeading
                        )
                    }
                },
                actions = {
                    OfflineBadge()
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
            LazyColumn(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 10.dp, bottom = 28.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Filter Chips Row
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Filter Solutions",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextDarkHeading
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val filters = listOf(
                                ReviewFilter.ALL to "All (${result.totalQuestions})",
                                ReviewFilter.CORRECT to "Correct (${result.correct})",
                                ReviewFilter.WRONG to "Wrong (${result.wrong})",
                                ReviewFilter.UNATTEMPTED to "Unattempted (${result.unattempted})"
                            )

                            items(filters) { (filter, label) ->
                                val isSelected = selectedFilter == filter
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedFilter = filter },
                                    label = {
                                        Text(
                                            text = label,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                            )
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = PrimaryBlue,
                                        selectedLabelColor = Color.White,
                                        containerColor = LightSurface,
                                        labelColor = TextDarkBody
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        borderColor = if (isSelected) PrimaryBlue else BorderLight,
                                        selectedBorderColor = PrimaryBlue,
                                        enabled = true,
                                        selected = isSelected
                                    ),
                                    modifier = Modifier.testTag("review_filter_chip_${filter.name.lowercase()}")
                                )
                            }
                        }
                    }
                }

                // Question Review Cards
                itemsIndexed(filteredQuestions, key = { _, item -> item.question.id }) { index, item ->
                    val isExpanded = expandedExplanations[item.question.id] ?: true

                    CustomQuestionReviewCard(
                        reviewItem = item,
                        questionIndex = index + 1,
                        isExplanationExpanded = isExpanded,
                        onToggleExplanation = {
                            expandedExplanations[item.question.id] = !isExpanded
                        }
                    )
                }

                // Bottom Back to Summary button
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = onBackToSummary,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("review_back_to_summary_bottom_button")
                    ) {
                        Text(
                            text = "Back to Test Summary",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomQuestionReviewCard(
    reviewItem: QuestionReviewItem,
    questionIndex: Int,
    isExplanationExpanded: Boolean,
    onToggleExplanation: () -> Unit,
    modifier: Modifier = Modifier
) {
    val q = reviewItem.question

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LightSurface),
        border = BorderStroke(
            width = 1.dp,
            color = when {
                reviewItem.isCorrect -> CorrectGreenBorder
                reviewItem.isAttempted -> WrongRedBorder
                else -> BorderLight
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("custom_review_card_${q.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top Metadata Row: Question Number, Subject Tag, Difficulty, Status, Time Spent
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Q.$questionIndex",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimaryBlue
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    // Subject Pill
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = LightCardBackgroundVariant,
                        border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.2f))
                    ) {
                        Text(
                            text = q.subjectTitle,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.sp,
                                color = PrimaryBlueDark
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Status Badge & Time spent
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (reviewItem.timeSpentSeconds > 0) {
                        Text(
                            text = "⏱ ${reviewItem.timeSpentSeconds}s",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 11.sp,
                                color = TextMediumGray
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    // Status Badge
                    val (statusText, statusBg, statusColor) = when {
                        reviewItem.isCorrect -> Triple("Correct (+2.0)", CorrectGreenBg, CorrectGreen)
                        reviewItem.isAttempted -> Triple("Wrong (-0.50)", WrongRedBg, WrongRed)
                        else -> Triple("Unattempted", UnattemptedGrayBg, UnattemptedGray)
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = statusBg,
                        border = BorderStroke(1.dp, statusColor.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = statusText,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = statusColor
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Question Text
            Text(
                text = q.questionText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                ),
                color = TextDarkHeading
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 4 Options with user answer & correct answer status
            val optionLabels = listOf("A", "B", "C", "D")
            q.options.forEachIndexed { optIndex, optionText ->
                val isCorrectAnswer = optIndex == reviewItem.correctOptionIndex
                val isUserSelected = optIndex == reviewItem.selectedOptionIndex
                val optionLabel = optionLabels.getOrElse(optIndex) { "${optIndex + 1}" }

                val (cardBg, borderColor) = when {
                    isCorrectAnswer -> Pair(CorrectGreenBg, CorrectGreen)
                    isUserSelected && !isCorrectAnswer -> Pair(WrongRedBg, WrongRed)
                    else -> Pair(LightSurface, BorderLight)
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = cardBg,
                    border = BorderStroke(1.dp, borderColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Option Letter Badge
                        Surface(
                            shape = CircleShape,
                            color = when {
                                isCorrectAnswer -> CorrectGreen
                                isUserSelected -> WrongRed
                                else -> LightCardBackground
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = optionLabel,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCorrectAnswer || isUserSelected) Color.White else TextDarkHeading
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        // Option Text
                        Text(
                            text = optionText,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 13.sp,
                                fontWeight = if (isCorrectAnswer || isUserSelected) FontWeight.SemiBold else FontWeight.Normal
                            ),
                            color = when {
                                isCorrectAnswer -> Color(0xFF047857)
                                isUserSelected -> Color(0xFFB91C1C)
                                else -> TextDarkBody
                            },
                            modifier = Modifier.weight(1f)
                        )

                        // Label badge for Answer status
                        if (isCorrectAnswer && isUserSelected) {
                            Text(
                                text = "Your Answer (Correct)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    color = CorrectGreen
                                )
                            )
                        } else if (isCorrectAnswer) {
                            Text(
                                text = "Correct Answer",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    color = CorrectGreen
                                )
                            )
                        } else if (isUserSelected) {
                            Text(
                                text = "Your Answer (Wrong)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    color = WrongRed
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Step-by-Step Explanation Accordion
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = LightCardBackgroundVariant),
                border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.25f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onToggleExplanation() }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Step-by-Step Solution",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBlueDark
                                )
                            )
                        }

                        Icon(
                            imageVector = if (isExplanationExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (isExplanationExpanded) "Collapse" else "Expand",
                            tint = PrimaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    AnimatedVisibility(visible = isExplanationExpanded) {
                        Column {
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(color = PrimaryBlue.copy(alpha = 0.15f))
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = q.explanation,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 13.sp,
                                    lineHeight = 19.sp,
                                    color = TextDarkBody
                                ),
                                modifier = Modifier.testTag("solution_text_${q.id}")
                            )
                        }
                    }
                }
            }

            // Shortcut Method Card (if available)
            if (!q.shortcutMethod.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = ShortcutAmberBg,
                    border = BorderStroke(1.2.dp, ShortcutAmberBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("shortcut_card_${q.id}")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = ShortcutAmberIcon,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Shortcut & Speed Trick",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.sp,
                                    color = ShortcutAmberText
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = q.shortcutMethod,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.5.sp,
                                lineHeight = 18.sp,
                                color = TextDarkHeading,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.testTag("shortcut_text_${q.id}")
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatPill(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = color
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
            color = TextMediumGray
        )
    }
}

@Composable
private fun SubjectPerformanceCard(
    subjectPerf: SubjectTestPerformance,
    modifier: Modifier = Modifier
) {
    val subjectIcon: ImageVector = when (subjectPerf.subjectId) {
        "maths" -> Icons.Default.Functions
        "reasoning" -> Icons.Default.Psychology
        "english" -> Icons.Default.MenuBook
        "gk_ga" -> Icons.Default.Public
        else -> Icons.Default.Functions
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = LightSurface),
        border = BorderStroke(1.dp, BorderLight),
        modifier = modifier
            .fillMaxWidth()
            .testTag("subject_perf_card_${subjectPerf.subjectId}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(PrimaryBlue.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = subjectIcon,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = subjectPerf.subjectTitle,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextDarkHeading
                        )
                        Text(
                            text = "${subjectPerf.totalQuestions} Questions • Score: ${String.format("%.1f", subjectPerf.score)}",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = TextMediumGray
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = PrimaryBlueLight.copy(alpha = 0.4f)
                ) {
                    Text(
                        text = String.format("%.0f%% Acc", subjectPerf.accuracyPercent),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        color = PrimaryBlueDark,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = BorderLight.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(8.dp))

            // Correct, Wrong, Unattempted Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Correct
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = SuccessGreen,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Correct: ${subjectPerf.correct}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.5.sp
                        ),
                        color = SuccessGreen
                    )
                }

                // Wrong
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = null,
                        tint = ErrorRed,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Wrong: ${subjectPerf.wrong}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.5.sp
                        ),
                        color = ErrorRed
                    )
                }

                // Unattempted
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = null,
                        tint = TextMediumGray,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Left: ${subjectPerf.unattempted}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.5.sp
                        ),
                        color = TextMediumGray
                    )
                }
            }
        }
    }
}
