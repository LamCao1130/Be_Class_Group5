package com.he181464.be_class.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDTO {
    private int totalActiveClasses;
    private int totalActiveStudents;
    private int totalActiveTeachers;
    private int totalActiveLessons;
}
