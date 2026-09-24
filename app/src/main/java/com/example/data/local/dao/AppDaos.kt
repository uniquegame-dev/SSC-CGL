package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.data.local.entities.AttemptAnswerEntity
import com.example.data.local.entities.AttemptEntity
import com.example.data.local.entities.BookmarkEntity
import com.example.data.local.entities.DailyNoteItemEntity
import com.example.data.local.entities.DailyNoteProgressEntity
import com.example.data.local.entities.DailyNoteSetEntity
import com.example.data.local.entities.ExamEntity
import com.example.data.local.entities.QuestionEntity
import com.example.data.local.entities.QuestionProgressEntity
import com.example.data.local.entities.SubjectEntity
import com.example.data.local.entities.SubtopicEntity
import com.example.data.local.entities.TestEntity
import com.example.data.local.entities.TestQuestionEntity
import com.example.data.local.entities.TopicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HierarchyDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExam(exam: ExamEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSubjects(subjects: List<SubjectEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTopics(topics: List<TopicEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSubtopics(subtopics: List<SubtopicEntity>): List<Long>

    @Query("SELECT * FROM exams WHERE is_active = 1 ORDER BY name")
    fun getExams(): Flow<List<ExamEntity>>

    @Query("SELECT * FROM subjects WHERE exam_id = :examId AND is_active = 1 ORDER BY display_order, name")
    fun getSubjects(examId: String): Flow<List<SubjectEntity>>

    @Query("SELECT * FROM topics WHERE subject_id = :subjectId AND is_active = 1 ORDER BY display_order, name")
    fun getTopics(subjectId: String): Flow<List<TopicEntity>>

    @Query("SELECT * FROM subtopics WHERE topic_id = :topicId AND is_active = 1 ORDER BY display_order, name")
    fun getSubtopics(topicId: String): Flow<List<SubtopicEntity>>

    @Query("SELECT * FROM subjects WHERE id = :id LIMIT 1")
    suspend fun getSubjectDirect(id: String): SubjectEntity?

    @Query("SELECT * FROM topics WHERE id = :id LIMIT 1")
    suspend fun getTopicDirect(id: String): TopicEntity?

    @Query("SELECT * FROM subtopics WHERE id = :id LIMIT 1")
    suspend fun getSubtopicDirect(id: String): SubtopicEntity?
}

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertQuestion(question: QuestionEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuestions(questions: List<QuestionEntity>): List<Long>

    @Update
    suspend fun updateQuestion(question: QuestionEntity)

    @Query("SELECT * FROM questions WHERE is_active = 1 ORDER BY id")
    fun getAllQuestions(): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE id = :id LIMIT 1")
    fun getQuestionById(id: Long): Flow<QuestionEntity?>

    @Query("SELECT * FROM questions WHERE id = :id LIMIT 1")
    suspend fun getQuestionByIdDirect(id: Long): QuestionEntity?

    @Query("""
        SELECT q.* FROM questions q
        INNER JOIN topics t ON t.id = q.topic_id
        WHERE t.subject_id = :subjectId AND q.is_active = 1
        ORDER BY t.display_order, q.id
    """)
    fun getQuestionsBySubject(subjectId: String): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE topic_id = :topicId AND is_active = 1 ORDER BY id")
    fun getQuestionsByTopic(topicId: String): Flow<List<QuestionEntity>>

    @Query("""
        SELECT q.* FROM questions q
        INNER JOIN topics t ON t.id = q.topic_id
        WHERE t.subject_id = :subjectId AND q.topic_id = :topicId AND q.is_active = 1
        ORDER BY q.id
    """)
    fun getQuestionsBySubjectAndTopic(subjectId: String, topicId: String): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE topic_id = :topicId AND is_active = 1 ORDER BY id")
    suspend fun getQuestionsByTopicDirect(topicId: String): List<QuestionEntity>

    @Query("""
        SELECT q.* FROM questions q
        INNER JOIN topics t ON t.id = q.topic_id
        WHERE t.subject_id = :subjectId AND q.topic_id = :topicId AND q.is_active = 1
        ORDER BY q.id
    """)
    suspend fun getQuestionsBySubjectAndTopicDirect(subjectId: String, topicId: String): List<QuestionEntity>

    @Query("""
        SELECT q.* FROM questions q
        INNER JOIN topics t ON t.id = q.topic_id
        WHERE t.subject_id = :subjectId AND q.is_active = 1
        ORDER BY RANDOM()
        LIMIT :limit
    """)
    suspend fun getRandomQuestionsBySubject(subjectId: String, limit: Int): List<QuestionEntity>

    @Query("SELECT * FROM questions WHERE subtopic_id = :subtopicId AND is_active = 1 ORDER BY id")
    fun getQuestionsBySubtopic(subtopicId: String): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE subtopic_id = :subtopicId AND is_active = 1 ORDER BY id")
    suspend fun getQuestionsBySubtopicDirect(subtopicId: String): List<QuestionEntity>

    @Query("SELECT COUNT(*) FROM questions WHERE topic_id = :topicId AND is_active = 1")
    suspend fun getQuestionsCountByTopic(topicId: String): Int

    @Query("SELECT * FROM questions WHERE year = :year AND is_active = 1 ORDER BY exam_date, shift, id")
    fun getQuestionsByYear(year: Int): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE year = :year AND shift = :shift AND is_active = 1 ORDER BY id")
    fun getQuestionsByYearAndShift(year: Int, shift: String): Flow<List<QuestionEntity>>

    @Query("""
        SELECT q.* FROM questions q
        INNER JOIN bookmarks b ON b.question_id = q.id
        WHERE q.is_active = 1
        ORDER BY b.created_at DESC
    """)
    fun getBookmarkedQuestions(): Flow<List<QuestionEntity>>

    @Query("""
        SELECT q.* FROM questions q
        INNER JOIN question_progress p ON p.question_id = q.id
        WHERE p.attempt_count > 0 AND p.mastery_score < :masteryThreshold AND q.is_active = 1
        ORDER BY p.mastery_score, p.last_attempted_at DESC
    """)
    fun getWeakAreaQuestions(masteryThreshold: Double = 60.0): Flow<List<QuestionEntity>>

    @Query("""
        SELECT q.* FROM questions q
        INNER JOIN question_progress p ON p.question_id = q.id
        WHERE p.needs_revision = 1 AND q.is_active = 1
        ORDER BY p.last_attempted_at ASC
    """)
    fun getRevisionQuestions(): Flow<List<QuestionEntity>>

    @Query("UPDATE questions SET is_active = 0, updated_at = :updatedAt WHERE id = :id")
    suspend fun archiveQuestion(id: Long, updatedAt: Long = System.currentTimeMillis())

    @Query("""
        UPDATE questions
        SET topic_id = :topicId, subtopic_id = :subtopicId, updated_at = :updatedAt
        WHERE content_key = :contentKey
    """)
    suspend fun updateQuestionClassification(
        contentKey: String,
        topicId: String,
        subtopicId: String?,
        updatedAt: Long = System.currentTimeMillis()
    )

    @Query("SELECT COUNT(*) FROM questions")
    fun getQuestionCount(): Flow<Int>
}

@Dao
interface TestDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTest(test: TestEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTests(tests: List<TestEntity>): List<Long>

    @Query("SELECT * FROM tests ORDER BY created_at DESC")
    fun getAllTests(): Flow<List<TestEntity>>

    @Query("SELECT * FROM tests WHERE test_type = :testType ORDER BY created_at DESC")
    fun getTestsByType(testType: String): Flow<List<TestEntity>>

    @Query("SELECT * FROM tests WHERE year = :year ORDER BY exam_date, shift, id")
    fun getTestsByYear(year: Int): Flow<List<TestEntity>>

    @Query("SELECT * FROM tests WHERE id = :id LIMIT 1")
    fun getTestById(id: Long): Flow<TestEntity?>

    @Query("SELECT * FROM tests WHERE id = :id LIMIT 1")
    suspend fun getTestByIdDirect(id: Long): TestEntity?

    @Query("DELETE FROM tests WHERE id = :id")
    suspend fun deleteTestById(id: Long)
}

@Dao
interface TestQuestionDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTestQuestion(testQuestion: TestQuestionEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTestQuestions(testQuestions: List<TestQuestionEntity>): List<Long>

    @Query("SELECT * FROM test_questions WHERE test_id = :testId ORDER BY section_order, question_order")
    fun getTestQuestionsForTest(testId: Long): Flow<List<TestQuestionEntity>>

    @Query("""
        SELECT q.* FROM questions q
        INNER JOIN test_questions tq ON q.id = tq.question_id
        WHERE tq.test_id = :testId
        ORDER BY tq.section_order, tq.question_order
    """)
    fun getQuestionsForTest(testId: Long): Flow<List<QuestionEntity>>

    @Query("""
        SELECT q.* FROM questions q
        INNER JOIN test_questions tq ON q.id = tq.question_id
        WHERE tq.test_id = :testId
        ORDER BY tq.section_order, tq.question_order
    """)
    suspend fun getQuestionsForTestDirect(testId: Long): List<QuestionEntity>

    @Query("DELETE FROM test_questions WHERE test_id = :testId")
    suspend fun deleteTestQuestionsByTestId(testId: Long)
}

@Dao
interface AttemptDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAttempt(attempt: AttemptEntity): Long

    @Update
    suspend fun updateAttempt(attempt: AttemptEntity)

    @Query("SELECT * FROM attempts ORDER BY start_time DESC")
    fun getAllAttempts(): Flow<List<AttemptEntity>>

    @Query("SELECT * FROM attempts WHERE test_id = :testId ORDER BY start_time DESC")
    fun getAttemptsByTestId(testId: Long): Flow<List<AttemptEntity>>

    @Query("SELECT * FROM attempts WHERE id = :id LIMIT 1")
    fun getAttemptById(id: Long): Flow<AttemptEntity?>

    @Query("SELECT * FROM attempts WHERE id = :id LIMIT 1")
    suspend fun getAttemptByIdDirect(id: Long): AttemptEntity?

    @Query("SELECT * FROM attempts ORDER BY start_time DESC LIMIT :limit")
    fun getLatestAttempts(limit: Int = 10): Flow<List<AttemptEntity>>

    @Query("DELETE FROM attempts WHERE id = :id")
    suspend fun deleteAttemptById(id: Long)
}

@Dao
interface AttemptAnswerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttemptAnswer(answer: AttemptAnswerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttemptAnswers(answers: List<AttemptAnswerEntity>): List<Long>

    @Query("SELECT * FROM attempt_answers WHERE attempt_id = :attemptId ORDER BY answer_order")
    fun getAnswersForAttempt(attemptId: Long): Flow<List<AttemptAnswerEntity>>

    @Query("SELECT * FROM attempt_answers WHERE attempt_id = :attemptId ORDER BY answer_order")
    suspend fun getAnswersForAttemptDirect(attemptId: Long): List<AttemptAnswerEntity>

    @Query("DELETE FROM attempt_answers WHERE attempt_id = :attemptId")
    suspend fun deleteAnswersByAttemptId(attemptId: Long)
}

@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBookmark(bookmark: BookmarkEntity): Long

    @Query("DELETE FROM bookmarks WHERE question_id = :questionId")
    suspend fun removeBookmark(questionId: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE question_id = :questionId)")
    fun isBookmarked(questionId: Long): Flow<Boolean>
}

@Dao
interface QuestionProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProgress(progress: QuestionProgressEntity): Long

    @Query("SELECT * FROM question_progress WHERE question_id = :questionId LIMIT 1")
    fun getProgress(questionId: Long): Flow<QuestionProgressEntity?>

    @Query("SELECT * FROM question_progress WHERE question_id = :questionId LIMIT 1")
    suspend fun getProgressDirect(questionId: Long): QuestionProgressEntity?

    @Query("SELECT * FROM question_progress WHERE needs_revision = 1 ORDER BY last_attempted_at")
    fun getRevisionProgress(): Flow<List<QuestionProgressEntity>>
}

@Dao
interface DailyNoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSet(set: DailyNoteSetEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSets(sets: List<DailyNoteSetEntity>): List<Long>

    @Update
    suspend fun updateSet(set: DailyNoteSetEntity)

    @Query("SELECT * FROM daily_note_sets WHERE type = :type AND is_active = 1 LIMIT 1")
    fun getActiveSetByType(type: String): Flow<DailyNoteSetEntity?>

    @Query("SELECT * FROM daily_note_sets WHERE type = :type AND is_active = 1 LIMIT 1")
    suspend fun getActiveSetByTypeDirect(type: String): DailyNoteSetEntity?

    @Query("SELECT * FROM daily_note_sets WHERE type = :type ORDER BY set_number")
    fun getAllSetsByType(type: String): Flow<List<DailyNoteSetEntity>>

    @Query("SELECT * FROM daily_note_sets WHERE type = :type ORDER BY set_number")
    suspend fun getAllSetsByTypeDirect(type: String): List<DailyNoteSetEntity>

    @Query("SELECT * FROM daily_note_sets WHERE type = :type AND set_number = :setNumber LIMIT 1")
    suspend fun getSetByNumber(type: String, setNumber: Int): DailyNoteSetEntity?

    @Query("SELECT COUNT(*) FROM daily_note_sets WHERE type = :type")
    suspend fun getSetsCountByType(type: String): Int

    @Query("UPDATE daily_note_sets SET is_active = 0 WHERE type = :type")
    suspend fun deactivateAllSetsForType(type: String)

    @Query("UPDATE daily_note_sets SET is_active = 1 WHERE id = :setId")
    suspend fun activateSetById(setId: Long)

    @Query("UPDATE daily_note_sets SET is_completed = 1, completed_at = :completedAt WHERE id = :setId")
    suspend fun markSetCompletedStatus(setId: Long, completedAt: Long = System.currentTimeMillis())

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: DailyNoteItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<DailyNoteItemEntity>): List<Long>

    @Query("SELECT * FROM daily_note_items WHERE set_id = :setId ORDER BY item_order")
    fun getItemsForSet(setId: Long): Flow<List<DailyNoteItemEntity>>

    @Query("SELECT * FROM daily_note_items WHERE set_id = :setId ORDER BY item_order")
    suspend fun getItemsForSetDirect(setId: Long): List<DailyNoteItemEntity>

    @Query("SELECT * FROM daily_note_items WHERE id = :itemId LIMIT 1")
    suspend fun getItemById(itemId: Long): DailyNoteItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProgress(progress: DailyNoteProgressEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProgressList(progressList: List<DailyNoteProgressEntity>): List<Long>

    @Query("SELECT * FROM daily_note_progress WHERE set_id = :setId")
    fun getProgressForSet(setId: Long): Flow<List<DailyNoteProgressEntity>>

    @Query("SELECT * FROM daily_note_progress WHERE set_id = :setId")
    suspend fun getProgressForSetDirect(setId: Long): List<DailyNoteProgressEntity>

    @Query("SELECT * FROM daily_note_progress WHERE item_id = :itemId LIMIT 1")
    fun getProgressForItem(itemId: Long): Flow<DailyNoteProgressEntity?>

    @Query("SELECT * FROM daily_note_progress WHERE item_id = :itemId LIMIT 1")
    suspend fun getProgressForItemDirect(itemId: Long): DailyNoteProgressEntity?

    @Query("""
        SELECT i.* FROM daily_note_items i
        INNER JOIN daily_note_progress p ON i.id = p.item_id
        INNER JOIN daily_note_sets s ON i.set_id = s.id
        WHERE s.type = :type AND p.review_later = 1
        ORDER BY p.updated_at DESC
    """)
    fun getReviewLaterItemsByType(type: String): Flow<List<DailyNoteItemEntity>>

    @Query("SELECT COUNT(*) FROM daily_note_sets WHERE type = :type AND is_completed = 1")
    suspend fun getCompletedSetsCount(type: String): Int

    @Query("SELECT COUNT(*) FROM daily_note_sets WHERE type = :type AND is_completed = 0")
    suspend fun getUncompletedSetsCount(type: String): Int

    @Transaction
    suspend fun markSetCompletedAndUnlockNext(type: String, currentSetId: Long): DailyNoteSetEntity? {
        markSetCompletedStatus(currentSetId)
        deactivateAllSetsForType(type)
        val allSets = getAllSetsByTypeDirect(type)
        val currentSetNumber = allSets.find { it.id == currentSetId }?.setNumber ?: 1
        val nextSet = allSets.firstOrNull { it.setNumber > currentSetNumber && !it.isCompleted }
            ?: allSets.firstOrNull { !it.isCompleted }
        if (nextSet != null) {
            activateSetById(nextSet.id)
            return nextSet.copy(isActive = true)
        }
        return null
    }
}
