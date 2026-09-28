package com.example.buspass.controller;

import com.example.buspass.dto.ApplyPassRequest;
import com.example.buspass.dto.DecisionRequest;
import com.example.buspass.entity.PassApplication;
import com.example.buspass.service.PassApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class PassApplicationController {

    private final PassApplicationService service;

    public PassApplicationController(PassApplicationService service) {
        this.service = service;
    }

    // Student submits an application
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PassApplication apply(@Valid @RequestBody ApplyPassRequest request) {
        return service.apply(request);
    }

    // Student checks an individual application
    @GetMapping("/{id}")
    public PassApplication getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // Student checks all their applications
    @GetMapping("/student/{studentId}")
    public List<PassApplication> getByStudent(@PathVariable Long studentId) {
        return service.getByStudent(studentId);
    }

    // Admin: list all applications
    @GetMapping
    public List<PassApplication> getAll() {
        return service.getAll();
    }

    // Admin: approve
    @PutMapping("/{id}/approve")
    public PassApplication approve(@PathVariable Long id,
                                   @RequestBody DecisionRequest request) {
        return service.approve(id, request);
    }

    // Admin: reject
    @PutMapping("/{id}/reject")
    public PassApplication reject(@PathVariable Long id,
                                  @RequestBody DecisionRequest request) {
        return service.reject(id, request);
    }

    // Admin: passes expiring in next 30 days
    @GetMapping("/expiring/30-days")
    public List<PassApplication> expiringWithin30Days() {
        return service.expiringWithin30Days();
    }
}
