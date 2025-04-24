package com.coffee_backend.repo;

import com.coffee_backend.entity.CoffeeBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Le Liu
 * @create 2025-04
 */
@Repository
public interface CoffeeBeanRepository extends JpaRepository<CoffeeBean, Long> {
    List<CoffeeBean> findByFarmId(Long farmId);


}
