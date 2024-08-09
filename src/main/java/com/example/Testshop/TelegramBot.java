package com.example.Testshop;

import com.example.Testshop.config.BotConfig;
import com.example.Testshop.controller.CallBackController;
import com.example.Testshop.controller.MainController;
import com.example.Testshop.entity.UserEntity;
import com.example.Testshop.enums.UserRole;
import com.example.Testshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig botConfig;

    public TelegramBot(BotConfig botConfig, MainController mainController, CallBackController callBackController) {
        super(botConfig.getToken());
        this.botConfig = botConfig;
        this.mainController = mainController;
        this.callBackController = callBackController;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    private final MainController mainController;

    private final CallBackController callBackController;
    @Autowired
    private UserRepository userRepository;


    @Override
    public void onUpdateReceived(Update update) {

        try {
            if (update.hasMessage()) {
                Message message = update.getMessage();
                mainController.handle(message);
            } else if (update.hasCallbackQuery()) {
                System.out.println(update);
                CallbackQuery callbackQuery = update.getCallbackQuery();
                String data = callbackQuery.getData();
                callBackController.handle(data, callbackQuery.getMessage());
            } else {
                System.out.println("my telegram hatto");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public Message sendMsg(SendMessage method) {
        try {
            return execute(method);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public Location sendMsg(SendLocation method) {
        try {
            return execute(method).getLocation();
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setReplyMarkup(new ForceReplyKeyboard());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String text, Long sendProfileId, ReplyKeyboardMarkup replyKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(sendProfileId);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMsg(sendMessage);
    }

    public Message sendMessage(String text, Long sendProfileId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(sendProfileId);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMsg(sendMessage);
    }

    public void sendPhoto(SendPhoto send) {
        try {
            execute(send);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
