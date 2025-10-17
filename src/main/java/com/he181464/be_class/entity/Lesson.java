package com.he181464.be_class.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY)
    private List<Vocabulary> vocabularies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_room_id")
    private ClassRoom classroom;

    @Column(name = "is_published", columnDefinition = "bit default 1")
    private boolean isPublish;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "has_homework", columnDefinition = "bit default 1")
    private boolean hasHomework ;

    @Column(name = "homework_instructions", columnDefinition = "TEXT")
    private String homeworkInstructions;

    @Column(name = "homework_deadline")
    private LocalDateTime homeworkDeadline;

    @Column(name = "homework_max_score")
    private Integer homeworkMaxScore;

    @Column(name = "homework_attachment_url")
    private String homeworkAttachmentUrl;
}
