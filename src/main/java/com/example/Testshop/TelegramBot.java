package com.example.Testshop;

import com.example.Testshop.config.BotConfig;
import com.example.Testshop.controller.CallBackController;
import com.example.Testshop.controller.MainController;
import com.example.Testshop.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig botConfig;

    public TelegramBot(BotConfig botConfig, MainController mainController, CallBackController callBackController,UserRepository userRepository) {
        super(botConfig.getToken());
        this.botConfig = botConfig;
        this.mainController = mainController;
        this.callBackController = callBackController;
        this.userRepository = userRepository;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    private final MainController mainController;

    private final CallBackController callBackController;
    private final UserRepository userRepository;


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
                System.out.println("my telegram hato");
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
    public File sendMsg(GetFile getFile) {
        try {
            return execute(getFile);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer sendMsg(SendLocation method) {
        try {
            Message sentMessage = execute(method);
            return sentMessage.getMessageId();
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    public void send(long chatId,ReplyKeyboardMarkup replyKeyboardMarkup){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setReplyMarkup(new ForceReplyKeyboard());

        try {
            Message sentMessage = execute(message);
            return sentMessage.getMessageId();

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
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

    public void sendMsg(Long chatId, InputFile excelFile) {
        SendDocument sendDocumentRequest = new SendDocument();
        sendDocumentRequest.setChatId(chatId);
        sendDocumentRequest.setDocument(excelFile);
        try {
            execute(sendDocumentRequest);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(DeleteMessage deleteMessage) {
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
