package com.example.Testshop.service;

import com.example.Testshop.dto.GoodsDto;
import com.example.Testshop.entity.GoodsEntity;
import com.example.Testshop.entity.ModelEntity;
import com.example.Testshop.repository.GoodsRepository;
import com.example.Testshop.repository.ModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GoodsService {

    private final GoodsRepository goodsRepository;
    private final ModelRepository modelRepository;

    // Save Goods
    public void saveGoods(GoodsDto dto) {
        ModelEntity entity = modelRepository.findById(dto.getModel()).get();
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setBonus(dto.getBonus());
        modelRepository.save(entity);
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
    public Boolean deleteGoods(String codeItem) {
        GoodsEntity entity = getGoods(codeItem);
        if (entity != null) {
            goodsRepository.deleteById(codeItem);
            modelRepository.deleteById(entity.getModelId());
            return true;
        }
        return false;
    }

    // List all Goods
    public List<GoodsEntity> listGoods() {
        return goodsRepository.findAll();
    }

    public List<GoodsEntity> getAll() {
        return goodsRepository.findAll();
    }

    public String getModel(Integer modelId) {
        Optional<ModelEntity> model = modelRepository.findById(modelId);
        return model.get().getModel();
    }
}

