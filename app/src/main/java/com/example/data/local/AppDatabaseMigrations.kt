package com.example.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/** Schema migrations for the central offline question bank. */
object AppDatabaseMigrations {
    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            renameLegacyCoreTables(db)
            createHierarchyTables(db)
            seedHierarchy(db)
            createQuestionBankTables(db)
            copyLegacyData(db)
            createQuestionBankIndices(db)
            dropLegacyCoreTables(db)
        }
    }

    private fun renameLegacyCoreTables(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE questions RENAME TO questions_legacy")
        db.execSQL("ALTER TABLE tests RENAME TO tests_legacy")
        db.execSQL("ALTER TABLE test_questions RENAME TO test_questions_legacy")
        db.execSQL("ALTER TABLE attempts RENAME TO attempts_legacy")
        db.execSQL("ALTER TABLE attempt_answers RENAME TO attempt_answers_legacy")
    }

    private fun createHierarchyTables(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS exams (
                id TEXT NOT NULL,
                code TEXT NOT NULL,
                name TEXT NOT NULL,
                description TEXT NOT NULL,
                is_active INTEGER NOT NULL,
                PRIMARY KEY(id)
            )
        """.trimIndent())
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS subjects (
                id TEXT NOT NULL,
                exam_id TEXT NOT NULL,
                name TEXT NOT NULL,
                description TEXT NOT NULL,
                display_order INTEGER NOT NULL,
                is_active INTEGER NOT NULL,
                PRIMARY KEY(id),
                FOREIGN KEY(exam_id) REFERENCES exams(id) ON UPDATE NO ACTION ON DELETE CASCADE
            )
        """.trimIndent())
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS topics (
                id TEXT NOT NULL,
                subject_id TEXT NOT NULL,
                name TEXT NOT NULL,
                description TEXT NOT NULL,
                display_order INTEGER NOT NULL,
                is_active INTEGER NOT NULL,
                PRIMARY KEY(id),
                FOREIGN KEY(subject_id) REFERENCES subjects(id) ON UPDATE NO ACTION ON DELETE CASCADE
            )
        """.trimIndent())
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS subtopics (
                id TEXT NOT NULL,
                topic_id TEXT NOT NULL,
                name TEXT NOT NULL,
                description TEXT NOT NULL,
                display_order INTEGER NOT NULL,
                is_active INTEGER NOT NULL,
                PRIMARY KEY(id),
                FOREIGN KEY(topic_id) REFERENCES topics(id) ON UPDATE NO ACTION ON DELETE CASCADE
            )
        """.trimIndent())

        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_exams_code ON exams(code)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_subjects_exam_id ON subjects(exam_id)")
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_subjects_exam_id_name ON subjects(exam_id, name)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_topics_subject_id ON topics(subject_id)")
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_topics_subject_id_name ON topics(subject_id, name)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_subtopics_topic_id ON subtopics(topic_id)")
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_subtopics_topic_id_name ON subtopics(topic_id, name)")
    }

    private fun seedHierarchy(db: SupportSQLiteDatabase) {
        db.execSQL("""
            INSERT OR IGNORE INTO exams(id, code, name, description, is_active)
            VALUES('ssc_cgl', 'SSC_CGL', 'SSC Combined Graduate Level',
                   'Tier I and Tier II practice, mock tests, and previous-year papers', 1)
        """.trimIndent())

        db.execSQL("""
            INSERT OR IGNORE INTO subjects(id, exam_id, name, description, display_order, is_active) VALUES
            ('reasoning', 'ssc_cgl', 'Reasoning', 'General Intelligence & Logical Reasoning', 1, 1),
            ('maths', 'ssc_cgl', 'Maths', 'Quantitative Aptitude & Numerical Ability', 2, 1),
            ('english', 'ssc_cgl', 'English', 'Grammar, Vocabulary & Comprehension', 3, 1),
            ('gk_ga', 'ssc_cgl', 'GK / GA', 'General Knowledge & Current Affairs', 4, 1)
        """.trimIndent())

        db.execSQL("""
            INSERT OR IGNORE INTO topics(id, subject_id, name, description, display_order, is_active) VALUES
            ('analogies', 'reasoning', 'Analogies & Classification', 'Semantic, symbolic, number, and figural analogy patterns', 1, 1),
            ('reasoning_general', 'reasoning', 'General Reasoning', 'Migrated uncategorized reasoning questions', 999, 1),
            ('maths_general', 'maths', 'General Maths', 'Migrated uncategorized maths questions', 999, 1),
            ('english_general', 'english', 'General English', 'Migrated uncategorized English questions', 999, 1),
            ('gk_ga_general', 'gk_ga', 'General Knowledge', 'Migrated uncategorized GK and GA questions', 999, 1)
        """.trimIndent())
    }

    private fun createQuestionBankTables(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE questions (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                content_key TEXT NOT NULL,
                topic_id TEXT NOT NULL,
                subtopic_id TEXT,
                question_text TEXT NOT NULL,
                option_a TEXT NOT NULL,
                option_b TEXT NOT NULL,
                option_c TEXT NOT NULL,
                option_d TEXT NOT NULL,
                correct_option INTEGER NOT NULL,
                explanation TEXT NOT NULL,
                difficulty TEXT NOT NULL,
                source_type TEXT NOT NULL,
                source_name TEXT,
                year INTEGER,
                exam_date TEXT,
                shift TEXT,
                language TEXT NOT NULL,
                is_active INTEGER NOT NULL,
                created_at INTEGER NOT NULL,
                updated_at INTEGER NOT NULL,
                FOREIGN KEY(topic_id) REFERENCES topics(id) ON UPDATE NO ACTION ON DELETE RESTRICT,
                FOREIGN KEY(subtopic_id) REFERENCES subtopics(id) ON UPDATE NO ACTION ON DELETE SET NULL
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE tests (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                exam_id TEXT NOT NULL,
                subject_id TEXT,
                topic_id TEXT,
                subtopic_id TEXT,
                title TEXT NOT NULL,
                test_type TEXT NOT NULL,
                year INTEGER,
                exam_date TEXT,
                shift TEXT,
                total_questions INTEGER NOT NULL,
                duration_minutes INTEGER NOT NULL,
                total_marks REAL NOT NULL,
                positive_marks REAL NOT NULL,
                negative_marks REAL NOT NULL,
                created_at INTEGER NOT NULL,
                FOREIGN KEY(exam_id) REFERENCES exams(id) ON UPDATE NO ACTION ON DELETE RESTRICT,
                FOREIGN KEY(subject_id) REFERENCES subjects(id) ON UPDATE NO ACTION ON DELETE SET NULL,
                FOREIGN KEY(topic_id) REFERENCES topics(id) ON UPDATE NO ACTION ON DELETE SET NULL,
                FOREIGN KEY(subtopic_id) REFERENCES subtopics(id) ON UPDATE NO ACTION ON DELETE SET NULL
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE test_questions (
                test_id INTEGER NOT NULL,
                question_id INTEGER NOT NULL,
                section_order INTEGER NOT NULL,
                question_order INTEGER NOT NULL,
                positive_marks REAL,
                negative_marks REAL,
                PRIMARY KEY(test_id, question_id),
                FOREIGN KEY(test_id) REFERENCES tests(id) ON UPDATE NO ACTION ON DELETE CASCADE,
                FOREIGN KEY(question_id) REFERENCES questions(id) ON UPDATE NO ACTION ON DELETE RESTRICT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE attempts (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                test_id INTEGER,
                subject_id TEXT,
                topic_id TEXT,
                subtopic_id TEXT,
                test_title TEXT NOT NULL,
                attempt_type TEXT NOT NULL,
                start_time INTEGER NOT NULL,
                end_time INTEGER,
                time_taken_seconds INTEGER NOT NULL,
                total_questions INTEGER NOT NULL,
                attempted_count INTEGER NOT NULL,
                correct_count INTEGER NOT NULL,
                wrong_count INTEGER NOT NULL,
                unattempted_count INTEGER NOT NULL,
                score REAL NOT NULL,
                max_score REAL NOT NULL,
                accuracy REAL NOT NULL,
                is_completed INTEGER NOT NULL,
                FOREIGN KEY(test_id) REFERENCES tests(id) ON UPDATE NO ACTION ON DELETE SET NULL,
                FOREIGN KEY(subject_id) REFERENCES subjects(id) ON UPDATE NO ACTION ON DELETE SET NULL,
                FOREIGN KEY(topic_id) REFERENCES topics(id) ON UPDATE NO ACTION ON DELETE SET NULL,
                FOREIGN KEY(subtopic_id) REFERENCES subtopics(id) ON UPDATE NO ACTION ON DELETE SET NULL
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE attempt_answers (
                attempt_id INTEGER NOT NULL,
                question_id INTEGER NOT NULL,
                answer_order INTEGER NOT NULL,
                selected_option INTEGER,
                correct_option INTEGER NOT NULL,
                is_correct INTEGER NOT NULL,
                is_marked_for_review INTEGER NOT NULL,
                time_spent_seconds INTEGER NOT NULL,
                awarded_marks REAL NOT NULL,
                PRIMARY KEY(attempt_id, question_id),
                FOREIGN KEY(attempt_id) REFERENCES attempts(id) ON UPDATE NO ACTION ON DELETE CASCADE,
                FOREIGN KEY(question_id) REFERENCES questions(id) ON UPDATE NO ACTION ON DELETE RESTRICT
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE bookmarks (
                question_id INTEGER NOT NULL,
                created_at INTEGER NOT NULL,
                PRIMARY KEY(question_id),
                FOREIGN KEY(question_id) REFERENCES questions(id) ON UPDATE NO ACTION ON DELETE CASCADE
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE question_progress (
                question_id INTEGER NOT NULL,
                attempt_count INTEGER NOT NULL,
                correct_count INTEGER NOT NULL,
                wrong_count INTEGER NOT NULL,
                skipped_count INTEGER NOT NULL,
                total_time_seconds INTEGER NOT NULL,
                last_attempted_at INTEGER,
                mastery_score REAL NOT NULL,
                needs_revision INTEGER NOT NULL,
                PRIMARY KEY(question_id),
                FOREIGN KEY(question_id) REFERENCES questions(id) ON UPDATE NO ACTION ON DELETE CASCADE
            )
        """.trimIndent())
    }

    private fun copyLegacyData(db: SupportSQLiteDatabase) {
        val subjectIdSql = """
            CASE
                WHEN lower(subject) LIKE '%reason%' THEN 'reasoning'
                WHEN lower(subject) LIKE '%math%' OR lower(subject) LIKE '%quant%' THEN 'maths'
                WHEN lower(subject) LIKE '%english%' THEN 'english'
                ELSE 'gk_ga'
            END
        """.trimIndent()
        val topicIdSql = """
            CASE
                WHEN lower(subject) LIKE '%reason%' AND lower(topic) LIKE '%analog%' THEN 'analogies'
                WHEN lower(subject) LIKE '%reason%' THEN 'reasoning_general'
                WHEN lower(subject) LIKE '%math%' OR lower(subject) LIKE '%quant%' THEN 'maths_general'
                WHEN lower(subject) LIKE '%english%' THEN 'english_general'
                ELSE 'gk_ga_general'
            END
        """.trimIndent()

        db.execSQL("""
            INSERT INTO questions(
                id, content_key, topic_id, subtopic_id, question_text,
                option_a, option_b, option_c, option_d, correct_option,
                explanation, difficulty, source_type, source_name, year,
                exam_date, shift, language, is_active, created_at, updated_at
            )
            SELECT id, 'legacy-' || id, $topicIdSql, NULL, question_text,
                   option_a, option_b, option_c, option_d, correct_option,
                   explanation, upper(difficulty),
                   CASE WHEN year IS NULL THEN 'PRACTICE' ELSE 'PYQ' END,
                   CASE
                       WHEN exam_name IS NOT NULL AND subtopic != '' THEN exam_name || ' • ' || subtopic
                       WHEN exam_name IS NOT NULL THEN exam_name
                       WHEN subtopic != '' THEN subtopic
                       ELSE NULL
                   END,
                   year, exam_date, shift, 'ENGLISH', 1, 0, 0
            FROM questions_legacy
        """.trimIndent())

        db.execSQL("""
            INSERT INTO tests(
                id, exam_id, subject_id, topic_id, subtopic_id, title, test_type,
                year, exam_date, shift, total_questions, duration_minutes,
                total_marks, positive_marks, negative_marks, created_at
            )
            SELECT id, 'ssc_cgl',
                   CASE WHEN subject IS NULL THEN NULL ELSE $subjectIdSql END,
                   NULL, NULL, title, test_type, year, exam_date, shift,
                   total_questions, duration_minutes, total_marks,
                   positive_marks, negative_marks, created_at
            FROM tests_legacy
        """.trimIndent())

        db.execSQL("""
            INSERT OR IGNORE INTO test_questions(
                test_id, question_id, section_order, question_order, positive_marks, negative_marks
            )
            SELECT test_id, question_id, section_order, question_order, NULL, NULL
            FROM test_questions_legacy
            ORDER BY id
        """.trimIndent())

        db.execSQL("""
            INSERT INTO attempts(
                id, test_id, subject_id, topic_id, subtopic_id, test_title, attempt_type,
                start_time, end_time, time_taken_seconds, total_questions, attempted_count,
                correct_count, wrong_count, unattempted_count, score, max_score, accuracy, is_completed
            )
            SELECT id, test_id, NULL, NULL, NULL, test_title, test_type,
                   start_time, end_time, time_taken_seconds, total_questions, attempted_count,
                   correct_count, wrong_count, unattempted_count, score, max_score, accuracy, is_completed
            FROM attempts_legacy
        """.trimIndent())

        db.execSQL("""
            INSERT OR REPLACE INTO attempt_answers(
                attempt_id, question_id, answer_order, selected_option, correct_option,
                is_correct, is_marked_for_review, time_spent_seconds, awarded_marks
            )
            SELECT attempt_id, question_id, id, selected_option, correct_option,
                   is_correct, is_marked_for_review, time_spent_seconds, 0.0
            FROM attempt_answers_legacy
            ORDER BY id
        """.trimIndent())

        db.execSQL("""
            INSERT OR IGNORE INTO bookmarks(question_id, created_at)
            SELECT id, 0 FROM questions_legacy WHERE is_bookmarked = 1
        """.trimIndent())

        db.execSQL("""
            INSERT OR REPLACE INTO question_progress(
                question_id, attempt_count, correct_count, wrong_count, skipped_count,
                total_time_seconds, last_attempted_at, mastery_score, needs_revision
            )
            SELECT aa.question_id,
                   COUNT(*),
                   SUM(CASE WHEN aa.is_correct = 1 THEN 1 ELSE 0 END),
                   SUM(CASE WHEN aa.selected_option IS NOT NULL AND aa.is_correct = 0 THEN 1 ELSE 0 END),
                   SUM(CASE WHEN aa.selected_option IS NULL THEN 1 ELSE 0 END),
                   SUM(aa.time_spent_seconds),
                   MAX(COALESCE(a.end_time, a.start_time)),
                   (SUM(CASE WHEN aa.is_correct = 1 THEN 1.0 ELSE 0.0 END) * 100.0) / COUNT(*),
                   CASE
                       WHEN SUM(CASE WHEN aa.is_correct = 1 THEN 1.0 ELSE 0.0 END) / COUNT(*) < 0.6
                            OR MAX(aa.is_marked_for_review) = 1
                       THEN 1 ELSE 0
                   END
            FROM attempt_answers aa
            INNER JOIN attempts a ON a.id = aa.attempt_id
            GROUP BY aa.question_id
        """.trimIndent())
    }

    private fun createQuestionBankIndices(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE UNIQUE INDEX index_questions_content_key ON questions(content_key)")
        db.execSQL("CREATE INDEX index_questions_topic_id ON questions(topic_id)")
        db.execSQL("CREATE INDEX index_questions_subtopic_id ON questions(subtopic_id)")
        db.execSQL("CREATE INDEX index_questions_source_type ON questions(source_type)")
        db.execSQL("CREATE INDEX index_questions_year_shift ON questions(year, shift)")
        db.execSQL("CREATE INDEX index_questions_difficulty ON questions(difficulty)")
        db.execSQL("CREATE INDEX index_tests_exam_id ON tests(exam_id)")
        db.execSQL("CREATE INDEX index_tests_subject_id ON tests(subject_id)")
        db.execSQL("CREATE INDEX index_tests_topic_id ON tests(topic_id)")
        db.execSQL("CREATE INDEX index_tests_subtopic_id ON tests(subtopic_id)")
        db.execSQL("CREATE INDEX index_tests_test_type ON tests(test_type)")
        db.execSQL("CREATE INDEX index_tests_year_shift ON tests(year, shift)")
        db.execSQL("CREATE INDEX index_test_questions_question_id ON test_questions(question_id)")
        db.execSQL("CREATE UNIQUE INDEX index_test_questions_test_id_question_order ON test_questions(test_id, question_order)")
        db.execSQL("CREATE INDEX index_attempts_test_id ON attempts(test_id)")
        db.execSQL("CREATE INDEX index_attempts_subject_id ON attempts(subject_id)")
        db.execSQL("CREATE INDEX index_attempts_topic_id ON attempts(topic_id)")
        db.execSQL("CREATE INDEX index_attempts_subtopic_id ON attempts(subtopic_id)")
        db.execSQL("CREATE INDEX index_attempts_start_time ON attempts(start_time)")
        db.execSQL("CREATE INDEX index_attempt_answers_question_id ON attempt_answers(question_id)")
        db.execSQL("CREATE INDEX index_question_progress_revision_mastery ON question_progress(needs_revision, mastery_score)")
        db.execSQL("CREATE INDEX index_question_progress_last_attempted_at ON question_progress(last_attempted_at)")
    }

    private fun dropLegacyCoreTables(db: SupportSQLiteDatabase) {
        db.execSQL("DROP TABLE attempt_answers_legacy")
        db.execSQL("DROP TABLE test_questions_legacy")
        db.execSQL("DROP TABLE attempts_legacy")
        db.execSQL("DROP TABLE tests_legacy")
        db.execSQL("DROP TABLE questions_legacy")
    }
}
