package com.example.Testshop.service;

import com.example.Testshop.SellerDto;
import com.example.Testshop.entity.SellerEntity;
import com.example.Testshop.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SellerService {

    private final SellerRepository sellerRepository;

    public SellerEntity saveUser(SellerDto dto, Long chatId) {
        SellerEntity entity = new SellerEntity();
        entity.setChatId(chatId);
        entity.setName(dto.getName());
        entity.setPhone(dto.getPhone());
        entity.setShopName(dto.getShopName());
        entity.setRegion(dto.getRegion());
        entity.setCard(dto.getCard());
        entity.setLocation_latitude(dto.getLatitude());
        entity.setLocation_longitude(dto.getLongitude());
        sellerRepository.save(entity);
        return entity;
    }


    public SellerEntity getSeller(Long chatId) {
        return sellerRepository.findByChatId(chatId);
    }

    public void update(SellerDto dto, Long chatId) {
        SellerEntity entity = sellerRepository.findByChatId(chatId);
        entity.setName(dto.getName());
        entity.setPhone(dto.getPhone());
        entity.setShopName(dto.getShopName());
        entity.setRegion(dto.getRegion());
        entity.setCard(dto.getCard());
        entity.setLocation_latitude(dto.getLatitude());
        entity.setLocation_longitude(dto.getLongitude());
        sellerRepository.save(entity);
    }
}
