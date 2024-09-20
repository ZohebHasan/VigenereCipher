import java.util.*;

public class CompleteCrack {

    public static double calcIndexConincidence(String text) {
        Map<Character, Integer> letterCount = new HashMap<>();
        int totalLetters = text.length();

        for (char c : text.toCharArray()) {
            letterCount.put(c, letterCount.getOrDefault(c, 0) + 1);
        }

        
        double indexCoincidence = 0.0;
        for (int count : letterCount.values()) {
            indexCoincidence += count * (count - 1);
        }

        indexCoincidence /= (totalLetters * (totalLetters - 1));

        return indexCoincidence;
    }

    public static int estimateKeyLength(String cipherText) {
        int maxKeyLength = 20;
        double bestIndexCoincidence = 0.0;
        int bestKeyLength = 1;

        for (int keyLength = 1; keyLength <= maxKeyLength; keyLength++) {
            double avgIndexConincidence = 0.0;
            for (int i = 0; i < keyLength; i++) {
                StringBuilder group = new StringBuilder();

                for (int j = i; j < cipherText.length(); j += keyLength) {
                    group.append(cipherText.charAt(j));
                }

                avgIndexConincidence += calcIndexConincidence(group.toString());
            }
            avgIndexConincidence /= keyLength;

            if (Math.abs(avgIndexConincidence - 0.068) < Math.abs(bestIndexCoincidence - 0.068)) {
                bestIndexCoincidence = avgIndexConincidence;
                bestKeyLength = keyLength;
            }
        }

        return bestKeyLength;
    }


    public static void main(String[] args) {
        Scanner stdin = new Scanner(System.in);
    

        System.out.println("Enter your encrypted text: ");
        String originalEncryptedText = stdin.nextLine(); 

        String encryptedText = originalEncryptedText.replaceAll("[^A-Za-z]", ""); 

        System.out.println("Loading..");

        int estimatedKeyLength = estimateKeyLength(encryptedText);
        System.out.println("Estimated Key Length: " + estimatedKeyLength);

        String[] groups = CrackWithKeyLen.divideEncryptedText(encryptedText, estimatedKeyLength);

        char[] key = new char[estimatedKeyLength];
        for (int i = 0; i < estimatedKeyLength; i++) {
            key[i] = CrackWithKeyLen.findKeyCharByFrequency(groups[i]);
        }

        String keyStr = new String(key);
        System.out.println("Discovered key: " + keyStr);


        String decryptedText = Decryption.decrypt(encryptedText, keyStr);



        String formattedPlainText = CrackWithKeyLen.reinsertSpecialCharacters(originalEncryptedText, decryptedText);
        System.out.println("Decrypted text: " + formattedPlainText);

        stdin.close();
    }
}
