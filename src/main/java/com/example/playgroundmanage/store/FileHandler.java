package com.example.playgroundmanage.store;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface FileHandler extends FileStore{
    String getFullPath(String filename);


    String getExtFullPath(String filename);



    boolean deleteFile(UploadFile uploadFile);

    boolean deleteFiles(List<? extends UploadFile> uploadFiles);



    List<? extends Object> extractFiles(List<? extends UploadFile> uploadFiles);

    Object extractFile(UploadFile uploadFile);

    String extractExt(String originalFilename);


}
