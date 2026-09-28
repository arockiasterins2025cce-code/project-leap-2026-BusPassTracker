package com.example.buspass.service;

import com.example.buspass.dto.ApplyPassRequest;
import com.example.buspass.dto.DecisionRequest;
import com.example.buspass.entity.*;
import com.example.buspass.exception.BusinessRuleException;
import com.example.buspass.exception.ResourceNotFoundException;
import com.example.buspass.repository.BusRouteRepository;
import com.example.buspass.repository.PassApplicationRepository;
import com.example.buspass.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PassApplicationService {

    private final PassApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;
    private final BusRouteRepository routeRepository;

    public PassApplicationService(PassApplicationRepository applicationRepository,
                                  StudentRepository studentRepository,
                                  BusRouteRepository routeRepository) {
        this.applicationRepository = applicationRepository;
        this.studentRepository = studentRepository;
        this.routeRepository = routeRepository;
    }

    public PassApplication apply(ApplyPassRequest request) {
        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + request.studentId()));

        BusRoute route = routeRepository.findById(request.routeId())
                .orElseThrow(() -> new ResourceNotFoundException("Bus route not found: " + request.routeId()));

        // Business rule: one student can have only one active pass.
        if (applicationRepository.existsByStudentIdAndStatus(student.getId(), PassStatus.APPROVED)) {
            throw new BusinessRuleException("Student already has an active bus pass.");
        }

        PassApplication application = new PassApplication();
        application.setStudent(student);
        application.setRoute(route);
        application.setBoardingPoint(request.boardingPoint());
        application.setPhotoReference(request.photoReference());
        application.setStatus(PassStatus.PENDING);
        application.setAppliedDate(LocalDate.now());

        return applicationRepository.save(application);
    }

    public PassApplication approve(Long id, DecisionRequest request) {
        PassApplication application = getById(id);

        if (application.getStatus() != PassStatus.PENDING) {
            throw new BusinessRuleException("Only pending applications can be approved.");
        }

        if (applicationRepository.existsByStudentIdAndStatus(
                application.getStudent().getId(), PassStatus.APPROVED)) {
            throw new BusinessRuleException("Student already has an active bus pass.");
        }

        int validityDays = request.validityDays() == null ? 180 : request.validityDays();
        if (validityDays <= 0) {
            throw new BusinessRuleException("Validity days must be greater than zero.");
        }

        LocalDate from = LocalDate.now();
        application.setStatus(PassStatus.APPROVED);
        application.setPassNumber("BP-" + String.format("%06d", application.getId()));
        application.setValidFrom(from);
        application.setValidUntil(from.plusDays(validityDays));
        application.setAdminRemark(request.remark());

        return applicationRepository.save(application);
    }

    public PassApplication reject(Long id, DecisionRequest request) {
        PassApplication application = getById(id);

        if (application.getStatus() != PassStatus.PENDING) {
            throw new BusinessRuleException("Only pending applications can be rejected.");
        }

        // Business rule: rejected application must contain a reason.
        if (request.rejectionReason() == null || request.rejectionReason().isBlank()) {
            throw new BusinessRuleException("Rejection reason is required.");
        }

        application.setStatus(PassStatus.REJECTED);
        application.setRejectionReason(request.rejectionReason());
        application.setAdminRemark(request.remark());

        return applicationRepository.save(application);
    }

    public PassApplication getById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found: " + id));
    }

    public List<PassApplication> getAll() {
        return applicationRepository.findAll();
    }

    public List<PassApplication> getByStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found: " + studentId);
        }
        return applicationRepository.findByStudentIdOrderByAppliedDateDesc(studentId);
    }

    public List<PassApplication> expiringWithin30Days() {
        LocalDate today = LocalDate.now();
        return applicationRepository.findByStatusAndValidUntilBetween(
                PassStatus.APPROVED, today, today.plusDays(30));
    }
}
