package dev.mj80.valorant.valorantbot;

import net.md_5.bungee.api.ChatColor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;

public class CoreUtils {
    @SuppressWarnings("deprecation")
    public static String translateAlternateColorCodes(char codeChar, String message) {
        message = message.replace("<RED>", codeChar+"#fd303a")
                .replace("<ORANGE>", codeChar+"#fcba03").replace("<YELLOW>", codeChar+"#fbfe3b")
                .replace("<DARKGREEN>", codeChar+"#2bba7c").replace("<GREEN>", codeChar+"#3afba7")
                .replace("<BLUE>", codeChar+"#31afec").replace("<PURPLE>", codeChar+"#a452e3")
                .replace("<MAGENTA>", codeChar+"#ea4adf").replace("<PINK>", codeChar+"#f6adc6")
                .replace("<GRAY>", codeChar+"#a4c1c2").replace("<DARKGRAY>", codeChar+"#787667");
        char[] chars = message.toCharArray();
        StringBuilder builder = new StringBuilder();
        String colorHex = "";
        boolean isHex = false;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == codeChar && i < chars.length - 1 && chars[i+1] == '#') {
                colorHex = "";
                isHex = true;
            } else if (isHex) {
                colorHex += chars[i];
                isHex = colorHex.length() < 7;
                if (!isHex)
                    builder.append(ChatColor.of(colorHex));
            } else
                builder.append(chars[i]);
        }
        return ChatColor.translateAlternateColorCodes(codeChar, builder.toString());
    }
    public static String readFile(String fileName) {
        return readFile(new File(ValorantBot.getInstance().getDataFolder() + File.separator + fileName));
    }
    public static String readFile(File file) {
        try {
            if (file.getParentFile().mkdirs()) {
                ValorantBot.getInstance().getLogger().log(Level.INFO, "Created plugin folder.");
            }
            if (file.createNewFile()) {
                ValorantBot.getInstance().getLogger().log(Level.INFO, "Created " + file.getName() + " in the plugin's folder.");
            }
            StringBuilder text = new StringBuilder();
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine());
            }
            if (text.toString().isBlank()) {
                scanner.close();
                return "null";
            }
            scanner.close();
            return text.toString();
        } catch (IOException exception) {
            ValorantBot.getInstance().getLogger().log(Level.WARNING, exception.getMessage());
            return "null";
        }
    }
    public static void writeFile(File file, String string) {
        if(file.getParentFile().mkdirs()) {
            ValorantBot.getInstance().getLogger().log(Level.INFO, "Created plugin folder.");
        }
        try {
            if (!file.createNewFile()) new FileWriter(file, false).close();
            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write(string);
            fileWriter.close();
        } catch (IOException exception) {
            ValorantBot.getInstance().getLogger().log(Level.WARNING, exception.getMessage());
        }
    }
}
