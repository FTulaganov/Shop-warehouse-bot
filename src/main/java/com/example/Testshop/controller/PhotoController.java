package com.example.Testshop.controller;

import com.example.Testshop.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/photos")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @PostMapping("/upload")
    public String uploadPhoto(@RequestParam("file") MultipartFile file) throws IOException {
        String token = photoService.savePhoto(file);
        return "Photo uploaded successfully. Token: " + token;
    }

    @GetMapping(value = "/{token}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getPhoto(@PathVariable String token) throws IOException {
        byte[] photo = photoService.getPhotoByToken(token);
        if (photo != null) {
            return ResponseEntity.ok(photo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
