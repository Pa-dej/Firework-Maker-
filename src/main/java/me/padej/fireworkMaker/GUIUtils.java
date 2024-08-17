package me.padej.fireworkMaker;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Arrays;
import java.util.List;

public class GUIUtils {

    public static void setupGUIItems(Inventory gui, List<String> randomColors) {
        gui.setItem(0, createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        gui.setItem(1, createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        gui.setItem(2, createItem(Material.ORANGE_STAINED_GLASS_PANE, " "));
        gui.setItem(3, createItem(Material.ORANGE_STAINED_GLASS_PANE, " "));
        gui.setItem(4, createItem(Material.FIREWORK_ROCKET,
                ChatColor.of("#fcc22d") + "Генератор фейерверков",
                ChatColor.of("#dfdfdf") + "Позволяет вам создавать", ChatColor.of("#dfdfdf") + "фейерверки легко и дешево!"));
        gui.setItem(5, createItem(Material.ORANGE_STAINED_GLASS_PANE, " "));
        gui.setItem(6, createItem(Material.ORANGE_STAINED_GLASS_PANE, " "));
        gui.setItem(7, createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        gui.setItem(8, createItem(Material.BLACK_STAINED_GLASS_PANE, " "));

        // Строка 2
        gui.setItem(9, createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        gui.setItem(10, createItem(Material.STRUCTURE_VOID, ChatColor.of("#fcc22d") + "Малый шар",
                ChatColor.of("#dfdfdf") + "Цена: " + ChatColor.of("#91db69") + "2$"));
        gui.setItem(11, createItem(Material.FIRE_CHARGE, ChatColor.of("#fcc22d") + "Большой шар",
                ChatColor.of("#dfdfdf") + "Цена: " + ChatColor.of("#91db69") + "4$"));
        gui.setItem(12, createItem(Material.GOLD_NUGGET, ChatColor.of("#fcc22d") + "Звезда",
                ChatColor.of("#dfdfdf") + "Цена: " + ChatColor.of("#91db69") + "2$"));
        gui.setItem(13, createItem(Material.CREEPER_HEAD, ChatColor.of("#fcc22d") + "Крипер",
                ChatColor.of("#dfdfdf") + "Цена: " + ChatColor.of("#91db69") + "2$"));
        gui.setItem(14, createItem(Material.FEATHER, ChatColor.of("#fcc22d") + "Всплеск",
                ChatColor.of("#dfdfdf") + "Цена: " + ChatColor.of("#91db69") + "2$"));
        gui.setItem(15, createItem(Material.GLOWSTONE_DUST, ChatColor.of("#fcc22d") + "Мерцание",
                ChatColor.of("#dfdfdf") + "Цена: " + ChatColor.of("#91db69") + "2$"));
        gui.setItem(16, createItem(Material.DIAMOND, ChatColor.of("#fcc22d") + "След",
                ChatColor.of("#dfdfdf") + "Цена: " + ChatColor.of("#91db69") + "17$"));
        gui.setItem(17, createItem(Material.BLACK_STAINED_GLASS_PANE, " "));

        // Строка 3
        gui.setItem(18, createItem(Material.ORANGE_STAINED_GLASS_PANE, " "));
        for (int i = 19; i <= 25; i++) {
            gui.setItem(i, createColoredLeatherChestplate(randomColors.get(i - 19)));
        }
        gui.setItem(26, createItem(Material.ORANGE_STAINED_GLASS_PANE, " "));

        // Строка 4
        gui.setItem(27, createItem(Material.ORANGE_STAINED_GLASS_PANE, " "));
        gui.setItem(28, createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        gui.setItem(29, createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        gui.setItem(30, createItem(Material.GUNPOWDER, 1, ChatColor.of("#fcc22d") + "Длительность полета: " + ChatColor.of("#f9883a") + "1",
                ChatColor.of("#dfdfdf") + "Цена: " + ChatColor.of("#91db69") + "2$"));
        gui.setItem(31, createItem(Material.GUNPOWDER, 2, ChatColor.of("#fcc22d") + "Длительность полета: " + ChatColor.of("#f9883a") + "2",
                ChatColor.of("#dfdfdf") + "Цена: " + ChatColor.of("#91db69") + "4$"));
        gui.setItem(32, createItem(Material.GUNPOWDER, 3, ChatColor.of("#fcc22d") + "Длительность полета: " + ChatColor.of("#f9883a") + "3",
                ChatColor.of("#dfdfdf") + "Цена: " + ChatColor.of("#91db69") + "6$"));
        gui.setItem(33, createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        gui.setItem(34, createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        gui.setItem(35, createItem(Material.BLACK_STAINED_GLASS_PANE, " "));

        // Строка 5
        gui.setItem(36, createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        gui.setItem(37, createItem(Material.ORANGE_STAINED_GLASS_PANE, " "));
        gui.setItem(38, createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        gui.setItem(39, createItem(Material.FIREWORK_ROCKET, ChatColor.of("#fcc22d") + "Количество: " + ChatColor.of("#f9883a") + "1"));
        gui.setItem(40, createItem(Material.SPECTRAL_ARROW, ChatColor.of("#fcc22d") + "Обновить все цвета"));
        gui.setItem(41, createGuiItem(Material.BUNDLE, ChatColor.of("#fcc22d") + "Купить фейерверк", List.of()));
        gui.setItem(42, createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        gui.setItem(43, createItem(Material.ORANGE_STAINED_GLASS_PANE, " "));
        gui.setItem(44, createItem(Material.BLACK_STAINED_GLASS_PANE, " "));

        // Строка 6
        gui.setItem(45, createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        gui.setItem(46, createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        gui.setItem(47, createItem(Material.ORANGE_STAINED_GLASS_PANE, " "));
        gui.setItem(48, createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        gui.setItem(49, createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        gui.setItem(50, createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        gui.setItem(51, createItem(Material.ORANGE_STAINED_GLASS_PANE, " "));
        gui.setItem(52, createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        gui.setItem(53, createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
    }

    public static ItemStack createColoredLeatherChestplate(String hexColor) {
        ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.of("#fcc22d") + "Нажмите, чтобы сменить цвет");
            meta.setCustomModelData(5000);
            meta.setColor(Color.fromRGB(
                    Integer.valueOf(hexColor.substring(1, 3), 16),
                    Integer.valueOf(hexColor.substring(3, 5), 16),
                    Integer.valueOf(hexColor.substring(5, 7), 16)));
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createItem(Material material, String name, String... lore) {
        return createItem(material, 1, name, lore);
    }

    public static ItemStack createItem(Material material, int amount, String name, String... lore) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (lore.length > 0) {
                meta.setLore(Arrays.asList(lore));
            }
            meta.setCustomModelData(5000);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack createGuiItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(lore);
            meta.setCustomModelData(5000);
            item.setItemMeta(meta);
        }
        return item;
    }
}