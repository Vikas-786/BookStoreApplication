package com.gkp.onlinebookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gkp.onlinebookstore.entity.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long> {

}
