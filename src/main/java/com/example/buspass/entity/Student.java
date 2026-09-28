package com.example.buspass.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String registerNumber;

    @NotBlank
    @Column(nullable = false)
    private String department;

    @NotBlank
    @Column(name = "student_year", nullable = false)
    private String year;

    @NotBlank
    @Column(nullable = false)
    private String phone;

    public Student() {}

    public Student(String name, String registerNumber, String department, String year, String phone) {
        this.name = name;
        this.registerNumber = registerNumber;
        this.department = department;
        this.year = year;
        this.phone = phone;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getRegisterNumber() { return registerNumber; }
    public String getDepartment() { return department; }
    public String getYear() { return year; }
    public String getPhone() { return phone; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setRegisterNumber(String registerNumber) { this.registerNumber = registerNumber; }
    public void setDepartment(String department) { this.department = department; }
    public void setYear(String year) { this.year = year; }
    public void setPhone(String phone) { this.phone = phone; }
}
