
import java.util.*;

public class Crack {

    // Expected frequency of each letter in English text
    private static final double[] ENGLISH_FREQ = {
        8.167, 1.492, 2.782, 4.253, 12.702, 2.228,
        2.015, 6.094, 6.966, 0.153, 0.772, 4.025,
        2.406, 6.749, 7.507, 1.929, 0.095, 5.987,
        6.327, 9.056, 2.758, 0.978, 2.360, 0.150,
        1.974, 0.074
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the encrypted text:");
        String cipherText = scanner.nextLine();

        System.out.println("Please enter the key length:");
        int keyLen = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Step 1: Clean cipher text (remove non-letter characters)
        // String cleanedCipherText = cipherText.replaceAll("[^A-Za-z]", "");
        // Step 2: Divide the cipher text
        String[] groups = divideCipherText(cipherText, keyLen);

        // Step 3: Analyze each group to find key characters
        char[] key = new char[keyLen];
        for (int i = 0; i < keyLen; i++) {
            key[i] = findKeyChar(groups[i]);
        }

        String keyStr = new String(key);
        System.out.println("Discovered key: " + keyStr);

        // Step 4: Decrypt the cipher text
        String plainText = decrypt(cipherText, keyStr);
        System.out.println("Decrypted text: " + plainText);

        scanner.close();
    }

    // Divide cipher text into keyLen groups
    private static String[] divideCipherText(String text, int keyLen) {
        String[] groups = new String[keyLen];
        Arrays.fill(groups, "");

        for (int i = 0; i < text.length(); i++) {
            groups[i % keyLen] += text.charAt(i);
        }
        return groups;
    }

    // Find the key character for a group using frequency analysis
    private static char findKeyChar(String group) {
        double minChiSquared = Double.MAX_VALUE;
        int bestShift = 0;

        for (int shift = 0; shift < 26; shift++) {
            String shiftedText = shiftText(group, shift);
            double chiSquared = calculateChiSquared(shiftedText);
            if (chiSquared < minChiSquared) {
                minChiSquared = chiSquared;
                bestShift = shift;
            }
        }

        // Convert shift to corresponding key character
        return (char) ('A' + bestShift);
    }

    // Shift text by a given shift amount
    private static String shiftText(String text, int shift) {
        StringBuilder shifted = new StringBuilder();
        for (char c : text.toCharArray()) {
            char base = Character.isUpperCase(c) ? 'A' : 'a';
            int shiftedChar = ((c - base - shift + 26) % 26) + base;
            shifted.append((char) shiftedChar);
        }
        return shifted.toString();
    }

    // Calculate chi-squared statistic for a given text
    private static double calculateChiSquared(String text) {
        int[] letterCounts = new int[26];
        int totalLetters = 0;

        // Count letter frequencies
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                letterCounts[Character.toUpperCase(c) - 'A']++;
                totalLetters++;
            }
        }

        double chiSquared = 0.0;
        for (int i = 0; i < 26; i++) {
            double observed = letterCounts[i];
            double expected = ENGLISH_FREQ[i] * totalLetters / 100;
            chiSquared += Math.pow(observed - expected, 2) / (expected + 1e-6); // Add a small number to avoid division by zero
        }

        return chiSquared;
    }

    // Decrypt the cipher text using the discovered key
    public static String decrypt(String cipherText, String key) {

        StringBuilder plainText = new StringBuilder();

        key = key.toUpperCase();

        for (int i = 0, j = 0; i < cipherText.length(); i++) {
            char currCiphChar = cipherText.charAt(i);

            if (Character.isLetter(currCiphChar)) {

                char currKeyChar = key.charAt(j % key.length());

                int shiftKeyLen = currKeyChar - 'A';

                int shiftCiphLen = Character.isUpperCase(currCiphChar)
                        ? currCiphChar - 'A'
                        : currCiphChar - 'a';

                char plainChar = Character.isUpperCase(currCiphChar)
                        ? (char) ((shiftCiphLen - shiftKeyLen + 26) % 26 + 'A')
                        : (char) ((shiftCiphLen - shiftKeyLen + 26) % 26 + 'a');

                plainText.append(plainChar);

                j++;
            } else {
                plainText.append(currCiphChar);
            }

        }

        return plainText.toString();
    }
}
