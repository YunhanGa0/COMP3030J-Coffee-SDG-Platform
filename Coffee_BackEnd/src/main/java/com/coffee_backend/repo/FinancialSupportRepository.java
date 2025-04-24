package com.coffee_backend.repo;

import com.coffee_backend.entity.FinancialSupport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialSupportRepository extends JpaRepository<FinancialSupport, Long> {
}
