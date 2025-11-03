package com.he181464.be_class.controller;

import com.he181464.be_class.dto.ClassRoomStudentDTO;
import com.he181464.be_class.entity.Account;
import com.he181464.be_class.repository.AccountRepository;
import com.he181464.be_class.service.ClassRoomStudentDTOService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/class-rooms-student/student")
@RequiredArgsConstructor
public class ClassRoomStudentController {
    @Autowired
    private final ClassRoomStudentDTOService classRoomStudentDTOService;
    @Autowired
    private final AccountRepository accountRepository;

    @GetMapping("/classroom")
    public ResponseEntity<Page<ClassRoomStudentDTO>> getClassRoomStudentsByStudentId(Authentication authentication, @RequestParam int page, @RequestParam int size) {

        if(authentication==null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        UserDetails userDetails=(UserDetails) authentication.getPrincipal();
        String email=userDetails.getUsername();
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        if(optionalAccount.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Account account=optionalAccount.get();
        Page<ClassRoomStudentDTO> classRoom=classRoomStudentDTOService.getClassRoomStudentsByStudentId(account.getId(),page,size);
        return ResponseEntity.ok(classRoom);
    }
}
