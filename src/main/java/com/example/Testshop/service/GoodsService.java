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

        goodsRepository.save(entity);
        return entity;
    }


    // Get Goods by ID
    public GoodsEntity getGoods(String codeItem) {
        return goodsRepository.findByCodeItem(codeItem).orElse(null);
    }

    // Update Goods
    public void updateGoods(GoodsDto dto, String codeItem) {
        GoodsEntity entity = goodsRepository.findByCodeItem(codeItem).orElse(null);
//        if (entity != null) {
//            entity.setCodeItem(dto.getCodeItem());
//            entity.setName(dto.getName());
//            entity.setModel(dto.getModel());
//            entity.setPrice(dto.getPrice());
//            entity.setBonus(dto.getBonus());
//            entity.setCreatedDate(dto.getCreatedDate() != null ? dto.getCreatedDate() : entity.getCreatedDate());
          goodsRepository.save(entity);
        }



    // Delete Goods
    public void deleteGoods(String codeItem) {
        goodsRepository.deleteByCodeItem(codeItem);
    }

    // List all Goods
    public List<GoodsEntity> listGoods() {
        return goodsRepository.findAll();
    }
}

