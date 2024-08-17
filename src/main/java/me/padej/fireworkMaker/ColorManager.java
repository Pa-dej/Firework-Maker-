package me.padej.fireworkMaker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColorManager {

    private static final Random random = new Random();

    public static List<String> generateRandomHexColors(int amount) {
        List<String> colors = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            colors.add(generateRandomHexColor());
        }
        return colors;
    }

    public static String generateRandomHexColor() {
        int color = random.nextInt(0xFFFFFF);
        return String.format("#%06X", color);
    }
}
