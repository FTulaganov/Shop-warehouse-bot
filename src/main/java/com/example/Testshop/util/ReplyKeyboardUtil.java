package com.example.Testshop.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.LinkedList;
import java.util.List;

public class ReplyKeyboardUtil {
    public static KeyboardButton button(String text) {
        KeyboardButton button = new KeyboardButton();
        button.setText(text);
        return button;
    }
    public static ReplyKeyboardMarkup region() {
        KeyboardButton region1 = button("Tashkent");
        KeyboardButton region2 = button("Andijan");
        KeyboardButton region3 = button("Bukhara");
        KeyboardButton region4 = button("Fergana");
        KeyboardButton region5 = button("Jizzakh");
        KeyboardButton region6 = button("Namangan ");
        KeyboardButton region7 = button("Navoiy ");
        KeyboardButton region8 = button("Kashkadarya ");
        KeyboardButton region9 = button("Samarkand  ");
        KeyboardButton region10 = button("Syrdarya  ");
        KeyboardButton region11 = button("Tashkent 2  ");
        KeyboardButton region12 = button("Khorezm  ");
        KeyboardButton region13 = button("Karakalpakstan   ");
        KeyboardRow row1 = new KeyboardRow();
        row1.add(region1);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(region2);
        row2.add(region3);
        KeyboardRow row3 = new KeyboardRow();
        row3.add(region4);
        row3.add(region5);
        KeyboardRow row4 = new KeyboardRow();
        row4.add(region6);
        row4.add(region7);
        KeyboardRow row5 = new KeyboardRow();
        row5.add(region8);
        row5.add(region9);
        KeyboardRow row6 = new KeyboardRow();
        row6.add(region10);
        row6.add(region11);
        KeyboardRow row7 = new KeyboardRow();
        row7.add(region12);
        row7.add(region13);

        List<KeyboardRow> rowList = new LinkedList<>();
        rowList.add(row1);
        rowList.add(row2);
        rowList.add(row3);
        rowList.add(row4);
        rowList.add(row5);
        rowList.add(row6);
        rowList.add(row7);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rowList);
        replyKeyboardMarkup.setResizeKeyboard(true);//buttonni razmerini to'g'irlaydi
        replyKeyboardMarkup.setSelective(true);// bottinga strelka qoshadi;
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }
    public static ReplyKeyboardMarkup phone(){
        KeyboardButton phone = button("Telefon raqamimni yuborish");

        KeyboardRow row1 = new KeyboardRow();
        phone.setRequestContact(true);
        row1.add(phone);

        List<KeyboardRow> keyboardRows = new LinkedList<>();
        keyboardRows.add(row1);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboardRows);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }
}
