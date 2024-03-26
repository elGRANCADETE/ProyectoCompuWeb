package com.eventoslive.eventosliveapp.controller;

import com.eventoslive.eventosliveapp.model.UserBlock;
import com.eventoslive.eventosliveapp.service.UserBlockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blocks")
public class UserBlockController {
    
    private final UserBlockService userBlockService;

    public UserBlockController(UserBlockService userBlockService) {
        this.userBlockService = userBlockService;
    }

    @PostMapping("/block")
    public ResponseEntity<UserBlock> blockUser(@RequestBody UserBlock userBlock) {
        UserBlock newUserBlock = userBlockService.blockUser(userBlock);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUserBlock);
    }

    // Other endpoints as needed...
}

