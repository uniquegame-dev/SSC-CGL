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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.OfflineBolt
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.models.SscDataRepository
import com.example.data.models.Subject
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BorderSubtle
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GreetingScreen(
    onSubjectClick: (Subject) -> Unit,
    modifier: Modifier = Modifier,
    userName: String = "",
    onBack: () -> Unit = {}
) {
    val subjects = SscDataRepository.subjects
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("greeting_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Subject Practice",
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
                    .widthIn(max = 560.dp)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // 4 Simple Rounded Cards with Subtle Shadows and Blue Accent Icons
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Row 1: Reasoning & Maths
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SubjectCard(
                            subject = subjects[0],
                            onClick = { onSubjectClick(subjects[0]) },
                            modifier = Modifier.weight(1f)
                        )
                        SubjectCard(
                            subject = subjects[1],
                            onClick = { onSubjectClick(subjects[1]) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Row 2: English & GK/GA
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SubjectCard(
                            subject = subjects[2],
                            onClick = { onSubjectClick(subjects[2]) },
                            modifier = Modifier.weight(1f)
                        )
                        SubjectCard(
                            subject = subjects[3],
                            onClick = { onSubjectClick(subjects[3]) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun SubjectCard(
    subject: Subject,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightSurface
        ),
        border = BorderStroke(1.dp, BorderLight),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        ),
        modifier = modifier
            .testTag(subject.testTag)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Blue Accent Icon in soft tinted rounded container
            Surface(
                modifier = Modifier.size(44.dp),
                shape = RoundedCornerShape(12.dp),
                color = LightCardBackgroundVariant,
                border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.15f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = subject.icon,
                        contentDescription = subject.title,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Subject Title
            Text(
                text = subject.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                color = TextDarkHeading
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Short Description / Helper
            Text(
                text = subject.description,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                ),
                color = TextMediumGray,
                maxLines = 2
            )
        }
    }
}


