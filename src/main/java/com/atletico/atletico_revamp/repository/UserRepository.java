package com.atletico.atletico_revamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.atletico.atletico_revamp.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
}
