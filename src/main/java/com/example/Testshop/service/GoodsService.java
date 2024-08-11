package com.example.Testshop.service;

import com.example.Testshop.dto.GoodsDto;
import com.example.Testshop.entity.GoodsEntity;
import com.example.Testshop.repository.GoodsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GoodsService {

    private final GoodsRepository goodsRepository;

    // Save Goods
    public GoodsEntity saveGoods(GoodsDto dto) {
        GoodsEntity entity = new GoodsEntity();
        entity.setCodeItem(dto.getCodeItem());
        entity.setName(dto.getName());
        entity.setModel(dto.getModel());
        entity.setPrice(dto.getPrice());
        entity.setBonus(dto.getBonus());
        goodsRepository.save(entity);
        return entity;
    }


    // Get Goods by ID
    public GoodsEntity getGoods(Long id) {
        return goodsRepository.findById(Math.toIntExact(id)).orElse(null);
    }

    // Update Goods
    public void updateGoods(GoodsDto dto, Long id) {
        GoodsEntity entity = goodsRepository.findById(Math.toIntExact(id)).orElse(null);
        if (entity != null) {
            entity.setCodeItem(dto.getCodeItem());
            entity.setName(dto.getName());
            entity.setModel(dto.getModel());
            entity.setPrice(dto.getPrice());
            entity.setBonus(dto.getBonus());
            entity.setCreatedDate(dto.getCreatedDate() != null ? dto.getCreatedDate() : entity.getCreatedDate());
            goodsRepository.save(entity);
        }
    }


    // Delete Goods
    public void deleteGoods(Long id) {
        goodsRepository.deleteById(Math.toIntExact(id));
    }

    // List all Goods
    public List<GoodsEntity> listGoods() {
        return goodsRepository.findAll();
    }
}

