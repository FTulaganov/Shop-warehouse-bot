package com.example.Testshop.controller;

import com.example.Testshop.TelegramBot;
import com.example.Testshop.enums.Status;
import com.example.Testshop.repository.UserRepository;
import com.example.Testshop.util.InlineKeyBoardUtil;
import com.example.Testshop.util.ReplyKeyboardUtil;
import com.example.Testshop.util.UserMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SellerController {
    @Autowired
    @Lazy
    private TelegramBot myTelegramBot;
    @Autowired
    private UserRepository userRepository;


    public void regis(Message message) {
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

    public void phoneCreate(Message message) {
        Map<String, String> map = new HashMap<>();
        map.put("name", message.getText());
        UserMap.list.add(map);
        UserMap.savesellerStep(message.getChatId(), Status.REGION);
        myTelegramBot.sendMessage("Iltimos, telefon raqamingizni yuboring:", message.getChatId(), ReplyKeyboardUtil.phone());
    }

    public void regionSelect(Message message) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", String.valueOf(message.getContact().getPhoneNumber()));
        UserMap.list.add(map);
        UserMap.savesellerStep(message.getChatId(), Status.SHOPNAME);
        myTelegramBot.sendMessage("O'z viloyatingizni kiriting!", message.getChatId(), ReplyKeyboardUtil.region());

    }

    public void shopName(Message message) {
        Map<String, String> map = new HashMap<>();
        map.put("region", message.getText());
        UserMap.list.add(map);
        UserMap.savesellerStep(message.getChatId(), Status.LOCATION);
        myTelegramBot.sendMessage(message.getChatId(), "Bozor va Do`koningiz nomini kiriting:");
    }

    public void location(Message message) {
        Map<String, String> map = new HashMap<>();
        map.put("shopName", message.getText());
        UserMap.list.add(map);
        UserMap.savesellerStep(message.getChatId(), Status.CARD);
        myTelegramBot.sendMessage(message.getChatId(), "Lokatsiyangizni yuboring:");
    }

    public void card(Message message) {
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
}
