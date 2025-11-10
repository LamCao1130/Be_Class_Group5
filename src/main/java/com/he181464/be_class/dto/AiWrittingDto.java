package com.he181464.be_class.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiWrittingDto {
    private String question;

    private String answer;
}
