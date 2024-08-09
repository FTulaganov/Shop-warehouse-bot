package com.example.Testshop.controller;

import com.example.Testshop.TelegramBot;
import com.example.Testshop.enums.Status;
import com.example.Testshop.service.SellerService;
import com.example.Testshop.util.UserMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.MaybeInaccessibleMessage;


@Component
public class CallBackController {
    @Autowired
    @Lazy
    private TelegramBot myTelegramBot;
    @Autowired
    private SellerService sellerService;

    public void handle(String text, MaybeInaccessibleMessage message) {
        if (text.equals("register")) {
            startRegistration(message.getChatId());
        } else if (text.equals("true")) {
            check(message);
        }

    }

    private void check(MaybeInaccessibleMessage message) {
        if (sellerService.getSeller(message.getChatId()) == null) {
            sellerService.saveUser(UserMap.getDTO(), message.getChatId());
            myTelegramBot.sendMessage(message.getChatId(), "malumotlaringgiz Operatorga yuborildi siz bilan tez orada bo'glanmiz");
        }
        else {
            sellerService.update(UserMap.getDTO(), message.getChatId());
            myTelegramBot.sendMessage(message.getChatId(), "malumotlaringgiz Operatorga yuborildi siz bilan tez orada bo'glanmiz");
        }
    }

    private void startRegistration(Long chatId) {
        UserMap.savesellerStep(chatId, Status.PHONE);
        myTelegramBot.sendMessage(chatId, "Please enter your name:");
    }
}
