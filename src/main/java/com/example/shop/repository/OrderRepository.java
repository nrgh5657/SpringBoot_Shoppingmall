package com.example.shop.repository;

import com.example.shop.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    /*
        SELECT o.*
        FROM orders o
        JOIN member m ON o.member_id = m.member_id
        WHERE m.email = 'admin@admin.com'
        order by o.order_date desc;
    */
    //JPQL
    @Query("SELECT o FROM Order o WHERE o.member.email = :email ORDER BY o.orderDate DESC")
    List<Order> findOrders(@Param("email") String email, Pageable pageable);

    /*
        select count(o.order_id)
        from orders o
        join member m
        on o.member_id = m.member_id
        where m.email ='admin@admin.com';
     */
    @Query("SELECT count(o) from Order o where o.member.email= :email")
    Long countOrders(@Param("email") String email);

}

