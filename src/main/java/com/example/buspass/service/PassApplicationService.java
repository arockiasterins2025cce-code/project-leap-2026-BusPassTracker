package com.example.buspass.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.buspass.dto.ApplyPassRequest;
import com.example.buspass.dto.DecisionRequest;
import com.example.buspass.entity.BusRoute;
import com.example.buspass.entity.PassApplication;
import com.example.buspass.entity.PassStatus;
import com.example.buspass.entity.Student;
import com.example.buspass.exception.BusinessRuleException;
import com.example.buspass.exception.ResourceNotFoundException;
import com.example.buspass.repository.BusRouteRepository;
import com.example.buspass.repository.PassApplicationRepository;
import com.example.buspass.repository.StudentRepository;

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

        Integer validityDaysValue = request.validityDays();
        int validityDays = validityDaysValue == null ? 180 : validityDaysValue;
        if (validityDays <= 0) {
            throw new BusinessRuleException("Validity days must be greater than zero.");
        }

        LocalDate from = LocalDate.now();
        application.setStatus(PassStatus.APPROVED);
        application.setValidFrom(from);
        application.setValidUntil(from.plusDays(validityDays));
        application.setAdminRemark(request.remark());

        PassApplication savedApplication = applicationRepository.save(application);
        savedApplication.setPassNumber("BP-" + String.format("%06d", savedApplication.getId()));
        return applicationRepository.save(savedApplication);
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
