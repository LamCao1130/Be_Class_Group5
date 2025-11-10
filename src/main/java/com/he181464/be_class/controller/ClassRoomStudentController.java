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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/class-rooms-student/student")
@RequiredArgsConstructor
public class ClassRoomStudentController {

    private final ClassRoomStudentDTOService classRoomStudentDTOService;

    private final AccountRepository accountRepository;


    @GetMapping("/classroom")
    public ResponseEntity<Page<ClassRoomStudentDTO>> getClassRoomStudentsByStudentId(@RequestParam int page, @RequestParam int size) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String email = userDetails.getUsername();
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        if (optionalAccount.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Account account = optionalAccount.get();
        Page<ClassRoomStudentDTO> classRoom = classRoomStudentDTOService.getClassRoomStudentsByStudentId(account.getId(), page, size);
        return ResponseEntity.ok(classRoom);
    }

    @PostMapping("/join/{code}")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public ResponseEntity<?> joinClassRoomByCode(@PathVariable String code) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        if (optionalAccount.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Account account = optionalAccount.get();
        try {
            ClassRoomStudentDTO classRoomStudentDTO = classRoomStudentDTOService.joinClassRoomByCode(account.getId(), code);
            return ResponseEntity.ok(classRoomStudentDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
