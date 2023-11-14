package com.example.playgroundmanage.validator;

import com.example.playgroundmanage.exception.FormatException;

public class UserValidator {
    public static final String USER_NAME_FORMAT = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$";

    public static final String USER_PASSWORD_FORMAT = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
}
