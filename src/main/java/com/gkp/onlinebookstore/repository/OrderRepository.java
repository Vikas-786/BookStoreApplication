package com.gkp.onlinebookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gkp.onlinebookstore.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
