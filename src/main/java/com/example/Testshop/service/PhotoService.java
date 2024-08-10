package com.example.Testshop.service;

import com.example.Testshop.entity.PhotoEntity;
import com.example.Testshop.repository.PhotoRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class PhotoService {

    private final String UPLOAD_DIR = "file:///C:/Users/Madina/Desktop/amur%20temur%20uchastka/Amir%20temur%20Finish%20interyer.pdf";

    @Autowired
    private PhotoRepository photoRepository;

    public String savePhoto(MultipartFile file) throws IOException {
        // Generate a unique token
        String token = UUID.randomUUID().toString();

        // Determine file extension
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        // Create file path
        String fileName = token + "." + extension;
        Path filePath = Paths.get(UPLOAD_DIR, fileName);

        // Save file to file system
        Files.write(filePath, file.getBytes());

        // Save token and file path in database
        PhotoEntity photo = new PhotoEntity();
        photo.setToken(token);
        photo.setFilePath(filePath.toString());
        photoRepository.save(photo);

        return token;
        // Return the token for later retrieval
    }

    public byte[] getPhotoByToken(String token) throws IOException {
        PhotoEntity photo = photoRepository.findByToken(token);
        if (photo != null) {
            Path filePath = Paths.get(photo.getFilePath());
            return Files.readAllBytes(filePath);
        }
        return null;
        // Handle not found case appropriately
    }
}
