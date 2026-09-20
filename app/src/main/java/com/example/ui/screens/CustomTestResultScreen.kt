package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.CustomTestResult
import com.example.data.models.SubjectTestPerformance
import com.example.ui.theme.AccentBlue
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTestResultScreen(
    result: CustomTestResult,
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
                    Text(
                        text = "Test Result",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextDarkHeading
                    )
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
                    .widthIn(max = 540.dp)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
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
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Custom Test Summary",
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

                        Spacer(modifier = Modifier.height(18.dp))
                        HorizontalDivider(color = BorderLight)
                        Spacer(modifier = Modifier.height(14.dp))

                        // Stats Summary Row (Attempted, Correct, Wrong, Time)
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
                                label = "Correct",
                                value = "${result.correct}",
                                color = SuccessGreen
                            )
                            StatPill(
                                label = "Wrong",
                                value = "${result.wrong}",
                                color = ErrorRed
                            )
                            StatPill(
                                label = "Time",
                                value = formattedTime,
                                color = PrimaryBlueDark
                            )
                        }
                    }
                }

                // Section: Subject-wise Correct / Wrong Breakdown
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

                    Spacer(modifier = Modifier.height(12.dp))

                    result.subjectBreakdown.forEach { subjectPerf ->
                        SubjectPerformanceCard(subjectPerf = subjectPerf)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                // Clean Note Card
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = LightCardBackgroundVariant,
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Detailed question explanations and solutions will be enabled in an upcoming release.",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = TextDarkBody
                        )
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
                            containerColor = PrimaryBlue,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("retake_test_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Replay,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
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
                        border = BorderStroke(1.2.dp, PrimaryBlue),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryBlue),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("new_custom_test_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
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
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
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

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = BorderLight.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(10.dp))

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
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Correct: ${subjectPerf.correct}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
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
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Wrong: ${subjectPerf.wrong}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
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
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Left: ${subjectPerf.unattempted}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        ),
                        color = TextMediumGray
                    )
                }
            }
        }
    }
}
