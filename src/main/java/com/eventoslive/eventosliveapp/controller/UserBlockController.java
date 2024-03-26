package com.eventoslive.eventosliveapp.controller;

import com.eventoslive.eventosliveapp.model.UserBlock;
import com.eventoslive.eventosliveapp.service.UserBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blocks")
public class UserBlockController {
    
    private final UserBlockService userBlockService;

    @Autowired
    public UserBlockController(UserBlockService userBlockService) {
        this.userBlockService = userBlockService;
    }

    @PostMapping
    public ResponseEntity<UserBlock> blockUser(@RequestBody UserBlock block) {
        UserBlock newUserBlock = userBlockService.blockUser(block);
        return ResponseEntity.ok(newUserBlock);
    }

    // Other endpoints as needed...
}

