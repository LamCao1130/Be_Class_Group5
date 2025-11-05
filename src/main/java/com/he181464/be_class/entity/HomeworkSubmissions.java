package com.he181464.be_class.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "homework_submissions")
public class HomeworkSubmissions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "submission_content", columnDefinition = "text")
    private String submissionContext;

    @Column(name = "attachment_url", columnDefinition = "varchar(500)")
    private String attachmentUrl;

    @Column(name = "submitted_at", columnDefinition = "datetime")
    private LocalDateTime submittedAt;

    @Column(name = "status", columnDefinition = "varchar(20)")
    private String status;

    @Column(name = "score", columnDefinition = "decimal(5,2)")
    private BigDecimal score;

    @Column(name = "teacher_feedback", columnDefinition = "text")
    private String teacherFeedback;

    @Column(name = "graded_by")
    private Integer  gradedBy;

    @Column(name = "graded_at")
    private LocalDateTime gradedAt;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "time_spent")
    private int timeSpent;

    @Column(name = "attempt_count")
    private int attemptCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Account studentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lessonId;
}
