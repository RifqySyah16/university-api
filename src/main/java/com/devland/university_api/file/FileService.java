package com.devland.university_api.file;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.devland.university_api.applicationuser.FileStorageException;
import com.devland.university_api.applicationuser.model.ApplicationUser;

@Service
public class FileService {

    public void savePhoto(ApplicationUser newUser, MultipartFile photo) {
        String fileName = StringUtils.cleanPath(photo.getOriginalFilename());
        try {
            Path uploadPath = Paths.get("uploads");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            newUser.setPhotoPath(fileName);
        } catch (Exception e) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!");
        }
    }
}
