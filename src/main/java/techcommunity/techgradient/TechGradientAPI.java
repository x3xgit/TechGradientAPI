package techcommunity.techgradient;

import net.md_5.bungee.api.ChatColor;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TechGradientAPI {
    private static final Pattern[] GRADIENT_PATTERNS = {Pattern.compile("<#([A-Fa-f0-9]{6})>(.*?)</#([A-Fa-f0-9]{6})>"), Pattern.compile("\\{#([A-Fa-f0-9]{6})>}(.+?)\\{#([A-Fa-f0-9]{6})<}"), Pattern.compile("<\\$#([A-Fa-f0-9]{6})>(.*?)<\\$#([A-Fa-f0-9]{6})>"), Pattern.compile("<gradient:#([A-Fa-f0-9]{6})>(.*?)<gradient:/#([A-Fa-f0-9]{6})>")};

    public static String applyAll(String text)
    {
        return applyGradient(ChatColor.translateAlternateColorCodes('&', text));
    }

    public static String applyGradient(String input)
    {
        String result = input;

        for (Pattern pattern : GRADIENT_PATTERNS)
            result = applyGradientPattern(input, pattern);

        return result;
    }

    public static String applyChatColors(char char1, String input)
    {
        return ChatColor.translateAlternateColorCodes(char1, input);
    }

    private static String applyGradientPattern(String input, Pattern pattern)
    {
        Matcher matcher = pattern.matcher(input);
        StringBuilder result = new StringBuilder();
        int lastEnd = 0;
        while (matcher.find()) {
            result.append(input, lastEnd, matcher.start());
            String startColorHex = matcher.group(1);
            String text = matcher.group(2);
            String endColorHex = matcher.group(3);
            Color startColor = Color.decode(startColorHex);
            Color endColor = Color.decode(endColorHex);
            result.append(createGradientTextWithCodes(text, startColor, endColor));
            lastEnd = matcher.end();
        }
        result.append(input.substring(lastEnd));
        return result.toString();
    }

    private static String createGradientTextWithCodes(String text, Color startColor, Color endColor)
    {
        StringBuilder gradientText = new StringBuilder();
        int length = text.length();
        String plainText = text.replaceAll("(?i)" + ChatColor.COLOR_CHAR + "[0-9A-FK-OR]", "");
        int plainCharCount = plainText.length();
        int colorIndex = 0;
        String currentFormatting = "";
        for (int i = 0; i < length; i++) {
            if (text.charAt(i) == ChatColor.COLOR_CHAR) {
                gradientText.append(ChatColor.COLOR_CHAR);
                i++;
                currentFormatting += ChatColor.COLOR_CHAR + text.substring(i, i + 1);
                gradientText.append(text.charAt(i));
            } else {
                float ratio = (float) colorIndex / (float) (plainCharCount - 1);
                int red = (int) (startColor.getRed() * (1 - ratio) + endColor.getRed() * ratio);
                int green = (int) (startColor.getGreen() * (1 - ratio) + endColor.getGreen() * ratio);
                int blue = (int) (startColor.getBlue() * (1 - ratio) + endColor.getBlue() * ratio);
                Color currentColor = new Color(red, green, blue);
                gradientText.append(ChatColor.of(currentColor)).append(currentFormatting).append(text.charAt(i));
                colorIndex++;
            }
        }
        return gradientText.toString();
    }
}