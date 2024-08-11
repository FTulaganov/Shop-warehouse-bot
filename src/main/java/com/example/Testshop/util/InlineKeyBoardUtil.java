package com.example.Testshop.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.LinkedList;
import java.util.List;

public class InlineKeyBoardUtil {
    public static InlineKeyboardButton button(String text, String callBack) {
        InlineKeyboardButton button = new InlineKeyboardButton(text);
        button.setCallbackData(callBack);
        return button;
    }
    public static InlineKeyboardMarkup checkRegis() {

        List<InlineKeyboardButton> row = new LinkedList<>();
        List<InlineKeyboardButton> row2 = new LinkedList<>();
        InlineKeyboardButton button1 = InlineKeyBoardUtil.button("To'gri", "true");
        InlineKeyboardButton button2 = InlineKeyBoardUtil.button("Xato", "false");
        row.add(button1);
        row2.add(button2);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new LinkedList<>();
        rowList.add(row);
        rowList.add(row2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }


}
