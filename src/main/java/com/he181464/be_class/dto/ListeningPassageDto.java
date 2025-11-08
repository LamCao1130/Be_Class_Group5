package com.he181464.be_class.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListeningPassageDto {
    private Integer id;

    private String scriptText;

    private String passage_type;

    private String title;


    private LocalDateTime createdAt;
}
