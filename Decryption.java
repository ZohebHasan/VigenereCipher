
import java.util.Scanner;

public class Decryption {

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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the encrypted text:");
        String cipherText = scanner.nextLine();

        System.out.println("Please enter your secret key:");
        String key = scanner.nextLine();

        System.out.println("Decrypted text: " + decrypt(cipherText, key));

        scanner.close();

    }

}
