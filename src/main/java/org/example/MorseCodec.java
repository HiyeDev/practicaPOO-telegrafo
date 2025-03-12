package org.example;

import java.util.HashMap;
import java.util.Map;

public class MorseCodec {

    private static final Map<Character, String> CHAR_TO_MORSE = new HashMap<>();
    private static final Map<String, Character> MORSE_TO_CHAR = new HashMap<>();

    static {
        CHAR_TO_MORSE.put('A', ".-");
        CHAR_TO_MORSE.put('B', "-...");
        CHAR_TO_MORSE.put('C', "-.-.");
        CHAR_TO_MORSE.put('D', "-..");
        CHAR_TO_MORSE.put('E', ".");
        CHAR_TO_MORSE.put('F', "..-.");
        CHAR_TO_MORSE.put('G', "--.");
        CHAR_TO_MORSE.put('H', "....");
        CHAR_TO_MORSE.put('I', "..");
        CHAR_TO_MORSE.put('J', ".---");
        CHAR_TO_MORSE.put('K', "-.-");
        CHAR_TO_MORSE.put('L', ".-..");
        CHAR_TO_MORSE.put('M', "--");
        CHAR_TO_MORSE.put('N', "-.");
        CHAR_TO_MORSE.put('O', "---");
        CHAR_TO_MORSE.put('P', ".--.");
        CHAR_TO_MORSE.put('Q', "--.-");
        CHAR_TO_MORSE.put('R', ".-.");
        CHAR_TO_MORSE.put('S', "...");
        CHAR_TO_MORSE.put('T', "-");
        CHAR_TO_MORSE.put('U', "..-");
        CHAR_TO_MORSE.put('V', "...-");
        CHAR_TO_MORSE.put('W', ".--");
        CHAR_TO_MORSE.put('X', "-..-");
        CHAR_TO_MORSE.put('Y', "-.--");
        CHAR_TO_MORSE.put('Z', "--..");

        CHAR_TO_MORSE.put('0', "-----");
        CHAR_TO_MORSE.put('1', ".----");
        CHAR_TO_MORSE.put('2', "..---");
        CHAR_TO_MORSE.put('3', "...--");
        CHAR_TO_MORSE.put('4', "....-");
        CHAR_TO_MORSE.put('5', ".....");
        CHAR_TO_MORSE.put('6', "-....");
        CHAR_TO_MORSE.put('7', "--...");
        CHAR_TO_MORSE.put('8', "---..");
        CHAR_TO_MORSE.put('9', "----.");

        CHAR_TO_MORSE.put(' ', "/");

        for (Map.Entry<Character, String> entry : CHAR_TO_MORSE.entrySet()) {
            MORSE_TO_CHAR.put(entry.getValue(), entry.getKey());
        }
    }

    public static String encode (String message) {
        if (message == null || message.isEmpty()) {
            return "";
        }

        StringBuilder morseBuilder = new StringBuilder();
        message = message.toUpperCase();

        for (int i= 0; i < message.length(); i++) {
            char character = message.charAt(i);

            if (CHAR_TO_MORSE.containsKey(character)) {
                morseBuilder.append(CHAR_TO_MORSE.get(character));
            } else {
                morseBuilder.append("*");
            }

            if (i < message.length() - 1) {
                morseBuilder.append(" ");
            }

        }

        return morseBuilder.toString();
    }

    public static String decode(String morseMessage) {
        if (morseMessage == null || morseMessage.isEmpty()) {
            return "";
        }

        StringBuilder textBuilder = new StringBuilder();
        String[] morseCharacter = morseMessage.split(" ");

        for (String morseChar: morseCharacter) {
            if (MORSE_TO_CHAR.containsKey(morseChar)) {
                textBuilder.append(MORSE_TO_CHAR.get(morseChar));
            } else if ("/".equals(morseChar)) {
                textBuilder.append(' ');
            } else {
                textBuilder.append("*");
            }
        }

        return textBuilder.toString();
    }
}
