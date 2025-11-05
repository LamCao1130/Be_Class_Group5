package com.he181464.be_class.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponseDto {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private Integer status;
    private LocalDate dateOfBirth;
    private Long roleId;
    private String roleName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<Long> classRoomIds;

    private List<ClassRoomResponseDto> classRooms;

}