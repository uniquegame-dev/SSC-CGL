package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

data class SubjectOption(
    val id: String,
    val title: String,
    val hindiTitle: String,
    val icon: ImageVector,
    val questionCount: Int = 25
)

val AVAILABLE_CUSTOM_SUBJECTS = listOf(
    SubjectOption("maths", "Quantitative Aptitude", "गणित", Icons.Default.Functions, 25),
    SubjectOption("reasoning", "General Intelligence & Reasoning", "तर्कशक्ति", Icons.Default.Psychology, 25),
    SubjectOption("english", "English Comprehension", "अंग्रेजी", Icons.Default.MenuBook, 25),
    SubjectOption("gk_ga", "General Awareness", "सामान्य जागरूकता", Icons.Default.Public, 25)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTestSetupScreen(
    onStartTest: (selectedSubjectIds: List<String>, isTimed: Boolean) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Default all 4 subjects selected (100 questions standard)
    val selectedSubjects = remember {
        mutableStateMapOf(
            "maths" to true,
            "reasoning" to true,
            "english" to true,
            "gk_ga" to true
        )
    }

    val selectedCount = selectedSubjects.values.count { it }
    val totalQuestions = selectedCount * 25
    val totalMarks = totalQuestions * 2
    val timedMinutes = selectedCount * 15 // 15 mins per 25 questions

    val selectedIds = AVAILABLE_CUSTOM_SUBJECTS.filter { selectedSubjects[it.id] == true }.map { it.id }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("custom_test_setup_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Custom Test",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextDarkHeading
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("custom_test_back_button")
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
                // Header Info Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = LightCardBackground),
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(PrimaryBlue.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Create Custom Test",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextDarkHeading
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Select subjects below. Each selected subject adds 25 questions to your test.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextDarkBody
                            )
                        }
                    }
                }

                // Section: Select Subjects
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Select Subjects (25 Qs each)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = TextDarkHeading
                        )

                        Row {
                            TextButton(
                                onClick = {
                                    val allSelected = selectedSubjects.values.all { it }
                                    AVAILABLE_CUSTOM_SUBJECTS.forEach {
                                        selectedSubjects[it.id] = !allSelected
                                    }
                                },
                                modifier = Modifier.testTag("toggle_all_subjects_button")
                            ) {
                                val allSelected = selectedSubjects.values.all { it }
                                Text(
                                    text = if (allSelected) "Deselect All" else "Select All",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = PrimaryBlue
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // List of Subject Checkbox Cards
                    AVAILABLE_CUSTOM_SUBJECTS.forEach { subject ->
                        val isChecked = selectedSubjects[subject.id] == true

                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isChecked) LightSurface else LightCardBackgroundVariant
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (isChecked) PrimaryBlue.copy(alpha = 0.5f) else BorderLight
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .clickable {
                                    selectedSubjects[subject.id] = !isChecked
                                }
                                .testTag("subject_checkbox_card_${subject.id}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { checked ->
                                        selectedSubjects[subject.id] = checked
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = PrimaryBlue,
                                        uncheckedColor = TextMediumGray,
                                        checkmarkColor = Color.White
                                    ),
                                    modifier = Modifier.testTag("checkbox_${subject.id}")
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isChecked) PrimaryBlue.copy(alpha = 0.12f)
                                            else TextMediumGray.copy(alpha = 0.1f)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = subject.icon,
                                        contentDescription = null,
                                        tint = if (isChecked) PrimaryBlue else TextMediumGray,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = subject.title,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Medium
                                        ),
                                        color = if (isChecked) TextDarkHeading else TextMediumGray
                                    )
                                    Text(
                                        text = subject.hindiTitle,
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                        color = TextMediumGray
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isChecked) PrimaryBlueLight.copy(alpha = 0.4f) else BorderLight.copy(alpha = 0.5f),
                                    modifier = Modifier.padding(start = 6.dp)
                                ) {
                                    Text(
                                        text = "+25 Qs",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        ),
                                        color = if (isChecked) PrimaryBlueDark else TextMediumGray,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Section: Auto Total Calculation Summary Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = LightSurface),
                    border = BorderStroke(1.2.dp, if (selectedCount > 0) PrimaryBlue.copy(alpha = 0.4f) else BorderLight),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_test_auto_total_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Test Summary (Auto Total)",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = TextDarkHeading
                            )

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (selectedCount > 0) SuccessGreen.copy(alpha = 0.12f) else ErrorRed.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = if (selectedCount > 0) "$selectedCount Selected" else "0 Selected",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (selectedCount > 0) SuccessGreen else ErrorRed,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Stats Grid
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // Total Questions
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("auto_total_questions")
                            ) {
                                Text(
                                    text = "$totalQuestions",
                                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                    color = PrimaryBlue
                                )
                                Text(
                                    text = "Questions",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = TextMediumGray
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .height(36.dp)
                                    .width(1.dp)
                                    .background(BorderLight)
                            )

                            // Total Marks
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("auto_total_marks")
                            ) {
                                Text(
                                    text = "$totalMarks",
                                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                    color = TextDarkHeading
                                )
                                Text(
                                    text = "Max Marks (+2/-0.5)",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = TextMediumGray,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .height(36.dp)
                                    .width(1.dp)
                                    .background(BorderLight)
                            )

                            // Timed Duration
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("auto_total_duration")
                            ) {
                                Text(
                                    text = "${timedMinutes}m",
                                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                    color = TextDarkHeading
                                )
                                Text(
                                    text = "Timer Mode",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = TextMediumGray
                                )
                            }
                        }

                        if (selectedCount == 0) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Please select at least 1 subject to start test.",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                color = ErrorRed,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Two Action Buttons: "Start with Timer" and "Practice without Timer"
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Button 1: Start with Timer
                    Button(
                        onClick = {
                            if (selectedIds.isNotEmpty()) {
                                onStartTest(selectedIds, true)
                            }
                        },
                        enabled = selectedIds.isNotEmpty(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryBlue,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("start_with_timer_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Start with Timer (${timedMinutes} mins)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    // Button 2: Practice without Timer
                    OutlinedButton(
                        onClick = {
                            if (selectedIds.isNotEmpty()) {
                                onStartTest(selectedIds, false)
                            }
                        },
                        enabled = selectedIds.isNotEmpty(),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.2.dp, if (selectedIds.isNotEmpty()) PrimaryBlue else BorderLight),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = PrimaryBlue
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("practice_without_timer_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Practice without Timer (Untimed)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
