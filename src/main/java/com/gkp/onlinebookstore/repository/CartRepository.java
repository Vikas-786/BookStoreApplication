package com.gkp.onlinebookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gkp.onlinebookstore.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
