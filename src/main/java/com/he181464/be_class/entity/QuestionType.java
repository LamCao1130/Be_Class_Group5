package com.he181464.be_class.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "questions_type")
@Entity
public class QuestionType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type")
    private String type;
    @Column(name = "name")
    private String name;

    @Column(name = "lesson_id", insertable = false, updatable = false)
    private Integer lessonId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exams_id", nullable = true)
    private Exam exam;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "questionType", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Questions> questions;
}
