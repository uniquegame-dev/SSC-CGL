package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.StickyNote2
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PracticeQuestionRepository
import com.example.data.models.CustomTestResult
import com.example.data.models.PracticeSessionResult
import com.example.data.models.SscDataRepository
import com.example.ui.screens.CustomTestResultScreen
import com.example.ui.screens.CustomTestScreen
import com.example.ui.screens.CustomTestSetupScreen
import com.example.ui.screens.GreetingScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NotesTabScreen
import com.example.ui.screens.PracticeQuestionScreen
import com.example.ui.screens.ResultAnalysisScreen
import com.example.ui.screens.SubtopicsScreen
import com.example.ui.screens.TestTabScreen
import com.example.ui.screens.TopicsScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.theme.BorderLight
import com.example.ui.theme.LightCardBackgroundVariant
import com.example.ui.theme.LightSurface
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.TextDarkHeading
import com.example.ui.theme.TextMediumGray

enum class BottomNavTab(
  val label: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
  val testTag: String
) {
  HOME("Home", Icons.Filled.Home, Icons.Outlined.Home, "nav_tab_home"),
  SUBJECTS("Subjects", Icons.Filled.MenuBook, Icons.Outlined.MenuBook, "nav_tab_subjects"),
  TEST("Test", Icons.Filled.Quiz, Icons.Outlined.Quiz, "nav_tab_test"),
  NOTES("Notes", Icons.Filled.StickyNote2, Icons.Outlined.StickyNote2, "nav_tab_notes")
}

enum class AppScreen {
  WELCOME,
  MAIN_TABS,
  TOPICS,
  SUBTOPICS,
  PRACTICE,
  RESULT,
  CUSTOM_TEST_SETUP,
  CUSTOM_TEST_RUN,
  CUSTOM_TEST_RESULT
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        SscCglPracticeApp()
      }
    }
  }
}

@Composable
fun SscCglPracticeApp(modifier: Modifier = Modifier) {
  var currentScreen by rememberSaveable { mutableStateOf(AppScreen.WELCOME) }
  var currentTab by rememberSaveable { mutableStateOf(BottomNavTab.HOME) }
  var userName by rememberSaveable { mutableStateOf("") }
  var selectedSubjectId by rememberSaveable { mutableStateOf<String?>(null) }
  var selectedTopicId by rememberSaveable { mutableStateOf<String?>(null) }
  var selectedSubtopicId by rememberSaveable { mutableStateOf<String?>(null) }
  var practiceResult by remember { mutableStateOf<PracticeSessionResult?>(null) }

  // Custom Test state
  var customTestSelectedSubjects by rememberSaveable { mutableStateOf<List<String>>(emptyList()) }
  var customTestIsTimed by rememberSaveable { mutableStateOf(true) }
  var customTestResult by remember { mutableStateOf<CustomTestResult?>(null) }

  val currentSubject = selectedSubjectId?.let { SscDataRepository.getSubjectById(it) }
  val currentTopic = if (selectedSubjectId != null && selectedTopicId != null) {
    SscDataRepository.getTopicById(selectedSubjectId!!, selectedTopicId!!)
  } else null
  val currentSubtopic = if (currentTopic != null && selectedSubtopicId != null) {
    currentTopic.subtopics.find { it.id == selectedSubtopicId }
  } else null

  // System back button handling for hierarchical navigation
  BackHandler(enabled = currentScreen != AppScreen.WELCOME) {
    when (currentScreen) {
      AppScreen.CUSTOM_TEST_RESULT -> {
        currentScreen = AppScreen.MAIN_TABS
        currentTab = BottomNavTab.TEST
      }
      AppScreen.CUSTOM_TEST_RUN -> {
        currentScreen = AppScreen.CUSTOM_TEST_SETUP
      }
      AppScreen.CUSTOM_TEST_SETUP -> {
        currentScreen = AppScreen.MAIN_TABS
        currentTab = BottomNavTab.TEST
      }
      AppScreen.RESULT -> {
        currentScreen = AppScreen.TOPICS
        selectedTopicId = null
        selectedSubtopicId = null
      }
      AppScreen.PRACTICE -> {
        currentScreen = AppScreen.TOPICS
        selectedTopicId = null
        selectedSubtopicId = null
      }
      AppScreen.SUBTOPICS -> {
        currentScreen = AppScreen.TOPICS
        selectedTopicId = null
        selectedSubtopicId = null
      }
      AppScreen.TOPICS -> {
        currentScreen = AppScreen.MAIN_TABS
        currentTab = BottomNavTab.SUBJECTS
        selectedSubjectId = null
        selectedTopicId = null
      }
      AppScreen.MAIN_TABS -> {
        if (currentTab != BottomNavTab.HOME) {
          currentTab = BottomNavTab.HOME
        } else {
          currentScreen = AppScreen.WELCOME
        }
      }
      AppScreen.WELCOME -> { /* Default exit */ }
    }
  }

  Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
    AnimatedContent(
      targetState = currentScreen,
      transitionSpec = {
        if (targetState.ordinal > initialState.ordinal) {
          (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
            slideOutHorizontally { width -> -width } + fadeOut()
          )
        } else {
          (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
            slideOutHorizontally { width -> width } + fadeOut()
          )
        }
      },
      label = "ScreenTransition",
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) { screen ->
      when (screen) {
        AppScreen.WELCOME -> {
          WelcomeScreen(
            initialName = userName,
            onContinue = { enteredName ->
              userName = enteredName
              currentScreen = AppScreen.MAIN_TABS
              currentTab = BottomNavTab.HOME
            }
          )
        }
        AppScreen.MAIN_TABS -> {
          Scaffold(
            bottomBar = {
              AppBottomNavigationBar(
                currentTab = currentTab,
                onTabSelected = { newTab ->
                  currentTab = newTab
                }
              )
            },
            modifier = Modifier.fillMaxSize()
          ) { tabInnerPadding ->
            Box(
              modifier = Modifier
                .fillMaxSize()
                .padding(tabInnerPadding)
            ) {
              when (currentTab) {
                BottomNavTab.HOME -> {
                  HomeScreen(
                    userName = userName,
                    onNavigateToSubjects = {
                      currentTab = BottomNavTab.SUBJECTS
                    },
                    onNavigateToTest = {
                      currentTab = BottomNavTab.TEST
                    }
                  )
                }
                BottomNavTab.SUBJECTS -> {
                  GreetingScreen(
                    userName = userName,
                    onSubjectClick = { subject ->
                      selectedSubjectId = subject.id
                      currentScreen = AppScreen.TOPICS
                    },
                    onBack = {
                      currentScreen = AppScreen.WELCOME
                    }
                  )
                }
                BottomNavTab.TEST -> {
                  TestTabScreen(
                    onNavigateToCustomTest = {
                      currentScreen = AppScreen.CUSTOM_TEST_SETUP
                    }
                  )
                }
                BottomNavTab.NOTES -> {
                  NotesTabScreen()
                }
              }
            }
          }
        }
        AppScreen.TOPICS -> {
          if (currentSubject != null) {
            TopicsScreen(
              subject = currentSubject,
              onTopicClick = { topic ->
                selectedTopicId = topic.id
                currentScreen = AppScreen.PRACTICE
              },
              onBack = {
                currentScreen = AppScreen.MAIN_TABS
                currentTab = BottomNavTab.SUBJECTS
                selectedSubjectId = null
              }
            )
          } else {
            currentScreen = AppScreen.MAIN_TABS
            currentTab = BottomNavTab.SUBJECTS
          }
        }
        AppScreen.SUBTOPICS -> {
          if (currentSubject != null && currentTopic != null) {
            SubtopicsScreen(
              subject = currentSubject,
              topic = currentTopic,
              onSubtopicClick = { subtopic ->
                selectedSubtopicId = subtopic.id
                currentScreen = AppScreen.PRACTICE
              },
              onBack = {
                currentScreen = AppScreen.TOPICS
                selectedTopicId = null
                selectedSubtopicId = null
              }
            )
          } else if (currentSubject != null) {
            TopicsScreen(
              subject = currentSubject,
              onTopicClick = { topic ->
                selectedTopicId = topic.id
                currentScreen = AppScreen.PRACTICE
              },
              onBack = {
                currentScreen = AppScreen.MAIN_TABS
                currentTab = BottomNavTab.SUBJECTS
                selectedSubjectId = null
              }
            )
          }
        }
        AppScreen.PRACTICE -> {
          if (currentSubject != null && currentTopic != null) {
            val questions = remember(currentTopic.id) {
              PracticeQuestionRepository.getQuestionsForTopic(
                subjectId = currentSubject.id,
                subjectTitle = currentSubject.title,
                topicId = currentTopic.id,
                topicTitle = currentTopic.title
              )
            }
            PracticeQuestionScreen(
              subject = currentSubject,
              topic = currentTopic,
              subtopic = currentSubtopic,
              questions = questions,
              onFinishPractice = { result ->
                practiceResult = result
                currentScreen = AppScreen.RESULT
              },
              onBackToTopics = {
                currentScreen = AppScreen.TOPICS
                selectedTopicId = null
                selectedSubtopicId = null
              }
            )
          } else {
            currentScreen = AppScreen.TOPICS
          }
        }
        AppScreen.RESULT -> {
          val currentResult = practiceResult
          if (currentResult != null) {
            ResultAnalysisScreen(
              result = currentResult,
              onPracticeAgain = {
                currentScreen = AppScreen.PRACTICE
              },
              onBackToTopics = {
                currentScreen = AppScreen.TOPICS
                selectedTopicId = null
                selectedSubtopicId = null
              }
            )
          } else {
            currentScreen = AppScreen.TOPICS
          }
        }
        AppScreen.CUSTOM_TEST_SETUP -> {
          CustomTestSetupScreen(
            onStartTest = { selectedSubjects, isTimed ->
              customTestSelectedSubjects = selectedSubjects
              customTestIsTimed = isTimed
              currentScreen = AppScreen.CUSTOM_TEST_RUN
            },
            onBack = {
              currentScreen = AppScreen.MAIN_TABS
              currentTab = BottomNavTab.TEST
            }
          )
        }
        AppScreen.CUSTOM_TEST_RUN -> {
          CustomTestScreen(
            selectedSubjectIds = customTestSelectedSubjects,
            isTimed = customTestIsTimed,
            onTestFinished = { result ->
              customTestResult = result
              currentScreen = AppScreen.CUSTOM_TEST_RESULT
            },
            onExit = {
              currentScreen = AppScreen.CUSTOM_TEST_SETUP
            }
          )
        }
        AppScreen.CUSTOM_TEST_RESULT -> {
          val res = customTestResult
          if (res != null) {
            CustomTestResultScreen(
              result = res,
              onRetakeTest = {
                currentScreen = AppScreen.CUSTOM_TEST_RUN
              },
              onNewCustomTest = {
                currentScreen = AppScreen.CUSTOM_TEST_SETUP
              },
              onBackToTestMenu = {
                currentScreen = AppScreen.MAIN_TABS
                currentTab = BottomNavTab.TEST
              }
            )
          } else {
            currentScreen = AppScreen.MAIN_TABS
            currentTab = BottomNavTab.TEST
          }
        }
      }
    }
  }
}

@Composable
fun AppBottomNavigationBar(
  currentTab: BottomNavTab,
  onTabSelected: (BottomNavTab) -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    color = LightSurface,
    border = BorderStroke(1.dp, BorderLight),
    shadowElevation = 8.dp,
    modifier = modifier
      .fillMaxWidth()
      .testTag("bottom_navigation_bar")
  ) {
    NavigationBar(
      containerColor = LightSurface,
      contentColor = TextDarkHeading,
      tonalElevation = 0.dp,
      windowInsets = WindowInsets.navigationBars
    ) {
      BottomNavTab.entries.forEach { tab ->
        val selected = currentTab == tab
        NavigationBarItem(
          selected = selected,
          onClick = { onTabSelected(tab) },
          icon = {
            Icon(
              imageVector = if (selected) tab.selectedIcon else tab.unselectedIcon,
              contentDescription = tab.label,
              modifier = Modifier.size(24.dp)
            )
          },
          label = {
            Text(
              text = tab.label,
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 11.sp
              )
            )
          },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = PrimaryBlue,
            selectedTextColor = PrimaryBlue,
            indicatorColor = LightCardBackgroundVariant,
            unselectedIconColor = TextMediumGray,
            unselectedTextColor = TextMediumGray
          ),
          modifier = Modifier.testTag(tab.testTag)
        )
      }
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  GreetingScreen(userName = name, onSubjectClick = {}, onBack = {}, modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
  MyApplicationTheme {
    WelcomeScreen(onContinue = {})
  }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  MyApplicationTheme {
    GreetingScreen(userName = "Aspirant", onSubjectClick = {}, onBack = {})
  }
}



