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
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "class_room_id", nullable = false)
    private Long classRoomId;

    @Column(length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "status")
    private Integer status;

    @Column(name = "has_homework")
    private Integer hasHomework;

    @Column(name = "homework_instructions", columnDefinition = "TEXT")
    private String homeworkInstructions;

    @Column(name = "homework_deadline")
    private LocalDateTime homeworkDeadline;

    @Column(name = "homework_max_score")
    private Integer homeworkMaxScore;

    @Column(name = "homework_attachment_url", length = 500)
    private String homeworkAttachmentUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
