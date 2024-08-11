package com.example.Testshop.controller;

import com.example.Testshop.TelegramBot;
import com.example.Testshop.entity.UserEntity;
import com.example.Testshop.enums.AdminStep;
import com.example.Testshop.repository.UserRepository;
import com.example.Testshop.util.ReplyKeyboardUtil;
import com.example.Testshop.util.UserMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;


@Component
public class AdminController {
    @Autowired
    @Lazy
    private TelegramBot myTelegramBot;
    @Autowired
    private UserRepository userRepository;

    public void login(Message message) {
        List<UserEntity> userEntity = userRepository.findAll();
        if (message.getText().equals(userEntity.get(0).getPassword())) {
            UserMap.saveAdminStep(message.getChatId(), AdminStep.MENU);
            updateAdmin(message.getChatId());
            myTelegramBot.sendMessage("Asosiy sahifaga Hush kelibsiz", message.getChatId(), ReplyKeyboardUtil.menu());
        } else {
            myTelegramBot.sendMessage(message.getChatId(), "Parol xato ");
        }
    }

    private void updateAdmin(Long chatId) {
        List<UserEntity> userEntity = userRepository.findAll();
        userEntity.get(0).setChatId(chatId);
        userRepository.save(userEntity.get(0));
    }

    private void showMainMenu(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Asosiy Menu:\n1. Maxsulotlar Menusi\n2. Sotuvchilar Menusi\n3. Exit");

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        row.add("Maxsulotlar Menusi");
        row.add("Sotuvchilar Menusi");
        keyboard.add(row);

        row = new KeyboardRow();
        row.add("Exit");
        keyboard.add(row);

        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        myTelegramBot.sendMsg(sendMessage);
        UserMap.saveAdminStep(chatId, AdminStep.MAIN_MENU);
    }

    private void handleOwnerMenuActions(Message message) {
        String text = message.getText();
        Long chatId = message.getChatId();

        if (text.equals("Maxsulotlar Menusi")) {
            myTelegramBot.sendMessage("Mahsulotlar menusi", message.getChatId(), ReplyKeyboardUtil.showGoodsMenu());
            UserMap.saveAdminStep(message.getChatId(), AdminStep.GOODS_MENU);
        } else if (text.equals("Sotuvchilar Menusi")) {
            myTelegramBot.sendMessage("Sotuvchilar Menusi:", message.getChatId(), ReplyKeyboardUtil.showSellerMenu());
            UserMap.saveAdminStep(message.getChatId(), AdminStep.SELLERS_MENU);
        } else if (text.equals("Exit")) {
            myTelegramBot.sendMessage(chatId, "Bizning Telegram botimizdan foydalangiz uchun raxmat!");
            UserMap.clearAdminStep(chatId);
        }
    }

    public void menu(Message message) {
        handleOwnerMenuActions(message);
    }

    public void goodsMenu(Message message) {
        String text = message.getText();
        Long id = message.getChatId();
        switch (text) {
            case "qo`shish Maxsulotlar" -> myTelegramBot.sendMessage(id, "Maxsulotning ma`lumotlarini kiriting.");
            case "Maxsulot ma`lumotlarini o`zgartirish" ->
                    myTelegramBot.sendMessage(id, "Maxsulotning S/R ni kiriting.");
            case "Maxsulotni ma`lumotlarini o`chirish" ->
                    myTelegramBot.sendMessage(id, "Maxsulotning S/R ni kiriting.");
            case "Maxsulotlar ro`yxati" -> myTelegramBot.sendMessage(id, "Maxsulotlar ro`yxati.");
            case "Asosiy Menuga qaytish" -> {
                myTelegramBot.sendMessage("Asosiy sahifaga Hush kelibsiz", message.getChatId(), ReplyKeyboardUtil.menu());
                UserMap.saveAdminStep(id, AdminStep.MENU);
            }

        }
        myTelegramBot.sendMessage("Asosiy sahifaga Hush kelibsiz", message.getChatId(), ReplyKeyboardUtil.menu());

    }

    public void sellersMenu(Message message) {
        String text = message.getText();
        Long id = message.getChatId();
        switch (text) {
            case "Sotuvchi qo`shish" -> myTelegramBot.sendMessage(id, "Sotuvchining ma`lumotlarini kiriting.");
            case "Sotuvchining ma`lumotlarini o`chirish" ->
                    myTelegramBot.sendMessage(id, "Sotuvchining ID sini kiriting.");
            case "Passiv Sotuvchilar (min bonus %)" ->
                    myTelegramBot.sendMessage(id, "Eng kam bonus yiqqan Sotuvchilarning ro`yxati.");
            case "Aktiv Sotuvchilar (most bonus %)" ->
                    myTelegramBot.sendMessage(id, "Eng ko`p bonus yiqqan Sotuvchilarning ro`yxati.");
            case "Asosiy Menuga qaytish" -> {
                myTelegramBot.sendMessage("Asosiy sahifaga Hush kelibsiz", message.getChatId(), ReplyKeyboardUtil.menu());
                UserMap.saveAdminStep(id, AdminStep.MENU);
            }
        }
    }
}