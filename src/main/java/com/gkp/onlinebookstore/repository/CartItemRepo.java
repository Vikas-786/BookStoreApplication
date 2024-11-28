package com.gkp.onlinebookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gkp.onlinebookstore.entity.CartItem;

public interface CartItemRepo extends JpaRepository<CartItem, Long> {

}
