package com.example.Testshop.util;

import com.example.Testshop.dto.SellerDto;
import com.example.Testshop.enums.AdminStep;
import com.example.Testshop.enums.Status;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserMap {
    private static Map<Long, Status> sellerStep = new HashMap<>();
    private static Map<Long, AdminStep> adminStep = new HashMap<>();

    public static List<Map<String, String>> list = new LinkedList<>();

    public static Status getCurrentStep(Long userId) {
        return sellerStep.get(userId);
    }

    public static AdminStep getAdminStep(Long userId) {
        return adminStep.get(userId);
    }

    public static void savesellerStep(Long userId, Status step) {
        sellerStep.put(userId, step);
    }

    public static void saveAdminStep(Long userId, AdminStep step) {
        adminStep.put(userId, step);
    }

    public static SellerDto getDTO() {
        SellerDto dto = new SellerDto();
        dto.setName(list.get(0).get("name"));
        dto.setPhone(list.get(1).get("phone"));
        dto.setRegion(list.get(2).get("region"));
        dto.setShopName(list.get(3).get("shopName"));
        dto.setCard(list.get(5).get("card"));
        dto.setLatitude(Double.valueOf(list.get(4).get("latitude")));
        dto.setLongitude(Double.valueOf(list.get(4).get("longitude")));
        return dto;
    }


    public static void clearAdminStep(Long chatId) {

    }
}
