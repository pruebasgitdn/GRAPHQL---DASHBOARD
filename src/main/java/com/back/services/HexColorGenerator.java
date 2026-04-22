package com.back.services;


import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class HexColorGenerator {

    public String generateRandomHexColor() {
        String color;

        do {
            color = String.format("#%06X", (int)(Math.random() * 0xFFFFFF));
        } while (
                color.equalsIgnoreCase("#ffffff") ||
                        color.equalsIgnoreCase("#000000")
        );

        return color;
    }
}



