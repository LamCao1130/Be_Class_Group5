package com.he181464.be_class.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "exams")
@AllArgsConstructor
@NoArgsConstructor
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", columnDefinition = "varchar(255)")
    private String title;

    @Column(name = "exam_type", columnDefinition = "varchar(20)")
    private String examType;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "total_marks")
    private Integer totalMarks;

    @Column(name = "passing_score")
    private Integer passingScore;

    @Column(name = "switch_tabs")
    private Integer switchTabs;

    @Column(name = "status", columnDefinition = "tinyint")
    private Integer status;

    @Column(name = "created_at", columnDefinition = "datetime")
    private Date createdAt;

    @Column(name = "exam_date", columnDefinition = "datetime")
    private Date examDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_room_id")
    private ClassRoom classRoom;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "exam")
    private List<ExamAttempts> examAttempts;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "exam")
    private List<QuestionType> questionTypes;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "exam")
    private List<ReadingPassage> readingPassages;
}
