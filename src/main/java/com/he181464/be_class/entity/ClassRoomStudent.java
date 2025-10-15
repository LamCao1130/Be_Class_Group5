package com.he181464.be_class.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "class_room_student")
@Builder
public class ClassRoomStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "id")
    private Long id;

    @Column(name = "class_room_id", nullable = false)
    private Long classRoomId;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "join_date", nullable = false)
    private LocalDateTime joinDate;


}
