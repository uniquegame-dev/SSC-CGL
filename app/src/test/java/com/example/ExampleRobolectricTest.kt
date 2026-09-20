package com.example

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import com.example.ui.screens.GreetingScreen
import com.example.ui.theme.MyApplicationTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("SSC CGL Practice", appName)
  }

  @Test
  fun `topics and subtopics navigation works as expected`() {
    val mathSubject = com.example.data.models.SscDataRepository.getSubjectById("maths")!!
    val mathTopic = mathSubject.topics.first()

    // Test TopicsScreen directly
    var selectedTopic: com.example.data.models.Topic? = null
    var topicsBackClicked = false
    composeTestRule.setContent {
      MyApplicationTheme {
        com.example.ui.screens.TopicsScreen(
          subject = mathSubject,
          onTopicClick = { selectedTopic = it },
          onBack = { topicsBackClicked = true }
        )
      }
    }

    composeTestRule.onNodeWithText("Topics (${mathSubject.topics.size})").assertExists()
    composeTestRule.onNodeWithTag("topic_card_${mathTopic.id}").assertExists()
    composeTestRule.onNodeWithTag("topic_card_${mathTopic.id}").performClick()
    assertEquals(mathTopic.id, selectedTopic?.id)

    composeTestRule.onNodeWithTag("back_to_subjects_button").performClick()
    assertEquals(true, topicsBackClicked)
  }

  @Test
  fun `subtopics screen displays list correctly`() {
    val mathSubject = com.example.data.models.SscDataRepository.getSubjectById("maths")!!
    val mathTopic = mathSubject.topics.first()

    var subtopicBackClicked = false
    composeTestRule.setContent {
      MyApplicationTheme {
        com.example.ui.screens.SubtopicsScreen(
          subject = mathSubject,
          topic = mathTopic,
          onBack = { subtopicBackClicked = true }
        )
      }
    }

    composeTestRule.onNodeWithText("Subtopics (${mathTopic.subtopics.size})").assertExists()
    val firstSubtopic = mathTopic.subtopics.first()
    composeTestRule.onNodeWithTag("subtopic_card_${firstSubtopic.id}").assertExists()
    composeTestRule.onNodeWithText(firstSubtopic.title).assertExists()

    composeTestRule.onNodeWithTag("back_to_topics_button").performClick()
    assertEquals(true, subtopicBackClicked)
  }

  @Test
  fun `greeting screen displays 4 subjects and triggers subject click`() {
    var clickedSubject: com.example.data.models.Subject? = null
    composeTestRule.setContent {
      MyApplicationTheme {
        GreetingScreen(
          userName = "Priya",
          onSubjectClick = { clickedSubject = it },
          onBack = {}
        )
      }
    }

    composeTestRule.onNodeWithText("Subject Practice").assertExists()
    composeTestRule.onNodeWithTag("subject_card_reasoning").assertExists()
    composeTestRule.onNodeWithTag("subject_card_maths").assertExists()
    composeTestRule.onNodeWithTag("subject_card_english").assertExists()
    composeTestRule.onNodeWithTag("subject_card_gk_ga").assertExists()

    composeTestRule.onNodeWithTag("subject_card_maths").performClick()
    assertEquals("maths", clickedSubject?.id)
  }

  @Test
  fun `practice question screen renders and responds to option selection`() {
    val mathSubject = com.example.data.models.SscDataRepository.getSubjectById("maths")!!
    val mathTopic = mathSubject.topics.first()
    val subtopic = mathTopic.subtopics.first()
    val questions = com.example.data.PracticeQuestionRepository.getQuestionsForSubtopic(
      subjectId = mathSubject.id,
      subjectTitle = mathSubject.title,
      topicId = mathTopic.id,
      topicTitle = mathTopic.title,
      subtopicId = subtopic.id,
      subtopicTitle = subtopic.title
    )

    var finishedResult: com.example.data.models.PracticeSessionResult? = null
    var backClicked = false

    composeTestRule.setContent {
      MyApplicationTheme {
        com.example.ui.screens.PracticeQuestionScreen(
          subject = mathSubject,
          topic = mathTopic,
          subtopic = subtopic,
          questions = questions,
          onFinishPractice = { finishedResult = it },
          onBackToSubtopics = { backClicked = true }
        )
      }
    }

    composeTestRule.onNodeWithTag("practice_question_screen").assertExists()
    composeTestRule.onNodeWithTag("question_header_title").assertExists()
    composeTestRule.onNodeWithTag("question_text").assertExists()
    composeTestRule.onNodeWithTag("option_0_button").assertExists()
    composeTestRule.onNodeWithTag("option_1_button").assertExists()
    composeTestRule.onNodeWithTag("save_next_button").assertExists()

    // Select option 1 (B)
    composeTestRule.onNodeWithTag("option_1_button").performClick()

    // Test palette toggle
    composeTestRule.onNodeWithTag("palette_toggle_button").performClick()
    composeTestRule.onNodeWithTag("question_palette_sheet").assertExists()
  }

  @Test
  fun `result analysis screen displays metrics and filter chips`() {
    val mathSubject = com.example.data.models.SscDataRepository.getSubjectById("maths")!!
    val mathTopic = mathSubject.topics.first()
    val subtopic = mathTopic.subtopics.first()
    val questions = com.example.data.PracticeQuestionRepository.getQuestionsForSubtopic(
      subjectId = mathSubject.id,
      subjectTitle = mathSubject.title,
      topicId = mathTopic.id,
      topicTitle = mathTopic.title,
      subtopicId = subtopic.id,
      subtopicTitle = subtopic.title
    )

    val dummyResult = com.example.data.models.PracticeSessionResult(
      subjectTitle = mathSubject.title,
      topicTitle = mathTopic.title,
      subtopicTitle = subtopic.title,
      totalQuestions = 20,
      attempted = 15,
      correct = 12,
      wrong = 3,
      unattempted = 5,
      accuracyPercent = 80.0,
      totalTimeSeconds = 600L,
      averageTimePerQuestionSeconds = 30.0,
      questionReviews = questions.mapIndexed { index, q ->
        com.example.data.models.QuestionReviewItem(
          question = q,
          selectedOptionIndex = if (index < 15) q.correctOptionIndex else null,
          correctOptionIndex = q.correctOptionIndex,
          isCorrect = index < 12,
          isAttempted = index < 15,
          timeSpentSeconds = 30L
        )
      }
    )

    var practiceAgainClicked = false
    var backToSubtopicsClicked = false

    composeTestRule.setContent {
      MyApplicationTheme {
        com.example.ui.screens.ResultAnalysisScreen(
          result = dummyResult,
          onPracticeAgain = { practiceAgainClicked = true },
          onBackToSubtopics = { backToSubtopicsClicked = true }
        )
      }
    }

    composeTestRule.onNodeWithTag("result_analysis_screen").assertExists()
    composeTestRule.onNodeWithTag("performance_summary_card").assertExists()
    composeTestRule.onNodeWithTag("result_accuracy_text").assertExists()
    composeTestRule.onNodeWithTag("practice_again_button").assertExists()
    composeTestRule.onNodeWithTag("result_back_subtopics_btn").assertExists()

    // Test practice again
    composeTestRule.onNodeWithTag("practice_again_button").performClick()
    assertEquals(true, practiceAgainClicked)
  }

  @Test
  fun `bottom navigation bar displays 4 tabs in order and switches tabs`() {
    var selectedTab = BottomNavTab.HOME
    composeTestRule.setContent {
      MyApplicationTheme {
        AppBottomNavigationBar(
          currentTab = selectedTab,
          onTabSelected = { selectedTab = it }
        )
      }
    }

    composeTestRule.onNodeWithTag("bottom_navigation_bar").assertExists()
    composeTestRule.onNodeWithTag("nav_tab_home").assertExists()
    composeTestRule.onNodeWithTag("nav_tab_subjects").assertExists()
    composeTestRule.onNodeWithTag("nav_tab_test").assertExists()
    composeTestRule.onNodeWithTag("nav_tab_notes").assertExists()

    composeTestRule.onNodeWithText("Home").assertExists()
    composeTestRule.onNodeWithText("Subjects").assertExists()
    composeTestRule.onNodeWithText("Test").assertExists()
    composeTestRule.onNodeWithText("Notes").assertExists()

    composeTestRule.onNodeWithTag("nav_tab_subjects").performClick()
    assertEquals(BottomNavTab.SUBJECTS, selectedTab)

    composeTestRule.onNodeWithTag("nav_tab_test").performClick()
    assertEquals(BottomNavTab.TEST, selectedTab)

    composeTestRule.onNodeWithTag("nav_tab_notes").performClick()
    assertEquals(BottomNavTab.NOTES, selectedTab)
  }

  @Test
  fun `direct topic practice fetches questions across all subtopics with subtopic tagging`() {
    val mathSubject = com.example.data.models.SscDataRepository.getSubjectById("maths")!!
    val numberSystemTopic = mathSubject.topics.first { it.id == "number_system" }

    val questions = com.example.data.PracticeQuestionRepository.getQuestionsForTopic(
      subjectId = mathSubject.id,
      subjectTitle = mathSubject.title,
      topicId = numberSystemTopic.id,
      topicTitle = numberSystemTopic.title
    )

    assertEquals(20, questions.size)
    // Verify that questions are tagged with subtopics
    val taggedSubtopicIds = questions.map { it.subtopicId }.distinct()
    assertEquals(true, taggedSubtopicIds.size > 1)

    var backToTopicsClicked = false
    composeTestRule.setContent {
      MyApplicationTheme {
        com.example.ui.screens.PracticeQuestionScreen(
          subject = mathSubject,
          topic = numberSystemTopic,
          questions = questions,
          onFinishPractice = {},
          onBackToTopics = { backToTopicsClicked = true }
        )
      }
    }

    composeTestRule.onNodeWithTag("practice_question_screen").assertExists()
    composeTestRule.onNodeWithTag("question_header_title").assertExists()
    composeTestRule.onNodeWithTag("option_0_button").assertExists()
  }

  @Test
  fun `home screen displays top bar, greeting, 2 shortcut cards, and progress placeholder`() {
    var subjectsClicked = false
    var testClicked = false

    composeTestRule.setContent {
      MyApplicationTheme {
        com.example.ui.screens.HomeScreen(
          userName = "Aspirant",
          onNavigateToSubjects = { subjectsClicked = true },
          onNavigateToTest = { testClicked = true }
        )
      }
    }

    composeTestRule.onNodeWithTag("home_screen").assertExists()
    composeTestRule.onNodeWithText("Offline").assertExists()

    // Greeting
    composeTestRule.onNodeWithTag("home_greeting_title").assertExists()
    composeTestRule.onNodeWithText("Hello, Aspirant").assertExists()
    composeTestRule.onNodeWithTag("home_greeting_subtitle").assertExists()
    composeTestRule.onNodeWithText("Ready for today's practice?").assertExists()

    // 2 Shortcut cards: Subjects and Test
    composeTestRule.onNodeWithTag("home_shortcut_subjects").assertExists()
    composeTestRule.onNodeWithText("Subjects").assertExists()
    composeTestRule.onNodeWithTag("home_shortcut_subjects").performClick()
    assertEquals(true, subjectsClicked)

    composeTestRule.onNodeWithTag("home_shortcut_test").assertExists()
    composeTestRule.onNodeWithText("Test").assertExists()
    composeTestRule.onNodeWithTag("home_shortcut_test").performClick()
    assertEquals(true, testClicked)

    // Today's Progress placeholder
    composeTestRule.onNodeWithTag("todays_progress_card").assertExists()
    composeTestRule.onNodeWithText("Today's Progress").assertExists()
    composeTestRule.onNodeWithTag("progress_stat_questions").assertExists()
    composeTestRule.onNodeWithTag("progress_stat_accuracy").assertExists()
    composeTestRule.onNodeWithText("0%").assertExists()
  }

  @Test
  fun `empty notes screen renders clean placeholder`() {
    composeTestRule.setContent {
      MyApplicationTheme {
        com.example.ui.screens.NotesTabScreen()
      }
    }

    composeTestRule.onNodeWithTag("notes_tab_screen").assertExists()
    composeTestRule.onNodeWithText("Quick revision notes, formulas, vocabulary lists, and cheat sheets will appear here.").assertExists()
    composeTestRule.onNodeWithText("Offline").assertExists()
  }

  @Test
  fun `test tab screen displays 3 placeholder test cards`() {
    composeTestRule.setContent {
      MyApplicationTheme {
        com.example.ui.screens.TestTabScreen()
      }
    }

    composeTestRule.onNodeWithTag("test_tab_screen").assertExists()
    composeTestRule.onNodeWithText("Test").assertExists()
    composeTestRule.onNodeWithText("Offline").assertExists()

    // 3 Test Cards
    composeTestRule.onNodeWithTag("test_card_custom").assertExists()
    composeTestRule.onNodeWithText("Custom Test").assertExists()

    composeTestRule.onNodeWithTag("test_card_pyq").assertExists()
    composeTestRule.onNodeWithText("PYQ Test").assertExists()

    composeTestRule.onNodeWithTag("test_card_mock").assertExists()
    composeTestRule.onNodeWithText("Mock Test").assertExists()
  }
}

