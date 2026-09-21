package com.example.data.local

import com.example.data.local.entities.DailyNoteItemEntity
import com.example.data.local.entities.DailyNoteSetEntity

/**
 * Seed data containing exactly 50 items per category (5 sets of 10 items each)
 * for Vocabulary, Idioms & Phrases, and Current Affairs.
 */
object DailyNotesSeedData {

    data class SetDefinition(
        val setNumber: Int,
        val title: String,
        val items: List<ItemDefinition>
    )

    data class ItemDefinition(
        val wordOrTitle: String,
        val phonetic: String = "",
        val partOfSpeech: String = "",
        val meaning: String,
        val synonym: String = "",
        val antonym: String = "",
        val example: String = "",
        val category: String = "",
        val factDate: String = ""
    )

    // ==========================================
    // VOCABULARY: 5 Sets x 10 Items = 50 Items
    // ==========================================
    val vocabSets: List<SetDefinition> = listOf(
        // Set 1 (10 Items)
        SetDefinition(
            setNumber = 1,
            title = "Set 1: High Frequency Words",
            items = listOf(
                ItemDefinition(
                    wordOrTitle = "Abate",
                    phonetic = "/əˈbeɪt/",
                    partOfSpeech = "Verb",
                    meaning = "To become less intense, severe, or widespread.",
                    synonym = "Subside, Diminish, Lessen, Decrease",
                    antonym = "Intensify, Escalate, Increase, Amplify",
                    example = "The heavy rainfall finally began to abate by late evening, allowing traffic to resume smoothly."
                ),
                ItemDefinition(
                    wordOrTitle = "Candid",
                    phonetic = "/ˈkændɪd/",
                    partOfSpeech = "Adjective",
                    meaning = "Truthful and straightforward; frank and unreserved.",
                    synonym = "Honest, Frank, Forthright, Blunt",
                    antonym = "Guarded, Deceitful, Evasive, Disingenuous",
                    example = "His candid explanation of his preparation strategy impressed the interview panel."
                ),
                ItemDefinition(
                    wordOrTitle = "Diligent",
                    phonetic = "/ˈdɪlɪdʒənt/",
                    partOfSpeech = "Adjective",
                    meaning = "Having or showing earnest care and steady, focused effort.",
                    synonym = "Hardworking, Assiduous, Industrious, Meticulous",
                    antonym = "Lazy, Negligent, Careless, Lethargic",
                    example = "A diligent aspirant solves mock tests regularly to identify weak areas."
                ),
                ItemDefinition(
                    wordOrTitle = "Ephemeral",
                    phonetic = "/ɪˈfemərəl/",
                    partOfSpeech = "Adjective",
                    meaning = "Lasting for a very short period of time; fleeting.",
                    synonym = "Transitory, Transient, Fleeting, Momentary",
                    antonym = "Permanent, Everlasting, Eternal, Perennial",
                    example = "Social media trends are often ephemeral, vanishing within a few days."
                ),
                ItemDefinition(
                    wordOrTitle = "Frugal",
                    phonetic = "/ˈfruːɡl/",
                    partOfSpeech = "Adjective",
                    meaning = "Economical with regard to money or food; sparing.",
                    synonym = "Thrifty, Economical, Sparing, Prudent",
                    antonym = "Extravagant, Wasteful, Profligate, Lavish",
                    example = "Living a frugal lifestyle helped him save enough funds during his exam preparation years."
                ),
                ItemDefinition(
                    wordOrTitle = "Gregarious",
                    phonetic = "/ɡrɪˈɡeəriəs/",
                    partOfSpeech = "Adjective",
                    meaning = "Fond of company; sociable and outgoing.",
                    synonym = "Sociable, Outgoing, Convivial, Friendly",
                    antonym = "Introverted, Unsociable, Solitary, Aloof",
                    example = "Being gregarious by nature, she quickly formed study groups at the library."
                ),
                ItemDefinition(
                    wordOrTitle = "Hamper",
                    phonetic = "/ˈhæmpər/",
                    partOfSpeech = "Verb",
                    meaning = "To hinder, impede, or obstruct the movement or progress of something.",
                    synonym = "Impede, Hinder, Obstruct, Inhibit",
                    antonym = "Facilitate, Encourage, Assist, Expedite",
                    example = "Lack of proper conceptual clarity can hamper your speed in quantitative aptitude."
                ),
                ItemDefinition(
                    wordOrTitle = "Impeccable",
                    phonetic = "/ɪmˈpekəbl/",
                    partOfSpeech = "Adjective",
                    meaning = "In accordance with the highest standards; faultless and flawless.",
                    synonym = "Flawless, Faultless, Pristine, Spotless",
                    antonym = "Defective, Flawed, Imperfect, Blemished",
                    example = "He scored full marks in English comprehension due to his impeccable grammar skills."
                ),
                ItemDefinition(
                    wordOrTitle = "Lucid",
                    phonetic = "/ˈluːsɪd/",
                    partOfSpeech = "Adjective",
                    meaning = "Expressed clearly; easy to understand and comprehend.",
                    synonym = "Clear, Coherent, Intelligible, Transparent",
                    antonym = "Vague, Confusing, Obscure, Ambiguous",
                    example = "The teacher provided a lucid explanation of complex geometry theorems."
                ),
                ItemDefinition(
                    wordOrTitle = "Mitigate",
                    phonetic = "/ˈmɪtɪɡeɪt/",
                    partOfSpeech = "Verb",
                    meaning = "To make something less severe, harmful, or painful.",
                    synonym = "Alleviate, Reduce, Lessen, Soften",
                    antonym = "Aggravate, Worsen, Intensify, Exacerbate",
                    example = "Consistent revision helps mitigate exam anxiety before the Tier 1 examination."
                )
            )
        ),
        // Set 2 (10 Items)
        SetDefinition(
            setNumber = 2,
            title = "Set 2: Tier I & II Essentials",
            items = listOf(
                ItemDefinition(
                    wordOrTitle = "Nefarious",
                    phonetic = "/nɪˈfeəriəs/",
                    partOfSpeech = "Adjective",
                    meaning = "Wicked, villainous, or criminal in nature.",
                    synonym = "Evil, Heinous, Wicked, Sinister",
                    antonym = "Noble, Virtuous, Honorable, Righteous",
                    example = "The cyber police foiled the nefarious activities of financial scammers."
                ),
                ItemDefinition(
                    wordOrTitle = "Obstinate",
                    phonetic = "/ˈɒbstɪnət/",
                    partOfSpeech = "Adjective",
                    meaning = "Stubbornly refusing to change one's opinion or chosen course of action.",
                    synonym = "Stubborn, Inflexible, Tenacious, Unyielding",
                    antonym = "Flexible, Compliant, Tractable, Yielding",
                    example = "His obstinate attitude towards following exam guidelines resulted in penalties."
                ),
                ItemDefinition(
                    wordOrTitle = "Paucity",
                    phonetic = "/ˈpɔːsəti/",
                    partOfSpeech = "Noun",
                    meaning = "The presence of something only in small or insufficient quantities; scarcity.",
                    synonym = "Scarcity, Dearth, Shortage, Deficit",
                    antonym = "Abundance, Surplus, Plethora, Copiousness",
                    example = "A paucity of standard reference books made self-study challenging initially."
                ),
                ItemDefinition(
                    wordOrTitle = "Quell",
                    phonetic = "/kwel/",
                    partOfSpeech = "Verb",
                    meaning = "To put an end to disorder, rebellion, or intense feelings, typically by force.",
                    synonym = "Suppress, Extinguish, Subdue, Crush",
                    antonym = "Incite, Instigate, Provoke, Inflame",
                    example = "The administration deployed extra security forces to quell any possible disturbance."
                ),
                ItemDefinition(
                    wordOrTitle = "Resilient",
                    phonetic = "/rɪˈzɪliənt/",
                    partOfSpeech = "Adjective",
                    meaning = "Able to withstand or recover quickly from difficult conditions or setbacks.",
                    synonym = "Tough, Adaptable, Hardy, Buoyant",
                    antonym = "Fragile, Vulnerable, Weak, Sensitive",
                    example = "Resilient students learn from low mock test scores and bounce back stronger."
                ),
                ItemDefinition(
                    wordOrTitle = "Superfluous",
                    phonetic = "/suːˈpɜːfluəs/",
                    partOfSpeech = "Adjective",
                    meaning = "Exceeding what is sufficient or necessary; extra and redundant.",
                    synonym = "Excessive, Redundant, Surplus, Unnecessary",
                    antonym = "Essential, Crucial, Necessary, Indispensable",
                    example = "Avoid writing superfluous details in descriptive essay writing to stay within word limits."
                ),
                ItemDefinition(
                    wordOrTitle = "Tenacious",
                    phonetic = "/təˈneɪʃəs/",
                    partOfSpeech = "Adjective",
                    meaning = "Tending to keep a firm hold of something; persistent and determined.",
                    synonym = "Persistent, Determined, Resolute, Steadfast",
                    antonym = "Weak, Irresolute, Wavering, Surrendering",
                    example = "Her tenacious dedication towards general studies syllabus ensured top percentile."
                ),
                ItemDefinition(
                    wordOrTitle = "Ubiquitous",
                    phonetic = "/juːˈbɪkwɪtəs/",
                    partOfSpeech = "Adjective",
                    meaning = "Present, appearing, or found everywhere simultaneously.",
                    synonym = "Omnipresent, Universal, Pervasive, Everywhere",
                    antonym = "Rare, Scarce, Uncommon, Seldom",
                    example = "Digital payments have now become ubiquitous across tier-1 and tier-2 Indian cities."
                ),
                ItemDefinition(
                    wordOrTitle = "Venerate",
                    phonetic = "/ˈvenəreɪt/",
                    partOfSpeech = "Verb",
                    meaning = "To regard with great respect and reverence.",
                    synonym = "Revere, Respect, Honor, Worship",
                    antonym = "Despise, Disdain, Scorn, Disrespect",
                    example = "Freedom fighters are venerated nationwide for their supreme sacrifices."
                ),
                ItemDefinition(
                    wordOrTitle = "Zealous",
                    phonetic = "/ˈzeləs/",
                    partOfSpeech = "Adjective",
                    meaning = "Having or showing great energy, enthusiasm, and devotion for a cause.",
                    synonym = "Enthusiastic, Passionate, Fervent, Eager",
                    antonym = "Apathetic, Indifferent, Unenthusiastic, Dispassionate",
                    example = "The candidate was zealous in solving previous years' questions daily."
                )
            )
        ),
        // Set 3 (10 Items)
        SetDefinition(
            setNumber = 3,
            title = "Set 3: Advanced Comprehension",
            items = listOf(
                ItemDefinition(
                    wordOrTitle = "Acumen",
                    phonetic = "/ˈækjəmən/",
                    partOfSpeech = "Noun",
                    meaning = "The ability to make good judgments and quick decisions, typically in a particular domain.",
                    synonym = "Shrewdness, Astuteness, Sharpness, Insight",
                    antonym = "Ignorance, Ineptitude, Stupidity, Naivety",
                    example = "His financial acumen allowed him to manage budget allocations effectively."
                ),
                ItemDefinition(
                    wordOrTitle = "Belligerent",
                    phonetic = "/bəˈlɪdʒərənt/",
                    partOfSpeech = "Adjective",
                    meaning = "Hostile, aggressive, and eager to fight or argue.",
                    synonym = "Combative, Pugnacious, Aggressive, Hostile",
                    antonym = "Peaceable, Friendly, Harmonious, Conciliatory",
                    example = "The belligerent stance of the opposing delegate stalled peaceful diplomatic talks."
                ),
                ItemDefinition(
                    wordOrTitle = "Capricious",
                    phonetic = "/kəˈprɪʃəs/",
                    partOfSpeech = "Adjective",
                    meaning = "Given to sudden and unaccountable changes of mood or behavior.",
                    synonym = "Fickle, Inconsistent, Variable, Mercurial",
                    antonym = "Stable, Consistent, Constant, Predictable",
                    example = "A capricious approach to daily study schedules ruins consistent long-term preparation."
                ),
                ItemDefinition(
                    wordOrTitle = "Disparate",
                    phonetic = "/ˈdɪspərət/",
                    partOfSpeech = "Adjective",
                    meaning = "Essentially different in kind; not allowing comparison.",
                    synonym = "Different, Contrasting, Diverse, Distinct",
                    antonym = "Similar, Uniform, Homogeneous, Identical",
                    example = "The committee brought together experts from disparate fields of technology and law."
                ),
                ItemDefinition(
                    wordOrTitle = "Eloquent",
                    phonetic = "/ˈeləkwənt/",
                    partOfSpeech = "Adjective",
                    meaning = "Fluent or persuasive in speaking or writing.",
                    synonym = "Articulate, Fluent, Expressive, Persuasive",
                    antonym = "Inarticulate, Hesitant, Mumbled, Tongue-tied",
                    example = "Her eloquent speech on constitutional values earned a standing ovation from the audience."
                ),
                ItemDefinition(
                    wordOrTitle = "Fastidious",
                    phonetic = "/fæˈstɪdiəs/",
                    partOfSpeech = "Adjective",
                    meaning = "Very attentive to and concerned about accuracy and detail.",
                    synonym = "Meticulous, Punctilious, Scrupulous, Particular",
                    antonym = "Careless, Sloppy, Easygoing, Lax",
                    example = "A fastidious proofreader catches minor typographic errors in official notifications."
                ),
                ItemDefinition(
                    wordOrTitle = "Garrulous",
                    phonetic = "/ˈɡærələs/",
                    partOfSpeech = "Adjective",
                    meaning = "Excessively talkative, especially on trivial matters.",
                    synonym = "Talkative, Loquacious, Voluble, Chatty",
                    antonym = "Taciturn, Reticent, Reserved, Silent",
                    example = "The garrulous passenger kept talking throughout the three-hour bus journey."
                ),
                ItemDefinition(
                    wordOrTitle = "Harangue",
                    phonetic = "/həˈræŋ/",
                    partOfSpeech = "Noun / Verb",
                    meaning = "A lengthy and aggressive speech delivered with strong emotion.",
                    synonym = "Tirade, Diatribe, Lecture, Rant",
                    antonym = "Praise, Eulogy, Panegyric, Compliment",
                    example = "The coach delivered a stern harangue to the team after a lackadaisical performance."
                ),
                ItemDefinition(
                    wordOrTitle = "Inundate",
                    phonetic = "/ˈɪnʌndeɪt/",
                    partOfSpeech = "Verb",
                    meaning = "To overwhelm with things or people to be dealt with; to flood.",
                    synonym = "Overwhelm, Flood, Deluge, Submerge",
                    antonym = "Drain, Parched, Deplete, Starve",
                    example = "The helpdesk was inundated with queries immediately after the release of the answer key."
                ),
                ItemDefinition(
                    wordOrTitle = "Juxtapose",
                    phonetic = "/ˌdʒʌkstəˈpəʊz/",
                    partOfSpeech = "Verb",
                    meaning = "To place or deal with close together for contrasting effect.",
                    synonym = "Collocate, Compare, Contrast, Parallel",
                    antonym = "Separate, Disconnect, Isolate, Divide",
                    example = "The essay juxtaposed the socio-economic conditions of rural and urban areas."
                )
            )
        ),
        // Set 4 (10 Items)
        SetDefinition(
            setNumber = 4,
            title = "Set 4: Frequent Exam Antonyms & Synonyms",
            items = listOf(
                ItemDefinition(
                    wordOrTitle = "Keen",
                    phonetic = "/kiːn/",
                    partOfSpeech = "Adjective",
                    meaning = "Having or showing eagerness or enthusiasm; sharp and penetrating.",
                    synonym = "Eager, Enthusiastic, Sharp, Acute",
                    antonym = "Dull, Reluctant, Apathetic, Blunt",
                    example = "He showed a keen interest in mastering statistical formulas for the Junior Statistical Officer post."
                ),
                ItemDefinition(
                    wordOrTitle = "Lethargic",
                    phonetic = "/ləˈθɑːdʒɪk/",
                    partOfSpeech = "Adjective",
                    meaning = "Affected by lethargy; sluggish, tired, and apathetic.",
                    synonym = "Sluggish, Inert, Inactive, Torpid",
                    antonym = "Energetic, Vigorous, Dynamic, Active",
                    example = "Eating heavy meals right before afternoon study sessions makes aspirants feel lethargic."
                ),
                ItemDefinition(
                    wordOrTitle = "Malleable",
                    phonetic = "/ˈmæliəbl/",
                    partOfSpeech = "Adjective",
                    meaning = "Easily influenced, pliable, or able to be shaped into sheets.",
                    synonym = "Pliable, Flexible, Adaptable, Ductile",
                    antonym = "Rigid, Inflexible, Stiff, Unyielding",
                    example = "Young trainees possess malleable minds that readily adapt to administrative protocol."
                ),
                ItemDefinition(
                    wordOrTitle = "Nonchalant",
                    phonetic = "/ˈnɒnʃələnt/",
                    partOfSpeech = "Adjective",
                    meaning = "Feeling or appearing casually calm and relaxed; unconcerned.",
                    synonym = "Casual, Unconcerned, Composed, Indifferent",
                    antonym = "Anxious, Concerned, Agitated, Panicky",
                    example = "Despite the exam ticking down its final minutes, he remained remarkably nonchalant."
                ),
                ItemDefinition(
                    wordOrTitle = "Onerous",
                    phonetic = "/ˈəʊnərəs/",
                    partOfSpeech = "Adjective",
                    meaning = "Involving an amount of effort and difficulty that is oppressively burdensome.",
                    synonym = "Burdensome, Heavy, Arduous, Taxing",
                    antonym = "Effortless, Easy, Light, Simple",
                    example = "Drafting the multi-departmental audit report proved to be an onerous task for the junior inspector."
                ),
                ItemDefinition(
                    wordOrTitle = "Pragmatic",
                    phonetic = "/præɡˈmætɪk/",
                    partOfSpeech = "Adjective",
                    meaning = "Dealing with things sensibly and realistically based on practical considerations.",
                    synonym = "Practical, Realistic, Sensible, Logical",
                    antonym = "Idealistic, Impractical, Unrealistic, Visionary",
                    example = "He took a pragmatic decision to skip lengthy questions and maximize overall attempt count."
                ),
                ItemDefinition(
                    wordOrTitle = "Quandary",
                    phonetic = "/ˈkwɒndri/",
                    partOfSpeech = "Noun",
                    meaning = "A state of perplexity or uncertainty over what to do in a difficult situation.",
                    synonym = "Dilemma, Predicament, Plight, Perplexity",
                    antonym = "Certainty, Clarity, Resolution, Confidence",
                    example = "The student was in a quandary over whether to attempt Tier 2 English mock first or Maths mock."
                ),
                ItemDefinition(
                    wordOrTitle = "Rancor",
                    phonetic = "/ˈræŋkər/",
                    partOfSpeech = "Noun",
                    meaning = "Bitterness or resentfulness, especially when long-standing.",
                    synonym = "Bitterness, Spite, Malice, Animosity",
                    antonym = "Goodwill, Amity, Benevolence, Kindness",
                    example = "The debate ended cordially without any personal rancor between the participants."
                ),
                ItemDefinition(
                    wordOrTitle = "Sagacious",
                    phonetic = "/səˈɡeɪʃəs/",
                    partOfSpeech = "Adjective",
                    meaning = "Having or showing keen mental discernment and good judgment; wise.",
                    synonym = "Wise, Shrewd, Insightful, Judicious",
                    antonym = "Foolish, Silly, Short-sighted, Ignorant",
                    example = "The sagacious advice of the veteran mentor guided many aspirants to final selection."
                ),
                ItemDefinition(
                    wordOrTitle = "Taciturn",
                    phonetic = "/ˈtæsɪtɜːn/",
                    partOfSpeech = "Adjective",
                    meaning = "Reserved or uncommunicative in speech; saying little.",
                    synonym = "Untalkative, Reticent, Quiet, Reserved",
                    antonym = "Garrulous, Loquacious, Chatty, Talkative",
                    example = "By nature taciturn, the officer listened carefully to everyone before issuing concise directions."
                )
            )
        ),
        // Set 5 (10 Items)
        SetDefinition(
            setNumber = 5,
            title = "Set 5: High-Scoring Vocabulary Mastery",
            items = listOf(
                ItemDefinition(
                    wordOrTitle = "Uncanny",
                    phonetic = "/ʌnˈkæni/",
                    partOfSpeech = "Adjective",
                    meaning = "Strange or mysterious, especially in an unsettling way; beyond normal explanation.",
                    synonym = "Eerie, Supernatural, Extraordinary, Remarkable",
                    antonym = "Ordinary, Normal, Commonplace, Typical",
                    example = "He had an uncanny ability to identify the correct option in multiple-choice questions rapidly."
                ),
                ItemDefinition(
                    wordOrTitle = "Vex",
                    phonetic = "/veks/",
                    partOfSpeech = "Verb",
                    meaning = "To make someone feel annoyed, frustrated, or worried.",
                    synonym = "Annoy, Irritate, Bother, Exasperate",
                    antonym = "Soothe, Please, Calm, Delight",
                    example = "Ambiguous grammar questions often vex aspirants during competitive test revisions."
                ),
                ItemDefinition(
                    wordOrTitle = "Wary",
                    phonetic = "/ˈweəri/",
                    partOfSpeech = "Adjective",
                    meaning = "Feeling or showing caution about possible dangers or problems.",
                    synonym = "Cautious, Careful, Vigilant, Alert",
                    antonym = "Unwary, Careless, Rash, Heedless",
                    example = "Be wary of negative marking and avoid blind guessing in the general awareness section."
                ),
                ItemDefinition(
                    wordOrTitle = "Xenophobia",
                    phonetic = "/ˌzenəˈfəʊbiə/",
                    partOfSpeech = "Noun",
                    meaning = "Dislike of or prejudice against people from other countries.",
                    synonym = "Chauvinism, Intolerance, Isolationism, Bigotry",
                    antonym = "Inclusiveness, Open-mindedness, Tolerance, Acceptance",
                    example = "Global organizations actively work to eliminate xenophobia and promote cultural exchange."
                ),
                ItemDefinition(
                    wordOrTitle = "Yield",
                    phonetic = "/jiːld/",
                    partOfSpeech = "Verb",
                    meaning = "To produce or provide a natural, agricultural, or industrial product; to surrender.",
                    synonym = "Produce, Generate, Surrender, Relinquish",
                    antonym = "Resist, Withstand, Defy, Retain",
                    example = "Consistent daily practice of reading comprehension passages will yield remarkable score gains."
                ),
                ItemDefinition(
                    wordOrTitle = "Zealot",
                    phonetic = "/ˈzelət/",
                    partOfSpeech = "Noun",
                    meaning = "A person who is fanatical and uncompromising in pursuit of their religious, political, or other ideals.",
                    synonym = "Fanatic, Enthusiast, Extremist, Radical",
                    antonym = "Moderate, Non-partisan, Pragmatist, Realist",
                    example = "He was a fitness zealot who never missed his morning workout even during exam week."
                ),
                ItemDefinition(
                    wordOrTitle = "Abhor",
                    phonetic = "/əbˈhɔːr/",
                    partOfSpeech = "Verb",
                    meaning = "To regard with disgust, hatred, and strong loathing.",
                    synonym = "Detest, Hate, Loathe, Despise",
                    antonym = "Love, Admire, Cherish, Adore",
                    example = "Ethical public servants abhor corruption in any form."
                ),
                ItemDefinition(
                    wordOrTitle = "Benevolent",
                    phonetic = "/bəˈnevələnt/",
                    partOfSpeech = "Adjective",
                    meaning = "Well meaning and kindly; serving a charitable rather than profit-making cause.",
                    synonym = "Kind, Charitable, Generous, Magnanimous",
                    antonym = "Malevolent, Cruel, Unkind, Spiteful",
                    example = "The benevolent patron funded scholarships for underprivileged competitive exam aspirants."
                ),
                ItemDefinition(
                    wordOrTitle = "Coerce",
                    phonetic = "/kəʊˈɜːs/",
                    partOfSpeech = "Verb",
                    meaning = "To persuade an unwilling person to do something by using force or threats.",
                    synonym = "Force, Compel, Pressure, Intimidate",
                    antonym = "Persuade, Encourage, Invite, Allow",
                    example = "The constitution ensures that no citizen can be coerced into self-incrimination."
                ),
                ItemDefinition(
                    wordOrTitle = "Debilitate",
                    phonetic = "/dɪˈbɪlɪteɪt/",
                    partOfSpeech = "Verb",
                    meaning = "To make someone very weak and infirm; to hinder or harm.",
                    synonym = "Weaken, Enfeeble, Impair, Cripple",
                    antonym = "Strengthen, Invigorate, Energize, Fortify",
                    example = "Severe viral fever can debilitate a candidate's focus during crucial study weeks."
                )
            )
        )
    )

    // ===============================================
    // IDIOMS & PHRASES: 5 Sets x 10 Items = 50 Items
    // ===============================================
    val idiomSets: List<SetDefinition> = listOf(
        // Set 1 (10 Items)
        SetDefinition(
            setNumber = 1,
            title = "Set 1: Common Exam Idioms",
            items = listOf(
                ItemDefinition(
                    wordOrTitle = "At the eleventh hour",
                    meaning = "At the very last possible moment before a deadline.",
                    example = "He submitted his SSC CGL application at the eleventh hour just before the portal closed."
                ),
                ItemDefinition(
                    wordOrTitle = "Burn the midnight oil",
                    meaning = "To work or study diligently late into the night.",
                    example = "During the final month of preparation, aspirants burn the midnight oil solving mock tests."
                ),
                ItemDefinition(
                    wordOrTitle = "Piece of cake",
                    meaning = "Something that is remarkably simple and easy to accomplish.",
                    example = "Once you master basic tables and squares, mental calculation becomes a piece of cake."
                ),
                ItemDefinition(
                    wordOrTitle = "A blessing in disguise",
                    meaning = "An apparent misfortune that eventually results in something good.",
                    example = "Missing the initial notification deadline was a blessing in disguise as it gave him extra time to prepare."
                ),
                ItemDefinition(
                    wordOrTitle = "Through thick and thin",
                    meaning = "Under all circumstances, no matter how difficult or challenging.",
                    example = "His study partner supported him through thick and thin until both cleared the examination."
                ),
                ItemDefinition(
                    wordOrTitle = "Hit the nail on the head",
                    meaning = "To state or describe a situation with exact precision.",
                    example = "The mentor hit the nail on the head when pointing out calculation errors in the arithmetic section."
                ),
                ItemDefinition(
                    wordOrTitle = "Bite the bullet",
                    meaning = "To face an unavoidable, difficult situation with courage.",
                    example = "He decided to bite the bullet and focus on his weakest subject, advanced trigonometry."
                ),
                ItemDefinition(
                    wordOrTitle = "Leave no stone unturned",
                    meaning = "To make every possible effort to achieve a specific goal.",
                    example = "The candidate left no stone unturned in revising general awareness blueprints."
                ),
                ItemDefinition(
                    wordOrTitle = "See eye to eye",
                    meaning = "To agree fully with someone on a given matter.",
                    example = "The study group saw eye to eye on dividing daily mock test analysis duties."
                ),
                ItemDefinition(
                    wordOrTitle = "Add fuel to the fire",
                    meaning = "To make an already bad or volatile situation worse.",
                    example = "Spreading unverified rumors about exam cutoffs will only add fuel to the fire."
                )
            )
        ),
        // Set 2 (10 Items)
        SetDefinition(
            setNumber = 2,
            title = "Set 2: Action & Strategy Phrases",
            items = listOf(
                ItemDefinition(
                    wordOrTitle = "A hard nut to crack",
                    meaning = "A difficult problem to solve or a person hard to persuade.",
                    example = "Data interpretation sets in Tier 2 mathematics can be a hard nut to crack without shortcut formulas."
                ),
                ItemDefinition(
                    wordOrTitle = "Barking up the wrong tree",
                    meaning = "Pursuing a mistaken line of thought or accusing the wrong person.",
                    example = "If you expect to clear the exam without daily revision, you are barking up the wrong tree."
                ),
                ItemDefinition(
                    wordOrTitle = "Break the ice",
                    meaning = "To initiate a conversation in a tense or quiet social situation.",
                    example = "The group leader told a light joke to break the ice during the interview orientation session."
                ),
                ItemDefinition(
                    wordOrTitle = "By leaps and bounds",
                    meaning = "Very rapidly and with substantial, visible progress.",
                    example = "His mock test accuracy improved by leaps and bounds after analyzing silly mistakes consistently."
                ),
                ItemDefinition(
                    wordOrTitle = "Call it a day",
                    meaning = "To stop working on something for the remainder of the day.",
                    example = "After completing eight hours of intensive reasoning practice, they decided to call it a day."
                ),
                ItemDefinition(
                    wordOrTitle = "Cry over spilt milk",
                    meaning = "To worry or complain about past mistakes that cannot be undone.",
                    example = "Do not cry over spilt milk about low scores in previous attempts; focus on current preparation."
                ),
                ItemDefinition(
                    wordOrTitle = "Curiosity killed the cat",
                    meaning = "Being overly inquisitive can lead you into unnecessary trouble.",
                    example = "Stick strictly to the prescribed syllabus blueprint, as curiosity killed the cat with out-of-scope topics."
                ),
                ItemDefinition(
                    wordOrTitle = "Cut corners",
                    meaning = "To do something poorly or cheaply in order to save time or money.",
                    example = "Never cut corners while learning fundamental grammar rules for the English language paper."
                ),
                ItemDefinition(
                    wordOrTitle = "Face the music",
                    meaning = "To accept the unpleasant consequences of one's actions.",
                    example = "Aspirants who skip syllabus revisions eventually have to face the music on exam day."
                ),
                ItemDefinition(
                    wordOrTitle = "Once in a blue moon",
                    meaning = "Something that occurs very rarely or seldom.",
                    example = "Questions on obscure archaic history appear only once in a blue moon in Tier 1."
                )
            )
        ),
        // Set 3 (10 Items)
        SetDefinition(
            setNumber = 3,
            title = "Set 3: Repeated SSC CGL Idioms",
            items = listOf(
                ItemDefinition(
                    wordOrTitle = "Spill the beans",
                    meaning = "To reveal a secret or confidential information prematurely.",
                    example = "The commission was careful not to spill the beans regarding question paper changes."
                ),
                ItemDefinition(
                    wordOrTitle = "Take it with a grain of salt",
                    meaning = "To view information with skepticism and not accept it completely as true.",
                    example = "Take speculative expected cutoff predictions on social media with a grain of salt."
                ),
                ItemDefinition(
                    wordOrTitle = "Under the weather",
                    meaning = "Feeling slightly unwell, sick, or indisposed.",
                    example = "He felt under the weather yesterday but recovered quickly to resume his study schedule."
                ),
                ItemDefinition(
                    wordOrTitle = "When pigs fly",
                    meaning = "Something that will never happen; impossible.",
                    example = "He jokingly said he would skip mathematics revision only when pigs fly."
                ),
                ItemDefinition(
                    wordOrTitle = "You can't judge a book by its cover",
                    meaning = "You should not form an opinion based purely on external appearance.",
                    example = "The question seemed lengthy and intimidating, but you can't judge a book by its cover; it was straightforward."
                ),
                ItemDefinition(
                    wordOrTitle = "Beat around the bush",
                    meaning = "To discuss a matter without coming directly to the main point.",
                    example = "Stop beating around the bush and directly state your doubt regarding permutation formulas."
                ),
                ItemDefinition(
                    wordOrTitle = "Best of both worlds",
                    meaning = "A situation where you can enjoy the advantages of two distinct things simultaneously.",
                    example = "Self-study combined with online mock test series offers the best of both worlds for working aspirants."
                ),
                ItemDefinition(
                    wordOrTitle = "Blow one's own trumpet",
                    meaning = "To boast or praise one's own achievements excessively.",
                    example = "A humble scholar never blows his own trumpet about clearing preliminary stages."
                ),
                ItemDefinition(
                    wordOrTitle = "Burn bridges",
                    meaning = "To destroy one's path, connections, or relationships so that return is impossible.",
                    example = "Never burn bridges with past mentors or peers who offered guidance during your journey."
                ),
                ItemDefinition(
                    wordOrTitle = "Cost an arm and a leg",
                    meaning = "To be extremely expensive or costly.",
                    example = "High-quality open-source mock tests ensure preparation does not cost an arm and a leg."
                )
            )
        ),
        // Set 4 (10 Items)
        SetDefinition(
            setNumber = 4,
            title = "Set 4: Figurative & Contextual Expressions",
            items = listOf(
                ItemDefinition(
                    wordOrTitle = "Every cloud has a silver lining",
                    meaning = "Every bad or difficult situation has some positive or hopeful aspect.",
                    example = "Failing the exam by two marks was heartbreaking, but every cloud has a silver lining as it highlighted calculation accuracy."
                ),
                ItemDefinition(
                    wordOrTitle = "Fit as a fiddle",
                    meaning = "In very good physical health and condition.",
                    example = "Physical endurance tests require candidates to remain fit as a fiddle."
                ),
                ItemDefinition(
                    wordOrTitle = "Go the extra mile",
                    meaning = "To make more effort than is expected of you.",
                    example = "Aspirants who go the extra mile by practicing speed drills routinely score in the 99th percentile."
                ),
                ItemDefinition(
                    wordOrTitle = "In the heat of the moment",
                    meaning = "Overwhelmed by what is happening at the moment without thinking carefully.",
                    example = "In the heat of the moment during the final minutes, do not make irrational guesses."
                ),
                ItemDefinition(
                    wordOrTitle = "Keep fingers crossed",
                    meaning = "To hope strongly for good luck or a favorable outcome.",
                    example = "After giving his best in Tier 2, he kept his fingers crossed for the final merit list."
                ),
                ItemDefinition(
                    wordOrTitle = "Kill two birds with one stone",
                    meaning = "To achieve two different objectives with a single action.",
                    example = "Reading daily newspaper editorials improves both vocabulary and current affairs awareness."
                ),
                ItemDefinition(
                    wordOrTitle = "Let the cat out of the bag",
                    meaning = "To disclose a secret, often unintentionally.",
                    example = "The instructor accidentally let the cat out of the bag regarding upcoming surprise mock drills."
                ),
                ItemDefinition(
                    wordOrTitle = "Make a long story short",
                    meaning = "To state the outcome without unnecessary explanatory details.",
                    example = "To make a long story short, strict daily discipline is the secret to competitive success."
                ),
                ItemDefinition(
                    wordOrTitle = "Off the hook",
                    meaning = "Freed from an obligation, blame, or difficult situation.",
                    example = "Once the verification committee validated his documents, he was off the hook."
                ),
                ItemDefinition(
                    wordOrTitle = "Put all eggs in one basket",
                    meaning = "To risk everything on a single venture or possibility.",
                    example = "Never put all eggs in one basket; apply for complementary staff selection and banking examinations."
                )
            )
        ),
        // Set 5 (10 Items)
        SetDefinition(
            setNumber = 5,
            title = "Set 5: Tier II High-Yield Idioms",
            items = listOf(
                ItemDefinition(
                    wordOrTitle = "Read between the lines",
                    meaning = "To understand the hidden meaning or real message behind stated words.",
                    example = "Reading between the lines in comprehension passages helps solve inference-based questions."
                ),
                ItemDefinition(
                    wordOrTitle = "Steal someone's thunder",
                    meaning = "To take credit for someone else's achievement or divert attention to oneself.",
                    example = "Presenting original study notes ahead of time prevented rivals from stealing his thunder."
                ),
                ItemDefinition(
                    wordOrTitle = "Take the bull by the horns",
                    meaning = "To confront a problem directly and boldly without hesitation.",
                    example = "He took the bull by the horns and solved 50 trigonometry problems every morning."
                ),
                ItemDefinition(
                    wordOrTitle = "Taste of one's own medicine",
                    meaning = "To experience the same unpleasant treatment that one has given to others.",
                    example = "The opponent received a taste of his own medicine when his delay tactics were used against him."
                ),
                ItemDefinition(
                    wordOrTitle = "The last straw",
                    meaning = "The final problem in a series that makes a situation intolerable.",
                    example = "Losing negative marks due to careless bubbles was the last straw that pushed him to switch to computer-based mock tests."
                ),
                ItemDefinition(
                    wordOrTitle = "Throw in the towel",
                    meaning = "To surrender, give up, or admit defeat.",
                    example = "Despite facing multiple setbacks, the determined aspirant refused to throw in the towel."
                ),
                ItemDefinition(
                    wordOrTitle = "Turn a blind eye",
                    meaning = "To pretend not to notice something wrong or undesirable.",
                    example = "The invigilator warned candidates that he would not turn a blind eye to rule violations."
                ),
                ItemDefinition(
                    wordOrTitle = "Up in the air",
                    meaning = "Uncertain, undecided, or not yet settled.",
                    example = "The official exam center schedule remained up in the air until the admit cards were released."
                ),
                ItemDefinition(
                    wordOrTitle = "Wild goose chase",
                    meaning = "A foolish, hopeless, or time-wasting search for something unattainable.",
                    example = "Looking for short tricks without understanding basic algebraic principles is a wild goose chase."
                ),
                ItemDefinition(
                    wordOrTitle = "Zero in on",
                    meaning = "To direct all attention and effort towards a specific target.",
                    example = "Use the last two weeks to zero in on high-yield geometry formulas and grammar error spots."
                )
            )
        )
    )

    // ================================================
    // CURRENT AFFAIRS: 5 Sets x 10 Items = 50 Items
    // ================================================
    val currentAffairsSets: List<SetDefinition> = listOf(
        // Set 1 (10 Items)
        SetDefinition(
            setNumber = 1,
            title = "Set 1: National Policy & Scientific Milestones",
            items = listOf(
                ItemDefinition(
                    wordOrTitle = "Cabinet Approves Unified Pension Scheme (UPS)",
                    category = "National & Governance",
                    factDate = "National Policy",
                    meaning = "Assured pension of 50% average basic pay for central government employees.",
                    example = "The Union Cabinet approved UPS ensuring assured pension of 50% basic pay for employees with minimum 25 years of service."
                ),
                ItemDefinition(
                    wordOrTitle = "ISRO Successfully Launches EOS-08 Earth Observation Satellite",
                    category = "Science & Technology",
                    factDate = "Space & Technology",
                    meaning = "SSLV-D3 launch carries payloads for disaster and thermal infrared monitoring.",
                    example = "EOS-08 was launched aboard the SSLV-D3 vehicle from Sriharikota, carrying advanced electro-optical sensors."
                ),
                ItemDefinition(
                    wordOrTitle = "India Wins Historic Double Gold at 45th FIDE Chess Olympiad",
                    category = "Sports & Honors",
                    factDate = "International Sports",
                    meaning = "Both Men's and Women's Indian teams clinch gold medals in Budapest.",
                    example = "D. Gukesh, Arjun Erigaisi, and Divya Deshmukh led India to an unprecedented historic double championship in Budapest."
                ),
                ItemDefinition(
                    wordOrTitle = "RBI Monetary Policy Committee Maintains Repo Rate at 6.5%",
                    category = "Economy & Banking",
                    factDate = "Banking & Finance",
                    meaning = "Benchmark policy repo rate held steady to balance retail inflation and GDP growth.",
                    example = "The RBI Monetary Policy Committee voted to keep the policy repo rate at 6.5% to anchor retail CPI inflation expectations."
                ),
                ItemDefinition(
                    wordOrTitle = "Global Biofuels Alliance Expands Technical Framework",
                    category = "Environment & Ecology",
                    factDate = "Global Initiatives",
                    meaning = "India partners with global stakeholders to accelerate sustainable biofuel standards.",
                    example = "Under India's leadership, the alliance introduced uniform certifications to scale ethanol blending in aviation and transport."
                ),
                ItemDefinition(
                    wordOrTitle = "INS Arighaat Commissioned into Indian Navy at Visakhapatnam",
                    category = "Defense & Security",
                    factDate = "Defense & Navy",
                    meaning = "Second indigenous nuclear-powered ballistic missile submarine strengthens deterrence.",
                    example = "Equipped with advanced K-15 SLBM missiles, INS Arighaat enhances India's continuous sea-based nuclear triad capability."
                ),
                ItemDefinition(
                    wordOrTitle = "India Adds Three More Wetlands to Global Ramsar List",
                    category = "Environment & Ecology",
                    factDate = "Biodiversity",
                    meaning = "Nanjarayan, Kazhuveli, and Tawa Reservoir take India's Ramsar tally to 85.",
                    example = "The Ministry of Environment confirmed the inclusion of three protected wetland sanctuaries, boosting international conservation status."
                ),
                ItemDefinition(
                    wordOrTitle = "National Teachers' Awards Bestowed on 50 Exceptional Educators",
                    category = "Awards & Appointments",
                    factDate = "National Honors",
                    meaning = "Conferred by the President of India at Vigyan Bhawan on Teachers' Day.",
                    example = "The awards recognize innovative pedagogical practices and community leadership across schools and vocational institutions."
                ),
                ItemDefinition(
                    wordOrTitle = "UIDAI Introduces AI-Based Face Authentication for Pensioners",
                    category = "Digital Governance",
                    factDate = "GovTech",
                    meaning = "Biometric facial verification launched to streamline Digital Life Certificate issuance.",
                    example = "Jeevan Pramaan face authentication allows senior citizens to submit life certificates securely from smartphones."
                ),
                ItemDefinition(
                    wordOrTitle = "India & Singapore Sign Semiconductor Ecosystem Partnership",
                    category = "International Affairs",
                    factDate = "Bilateral Pacts",
                    meaning = "MoU signed to foster supply chain resilience, R&D, and skill training.",
                    example = "The bilateral agreement strengthens bilateral talent exchange and semiconductor fab infrastructure development."
                )
            )
        ),
        // Set 2 (10 Items)
        SetDefinition(
            setNumber = 2,
            title = "Set 2: Economic Indicators & Global Summits",
            items = listOf(
                ItemDefinition(
                    wordOrTitle = "GST Collections Touch Landmark ₹1.74 Lakh Crore",
                    category = "Economy & Banking",
                    factDate = "Fiscal Updates",
                    meaning = "Strong domestic transactions and compliance drive double-digit year-on-year growth.",
                    example = "Robust economic activity and increased compliance propelled gross Goods and Services Tax revenue to ₹1.74 lakh crore."
                ),
                ItemDefinition(
                    wordOrTitle = "G20 Summit Declaration Endorses Inclusive AI Governance",
                    category = "International Affairs",
                    factDate = "Global Summits",
                    meaning = "Global leaders commit to responsible AI development and digital public infrastructure.",
                    example = "The summit communique emphasized bridging the digital divide using open DPI frameworks pioneered by India."
                ),
                ItemDefinition(
                    wordOrTitle = "DRDO Successfully Flight-Tests Long-Range Glide Bomb 'Gaurav'",
                    category = "Defense & Security",
                    factDate = "Defense R&D",
                    meaning = "Indigenous 1,000 kg class glide bomb tested from Sukhoi-30 MKI fighter jet.",
                    example = "Gaurav uses indigenous satellite navigation guidance to strike pinpoint ground targets at standoff ranges."
                ),
                ItemDefinition(
                    wordOrTitle = "Ayushman Bharat Scheme Expanded to All Senior Citizens Aged 70+",
                    category = "National & Governance",
                    factDate = "Social Welfare",
                    meaning = "Universal health coverage of ₹5 lakh per year regardless of household income.",
                    example = "The Union Cabinet approved universal AB-PMJAY coverage offering free health insurance to every citizen aged 70 years and above."
                ),
                ItemDefinition(
                    wordOrTitle = "National Sports Day Celebrated Honoring Major Dhyan Chand",
                    category = "Sports & Honors",
                    factDate = "Commemorations",
                    meaning = "Observed on August 29 with nationwide 'Fit India' fitness challenges.",
                    example = "Athletes across India participated in community sporting events commemorating the hockey wizard Major Dhyan Chand."
                ),
                ItemDefinition(
                    wordOrTitle = "India's Foreign Exchange Reserves Cross Historic $700 Billion Mark",
                    category = "Economy & Banking",
                    factDate = "Reserve Bank",
                    meaning = "Highest-ever foreign currency assets and gold reserves bolster financial stability.",
                    example = "Sustained foreign portfolio inflows and strong current account fundamentals pushed India's forex reserves beyond $700 billion."
                ),
                ItemDefinition(
                    wordOrTitle = "Supreme Court Rules Sub-Classification of SCs and STs Permissible",
                    category = "Judiciary & Law",
                    factDate = "Constitutional Law",
                    meaning = "7-judge Constitution bench upholds state powers for equitable quota distribution.",
                    example = "The landmark 6:1 majority verdict affirmed that states can create sub-categories within reserved quotas to uplift more backward groups."
                ),
                ItemDefinition(
                    wordOrTitle = "PM Launches 'PM Surya Ghar: Muft Bijli Yojana'",
                    category = "Renewable Energy",
                    factDate = "Energy Schemes",
                    meaning = "Subsidies for rooftop solar panels to provide up to 300 units of free electricity.",
                    example = "With an investment of ₹75,000 crore, the scheme aims to light up 1 crore households via decentralized solar power."
                ),
                ItemDefinition(
                    wordOrTitle = "Cochin Shipyard Delivers India's First Hydrogen Fuel Cell Ferry",
                    category = "Science & Technology",
                    factDate = "Green Mobility",
                    meaning = "Zero-emission hydrogen passenger vessel built indigenously under Harit Nauka initiative.",
                    example = "The hydrogen vessel is designed for inland waterways in Varanasi to reduce carbon emissions and acoustic noise."
                ),
                ItemDefinition(
                    wordOrTitle = "WHO Certifies India for Elimination of Trachoma as Public Health Problem",
                    category = "Health & Sanitation",
                    factDate = "Public Health",
                    meaning = "Milestone achievement in preventing infectious blindness nationwide.",
                    example = "Sustained hygiene interventions, antibiotic coverage, and surgical camps led to WHO validation of trachoma elimination."
                )
            )
        ),
        // Set 3 (10 Items)
        SetDefinition(
            setNumber = 3,
            title = "Set 3: Defense Exercises & Environmental Pacts",
            items = listOf(
                ItemDefinition(
                    wordOrTitle = "Indian Air Force Hosts Mega Multinational Exercise 'Tarang Shakti'",
                    category = "Defense & Security",
                    factDate = "Military Drills",
                    meaning = "Over 30 countries participate in India's largest multilateral air combat drill in Jodhpur.",
                    example = "Tarang Shakti demonstrated high-level interoperability with fighter jets from Germany, France, Spain, and the UK."
                ),
                ItemDefinition(
                    wordOrTitle = "India Becomes 4th Nation to Achieve Lunar South Pole Landing",
                    category = "Science & Technology",
                    factDate = "Space Exploration",
                    meaning = "National Space Day marked on August 23 to celebrate Chandrayaan-3 triumph.",
                    example = "Pragyan rover data revealed presence of sulfur, iron, and titanium minerals across the south polar lunar regolith."
                ),
                ItemDefinition(
                    wordOrTitle = "National Highway Network Crosses 1,46,000 Kilometers",
                    category = "Infrastructure",
                    factDate = "Road Transport",
                    meaning = "Bharatmala Pariyojana and access-controlled greenfield expressways expand connectivity.",
                    example = "Expressways like Delhi-Mumbai and Bengaluru-Chennai reduce logistics turnaround times and freight transport costs."
                ),
                ItemDefinition(
                    wordOrTitle = "Dada Saheb Phalke Award Conferred on Veteran Actor Mithun Chakraborty",
                    category = "Awards & Honors",
                    factDate = "Cinema & Arts",
                    meaning = "India's highest cinema honor presented at the 70th National Film Awards.",
                    example = "The award recognized five decades of cinematic excellence and iconic contributions across Hindi and Bengali cinema."
                ),
                ItemDefinition(
                    wordOrTitle = "India Signs Free Trade Agreement with EFTA Nations",
                    category = "International Trade",
                    factDate = "Trade Agreements",
                    meaning = "$100 billion investment commitment from Switzerland, Norway, Iceland, and Liechtenstein.",
                    example = "The Trade and Economic Partnership Agreement (TEPA) eliminates customs tariffs on engineering, pharma, and gems."
                ),
                ItemDefinition(
                    wordOrTitle = "Project Cheetah Welcomes New Cubs in Kuno National Park",
                    category = "Environment & Ecology",
                    factDate = "Wildlife Ecology",
                    meaning = "Successful captive breeding marks progress in world's first intercontinental carnivore translocation.",
                    example = "The birth of healthy cubs confirms adaptation of translocated Namibian and South African cheetahs to Indian habitats."
                ),
                ItemDefinition(
                    wordOrTitle = "NITI Aayog Releases State Energy and Climate Index (SECI)",
                    category = "Governance & Reports",
                    factDate = "Policy Index",
                    meaning = "Gujarat, Kerala, and Punjab emerge as top-performing states in clean energy transition.",
                    example = "SECI evaluates discom performance, energy efficiency, clean energy initiatives, and environmental sustainability."
                ),
                ItemDefinition(
                    wordOrTitle = "India Unveils First Indigenous Car T-Cell Therapy 'NexCAR19'",
                    category = "Healthcare & Biotech",
                    factDate = "Medical Innovation",
                    meaning = "Affordable personalized gene therapy for refractory B-cell lymphomas approved by CDSCO.",
                    example = "Developed jointly by IIT Bombay and Tata Memorial Centre, NexCAR19 drastically reduces cancer treatment costs."
                ),
                ItemDefinition(
                    wordOrTitle = "World Bank Raises India's FY25 GDP Growth Forecast to 7.0%",
                    category = "Economy & Growth",
                    factDate = "Global Reports",
                    meaning = "Resilient urban consumption and government capital expenditure drive economic expansion.",
                    example = "The World Bank noted that public investment in roads, railways, and ports continues to crowd in private capital."
                ),
                ItemDefinition(
                    wordOrTitle = "Indian Coast Guard Conducts Coastal Security Drill 'Sagar Kavach'",
                    category = "Maritime Security",
                    factDate = "Coastal Defense",
                    meaning = "Multi-agency coordination drill evaluates coastal surveillance and port security.",
                    example = "State marine police, customs, and naval reconnaissance aircraft participated along the western coastline."
                )
            )
        ),
        // Set 4 (10 Items)
        SetDefinition(
            setNumber = 4,
            title = "Set 4: Appointments, Awards & Regional Summits",
            items = listOf(
                ItemDefinition(
                    wordOrTitle = "Justice Sanjiv Khanna Appointed as the 51st Chief Justice of India",
                    category = "Appointments & Judiciary",
                    factDate = "Judiciary",
                    meaning = "Succeeds CJI D.Y. Chandrachud following official presidential notification.",
                    example = "Justice Khanna took oath as the 51st CJI, bringing distinguished judicial experience from the Delhi High Court and Supreme Court."
                ),
                ItemDefinition(
                    wordOrTitle = "BIMSTEC Summit Strengthens Maritime Connectivity in Bay of Bengal",
                    category = "Regional Summits",
                    factDate = "Regional Diplomacy",
                    meaning = "Member states approve Master Plan for Transport Connectivity across South & SE Asia.",
                    example = "The agreement enhances deep-sea port interoperability, inland waterways, and cross-border energy grids."
                ),
                ItemDefinition(
                    wordOrTitle = "World Athletics U20 Championships: Pooja Clinches Silver in High Jump",
                    category = "Sports & Honors",
                    factDate = "Athletics",
                    meaning = "Remarkable 1.85m leap in Lima, Peru secures India's medal in field events.",
                    example = "Pooja's medal marks another milestone in India's emerging track and field excellence on the global stage."
                ),
                ItemDefinition(
                    wordOrTitle = "Government Launches 'Agrisure' Fund to Boost Agri-Tech Startups",
                    category = "Agriculture & Rural",
                    factDate = "Agri-Finance",
                    meaning = "₹750 crore blended capital fund launched by NABARD to foster rural farm innovations.",
                    example = "Agrisure targets early-stage farm mechanization, precision AI farming, and rural food processing supply chains."
                ),
                ItemDefinition(
                    wordOrTitle = "India Signs Protocol Amending DTAA with Mauritius",
                    category = "Economy & Taxation",
                    factDate = "Tax Treaties",
                    meaning = "Incorporates Principal Purpose Test (PPT) to curb tax avoidance and round-tripping.",
                    example = "The amended Double Tax Avoidance Agreement ensures treaty benefits apply only to genuine commercial investments."
                ),
                ItemDefinition(
                    wordOrTitle = "ISRO & NASA Finalize NISAR Synthetic Aperture Radar Satellite Tests",
                    category = "Science & Technology",
                    factDate = "Space Cooperation",
                    meaning = "Dual-frequency L-band and S-band radar satellite to map earth ecosystems every 12 days.",
                    example = "NISAR will provide unprecedented measurements of ice sheet collapse, tectonic faults, and forest biomass changes."
                ),
                ItemDefinition(
                    wordOrTitle = "Kavach Automatic Train Protection System Deployed on Key Corridors",
                    category = "Railways & Transport",
                    factDate = "Rail Safety",
                    meaning = "Version 4.0 approved by RDSO for dense traffic routes between Delhi and Howrah/Mumbai.",
                    example = "Kavach automatically applies brakes to prevent Signal Passed at Danger (SPAD) and head-on collisions."
                ),
                ItemDefinition(
                    wordOrTitle = "India Tops Global Crypto Adoption Index by Chainalysis",
                    category = "Digital Finance",
                    factDate = "Fintech Index",
                    meaning = "Ranked #1 globally in grassroot crypto and DeFi usage volume for the second year.",
                    example = "High retail engagement in decentralized finance protocols and remittance transfers placed India at the top."
                ),
                ItemDefinition(
                    wordOrTitle = "Pritzker Architecture Prize Awarded for Sustainable Urban Living",
                    category = "Global Awards",
                    factDate = "International Honors",
                    meaning = "Celebrates ecological materials, natural ventilation, and human-centric urban design.",
                    example = "The jury commended pioneering designs that harmonize climate resilience with social housing."
                ),
                ItemDefinition(
                    wordOrTitle = "India Hosts International Solar Alliance (ISA) General Assembly",
                    category = "Renewable Energy",
                    factDate = "Global Climate",
                    meaning = "120+ member nations discuss scaling solar mini-grids across African and Pacific island nations.",
                    example = "The assembly approved technical viability grants for distributed solar pumping and rooftop installations."
                )
            )
        ),
        // Set 5 (10 Items)
        SetDefinition(
            setNumber = 5,
            title = "Set 5: Geopolitics, Heritage & Science Innovations",
            items = listOf(
                ItemDefinition(
                    wordOrTitle = "BRICS Summit Expands with Induction of New Full Member Nations",
                    category = "Geopolitics",
                    factDate = "Multilateral Pacts",
                    meaning = "Kazan summit focuses on local currency settlements and New Development Bank projects.",
                    example = "Leaders emphasized reforming global multilateral financial architectures to reflect emerging market economies."
                ),
                ItemDefinition(
                    wordOrTitle = "Moidams of Charaideo Inscribed as India's 43rd UNESCO World Heritage Site",
                    category = "Culture & Heritage",
                    factDate = "Heritage Status",
                    meaning = "Royal burial mounds of the Ahom Dynasty in Assam gain international protection.",
                    example = "The Moidams represent 600 years of unique earthen pyramid burial architecture of the medieval Ahom kingdom."
                ),
                ItemDefinition(
                    wordOrTitle = "India Successfully Test-Fires Agni-4 Intermediate Range Ballistic Missile",
                    category = "Defense & Security",
                    factDate = "Strategic Defense",
                    meaning = "4,000-km range nuclear-capable missile test validated by Strategic Forces Command at Chandipur.",
                    example = "The launch successfully validated all operational parameters, terminal accuracy, and re-entry heat shield performance."
                ),
                ItemDefinition(
                    wordOrTitle = "Cabinet Approves 'BioE3' Policy to Propel Biomanufacturing",
                    category = "Science & Economy",
                    factDate = "Biotechnology",
                    meaning = "High-performance biomanufacturing framework for bio-plastics, bio-enzymes, and smart proteins.",
                    example = "BioE3 stands for Biotechnology for Economy, Environment, and Employment, aiming for a $300 billion bioeconomy by 2030."
                ),
                ItemDefinition(
                    wordOrTitle = "India Ranks 39th in Global Innovation Index (GII) by WIPO",
                    category = "Reports & Indices",
                    factDate = "WIPO Ranking",
                    meaning = "Continues steady rise among lower-middle-income economies in patent filings and ICT exports.",
                    example = "India maintained its leadership in Central and Southern Asia in knowledge output and startup density."
                ),
                ItemDefinition(
                    wordOrTitle = "Indian Women's Table Tennis Team Wins Historic Asian Championship Bronze",
                    category = "Sports & Honors",
                    factDate = "Table Tennis",
                    meaning = "Manika Batra and Ayhika Mukherjee lead squad to India's first-ever women's team Asian medal.",
                    example = "The Indian team defeated higher-seeded opponents in Astana, Kazakhstan, securing a historic podium finish."
                ),
                ItemDefinition(
                    wordOrTitle = "National Green Hydrogen Mission Sanctions Pilot Green Steel Plants",
                    category = "Energy & Industry",
                    factDate = "Clean Energy",
                    meaning = "Direct Reduced Iron (DRI) plants powered by green hydrogen to decarbonize primary steel production.",
                    example = "The Ministry of Steel allocated grants to consortiums to replace coking coal with hydrogen in blast furnaces."
                ),
                ItemDefinition(
                    wordOrTitle = "Pradhan Mantri Jan Dhan Yojana Completes 10 Glorious Years",
                    category = "Financial Inclusion",
                    factDate = "Milestones",
                    meaning = "Over 53 crore bank accounts opened with total deposit balances exceeding ₹2.3 lakh crore.",
                    example = "PMJDY serves as the foundation for Direct Benefit Transfer (DBT) and digital micro-insurance access across India."
                ),
                ItemDefinition(
                    wordOrTitle = "Ministry of Education Launches 'APAAR' ID for One Nation One Student",
                    category = "Education & Tech",
                    factDate = "Digital ID",
                    meaning = "Automated Permanent Academic Account Registry links academic credits to DigiLocker.",
                    example = "APAAR ID enables seamless transfer of credits and digital verification of degree certificates across universities."
                ),
                ItemDefinition(
                    wordOrTitle = "India Signs Critical Minerals Partnership Agreement with Australia",
                    category = "International Trade",
                    factDate = "Strategic Minerals",
                    meaning = "Secures lithium and cobalt supply chains for electric mobility and high-tech manufacturing.",
                    example = "The joint venture commits funds to explore and develop critical mineral processing hubs across Western Australia and India."
                )
            )
        )
    )
}
