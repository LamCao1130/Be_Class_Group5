package com.he181464.be_class.dto;

import com.he181464.be_class.constant.WordType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VocabularyCreateDto {


    @NotBlank(message = "Từ không được để trống")
    private String englishWord;


    @NotBlank(message = "Phát âm không được để trống")
    private String pronunciation;

    @NotBlank(message = "Nghĩa không được để trống")
    private String vietnameseMeaning;

    private String exampleSample;

     private WordType wordType;

    private String imageUrl;

    private String exampleAudio;


    private Long lessonId;

    private int sortOrder;
}
