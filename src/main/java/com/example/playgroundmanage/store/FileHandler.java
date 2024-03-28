package com.example.playgroundmanage.store;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface FileHandler{
    String getFullPath(String filename);

    String getFullPath(UploadFile uploadFile);


    String getExtFullPath(String filename);

    UploadFile storeFile(MultipartFile multipartFile) throws IOException;

    boolean deleteFile(UploadFile uploadFile);

    boolean deleteFiles(List<? extends UploadFile> uploadFiles);

    List<? extends UploadFile> storeFiles(List<MultipartFile> multipartFiles, Object objectBy) throws IOException;

    List<InMemoryMultipartFile> extractFiles(List<? extends UploadFile> uploadFiles);

    InMemoryMultipartFile extractFile(UploadFile uploadFile);

    String extractExt(String originalFilename);


}
