package com.he181464.be_class.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "listening_passage")
@Entity
public class ListeningPassage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "script_text",columnDefinition = "text")
    private String scriptText;

    @Column(name = "passage_type",columnDefinition = "VARCHAR(20)")
    private String passage_type;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "lesson_id", insertable = false, updatable = false)
    private Integer lessonId;

    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "listeningPassage",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    List<Questions>questions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", unique = true)
    private Lesson lesson;


}
