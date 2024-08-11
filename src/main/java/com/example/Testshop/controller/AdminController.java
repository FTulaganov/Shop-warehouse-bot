package com.example.Testshop.controller;

import com.example.Testshop.TelegramBot;
import com.example.Testshop.entity.UserEntity;
import com.example.Testshop.enums.AdminStep;
import com.example.Testshop.repository.UserRepository;
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
            myTelegramBot.sendMessage(message.getChatId(), "Hush kelibsiz");
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
            showGoodsMenu(chatId);
        } else if (text.equals("Sotuvchilar Menu")) {
            showSellerMenu(chatId);
        } else if (text.equals("Exit")) {
            myTelegramBot.sendMessage(chatId, "Bizning Telegram botimizdan foydalangiz uchun raxmat!");
            UserMap.clearAdminStep(chatId);
        } else {
            handleGoodsAndSellersActions(chatId, text);
        }
    }

    private void showGoodsMenu(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Maxsulotlar Menusi:\n1. Maxsulot qo`shish\n2. Maxsulotni o`zgartirish\n3. Maxsulotni o`chirish\n4. Maxsulotlar ro`yxati\n5. Exit\n6.Asosiy Menuga qaytish");

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        row.add("Maxsulot qo`shish ");
        row.add("Maxsulotni o`zgartirish");
        row.add("Maxsulotni o`chirish ");
        keyboard.add(row);

        row = new KeyboardRow();
        row.add("Maxsulotlar ro`yxati");
        row.add("Asosiy Menuga qaytish");
        keyboard.add(row);

        row = new KeyboardRow();
        row.add("Exit");
        keyboard.add(row);

        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        myTelegramBot.sendMsg(sendMessage);
        UserMap.saveAdminStep(chatId, AdminStep.GOODS_MENU);
    }

    private void showSellerMenu(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Sotuvchilar Menusi:\n1. Sotuvchi qo`shish\n2.Sotuvchini o`chirish\n3.Passiv Sotuvchilar (min bonus %)\n4. Aktiv Sotuvchilar (most bonus %)\n5. Exit\n6.Asosiy Menuga qaytish");
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Sotuvchi qo`shish");
        row.add("Delete Seller");
        keyboard.add(row);

        row = new KeyboardRow();
        row.add("Passiv Sotuvchilar (min bonus %)");
        row.add("Aktiv Sotuvchilar (most bonus %)");
        keyboard.add(row);

        row = new KeyboardRow();
        row.add("Asosiy Menuga qaytish");
        row.add("Exit");
        keyboard.add(row);

        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);

        myTelegramBot.sendMsg(sendMessage);
        UserMap.saveAdminStep(chatId, AdminStep.SELLERS_MENU);
    }

    private void handleGoodsAndSellersActions(Long chatId, String text) {
        switch (UserMap.getAdminStep(chatId)) {
            case GOODS_MENU -> {
                switch (text) {
                    case "qo`shish Maxsulotlar" -> myTelegramBot.sendMessage(chatId, "Maxsulotning ma`lumotlarini kiriting.");
                    case "Maxsulot ma`lumotlarini o`zgartirish" -> myTelegramBot.sendMessage(chatId, "Maxsulotning S/R ni kiriting.");
                    case "Maxsulotni ma`lumotlarini o`chirish" -> myTelegramBot.sendMessage(chatId, "Maxsulotning S/R ni kiriting.");
                    case "Maxsulotlar ro`yxati" -> myTelegramBot.sendMessage(chatId, "Maxsulotlar ro`yxati.");
                    case "Asosiy Menuga qaytish" -> showMainMenu(chatId);
                    case "Exit" -> myTelegramBot.sendMessage(chatId, "Bizning Telegram botimizdan foydalangiz uchun raxmat!");
                }
            }
            case SELLERS_MENU -> {
                switch (text) {
                    case "Sotuvchi qo`shish" -> myTelegramBot.sendMessage(chatId, "Sotuvchining ma`lumotlarini kiriting.");
                    case "Sotuvchining ma`lumotlarini o`chirish" -> myTelegramBot.sendMessage(chatId, "Sotuvchining ID sini kiriting.");
                    case "Passiv Sotuvchilar (min bonus %)" -> myTelegramBot.sendMessage(chatId, "Eng kam bonus yiqqan Sotuvchilarning ro`yxati.");
                    case "Aktiv Sotuvchilar (most bonus %)" -> myTelegramBot.sendMessage(chatId, "Eng ko`p bonus yiqqan Sotuvchilarning ro`yxati.");
                    case "Asosiy Menuga qaytish" -> showMainMenu(chatId);
                    case "Exit" -> myTelegramBot.sendMessage(chatId, "Bizning Telegram botimizdan foydalangiz uchun raxmat!");
                }
            }
        }
    }

    public void menu(Long chatId) {

    }
}