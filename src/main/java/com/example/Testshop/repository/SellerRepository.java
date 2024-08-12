package com.example.Testshop.repository;

import com.example.Testshop.entity.SellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerRepository extends JpaRepository<SellerEntity,Long> {
    SellerEntity findByChatId(Long id);
    List<SellerEntity> findAllByVisibleFalse();
    List<SellerEntity> findByBonusPercentageGreaterThanEqual(double minBonusPercentage);

    List<SellerEntity> findTopSellersByBonusPercentage(double topCount);
}
