package dev.mj80.valorant.valorantbot;

import net.md_5.bungee.api.ChatColor;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class CoreUtils {
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
    public static void writeFile(File file, String string) {
        file.getParentFile().mkdirs();
        try {
            if (!file.createNewFile()) new FileWriter(file, false).close();
            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write(string);
            fileWriter.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    public static String readResource(String fileName) {
        StringBuilder builder = new StringBuilder();
        Scanner scanner = new Scanner(ValorantBot.getInstance().getResource(fileName), StandardCharsets.UTF_8.name());
        while (scanner.hasNextLine()) {
            builder.append(scanner.nextLine());
        }
        if (builder.toString().isBlank()) {
            scanner.close();
            return "null";
        }
        scanner.close();
        return builder.toString();
    }
}
