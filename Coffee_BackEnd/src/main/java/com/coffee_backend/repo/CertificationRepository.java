package com.coffee_backend.repo;

import com.coffee_backend.entity.CertificationApplication;
import com.coffee_backend.enumType.CertificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CertificationRepository extends JpaRepository<CertificationApplication, Long> {
    Optional<CertificationApplication> findByFarmId(Long farmId);

    @Query("SELECT ca FROM CertificationApplication ca WHERE ca.status = :status")
    List<CertificationApplication> findByStatus(@Param("status") CertificationStatus status);
}
