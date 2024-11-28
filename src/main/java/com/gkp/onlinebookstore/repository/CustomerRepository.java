package com.gkp.onlinebookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gkp.onlinebookstore.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
