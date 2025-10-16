package com.he181464.be_class.entity;

import com.he181464.be_class.constant.WordType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Vocabulary")
public class Vocabulary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "english_word",nullable = false)
    private String englishWord;

    @Column(name = "pronunciation",nullable = false)
    private String pronunciation;

    @Column(name = "vietnamese_meaning",nullable = false)
    private String vietnameseMeaning;

    @Column(name = "example_sentence",nullable = false)
    private String exampleSample;

    @Column(name = "sort_order",nullable = false)
    private int sortOrder;

    @Column(name = "word_type")
    private WordType wordType;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "example_audio")
    private String exampleAudio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id",referencedColumnName = "id")
    private Lesson lesson;
}
