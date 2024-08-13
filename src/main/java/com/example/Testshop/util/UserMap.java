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

    /// seller

    private static Map<Long, Status> sellerStep = new HashMap<>();
    public static Map<Long, SellerDto> list = new ConcurrentHashMap<>();

    public static Status getCurrentStep(Long userId) {
        return sellerStep.get(userId);
    }

    public static void savesellerStep(Long userId, Status step) {
        sellerStep.put(userId, step);
    }

    public static SellerDto getDTO(Long id) {
        return list.get(id);
    }


    /// admin

    private static Map<Long, AdminStep> adminStep = new HashMap<>();

    public static AdminStep getAdminStep(Long userId) {
        return adminStep.get(userId);
    }

    public static void saveAdminStep(Long userId, AdminStep step) {
        adminStep.put(userId, step);
    }

    /// product

    private static Map<Long, ProductStep> productStep = new HashMap<>();
    public static Map<Long, GoodsDto> editProduct = new HashMap<>();

    public static ProductStep getProductStep(Long userId) {
        return productStep.get(userId);
    }

    public static GoodsDto getEditProduct(Long id) {
        return editProduct.get(id);
    }

    public static void saveProductStep(Long userId, ProductStep step) {
        productStep.put(userId, step);
    }

    /// chat

    public static Map<Long, List<Integer>> chatIdList = new HashMap<>();
    public static List<Integer> getChatIdList(Long id){
        return chatIdList.get(id);
    }

    public static UserRole getRole(Long userId) {
        return role.get(userId);
    }

    public static void saveRole(Long userId, UserRole step) {
        role.put(userId, step);
    }

    private static Map<Long, UserRole> role = new HashMap<>();
}
