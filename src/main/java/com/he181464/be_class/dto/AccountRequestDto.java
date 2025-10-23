package com.he181464.be_class.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDto {

    private String email;
    private String name;
    private String fullName;
    private String password;
    private Long roleId;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth; // Format: "YYYY-MM-DD"
    private Integer status; // e.g., 1 for active, 0 for inactive


}
