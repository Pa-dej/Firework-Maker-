package me.padej.fireworkMaker;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;

public class FireworkGeneratorGUI implements Listener {

    private static final String GUI_TITLE = ChatColor.of("#3e3546") + "Firework Generator";
    private static final boolean[] selectedPatterns = new boolean[7];
    public static final Map<Player, List<String>> playerColors = new HashMap<>();
    private static final Map<Player, Integer> flightDurations = new HashMap<>();
    private static final File fireworksFile = new File("plugins/fireworks.txt");
    private static double totalSpent = 0.0;

    public FireworkGeneratorGUI(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        loadTotalSpent();
    }

    private void loadTotalSpent() {
        try {
            if (!fireworksFile.exists()) fireworksFile.createNewFile();
            BufferedReader reader = new BufferedReader(new FileReader(fireworksFile));
            String line = reader.readLine();
            if (line != null) totalSpent = Double.parseDouble(line);
            reader.close();
        } catch (IOException | NumberFormatException e) {
            Bukkit.getLogger().warning("Не удалось загрузить сумму потраченных денег: " + e.getMessage());
        }
    }

    private void updateTotalSpent(double amount) {
        totalSpent += amount;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fireworksFile));
            writer.write(String.valueOf(totalSpent));
            writer.close();
        } catch (IOException e) {
            Bukkit.getLogger().warning("Не удалось записать сумму потраченных денег: " + e.getMessage());
        }
    }

    public static void openGUI(Player player) {
        if (!flightDurations.containsKey(player)) flightDurations.put(player, 2);
        if (!playerColors.containsKey(player)) playerColors.put(player, ColorManager.generateRandomHexColors(7));
        Inventory gui = Bukkit.createInventory(player, 54, GUI_TITLE);
        setColoredArmor(gui, playerColors.get(player));
        GUIUtils.setupGUIItems(gui, playerColors.get(player));
        updateGUI(gui, player);
        player.openInventory(gui);
        player.playSound(player.getLocation(), Sound.ITEM_BUNDLE_DROP_CONTENTS, 1, 1.1F);
    }

    private static void updateGUI(Inventory gui, Player player) {
        for (int i = 0; i < selectedPatterns.length; i++) {
            ItemStack item = gui.getItem(10 + i);
            if (item != null) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    List<String> lore = meta.getLore() != null ? new ArrayList<>(meta.getLore()) : new ArrayList<>();

                    // Удаляем старый индикатор паттерна
                    lore.removeIf(line -> line.contains("□") || line.contains("■"));

                    // Добавляем новый индикатор паттерна
                    if (selectedPatterns[i]) {
                        meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
                        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        lore.add(0, "§a■§f┇§c□");
                    } else {
                        meta.removeEnchant(Enchantment.ARROW_DAMAGE);
                        lore.add(0, "§a□§f┇§c■");
                    }
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                }
            } else {
                Bukkit.getLogger().warning("Item at slot " + (10 + i) + " is null");
            }
        }

        // Очищаем зачарования для слотов с длительностью полета
        for (int i = 30; i <= 32; i++) {
            ItemStack item = gui.getItem(i);
            if (item != null) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    meta.removeEnchant(Enchantment.ARROW_DAMAGE);
                    item.setItemMeta(meta);
                }
            }
        }

        // Устанавливаем зачарование на текущий элемент длительности полета
        for (Map.Entry<Player, Integer> entry : flightDurations.entrySet()) {
            int flightDuration = entry.getValue();
            ItemStack item = gui.getItem(29 + flightDuration);
            if (item != null) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    item.setItemMeta(meta);
                }
            }
        }

        // Обновляем описание мешочка
        ItemStack bundleItem = gui.getItem(41);
        if (bundleItem != null) {
            ItemMeta bundleMeta = bundleItem.getItemMeta();
            if (bundleMeta != null) {
                int fireworkAmount = gui.getItem(39).getAmount();
                int totalPrice = FireworkGenerator.calculateTotalPrice(selectedPatterns, flightDurations.getOrDefault(player, 1), fireworkAmount);
                double balance = FireworkMaker.getEconomy().getBalance(player);
                bundleMeta.setLore(List.of(
                        ChatColor.of("#dfdfdf") + "Цена: " + ChatColor.of("#91db69") + totalPrice + "$",
                        ChatColor.of("#dfdfdf") + "Ваш баланс: " + ChatColor.of("#91db69") + balance + "$"));
                bundleItem.setItemMeta(bundleMeta);
            }
        }
    }

    private static void setColoredArmor(Inventory gui, List<String> colors) {
        for (int i = 0; i < 7; i++) {
            gui.setItem(19 + i, GUIUtils.createColoredLeatherChestplate(colors.get(i)));
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(GUI_TITLE)) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null && clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasCustomModelData()) {
                int customModelData = clickedItem.getItemMeta().getCustomModelData();
                if (customModelData == 5000) { // Обработка только для предметов с CustomModelData == 5000
                    int slot = event.getSlot();

                    if (isEnchantable(clickedItem)) {
                        toggleEnchantment(clickedItem, player, event.getInventory());
                        player.playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1, 2);
                    } else if (clickedItem.getType() == Material.LEATHER_CHESTPLATE && slot >= 19 && slot <= 25) {
                        int index = slot - 19;
                        List<String> colors = playerColors.get(player);
                        colors.set(index, ColorManager.generateRandomHexColors(1).get(0));
                        setColoredArmor(event.getInventory(), colors);
                        player.playSound(player.getLocation(), Sound.UI_LOOM_TAKE_RESULT, .7F, 1);
                    } else if (clickedItem.getType() == Material.SPECTRAL_ARROW) {
                        List<String> colors = ColorManager.generateRandomHexColors(7);
                        playerColors.put(player, colors);
                        setColoredArmor(event.getInventory(), colors);
                        player.playSound(player.getLocation(), Sound.UI_LOOM_SELECT_PATTERN, .7F, 1);
                    } else if (clickedItem.getType() == Material.FIREWORK_ROCKET && slot == 39) {
                        ItemStack fireworkItem = clickedItem;
                        int fireworkAmount = fireworkItem.getAmount();
                        if (event.isLeftClick() && fireworkAmount < 64) {
                            fireworkAmount++;
                        } else if (event.isRightClick() && fireworkAmount > 1) {
                            fireworkAmount--;
                        }
                        updateBundleDescription(event.getInventory(), player, fireworkAmount);
                        fireworkItem.setAmount(fireworkAmount);
                        fireworkItem.setItemMeta(GUIUtils.createItem(Material.FIREWORK_ROCKET, fireworkAmount, ChatColor.of("#fcc22d") + "Количество: " + ChatColor.of("#f9883a") + fireworkAmount).getItemMeta());
                        player.playSound(player.getLocation(), Sound.UI_TOAST_IN, 1, 1F);
                    } else if (slot == 41) {
                        int fireworkAmount = event.getInventory().getItem(39).getAmount();
                        int totalPrice = FireworkGenerator.calculateTotalPrice(selectedPatterns, flightDurations.getOrDefault(player, 1), fireworkAmount);
                        if (FireworkMaker.getEconomy().has(player, totalPrice)) {
                            if (hasInventorySpace(player)) {
                                FireworkMaker.getEconomy().withdrawPlayer(player, totalPrice);
                                FireworkGenerator.giveFireworks(player, fireworkAmount, playerColors.get(player), selectedPatterns, flightDurations.getOrDefault(player, 1));
                                updateTotalSpent(totalPrice);
                                player.playSound(player.getLocation(), Sound.ITEM_BUNDLE_DROP_CONTENTS, 1, 1);
                                player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1, 1);
                                player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 1.3F, 1);

                                // Обновляем описание мешочка после покупки
                                updateBundleDescription(event.getInventory(), player, fireworkAmount);
                            } else {
                                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 1, 1.5F);
                                player.sendMessage(ChatColor.of("#fcc22d") + "Недостаточно места в инвентаре!");
                            }
                        } else {
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 1, 1.5F);
                            player.sendMessage(ChatColor.of("#fcc22d") + "Недостаточно средств!");
                        }
                    } else if (slot >= 30 && slot <= 32) {
                        int fireworkAmount = event.getInventory().getItem(39).getAmount();
                        setFlightDuration(player, slot - 29);
                        if (slot == 30) player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, .5F);
                        if (slot == 31) player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
                        if (slot == 32) player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1.5F);
                        updateGUI(event.getInventory(), player);
                        updateBundleDescription(event.getInventory(), player, fireworkAmount);
                    }
                }
            }
        }
    }


    private boolean isEnchantable(ItemStack item) {
        return item.getType() == Material.STRUCTURE_VOID ||
                item.getType() == Material.FIRE_CHARGE ||
                item.getType() == Material.GOLD_NUGGET ||
                item.getType() == Material.CREEPER_HEAD ||
                item.getType() == Material.FEATHER ||
                item.getType() == Material.GLOWSTONE_DUST ||
                item.getType() == Material.DIAMOND;
    }

    private void toggleEnchantment(ItemStack item, Player player, Inventory gui) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            int index = -1;
            if (item.getType() == Material.STRUCTURE_VOID) index = 0;
            else if (item.getType() == Material.FIRE_CHARGE) index = 1;
            else if (item.getType() == Material.GOLD_NUGGET) index = 2;
            else if (item.getType() == Material.CREEPER_HEAD) index = 3;
            else if (item.getType() == Material.FEATHER) index = 4;
            else if (item.getType() == Material.GLOWSTONE_DUST) index = 5;
            else if (item.getType() == Material.DIAMOND) index = 6;

            if (index >= 0) {
                selectedPatterns[index] = !selectedPatterns[index];
                List<String> lore = meta.getLore() != null ? new ArrayList<>(meta.getLore()) : new ArrayList<>();

                // Удаляем старый индикатор паттерна
                lore.removeIf(line -> line.contains("□") || line.contains("■"));

                // Добавляем новый индикатор паттерна
                if (selectedPatterns[index]) {
                    meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    lore.add(0, "§a■§f|§c□"); // Добавляем в начало, чтобы он был первым
                } else {
                    meta.removeEnchant(Enchantment.ARROW_DAMAGE);
                    lore.add(0, "§a□§f|§c■"); // Добавляем в начало, чтобы он был первым
                }

                // Устанавливаем обновленный лор
                meta.setLore(lore);
                item.setItemMeta(meta);
                updateGUI(gui, player);
            }
        }
    }

    private static void setFlightDuration(Player player, int duration) {
        flightDurations.put(player, duration);
    }

    private void updateBundleDescription(Inventory gui, Player player, int fireworkAmount) {
        ItemStack bundleItem = gui.getItem(41);
        if (bundleItem != null) {
            ItemMeta bundleMeta = bundleItem.getItemMeta();
            if (bundleMeta != null) {
                int totalPrice = FireworkGenerator.calculateTotalPrice(selectedPatterns, flightDurations.getOrDefault(player, 1), fireworkAmount);
                double balance = FireworkMaker.getEconomy().getBalance(player);
                bundleMeta.setLore(List.of(
                        ChatColor.of("#dfdfdf") + "Цена: " + ChatColor.of("#91db69") + totalPrice + "$",
                        ChatColor.of("#dfdfdf") + "Ваш баланс: " + ChatColor.of("#91db69") + balance + "$"));
                bundleItem.setItemMeta(bundleMeta);
            }
        }
    }

    private boolean hasInventorySpace(Player player) {
        Inventory inventory = player.getInventory();
        for (ItemStack item : inventory.getContents()) {
            if (item == null || item.getType() == Material.AIR) {
                return true;
            }
        }
        return false;
    }
}
