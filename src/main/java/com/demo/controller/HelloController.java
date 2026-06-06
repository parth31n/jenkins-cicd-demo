package com.demo.controller;

import com.demo.model.ApiResponse;
import com.demo.service.HelloService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HelloController {

    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping("/hello")
    public ResponseEntity<ApiResponse> hello(@RequestParam(required = false) String name) {
        String message = helloService.greet(name);
        return ResponseEntity.ok(new ApiResponse(message, "success"));
    }

    @GetMapping("/info")
    public ResponseEntity<ApiResponse> info() {
        return ResponseEntity.ok(new ApiResponse(helloService.getAppInfo(), "success"));
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse> health() {
        return ResponseEntity.ok(new ApiResponse("Application is running", "UP"));
    }
}
