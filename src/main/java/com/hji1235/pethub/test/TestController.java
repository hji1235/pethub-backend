package com.hji1235.pethub.test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/example")
    public ResponseEntity<Object> example() {
        return ResponseEntity.ok("성공");
    }
}
