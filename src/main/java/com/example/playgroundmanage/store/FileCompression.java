package com.example.playgroundmanage.store;

import org.springframework.core.io.InputStreamSource;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public interface FileCompression {


    BufferedImage compressFile(InputStreamSource inputStreamSource) throws Exception;
}
