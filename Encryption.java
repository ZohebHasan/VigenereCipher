
import java.util.Scanner;

public class Encryption {

    public static String encrypt(String plainText, String key) {

        StringBuilder cipherText = new StringBuilder(); // I'm using string builder as this is one of the most popular libary for building strings with characters

        key = key.toUpperCase(); //converting key to uppercase to avoid edge cases.


        for (int i = 0, j = 0; i < plainText.length(); i++) {  //just a standard for loop from 0 to plaintext.length() - 1
            char currPlainChar = plainText.charAt(i);

            if (Character.isLetter(currPlainChar)) {

                char currKeyChar = key.charAt(j % key.length());

                int shiftKeyLen = currKeyChar - 'A';
            
                int shiftPlainLen = Character.isUpperCase(currPlainChar) 
                            ? currPlainChar - 'A' 
                            : currPlainChar - 'a';

                char encrChar = Character.isUpperCase(currPlainChar) 
                            ?  (char) ((shiftPlainLen + shiftKeyLen) % 26 + 'A') 
                            :  (char) ((shiftPlainLen + shiftKeyLen) % 26 + 'a');
                
                 cipherText.append(encrChar);
                            
                j++;
            }
             else {
                cipherText.append(currPlainChar);
            }

        }

        return cipherText.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the plaintext:");
        String plainText = scanner.nextLine();

        System.out.println("Please enter your secret key:");
        String key = scanner.nextLine();

        System.out.println("Encrypted text: \n" + encrypt(plainText, key));

        scanner.close();

    }

}
