package com.he181464.be_class.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassRoomRequest {
    private String name;
    private String title;
    private Long teacherId;
}
