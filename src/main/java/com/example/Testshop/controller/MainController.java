package com.example.Testshop.controller;

import com.example.Testshop.enums.AdminStep;
import com.example.Testshop.enums.Status;
import com.example.Testshop.service.SellerService;
import com.example.Testshop.TelegramBot;
import com.example.Testshop.util.InlineKeyBoardUtil;
import com.example.Testshop.util.ReplyKeyboardUtil;
import com.example.Testshop.util.UserMap;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.File;
import java.util.*;


@Component
public class MainController {
    @Autowired
    @Lazy
    private TelegramBot myTelegramBot;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private AdminController adminController;

    public void handle(Message message) {

        if (message.getText() != null && message.getText().equals("/start")) {
            UserMap.savesellerStep(message.getChatId(), Status.MENU);

            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Assalamu alaykum! Rosso kompaniyasi Sizni qutlaydi va Siz uchun tayyorlangan bonuslarni taqdim etishga tayyor!\n" +
                    "Bonusli aksiya ishtirokchisiga aylanish uchun ro'yxatdan o'tishingiz kifoya!");
            sendMessage.setChatId(message.getChatId());

            SendMessage sendMessage2 = new SendMessage();
            sendMessage2.setText("Ushbu bot esa Sizga ro'yxatdan o'tishingizga yordam beradi.\uD83D\uDC47");
            sendMessage2.setChatId(message.getChatId());

            SendPhoto sendPhotoRequest = new SendPhoto();
            sendPhotoRequest.setChatId(message.getChatId());
            sendPhotoRequest.setPhoto(new InputFile(new File("base/images/img.png")));


            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            List<InlineKeyboardButton> rowInline = new ArrayList<>();

            // Creating the button
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText("Ro'yxatdan o'tish");
            button.setCallbackData("register");

            rowInline.add(button);
            rowsInline.add(rowInline);
            inlineKeyboardMarkup.setKeyboard(rowsInline);
            sendPhotoRequest.setReplyMarkup(inlineKeyboardMarkup);

            myTelegramBot.sendMsg(sendMessage);
            myTelegramBot.sendMsg(sendMessage2);
            myTelegramBot.sendPhoto(sendPhotoRequest);
        } else if (message.getText() != null && message.getText().equals("/login")) {
            myTelegramBot.sendMessage(message.getChatId(), "Parolni kiriting :");
            UserMap.saveAdminStep(message.getChatId(), AdminStep.LOGIN);
        } else if (UserMap.getAdminStep(message.getChatId()) != null) {
            switch (UserMap.getAdminStep(message.getChatId())) {
                case LOGIN -> adminController.login(message);
                case MENU -> adminController.menu(message.getChatId());
            }
        } else if (UserMap.getCurrentStep(message.getChatId()) != null) {
            switch (UserMap.getCurrentStep(message.getChatId())) {
                case REGIS -> {
                    regis(message);
                }
                case NAME -> {
                }
                case PHONE -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("name", message.getText());
                    UserMap.list.add(map);
                    phoneCreate(message.getChatId());
                }

                case REGION -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("phone", String.valueOf(message.getContact().getPhoneNumber()));
                    UserMap.list.add(map);
                    regionSelect(message.getChatId());
                }
                case SHOPNAME -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("region", message.getText());
                    UserMap.list.add(map);
                    shopName(message.getChatId());
                }
                case LOCATION -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("shopName", message.getText());
                    UserMap.list.add(map);
                    location(message);
                }
                case CARD -> {
                    card(message);
                }
            }
        }

    }

    private void regis(Message message) {
        Map<String, String> map = new HashMap<>();
        map.put("card", message.getText());
        UserMap.list.add(map);

        List<Map<String, String>> result = UserMap.list;
        String re = "name : " + result.get(0).get("name") +
                "\nphone : " + result.get(1).get("phone") +
                "\nregion : " + result.get(2).get("region") +
                "\nshopName : " + result.get(3).get("shopName") +
                "\ncard : " + result.get(5).get("card");

        SendLocation sendLocation = new SendLocation();
        sendLocation.setChatId(message.getChatId());
        sendLocation.setLatitude(Double.valueOf(result.get(4).get("latitude")));
        sendLocation.setLongitude(Double.valueOf(result.get(4).get("longitude")));
        UserMap.savesellerStep(message.getChatId(), Status.CHECK_TIME);
        myTelegramBot.sendMessage(message.getChatId(), re);
        myTelegramBot.sendMsg(sendLocation);
        myTelegramBot.sendMessage("Ma`lumotlaringiz to'gri ekanligini tasdiqlang ", message.getChatId(), InlineKeyBoardUtil.checkRegis());

    }

    private void card(Message message) {
        Location location = message.getLocation();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Map<String, String> map = new HashMap<>();
        map.put("latitude", String.valueOf(latitude));
        map.put("longitude", String.valueOf(longitude));
        UserMap.list.add(map);
        UserMap.savesellerStep(message.getChatId(), Status.REGIS);
        myTelegramBot.sendMessage(message.getChatId(), "Plastik karta raqamingizni kiriting:");
    }

    private void location(Message message) {
        UserMap.savesellerStep(message.getChatId(), Status.CARD);
        myTelegramBot.sendMessage(message.getChatId(), "Lokatsiyangizni yuboring:");
    }

    private void shopName(Long chatId) {
        UserMap.savesellerStep(chatId, Status.LOCATION);
        myTelegramBot.sendMessage(chatId, "Bozor va Do`koningiz nomini kiriting:");
    }

    private void regionSelect(Long chatId) {
        UserMap.savesellerStep(chatId, Status.SHOPNAME);
        myTelegramBot.sendMessage("O'z viloyatingizni kiriting!", chatId, ReplyKeyboardUtil.region());
    }

    private void phoneCreate(Long chatId) {
        UserMap.savesellerStep(chatId, Status.REGION);
        myTelegramBot.sendMessage("Iltimos, telefon raqamingizni yuboring:", chatId, ReplyKeyboardUtil.phone());
    }
}
