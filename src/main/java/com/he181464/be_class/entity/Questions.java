package com.he181464.be_class.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "questions")
public class Questions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "question_text", columnDefinition = "text")
    private String questionText;

    @Column(name = "correct_answer", columnDefinition = "text")
    private String correctAnswer;

    @Column(name = "explanation", columnDefinition = "text")
    private String explanation;

    @Column(name = "marks")
    private Integer marks;

    @Column(name = "difficulty", columnDefinition = "varchar(10)")
    private String difficulty;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "listening_text",columnDefinition = "text")
    private String listeningText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reading_passage_id")
    private ReadingPassage readingPassage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_type_id")
    private QuestionType questionType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "questions", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<QuestionOption> questionOptions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listening_passage_id")
    private ListeningPassage listeningPassage;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "questions",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<QuestionAnswers>questionAnswers;
}
