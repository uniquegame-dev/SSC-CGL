package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.data.local.entities.AttemptAnswerEntity
import com.example.data.local.entities.AttemptEntity
import com.example.data.local.entities.DailyNoteItemEntity
import com.example.data.local.entities.DailyNoteProgressEntity
import com.example.data.local.entities.DailyNoteSetEntity
import com.example.data.local.entities.QuestionEntity
import com.example.data.local.entities.TestEntity
import com.example.data.local.entities.TestQuestionEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Questions table.
 */
@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuestionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionEntity>): List<Long>

    @Update
    suspend fun updateQuestion(question: QuestionEntity)

    @Query("SELECT * FROM questions ORDER BY id ASC")
    fun getAllQuestions(): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE id = :id LIMIT 1")
    fun getQuestionById(id: Long): Flow<QuestionEntity?>

    @Query("SELECT * FROM questions WHERE id = :id LIMIT 1")
    suspend fun getQuestionByIdDirect(id: Long): QuestionEntity?

    @Query("SELECT * FROM questions WHERE subject = :subject ORDER BY id ASC")
    fun getQuestionsBySubject(subject: String): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE subject = :subject AND topic = :topic ORDER BY id ASC")
    fun getQuestionsBySubjectAndTopic(subject: String, topic: String): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE subject = :subject AND topic = :topic ORDER BY id ASC")
    suspend fun getQuestionsBySubjectAndTopicDirect(subject: String, topic: String): List<QuestionEntity>

    @Query("SELECT COUNT(*) FROM questions WHERE subject = :subject AND topic = :topic")
    suspend fun getQuestionsCountBySubjectAndTopic(subject: String, topic: String): Int

    @Query("SELECT * FROM questions WHERE year = :year ORDER BY id ASC")
    fun getQuestionsByYear(year: Int): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE year = :year AND shift = :shift ORDER BY id ASC")
    fun getQuestionsByYearAndShift(year: Int, shift: String): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE is_bookmarked = 1 ORDER BY id DESC")
    fun getBookmarkedQuestions(): Flow<List<QuestionEntity>>

    @Query("UPDATE questions SET is_bookmarked = :isBookmarked WHERE id = :id")
    suspend fun updateBookmarkStatus(id: Long, isBookmarked: Boolean)

    @Query("DELETE FROM questions WHERE id = :id")
    suspend fun deleteQuestionById(id: Long)

    @Query("SELECT COUNT(*) FROM questions")
    fun getQuestionCount(): Flow<Int>
}

/**
 * Data Access Object for Tests table.
 */
@Dao
interface TestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTest(test: TestEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTests(tests: List<TestEntity>): List<Long>

    @Query("SELECT * FROM tests ORDER BY created_at DESC")
    fun getAllTests(): Flow<List<TestEntity>>

    @Query("SELECT * FROM tests WHERE test_type = :testType ORDER BY created_at DESC")
    fun getTestsByType(testType: String): Flow<List<TestEntity>>

    @Query("SELECT * FROM tests WHERE year = :year ORDER BY id ASC")
    fun getTestsByYear(year: Int): Flow<List<TestEntity>>

    @Query("SELECT * FROM tests WHERE id = :id LIMIT 1")
    fun getTestById(id: Long): Flow<TestEntity?>

    @Query("SELECT * FROM tests WHERE id = :id LIMIT 1")
    suspend fun getTestByIdDirect(id: Long): TestEntity?

    @Query("DELETE FROM tests WHERE id = :id")
    suspend fun deleteTestById(id: Long)
}

/**
 * Data Access Object for TestQuestions mapping table.
 */
@Dao
interface TestQuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestQuestion(testQuestion: TestQuestionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTestQuestions(testQuestions: List<TestQuestionEntity>): List<Long>

    @Query("SELECT * FROM test_questions WHERE test_id = :testId ORDER BY section_order ASC, question_order ASC")
    fun getTestQuestionsForTest(testId: Long): Flow<List<TestQuestionEntity>>

    @Query("""
        SELECT q.* FROM questions q
        INNER JOIN test_questions tq ON q.id = tq.question_id
        WHERE tq.test_id = :testId
        ORDER BY tq.section_order ASC, tq.question_order ASC
    """)
    fun getQuestionsForTest(testId: Long): Flow<List<QuestionEntity>>

    @Query("""
        SELECT q.* FROM questions q
        INNER JOIN test_questions tq ON q.id = tq.question_id
        WHERE tq.test_id = :testId
        ORDER BY tq.section_order ASC, tq.question_order ASC
    """)
    suspend fun getQuestionsForTestDirect(testId: Long): List<QuestionEntity>

    @Query("DELETE FROM test_questions WHERE test_id = :testId")
    suspend fun deleteTestQuestionsByTestId(testId: Long)
}

/**
 * Data Access Object for Attempts table.
 */
@Dao
interface AttemptDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: AttemptEntity): Long

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

/**
 * Data Access Object for AttemptAnswers table.
 */
@Dao
interface AttemptAnswerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttemptAnswer(answer: AttemptAnswerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttemptAnswers(answers: List<AttemptAnswerEntity>): List<Long>

    @Query("SELECT * FROM attempt_answers WHERE attempt_id = :attemptId ORDER BY id ASC")
    fun getAnswersForAttempt(attemptId: Long): Flow<List<AttemptAnswerEntity>>

    @Query("SELECT * FROM attempt_answers WHERE attempt_id = :attemptId ORDER BY id ASC")
    suspend fun getAnswersForAttemptDirect(attemptId: Long): List<AttemptAnswerEntity>

    @Query("DELETE FROM attempt_answers WHERE attempt_id = :attemptId")
    suspend fun deleteAnswersByAttemptId(attemptId: Long)
}

/**
 * Data Access Object for Daily Notes (Sets, Items, Progress).
 */
@Dao
interface DailyNoteDao {

    // --- Sets Operations ---
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

    @Query("SELECT * FROM daily_note_sets WHERE type = :type ORDER BY set_number ASC")
    fun getAllSetsByType(type: String): Flow<List<DailyNoteSetEntity>>

    @Query("SELECT * FROM daily_note_sets WHERE type = :type ORDER BY set_number ASC")
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

    // --- Items Operations ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: DailyNoteItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<DailyNoteItemEntity>): List<Long>

    @Query("SELECT * FROM daily_note_items WHERE set_id = :setId ORDER BY item_order ASC")
    fun getItemsForSet(setId: Long): Flow<List<DailyNoteItemEntity>>

    @Query("SELECT * FROM daily_note_items WHERE set_id = :setId ORDER BY item_order ASC")
    suspend fun getItemsForSetDirect(setId: Long): List<DailyNoteItemEntity>

    @Query("SELECT * FROM daily_note_items WHERE id = :itemId LIMIT 1")
    suspend fun getItemById(itemId: Long): DailyNoteItemEntity?

    // --- Progress Operations ---
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

    /**
     * Completes the current active set and unlocks/activates the next set.
     * After all sets are done, returns null with no auto-restart.
     */
    @Transaction
    suspend fun markSetCompletedAndUnlockNext(type: String, currentSetId: Long): DailyNoteSetEntity? {
        val now = System.currentTimeMillis()
        // 1. Mark current set as completed and inactive
        markSetCompletedStatus(currentSetId, now)
        deactivateAllSetsForType(type)

        // 2. Fetch all sets for this type
        val allSets = getAllSetsByTypeDirect(type)
        if (allSets.isEmpty()) return null

        val currentSet = allSets.find { it.id == currentSetId }
        val currentSetNumber = currentSet?.setNumber ?: 1

        // 3. Find next uncompleted set with setNumber > currentSetNumber, else any remaining uncompleted set
        val nextSet = allSets.firstOrNull { it.setNumber > currentSetNumber && !it.isCompleted }
            ?: allSets.firstOrNull { !it.isCompleted }

        // If an uncompleted set is found, activate it. If all are completed, return null without auto-restart.
        if (nextSet != null) {
            activateSetById(nextSet.id)
            return nextSet.copy(isActive = true)
        }
        return null
    }
}

