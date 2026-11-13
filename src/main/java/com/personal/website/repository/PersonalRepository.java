package com.personal.website.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.personal.website.models.PersonalModel;

public interface PersonalRepository extends JpaRepository<PersonalModel, Long> {
    
}
