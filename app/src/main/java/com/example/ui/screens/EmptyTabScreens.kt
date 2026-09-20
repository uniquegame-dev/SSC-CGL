package com.example.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.OfflineBolt
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.ui.theme.BorderLight
import com.example.ui.theme.LightBackground
import com.example.ui.theme.LightCardBackgroundVariant
import com.example.ui.theme.LightSurface
import com.example.ui.theme.OfflineBadgeBg
import com.example.ui.theme.OfflineBadgeBorder
import com.example.ui.theme.OfflineBadgeText
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.TextDarkHeading
import com.example.ui.theme.TextMediumGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String = "",
    onNavigateToSubjects: () -> Unit = {},
    onNavigateToTest: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val displayName = userName.trim()
    val headingGreeting = if (displayName.isNotEmpty()) "Hello, $displayName" else "Hello, Aspirant"
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Home",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        color = TextDarkHeading
                    )
                },
                actions = {
                    OfflineBadge(
                        modifier = Modifier.padding(end = 16.dp)
                    )
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
                    .widthIn(max = 520.dp)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                // Greeting and Subtitle
                Text(
                    text = headingGreeting,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    ),
                    color = TextDarkHeading,
                    modifier = Modifier.testTag("home_greeting_title")
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ready for today's practice?",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp
                    ),
                    color = TextMediumGray,
                    modifier = Modifier.testTag("home_greeting_subtitle")
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Two Small Shortcut Cards: Subjects and Test
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Subjects Shortcut Card
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = LightSurface),
                        border = BorderStroke(1.dp, BorderLight),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onNavigateToSubjects() }
                            .testTag("home_shortcut_subjects")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Surface(
                                modifier = Modifier.size(40.dp),
                                shape = CircleShape,
                                color = LightCardBackgroundVariant,
                                border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.2f))
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.MenuBook,
                                        contentDescription = null,
                                        tint = PrimaryBlue,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Subjects",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                ),
                                color = TextDarkHeading
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Explore & learn",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                color = TextMediumGray
                            )
                        }
                    }

                    // Test Shortcut Card
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = LightSurface),
                        border = BorderStroke(1.dp, BorderLight),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onNavigateToTest() }
                            .testTag("home_shortcut_test")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Surface(
                                modifier = Modifier.size(40.dp),
                                shape = CircleShape,
                                color = LightCardBackgroundVariant,
                                border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.2f))
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Quiz,
                                        contentDescription = null,
                                        tint = PrimaryBlue,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Test",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                ),
                                color = TextDarkHeading
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Mock & PYQs",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                color = TextMediumGray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Today's Progress Section
                Text(
                    text = "Today's Progress",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    ),
                    color = TextDarkHeading
                )
                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = LightSurface),
                    border = BorderStroke(1.dp, BorderLight),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("todays_progress_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("progress_stat_questions")
                            ) {
                                Text(
                                    text = "0",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp
                                    ),
                                    color = PrimaryBlue
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Questions Practiced",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                    color = TextMediumGray,
                                    textAlign = TextAlign.Center
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(1.dp)
                                    .background(BorderLight)
                            )

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("progress_stat_accuracy")
                            ) {
                                Text(
                                    text = "0%",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp
                                    ),
                                    color = PrimaryBlue
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Accuracy",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                    color = TextMediumGray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = LightCardBackgroundVariant,
                            border = BorderStroke(1.dp, BorderLight),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Start practicing questions to see your accuracy and daily stats.",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                color = TextMediumGray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestTabScreen(
    onNavigateToCustomTest: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("test_tab_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Test",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextDarkHeading
                    )
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
                    .widthIn(max = 520.dp)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // 1. Custom Test Card
                TestPlaceholderCard(
                    title = "Custom Test",
                    description = "Create tests by choosing subjects, topics, question count, and timer.",
                    icon = Icons.Default.Tune,
                    testTag = "test_card_custom",
                    onClick = onNavigateToCustomTest
                )

                // 2. PYQ Test Card
                TestPlaceholderCard(
                    title = "PYQ Test",
                    description = "Practice previous year exam question papers from SSC CGL exams.",
                    icon = Icons.Default.Assignment,
                    testTag = "test_card_pyq"
                )

                // 3. Mock Test Card
                TestPlaceholderCard(
                    title = "Mock Test",
                    description = "Take full-length timed tests matching the actual exam pattern.",
                    icon = Icons.Default.Timer,
                    testTag = "test_card_mock"
                )
            }
        }
    }
}

@Composable
private fun TestPlaceholderCard(
    title: String,
    description: String,
    icon: ImageVector,
    testTag: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LightSurface),
        border = BorderStroke(1.dp, BorderLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    modifier = Modifier.size(46.dp),
                    shape = CircleShape,
                    color = LightCardBackgroundVariant,
                    border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.2f))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        color = TextDarkHeading
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        ),
                        color = TextMediumGray
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = PrimaryBlue.copy(alpha = 0.6f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesTabScreen(
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("notes_tab_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notes",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextDarkHeading
                    )
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
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    modifier = Modifier.size(72.dp),
                    shape = CircleShape,
                    color = LightCardBackgroundVariant,
                    border = BorderStroke(1.5.dp, PrimaryBlue.copy(alpha = 0.25f))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.StickyNote2,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Notes",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextDarkHeading,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Quick revision notes, formulas, vocabulary lists, and cheat sheets will appear here.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        lineHeight = 21.sp
                    ),
                    color = TextMediumGray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun OfflineBadge(
    modifier: Modifier = Modifier.padding(end = 12.dp)
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = OfflineBadgeBg,
        border = BorderStroke(1.dp, OfflineBadgeBorder),
        modifier = modifier
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
}

