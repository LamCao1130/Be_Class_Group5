package com.he181464.be_class.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "exam_attempts")
@Entity
public class ExamAttempts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Account student;

    @Column(name = "score", precision = 5, scale = 2)
    private Integer score;

    @Column(name = "time_spent")
    private Integer timeSpent;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "status", length = 20)
    private String status; //

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "examAttempts")
    private List<QuestionAnswers>questionAnswers;
}
