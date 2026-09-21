package com.example.data.models

import android.content.Context
import android.content.SharedPreferences

data class MemoryBite(
    val id: String,
    val subjectTag: String,
    val title: String,
    val fact: String
)

object MemoryBiteRepository {
    val sampleBites: List<MemoryBite> = listOf(
        MemoryBite(
            id = "mb_1",
            subjectTag = "Polity • Article 324",
            title = "Election Commission of India",
            fact = "Article 324 of the Indian Constitution vests the superintendence, direction, and control of elections in the Election Commission of India. It is an autonomous, permanent constitutional body."
        ),
        MemoryBite(
            id = "mb_2",
            subjectTag = "History • Medieval India",
            title = "Battle of Khanwa (1527)",
            fact = "Fought between Mughal Emperor Babur and Rana Sanga of Mewar. Babur utilized the Tulghuma battle tactic and artillery to establish Mughal hegemony in Northern India."
        ),
        MemoryBite(
            id = "mb_3",
            subjectTag = "Geography • Indian Geography",
            title = "Tropic of Cancer in India",
            fact = "The Tropic of Cancer (23.5° N latitude) passes through 8 Indian states: Gujarat, Rajasthan, Madhya Pradesh, Chhattisgarh, Jharkhand, West Bengal, Tripura, and Mizoram."
        ),
        MemoryBite(
            id = "mb_4",
            subjectTag = "Science • Cell Biology",
            title = "Powerhouse of the Cell",
            fact = "Mitochondria generate adenosine triphosphate (ATP), which cells use as a source of chemical energy for essential cellular processes."
        ),
        MemoryBite(
            id = "mb_5",
            subjectTag = "Economics • National Income",
            title = "GNP vs GDP Concept",
            fact = "Gross National Product (GNP) = GDP + Net Factor Income from Abroad (NFIA). It accounts for income earned by domestic residents globally minus income earned by foreigners domestically."
        ),
        MemoryBite(
            id = "mb_6",
            subjectTag = "English • Grammar Rule",
            title = "Proximity Rule with Either/Or",
            fact = "When two subjects are joined by 'neither... nor' or 'either... or', the verb must always agree with the closer subject in number and person."
        )
    )

    private const val PREFS_NAME = "ssc_memory_bites_prefs"
    private const val KEY_REMEMBERED_IDS = "remembered_bite_ids"
    private const val TWO_DAYS_MILLIS = 2L * 24 * 60 * 60 * 1000L

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getRememberedIds(context: Context): Set<String> {
        val prefs = getPrefs(context)
        return prefs.getStringSet(KEY_REMEMBERED_IDS, emptySet()) ?: emptySet()
    }

    fun markRemembered(context: Context, id: String) {
        val prefs = getPrefs(context)
        val current = getRememberedIds(context).toMutableSet()
        current.add(id)
        prefs.edit().putStringSet(KEY_REMEMBERED_IDS, current).apply()
    }

    fun markReviewLater(context: Context, id: String) {
        val prefs = getPrefs(context)
        val reviewTime = System.currentTimeMillis() + TWO_DAYS_MILLIS
        prefs.edit().putLong("review_time_$id", reviewTime).apply()
    }

    fun getActiveMemoryBite(context: Context): MemoryBite {
        val remembered = getRememberedIds(context)
        val currentTime = System.currentTimeMillis()
        val prefs = getPrefs(context)

        // 1. Check for unremembered items whose review time has elapsed or wasn't postponed
        for (bite in sampleBites) {
            if (bite.id !in remembered) {
                val reviewTime = prefs.getLong("review_time_${bite.id}", 0L)
                if (reviewTime == 0L || reviewTime <= currentTime) {
                    return bite
                }
            }
        }

        // 2. If all unremembered are postponed for later, select the one due earliest
        val postponed = sampleBites.filter { it.id !in remembered }
        if (postponed.isNotEmpty()) {
            return postponed.minByOrNull { prefs.getLong("review_time_${it.id}", Long.MAX_VALUE) } ?: postponed.first()
        }

        // 3. If all items are remembered, return the first one for ongoing periodic reinforcement
        return sampleBites.first()
    }
}
