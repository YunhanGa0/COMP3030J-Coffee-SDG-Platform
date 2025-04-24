package com.coffee_backend.repo;

import com.coffee_backend.entity.FinancialApplication;
import com.coffee_backend.entity.FinancialSupport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FinancialApplicationRepository extends JpaRepository<FinancialApplication, Long> {
    Optional<FinancialApplication> findByFarmerId(Long farmerId);
}
