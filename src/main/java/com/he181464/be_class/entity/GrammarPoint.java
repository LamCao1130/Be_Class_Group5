package com.he181464.be_class.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "grammar_points")
public class GrammarPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", columnDefinition = "varchar(255)")
    private String title;

    @Column(name = "explanation", columnDefinition = "text")
    private String explanation;

    @Column(name = "structure", columnDefinition = "varchar(500)")
    private String structure;

    @Column(name = "examples", columnDefinition = "text")
    private String examples;

    @Column(name = "usage_notes", columnDefinition = "text")
    private String usageNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;


}
