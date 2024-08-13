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
        KeyboardButton region9 = button("Surxandaryo");
        KeyboardButton region10 = button("Samarqand  ");
        KeyboardButton region11 = button("Sirdaryo  ");
        KeyboardButton region12 = button("Toshkent vil  ");
        KeyboardButton region13 = button("Xorazm  ");
        KeyboardButton region14 = button("Qoraqalpogʻiston Res  ");
        KeyboardRow row1 = new KeyboardRow();
        row1.add(region1);
        row1.add(region2);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(region3);
        row2.add(region4);
        KeyboardRow row3 = new KeyboardRow();
        row3.add(region5);
        row3.add(region6);
        KeyboardRow row4 = new KeyboardRow();
        row4.add(region7);
        row4.add(region8);
        KeyboardRow row5 = new KeyboardRow();
        row5.add(region9);
        row5.add(region10);
        KeyboardRow row6 = new KeyboardRow();
        row6.add(region11);
        row6.add(region12);
        KeyboardRow row7 = new KeyboardRow();
        row7.add(region13);
        row7.add(region14);

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
        KeyboardButton bonuses = button("Bonuslar Menusi");
        KeyboardButton loginParol = button("Parolni o`zgartirish");
        KeyboardButton exit = button("Chiqish");

        KeyboardRow row1 = new KeyboardRow();
        row1.add(product);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(sellers);
        KeyboardRow row3 = new KeyboardRow();
        row3.add(bonuses);
        KeyboardRow row4 = new KeyboardRow();
        row4.add(loginParol);
        KeyboardRow row5 = new KeyboardRow();
        row5.add(exit);

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
        KeyboardRow row2 = new KeyboardRow();
        row2.add("Maxsulotni o`zgartirish");
        KeyboardRow row3 = new KeyboardRow();
        row3.add("Maxsulotni o`chirish ");
        KeyboardRow row4 = new KeyboardRow();
        row4.add("Maxsulotlar ro`yxati");
        KeyboardRow row5 = new KeyboardRow();
        row5.add("Orqaga qaytish");

        keyboard.add(row2);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    public static ReplyKeyboardMarkup showSellerMenu() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Sotuvchi qo`shish");
        KeyboardRow row2 = new KeyboardRow();
        row2.add("Sotuvchini o`chirish");

        KeyboardRow row3 = new KeyboardRow();
        row3.add("Passiv Sotuvchilar (min bonus %)");
        KeyboardRow row4 = new KeyboardRow();
        row4.add("Aktiv Sotuvchilar (most bonus %)");
        KeyboardRow row5 = new KeyboardRow();
        row5.add("Orqaga qaytish");
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        keyboard.add(row5);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;

    }

    public static ReplyKeyboardMarkup back() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Orqaga qaytish");
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

}
