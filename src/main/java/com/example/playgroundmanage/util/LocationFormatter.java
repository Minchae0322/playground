package com.example.playgroundmanage.util;

import com.example.playgroundmanage.althlectis.vo.Athletics;

import org.springframework.stereotype.Component;

@Component
public class LocationFormatter {

    public static String getLocation(Athletics athletics) {
        return athletics.getPlayground().getCampus().getCampusName() + ", " + athletics.getPlayground().getName();
    }
}
