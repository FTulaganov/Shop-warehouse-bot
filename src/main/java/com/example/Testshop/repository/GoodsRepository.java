package com.example.Testshop.repository;

import com.example.Testshop.entity.GoodsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsRepository extends JpaRepository<GoodsEntity,Integer> {
}
