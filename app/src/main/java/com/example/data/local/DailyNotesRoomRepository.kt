package com.example.data.local

import android.content.Context
import com.example.data.local.dao.DailyNoteDao
import com.example.data.local.entities.DailyNoteItemEntity
import com.example.data.local.entities.DailyNoteProgressEntity
import com.example.data.local.entities.DailyNoteSetEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class DailyNoteSetWithItems(
    val set: DailyNoteSetEntity,
    val items: List<DailyNoteItemEntity>,
    val progressList: List<DailyNoteProgressEntity> = emptyList()
)

class DailyNotesRoomRepository(private val database: AppDatabase) {

    private val dailyNoteDao: DailyNoteDao = database.dailyNoteDao()

    companion object {
        const val TYPE_VOCAB = "VOCAB"
        const val TYPE_IDIOM = "IDIOM"
        const val TYPE_CURRENT_AFFAIRS = "CURRENT_AFFAIRS"

        @Volatile
        private var INSTANCE: DailyNotesRoomRepository? = null

        fun getInstance(context: Context): DailyNotesRoomRepository {
            return INSTANCE ?: synchronized(this) {
                val db = AppDatabase.getDatabase(context)
                val repo = DailyNotesRoomRepository(db)
                INSTANCE = repo
                CoroutineScope(Dispatchers.IO).launch {
                    repo.seedInitialDataIfEmpty()
                }
                repo
            }
        }
    }

    // --- Active Set Queries (Flow) ---
    fun getActiveSet(type: String): Flow<DailyNoteSetEntity?> =
        dailyNoteDao.getActiveSetByType(type)

    fun getAllSets(type: String): Flow<List<DailyNoteSetEntity>> =
        dailyNoteDao.getAllSetsByType(type)

    fun getItemsForSet(setId: Long): Flow<List<DailyNoteItemEntity>> =
        dailyNoteDao.getItemsForSet(setId)

    fun getProgressForSet(setId: Long): Flow<List<DailyNoteProgressEntity>> =
        dailyNoteDao.getProgressForSet(setId)

    fun getReviewLaterItems(type: String): Flow<List<DailyNoteItemEntity>> =
        dailyNoteDao.getReviewLaterItemsByType(type)

    // --- Direct Suspend Operations ---
    suspend fun getActiveSetDirect(type: String): DailyNoteSetEntity? = withContext(Dispatchers.IO) {
        seedInitialDataIfEmpty()
        dailyNoteDao.getActiveSetByTypeDirect(type)
    }

    suspend fun getTotalSetsCount(type: String): Int = withContext(Dispatchers.IO) {
        seedInitialDataIfEmpty()
        dailyNoteDao.getSetsCountByType(type)
    }

    suspend fun getCompletedSetsCount(type: String): Int = withContext(Dispatchers.IO) {
        seedInitialDataIfEmpty()
        dailyNoteDao.getCompletedSetsCount(type)
    }

    suspend fun isCategoryCompleted(type: String): Boolean = withContext(Dispatchers.IO) {
        seedInitialDataIfEmpty()
        val total = dailyNoteDao.getSetsCountByType(type)
        val completed = dailyNoteDao.getCompletedSetsCount(type)
        total > 0 && completed >= total
    }

    suspend fun getActiveSetWithItems(type: String): DailyNoteSetWithItems? = withContext(Dispatchers.IO) {
        seedInitialDataIfEmpty()
        var activeSet = dailyNoteDao.getActiveSetByTypeDirect(type)
        if (activeSet == null) {
            val allSets = dailyNoteDao.getAllSetsByTypeDirect(type)
            // If all sets are completed, return null with no auto-restart
            val uncompleted = allSets.firstOrNull { !it.isCompleted }
            if (uncompleted != null) {
                dailyNoteDao.activateSetById(uncompleted.id)
                activeSet = uncompleted.copy(isActive = true)
            }
        }
        if (activeSet != null) {
            val items = dailyNoteDao.getItemsForSetDirect(activeSet.id)
            val progress = dailyNoteDao.getProgressForSetDirect(activeSet.id)
            DailyNoteSetWithItems(activeSet, items, progress)
        } else {
            null
        }
    }

    suspend fun getItemsForSetDirect(setId: Long): List<DailyNoteItemEntity> = withContext(Dispatchers.IO) {
        dailyNoteDao.getItemsForSetDirect(setId)
    }

    suspend fun getProgressForSetDirect(setId: Long): List<DailyNoteProgressEntity> = withContext(Dispatchers.IO) {
        dailyNoteDao.getProgressForSetDirect(setId)
    }

    suspend fun markSetCompleted(type: String, currentSetId: Long): DailyNoteSetEntity? = withContext(Dispatchers.IO) {
        dailyNoteDao.markSetCompletedAndUnlockNext(type, currentSetId)
    }

    suspend fun markSetCompletedWithItems(type: String, currentSetId: Long): DailyNoteSetWithItems? = withContext(Dispatchers.IO) {
        val nextSet = dailyNoteDao.markSetCompletedAndUnlockNext(type, currentSetId)
        if (nextSet != null) {
            val items = dailyNoteDao.getItemsForSetDirect(nextSet.id)
            val progress = dailyNoteDao.getProgressForSetDirect(nextSet.id)
            DailyNoteSetWithItems(nextSet, items, progress)
        } else {
            null
        }
    }

    suspend fun markItemCompleted(setId: Long, itemId: Long) = withContext(Dispatchers.IO) {
        val existing = dailyNoteDao.getProgressForItemDirect(itemId)
        val progress = existing?.copy(
            isCompleted = true,
            updatedAt = System.currentTimeMillis()
        ) ?: DailyNoteProgressEntity(
            setId = setId,
            itemId = itemId,
            isCompleted = true,
            reviewLater = false,
            updatedAt = System.currentTimeMillis()
        )
        dailyNoteDao.insertOrUpdateProgress(progress)
    }

    suspend fun markItemReviewLater(setId: Long, itemId: Long, reviewLater: Boolean) = withContext(Dispatchers.IO) {
        val existing = dailyNoteDao.getProgressForItemDirect(itemId)
        val progress = existing?.copy(
            reviewLater = reviewLater,
            updatedAt = System.currentTimeMillis()
        ) ?: DailyNoteProgressEntity(
            setId = setId,
            itemId = itemId,
            isCompleted = false,
            reviewLater = reviewLater,
            updatedAt = System.currentTimeMillis()
        )
        dailyNoteDao.insertOrUpdateProgress(progress)
    }

    /**
     * Seeds structured initial data for Vocab (5 sets of 10 items = 50 total),
     * Idioms (5 sets of 10 items = 50 total), and Current Affairs (5 sets of 10 items = 50 total)
     * if not already populated.
     */
    suspend fun seedInitialDataIfEmpty() = withContext(Dispatchers.IO) {
        val vocabCount = dailyNoteDao.getSetsCountByType(TYPE_VOCAB)
        if (vocabCount == 0) {
            seedCategory(TYPE_VOCAB, DailyNotesSeedData.vocabSets)
        }

        val idiomCount = dailyNoteDao.getSetsCountByType(TYPE_IDIOM)
        if (idiomCount == 0) {
            seedCategory(TYPE_IDIOM, DailyNotesSeedData.idiomSets)
        }

        val caCount = dailyNoteDao.getSetsCountByType(TYPE_CURRENT_AFFAIRS)
        if (caCount == 0) {
            seedCategory(TYPE_CURRENT_AFFAIRS, DailyNotesSeedData.currentAffairsSets)
        }
    }

    private suspend fun seedCategory(
        type: String,
        setDefinitions: List<DailyNotesSeedData.SetDefinition>
    ) {
        setDefinitions.forEachIndexed { index, def ->
            val setId = dailyNoteDao.insertSet(
                DailyNoteSetEntity(
                    type = type,
                    setNumber = def.setNumber,
                    title = def.title,
                    totalItems = def.items.size,
                    isActive = (index == 0), // First set is active by default
                    isCompleted = false
                )
            )
            val items = def.items.mapIndexed { itemIndex, itemDef ->
                DailyNoteItemEntity(
                    setId = setId,
                    itemOrder = itemIndex + 1,
                    wordOrTitle = itemDef.wordOrTitle,
                    phonetic = itemDef.phonetic,
                    partOfSpeech = itemDef.partOfSpeech,
                    meaning = itemDef.meaning,
                    synonym = itemDef.synonym,
                    antonym = itemDef.antonym,
                    example = itemDef.example,
                    category = itemDef.category,
                    factDate = itemDef.factDate
                )
            }
            dailyNoteDao.insertItems(items)
        }
    }
}
