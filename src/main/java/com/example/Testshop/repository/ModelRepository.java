package com.example.Testshop.repository;

import com.example.Testshop.entity.GoodsEntity;
import com.example.Testshop.entity.ModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModelRepository extends JpaRepository<ModelEntity,Integer> {
    Optional<ModelEntity> findByName(String modelName);
}
