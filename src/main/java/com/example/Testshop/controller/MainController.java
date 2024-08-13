package com.example.Testshop.controller;

import com.example.Testshop.TelegramBot;
import com.example.Testshop.enums.AdminStep;
import com.example.Testshop.enums.Status;
import com.example.Testshop.service.SellerService;
import com.example.Testshop.util.InlineKeyBoardUtil;
import com.example.Testshop.util.ReplyKeyboardUtil;
import com.example.Testshop.util.UserMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class MainController {
    @Autowired
    @Lazy
    private TelegramBot myTelegramBot;
    @Autowired
    private AdminController adminController;
    @Autowired
    private SellerController sellerController;

    public void handle(Message message) {

        if (message.getText() != null && message.getText().equals("/start")) {
            UserMap.savesellerStep(message.getChatId(), Status.MENU);
            sellerController.start(message);
        } else if (message.getText() != null && message.getText().equals("/login")) {
            myTelegramBot.sendMessage(message.getChatId(), "Parolni kiriting :");
            UserMap.saveAdminStep(message.getChatId(), AdminStep.LOGIN);
        } else if (UserMap.getAdminStep(message.getChatId()) != null) {
            switch (UserMap.getAdminStep(message.getChatId())) {
                case LOGIN -> adminController.login(message);
                case MENU -> adminController.menu(message);
                case GOODS_MENU -> adminController.goodsMenu(message);
                case SELLERS_MENU -> adminController.sellersMenu(message);
                case ADD_PRODUCT -> adminController.addProduct(message);
                case EDIT_PRODUCT -> adminController.editProduct(message);
                case DELETE_PRODUCT -> adminController.deleteProduct(message);
                case GET_PRODUCT -> adminController.getProduct(message);
                case ADD_SELLER -> adminController.add(message);
                case DELETE_SELLER -> adminController.deleteSeller(message);
            }
        } else if (UserMap.getCurrentStep(message.getChatId()) != null) {
            switch (UserMap.getCurrentStep(message.getChatId())) {
                case REGIS -> sellerController.regis(message);
                case NAME -> {
                }
                case PHONE -> sellerController.phoneCreate(message);
                case REGION -> sellerController.regionSelect(message);
                case SHOPNAME -> sellerController.shopName(message);
                case LOCATION -> sellerController.location(message);
                case CARD -> sellerController.card(message);
            }
        }

    }


}
