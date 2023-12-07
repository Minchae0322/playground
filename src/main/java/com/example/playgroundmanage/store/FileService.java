package com.example.playgroundmanage.store;

import com.example.playgroundmanage.game.repository.UserRepository;
import com.example.playgroundmanage.game.vo.User;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileService {
    private final UserRepository userRepository;

    private final FileHandler fileHandler;

    public void uploadProfile(User user, MultipartFile multipartFile) throws IOException {
        UploadFile uploadFile = fileHandler.storeFile(multipartFile);
        userRepository.save(user.updateProfile(uploadFile));
    }

}
