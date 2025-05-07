package com.coffee_backend.repo;

import com.coffee_backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    /** 查询某个买家的全部订单，按下单时间倒序 */
    List<Order> findByUser_IdOrderByOrderTimeDesc(Long userId);

    /** 农庄主收到的全部订单 */
    @Query("""
        select distinct o
        from   Order o
        join   o.coffeeBean cb
        join   cb.farm f
        where  f.user.id = :farmerId
        order  by o.orderTime desc
    """)
    List<Order> findAllForFarmer(@Param("farmerId") Long farmerId);
}


