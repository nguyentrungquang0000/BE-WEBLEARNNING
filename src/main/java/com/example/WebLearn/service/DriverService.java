package com.example.WebLearn.service;

import com.example.WebLearn.model.response.Response;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class DriverService {
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String SERVICE_ACOUNT_KEY_PATH = getPathToGoodleCredentials();

    public static String getPathToGoodleCredentials() {
        String currentDirectory = System.getProperty("user.dir");
        Path filePath = Paths.get(currentDirectory, "cred.json");
        return filePath.toString();
    }

    public String uploadFileToDrive(MultipartFile file) throws GeneralSecurityException, IOException {
        try {
            // Tạo metadata file
            String folderId = "1Zx6KGCp75g_6X8sF4puvxJYH_Nu3_V_R";
            Drive drive = createDriveService(); // Tạo Drive service

            // Lưu file vào hệ thống tạm thời (ví dụ: thư mục temp)
            File tempFile = new File(System.getProperty("java.io.tmpdir"), file.getOriginalFilename());
            file.transferTo(tempFile); // Lưu file từ MultipartFile vào tệp tin hệ thống

            // Tạo metadata cho file
            com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
            fileMetadata.setName(file.getOriginalFilename());
            fileMetadata.setParents(Collections.singletonList(folderId));

            // Lấy mimeType của file
            String mimeType = URLConnection.guessContentTypeFromName(file.getOriginalFilename());
            FileContent mediaContent = new FileContent(mimeType, tempFile);

            // Upload file
            com.google.api.services.drive.model.File uploadedFile = drive.files()
                    .create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();

            // Xóa file tạm thời sau khi upload
            tempFile.delete();

            return "https://drive.google.com/uc?export=view&id=" + uploadedFile.getId();
        } catch (Exception e) {
            throw new GeneralSecurityException(e.getMessage());
        }
    }


    private Drive createDriveService() throws GeneralSecurityException, IOException {
        // Đọc file cred.json
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(SERVICE_ACOUNT_KEY_PATH))
                .createScoped(Collections.singleton(DriveScopes.DRIVE));

        return new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                credential)
                .build();
    }

    public ResponseEntity<Response<Object>> deleteFileToDrive(String fileUrl) throws GeneralSecurityException, IOException {
        try{
            String fileDriveId = extractDriveIdFromUrl(fileUrl);
            Drive driveService = createDriveService();
            driveService.files().delete(fileDriveId).execute();
            return ResponseEntity.ok(new Response<>(200, "ok", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new Response<>(500,e.getMessage(), null));
        }

    }

    private String extractDriveIdFromUrl(String fileUrl) {
        return fileUrl.substring(fileUrl.lastIndexOf("=") + 1);
    }

}
