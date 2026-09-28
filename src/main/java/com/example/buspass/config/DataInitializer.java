package com.example.buspass.config;

import com.example.buspass.entity.BusRoute;
import com.example.buspass.entity.Student;
import com.example.buspass.repository.BusRouteRepository;
import com.example.buspass.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(StudentRepository students, BusRouteRepository routes) {
        return args -> {
            if (students.count() == 0) {
                students.save(new Student(
                        "Arockia Sterin",
                        "25CC002",
                        "CCE",
                        "2",
                        "9876543210"
                ));
                students.save(new Student(
                        "Sample Student",
                        "25CC003",
                        "CSE",
                        "2",
                        "9876500000"
                ));
            }

            if (routes.count() == 0) {
                routes.save(new BusRoute("R01", "Coimbatore - College", "Gandhipuram"));
                routes.save(new BusRoute("R02", "Pollachi - College", "Pollachi Bus Stand"));
                routes.save(new BusRoute("R03", "Mettupalayam - College", "Mettupalayam"));
            }
        };
    }
}
