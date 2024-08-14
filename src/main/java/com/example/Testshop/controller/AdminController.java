package com.example.Testshop.controller;

import com.example.Testshop.TelegramBot;
import com.example.Testshop.dto.SellerDto;
import com.example.Testshop.entity.GoodsEntity;
import com.example.Testshop.entity.ModelEntity;
import com.example.Testshop.entity.SellerEntity;
import com.example.Testshop.entity.UserEntity;
import com.example.Testshop.enums.AdminStep;
import com.example.Testshop.enums.Bonus;
import com.example.Testshop.enums.ProductStep;
import com.example.Testshop.enums.Status;
import com.example.Testshop.repository.GoodsRepository;
import com.example.Testshop.repository.ModelRepository;
import com.example.Testshop.repository.UserRepository;
import com.example.Testshop.service.SellerService;
import com.example.Testshop.util.InlineKeyBoardUtil;
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
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
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
    @Autowired
    private SellerService sellerService;

    public void login(Message message) {
        List<UserEntity> userEntity = userRepository.findAll();
        if (message.getText().equals(userEntity.get(0).getPassword())) {
            UserMap.saveAdminStep(message.getChatId(), AdminStep.MENU);
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setChatId(message.getChatId());
            deleteMessage.setMessageId(message.getMessageId());
            myTelegramBot.sendMessage(deleteMessage);
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
        } else if (text.equals("Bonuslar Menusi")) {
            myTelegramBot.sendMessage("Bonuslar : ", message.getChatId(), ReplyKeyboardUtil.bonusOfOwnerMenu());
            UserMap.saveAdminStep(message.getChatId(), AdminStep.BONUS_PRODUCT);
        } else if (text.equals("Parolni o`zgartirish")) {
            myTelegramBot.sendMessage("Eski parolni kiriting : ", message.getChatId(), ReplyKeyboardUtil.back());
            UserMap.saveAdminStep(message.getChatId(), AdminStep.RESTART_PASSWORD);
        } else if (text.equals("Exit")) {
            myTelegramBot.sendMessage(chatId, "Bizning Telegram botimizdan foydalangiz uchun raxmat!");
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
                myTelegramBot.sendMessage("Maxsulotning S/R ni kiriting.", id, ReplyKeyboardUtil.back());
                UserMap.saveAdminStep(message.getChatId(), AdminStep.EDIT_PRODUCT);
                UserMap.saveProductStep(message.getChatId(), ProductStep.START);
            }
            case "Maxsulotni o`chirish" -> {
                myTelegramBot.sendMessage("Maxsulotning S/R ni kiriting.", id, ReplyKeyboardUtil.back());
                UserMap.saveAdminStep(message.getChatId(), AdminStep.DELETE_PRODUCT);
            }
            case "Maxsulotlar ro`yxati" -> {
                myTelegramBot.sendMessage("Pasdagi tugma orqali ro`yxatni yuklab oling.", id, ReplyKeyboardUtil.getProduct());
                UserMap.saveAdminStep(message.getChatId(), AdminStep.GET_PRODUCT);
            }
            case "Orqaga qaytish" -> {
                myTelegramBot.sendMessage("Asosiy sahifaga Hush kelibsiz", message.getChatId(), ReplyKeyboardUtil.menu());
                UserMap.saveAdminStep(id, AdminStep.MENU);
            }
        }

    }

    public void sellersMenu(Message message) {
        String text = message.getText();
        Long id = message.getChatId();
        switch (text) {
            case "Sotuvchi qo`shish" -> {
                signUpSeller(message);
                myTelegramBot.sendMessage("Sotuvchilar", id, ReplyKeyboardUtil.back());
                UserMap.saveAdminStep(message.getChatId(), AdminStep.ADD_SELLER);
            }
            case "Sotuvchini o`chirish" -> {
                myTelegramBot.sendMessage("Sotuvchining Telefon raqamini kiriting", id, ReplyKeyboardUtil.back());
                UserMap.saveAdminStep(message.getChatId(), AdminStep.DELETE_SELLER);
            }
            case "Passiv Sotuvchilar (min bonus %)" ->
                    myTelegramBot.sendMessage(id, "Eng kam bonus yiqqan Sotuvchilarning ro`yxati.");
            case "Aktiv Sotuvchilar (most bonus %)" ->
                    myTelegramBot.sendMessage(id, "Eng ko`p bonus yiqqan Sotuvchilarning ro`yxati.");
            case "Orqaga qaytish" -> {
                myTelegramBot.sendMessage("Asosiy sahifaga Hush kelibsiz", message.getChatId(), ReplyKeyboardUtil.menu());
                UserMap.saveAdminStep(id, AdminStep.MENU);
            }
        }
    }

    private void signUpSeller(Message message) {
        List<SellerEntity> list = sellerService.allSeller();
        if (list != null) {
            for (SellerEntity entity : list) {
                String re = "Ism : " + entity.getName() +
                        "\nTelefon : " + entity.getPhone() +
                        "\nViloyat : " + entity.getRegion() +
                        "\nBozor,do`kon : " + entity.getShopName() +
                        "\nKarta raqami : " + entity.getCard();

                SendLocation sendLocation = new SendLocation();
                sendLocation.setChatId(message.getChatId());
                sendLocation.setLatitude(entity.getLocation_latitude());
                sendLocation.setLongitude(entity.getLocation_longitude());
                int id1 = myTelegramBot.sendMessage(message.getChatId(), re);
                int id2 = myTelegramBot.sendMsg(sendLocation);
                int id3 = myTelegramBot.sendMessage("Sotuvchilar royhatiag qoshasizmi", message.getChatId(), InlineKeyBoardUtil.checkRegisAdmin(entity.getChatId())).getMessageId();
                List<Integer> idlist = new LinkedList<>();
                idlist.add(id1);
                idlist.add(id2);
                idlist.add(id3);
                UserMap.chatIdList.put(entity.getChatId(), idlist);
            }
        }
    }

    public void addProduct(Message message) {
        if (message.getText() != null && message.getText().equals("Orqaga qaytish")) {
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
        if (message.getText() != null && message.getText().equals("Orqaga qaytish")) {
            myTelegramBot.sendMessage("Mahsulotlar menusi", message.getChatId(), ReplyKeyboardUtil.showGoodsMenu());
            UserMap.saveAdminStep(message.getChatId(), AdminStep.GOODS_MENU);
        } else if (UserMap.getProductStep(message.getChatId()) != null) {
            switch (UserMap.getProductStep(message.getChatId())) {
                case START -> goodsController.start(message);
                case ALL -> goodsController.all(message);
            }

        }
    }

    public void deleteProduct(Message message) {
        if (message.getText() != null && message.getText().equals("Orqaga qaytish")) {
            myTelegramBot.sendMessage("Mahsulotlar menusi", message.getChatId(), ReplyKeyboardUtil.showGoodsMenu());
            UserMap.saveAdminStep(message.getChatId(), AdminStep.GOODS_MENU);
        } else {
            if (!goodsController.deleteProduct(message)) {
                myTelegramBot.sendMessage(message.getChatId(), "Bu S/R Malumotlar bazasida yoq");
                myTelegramBot.sendMessage("Maxsulotning S/R ni kiriting.", message.getChatId(), ReplyKeyboardUtil.back());
            }
        }
    }

    public void getProduct(Message message) {
        if (message.getText().equals("Malumotlarni yuklab olish")) {
            try {
                ByteArrayOutputStream out = goodsController.getProduct(message);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toByteArray());

                InputFile excelFile = new InputFile(inputStream, "data.xlsx");
                myTelegramBot.sendMsg(message.getChatId(), excelFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (message.getText() != null && message.getText().equals("Orqaga qaytish")) {
            myTelegramBot.sendMessage("Mahsulotlar menusi", message.getChatId(), ReplyKeyboardUtil.showGoodsMenu());
            UserMap.saveAdminStep(message.getChatId(), AdminStep.GOODS_MENU);
        }

    }

    public void add(Message message) {
        if (message.getText() != null && message.getText().equals("Orqaga qaytish")) {
            myTelegramBot.sendMessage("Sotuvchilar Menusi:", message.getChatId(), ReplyKeyboardUtil.showSellerMenu());
            UserMap.saveAdminStep(message.getChatId(), AdminStep.SELLERS_MENU);
        }
    }

    public void deleteSeller(Message message) {
        if (message.getText() != null && message.getText().equals("Orqaga qaytish")) {
            myTelegramBot.sendMessage("Sotuvchilar Menusi:", message.getChatId(), ReplyKeyboardUtil.showSellerMenu());
            UserMap.saveAdminStep(message.getChatId(), AdminStep.SELLERS_MENU);
        } else if (sellerService.getSeller(message.getText())) {
            myTelegramBot.sendMessage(message.getChatId(), "Muaffaqiatli ochirildi");
            myTelegramBot.sendMessage("Sotuvchining Telefon raqamini kiriting", message.getChatId(), ReplyKeyboardUtil.back());

        } else if (!sellerService.getSeller(message.getText())) {
            myTelegramBot.sendMessage(message.getChatId(), "Bunday raqamli foydalanuuvchi yoq!!");
            myTelegramBot.sendMessage("Sotuvchining Telefon raqamini kiriting", message.getChatId(), ReplyKeyboardUtil.back());

        }
    }

    public void bonusProduct(Message message) {
        String text = message.getText();
        Long id = message.getChatId();
        switch (text) {
            case "Bonus qo`shish" -> {
                myTelegramBot.sendMessage("Mahsulotning modelini kiriting : ", id, ReplyKeyboardUtil.back());
                UserMap.saveAdminStep(message.getChatId(), AdminStep.ADD_BONUS);
            }
            case "Bonusni o`zgartirish" -> {
                myTelegramBot.sendMessage("Mahsulotning modelini kiriting : ", id, ReplyKeyboardUtil.back());
                UserMap.saveAdminStep(message.getChatId(), AdminStep.ADD_BONUS);
            }
            case "Bonusni o`chirish" -> {
                myTelegramBot.sendMessage("Mahsulotning modelini kiriting : ", id, ReplyKeyboardUtil.back());
                UserMap.saveAdminStep(message.getChatId(), AdminStep.DELETE_BONUS);
            }
            case "Orqaga qaytish" -> {
                myTelegramBot.sendMessage("Asosiy sahifaga Hush kelibsiz", message.getChatId(), ReplyKeyboardUtil.menu());
                UserMap.saveAdminStep(id, AdminStep.MENU);
            }
        }


    }

    public void restartPassword(Message message) {
        UserEntity userEntity = userRepository.findByChatId(message.getChatId());
        if (message.getText() != null && message.getText().equals("Orqaga qaytish")) {
            myTelegramBot.sendMessage("Asosiy sahifaga Hush kelibsiz", message.getChatId(), ReplyKeyboardUtil.menu());
            UserMap.saveAdminStep(message.getChatId(), AdminStep.MENU);
        } else if (message.getText().equals(userEntity.getPassword())) {
            UserMap.checkpasword.put(message.getChatId(), true);
            myTelegramBot.sendMessage(message.getChatId(), "Yangi parolni kiriting ; ");
        } else if (UserMap.checkpasword.get(message.getChatId()) == null || !UserMap.checkpasword.get(message.getChatId())) {
            UserMap.checkpasword.put(message.getChatId(), false);
            myTelegramBot.sendMessage(message.getChatId(), "Eski parol hato kiritildi : ");
            myTelegramBot.sendMessage("Eski parolni kiriting : ", message.getChatId(), ReplyKeyboardUtil.back());
        } else if (UserMap.checkpasword.get(message.getChatId())) {
            userEntity.setPassword(message.getText());
            userRepository.save(userEntity);
            myTelegramBot.sendMessage("Parol muaffaqiyatli ozgartirildi", message.getChatId(), ReplyKeyboardUtil.back());
            UserMap.checkpasword.put(message.getChatId(), false);
            DeleteMessage deleteMessage = new DeleteMessage();
            deleteMessage.setMessageId(message.getMessageId());
            deleteMessage.setChatId(message.getChatId());
            myTelegramBot.sendMessage(deleteMessage);
        }
    }

    public void addBonus(Message message) {
        String text = message.getText();
        List<ModelEntity> list = modelRepository.findAllByModel(text);

        if (message.getText() != null && message.getText().equals("Orqaga qaytish")) {
            myTelegramBot.sendMessage("Bonuslar : ", message.getChatId(), ReplyKeyboardUtil.bonusOfOwnerMenu());
            UserMap.saveAdminStep(message.getChatId(), AdminStep.BONUS_PRODUCT);
        } else if (UserMap.getBonus(message.getChatId()) != null) {
            switch (UserMap.getBonus(message.getChatId())) {
                case ADD_BONUS -> goodsController.startBonus(message);
                case ADD_PRICE -> goodsController.price(message);
            }
        } else if (!list.isEmpty()) {
            for (ModelEntity model : list) {
                UserMap.bonus.add(model.getId());
            }
            myTelegramBot.sendMessage(message.getChatId(), "Bonusni kiriting : ");
            UserMap.saveBonusStep(message.getChatId(), Bonus.ADD_BONUS);
        } else if ((UserMap.getBonus(message.getChatId()) == null && list.isEmpty())) {
            myTelegramBot.sendMessage(message.getChatId(), "Bunday model bazada yoq ");
            myTelegramBot.sendMessage("Mahsulotning modelini kiriting : ", message.getChatId(), ReplyKeyboardUtil.back());
        }
    }

    public void deleteBonus(Message message) {
        String text = message.getText();
        List<ModelEntity> list = modelRepository.findAllByModel(text);
        if (message.getText() != null && message.getText().equals("Orqaga qaytish")) {
            myTelegramBot.sendMessage("Bonuslar : ", message.getChatId(), ReplyKeyboardUtil.bonusOfOwnerMenu());
            UserMap.saveAdminStep(message.getChatId(), AdminStep.BONUS_PRODUCT);
        } else if (!list.isEmpty()) {
            for (ModelEntity model : list) {
                UserMap.bonus.add(model.getId());
            }
            goodsController.deleteBonus(message);
        } else {
            myTelegramBot.sendMessage(message.getChatId(), "Bunday model bazada yoq ");
            myTelegramBot.sendMessage("Mahsulotning modelini kiriting : ", message.getChatId(), ReplyKeyboardUtil.back());
        }
    }
}