package com.he181464.be_class.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrammarPointDto {
    private int id;

    private int lessonId;

    @NotBlank(message = "Tên ngữ pháp không được để trống")
    private String title;

    private String explanation;

    private String structure;

    private String examples;

    private String usageNotes;
}
