package com.he181464.be_class.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "submission_history")
@Builder
public class SubmissionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = true)
    private String type;

    @Column(name = "number_wrong", nullable = true)
    private Integer numberWrong;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "answer_writing")
    private String answerWriting;

    @Column(name ="student_id")
    private Long studentId;

    @Column(name ="lesson_id")
    private Long lessonId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id",insertable = false, updatable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id",insertable = false, updatable = false)
    private Lesson lesson;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "submissionHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionAnswers> questionAnswers;
}
