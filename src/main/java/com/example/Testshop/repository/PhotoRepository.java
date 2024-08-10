package com.example.Testshop.repository;

import com.example.Testshop.entity.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {
    PhotoEntity findByToken(String token);
}

