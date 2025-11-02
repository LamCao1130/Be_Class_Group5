package com.he181464.be_class.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "exams")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_room_id", nullable = false)
    private Long classRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_room_id", insertable = false, updatable = false)
    private ClassRoom classRoom;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "exam_type", length = 20, nullable = false)
    private String examType;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "total_marks", nullable = false)
    private Integer totalMarks;

    @Column(name = "passing_score", nullable = false)
    private Integer passingScore;

    @Column(name = "exam_date")
    private LocalDateTime examDate;

    @Column(name = "Switch_tabs")
    private Integer switchTabs;

    @Column(name = "status")
    private Integer status;

    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", insertable = false, updatable = false)
    private Account creator;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = 1;
        }
    }
}