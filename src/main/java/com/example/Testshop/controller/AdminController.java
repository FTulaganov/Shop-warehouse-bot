package com.example.Testshop.controller;

import com.example.Testshop.TelegramBot;
import com.example.Testshop.entity.GoodsEntity;
import com.example.Testshop.entity.ModelEntity;
import com.example.Testshop.entity.UserEntity;
import com.example.Testshop.enums.AdminStep;
import com.example.Testshop.enums.ProductStep;
import com.example.Testshop.repository.GoodsRepository;
import com.example.Testshop.repository.ModelRepository;
import com.example.Testshop.repository.UserRepository;
import com.example.Testshop.util.ReplyKeyboardUtil;
import com.example.Testshop.util.UserMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


@Component
public class AdminController {
    @Autowired
    @Lazy
    private TelegramBot myTelegramBot;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GoodsController goodsController;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private ModelRepository modelRepository;

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
            case "Maxsulot qo`shish" -> {
                UserMap.saveAdminStep(message.getChatId(), AdminStep.ADD_PRODUCT);
                myTelegramBot.sendMessage("Maxsulot Excel faylini yuklang :", id, ReplyKeyboardUtil.back());
            }
            case "Maxsulotni o`zgartirish" -> {
                myTelegramBot.sendMessage("Maxsulotning S/R ni kiriting.", message.getChatId(), ReplyKeyboardUtil.back());
                UserMap.saveAdminStep(message.getChatId(), AdminStep.EDIT_PRODUCT);
                UserMap.saveProductStep(message.getChatId(), ProductStep.START);
            }
            case "Maxsulotni o`chirish" -> {
                myTelegramBot.sendMessage(id, "Maxsulotning S/R ni kiriting.");
                UserMap.saveAdminStep(message.getChatId(), AdminStep.DELETE_PORDUCT);
            }
            case "Maxsulotlar ro`yxati" -> myTelegramBot.sendMessage(id, "Maxsulotlar ro`yxati.");
            case "Asosiy Menuga qaytish" -> {
                myTelegramBot.sendMessage("Asosiy sahifaga Hush kelibsiz", message.getChatId(), ReplyKeyboardUtil.menu());
                UserMap.saveAdminStep(id, AdminStep.MENU);
            }
        }

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

    public void addProduct(Message message) {
        if (message.getText() != null && message.getText().equals("Back")) {
            myTelegramBot.sendMessage("Mahsulotlar menusi", message.getChatId(), ReplyKeyboardUtil.showGoodsMenu());
            UserMap.saveAdminStep(message.getChatId(), AdminStep.GOODS_MENU);
        } else {
            Document document = message.getDocument();
            String fileId = document.getFileId();

            try {
                GetFile getFile = new GetFile();
                getFile.setFileId(fileId);
                File file = myTelegramBot.sendMsg(getFile);

                String filePath = "https://api.telegram.org/file/bot" + "6960550759:AAELl3_KQsCm5d9DcIOH4GpXJltupiRGDMc" + "/" + file.getFilePath();
                InputStream inputStream = new URL(filePath).openStream();

                Workbook workbook = new XSSFWorkbook(inputStream);
                Sheet sheet = workbook.getSheetAt(0);

                for (Row row : sheet) {
                    Cell modelCell = row.getCell(0);
                    Cell serialCell = row.getCell(1);

                    if (modelCell != null && serialCell != null) {
                        String modelName = modelCell.getStringCellValue();
                        String serialNumber = serialCell.getStringCellValue();

                        if (modelName != null && serialNumber != null) {
                            ModelEntity model = new ModelEntity();
                            model.setModel(modelName);
                            modelRepository.save(model);

                            Integer modelId = model.getId();

                            GoodsEntity serial = new GoodsEntity();
                            serial.setModelId(modelId);
                            serial.setCodeItem(serialNumber);
                            goodsRepository.save(serial);
                        } else {
                            System.out.println("Model name or serial number is null.");
                        }
                    } else {
                        System.out.println("Excel cell is null.");
                    }
                }
                workbook.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            myTelegramBot.sendMessage(message.getChatId(), "Ma'lumotlar yuklandi ");
            myTelegramBot.sendMessage("Maxsulot Excel faylini yuklang :", message.getChatId(), ReplyKeyboardUtil.back());

        }
    }

    public void editProduct(Message message) {
        if (message.getText() != null && message.getText().equals("Back")) {
            myTelegramBot.sendMessage("Mahsulotlar menusi", message.getChatId(), ReplyKeyboardUtil.showGoodsMenu());
            UserMap.saveAdminStep(message.getChatId(), AdminStep.GOODS_MENU);
        } else if (UserMap.getProductStep(message.getChatId()) != null) {
            switch (UserMap.getProductStep(message.getChatId())) {
                case START -> goodsController.start(message);
                case NAME -> goodsController.name(message);
                case PRICE -> goodsController.price(message);
                case BONUS -> goodsController.bonus(message);
                case ALL -> goodsController.all(message);

            }
        }
    }

    public void deleteProduct(Message message) {
        goodsController.deleteProduct(message);
    }
}