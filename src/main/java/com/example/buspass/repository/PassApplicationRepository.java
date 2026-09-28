package com.example.buspass.repository;

import com.example.buspass.entity.PassApplication;
import com.example.buspass.entity.PassStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PassApplicationRepository extends JpaRepository<PassApplication, Long> {

    boolean existsByStudentIdAndStatus(Long studentId, PassStatus status);

    List<PassApplication> findByStudentIdOrderByAppliedDateDesc(Long studentId);

    List<PassApplication> findByStatusAndValidUntilBetween(
            PassStatus status, LocalDate start, LocalDate end);
}
