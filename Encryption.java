
import java.util.Scanner;


public class Encryption {

    public static String encrypt (String plainText, String key) {

        StringBuilder cipherText = new StringBuilder();

        key = key.toUpperCase();

        for(int i = 0; i  < plainText.length(); i++) {
            if(Character.isLetter(plainText.charAt(i))){
                if(Character.isUpperCase(plainText.charAt(i))) {

                }
                else {
                    
                }
            }
            
        }

        return null;
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the plaintext:");
        String plainText = scanner.nextLine();

        System.out.println("Please enter your secret key:");
        String key = scanner.nextLine();

        System.out.println("Encrypted text: " + encrypt(plainText, key));
       
        scanner.close();
        
    }
    
}
