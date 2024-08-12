package com.example.Testshop.util;

import com.example.Testshop.dto.GoodsDto;
import com.example.Testshop.dto.SellerDto;
import com.example.Testshop.enums.AdminStep;
import com.example.Testshop.enums.ProductStep;
import com.example.Testshop.enums.Status;
import com.example.Testshop.enums.UserRole;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserMap {
    private static Map<Long, Status> sellerStep = new HashMap<>();
    private static Map<Long, AdminStep> adminStep = new HashMap<>();
    private static Map<Long, ProductStep> productStep = new HashMap<>();
    public static Map<Long, GoodsDto> editProduct = new HashMap<>();
    private static Map<Long, UserRole> role = new HashMap<>();

    public static Map<Long, SellerDto> list = new ConcurrentHashMap<>();
    public static Map<Integer, List<Integer>> chatIdList = new HashMap<>();

    public static Status getCurrentStep(Long userId) {
        return sellerStep.get(userId);
    }

    public static AdminStep getAdminStep(Long userId) {
        return adminStep.get(userId);
    }

    public static ProductStep getProductStep(Long userId) {
        return productStep.get(userId);
    }

    public static UserRole getRole(Long userId) {
        return role.get(userId);
    }

    public static void savesellerStep(Long userId, Status step) {
        sellerStep.put(userId, step);
    }

    public static void saveAdminStep(Long userId, AdminStep step) {
        adminStep.put(userId, step);
    }

    public static void saveProductStep(Long userId, ProductStep step) {
        productStep.put(userId, step);
    }

    public static void saveRole(Long userId, UserRole step) {
        role.put(userId, step);
    }

    public static SellerDto getDTO(Long id) {
        return list.get(id);
    }

    public static GoodsDto getEditProduct(Long id) {
        return editProduct.get(id);
    }

    public static void clearAdminStep(Long chatId) {

    }
}
