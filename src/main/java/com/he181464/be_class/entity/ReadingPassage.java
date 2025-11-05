package com.he181464.be_class.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "reading_passages")
@Entity
public class ReadingPassage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", columnDefinition = "varchar(255)")
    private String title;

    @Column(name = "passage_content", columnDefinition = "text")
    private String passageContent;

    @Column(name = "difficulty", columnDefinition = "varchar(10)")
    private String difficulty;

    @Column(name = "created_at")
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", unique = true)
    private Lesson lesson;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "readingPassage", cascade = CascadeType.ALL)
    private List<Questions> questions;
}
