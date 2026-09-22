package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.OfflineBolt
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DailyNoteSetWithItems
import com.example.data.local.DailyNotesRoomRepository
import com.example.data.local.entities.DailyNoteItemEntity
import com.example.data.local.entities.DailyNoteSetEntity
import com.example.data.models.CurrentAffairItem
import com.example.data.models.DailyNoteCategory
import com.example.data.models.DailyNotesRepository
import com.example.data.models.IdiomItem
import com.example.data.models.MemoryBite
import com.example.data.models.MemoryBiteRepository
import com.example.data.models.VocabularyItem
import kotlinx.coroutines.launch
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CorrectGreen
import com.example.ui.theme.LightBackground
import com.example.ui.theme.LightCardBackgroundVariant
import com.example.ui.theme.LightSurface
import com.example.ui.theme.OfflineBadgeBg
import com.example.ui.theme.OfflineBadgeBorder
import com.example.ui.theme.OfflineBadgeText
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.TextDarkHeading
import com.example.ui.theme.TextMediumGray
import com.example.ui.theme.WrongRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String = "",
    onNavigateToSubjects: () -> Unit = {},
    onNavigateToTest: () -> Unit = {},
    onNavigateToNotes: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val displayName = userName.trim()
    val headingGreeting = if (displayName.isNotEmpty()) "Hello, $displayName." else "Hello, Aspirant."
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var currentBite by remember {
        mutableStateOf(MemoryBiteRepository.getActiveMemoryBite(context))
    }
    var actionFeedback by remember { mutableStateOf<String?>(null) }

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
                // 1. Greeting
                Text(
                    text = headingGreeting,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    ),
                    color = TextDarkHeading,
                    modifier = Modifier.testTag("home_greeting_title")
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "Ready for today's practice?",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp
                    ),
                    color = TextMediumGray,
                    modifier = Modifier.testTag("home_greeting_subtitle")
                )

                Spacer(modifier = Modifier.height(18.dp))

                // 2. Today's Progress Report (directly below greeting)
                Text(
                    text = "Today's Progress",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    ),
                    color = TextDarkHeading,
                    modifier = Modifier.testTag("home_progress_title")
                )
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = LightSurface),
                    border = BorderStroke(1.dp, BorderLight),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
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
                                    .height(38.dp)
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

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = LightCardBackgroundVariant,
                            border = BorderStroke(1.dp, BorderSubtle),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Start practicing questions or tests to build your daily streak.",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                color = TextMediumGray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 3. Three Clear Options: Subjects, Test, and Notes
                Text(
                    text = "Quick Options",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    ),
                    color = TextDarkHeading,
                    modifier = Modifier.testTag("home_options_title")
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Option 1: Subjects
                    HomeOptionShortcutCard(
                        title = "Subjects",
                        subtitle = "Chapters & Qs",
                        icon = Icons.Default.MenuBook,
                        testTag = "home_shortcut_subjects",
                        onClick = onNavigateToSubjects,
                        modifier = Modifier.weight(1f)
                    )

                    // Option 2: Test
                    HomeOptionShortcutCard(
                        title = "Test",
                        subtitle = "Mocks & PYQs",
                        icon = Icons.Default.Quiz,
                        testTag = "home_shortcut_test",
                        onClick = onNavigateToTest,
                        modifier = Modifier.weight(1f)
                    )

                    // Option 3: Notes
                    HomeOptionShortcutCard(
                        title = "Notes",
                        subtitle = "Daily Notes",
                        icon = Icons.Default.StickyNote2,
                        testTag = "home_shortcut_notes",
                        onClick = onNavigateToNotes,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                // 4. Today's Memory Bite
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = LightSurface),
                    border = BorderStroke(1.dp, BorderLight),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("todays_memory_bite_card")
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
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    modifier = Modifier.size(36.dp),
                                    shape = CircleShape,
                                    color = PrimaryBlue.copy(alpha = 0.1f),
                                    border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.2f))
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Lightbulb,
                                            contentDescription = null,
                                            tint = PrimaryBlue,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Today’s Memory Bite",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    ),
                                    color = TextDarkHeading
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = LightCardBackgroundVariant,
                                border = BorderStroke(0.5.dp, BorderLight)
                            ) {
                                Text(
                                    text = currentBite.subjectTag,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 10.sp,
                                        color = TextMediumGray
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = LightCardBackgroundVariant,
                            border = BorderStroke(1.dp, BorderSubtle),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = currentBite.title,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = PrimaryBlue
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = currentBite.fact,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 12.5.sp,
                                        lineHeight = 18.sp
                                    ),
                                    color = TextDarkHeading
                                )
                            }
                        }

                        if (actionFeedback != null) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = CorrectGreen.copy(alpha = 0.1f),
                                border = BorderStroke(0.5.dp, CorrectGreen.copy(alpha = 0.3f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = actionFeedback ?: "",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = CorrectGreen
                                    ),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Two action buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Review again later button
                            OutlinedButton(
                                onClick = {
                                    MemoryBiteRepository.markReviewLater(context, currentBite.id)
                                    actionFeedback = "Resurfacing for review in 2 days."
                                    currentBite = MemoryBiteRepository.getActiveMemoryBite(context)
                                },
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, BorderLight),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("btn_review_later")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = TextMediumGray,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Review later",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 12.sp,
                                            color = TextDarkHeading
                                        ),
                                        maxLines = 1
                                    )
                                }
                            }

                            // Mark as remembered button
                            Button(
                                onClick = {
                                    MemoryBiteRepository.markRemembered(context, currentBite.id)
                                    actionFeedback = "Marked as remembered! New Memory Bite loaded."
                                    currentBite = MemoryBiteRepository.getActiveMemoryBite(context)
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("btn_mark_remembered")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Remembered",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = Color.White
                                        ),
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun HomeOptionShortcutCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = LightSurface),
        border = BorderStroke(1.dp, BorderLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(38.dp),
                shape = CircleShape,
                color = LightCardBackgroundVariant,
                border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.2f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                ),
                color = TextDarkHeading,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = TextMediumGray,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestTabScreen(
    onNavigateToCustomTest: () -> Unit = {},
    onNavigateToPyqTest: () -> Unit = {},
    onNavigateToMockTest: () -> Unit = {},
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
                        text = "SSC CGL Tests",
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
                    description = "Explore year-wise question papers (2025 – 2010), date shifts, and blueprints.",
                    icon = Icons.Default.Assignment,
                    testTag = "test_card_pyq",
                    onClick = onNavigateToPyqTest
                )

                // 3. Mock Test Card
                TestPlaceholderCard(
                    title = "Mock Test",
                    description = "Take full-length timed tests matching the SSC CGL Tier I exam pattern (100 Qs, 60 mins).",
                    icon = Icons.Default.Timer,
                    testTag = "test_card_mock",
                    onClick = onNavigateToMockTest
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
    var selectedCategory by rememberSaveable { mutableStateOf<DailyNoteCategory?>(null) }

    if (selectedCategory != null) {
        BackHandler {
            selectedCategory = null
        }
    }

    when (val current = selectedCategory) {
        null -> {
            NotesOverviewScreen(
                onSelectCategory = { category ->
                    selectedCategory = category
                },
                modifier = modifier
            )
        }
        DailyNoteCategory.VOCABULARY -> {
            VocabularyDetailScreen(
                onBack = { selectedCategory = null },
                modifier = modifier
            )
        }
        DailyNoteCategory.IDIOMS -> {
            IdiomsDetailScreen(
                onBack = { selectedCategory = null },
                modifier = modifier
            )
        }
        DailyNoteCategory.CURRENT_AFFAIRS -> {
            CurrentAffairsDetailScreen(
                onBack = { selectedCategory = null },
                modifier = modifier
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesOverviewScreen(
    onSelectCategory: (DailyNoteCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val roomRepo = remember { DailyNotesRoomRepository.getInstance(context) }
    var vocabSetIdx by remember { mutableStateOf(1) }
    var vocabTotalSets by remember { mutableStateOf(5) }
    var vocabCompleted by remember { mutableStateOf(false) }

    var idiomsSetIdx by remember { mutableStateOf(1) }
    var idiomsTotalSets by remember { mutableStateOf(5) }
    var idiomsCompleted by remember { mutableStateOf(false) }

    var caSetIdx by remember { mutableStateOf(1) }
    var caTotalSets by remember { mutableStateOf(5) }
    var caCompleted by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val vocabActive = roomRepo.getActiveSetDirect(DailyNotesRoomRepository.TYPE_VOCAB)
        val vocabTotal = roomRepo.getTotalSetsCount(DailyNotesRoomRepository.TYPE_VOCAB)
        if (vocabTotal > 0) vocabTotalSets = vocabTotal
        vocabCompleted = roomRepo.isCategoryCompleted(DailyNotesRoomRepository.TYPE_VOCAB)
        if (vocabActive != null) vocabSetIdx = vocabActive.setNumber

        val idiomsActive = roomRepo.getActiveSetDirect(DailyNotesRoomRepository.TYPE_IDIOM)
        val idiomsTotal = roomRepo.getTotalSetsCount(DailyNotesRoomRepository.TYPE_IDIOM)
        if (idiomsTotal > 0) idiomsTotalSets = idiomsTotal
        idiomsCompleted = roomRepo.isCategoryCompleted(DailyNotesRoomRepository.TYPE_IDIOM)
        if (idiomsActive != null) idiomsSetIdx = idiomsActive.setNumber

        val caActive = roomRepo.getActiveSetDirect(DailyNotesRoomRepository.TYPE_CURRENT_AFFAIRS)
        val caTotal = roomRepo.getTotalSetsCount(DailyNotesRoomRepository.TYPE_CURRENT_AFFAIRS)
        if (caTotal > 0) caTotalSets = caTotal
        caCompleted = roomRepo.isCategoryCompleted(DailyNotesRoomRepository.TYPE_CURRENT_AFFAIRS)
        if (caActive != null) caSetIdx = caActive.setNumber
    }

    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("notes_tab_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "SSC CGL Daily Notes",
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
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                // Section Title: Daily Notes
                Text(
                    text = "Daily Notes",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = TextDarkHeading,
                    modifier = Modifier.testTag("daily_notes_section_title")
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Bite-sized daily booster for SSC CGL Tier 1 & 2",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 13.sp
                    ),
                    color = TextMediumGray,
                    modifier = Modifier.testTag("daily_notes_section_subtitle")
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 1. Vocabulary Card
                DailyNoteOptionCard(
                    title = "Vocabulary",
                    subtitle = "10 words with meanings, synonyms, antonyms & usage",
                    icon = Icons.Default.Spellcheck,
                    countLabel = if (vocabCompleted) "Completed" else "Set $vocabSetIdx of $vocabTotalSets • 10 Words",
                    testTag = "daily_notes_card_vocabulary",
                    onClick = { onSelectCategory(DailyNoteCategory.VOCABULARY) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 2. Idioms & Phrases Card
                DailyNoteOptionCard(
                    title = "Idioms & Phrases",
                    subtitle = "Frequently repeated idioms with clear meanings & examples",
                    icon = Icons.Default.RecordVoiceOver,
                    countLabel = if (idiomsCompleted) "Completed" else "Set $idiomsSetIdx of $idiomsTotalSets • 10 Phrases",
                    testTag = "daily_notes_card_idioms",
                    onClick = { onSelectCategory(DailyNoteCategory.IDIOMS) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 3. Current Affairs Card
                DailyNoteOptionCard(
                    title = "Current Affairs",
                    subtitle = "Categorized daily headlines & concise exam summaries",
                    icon = Icons.Default.Public,
                    countLabel = "16 Categories • January–September",
                    testTag = "daily_notes_card_current_affairs",
                    onClick = { onSelectCategory(DailyNoteCategory.CURRENT_AFFAIRS) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Informational Tip Banner
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = LightCardBackgroundVariant,
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(36.dp),
                            shape = CircleShape,
                            color = PrimaryBlue.copy(alpha = 0.1f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = null,
                                    tint = PrimaryBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Daily notes remain saved until you tap 'Mark as completed' at the bottom.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            ),
                            color = TextMediumGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DailyNoteOptionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    countLabel: String,
    testTag: String,
    onClick: () -> Unit,
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
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    modifier = Modifier.size(44.dp),
                    shape = CircleShape,
                    color = LightCardBackgroundVariant,
                    border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.2f))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                            color = TextDarkHeading
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = PrimaryBlue.copy(alpha = 0.08f),
                            border = BorderStroke(0.5.dp, PrimaryBlue.copy(alpha = 0.2f))
                        ) {
                            Text(
                                text = countLabel,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryBlue
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        ),
                        color = TextMediumGray,
                        maxLines = 2
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
fun VocabularyDetailScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val roomRepo = remember { DailyNotesRoomRepository.getInstance(context) }
    val coroutineScope = rememberCoroutineScope()

    var activeSetWithItems by remember { mutableStateOf<DailyNoteSetWithItems?>(null) }
    var totalSets by remember { mutableStateOf(5) }
    var isCategoryDone by remember { mutableStateOf(false) }
    var showCompletedFeedback by remember { mutableStateOf(false) }
    var isMarkingCompleted by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        val total = roomRepo.getTotalSetsCount(DailyNotesRoomRepository.TYPE_VOCAB)
        if (total > 0) totalSets = total
        val active = roomRepo.getActiveSetWithItems(DailyNotesRoomRepository.TYPE_VOCAB)
        activeSetWithItems = active
        isCategoryDone = (active == null)
    }

    val currentSet = activeSetWithItems?.set
    val items = activeSetWithItems?.items ?: emptyList()
    val currentSetNumber = currentSet?.setNumber ?: totalSets
    val itemsCount = if (items.isNotEmpty()) items.size else 10

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("vocabulary_detail_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Vocabulary",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextDarkHeading
                        )
                        Text(
                            text = if (isCategoryDone) "All 50 words completed" else "Set $currentSetNumber of $totalSets • $itemsCount Words",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            color = if (isCategoryDone) CorrectGreen else PrimaryBlue
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("btn_back_vocabulary")
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
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (showCompletedFeedback && !isCategoryDone) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = CorrectGreen.copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, CorrectGreen.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = CorrectGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Set completed! Loaded Set $currentSetNumber for study.",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = CorrectGreen
                                )
                            )
                        }
                    }
                }

                if (isCategoryDone || items.isEmpty()) {
                    // All available sets completed state
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = LightSurface,
                        border = BorderStroke(1.dp, BorderLight),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("all_sets_completed_vocabulary")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Surface(
                                modifier = Modifier.size(60.dp),
                                shape = CircleShape,
                                color = CorrectGreen.copy(alpha = 0.12f),
                                border = BorderStroke(1.dp, CorrectGreen.copy(alpha = 0.3f))
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = CorrectGreen,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "All available sets completed",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                ),
                                color = TextDarkHeading,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "You have completed all 50 vocabulary words in the database.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                ),
                                color = TextMediumGray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    items.forEachIndexed { index, item ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = LightSurface),
                            border = BorderStroke(1.dp, BorderLight),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("vocab_card_${item.id}")
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp)
                            ) {
                                // Header Row: Word + Part of Speech + Index Tag
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = "${index + 1}. ${item.wordOrTitle}",
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 19.sp
                                            ),
                                            color = PrimaryBlue
                                        )
                                        if (item.phonetic.isNotEmpty()) {
                                            Text(
                                                text = item.phonetic,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontStyle = FontStyle.Italic,
                                                    fontSize = 13.sp
                                                ),
                                                color = TextMediumGray
                                            )
                                        }
                                    }

                                    if (item.partOfSpeech.isNotEmpty()) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = LightCardBackgroundVariant,
                                            border = BorderStroke(0.5.dp, BorderLight)
                                        ) {
                                            Text(
                                                text = item.partOfSpeech,
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = TextDarkHeading,
                                                    fontSize = 11.sp
                                                ),
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Meaning
                                Text(
                                    text = "Meaning",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = TextMediumGray
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = item.meaning,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 14.sp,
                                        lineHeight = 20.sp
                                    ),
                                    color = TextDarkHeading
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // Synonyms & Antonyms
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    // Synonyms box
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = LightCardBackgroundVariant,
                                        border = BorderStroke(0.5.dp, BorderSubtle),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
                                            Text(
                                                text = "Synonyms",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 10.5.sp,
                                                    color = CorrectGreen
                                                )
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = item.synonym,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontSize = 12.sp,
                                                    lineHeight = 16.sp
                                                ),
                                                color = TextDarkHeading
                                            )
                                        }
                                    }

                                    // Antonyms box
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = LightCardBackgroundVariant,
                                        border = BorderStroke(0.5.dp, BorderSubtle),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
                                            Text(
                                                text = "Antonyms",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 10.5.sp,
                                                    color = WrongRed
                                                )
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = item.antonym,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontSize = 12.sp,
                                                    lineHeight = 16.sp
                                                ),
                                                color = TextDarkHeading
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Example Sentence Box
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = LightCardBackgroundVariant,
                                    border = BorderStroke(1.dp, BorderSubtle),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "Example Sentence",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                color = PrimaryBlue
                                            )
                                        )
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = "\"${item.example}\"",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontStyle = FontStyle.Italic,
                                                fontSize = 13.sp,
                                                lineHeight = 19.sp
                                            ),
                                            color = TextDarkHeading
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Bottom "Mark as completed" Button
                    Button(
                        onClick = {
                            if (currentSet != null && !isMarkingCompleted) {
                                isMarkingCompleted = true
                                coroutineScope.launch {
                                    val nextSetWithItems = roomRepo.markSetCompletedWithItems(
                                        DailyNotesRoomRepository.TYPE_VOCAB,
                                        currentSet.id
                                    )
                                    activeSetWithItems = nextSetWithItems
                                    isCategoryDone = (nextSetWithItems == null)
                                    showCompletedFeedback = true
                                    isMarkingCompleted = false
                                    scrollState.animateScrollTo(0)
                                }
                            }
                        },
                        enabled = !isMarkingCompleted,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_complete_vocabulary")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Mark as completed",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdiomsDetailScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val roomRepo = remember { DailyNotesRoomRepository.getInstance(context) }
    val coroutineScope = rememberCoroutineScope()

    var activeSetWithItems by remember { mutableStateOf<DailyNoteSetWithItems?>(null) }
    var totalSets by remember { mutableStateOf(5) }
    var isCategoryDone by remember { mutableStateOf(false) }
    var showCompletedFeedback by remember { mutableStateOf(false) }
    var isMarkingCompleted by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        val total = roomRepo.getTotalSetsCount(DailyNotesRoomRepository.TYPE_IDIOM)
        if (total > 0) totalSets = total
        val active = roomRepo.getActiveSetWithItems(DailyNotesRoomRepository.TYPE_IDIOM)
        activeSetWithItems = active
        isCategoryDone = (active == null)
    }

    val currentSet = activeSetWithItems?.set
    val items = activeSetWithItems?.items ?: emptyList()
    val currentSetNumber = currentSet?.setNumber ?: totalSets
    val itemsCount = if (items.isNotEmpty()) items.size else 10

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("idioms_detail_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Idioms & Phrases",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextDarkHeading
                        )
                        Text(
                            text = if (isCategoryDone) "All 50 idioms completed" else "Set $currentSetNumber of $totalSets • $itemsCount Phrases",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            color = if (isCategoryDone) CorrectGreen else PrimaryBlue
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("btn_back_idioms")
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
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (showCompletedFeedback && !isCategoryDone) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = CorrectGreen.copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, CorrectGreen.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = CorrectGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Set completed! Loaded Set $currentSetNumber for study.",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = CorrectGreen
                                )
                            )
                        }
                    }
                }

                if (isCategoryDone || items.isEmpty()) {
                    // All available sets completed state
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = LightSurface,
                        border = BorderStroke(1.dp, BorderLight),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("all_sets_completed_idioms")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Surface(
                                modifier = Modifier.size(60.dp),
                                shape = CircleShape,
                                color = CorrectGreen.copy(alpha = 0.12f),
                                border = BorderStroke(1.dp, CorrectGreen.copy(alpha = 0.3f))
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = CorrectGreen,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "All available sets completed",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                ),
                                color = TextDarkHeading,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "You have completed all 50 idioms & phrases in the database.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                ),
                                color = TextMediumGray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    items.forEachIndexed { index, item ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = LightSurface),
                            border = BorderStroke(1.dp, BorderLight),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("idiom_card_${item.id}")
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp)
                            ) {
                                // Phrase Title
                                Text(
                                    text = "${index + 1}. ${item.wordOrTitle}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp
                                    ),
                                    color = TextDarkHeading
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // Meaning
                                Text(
                                    text = "Meaning",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = TextMediumGray
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = item.meaning,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 14.sp,
                                        lineHeight = 20.sp
                                    ),
                                    color = TextDarkHeading
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Example Sentence Box
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = LightCardBackgroundVariant,
                                    border = BorderStroke(1.dp, BorderSubtle),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "Usage in Sentence",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                color = PrimaryBlue
                                            )
                                        )
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = "\"${item.example}\"",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontStyle = FontStyle.Italic,
                                                fontSize = 13.sp,
                                                lineHeight = 19.sp
                                            ),
                                            color = TextDarkHeading
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Bottom "Mark as completed" Button
                    Button(
                        onClick = {
                            if (currentSet != null && !isMarkingCompleted) {
                                isMarkingCompleted = true
                                coroutineScope.launch {
                                    val nextSetWithItems = roomRepo.markSetCompletedWithItems(
                                        DailyNotesRoomRepository.TYPE_IDIOM,
                                        currentSet.id
                                    )
                                    activeSetWithItems = nextSetWithItems
                                    isCategoryDone = (nextSetWithItems == null)
                                    showCompletedFeedback = true
                                    isMarkingCompleted = false
                                    scrollState.animateScrollTo(0)
                                }
                            }
                        },
                        enabled = !isMarkingCompleted,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_complete_idioms")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Mark as completed",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
}

private val currentAffairsCategories = listOf(
    "Sports",
    "Obituaries",
    "Awards & Honours",
    "National Appointment",
    "International Appointment",
    "National News",
    "International News",
    "State News",
    "Index & Ranking",
    "Events and Summit",
    "Important Books",
    "Military Exercises",
    "Important Days & Themes",
    "Science & Technology",
    "Important Schemes",
    "Miscellaneous"
)

private val currentAffairsMonths = listOf(
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September"
)

private data class CurrentAffairsPlaceholder(
    val id: String,
    val headline: String,
    val summary: String,
    val date: String
)

private fun currentAffairsPlaceholders(
    category: String,
    month: String
): List<CurrentAffairsPlaceholder> {
    val headlinePatterns = listOf(
        "$category development highlighted in $month",
        "Key $category update for SSC CGL revision",
        "$month briefing: important $category milestone",
        "Exam focus: notable $category announcement",
        "$category monthly recap and quick facts",
        "One-liner revision: $category in $month",
        "$month current affairs digest: $category"
    )
    val summaryPatterns = listOf(
        "This is placeholder content for testing the category and month filtering flow. Replace it with a verified current-affairs summary before release.",
        "A concise SSC CGL-style explanation will appear here, covering the key person, place, organisation, date, and exam-relevant fact.",
        "This sample entry confirms that the selected category and month are being applied correctly while the real offline dataset is prepared.",
        "Use this area for a short factual summary followed by the most likely one-line exam takeaway and any essential related detail.",
        "Placeholder revision note: the production record should be sourced, dated, reviewed, and stored locally for fully offline access."
    )
    val seed = (category + month).hashCode().toLong() and 0x7FFFFFFFL

    return List(5) { index ->
        val headlineIndex = ((seed + index * 3L) % headlinePatterns.size).toInt()
        val summaryIndex = ((seed + index * 2L) % summaryPatterns.size).toInt()
        val day = 1 + ((seed + index * 5L) % 28).toInt()
        CurrentAffairsPlaceholder(
            id = "${category.lowercase().replace(" ", "_")}_${month.lowercase()}_$index",
            headline = headlinePatterns[headlineIndex],
            summary = summaryPatterns[summaryIndex],
            date = "$month $day"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentAffairsDetailScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCurrentAffairsCategory by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedCurrentAffairsMonth by rememberSaveable { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    LaunchedEffect(selectedCurrentAffairsCategory, selectedCurrentAffairsMonth) {
        scrollState.scrollTo(0)
    }

    val navigateBack: () -> Unit = {
        when {
            selectedCurrentAffairsMonth != null -> selectedCurrentAffairsMonth = null
            selectedCurrentAffairsCategory != null -> selectedCurrentAffairsCategory = null
            else -> onBack()
        }
    }

    BackHandler(
        enabled = selectedCurrentAffairsCategory != null || selectedCurrentAffairsMonth != null,
        onBack = navigateBack
    )

    val subtitle = when {
        selectedCurrentAffairsCategory == null -> "Step 1 of 2 • Select category"
        selectedCurrentAffairsMonth == null -> "Step 2 of 2 • Select month"
        else -> "$selectedCurrentAffairsCategory • $selectedCurrentAffairsMonth"
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("current_affairs_detail_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Current Affairs",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextDarkHeading
                        )
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            color = PrimaryBlue
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = navigateBack,
                        modifier = Modifier.testTag("btn_back_current_affairs")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextDarkHeading
                        )
                    }
                },
                actions = { OfflineBadge() },
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
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when {
                    selectedCurrentAffairsCategory == null -> {
                        CurrentAffairsSectionHeading(
                            title = "Select Category",
                            subtitle = "Choose one topic to view its month-wise updates."
                        )
                        currentAffairsCategories.forEachIndexed { index, category ->
                            CurrentAffairsSelectorCard(
                                title = category,
                                supportingText = "Category ${index + 1} of ${currentAffairsCategories.size}",
                                leadingText = (index + 1).toString().padStart(2, '0'),
                                testTag = "current_affairs_category_${category.toTestTag()}",
                                onClick = {
                                    selectedCurrentAffairsCategory = category
                                    selectedCurrentAffairsMonth = null
                                }
                            )
                        }
                    }

                    selectedCurrentAffairsMonth == null -> {
                        CurrentAffairsSectionHeading(
                            title = "Select Month",
                            subtitle = "Showing available months for $selectedCurrentAffairsCategory."
                        )
                        currentAffairsMonths.forEachIndexed { index, month ->
                            CurrentAffairsSelectorCard(
                                title = month,
                                supportingText = "$selectedCurrentAffairsCategory • 5 sample updates",
                                leadingText = (index + 1).toString().padStart(2, '0'),
                                testTag = "current_affairs_month_${month.lowercase()}",
                                onClick = { selectedCurrentAffairsMonth = month }
                            )
                        }
                    }

                    else -> {
                        val category = selectedCurrentAffairsCategory.orEmpty()
                        val month = selectedCurrentAffairsMonth.orEmpty()
                        val placeholderItems = remember(category, month) {
                            currentAffairsPlaceholders(category, month)
                        }

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = LightCardBackgroundVariant,
                            border = BorderStroke(1.dp, BorderLight),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("current_affairs_active_filters")
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = category,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    ),
                                    color = TextDarkHeading
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "$month • ${placeholderItems.size} placeholder updates",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                    color = TextMediumGray
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedButton(
                                    onClick = {
                                        selectedCurrentAffairsCategory = null
                                        selectedCurrentAffairsMonth = null
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.35f)),
                                    modifier = Modifier.testTag("btn_change_current_affairs_filters")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Tune,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(7.dp))
                                    Text(text = "Change selection", fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }

                        placeholderItems.forEachIndexed { index, item ->
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = LightSurface),
                                border = BorderStroke(1.dp, BorderLight),
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("current_affair_card_${item.id}")
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(18.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = PrimaryBlue.copy(alpha = 0.08f),
                                            border = BorderStroke(0.5.dp, PrimaryBlue.copy(alpha = 0.2f))
                                        ) {
                                            Text(
                                                text = category,
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 11.sp,
                                                    color = PrimaryBlue
                                                ),
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                        Text(
                                            text = item.date,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 11.sp
                                            ),
                                            color = TextMediumGray
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "${index + 1}. ${item.headline}",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp,
                                            lineHeight = 21.sp
                                        ),
                                        color = TextDarkHeading
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = item.summary,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 13.sp,
                                            lineHeight = 19.sp
                                        ),
                                        color = TextDarkHeading.copy(alpha = 0.85f)
                                    )
                                }
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = PrimaryBlue.copy(alpha = 0.06f),
                            border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.18f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Preview data only • Real verified current affairs will replace these entries later.",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp,
                                    lineHeight = 17.sp
                                ),
                                color = PrimaryBlue,
                                modifier = Modifier.padding(14.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
}

@Composable
private fun CurrentAffairsSectionHeading(
    title: String,
    subtitle: String
) {
    Column(modifier = Modifier.padding(bottom = 4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            color = TextDarkHeading
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 13.sp,
                lineHeight = 18.sp
            ),
            color = TextMediumGray
        )
    }
}

@Composable
private fun CurrentAffairsSelectorCard(
    title: String,
    supportingText: String,
    leadingText: String,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = LightSurface),
        border = BorderStroke(1.dp, BorderLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(10.dp),
                color = PrimaryBlue.copy(alpha = 0.09f),
                border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.18f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = leadingText,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        ),
                        color = PrimaryBlue
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                    color = TextDarkHeading
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = supportingText,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = TextMediumGray
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = PrimaryBlue.copy(alpha = 0.65f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

private fun String.toTestTag(): String = lowercase()
    .replace("&", "and")
    .replace(" ", "_")

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

