package dev.sterner.dogma.foundation.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import org.apache.logging.log4j.core.pattern.TextRenderer;
import team.lodestar.lodestone.systems.easing.Easing;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.util.FastColor.ARGB32.color;

public class ComponentUtils {
    /**
     * stylized two strings with different colors on the same line of Text
     *
     * @param first      string to be stylized
     * @param second     string to be stylized
     * @param nameColor  color of first string
     * @param valueColor color fo second string
     * @return Text styled to "first: second"
     */
    public static Component formattedFromTwoStrings(String first, String second, int nameColor, int valueColor) {
        final MutableComponent name = styledText(first, nameColor);
        final MutableComponent value = styledText(second, valueColor);

        return name.append(Component.literal(": ")).append(value);
    }

    public static MutableComponent styledText(Object string, int color) {
        return Component.literal(string.toString()).setStyle(Style.EMPTY.withColor(color));
    }

    /**
     * turns "a_fun, string.minecraft:stick" to "A Fun, String Minecraft:stick"
     *
     * @param string string to be formatted
     * @return formatted string
     */
    public static String capitalizeString(String string) {
        if (string == null || string.trim().isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder(string.toLowerCase());
        boolean capitalizeNext = true;

        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            if (Character.isWhitespace(c) || c == '.' || c == '\'' || c == '_') {
                capitalizeNext = true;
            } else if (capitalizeNext && Character.isLetter(c)) {
                sb.setCharAt(i, Character.toUpperCase(c));
                capitalizeNext = false;
            }
        }

        return sb.toString();
    }

    public static void renderWrappingText(GuiGraphics guiGraphics, String text, int x, int y, int w) {
        net.minecraft.client.gui.Font font = Minecraft.getInstance().font;
        text = Component.translatable(text).getString() + "\n";
        java.util.List<String> lines = new ArrayList<>();

        boolean italic = false;
        boolean bold = false;
        boolean strikethrough = false;
        boolean underline = false;
        boolean obfuscated = false;

        StringBuilder line = new StringBuilder();
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char chr = text.charAt(i);
            if (chr == ' ' || chr == '\n') {
                if (word.length() > 0) {
                    if (font.width(line.toString()) + font.width(word.toString()) > w) {
                        line = newLine(lines, italic, bold, strikethrough, underline, obfuscated, line);
                    }
                    line.append(word).append(' ');
                    word = new StringBuilder();
                }

                String noFormatting = ChatFormatting.stripFormatting(line.toString());

                if (chr == '\n' && !(noFormatting == null || noFormatting.isEmpty())) {
                    line = newLine(lines, italic, bold, strikethrough, underline, obfuscated, line);
                }
            } else if (chr == '$') {
                if (i != text.length() - 1) {
                    char peek = text.charAt(i + 1);
                    switch (peek) {
                        case 'i' -> {
                            word.append(ChatFormatting.ITALIC);
                            italic = true;
                            i++;
                        }
                        case 'b' -> {
                            word.append(ChatFormatting.BOLD);
                            bold = true;
                            i++;
                        }
                        case 's' -> {
                            word.append(ChatFormatting.STRIKETHROUGH);
                            strikethrough = true;
                            i++;
                        }
                        case 'u' -> {
                            word.append(ChatFormatting.UNDERLINE);
                            underline = true;
                            i++;
                        }
                        case 'k' -> {
                            word.append(ChatFormatting.OBFUSCATED);
                            obfuscated = true;
                            i++;
                        }
                        default -> word.append(chr);
                    }
                } else {
                    word.append(chr);
                }
            } else if (chr == '/') {
                if (i != text.length() - 1) {
                    char peek = text.charAt(i + 1);
                    if (peek == '$') {
                        italic = bold = strikethrough = underline = obfuscated = false;
                        word.append(ChatFormatting.RESET);
                        i++;
                    } else
                        word.append(chr);
                } else
                    word.append(chr);
            } else {
                word.append(chr);
            }
        }

        for (int i = 0; i < lines.size(); i++) {
            String currentLine = lines.get(i);
            renderRawText(guiGraphics, currentLine, x, y + i * (font.lineHeight + 1), getTextGlow(i / 4f));
        }
    }

    private static StringBuilder newLine(List<String> lines, boolean italic, boolean bold, boolean strikethrough, boolean underline, boolean obfuscated, StringBuilder line) {
        lines.add(line.toString());
        line = new StringBuilder();
        if (italic) line.append(ChatFormatting.ITALIC);
        if (bold) line.append(ChatFormatting.BOLD);
        if (strikethrough) line.append(ChatFormatting.STRIKETHROUGH);
        if (underline) line.append(ChatFormatting.UNDERLINE);
        if (obfuscated) line.append(ChatFormatting.OBFUSCATED);
        return line;
    }

    public static void renderText(GuiGraphics guiGraphics, String text, int x, int y) {
        renderText(guiGraphics, Component.translatable(text), x, y, getTextGlow(0));
    }

    public static void renderText(GuiGraphics guiGraphics, net.minecraft.network.chat.Component component, int x, int y) {
        String text = component.getString();
        renderRawText(guiGraphics, text, x, y, getTextGlow(0));
    }

    public static void renderText(GuiGraphics guiGraphics, String text, int x, int y, float glow) {
        renderText(guiGraphics, Component.translatable(text), x, y, glow);
    }

    public static void renderText(GuiGraphics guiGraphics, Component component, int x, int y, float glow) {
        String text = component.getString();
        renderRawText(guiGraphics, text, x, y, glow);
    }

    private static void renderRawText(GuiGraphics guiGraphics, String text, int x, int y, float glow) {
        Font font = Minecraft.getInstance().font;

        glow = Easing.CUBIC_IN.ease(glow, 0, 1, 1);
        int r = (int) Mth.lerp(glow, 163, 227);
        int g = (int) Mth.lerp(glow, 44, 39);
        int b = (int) Mth.lerp(glow, 191, 228);

        guiGraphics.drawString(font, text, x - 0.5f, y, color(96, 255, 210, 243), false);
        guiGraphics.drawString(font, text, x + 0.5f, y, color(128, 240, 131, 232), false);
        guiGraphics.drawString(font, text, x, y - 0.5f, color(128, 255, 183, 236), false);
        guiGraphics.drawString(font, text, x, y + 0.5f, color(96, 236, 110, 226), false);

        guiGraphics.drawString(font, text, x, y, color(255, r, g, b), false);
    }

    public static float getTextGlow(float offset) {
        return Mth.sin(offset + Minecraft.getInstance().player.level().getGameTime() / 40f) / 2f + 0.5f;
    }

    public static boolean isHovering(double mouseX, double mouseY, int posX, int posY, int width, int height) {
        return mouseX > posX && mouseX < posX + width && mouseY > posY && mouseY < posY + height;
    }
}
