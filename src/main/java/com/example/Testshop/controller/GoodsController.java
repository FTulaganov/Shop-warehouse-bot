package com.example.Testshop.controller;


import com.example.Testshop.TelegramBot;
import com.example.Testshop.dto.GoodsDto;
import com.example.Testshop.entity.GoodsEntity;
import com.example.Testshop.entity.ModelEntity;
import com.example.Testshop.enums.Bonus;
import com.example.Testshop.enums.ProductStep;
import com.example.Testshop.service.GoodsService;
import com.example.Testshop.util.InlineKeyBoardUtil;
import com.example.Testshop.util.ReplyKeyboardUtil;
import com.example.Testshop.util.UserMap;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Component
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    @Lazy
    private TelegramBot myTelegramBot;

    public void start(Message message) {
        GoodsEntity entity = goodsService.getGoods(message.getText());
        ModelEntity model = goodsService.getModelEntity(entity.getModelId());
        if (entity != null) {
            GoodsDto dto = new GoodsDto();
            dto.setCodeItem(entity.getCodeItem());
            dto.setModel(entity.getModelId());
            dto.setBonus(model.getBonus());
            dto.setPrice(model.getPrice());
            UserMap.editProduct.put(message.getChatId(), dto);
            myTelegramBot.sendMessage(message.getChatId(), "Nomini kiriting");
            UserMap.saveProductStep(message.getChatId(), ProductStep.ALL);
        } else {
            myTelegramBot.sendMessage(message.getChatId(), "Bu S/R Malumotlar bazasida yoq");
            myTelegramBot.sendMessage("Maxsulotning S/R ni kiriting.", message.getChatId(), ReplyKeyboardUtil.back());

        }
    }


    public void all(Message message) {
        GoodsDto dto = UserMap.getEditProduct(message.getChatId());
        dto.setName(message.getText());
        UserMap.editProduct.put(message.getChatId(), dto);
        String result = "Togri ekanligini tekshiring\n" + "\nS/R : " + dto.getCodeItem() +
                "\nNomi : " + dto.getName();
        myTelegramBot.sendMessage(result, message.getChatId(), InlineKeyBoardUtil.editProduct());
        UserMap.saveProductStep(message.getChatId(), ProductStep.ALL);
    }

    public boolean deleteProduct(Message message) {
        boolean check = goodsService.deleteGoods(message.getText());
        if (check) {
            myTelegramBot.sendMessage(message.getChatId(), "Malumot bazadan muaffaqiyatli o'chirildi");
            myTelegramBot.sendMessage("Maxsulotning S/R ni kiriting.", message.getChatId(), ReplyKeyboardUtil.getProduct());
            return true;
        }
        return false;

    }

    public ByteArrayOutputStream getProduct(Message message) throws IOException {
        List<GoodsEntity> list = goodsService.getAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("S/R");
        headerRow.createCell(1).setCellValue("Model");

        // Ma'lumotlarni qo'shamiz
        int rowNum = 1;
        for (GoodsEntity dto : list) {
            String name = goodsService.getModel(dto.getModelId());
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(dto.getCodeItem());
            row.createCell(1).setCellValue(name);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out;

    }

    public void startBonus(Message message) {
        UserMap.bonus_price.add(message.getText());
        myTelegramBot.sendMessage(message.getChatId(), "Narxini kiriting : ");
        UserMap.saveBonusStep(message.getChatId(), Bonus.ADD_PRICE);
        UserMap.getBonus(message.getChatId());
    }

    public void price(Message message) {
        List<Integer> list = UserMap.bonus;
        for (Integer integer : list) {
            ModelEntity entity = goodsService.getModelEntity(integer);
            entity.setBonus(Double.valueOf(UserMap.bonus_price.get(0)));
            entity.setPrice(Double.valueOf(message.getText()));
            goodsService.saveBonus(entity);
        }
        UserMap.bonus.clear();
        myTelegramBot.sendMessage(message.getChatId(), "Bonuslar qoshildi");
        myTelegramBot.sendMessage("Mahsulotning modelini kiriting : ", message.getChatId(), ReplyKeyboardUtil.back());

    }

    public void deleteBonus(Message message) {
        List<Integer> list = UserMap.bonus;
        for (Integer integer : list) {
            ModelEntity entity = goodsService.getModelEntity(integer);
            entity.setBonus(0d);
            goodsService.saveBonus(entity);
        }
        UserMap.bonus.clear();
        myTelegramBot.sendMessage(message.getChatId(), "Malumot bazadan muaffaqiyatli o'chirildi");
        myTelegramBot.sendMessage("Mahsulotning modelini kiriting.", message.getChatId(), ReplyKeyboardUtil.back());

    }
}
