package com.example.Testshop.controller;

import com.example.Testshop.TelegramBot;
import com.example.Testshop.dto.GoodsDto;
import com.example.Testshop.enums.AdminStep;
import com.example.Testshop.enums.ProductStep;
import com.example.Testshop.enums.Status;
import com.example.Testshop.enums.UserRole;
import com.example.Testshop.service.GoodsService;
import com.example.Testshop.service.SellerService;
import com.example.Testshop.util.ReplyKeyboardUtil;
import com.example.Testshop.util.UserMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.MaybeInaccessibleMessage;

import java.util.List;
import java.util.Map;


@Component
public class CallBackController {
    @Autowired
    @Lazy
    private TelegramBot myTelegramBot;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private GoodsService goodsService;

    public void handle(String text, MaybeInaccessibleMessage message) {
        if (text.equals("register")) {
            startRegistration(message.getChatId());
        } else if (text.equals("true") && UserMap.getCurrentStep(message.getChatId()) == Status.CHECK_TIME) {
            check(message);
        } else if (text.equals("true") && UserMap.getProductStep(message.getChatId()) == ProductStep.ALL) {
            save(message);
        } else if (text.equals("false") && UserMap.getProductStep(message.getChatId()) == ProductStep.ALL) {
            editDelete(message);
        } else if (text.equals("false") && UserMap.getCurrentStep(message.getChatId()) == Status.CHECK_TIME) {
            restartRegis(message);
        } else if (text.equals("xa") && UserMap.getRole(message.getChatId()) == UserRole.ADMIN) {
            deleteMessages(message.getChatId(), UserMap.chatIdList.get(message.getChatId()));
        } else if (UserMap.getAdminStep(message.getChatId()) == AdminStep.ADD_SELLER) {
            if (text.startsWith("false")) {
                Long id = Long.valueOf(text.substring(5));
                myTelegramBot.sendMessage(id, "Kechirasiz arizangiz qabul qilinmadi ");
                sellerService.deleteSeller(id);
                List<Integer> list = UserMap.getChatIdList(id);
                deleteMessages(message.getChatId(), list);

            } else {
                long id = Long.parseLong(text);
                sellerService.signIn(id);
                List<Integer> list = UserMap.getChatIdList(id);
                deleteMessages(message.getChatId(), list);
            }
        }

    }

    private void restartRegis(MaybeInaccessibleMessage message) {
        UserMap.savesellerStep(message.getChatId(), Status.PHONE);
        myTelegramBot.sendMessage(message.getChatId(), "Ismingizni kiriting:");
    }

    private void editDelete(MaybeInaccessibleMessage message) {
        myTelegramBot.sendMessage(message.getChatId(), "Malumotlar saqlanmadi");
        myTelegramBot.sendMessage("Maxsulotning S/R ni kiriting.", message.getChatId(), ReplyKeyboardUtil.back());
        UserMap.saveProductStep(message.getChatId(), ProductStep.START);
    }

    private void save(MaybeInaccessibleMessage message) {
        GoodsDto dto = UserMap.getEditProduct(message.getChatId());
        goodsService.saveGoods(dto);
        myTelegramBot.sendMessage(message.getChatId(), "Malumotlar saqlandi");
        myTelegramBot.sendMessage("Maxsulotning S/R ni kiriting.", message.getChatId(), ReplyKeyboardUtil.back());
        UserMap.saveProductStep(message.getChatId(), ProductStep.START);
    }

    private void check(MaybeInaccessibleMessage message) {
        if (sellerService.getSeller(message.getChatId()) == null) {
            UserMap.savesellerStep(message.getChatId(), Status.SEND_ADMIN);
            sellerService.saveUser(UserMap.getDTO(message.getChatId()), message.getChatId());
            myTelegramBot.sendMessage(message.getChatId(), "Ma`lumotlaringgiz Operatorga yuborildi siz bilan tez orada bog`lanamiz");
        } else {
            UserMap.savesellerStep(message.getChatId(), Status.SEND_ADMIN);
            sellerService.update(UserMap.getDTO(message.getChatId()), message.getChatId());
            myTelegramBot.sendMessage(message.getChatId(), "Ma`lumotlaringgiz Operatorga yuborildi siz bilan tez orada bog`lanamiz");
        }
    }

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
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);
        myTelegramBot.sendMessage(deleteMessage);

    }
}
