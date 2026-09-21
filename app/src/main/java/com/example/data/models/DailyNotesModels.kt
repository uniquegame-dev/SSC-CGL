package com.example.data.models

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.launch

enum class DailyNoteCategory(val title: String, val subtitle: String) {
    VOCABULARY(
        title = "Vocabulary",
        subtitle = "10 exam words with meaning, synonyms, antonyms & usage"
    ),
    IDIOMS(
        title = "Idioms & Phrases",
        subtitle = "Repeated idioms with clear meanings & sentence usage"
    ),
    CURRENT_AFFAIRS(
        title = "Current Affairs",
        subtitle = "Categorized daily headlines & concise exam summaries"
    )
}

data class VocabularyItem(
    val id: String,
    val word: String,
    val phonetic: String = "",
    val partOfSpeech: String = "",
    val meaning: String,
    val synonym: String,
    val antonym: String,
    val example: String
)

data class IdiomItem(
    val id: String,
    val phrase: String,
    val meaning: String,
    val example: String
)

data class CurrentAffairItem(
    val id: String,
    val category: String,
    val headline: String,
    val explanation: String,
    val date: String = "Today"
)

object DailyNotesRepository {
    private const val PREFS_NAME = "ssc_daily_notes_prefs"
    private const val KEY_VOCAB_SET = "vocab_current_set_index"
    private const val KEY_IDIOMS_SET = "idioms_current_set_index"
    private const val KEY_CA_SET = "ca_current_set_index"

    // Set of 10 Vocabulary items per set
    val vocabularySets: List<List<VocabularyItem>> = listOf(
        // Set 1 (10 items)
        listOf(
            VocabularyItem(
                id = "v1_1",
                word = "Abate",
                phonetic = "/əˈbeɪt/",
                partOfSpeech = "Verb",
                meaning = "To become less intense, severe, or widespread.",
                synonym = "Subside, Diminish, Lessen, Decrease",
                antonym = "Intensify, Escalate, Increase, Amplify",
                example = "The heavy rainfall finally began to abate by late evening, allowing traffic to resume smoothly."
            ),
            VocabularyItem(
                id = "v1_2",
                word = "Candid",
                phonetic = "/ˈkændɪd/",
                partOfSpeech = "Adjective",
                meaning = "Truthful and straightforward; frank and unreserved.",
                synonym = "Honest, Frank, Forthright, Blunt",
                antonym = "Guarded, Deceitful, Evasive, Disingenuous",
                example = "His candid explanation of his preparation strategy impressed the interview panel."
            ),
            VocabularyItem(
                id = "v1_3",
                word = "Diligent",
                phonetic = "/ˈdɪlɪdʒənt/",
                partOfSpeech = "Adjective",
                meaning = "Having or showing earnest care and steady, focused effort.",
                synonym = "Hardworking, Assiduous, Industrious, Meticulous",
                antonym = "Lazy, Negligent, Careless, Lethargic",
                example = "A diligent aspirant solves mock tests regularly to identify weak areas."
            ),
            VocabularyItem(
                id = "v1_4",
                word = "Ephemeral",
                phonetic = "/ɪˈfemərəl/",
                partOfSpeech = "Adjective",
                meaning = "Lasting for a very short period of time; fleeting.",
                synonym = "Transitory, Transient, Fleeting, Momentary",
                antonym = "Permanent, Everlasting, Eternal, Perennial",
                example = "Social media trends are often ephemeral, vanishing within a few days."
            ),
            VocabularyItem(
                id = "v1_5",
                word = "Frugal",
                phonetic = "/ˈfruːɡl/",
                partOfSpeech = "Adjective",
                meaning = "Economical with regard to money or food; sparing.",
                synonym = "Thrifty, Economical, Sparing, Prudent",
                antonym = "Extravagant, Wasteful, Profligate, Lavish",
                example = "Living a frugal lifestyle helped him save enough funds during his exam preparation years."
            ),
            VocabularyItem(
                id = "v1_6",
                word = "Gregarious",
                phonetic = "/ɡrɪˈɡeəriəs/",
                partOfSpeech = "Adjective",
                meaning = "Fond of company; sociable and outgoing.",
                synonym = "Sociable, Outgoing, Convivial, Friendly",
                antonym = "Introverted, Unsociable, Solitary, Aloof",
                example = "Being gregarious by nature, she quickly formed study groups at the library."
            ),
            VocabularyItem(
                id = "v1_7",
                word = "Hamper",
                phonetic = "/ˈhæmpər/",
                partOfSpeech = "Verb",
                meaning = "To hinder, impede, or obstruct the movement or progress of something.",
                synonym = "Impede, Hinder, Obstruct, Inhibit",
                antonym = "Facilitate, Encourage, Assist, Expedite",
                example = "Lack of proper conceptual clarity can hamper your speed in quantitative aptitude."
            ),
            VocabularyItem(
                id = "v1_8",
                word = "Impeccable",
                phonetic = "/ɪmˈpekəbl/",
                partOfSpeech = "Adjective",
                meaning = "In accordance with the highest standards; faultless and flawless.",
                synonym = "Flawless, Faultless, Pristine, Spotless",
                antonym = "Defective, Flawed, Imperfect, Blemished",
                example = "He scored full marks in English comprehension due to his impeccable grammar skills."
            ),
            VocabularyItem(
                id = "v1_9",
                word = "Lucid",
                phonetic = "/ˈluːsɪd/",
                partOfSpeech = "Adjective",
                meaning = "Expressed clearly; easy to understand and comprehend.",
                synonym = "Clear, Coherent, Intelligible, Transparent",
                antonym = "Vague, Confusing, Obscure, Ambiguous",
                example = "The teacher provided a lucid explanation of complex geometry theorems."
            ),
            VocabularyItem(
                id = "v1_10",
                word = "Mitigate",
                phonetic = "/ˈmɪtɪɡeɪt/",
                partOfSpeech = "Verb",
                meaning = "To make something less severe, harmful, or painful.",
                synonym = "Alleviate, Reduce, Lessen, Soften",
                antonym = "Aggravate, Worsen, Intensify, Exacerbate",
                example = "Consistent revision helps mitigate exam anxiety before the Tier 1 examination."
            )
        ),
        // Set 2 (10 items)
        listOf(
            VocabularyItem(
                id = "v2_1",
                word = "Nefarious",
                phonetic = "/nɪˈfeəriəs/",
                partOfSpeech = "Adjective",
                meaning = "Wicked, villainous, or criminal in nature.",
                synonym = "Evil, Heinous, Wicked, Sinister",
                antonym = "Noble, Virtuous, Honorable, Righteous",
                example = "The cyber police foiled the nefarious activities of financial scammers."
            ),
            VocabularyItem(
                id = "v2_2",
                word = "Obstinate",
                phonetic = "/ˈɒbstɪnət/",
                partOfSpeech = "Adjective",
                meaning = "Stubbornly refusing to change one's opinion or chosen course of action.",
                synonym = "Stubborn, Inflexible, Tenacious, Unyielding",
                antonym = "Flexible, Compliant, Tractable, Yielding",
                example = "His obstinate attitude towards following exam guidelines resulted in penalties."
            ),
            VocabularyItem(
                id = "v2_3",
                word = "Paucity",
                phonetic = "/ˈpɔːsəti/",
                partOfSpeech = "Noun",
                meaning = "The presence of something only in small or insufficient quantities; scarcity.",
                synonym = "Scarcity, Dearth, Shortage, Deficit",
                antonym = "Abundance, Surplus, Plethora, Copiousness",
                example = "A paucity of standard reference books made self-study challenging initially."
            ),
            VocabularyItem(
                id = "v2_4",
                word = "Quell",
                phonetic = "/kwel/",
                partOfSpeech = "Verb",
                meaning = "To put an end to disorder, rebellion, or intense feelings, typically by force.",
                synonym = "Suppress, Extinguish, Subdue, Crush",
                antonym = "Incite, Instigate, Provoke, Inflame",
                example = "The administration deployed extra security forces to quell any possible disturbance."
            ),
            VocabularyItem(
                id = "v2_5",
                word = "Resilient",
                phonetic = "/rɪˈzɪliənt/",
                partOfSpeech = "Adjective",
                meaning = "Able to withstand or recover quickly from difficult conditions or setbacks.",
                synonym = "Tough, Adaptable, Hardy, Buoyant",
                antonym = "Fragile, Vulnerable, Weak, Sensitive",
                example = "Resilient students learn from low mock test scores and bounce back stronger."
            ),
            VocabularyItem(
                id = "v2_6",
                word = "Superfluous",
                phonetic = "/suːˈpɜːfluəs/",
                partOfSpeech = "Adjective",
                meaning = "Exceeding what is sufficient or necessary; extra and redundant.",
                synonym = "Excessive, Redundant, Surplus, Unnecessary",
                antonym = "Essential, Crucial, Necessary, Indispensable",
                example = "Avoid writing superfluous details in descriptive essay writing to stay within word limits."
            ),
            VocabularyItem(
                id = "v2_7",
                word = "Tenacious",
                phonetic = "/təˈneɪʃəs/",
                partOfSpeech = "Adjective",
                meaning = "Tending to keep a firm hold of something; persistent and determined.",
                synonym = "Persistent, Determined, Resolute, Steadfast",
                antonym = "Weak, Irresolute, Wavering, Surrendering",
                example = "Her tenacious dedication towards general studies syllabus ensured top percentile."
            ),
            VocabularyItem(
                id = "v2_8",
                word = "Ubiquitous",
                phonetic = "/juːˈbɪkwɪtəs/",
                partOfSpeech = "Adjective",
                meaning = "Present, appearing, or found everywhere simultaneously.",
                synonym = "Omnipresent, Universal, Pervasive, Everywhere",
                antonym = "Rare, Scarce, Uncommon, Seldom",
                example = "Digital payments have now become ubiquitous across tier-1 and tier-2 Indian cities."
            ),
            VocabularyItem(
                id = "v2_9",
                word = "Venerate",
                phonetic = "/ˈvenəreɪt/",
                partOfSpeech = "Verb",
                meaning = "To regard with great respect and reverence.",
                synonym = "Revere, Respect, Honor, Worship",
                antonym = "Despise, Disdain, Scorn, Disrespect",
                example = "Freedom fighters are venerated nationwide for their supreme sacrifices."
            ),
            VocabularyItem(
                id = "v2_10",
                word = "Zealous",
                phonetic = "/ˈzeləs/",
                partOfSpeech = "Adjective",
                meaning = "Having or showing great energy, enthusiasm, and devotion for a cause.",
                synonym = "Enthusiastic, Passionate, Fervent, Eager",
                antonym = "Apathetic, Indifferent, Unenthusiastic, Dispassionate",
                example = "The candidate was zealous in solving previous years' questions daily."
            )
        )
    )

    // Sets of Idioms & Phrases
    val idiomSets: List<List<IdiomItem>> = listOf(
        // Set 1
        listOf(
            IdiomItem(
                id = "i1_1",
                phrase = "At the eleventh hour",
                meaning = "At the very last possible moment before a deadline.",
                example = "He submitted his SSC CGL application at the eleventh hour just before the portal closed."
            ),
            IdiomItem(
                id = "i1_2",
                phrase = "Burn the midnight oil",
                meaning = "To work or study diligently late into the night.",
                example = "During the final month of preparation, aspirants burn the midnight oil solving mock tests."
            ),
            IdiomItem(
                id = "i1_3",
                phrase = "Piece of cake",
                meaning = "Something that is remarkably simple and easy to accomplish.",
                example = "Once you master basic tables and squares, mental calculation becomes a piece of cake."
            ),
            IdiomItem(
                id = "i1_4",
                phrase = "A blessing in disguise",
                meaning = "An apparent misfortune that eventually results in something good.",
                example = "Missing the initial notification deadline was a blessing in disguise as it gave him extra time to prepare."
            ),
            IdiomItem(
                id = "i1_5",
                phrase = "Through thick and thin",
                meaning = "Under all circumstances, no matter how difficult or challenging.",
                example = "His study partner supported him through thick and thin until both cleared the examination."
            )
        ),
        // Set 2
        listOf(
            IdiomItem(
                id = "i2_1",
                phrase = "Hit the nail on the head",
                meaning = "To state or describe a situation with exact precision.",
                example = "The mentor hit the nail on the head when pointing out calculation errors in the arithmetic section."
            ),
            IdiomItem(
                id = "i2_2",
                phrase = "Bite the bullet",
                meaning = "To face an unavoidable, difficult situation with courage.",
                example = "He decided to bite the bullet and focus on his weakest subject, advanced trigonometry."
            ),
            IdiomItem(
                id = "i2_3",
                phrase = "Leave no stone unturned",
                meaning = "To make every possible effort to achieve a specific goal.",
                example = "The candidate left no stone unturned in revising general awareness blueprints."
            ),
            IdiomItem(
                id = "i2_4",
                phrase = "See eye to eye",
                meaning = "To agree fully with someone on a given matter.",
                example = "The study group saw eye to eye on dividing daily mock test analysis duties."
            ),
            IdiomItem(
                id = "i2_5",
                phrase = "Add fuel to the fire",
                meaning = "To make an already bad or volatile situation worse.",
                example = "Spreading unverified rumors about exam cutoffs will only add fuel to the fire."
            )
        )
    )

    // Sets of Current Affairs
    val currentAffairsSets: List<List<CurrentAffairItem>> = listOf(
        // Set 1
        listOf(
            CurrentAffairItem(
                id = "ca1_1",
                category = "National & Governance",
                headline = "Cabinet Approves Unified Pension Scheme (UPS) for Central Employees",
                explanation = "The Union Cabinet approved the Unified Pension Scheme guaranteeing an assured pension of 50% of the average basic pay drawn in the last 12 months for employees with at least 25 years of service.",
                date = "Latest Edition"
            ),
            CurrentAffairItem(
                id = "ca1_2",
                category = "Science & Technology",
                headline = "ISRO Successfully Launches EOS-08 Earth Observation Satellite",
                explanation = "Launched aboard the SSLV-D3 vehicle, EOS-08 carries state-of-the-art payloads for disaster monitoring, infrared imaging, and ocean surface wind tracking.",
                date = "Latest Edition"
            ),
            CurrentAffairItem(
                id = "ca1_3",
                category = "Sports & Honors",
                headline = "India Wins Historic Double Gold at 45th FIDE Chess Olympiad",
                explanation = "Indian Men's and Women's national chess teams both clinched gold medals in Budapest, marking an unprecedented milestone in global chess history.",
                date = "Latest Edition"
            ),
            CurrentAffairItem(
                id = "ca1_4",
                category = "Economy & Banking",
                headline = "RBI Maintains Repo Rate at 6.5% to Balance Growth and Inflation",
                explanation = "The Monetary Policy Committee voted to keep the policy benchmark repo rate unchanged while focusing on retail inflation targets.",
                date = "Latest Edition"
            )
        ),
        // Set 2
        listOf(
            CurrentAffairItem(
                id = "ca2_1",
                category = "International Affairs",
                headline = "India Assumes Key Leadership Role in Global Biofuels Alliance",
                explanation = "Expanding renewable energy initiatives, India partnered with international stakeholders to accelerate adoption of sustainable biofuels across transport sectors.",
                date = "Updated Edition"
            ),
            CurrentAffairItem(
                id = "ca2_2",
                category = "Awards & Appointments",
                headline = "National Teacher Awards Bestowed on 50 Educators by the President",
                explanation = "Conferred at Vigyan Bhawan, New Delhi on Teachers' Day to honor exceptional pedagogical innovations and community leadership in education.",
                date = "Updated Edition"
            ),
            CurrentAffairItem(
                id = "ca2_3",
                category = "Environment & Ecology",
                headline = "India Adds Three More Wetlands to the Ramsar List of Global Importance",
                explanation = "With the inclusion of newly designated bird sanctuaries from Tamil Nadu and Madhya Pradesh, India's total Ramsar wetland tally increased to 85.",
                date = "Updated Edition"
            ),
            CurrentAffairItem(
                id = "ca2_4",
                category = "Defense & Security",
                headline = "Indian Navy Commissioned Second Arihant-Class Submarine INS Arighaat",
                explanation = "Strengthening India's nuclear triad deterrence, INS Arighaat was commissioned at Visakhapatnam equipped with indigenous technology and SLBM capabilities.",
                date = "Updated Edition"
            )
        )
    )

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Vocabulary Set helpers
    fun getVocabularySetIndex(context: Context): Int {
        val prefs = getPrefs(context)
        return prefs.getInt(KEY_VOCAB_SET, 0) % vocabularySets.size
    }

    fun getCurrentVocabularySet(context: Context): List<VocabularyItem> {
        val index = getVocabularySetIndex(context)
        return vocabularySets[index]
    }

    fun markVocabularyCompleted(context: Context): Int {
        val prefs = getPrefs(context)
        val currentIndex = getVocabularySetIndex(context)
        val nextIndex = (currentIndex + 1) % vocabularySets.size
        prefs.edit().putInt(KEY_VOCAB_SET, nextIndex).apply()
        
        // Asynchronously update Room tables
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
            try {
                val roomRepo = com.example.data.local.DailyNotesRoomRepository.getInstance(context)
                val active = roomRepo.getActiveSetDirect(com.example.data.local.DailyNotesRoomRepository.TYPE_VOCAB)
                if (active != null) {
                    roomRepo.markSetCompleted(com.example.data.local.DailyNotesRoomRepository.TYPE_VOCAB, active.id)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return nextIndex
    }

    // Idioms Set helpers
    fun getIdiomsSetIndex(context: Context): Int {
        val prefs = getPrefs(context)
        return prefs.getInt(KEY_IDIOMS_SET, 0) % idiomSets.size
    }

    fun getIdiomsSet(context: Context): List<IdiomItem> {
        val index = getIdiomsSetIndex(context)
        return idiomSets[index]
    }

    fun markIdiomsCompleted(context: Context): Int {
        val prefs = getPrefs(context)
        val currentIndex = getIdiomsSetIndex(context)
        val nextIndex = (currentIndex + 1) % idiomSets.size
        prefs.edit().putInt(KEY_IDIOMS_SET, nextIndex).apply()

        // Asynchronously update Room tables
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
            try {
                val roomRepo = com.example.data.local.DailyNotesRoomRepository.getInstance(context)
                val active = roomRepo.getActiveSetDirect(com.example.data.local.DailyNotesRoomRepository.TYPE_IDIOM)
                if (active != null) {
                    roomRepo.markSetCompleted(com.example.data.local.DailyNotesRoomRepository.TYPE_IDIOM, active.id)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return nextIndex
    }

    // Current Affairs Set helpers
    fun getCurrentAffairsSetIndex(context: Context): Int {
        val prefs = getPrefs(context)
        return prefs.getInt(KEY_CA_SET, 0) % currentAffairsSets.size
    }

    fun getCurrentAffairsSet(context: Context): List<CurrentAffairItem> {
        val index = getCurrentAffairsSetIndex(context)
        return currentAffairsSets[index]
    }

    fun markCurrentAffairsCompleted(context: Context): Int {
        val prefs = getPrefs(context)
        val currentIndex = getCurrentAffairsSetIndex(context)
        val nextIndex = (currentIndex + 1) % currentAffairsSets.size
        prefs.edit().putInt(KEY_CA_SET, nextIndex).apply()

        // Asynchronously update Room tables
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
            try {
                val roomRepo = com.example.data.local.DailyNotesRoomRepository.getInstance(context)
                val active = roomRepo.getActiveSetDirect(com.example.data.local.DailyNotesRoomRepository.TYPE_CURRENT_AFFAIRS)
                if (active != null) {
                    roomRepo.markSetCompleted(com.example.data.local.DailyNotesRoomRepository.TYPE_CURRENT_AFFAIRS, active.id)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return nextIndex
    }
}
