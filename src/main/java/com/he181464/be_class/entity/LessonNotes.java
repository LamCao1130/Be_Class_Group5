package com.he181464.be_class.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lesson_notes")
@Entity
public class LessonNotes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "note_type", columnDefinition = "varchar(20)")
    private String noteType;

    @Column(name = "title", columnDefinition = "varchar(255)")
    private String title;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    @Column(name = "attachment_url", columnDefinition = "varchar(500)")
    private String attachmentUrl;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", unique = true)
    private Account createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", unique = true)
    private Lesson lesson;
}
