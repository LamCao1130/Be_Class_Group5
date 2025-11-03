package com.he181464.be_class.entity;

import com.he181464.be_class.constant.AppConstant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Persistent;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "class_rooms")
public class ClassRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "teacher_id", nullable = false)
    private Long teacherId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Account teacher;

    @OneToMany(mappedBy = "classRoom")
    private List<Exam> exams;


    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = AppConstant.STATUS_ACTIVE;
        }
    }



}
