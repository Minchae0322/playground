package com.example.playgroundmanage.store.tool;


import org.springframework.stereotype.Component;

@Component
public class FileRootParser {

    private static final String rootPath = System.getProperty("user.dir");

    // 프로젝트 루트 경로에 있는 files 디렉토리
    private static final String fileStoreDir = "/var/www/html" + "/images/";
    private static final String fileExtDir = "images/";

    private static final String fileStoreDir_dev = rootPath + "/front/src/assets/";
    private static final String fileExtDir_dev = "src/assets/";


    public static String getExtFilePath() {
        return fileExtDir;
    }

    public static String getStoreFilePath() {
        return fileStoreDir;
    }
}
