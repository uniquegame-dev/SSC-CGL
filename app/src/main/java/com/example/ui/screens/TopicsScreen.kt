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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.OfflineBolt
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.models.Subject
import com.example.data.models.Topic
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
import com.example.ui.theme.TextDarkHeading
import com.example.ui.theme.TextMediumGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicsScreen(
    subject: Subject,
    onTopicClick: (Topic) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("topics_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = subject.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextDarkHeading
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("back_to_subjects_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to subjects",
                            tint = TextDarkHeading
                        )
                    }
                },
                actions = {
                    // Offline badge
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
                    .widthIn(max = 560.dp)
                    .fillMaxSize()
                    .testTag("topics_list"),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header Banner for Subject
                item {
                    SubjectHeaderBanner(subject = subject)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Topics (${subject.topics.size})",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDarkHeading
                        ),
                        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                    )
                }

                itemsIndexed(subject.topics, key = { _, topic -> topic.id }) { index, topic ->
                    TopicCard(
                        index = index + 1,
                        topic = topic,
                        onClick = { onTopicClick(topic) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun SubjectHeaderBanner(
    subject: Subject,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = LightCardBackground),
        border = BorderStroke(1.dp, BorderLight),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(52.dp),
                shape = RoundedCornerShape(14.dp),
                color = LightSurface,
                border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.2f)),
                shadowElevation = 2.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = subject.icon,
                        contentDescription = subject.title,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = subject.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = TextDarkHeading
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subject.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMediumGray
                )
            }
        }
    }
}

@Composable
fun TopicCard(
    index: Int,
    topic: Topic,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LightSurface),
        border = BorderStroke(1.dp, BorderLight),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.5.dp,
            pressedElevation = 3.dp
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("topic_card_${topic.id}")
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Index number badge in soft blue-gray circle
            Surface(
                modifier = Modifier.size(36.dp),
                shape = CircleShape,
                color = LightCardBackgroundVariant,
                border = BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.2f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "$index",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Topic Information
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = topic.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    ),
                    color = TextDarkHeading
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = topic.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    ),
                    color = TextMediumGray,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Subtopic count badge
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Layers,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${topic.subtopics.size} subtopics",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = PrimaryBlue
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Arrow forward indicator
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "Open topic",
                tint = BorderSubtle,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
