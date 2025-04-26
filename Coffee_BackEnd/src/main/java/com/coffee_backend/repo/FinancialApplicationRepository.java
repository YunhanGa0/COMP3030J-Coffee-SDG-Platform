package com.coffee_backend.repo;

import com.coffee_backend.entity.FinancialApplication;
import com.coffee_backend.entity.FinancialSupport;
import com.coffee_backend.enumType.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FinancialApplicationRepository extends JpaRepository<FinancialApplication, Long> {
    Optional<FinancialApplication> findByFarmerId(Long farmerId);

    @Query("SELECT fa FROM FinancialApplication fa WHERE fa.status = :status")
    List<FinancialApplication> findByStatus(@Param("status") ApplicationStatus status);
}
