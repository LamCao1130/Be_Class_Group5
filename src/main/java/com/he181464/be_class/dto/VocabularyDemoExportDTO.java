package com.he181464.be_class.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VocabularyDemoExportDTO {
    private String word;
    private String pronunciation;
    private String meaning;
    private String type;
    private String example;
}
