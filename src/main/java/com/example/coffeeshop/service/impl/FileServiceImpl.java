package com.example.coffeeshop.service.impl;

import com.example.coffeeshop.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileServiceImpl implements FileService {
    private final String uploadDir = "uploads/";

    public String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;
        
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        file.transferTo(new File(dir.getAbsolutePath() + File.separator + filename));
        return filename;
    }

    public void deleteFile(String filename) {
        if (filename != null) {
            File file = new File(uploadDir + filename);
            if (file.exists()) file.delete();
        }
    }
}