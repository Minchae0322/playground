package com.example.playgroundmanage.store.impl;

import com.example.playgroundmanage.store.FileCompression;
import org.apache.tomcat.util.codec.binary.Base64;
import org.imgscalr.Scalr;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.UUID;

@Component
public class MultipartFileCompression implements FileCompression {

    @Override
    public BufferedImage compressFile(InputStreamSource file) throws IOException {


        //mutipartfile->bufferedImage
        BufferedImage bi = ImageIO.read(file.getInputStream());
        //이미지 사이즈변경
        bi = resizeImage(bi, 450, 450);

        //이미지 위치에 저장
        return bi;
    }

    BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        // resize에 들어가는 속성을 변경해서 여러 모드로 변경해줄수있다
        return Scalr.resize(originalImage, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_EXACT, targetWidth, targetHeight, Scalr.OP_ANTIALIAS);
    }
}
