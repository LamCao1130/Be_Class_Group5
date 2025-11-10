package com.he181464.be_class.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "accounts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "status")
    private Integer status;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "facebook_account_id")
    private Integer facebookAccountId;

    @Column(name = "google_account_id")
    private Integer googleAccountId;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "secret_code")
    private String secretCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Role role;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<Token> tokens;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<Exam> examList;

    @OneToMany(mappedBy = "studentId", fetch = FetchType.LAZY)
    private List<HomeworkSubmissions> homeworkSubmissions;

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    private List<LessonNotes> lessonNotes;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<ExamAttempts> examAttempts;

    @OneToMany(mappedBy = "account",fetch = FetchType.LAZY)
    private List<SubmissionHistory>submissionHistories;
}
