package com.example.buspass.service;

import com.example.buspass.entity.BusRoute;
import com.example.buspass.exception.ResourceNotFoundException;
import com.example.buspass.repository.BusRouteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusRouteService {

    private final BusRouteRepository repository;

    public BusRouteService(BusRouteRepository repository) {
        this.repository = repository;
    }

    public BusRoute create(BusRoute route) {
        return repository.save(route);
    }

    public List<BusRoute> getAll() {
        return repository.findAll();
    }

    public BusRoute getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bus route not found: " + id));
    }
}
