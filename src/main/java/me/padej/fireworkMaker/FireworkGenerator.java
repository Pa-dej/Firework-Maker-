package me.padej.fireworkMaker;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.List;

public class FireworkGenerator {

    public static void giveFireworks(Player player, int fireworkAmount, List<String> randomColors, boolean[] selectedPatterns, Integer flightDuration) {
        ItemStack firework = new ItemStack(Material.FIREWORK_ROCKET, fireworkAmount);
        FireworkMeta meta = (FireworkMeta) firework.getItemMeta();
        if (meta != null) {
            List<FireworkEffect> effects = new ArrayList<>();

            for (int i = 0; i < selectedPatterns.length; i++) {
                if (selectedPatterns[i] && i < 5) { // Только для первых 5 паттернов
                    FireworkEffect.Builder effectBuilder = FireworkEffect.builder();

                    switch (i) {
                        case 0 -> effectBuilder.with(FireworkEffect.Type.BALL);
                        case 1 -> effectBuilder.with(FireworkEffect.Type.BALL_LARGE);
                        case 2 -> effectBuilder.with(FireworkEffect.Type.STAR);
                        case 3 -> effectBuilder.with(FireworkEffect.Type.CREEPER);
                        case 4 -> effectBuilder.with(FireworkEffect.Type.BURST);
                    }

                    for (String color : randomColors) {
                        effectBuilder.withColor(Color.fromRGB(
                                Integer.valueOf(color.substring(1, 3), 16),
                                Integer.valueOf(color.substring(3, 5), 16),
                                Integer.valueOf(color.substring(5, 7), 16)));
                    }

                    effects.add(effectBuilder.build());
                }
            }

            if (!effects.isEmpty()) {
                List<FireworkEffect> updatedEffects = new ArrayList<>();
                for (FireworkEffect effect : effects) {
                    FireworkEffect.Builder builder = FireworkEffect.builder()
                            .with(effect.getType())
                            .withColor(effect.getColors())
                            .withFade(effect.getFadeColors());

                    if (selectedPatterns[5]) {
                        builder.flicker(true);
                    }

                    if (selectedPatterns[6]) {
                        builder.trail(true);
                    }

                    updatedEffects.add(builder.build());
                }
                meta.addEffects(updatedEffects);
            }

            if (flightDuration != null) {
                meta.setPower(flightDuration);
            }

            firework.setItemMeta(meta);
        }
        player.getInventory().addItem(firework);
    }

    public static int calculateTotalPrice(boolean[] selectedPatterns, int flightDuration, int fireworkAmount) {
        int totalPrice = 0;
        int[] patternPrices = {2, 4, 2, 2, 2, 2, 17};

        for (int i = 0; i < selectedPatterns.length; i++) {
            if (selectedPatterns[i]) {
                totalPrice += patternPrices[i];
            }
        }

        totalPrice += flightDuration * 2;
        totalPrice *= fireworkAmount;

        return Math.abs(totalPrice);
    }
}