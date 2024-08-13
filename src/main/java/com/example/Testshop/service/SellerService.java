package com.example.Testshop.service;

import com.example.Testshop.dto.SellerDto;
import com.example.Testshop.entity.SellerEntity;
import com.example.Testshop.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    // Delete Seller
    public void deleteSeller(Long chatId) {
        SellerEntity entity = sellerRepository.findByChatId(chatId);
        sellerRepository.delete(entity);
    }

    // List Sellers
    public List<SellerEntity> listSellers() {
        return sellerRepository.findAll();
    }

    // List Sellers with at least a certain bonus percentage
    public List<SellerEntity> listSellersWithBonusPercentage(double minBonusPercentage) {
        return sellerRepository.findByBonusPercentageGreaterThanEqual(minBonusPercentage);
    }

    // List Sellers with the highest bonus percentage
    public List<SellerEntity> listTopSellers(int topCount) {
        return sellerRepository.findTopSellersByBonusPercentage(topCount);
    }

    public List<SellerEntity> allSeller() {
        return sellerRepository.findAllByVisibleFalse();
    }

    public void signIn(long id) {
        SellerEntity entity = getSeller(id);
        entity.setVisible(true);
        sellerRepository.save(entity);
    }

    public Boolean getSeller(String text) {
        List<SellerEntity> entities = sellerRepository.findAllByVisibleTrue();
        for (SellerEntity entity : entities) {
            String phone = entity.getPhone();
            if (phone.startsWith("+")) {
                if (phone.equals(text)) {
                    sellerRepository.delete(entity);
                    return true;
                }
            } else {
                String result = text.substring(1);
                if (result.equals(phone)) {
                    sellerRepository.delete(entity);
                    return true;
                }
            }
        }
        return false;
    }
}

