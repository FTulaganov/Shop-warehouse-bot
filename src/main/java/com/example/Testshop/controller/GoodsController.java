package com.example.Testshop.controller;


import com.example.Testshop.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin")
public class GoodsController {

    @Autowired
    private ExcelService excelService;

    @PostMapping("/upload-goods")
    public ResponseEntity<String> uploadGoodsFile(@RequestParam("file") MultipartFile file) {
        try {
            excelService.saveGoodsFromExcel(file);
            return ResponseEntity.ok("File uploaded and data saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }
    }
}
