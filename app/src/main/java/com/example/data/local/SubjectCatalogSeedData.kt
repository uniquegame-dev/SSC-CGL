package com.example.data.local

import com.example.data.local.entities.SubtopicEntity
import com.example.data.local.entities.TopicEntity
import com.example.data.models.SscDataRepository

/**
 * Transitional bundled catalog seed. Runtime subject practice reads Room only; this converts the
 * app's existing SSC CGL catalog into normalized topic and subtopic rows on first launch.
 */
object SubjectCatalogSeedData {
    val topics: List<TopicEntity> by lazy {
        SscDataRepository.subjects.flatMap { subject ->
            subject.topics.mapIndexed { index, topic ->
                TopicEntity(
                    id = topic.id,
                    subjectId = subject.id,
                    name = topic.title,
                    description = topic.description,
                    displayOrder = index + 1
                )
            }
        }
    }

    val subtopics: List<SubtopicEntity> by lazy {
        SscDataRepository.subjects.flatMap { subject ->
            subject.topics.flatMap { topic ->
                topic.subtopics.mapIndexed { index, subtopic ->
                    SubtopicEntity(
                        id = subtopic.id,
                        topicId = topic.id,
                        name = subtopic.title,
                        description = subtopic.description,
                        displayOrder = index + 1
                    )
                }
            }
        }
    }
}
