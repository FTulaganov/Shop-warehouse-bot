package com.example.Testshop.controller;

import com.example.Testshop.TelegramBot;
import com.example.Testshop.enums.Status;
import com.example.Testshop.enums.UserRole;
import com.example.Testshop.service.SellerService;
import com.example.Testshop.util.UserMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.MaybeInaccessibleMessage;

import java.util.List;


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
        } else if (text.equals("xa") && UserMap.getRole(message.getChatId()) == UserRole.ADMIN) {
            deleteMessages(message.getChatId(), UserMap.chatIdList.get(message.getChatId()));
        }

    }

    private void check(MaybeInaccessibleMessage message) {
        if (sellerService.getSeller(message.getChatId()) == null) {
            sellerService.saveUser(UserMap.getDTO(), message.getChatId());
            myTelegramBot.sendMessage(message.getChatId(), "Ma`lumotlaringgiz Operatorga yuborildi siz bilan tez orada bog`lanamiz");
        } else {
            sellerService.update(UserMap.getDTO(), message.getChatId());
            myTelegramBot.sendMessage(message.getChatId(), "Ma`lumotlaringgiz Operatorga yuborildi siz bilan tez orada bog`lanamiz");
        }
    }

/*    private void checkAdmin(MaybeInaccessibleMessage message) {
        if () {
            sellerService.saveUser(UserMap.getDTO(), message.getChatId());
            myTelegramBot.sendMessage(message.getChatId(), "Ma`lumotlaringgiz Operatorga yuborildi siz bilan tez orada bog`lanamiz");
        } else {
            sellerService.update(UserMap.getDTO(), message.getChatId());
            myTelegramBot.sendMessage(message.getChatId(), "Ma`lumotlaringgiz Operatorga yuborildi siz bilan tez orada bog`lanamiz");
        }
    }*/

    private void startRegistration(Long chatId) {
        UserMap.savesellerStep(chatId, Status.PHONE);
        myTelegramBot.sendMessage(chatId, "Ismingizni kiriting:");
    }

    public void deleteMessages(Long chatId, List<Integer> messageIds) {
        for (Integer messageId : messageIds) {
            deleteMessage(chatId, messageId);
        }
    }

    public void deleteMessage(Long chatId, Integer messageId) {
        String url = "https://api.telegram.org/bot" + "6960550759:AAELl3_KQsCm5d9DcIOH4GpXJltupiRGDMc" + "/deleteMessage";
        RestTemplate restTemplate = new RestTemplate();

        // Parametrlarni tayyorlash
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("chat_id", chatId.toString());
        params.add("message_id", messageId.toString());

        // So'rovni yuborish
        restTemplate.postForObject(url, params, String.class);
    }
}
