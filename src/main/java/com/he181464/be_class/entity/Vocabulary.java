package com.he181464.be_class.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Vocabulary")
public class Vocabulary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "english_word", nullable = false)
    private String englishWord;

    @Column(name = "pronunciation", nullable = false)
    private String pronunciation;

    @Column(name = "vietnamese_meaning", nullable = false)
    private String vietnameseMeaning;

    @Column(name = "example_sentence", nullable = false)
    private String exampleSentence;

    @Column(name = "word_type")
    private String wordType;

    @Column(name = "lesson_id", nullable = false)
    private Long lessonId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Lesson lesson;
}
