package com.example.ui.screens

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.OfflineBolt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.ExamIcon
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.LightBackground
import com.example.ui.theme.LightCardBackground
import com.example.ui.theme.LightSurface
import com.example.ui.theme.OfflineBadgeBg
import com.example.ui.theme.OfflineBadgeBorder
import com.example.ui.theme.OfflineBadgeText
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.TextDarkHeading
import com.example.ui.theme.TextMediumGray

@Composable
fun WelcomeScreen(
    onContinue: (String) -> Unit,
    modifier: Modifier = Modifier,
    initialName: String = ""
) {
    var name by rememberSaveable { mutableStateOf(initialName) }
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(LightBackground)
            .testTag("welcome_screen"),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 540.dp)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .imePadding()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Offline-first badge indicator
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = OfflineBadgeBg,
                    border = BorderStroke(1.dp, OfflineBadgeBorder),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.OfflineBolt,
                            contentDescription = null,
                            tint = OfflineBadgeText,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.offline_badge),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = OfflineBadgeText
                            )
                        )
                    }
                }

                // Simple Exam-Style Icon with primary accent blue
                ExamIcon(
                    size = 96.dp,
                    iconSize = 52.dp,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // Heading with dark text
                Text(
                    text = stringResource(R.string.welcome_heading),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    ),
                    color = TextDarkHeading,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Short subtext with medium gray
                Text(
                    text = stringResource(R.string.welcome_subtext),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    ),
                    color = TextMediumGray,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // "Let's get started" Card Section in soft light blue-gray with subtle border & shadow
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = LightCardBackground
                    ),
                    border = BorderStroke(1.dp, BorderLight),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = stringResource(R.string.section_get_started),
                            style = MaterialTheme.typography.titleLarge,
                            color = TextDarkHeading
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Helper text with medium gray
                        Text(
                            text = stringResource(R.string.helper_enter_name),
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMediumGray
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Text input with placeholder 'Enter your name'
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.name_input_placeholder),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = TextMediumGray
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = PrimaryBlue
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    onContinue(name.trim())
                                }
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextDarkHeading,
                                unfocusedTextColor = TextDarkHeading,
                                focusedBorderColor = PrimaryBlue,
                                unfocusedBorderColor = BorderSubtle,
                                focusedContainerColor = LightSurface,
                                unfocusedContainerColor = LightSurface
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("name_input_field")
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Primary button 'Continue' with primary accent blue
                        Button(
                            onClick = {
                                onContinue(name.trim())
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryBlue,
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 2.dp,
                                pressedElevation = 4.dp
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("continue_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.btn_continue),
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Offline-first footer messaging with medium gray
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp, bottom = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.offline_sub_badge),
                    style = MaterialTheme.typography.labelMedium,
                    color = TextMediumGray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

