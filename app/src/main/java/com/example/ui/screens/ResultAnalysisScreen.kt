package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.OfflineBolt
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Timer
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.Difficulty
import com.example.data.models.PracticeSessionResult
import com.example.data.models.QuestionReviewItem
import com.example.data.models.ReviewFilter
import com.example.ui.theme.BorderLight
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
import com.example.ui.theme.LightBackground
import com.example.ui.theme.LightCardBackground
import com.example.ui.theme.LightCardBackgroundVariant
import com.example.ui.theme.LightSurface
import com.example.ui.theme.OfflineBadgeBg
import com.example.ui.theme.OfflineBadgeBorder
import com.example.ui.theme.OfflineBadgeText
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PrimaryBlueDark
import com.example.ui.theme.TextDarkBody
import com.example.ui.theme.TextDarkHeading
import com.example.ui.theme.TextMediumGray
import com.example.ui.theme.UnattemptedGray
import com.example.ui.theme.UnattemptedGrayBg
import com.example.ui.theme.UnattemptedGrayBorder
import com.example.ui.theme.WrongRed
import com.example.ui.theme.WrongRedBg
import com.example.ui.theme.WrongRedBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultAnalysisScreen(
    result: PracticeSessionResult,
    onPracticeAgain: () -> Unit,
    onBackToSubtopics: () -> Unit = {},
    onBackToTopics: () -> Unit = onBackToSubtopics,
    modifier: Modifier = Modifier
) {
    var selectedFilter by rememberSaveable { mutableStateOf(ReviewFilter.ALL) }
    val expandedExplanations = remember { mutableStateMapOf<Int, Boolean>() }

    val filteredQuestions = when (selectedFilter) {
        ReviewFilter.ALL -> result.questionReviews
        ReviewFilter.CORRECT -> result.questionReviews.filter { it.isCorrect }
        ReviewFilter.WRONG -> result.questionReviews.filter { it.isAttempted && !it.isCorrect }
        ReviewFilter.UNATTEMPTED -> result.questionReviews.filter { !it.isAttempted }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("result_analysis_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Result Analysis",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextDarkHeading,
                            modifier = Modifier.testTag("result_analysis_title")
                        )
                        val subtitle = if (result.subtopicTitle.isNotBlank() && result.subtopicTitle != result.topicTitle) {
                            "${result.subjectTitle} • ${result.topicTitle} • ${result.subtopicTitle}"
                        } else {
                            "${result.subjectTitle} • ${result.topicTitle}"
                        }
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = TextMediumGray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackToTopics,
                        modifier = Modifier.testTag("result_back_to_subtopics_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to topics",
                            tint = TextDarkHeading
                        )
                    }
                },
                actions = {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = OfflineBadgeBg,
                        border = BorderStroke(1.dp, OfflineBadgeBorder),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.OfflineBolt,
                                contentDescription = null,
                                tint = OfflineBadgeText,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Offline",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = OfflineBadgeText
                                )
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LightBackground,
                    titleContentColor = TextDarkHeading
                )
            )
        },
        bottomBar = {
            Surface(
                color = LightSurface,
                border = BorderStroke(1.dp, BorderLight),
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onBackToTopics,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, PrimaryBlue),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryBlue),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("result_back_subtopics_btn")
                    ) {
                        Text(
                            text = "Back to Topics",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Button(
                        onClick = onPracticeAgain,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("practice_again_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Replay,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Practice Again",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
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
            LazyColumn(
                modifier = Modifier
                    .widthIn(max = 640.dp)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Section 1: Summary Performance Card
                item {
                    PerformanceSummaryCard(result = result)
                }

                // Section 2: Review Answers Header & Filters
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Review Answers (${result.totalQuestions})",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextDarkHeading,
                            modifier = Modifier.testTag("review_answers_section_title")
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Analyze questions, correct solutions, and time spent",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMediumGray
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Filter Chips
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
                                    modifier = Modifier.testTag("filter_chip_${filter.name.lowercase()}")
                                )
                            }
                        }
                    }
                }

                // Section 3: Question Review Items
                items(filteredQuestions, key = { it.question.id }) { item ->
                    val isExpanded = expandedExplanations[item.question.id] ?: true

                    QuestionReviewCard(
                        reviewItem = item,
                        isExplanationExpanded = isExpanded,
                        onToggleExplanation = {
                            expandedExplanations[item.question.id] = !isExpanded
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PerformanceSummaryCard(result: PracticeSessionResult) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = LightSurface),
        border = BorderStroke(1.dp, BorderLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("performance_summary_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header with score & accuracy
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Score & Accuracy",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextDarkHeading
                    )
                    Text(
                        text = "SSC Tier-1 Practice Standard",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMediumGray
                    )
                }

                // Accuracy circular tag
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = LightCardBackgroundVariant,
                    border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = String.format("%.1f%%", result.accuracyPercent),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = PrimaryBlue
                            ),
                            modifier = Modifier.testTag("result_accuracy_text")
                        )
                        Text(
                            text = "Accuracy",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                color = TextMediumGray
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = BorderLight)
            Spacer(modifier = Modifier.height(16.dp))

            // 4 Key Stats Grid (Attempted, Correct, Wrong, Unattempted)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatPill(
                    label = "Attempted",
                    value = "${result.attempted}/${result.totalQuestions}",
                    bg = LightCardBackground,
                    fg = TextDarkHeading
                )
                StatPill(
                    label = "Correct",
                    value = "${result.correct}",
                    bg = CorrectGreenBg,
                    fg = CorrectGreen,
                    border = CorrectGreenBorder
                )
                StatPill(
                    label = "Wrong",
                    value = "${result.wrong}",
                    bg = WrongRedBg,
                    fg = WrongRed,
                    border = WrongRedBorder
                )
                StatPill(
                    label = "Unattempted",
                    value = "${result.unattempted}",
                    bg = UnattemptedGrayBg,
                    fg = UnattemptedGray,
                    border = UnattemptedGrayBorder
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Time Breakdown Row (Total Time & Avg Time per Question)
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = LightCardBackground),
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Total Time
                    val minutes = result.totalTimeSeconds / 60
                    val seconds = result.totalTimeSeconds % 60
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "Total Time",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = TextMediumGray
                            )
                            Text(
                                text = String.format("%02d min %02d sec", minutes, seconds),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextDarkHeading
                                ),
                                modifier = Modifier.testTag("result_total_time_text")
                            )
                        }
                    }

                    // Average Time per Question
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Avg Time / Q",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = TextMediumGray
                        )
                        Text(
                            text = String.format("%.1f sec", result.averageTimePerQuestionSeconds),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextDarkHeading
                            ),
                            modifier = Modifier.testTag("result_avg_time_text")
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatPill(
    label: String,
    value: String,
    bg: Color,
    fg: Color,
    border: Color = BorderLight
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = bg,
        border = BorderStroke(1.dp, border),
        modifier = Modifier.width(72.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = fg,
                    fontSize = 15.sp
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    color = TextMediumGray
                )
            )
        }
    }
}

@Composable
fun QuestionReviewCard(
    reviewItem: QuestionReviewItem,
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
            .testTag("review_card_${q.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top Metadata Row: Question Number, Difficulty, Status, Time Spent
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Q.${q.questionNumber}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimaryBlue
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    // Difficulty Tag
                    val (diffColor, diffBg) = when (q.difficulty) {
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
                            text = q.difficulty.label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = diffColor
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Status Badge & Time spent
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Time Spent
                    Text(
                        text = "⏱ ${reviewItem.timeSpentSeconds}s",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            color = TextMediumGray
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))

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

            Spacer(modifier = Modifier.height(14.dp))

            // 4 Options with user answer & correct answer status
            val optionLabels = listOf("A", "B", "C", "D")
            q.options.forEachIndexed { optIndex, optionText ->
                val isCorrectAnswer = optIndex == reviewItem.correctOptionIndex
                val isUserSelected = optIndex == reviewItem.selectedOptionIndex
                val optionLabel = optionLabels.getOrElse(optIndex) { "${optIndex + 1}" }

                val (cardBg, borderColor, indicatorIcon) = when {
                    isCorrectAnswer -> Triple(CorrectGreenBg, CorrectGreen, Icons.Default.Check)
                    isUserSelected && !isCorrectAnswer -> Triple(WrongRedBg, WrongRed, Icons.Default.Close)
                    else -> Triple(LightSurface, BorderLight, null)
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
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Option Letter
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

                        // Option Content
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

                        // Visual tag for Correct / Your Answer
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
                                text = "Your Answer",
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

            // Step-by-Step Solution Accordion
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = LightCardBackgroundVariant),
                border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.2f)),
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
                                    color = PrimaryBlue
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
        }
    }
}
