package com.example.Testshop.repository;

import com.example.Testshop.entity.GoodsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoodsRepository extends JpaRepository<GoodsEntity,Integer> {
    Optional<GoodsEntity> findByCodeItem(String codeItem);

    void deleteByCodeItem(String codeItem);
}
