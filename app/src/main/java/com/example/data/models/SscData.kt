package com.example.data.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Public
import androidx.compose.ui.graphics.vector.ImageVector

data class Subtopic(
    val id: String,
    val title: String,
    val description: String
)

data class Topic(
    val id: String,
    val title: String,
    val description: String,
    val subtopics: List<Subtopic>
)

data class Subject(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val testTag: String,
    val topics: List<Topic>
)

object SscDataRepository {
    val subjects: List<Subject> = listOf(
        Subject(
            id = "reasoning",
            title = "Reasoning",
            description = "General Intelligence & Logical Reasoning",
            icon = Icons.Default.Psychology,
            testTag = "subject_card_reasoning",
            topics = listOf(
                Topic(
                    id = "analogies",
                    title = "Analogies & Classification",
                    description = "Semantic, symbolic, and figural classification patterns",
                    subtopics = listOf(
                        Subtopic("sub_sem_ana", "Semantic Analogy", "Word and concept relationship pairings"),
                        Subtopic("sub_sym_ana", "Symbolic / Number Analogy", "Numerical relations, squares, and cubes"),
                        Subtopic("sub_fig_ana", "Figural Analogy", "Pattern transformation and rotation rules"),
                        Subtopic("sub_odd_one", "Odd One Out / Classification", "Identifying non-matching elements in sets")
                    )
                ),
                Topic(
                    id = "coding_decoding",
                    title = "Coding & Decoding",
                    description = "Letter shifting, number substitution, and deciphering",
                    subtopics = listOf(
                        Subtopic("sub_letter_code", "Letter Coding", "Forward & backward alphabet shifting"),
                        Subtopic("sub_number_code", "Number & Symbol Coding", "Letter position values and mathematical operations"),
                        Subtopic("sub_msg_decode", "Deciphering Message Codes", "Common word elimination in encoded sentences"),
                        Subtopic("sub_substitution", "Substitution Coding", "Direct name mapping and conditional shifts")
                    )
                ),
                Topic(
                    id = "series",
                    title = "Series Completion",
                    description = "Number progressions, letter patterns, and missing terms",
                    subtopics = listOf(
                        Subtopic("sub_num_series", "Number Series", "Arithmetic, geometric, and alternate difference sequences"),
                        Subtopic("sub_alpha_series", "Alphabet Series", "Skip patterns and cyclical position logic"),
                        Subtopic("sub_mixed_series", "Continuous Pattern Series", "Repeating letter and symbol cluster blanks"),
                        Subtopic("sub_missing_term", "Missing Number in Matrix", "Grid and circle number logic puzzles")
                    )
                ),
                Topic(
                    id = "blood_relations",
                    title = "Blood Relations",
                    description = "Family trees, relationship puzzles, and coded linkages",
                    subtopics = listOf(
                        Subtopic("sub_direct_rel", "Direct Statement Relations", "Pointing to photograph / person statements"),
                        Subtopic("sub_family_tree", "Family Tree Generation", "Generational hierarchy and gender deductions"),
                        Subtopic("sub_coded_rel", "Coded Blood Relations", "Symbolic definitions like A + B, A x B")
                    )
                ),
                Topic(
                    id = "direction_distance",
                    title = "Direction & Distance",
                    description = "Cardinals, shortest distance, turns, and shadow angles",
                    subtopics = listOf(
                        Subtopic("sub_cardinal_turns", "Turns & Cardinal Bearings", "Left/Right turns and final direction faced"),
                        Subtopic("sub_pythagoras", "Shortest Distance (Pythagoras)", "Displacement calculation from start point"),
                        Subtopic("sub_shadows", "Sunrise & Sunset Shadows", "Shadow positioning at morning and evening")
                    )
                ),
                Topic(
                    id = "syllogisms",
                    title = "Syllogism & Deductions",
                    description = "Statements, Venn conclusions, and possibility logic",
                    subtopics = listOf(
                        Subtopic("sub_venn_basics", "Standard Syllogisms", "All / Some / No statement conclusions"),
                        Subtopic("sub_possibility", "Possibility & 'Only A Few'", "Modern conditional syllogism formats"),
                        Subtopic("sub_either_or", "Either-Or Complementary Pairs", "Contradictory condition pairings")
                    )
                ),
                Topic(
                    id = "order_ranking",
                    title = "Order & Ranking",
                    description = "Linear position calculations and interchange puzzles",
                    subtopics = listOf(
                        Subtopic("sub_linear_rank", "Total Count from Ends", "Left + Right - 1 positional formulas"),
                        Subtopic("sub_interchange", "Position Interchange", "Calculating shift and total strength"),
                        Subtopic("sub_comparison_rank", "Height & Weight Comparisons", "Sequential order deductions")
                    )
                ),
                Topic(
                    id = "non_verbal",
                    title = "Non-Verbal Reasoning",
                    description = "Paper folding, mirror images, embedded figures, and dice",
                    subtopics = listOf(
                        Subtopic("sub_mirror_water", "Mirror & Water Images", "Reflection axis and lateral inversions"),
                        Subtopic("sub_paper_cut", "Paper Folding & Cutting", "Fold progression and unfolding punch holes"),
                        Subtopic("sub_embedded_fig", "Embedded & Hidden Figures", "Tracing hidden patterns in complex grids"),
                        Subtopic("sub_dice_cube", "Dice & Cube Faces", "Opposite face deductions in standard/open dice")
                    )
                )
            )
        ),
        Subject(
            id = "maths",
            title = "Maths",
            description = "Quantitative Aptitude & Numerical Ability",
            icon = Icons.Default.Calculate,
            testTag = "subject_card_maths",
            topics = listOf(
                Topic(
                    id = "number_system",
                    title = "Number System",
                    description = "Divisibility, remainders, unit digit, and LCM/HCF",
                    subtopics = listOf(
                        Subtopic("sub_divisibility", "Divisibility Rules", "Rules for 7, 11, 13, 72, 88, and composite numbers"),
                        Subtopic("sub_unit_digit", "Unit Digit & Cyclicity", "Power cycle rules and last digit calculation"),
                        Subtopic("sub_remainders", "Remainder Theorems", "Fermat's, Euler's, and Chinese Remainder basics"),
                        Subtopic("sub_lcm_hcf", "LCM & HCF", "Factorization, fractional LCM/HCF, and word problems"),
                        Subtopic("sub_surds_indices", "Surds & Indices", "Radical simplification and power comparisons")
                    )
                ),
                Topic(
                    id = "percentage_profit",
                    title = "Percentage & Profit/Loss",
                    description = "Successive changes, discount, marked price, and faulty weights",
                    subtopics = listOf(
                        Subtopic("sub_perc_fraction", "Fraction to Percentage", "Mental math conversion tables and applications"),
                        Subtopic("sub_succ_percent", "Successive Percentage", "Net change formula: a + b + ab/100"),
                        Subtopic("sub_profit_loss", "Cost Price, Selling Price & Profit", "Profit% and Loss% on CP vs SP"),
                        Subtopic("sub_marked_discount", "Marked Price & Discounts", "Equivalent single discount and cash rebates"),
                        Subtopic("sub_dishonest_dealer", "Dishonest Dealer & Faulty Weights", "Cheating in buying vs selling percentages")
                    )
                ),
                Topic(
                    id = "ratio_proportion",
                    title = "Ratio, Proportion & Mixture",
                    description = "Compound ratios, partnership, alligation, and ages",
                    subtopics = listOf(
                        Subtopic("sub_ratio_basics", "Ratio & Continued Proportions", "Mean, third, and fourth proportional"),
                        Subtopic("sub_alligation", "Rule of Alligation & Mixtures", "Weighted averages and liquid replacement formulas"),
                        Subtopic("sub_partnership", "Partnership & Profit Sharing", "Capital x Time period distribution"),
                        Subtopic("sub_age_problems", "Problems on Ages", "Past, present, and future age ratio shifts")
                    )
                ),
                Topic(
                    id = "interest",
                    title = "Simple & Compound Interest",
                    description = "Annual/half-yearly compounding and SI vs CI difference",
                    subtopics = listOf(
                        Subtopic("sub_si_core", "Simple Interest (SI)", "P x R x T / 100, varying rate periods"),
                        Subtopic("sub_ci_core", "Compound Interest (CI)", "Effective rate method, annual vs half-yearly"),
                        Subtopic("sub_si_ci_diff", "Difference Between CI & SI", "2-year and 3-year standard shortcut formulas"),
                        Subtopic("sub_installments", "Loan Installments", "Equal annual payments under SI and CI")
                    )
                ),
                Topic(
                    id = "time_work",
                    title = "Time & Work, Pipes & Cisterns",
                    description = "Efficiency method, alternate days, and inlet/outlet pipes",
                    subtopics = listOf(
                        Subtopic("sub_work_efficiency", "Efficiency & Total Work (LCM)", "Work = Efficiency x Time calculations"),
                        Subtopic("sub_leaving_joining", "Leaving & Joining Mid-Way", "Adjusting remaining work among workers"),
                        Subtopic("sub_pipes_cisterns", "Pipes & Cisterns", "Positive inlet and negative leak efficiencies"),
                        Subtopic("sub_wages_dist", "Wages Distribution", "Dividing earnings proportionally by work done")
                    )
                ),
                Topic(
                    id = "speed_distance",
                    title = "Speed, Time & Distance",
                    description = "Relative speed, trains, platforms, and boats & streams",
                    subtopics = listOf(
                        Subtopic("sub_speed_basics", "Average Speed & Unit Conversion", "Harmonic mean 2xy/(x+y) and km/h to m/s"),
                        Subtopic("sub_trains", "Trains & Moving Objects", "Crossing poles, platforms, and opposite/same trains"),
                        Subtopic("sub_boats_streams", "Boats & Streams", "Upstream (u - v) and Downstream (u + v) speed logic"),
                        Subtopic("sub_linear_races", "Linear & Circular Races", "Head-starts, dead heats, and meeting points")
                    )
                ),
                Topic(
                    id = "algebra",
                    title = "Algebra & Polynomials",
                    description = "Identities, x + 1/x substitutions, and quadratic roots",
                    subtopics = listOf(
                        Subtopic("sub_alg_identities", "Standard Algebraic Identities", "(a+b)^2, (a+b+c)^2, and a^3+b^3+c^3 - 3abc"),
                        Subtopic("sub_reciprocal_x", "Reciprocal Powers (x + 1/x)", "Formulae for x^2 + 1/x^2 up to x^7 + 1/x^7"),
                        Subtopic("sub_quadratics", "Quadratic Equations", "Sum & product of roots, discriminant nature")
                    )
                ),
                Topic(
                    id = "geometry",
                    title = "Geometry & Mensuration",
                    description = "Triangles, circles, chords, tangents, and 2D/3D shapes",
                    subtopics = listOf(
                        Subtopic("sub_triangles_centers", "Triangle Centers & Similarity", "Incenter, circumcenter, orthocenter, and centroid"),
                        Subtopic("sub_circles_tangents", "Circles, Chords & Tangents", "Tangent-secant theorem and cyclic quadrilaterals"),
                        Subtopic("sub_mensuration_2d", "2D Mensuration", "Areas and perimeters of polygons and circles"),
                        Subtopic("sub_mensuration_3d", "3D Mensuration", "Cylinders, cones, spheres, prisms, and pyramids")
                    )
                ),
                Topic(
                    id = "trigonometry",
                    title = "Trigonometry & Heights",
                    description = "Ratios, identities, complementary angles, and elevations",
                    subtopics = listOf(
                        Subtopic("sub_trig_ratios", "Trigonometric Ratios & Table", "Values of standard angles 0, 30, 45, 60, 90"),
                        Subtopic("sub_trig_identities", "Fundamental Identities", "sin^2 + cos^2 = 1, sec^2 - tan^2 = 1 transformations"),
                        Subtopic("sub_heights_dist", "Heights & Distances", "Angle of elevation and depression with 30-60-90 ratios")
                    )
                )
            )
        ),
        Subject(
            id = "english",
            title = "English",
            description = "Grammar, Vocabulary & Comprehension",
            icon = Icons.Default.MenuBook,
            testTag = "subject_card_english",
            topics = listOf(
                Topic(
                    id = "spotting_errors",
                    title = "Spotting Errors",
                    description = "Subject-verb agreement, tenses, prepositions, and modifiers",
                    subtopics = listOf(
                        Subtopic("sub_sva", "Subject-Verb Agreement", "Singular/plural subjects, 'neither-nor', and collective nouns"),
                        Subtopic("sub_tenses_cond", "Tenses & Conditionals", "Past perfect, present continuous, and 'if' clauses"),
                        Subtopic("sub_prepositions", "Fixed Prepositions & Articles", "Prepositional combinations and definite article rules"),
                        Subtopic("sub_pronoun_mod", "Pronoun Cases & Modifiers", "Dangling modifiers and relative pronoun agreement")
                    )
                ),
                Topic(
                    id = "sentence_improvement",
                    title = "Sentence Improvement",
                    description = "Phrase replacements and grammatical flow refinements",
                    subtopics = listOf(
                        Subtopic("sub_phrase_rep", "Phrase Replacement", "Selecting grammatically accurate alternative phrases"),
                        Subtopic("sub_parallelism", "Parallelism & Conjunctions", "Balanced structures with paired correlatives"),
                        Subtopic("sub_superfluous", "Redundancy & Superfluous Expressions", "Removing duplicate meaning phrases")
                    )
                ),
                Topic(
                    id = "vocabulary",
                    title = "Vocabulary, Synonyms & Antonyms",
                    description = "High-frequency word roots, synonyms, and antonyms",
                    subtopics = listOf(
                        Subtopic("sub_synonyms", "High-Yield Synonyms", "Contextual word definitions and closely matched meanings"),
                        Subtopic("sub_antonyms", "High-Yield Antonyms", "Opposite word pairs and antonym identification"),
                        Subtopic("sub_one_word", "One Word Substitution", "Scientific terms, phobias, manias, and professions"),
                        Subtopic("sub_spelling", "Spelling Corrections", "Commonly misspelled double-letter and silent words")
                    )
                ),
                Topic(
                    id = "idioms_phrases",
                    title = "Idioms & Phrasal Verbs",
                    description = "Figurative expressions, origin idioms, and verb prepositions",
                    subtopics = listOf(
                        Subtopic("sub_common_idioms", "Most Repeated CGL Idioms", "Metaphorical idioms and everyday expressions"),
                        Subtopic("sub_phrasal_verbs", "Essential Phrasal Verbs", "Break out, call off, look into, bring about"),
                        Subtopic("sub_proverbs", "Proverbs & Adages", "Traditional wisdom sayings and sentence placements")
                    )
                ),
                Topic(
                    id = "comprehension",
                    title = "Reading Comprehension",
                    description = "Passage analysis, main ideas, tone, and direct inference",
                    subtopics = listOf(
                        Subtopic("sub_central_theme", "Central Theme & Main Idea", "Grasping author intent and summary statements"),
                        Subtopic("sub_direct_inference", "Direct Fact Retrieval", "Extracting specific details and factual figures"),
                        Subtopic("sub_tone_title", "Tone of Passage & Title", "Analytical, critical, narrative, or informative tones")
                    )
                ),
                Topic(
                    id = "cloze_test",
                    title = "Cloze Test & Fillers",
                    description = "Passage flow blanks and contextual vocabulary selection",
                    subtopics = listOf(
                        Subtopic("sub_cloze_passage", "Cloze Test Paragraphs", "Filling multiple contextual blanks in a single text"),
                        Subtopic("sub_single_fillers", "Single & Double Fillers", "Collocations and grammatical connector fillers")
                    )
                ),
                Topic(
                    id = "voice_narration",
                    title = "Active/Passive & Direct/Indirect",
                    description = "Voice conversions and reported speech tense shifts",
                    subtopics = listOf(
                        Subtopic("sub_active_passive", "Active & Passive Voice", "Tense conversions, modal verbs, and imperative forms"),
                        Subtopic("sub_direct_indirect", "Direct & Indirect Speech", "Backshift of tenses, pronoun, and time phrase rules")
                    )
                )
            )
        ),
        Subject(
            id = "gk_ga",
            title = "GK / GA",
            description = "General Knowledge & Current Affairs",
            icon = Icons.Default.Public,
            testTag = "subject_card_gk_ga",
            topics = listOf(
                Topic(
                    id = "history",
                    title = "Indian History",
                    description = "Ancient civilizations, medieval dynasties, and freedom struggle",
                    subtopics = listOf(
                        Subtopic("sub_ancient_ind", "Ancient India & Harappan Culture", "Indus Valley sites, Vedic period, and Buddhism/Jainism"),
                        Subtopic("sub_medieval_ind", "Delhi Sultanate & Mughal Dynasty", "Key rulers, administrative reforms, and architecture"),
                        Subtopic("sub_modern_ind", "Modern History & Freedom Movement", "1857 Revolt, Indian National Congress, and Gandhian Era")
                    )
                ),
                Topic(
                    id = "polity",
                    title = "Indian Polity & Constitution",
                    description = "Articles, fundamental rights, parliament, and amendments",
                    subtopics = listOf(
                        Subtopic("sub_preamble_rights", "Preamble & Fundamental Rights", "Articles 12-35, Fundamental Duties, and DPSP"),
                        Subtopic("sub_executive_parliament", "President, PM & Parliament", "Lok Sabha, Rajya Sabha bills, and emergency powers"),
                        Subtopic("sub_judiciary_bodies", "Judiciary & Constitutional Bodies", "Supreme Court, High Courts, CAG, Election Commission"),
                        Subtopic("sub_amendments", "Important Constitutional Amendments", "42nd, 44th, 73rd, 86th, and 101st GST amendments")
                    )
                ),
                Topic(
                    id = "geography",
                    title = "Geography of India & World",
                    description = "River systems, physiography, soils, and climate zones",
                    subtopics = listOf(
                        Subtopic("sub_physiography", "Physiography of India", "Himalayan ranges, Peninsular Plateau, and Coastal Plains"),
                        Subtopic("sub_drainage_system", "Drainage System & River Basins", "Himalayan vs Peninsular rivers, tributaries, and dams"),
                        Subtopic("sub_climate_monsoon", "Climate, Monsoons & Agriculture", "South-West monsoon, Kharif/Rabi crops, and soil types"),
                        Subtopic("sub_world_geo", "World Geography Essentials", "Continents, major straits, ocean currents, and atmosphere layers")
                    )
                ),
                Topic(
                    id = "general_science",
                    title = "General Science (PCB)",
                    description = "Physics mechanics, chemical elements, and human biology",
                    subtopics = listOf(
                        Subtopic("sub_physics", "Physics: Mechanics & Optics", "Newton's laws, gravitation, lens/mirrors, and units/dimensions"),
                        Subtopic("sub_chemistry", "Chemistry: Elements & Compounds", "Periodic table, acids/bases/salts, metals, and chemical reactions"),
                        Subtopic("sub_biology", "Biology: Human Physiology & Diseases", "Circulatory/Digestive systems, vitamins, and pathogens")
                    )
                ),
                Topic(
                    id = "economy",
                    title = "Indian Economy",
                    description = "GDP, monetary policy, banking, inflation, and budget",
                    subtopics = listOf(
                        Subtopic("sub_macro_econ", "National Income & GDP Concepts", "GNP, NNP, real vs nominal GDP calculations"),
                        Subtopic("sub_rbi_banking", "RBI & Monetary Policy", "Repo rate, reverse repo, CRR, SLR, and bank types"),
                        Subtopic("sub_inflation_fiscal", "Inflation & Fiscal Budget", "CPI/WPI indicators, fiscal deficit, and direct/indirect taxes")
                    )
                ),
                Topic(
                    id = "static_gk",
                    title = "Static GK & Culture",
                    description = "Dance forms, festivals, national parks, and awards",
                    subtopics = listOf(
                        Subtopic("sub_folk_classical_dance", "Classical & Folk Dances", "8 classical dances, state folk forms, and exponents"),
                        Subtopic("sub_festivals_fairs", "Festivals & Tribal Heritage", "Harvest festivals, regional fairs, and UNESCO sites"),
                        Subtopic("sub_national_parks", "National Parks & Sanctuaries", "Tiger reserves, biosphere reserves, and bird sanctuaries"),
                        Subtopic("sub_books_awards", "Books, Authors & Honors", "Bharat Ratna, Padma awards, Nobel prizes, and sports trophies")
                    )
                )
            )
        )
    )

    fun getSubjectById(id: String): Subject? = subjects.find { it.id == id }

    fun getTopicById(subjectId: String, topicId: String): Topic? {
        return getSubjectById(subjectId)?.topics?.find { it.id == topicId }
    }
}
