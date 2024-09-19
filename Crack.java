import java.util.*;

public class Crack {
    private static final double[] ENGLISH_FREQ = {
        8.167, 1.492, 2.782, 4.253, 12.702, 2.228,
        2.015, 6.094, 6.966, 0.153, 0.772, 4.025,
        2.406, 6.749, 7.507, 1.929, 0.095, 5.987,
        6.327, 9.056, 2.758, 0.978, 2.360, 0.150,
        1.974, 0.074
    };

    private static String[] divideEncryptedText(String text, int keyLen) {
        String[] groups = new String[keyLen];
        Arrays.fill(groups, "");  

        for (int i = 0; i < text.length(); i++) {
            groups[i % keyLen] += text.charAt(i);
        }
        return groups;
    }

    private static char findKeyCharByFrequency(String group) {
        double bestMatch = Double.MAX_VALUE;
        int bestShift = 0;

        for (int shift = 0; shift < 26; shift++) {
            String shiftedText = shiftText(group, shift);
            double matchScore = calculateFrequencyMatchScore(shiftedText);

            if (matchScore < bestMatch) {
                bestMatch = matchScore;
                bestShift = shift;
            }
        }
        return (char) ('A' + bestShift);
    }

    private static String shiftText(String text, int shift) {
        StringBuilder shifted = new StringBuilder();
        for (char c : text.toCharArray()) {
            char base = Character.isUpperCase(c) ? 'A' : 'a';
            int shiftedChar = ((c - base - shift + 26) % 26) + base;
            shifted.append((char) shiftedChar);
        }
        return shifted.toString();
    }

    private static double calculateFrequencyMatchScore(String text) {
        int[] letterCounts = new int[26];
        int totalLetters = 0;
    
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char upperChar = Character.toUpperCase(c); 
                int letterIndex = upperChar - 'A';
                letterCounts[letterIndex]++;      
                totalLetters++;        
            }
        }
    
        double matchScore = 0.0;
        for (int i = 0; i < 26; i++) {
            double observedFreq = (double) letterCounts[i] / totalLetters * 100;
            double expectedFreq = ENGLISH_FREQ[i];
            matchScore += Math.abs(observedFreq - expectedFreq); 
        }
    
        return matchScore;
    }

    private static String decrypt(String cipherText, String key) {
        StringBuilder plainText = new StringBuilder();
        key = key.toUpperCase();

        for (int i = 0, j = 0; i < cipherText.length(); i++) {
            char currCipherChar = cipherText.charAt(i);

            if (Character.isLetter(currCipherChar)) {
                char currKeyChar = key.charAt(j % key.length());
                int shift = currKeyChar - 'A';

                char base = Character.isUpperCase(currCipherChar) ? 'A' : 'a';
                int decryptedChar = ((currCipherChar - base - shift + 26) % 26) + base;
                plainText.append((char) decryptedChar);

                j++;
            } else {
                plainText.append(currCipherChar);  
            }
        }

        return plainText.toString();

    }

    
    private static String reinsertSpecialCharacters(String originalCipherText, String decryptedText) {
        StringBuilder result = new StringBuilder();
        int decryptedIndex = 0;

        for (int i = 0; i < originalCipherText.length(); i++) {
            char currentChar = originalCipherText.charAt(i);

            if (Character.isLetter(currentChar)) {
                result.append(decryptedText.charAt(decryptedIndex));
                decryptedIndex++;
            } else {
                result.append(currentChar);
            }
        }

        return result.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the encrypted text(<150 characters for optimal performance):");
        String originalCipherText = scanner.nextLine(); 

        String cipherText = originalCipherText.replaceAll("[^A-Za-z]", ""); 

        System.out.println("Please enter the key length:");
        int keyLen = scanner.nextInt();
        scanner.nextLine(); 


        String[] groups = divideEncryptedText(cipherText, keyLen);

        char[] key = new char[keyLen];
        for (int i = 0; i < keyLen; i++) {
            key[i] = findKeyCharByFrequency(groups[i]);
        }

        String keyStr = new String(key);
        System.out.println("Discovered key: " + keyStr);

        String decryptedText = decrypt(cipherText, keyStr);

        String formattedPlainText = reinsertSpecialCharacters(originalCipherText, decryptedText);
        System.out.println("Decrypted text: " + formattedPlainText);

        scanner.close();
    }

}