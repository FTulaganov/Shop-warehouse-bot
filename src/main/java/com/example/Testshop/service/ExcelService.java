package com.example.Testshop.service;

import com.example.Testshop.entity.GoodsEntity;
import com.example.Testshop.entity.ModelEntity;
import com.example.Testshop.repository.GoodsRepository;
import com.example.Testshop.repository.ModelRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Iterator;

@Service
public class ExcelService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ModelRepository modelRepository;

    public void saveGoodsFromExcel(MultipartFile file) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            // Iterate through each sheet
            for (Sheet sheet : workbook) {
                processSheet(sheet);
            }
        }
    }

    private void processSheet(Sheet sheet) {
        Iterator<Row> rows = sheet.iterator();

        while (rows.hasNext()) {
            Row currentRow = rows.next();

            if (currentRow.getRowNum() == 0) { // Skip header row
                continue;
            }

            String modelName = currentRow.getCell(0).getStringCellValue();
            String itemCode = currentRow.getCell(1).getStringCellValue();

            // Assuming that model already exists, fetch or create ModelEntity
            ModelEntity modelEntity = modelRepository.findByName(modelName)
                    .orElseGet(() -> {
                        ModelEntity newModel = new ModelEntity();
                        newModel.setName(modelName);
                        // Set other fields as necessary
                        modelRepository.save(newModel);
                        return newModel;
                    });

            // Fetch existing GoodsEntity or create a new one
            GoodsEntity goodsEntity = goodsRepository.findByCodeItem(itemCode)
                    .orElseGet(() -> {
                        GoodsEntity newGoods = new GoodsEntity();
                        newGoods.setCodeItem(itemCode);
                        newGoods.setModel(modelEntity);
                        return newGoods;
                    });

            // Save or update to database
            goodsRepository.save(goodsEntity);
        }
    }
}
