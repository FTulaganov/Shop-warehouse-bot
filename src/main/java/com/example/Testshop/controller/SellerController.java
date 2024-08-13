package com.example.Testshop.controller;

import com.example.Testshop.TelegramBot;
import com.example.Testshop.dto.SellerDto;
import com.example.Testshop.enums.Status;
import com.example.Testshop.repository.UserRepository;
import com.example.Testshop.util.InlineKeyBoardUtil;
import com.example.Testshop.util.ReplyKeyboardUtil;
import com.example.Testshop.util.UserMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
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

@Controller
public class SellerController {
    @Autowired
    @Lazy
    private TelegramBot myTelegramBot;
    @Autowired
    private UserRepository userRepository;


    public void regis(Message message) {
        SellerDto dto = UserMap.getDTO(message.getChatId());
        dto.setCard(message.getText());
        UserMap.list.put(message.getChatId(), dto);

        SellerDto result = UserMap.getDTO(message.getChatId());
        String re = "Ism : " + result.getName() +
                "\nTelefon : " + result.getPhone() +
                "\nViloyat : " + result.getRegion() +
                "\nBozor,do`kon : " + result.getShopName() +
                "\nKarta raqami : " + result.getCard();

        SendLocation sendLocation = new SendLocation();
        sendLocation.setChatId(message.getChatId());
        sendLocation.setLatitude(result.getLatitude());
        sendLocation.setLongitude(result.getLongitude());
        UserMap.savesellerStep(message.getChatId(), Status.CHECK_TIME);
        myTelegramBot.sendMessage(message.getChatId(), re);
        myTelegramBot.sendMsg(sendLocation);
        myTelegramBot.sendMessage("Ma`lumotlaringiz to'gri ekanligini tasdiqlang ", message.getChatId(), InlineKeyBoardUtil.checkRegis());

    }

    public void phoneCreate(Message message) {
        SellerDto dto = new SellerDto();
        dto.setName(message.getText());
        UserMap.list.put(message.getChatId(), dto);
        UserMap.savesellerStep(message.getChatId(), Status.REGION);
        myTelegramBot.sendMessage("Iltimos, telefon raqamingizni yuboring:", message.getChatId(), ReplyKeyboardUtil.phone());
    }

    public void regionSelect(Message message) {
        SellerDto dto = UserMap.getDTO(message.getChatId());
        dto.setPhone(message.getContact().getPhoneNumber());
        UserMap.list.put(message.getChatId(), dto);
        UserMap.savesellerStep(message.getChatId(), Status.SHOPNAME);
        myTelegramBot.sendMessage("O'z viloyatingizni kiriting!", message.getChatId(), ReplyKeyboardUtil.region());

    }

    public void shopName(Message message) {
        SellerDto dto = UserMap.getDTO(message.getChatId());
        dto.setRegion(message.getText());
        UserMap.list.put(message.getChatId(), dto);
        UserMap.savesellerStep(message.getChatId(), Status.LOCATION);
        myTelegramBot.sendMessage(message.getChatId(), "Bozor va Do`koningiz nomini kiriting:");
    }

    public void location(Message message) {
        SellerDto dto = UserMap.getDTO(message.getChatId());
        dto.setShopName(message.getText());
        UserMap.list.put(message.getChatId(), dto);
        UserMap.savesellerStep(message.getChatId(), Status.CARD);
        myTelegramBot.sendMessage(message.getChatId(), "Lokatsiyangizni yuboring:");
    }

    public void card(Message message) {
        Location location = message.getLocation();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        SellerDto dto = UserMap.getDTO(message.getChatId());
        dto.setLatitude(latitude);
        dto.setLongitude(longitude);
        UserMap.list.put(message.getChatId(), dto);
        UserMap.savesellerStep(message.getChatId(), Status.REGIS);
        myTelegramBot.sendMessage(message.getChatId(), "Plastik karta raqamingizni kiriting:");
    }

    public void start(Message message) {
        UserMap.savesellerStep(message.getChatId(), Status.MENU);

        myTelegramBot.sendMessage(message.getChatId(), "Assalamu alaykum! Rosso kompaniyasi Sizni qutlaydi va Siz uchun tayyorlangan bonuslarni taqdim etishga tayyor!\n" +
                "Bonusli aksiya ishtirokchisiga aylanish uchun ro'yxatdan o'tishingiz kifoya!");
        myTelegramBot.sendMessage(message.getChatId(), "Ushbu bot esa Sizga ro'yxatdan o'tishingizga yordam beradi.\uD83D\uDC47");

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

        myTelegramBot.sendPhoto(sendPhotoRequest);
    }
}
