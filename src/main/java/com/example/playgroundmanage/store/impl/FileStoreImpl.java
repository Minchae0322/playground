package com.example.playgroundmanage.store.impl;

import com.example.playgroundmanage.store.FileStore;
import com.example.playgroundmanage.store.UploadFile;
import com.example.playgroundmanage.store.UploadFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.playgroundmanage.store.FileRootParser.getStoreFilePath;


@Component
@RequiredArgsConstructor
public class FileStoreImpl implements FileStore {

    private final UploadFileRepository uploadFileRepository;

    @Override
    public UploadFile storeFile(MultipartFile multipartFile) {
        if(multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        // 작성자가 업로드한 파일명 -> 서버 내부에서 관리하는 파일명
        // 파일명을 중복되지 않게끔 UUID로 정하고 ".확장자"는 그대로

        String storeFilename = UUID.randomUUID() + "." + extractExt(originalFilename);
        //System.out.println(getFullPath(storeFilename));
        // 파일을 저장하는 부분 -> 파일경로 + storeFilename 에 저장

        try {
            multipartFile.transferTo(Path.of(getStoreFilePath() + storeFilename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return uploadFileRepository.save(UploadFile.builder()
                .orgFileName(originalFilename)
                .storeFileName(storeFilename)
                .build());
    }

    @Override
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if(!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile));
            }
        }
        return storeFileResult;
    }

    @Override
    public String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
