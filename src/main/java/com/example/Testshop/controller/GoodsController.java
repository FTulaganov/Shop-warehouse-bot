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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

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

    public void deleteProduct(Message message) {
        boolean check = goodsService.deleteGoods(message.getText());
        if (check){
            myTelegramBot.sendMessage(message.getChatId(), "Malumot bazadan muaffaqiyatli o'chirildi");
            myTelegramBot.sendMessage("Mahsulotlar menusi", message.getChatId(), ReplyKeyboardUtil.showGoodsMenu());
            UserMap.saveAdminStep(message.getChatId(), AdminStep.GOODS_MENU);
        }

    }
}
