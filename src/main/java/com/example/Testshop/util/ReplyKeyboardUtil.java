package com.example.Testshop.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ReplyKeyboardUtil {
    public static KeyboardButton button(String text) {
        KeyboardButton button = new KeyboardButton();
        button.setText(text);
        return button;
    }

    public static ReplyKeyboardMarkup region() {
        KeyboardButton region1 = button("Toshkent");
        KeyboardButton region2 = button("Andijon");
        KeyboardButton region3 = button("Buxoro");
        KeyboardButton region4 = button("Farg`ona");
        KeyboardButton region5 = button("Jizzax");
        KeyboardButton region6 = button("Namangan ");
        KeyboardButton region7 = button("Navoiy ");
        KeyboardButton region8 = button("Qashqadaryo ");
        KeyboardButton region9 = button("Samarqand  ");
        KeyboardButton region10 = button("Sirdaryo  ");
        KeyboardButton region11 = button("Toshkent vil  ");
        KeyboardButton region12 = button("Xorazm  ");
        KeyboardButton region13 = button("Qoraqalpogʻiston Res  ");
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

    public static ReplyKeyboardMarkup phone() {
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

    public static ReplyKeyboardMarkup menu() {
        KeyboardButton product = button("Maxsulotlar Menusi");
        KeyboardButton sellers = button("Sotuvchilar Menusi");
        KeyboardButton exit = button("Exit");

        KeyboardRow row1 = new KeyboardRow();
        row1.add(product);
        row1.add(sellers);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(exit);

        List<KeyboardRow> keyboardRows = new LinkedList<>();
        keyboardRows.add(row1);
        keyboardRows.add(row2);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboardRows);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    public static ReplyKeyboardMarkup showGoodsMenu() {
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("Maxsulot qo`shish ");
        row1.add("Maxsulotni o`zgartirish");
        row1.add("Maxsulotni o`chirish ");
        keyboard.add(row1);

        KeyboardRow row2 = new KeyboardRow();
        row2.add("Maxsulotlar ro`yxati");
        row2.add("Asosiy Menuga qaytish");
        keyboard.add(row2);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    public static ReplyKeyboardMarkup showSellerMenu() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Sotuvchi qo`shish");
        row1.add("Delete Seller");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("Passiv Sotuvchilar (min bonus %)");
        row2.add("Aktiv Sotuvchilar (most bonus %)");

        KeyboardRow row3 = new KeyboardRow();
        row3.add("Asosiy Menuga qaytish");
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;

    }

    public static ReplyKeyboardMarkup back() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Back");
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    public static ReplyKeyboardMarkup getProduct() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Back");
        KeyboardRow row2 = new KeyboardRow();
        row2.add("Malumotlarni yuklab olish");
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row2);
        keyboard.add(row1);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }
}
