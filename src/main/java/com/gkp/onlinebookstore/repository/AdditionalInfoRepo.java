package com.gkp.onlinebookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gkp.onlinebookstore.entity.AdditionalInformation;

public interface AdditionalInfoRepo extends JpaRepository<AdditionalInformation, Long> {

}
