package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BorderMedium
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CorrectGreen
import com.example.ui.theme.CorrectGreenBg
import com.example.ui.theme.CorrectGreenBorder
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
import com.example.ui.theme.WrongRed
import com.example.ui.theme.WrongRedBg
import com.example.ui.theme.WrongRedBorder

data class ExamSectionInfo(
    val sectionNumber: Int,
    val name: String,
    val hindiName: String,
    val questionCount: Int,
    val maxMarks: Int,
    val durationMinutes: Int,
    val icon: ImageVector
)

val SSC_CGL_TIER_1_SECTIONS = listOf(
    ExamSectionInfo(
        sectionNumber = 1,
        name = "General Intelligence & Reasoning",
        hindiName = "सामान्य बुद्धिमत्ता एवं तर्कशक्ति",
        questionCount = 25,
        maxMarks = 50,
        durationMinutes = 15,
        icon = Icons.Default.Psychology
    ),
    ExamSectionInfo(
        sectionNumber = 2,
        name = "General Awareness",
        hindiName = "सामान्य जागरूकता",
        questionCount = 25,
        maxMarks = 50,
        durationMinutes = 15,
        icon = Icons.Default.Public
    ),
    ExamSectionInfo(
        sectionNumber = 3,
        name = "Quantitative Aptitude",
        hindiName = "मात्रात्मक अभिरुचि",
        questionCount = 25,
        maxMarks = 50,
        durationMinutes = 15,
        icon = Icons.Default.Calculate
    ),
    ExamSectionInfo(
        sectionNumber = 4,
        name = "English Comprehension",
        hindiName = "अंग्रेजी समझ (English Only)",
        questionCount = 25,
        maxMarks = 50,
        durationMinutes = 15,
        icon = Icons.Default.MenuBook
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockTestOverviewScreen(
    onStartTest: (defaultLanguage: String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedLanguage by rememberSaveable { mutableStateOf("English") }
    val scrollState = rememberScrollState()

    BackHandler {
        onBack()
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("mock_test_pre_overview_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "SSC CGL Tier I Mock Test",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            ),
                            color = TextDarkHeading
                        )
                        Text(
                            text = "Computer Based Examination (CBT)",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = PrimaryBlue
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("mock_overview_back_button")
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
        bottomBar = {
            Surface(
                color = LightSurface,
                border = BorderStroke(1.dp, BorderLight),
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = { onStartTest(selectedLanguage) },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryBlue,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .widthIn(max = 520.dp)
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("ready_to_begin_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "I am Ready to Begin",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
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
                    .widthIn(max = 540.dp)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header Title Card
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = LightSurface),
                    border = BorderStroke(1.2.dp, BorderLight),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("mock_test_header_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = LightCardBackgroundVariant,
                                    border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.25f))
                                ) {
                                    Text(
                                        text = "OFFICIAL PATTERN • TIER I",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            color = PrimaryBlueDark
                                        ),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "SSC CGL Tier I Mock Test",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 20.sp
                                    ),
                                    color = TextDarkHeading
                                )
                            }

                            Surface(
                                shape = CircleShape,
                                color = LightCardBackgroundVariant,
                                border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.2f)),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Computer,
                                        contentDescription = null,
                                        tint = PrimaryBlue,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = BorderLight)
                        Spacer(modifier = Modifier.height(14.dp))

                        // Key Highlights Grid (Questions, Marks, Duration)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Total Questions
                            ExamMetricPill(
                                label = "Total Questions",
                                value = "100",
                                icon = Icons.Default.Quiz,
                                modifier = Modifier.weight(1f),
                                testTag = "metric_total_questions"
                            )

                            // Maximum Marks
                            ExamMetricPill(
                                label = "Maximum Marks",
                                value = "200",
                                icon = Icons.Default.Speed,
                                modifier = Modifier.weight(1f),
                                testTag = "metric_max_marks"
                            )

                            // Total Duration
                            ExamMetricPill(
                                label = "Total Duration",
                                value = "60 mins",
                                icon = Icons.Default.Timer,
                                modifier = Modifier.weight(1f),
                                testTag = "metric_total_duration"
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Marking Scheme Row (+2 marks, -0.50 marks)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Correct Answer +2 marks
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = CorrectGreenBg,
                                border = BorderStroke(1.dp, CorrectGreenBorder),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("metric_marking_correct")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = CorrectGreen,
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "Correct Answer",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Medium
                                            ),
                                            color = TextDarkBody
                                        )
                                        Text(
                                            text = "+2 marks",
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 14.sp
                                            ),
                                            color = CorrectGreen
                                        )
                                    }
                                }
                            }

                            // Wrong Answer -0.50 marks
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = WrongRedBg,
                                border = BorderStroke(1.dp, WrongRedBorder),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("metric_marking_wrong")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = WrongRed,
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.RemoveCircleOutline,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "Wrong Answer",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Medium
                                            ),
                                            color = TextDarkBody
                                        )
                                        Text(
                                            text = "−0.50 marks",
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 14.sp
                                            ),
                                            color = WrongRed
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Section-Wise Table Card
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = LightSurface),
                    border = BorderStroke(1.2.dp, BorderLight),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("section_wise_table_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Section-Wise Scheme",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                ),
                                color = TextDarkHeading
                            )

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = LightCardBackgroundVariant,
                                border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.2f))
                            ) {
                                Text(
                                    text = "4 Sections",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = PrimaryBlueDark
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Professional CBT Table Container
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = LightSurface,
                            border = BorderStroke(1.dp, BorderLight),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                // Table Header Row
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(LightCardBackgroundVariant)
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Section",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        ),
                                        color = PrimaryBlueDark,
                                        modifier = Modifier.weight(2.2f)
                                    )
                                    Text(
                                        text = "Questions",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            textAlign = TextAlign.Center
                                        ),
                                        color = PrimaryBlueDark,
                                        modifier = Modifier.weight(1.2f)
                                    )
                                    Text(
                                        text = "Marks",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            textAlign = TextAlign.Center
                                        ),
                                        color = PrimaryBlueDark,
                                        modifier = Modifier.weight(1.0f)
                                    )
                                    Text(
                                        text = "Time",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            textAlign = TextAlign.End
                                        ),
                                        color = PrimaryBlueDark,
                                        modifier = Modifier.weight(1.1f)
                                    )
                                }

                                HorizontalDivider(color = BorderLight)

                                // Table Rows
                                SSC_CGL_TIER_1_SECTIONS.forEachIndexed { index, sec ->
                                    val isEven = index % 2 == 0
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(if (isEven) LightSurface else LightBackground)
                                            .padding(horizontal = 12.dp, vertical = 11.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Section Name & Number
                                        Column(modifier = Modifier.weight(2.2f)) {
                                            Text(
                                                text = sec.name,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 12.5.sp,
                                                    lineHeight = 16.sp
                                                ),
                                                color = TextDarkHeading
                                            )
                                        }

                                        // Questions Count
                                        Text(
                                            text = "${sec.questionCount}",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                textAlign = TextAlign.Center
                                            ),
                                            color = TextDarkBody,
                                            modifier = Modifier.weight(1.2f)
                                        )

                                        // Max Marks
                                        Text(
                                            text = "${sec.maxMarks}",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                textAlign = TextAlign.Center
                                            ),
                                            color = TextDarkBody,
                                            modifier = Modifier.weight(1.0f)
                                        )

                                        // Duration
                                        Text(
                                            text = "${sec.durationMinutes} min",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 12.sp,
                                                textAlign = TextAlign.End
                                            ),
                                            color = PrimaryBlueDark,
                                            modifier = Modifier.weight(1.1f)
                                        )
                                    }

                                    if (index < SSC_CGL_TIER_1_SECTIONS.lastIndex) {
                                        HorizontalDivider(color = BorderLight.copy(alpha = 0.6f))
                                    }
                                }

                                HorizontalDivider(color = BorderMedium)

                                // Total Row
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(LightCardBackgroundVariant)
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Total",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 13.sp
                                        ),
                                        color = TextDarkHeading,
                                        modifier = Modifier.weight(2.2f)
                                    )
                                    Text(
                                        text = "100",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 13.sp,
                                            textAlign = TextAlign.Center
                                        ),
                                        color = PrimaryBlueDark,
                                        modifier = Modifier.weight(1.2f)
                                    )
                                    Text(
                                        text = "200",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 13.sp,
                                            textAlign = TextAlign.Center
                                        ),
                                        color = PrimaryBlueDark,
                                        modifier = Modifier.weight(1.0f)
                                    )
                                    Text(
                                        text = "60 min",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 13.sp,
                                            textAlign = TextAlign.End
                                        ),
                                        color = PrimaryBlueDark,
                                        modifier = Modifier.weight(1.1f)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Fixed Section Timer Callout
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = LightCardBackgroundVariant,
                            border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.25f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = PrimaryBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Each section has its own fixed 15-minute timer.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        color = PrimaryBlueDark
                                    )
                                )
                            }
                        }
                    }
                }

                // Default Language Selection Card
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = LightSurface),
                    border = BorderStroke(1.2.dp, BorderLight),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("language_selection_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Choose your default language",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                ),
                                color = TextDarkHeading
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Language Selection Options: English and Hindi
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // English Option
                            val isEnglishSelected = selectedLanguage == "English"
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isEnglishSelected) LightCardBackgroundVariant else LightSurface,
                                border = BorderStroke(
                                    width = if (isEnglishSelected) 1.5.dp else 1.dp,
                                    color = if (isEnglishSelected) PrimaryBlue else BorderLight
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { selectedLanguage = "English" }
                                    .testTag("language_selector_english")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isEnglishSelected,
                                        onClick = { selectedLanguage = "English" },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = PrimaryBlue,
                                            unselectedColor = TextMediumGray
                                        ),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "English",
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = if (isEnglishSelected) FontWeight.Bold else FontWeight.Medium,
                                                fontSize = 14.sp
                                            ),
                                            color = if (isEnglishSelected) PrimaryBlueDark else TextDarkHeading
                                        )
                                        Text(
                                            text = "Default",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 10.sp,
                                                color = TextMediumGray
                                            )
                                        )
                                    }
                                }
                            }

                            // Hindi Option
                            val isHindiSelected = selectedLanguage == "Hindi"
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isHindiSelected) LightCardBackgroundVariant else LightSurface,
                                border = BorderStroke(
                                    width = if (isHindiSelected) 1.5.dp else 1.dp,
                                    color = if (isHindiSelected) PrimaryBlue else BorderLight
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { selectedLanguage = "Hindi" }
                                    .testTag("language_selector_hindi")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isHindiSelected,
                                        onClick = { selectedLanguage = "Hindi" },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = PrimaryBlue,
                                            unselectedColor = TextMediumGray
                                        ),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "हिन्दी (Hindi)",
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = if (isHindiSelected) FontWeight.Bold else FontWeight.Medium,
                                                fontSize = 14.sp
                                            ),
                                            color = if (isHindiSelected) PrimaryBlueDark else TextDarkHeading
                                        )
                                        Text(
                                            text = "द्विभाषी",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 10.sp,
                                                color = TextMediumGray
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // English Comprehension Note
                        Text(
                            text = "Note: English Comprehension section will be available in English only.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.5.sp,
                                color = TextMediumGray,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun ExamMetricPill(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = LightCardBackgroundVariant,
        border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.2f)),
        modifier = modifier.testTag(testTag)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                ),
                color = PrimaryBlueDark,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = TextMediumGray,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
