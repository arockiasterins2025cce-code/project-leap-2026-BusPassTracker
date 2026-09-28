package com.example.buspass.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Entity
@Table(name = "pass_applications")
public class PassApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "route_id")
    private BusRoute route;

    @NotBlank
    @Column(nullable = false)
    private String boardingPoint;

    @NotBlank
    @Column(nullable = false)
    private String photoReference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PassStatus status = PassStatus.PENDING;

    @Column(unique = true)
    private String passNumber;

    private LocalDate validFrom;
    private LocalDate validUntil;

    private String rejectionReason;
    private String adminRemark;

    private LocalDate appliedDate = LocalDate.now();

    public PassApplication() {}

    public Long getId() { return id; }
    public Student getStudent() { return student; }
    public BusRoute getRoute() { return route; }
    public String getBoardingPoint() { return boardingPoint; }
    public String getPhotoReference() { return photoReference; }
    public PassStatus getStatus() { return status; }
    public String getPassNumber() { return passNumber; }
    public LocalDate getValidFrom() { return validFrom; }
    public LocalDate getValidUntil() { return validUntil; }
    public String getRejectionReason() { return rejectionReason; }
    public String getAdminRemark() { return adminRemark; }
    public LocalDate getAppliedDate() { return appliedDate; }

    public void setId(Long id) { this.id = id; }
    public void setStudent(Student student) { this.student = student; }
    public void setRoute(BusRoute route) { this.route = route; }
    public void setBoardingPoint(String boardingPoint) { this.boardingPoint = boardingPoint; }
    public void setPhotoReference(String photoReference) { this.photoReference = photoReference; }
    public void setStatus(PassStatus status) { this.status = status; }
    public void setPassNumber(String passNumber) { this.passNumber = passNumber; }
    public void setValidFrom(LocalDate validFrom) { this.validFrom = validFrom; }
    public void setValidUntil(LocalDate validUntil) { this.validUntil = validUntil; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    public void setAdminRemark(String adminRemark) { this.adminRemark = adminRemark; }
    public void setAppliedDate(LocalDate appliedDate) { this.appliedDate = appliedDate; }
}
