package com.example.playgroundmanage.store.impl;

import com.example.playgroundmanage.store.FileHandler;
import com.example.playgroundmanage.store.InMemoryMultipartFile;
import com.example.playgroundmanage.store.UploadFile;
import com.example.playgroundmanage.store.UploadFileRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.*;

import static com.example.playgroundmanage.store.FileRootParser.getExtFilePath;
import static com.example.playgroundmanage.store.FileRootParser.getStoreFilePath;

@Component
public class DirectoryPathFileHandler extends FileStoreImpl implements FileHandler {

    public DirectoryPathFileHandler(UploadFileRepository uploadFileRepository) {
        super(uploadFileRepository);
    }

    @Override
    public String getFullPath(String filename) {
        return getStoreFilePath() + filename; }

    @Override
    public String getExtFullPath(String filename) {
        return getExtFilePath() + filename;
    }


    @Override
    public List<InMemoryMultipartFile> extractFiles(List<? extends UploadFile> uploadFiles)  {
        List<InMemoryMultipartFile> list = new ArrayList<>();
        for (UploadFile file : uploadFiles) {
            list.add(extractFile(file));
        }
        return list;
    }

    @Override
    public boolean deleteFile(UploadFile uploadFile) {
        String storeFileName = uploadFile.getStoreFileName();
        String filePath = getFullPath(storeFileName);
        File file = new File(filePath);

        if (file.exists()) {
            if (file.delete()) {
                return true; // File deleted successfully
            } else {
                return false; // Failed to delete the file
            }
        } else {
            return true; // File doesn't exist, treat as a successful deletion
        }
    }

    @Override
    public boolean deleteFiles(List<? extends UploadFile> uploadFiles) {
        for (UploadFile file : uploadFiles) {
            if (!deleteFile(file)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public InMemoryMultipartFile extractFile(UploadFile uploadFile) {
        if (uploadFile == null) {
            return null;
        }
        File inMemoryFile = new File(getFullPath(uploadFile.getStoreFileName()));
        byte[] fileContent = new byte[0];
        try {
            fileContent = FileCopyUtils.copyToByteArray(inMemoryFile);
        } catch (IOException e) {
            return null;
        }
        return new InMemoryMultipartFile(uploadFile.getOrgFileName(), fileContent);
    }

    // 확장자 추출
    @Override
    public String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    public static String multipartFileToString(InMemoryMultipartFile inMemoryMultipartFile) {
        return Optional.ofNullable(inMemoryMultipartFile)
                .map(file -> {
                    try {
                        return Base64.getEncoder().encodeToString(file.getBytes());
                    } catch (IOException e) {
                        throw new UncheckedIOException("이미지 변환 실패", e);
                    }
                })
                .orElse("");
    }
}
