package com.example.playgroundmanage.store;

import com.example.playgroundmanage.store.vo.UploadFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStore {

    UploadFile storeFile(MultipartFile multipartFile);


    List<UploadFile> storeFiles(List<MultipartFile> multipartFiles);

    String extractExt(String originalFilename);
}
