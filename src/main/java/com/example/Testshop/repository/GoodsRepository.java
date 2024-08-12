package com.example.Testshop.repository;

import com.example.Testshop.entity.GoodsEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GoodsRepository extends JpaRepository<GoodsEntity,Integer> {
    Optional<GoodsEntity> findByCodeItem(String codeItem);

   // void deleteByCodeItem(String codeItem);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM goods WHERE code_item = :id", nativeQuery = true)
    void deleteById(@Param("id") String id);
}
