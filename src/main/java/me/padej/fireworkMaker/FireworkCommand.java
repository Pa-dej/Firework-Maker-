package me.padej.fireworkMaker;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.padej.fireworkMaker.ColorManager.generateRandomHexColor;
import static me.padej.fireworkMaker.FireworkGeneratorGUI.playerColors;

public class FireworkCommand implements CommandExecutor, TabCompleter {

    public FireworkCommand(JavaPlugin plugin) {
        Objects.requireNonNull(plugin.getCommand("firework")).setExecutor(this);
        Objects.requireNonNull(plugin.getCommand("firework")).setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            List<String> colors = new ArrayList<>();

            if (args.length > 0 && args[0].equalsIgnoreCase("custom")) {
                for (int i = 1; i < args.length; i++) if (args[i].matches("^#[0-9A-Fa-f]{6}$")) colors.add(args[i]);
            } else {
                colors = playerColors.getOrDefault(player, new ArrayList<>());
            }
            while (colors.size() < 7) colors.add(ColorManager.generateRandomHexColors(1).get(0));
            playerColors.put(player, colors);
            FireworkGeneratorGUI.openGUI(player);
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            suggestions.add("custom");
        } else if (args.length >= 2 && args.length <= 8) {
            for (int i = 0; i < 7; i++) {
                if (i + 2 == args.length) {
                    String randomColor = generateRandomHexColor();
                    suggestions.add(randomColor);
                }
            }
        }
        return suggestions;
    }
}