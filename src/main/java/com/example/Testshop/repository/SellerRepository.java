package com.example.Testshop.repository;

import com.example.Testshop.entity.SellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<SellerEntity,Integer> {
    SellerEntity findByChatId(Long id);

}
