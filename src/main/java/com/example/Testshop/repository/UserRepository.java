package com.example.Testshop.repository;

import com.example.Testshop.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findByChatId(Long id);
}
