package com.example.buspass.controller;

import com.example.buspass.entity.BusRoute;
import com.example.buspass.service.BusRouteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class BusRouteController {

    private final BusRouteService service;

    public BusRouteController(BusRouteService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BusRoute create(@Valid @RequestBody BusRoute route) {
        return service.create(route);
    }

    @GetMapping
    public List<BusRoute> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public BusRoute getById(@PathVariable Long id) {
        return service.getById(id);
    }
}
