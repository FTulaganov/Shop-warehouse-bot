package com.example.Testshop.controller;


import com.example.Testshop.TelegramBot;
import com.example.Testshop.dto.GoodsDto;
import com.example.Testshop.entity.GoodsEntity;
import com.example.Testshop.enums.AdminStep;
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
        if (entity != null) {
            GoodsDto dto = new GoodsDto();
            dto.setCodeItem(entity.getCodeItem());
            dto.setModel(entity.getModelId());
            UserMap.editProduct.put(message.getChatId(), dto);
            myTelegramBot.sendMessage(message.getChatId(), "Nomini kiriting");
            UserMap.saveProductStep(message.getChatId(), ProductStep.NAME);
        }
    }

    public void all(Message message) {

    }

    public void bonus(Message message) {
        GoodsDto dto = UserMap.getEditProduct(message.getChatId());
        dto.setBonus(Double.valueOf(message.getText()));
        UserMap.editProduct.put(message.getChatId(), dto);
        String result = "Togri ekanligini tekshiring\n" + "\nS/R : " + dto.getCodeItem() +
                "\nNomi : " + dto.getName() +
                "\nNarxi : " + dto.getPrice() +
                "\nBonus : " + dto.getBonus();
        myTelegramBot.sendMessage(result, message.getChatId(), InlineKeyBoardUtil.editProduct());
        UserMap.saveProductStep(message.getChatId(), ProductStep.ALL);

    }

    public void price(Message message) {
        GoodsDto dto = UserMap.getEditProduct(message.getChatId());
        dto.setPrice(Double.valueOf(message.getText()));
        UserMap.editProduct.put(message.getChatId(), dto);
        myTelegramBot.sendMessage(message.getChatId(), "Bonus kiriting");
        UserMap.saveProductStep(message.getChatId(), ProductStep.BONUS);
    }

    public void name(Message message) {
        GoodsDto dto = UserMap.getEditProduct(message.getChatId());
        dto.setName(message.getText());
        UserMap.editProduct.put(message.getChatId(), dto);
        myTelegramBot.sendMessage(message.getChatId(), "Narxini kiriting");
        UserMap.saveProductStep(message.getChatId(), ProductStep.PRICE);
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

    public boolean check(String id) {
        GoodsEntity entity = goodsService.getGoods(id);
        return entity != null;
    }
}
