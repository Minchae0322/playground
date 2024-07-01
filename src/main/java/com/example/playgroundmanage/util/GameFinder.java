package com.example.playgroundmanage.util;

import com.example.playgroundmanage.date.DateTime;
import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.exception.NoOngoingGameException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;



@Getter
@Component
@AllArgsConstructor
public class GameFinder {

    private final DateFormat dateFormat;




}
