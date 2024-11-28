package com.gkp.onlinebookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gkp.onlinebookstore.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
