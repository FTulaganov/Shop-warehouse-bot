package com.example.Testshop.controller;

import com.example.Testshop.TelegramBot;
import com.example.Testshop.entity.SellerEntity;
import com.example.Testshop.entity.UserEntity;
import com.example.Testshop.enums.AdminStep;
import com.example.Testshop.enums.Status;
import com.example.Testshop.repository.UserRepository;
import com.example.Testshop.service.SellerService;
import com.example.Testshop.util.InlineKeyBoardUtil;
import com.example.Testshop.util.UserMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Component
public class AdminController {
    @Autowired
    @Lazy
    private TelegramBot myTelegramBot;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SellerService sellerService;

    public void login(Message message) {
        List<UserEntity> userEntity = userRepository.findAll();
        if (message.getText().equals(userEntity.get(0).getPassword())) {
            UserMap.saveAdminStep(message.getChatId(), AdminStep.MENU);
            updateAdmin(message.getChatId());
            myTelegramBot.sendMessage(message.getChatId(), "Hush kelibsiz");
            menu(message.getChatId());
        } else {
            myTelegramBot.sendMessage(message.getChatId(), "Parol xato ");
        }
    }

    private void updateAdmin(Long chatId) {
        List<UserEntity> userEntity = userRepository.findAll();
        userEntity.get(0).setChatId(chatId);
        userRepository.save(userEntity.get(0));
    }

    public void menu(Long id) {
        List<SellerEntity> list = sellerService.getAllCheckSeller();
        for (SellerEntity entity : list) {
            String re = "name : " + entity.getName() +
                    "\nphone : " + entity.getPhone() +
                    "\nregion : " + entity.getRegion() +
                    "\nshopName : " + entity.getShopName() +
                    "\ncard : " + entity.getCard();
            SendLocation sendLocation = new SendLocation();
            sendLocation.setChatId(id);
            sendLocation.setLatitude(entity.getLocation_latitude());
            sendLocation.setLongitude(entity.getLocation_longitude());
            myTelegramBot.sendMessage(id, re);
            myTelegramBot.sendMsg(sendLocation);
            myTelegramBot.sendMessage("Sotuvchilar qatoriga qoshasizmi? ", id, InlineKeyBoardUtil.chekAdmin());
        }
    }

    public Long getAdminId() {
        List<UserEntity> entity = userRepository.findAll();
        return entity.get(0).getChatId();
    }
}
