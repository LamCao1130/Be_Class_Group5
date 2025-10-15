package com.he181464.be_class.controller;

import com.he181464.be_class.dto.ClassRoomDto;
import com.he181464.be_class.service.ClassRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/public/class-rooms")
@RequiredArgsConstructor
public class ClassRoomController {

    private final ClassRoomService classRoomService;

    @PostMapping("/create")
    public ResponseEntity<?> createClassRoom(@Valid @RequestBody ClassRoomDto classRoomDto) {
        return ResponseEntity.ok(classRoomService.createClassRoom(classRoomDto));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateClassRoom(@Valid @RequestBody ClassRoomDto classRoomDto) {
        return ResponseEntity.ok(classRoomService.updateClassRoom(classRoomDto));
    }

}
