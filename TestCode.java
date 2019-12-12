/* Author: Joel Salomon
   Date:   10/16/209
   RSA Encryption Algorithm
   This program takes a hard-coded message consisting of letters and
   primitive punctuation and converts each character to a 2 digit number code.
   Each grouping of 2 numeric characters is then encrypted as a 4 digit number using
   a public key. To find this public key, we use the extended euclidean algorithm to
   given primes p, q, and a prime private key > 1. After the message is encrypted, it is
   then decrypted using the private key and the decrypted numeric message is converted
   back to the appropriate characters and printed.
 */

import java.io.*;
import java.util.*;

public class ProtectFiles {
    public static int addSubValue = 10;
    public static String[] values = {"A","B","C","D","E","F","G","H","I","J","K","L",
            "M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","!","@","#","$",
            "%","^","&","*","(",")","-","_","=","+","[","{","]","}","\\","|",";",":",
            "'","\"",",","<",".",">","/","?"," ","0","1","2","3","4","5","6","7","8",
            "9","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q",
            "r","s","t","u","v","w","x","y","z","~","`"};
    // This recursive function is an extension to euclids algorithm
    // and returns an array of values [gcd, i, j] such that
    // gcd(p,q) = in + jm
    // extended Euclidean Algorithm
    public static long[] extendedGCD(long p, long q) {
        if (q == 0)
            return new long[] { p, 1, 0 };

        long[] vals = extendedGCD(q, p % q);
        long d = vals[0];
        long a = vals[2];
        long b = vals[1] - (p / q) * vals[2];
        return new long[] { d, a, b };
    }

    // This function can either decrypt or encrypt a message of type long given
    // either a publickey or private key that has been converted to a binary string.
    // If binaryMessage = '1' then it decrypts
    // Inputs: binary message, n (p*q), and the message itself.
    // Output: returns encrypted or decrypted message.
    public static long encryptOrDecrypt(String binaryMessage, long n, long message) {
        long a = 1;
        for (int i = 0; i < binaryMessage.length(); i++) {
            if (binaryMessage.charAt(i) == '1')
                a = ((a*a)%n * message%n)%n;
            else if (binaryMessage.charAt(i) == '0')
                a = (a*a)%n;
            else
                System.out.println("Invalid binary number");
        }
        return a;
    }

    public static List<Long> convertMessage(String message) {
        //Step 7 convert message with respect to addSubValue.
        List<Long> convertedMessage = new ArrayList<>();
        boolean isCharValid;
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            //Convert char to string for comparison.
            String character = String.valueOf(c);
            isCharValid = false;
            for (int j = 0; j < values.length; j++) {
                if (values[j].equals(character)) {
                    isCharValid = true;
                    long append = j + addSubValue;
                    convertedMessage.add(append);
                }
            }
            if (isCharValid == false) {
                System.out.println("  character = " + character);
                System.out.println("Message not valid for encryption.");
                return convertedMessage;
            }
        }
        return convertedMessage;
    }

    // This function simply just strips symbols from lists for print formatting
    public static String stripSymbols(String message) {
        String joinedMessage = message;
        joinedMessage = joinedMessage.replace("[","");
        joinedMessage = joinedMessage.replace("]","");
        joinedMessage = joinedMessage.replace(",","");
        return joinedMessage;
    }

    public static void main(String[] args) {
        // Get user input to either encrypt or decrypt
        Scanner userInput = new Scanner(System.in);
        System.out.println("Choose an option:\n1. Encrypt\n2. Decrypt");
        String option = userInput.nextLine();
        System.out.println("Option Selected: " + option);
        if (option.equals("1")) {
            Scanner userInput2E = new Scanner(System.in);
            System.out.println("Are you sure you want to Encrypt all files?\n" +
                               "Choose an option:\n1. Yes\n2. No");
            String option2E = userInput2E.nextLine();
            if (option2E.equals("1")) {

            }
            else if (option2E.equals("2")) {

            }
            else {
                System.out.println("Invalid Input");
                return;
            }

        }


        //Step 1 find two 4-digit primes p and q
        long p = 9967;
        long q = 9973;
        //System.out.println("p = " + p + "  q = " + q);

        //Step 2 & 3:  set phi = p * q and n = p*q
        long n = p*q;
        long phi = (p-1) * (q-1);
        //System.out.println("phi = " + phi + " n = " + n);

        //Step 4 find number h > 1 so that gcd(h,phi) = 1. Set private key = h.
        long h = 23;
        System.out.println("private key = " + h);


        //Step 5 find g so that [g]phi * [h]phi = [1]phi
        long[] funcValues = extendedGCD(h, phi); //get Public key using extended GCD
        long g = funcValues[1]; // previous statement returned array where funcValues[1] = j in formula
        while (g < 0) { // if g is negative, we have to add to retrieve the correct value, since modulo cannot
            g += phi;                                                                      // be negative
        }
        System.out.println("public key = " + g);

        //Step 6 get message from user and encode message.
        //  All characters in the values array are equal to their position in the list + 3.
        //  In otherwords, values[position] = position + addSubValue;
        String message = "aaaa  aaaa";
        // join the converted message altogether to setup for grouping by 4 digits
        List<Long> joinedMessage = convertMessage(message);
        String formatMessage = joinedMessage.toString();
        formatMessage = stripSymbols(formatMessage);
        formatMessage = formatMessage.replace(" ","");
        //Group the list by 4 digits and insert each new grouping into a new int array.
        List<String> groupBy4Digits = new ArrayList<>();
        for (int i = 0; i < formatMessage.length(); i += 4) {
            groupBy4Digits.add(formatMessage.substring(i, Math.min(formatMessage.length(), i + 4)));
        }

        // New array with integers instead of strings grouped as 4 digits.
        List<Integer> intMessage = new ArrayList<>();
        for (int i = 0; i < groupBy4Digits.size(); i++) {
            intMessage.add(Integer.parseInt(groupBy4Digits.get(i)));
        }
        String formatMessage2 = intMessage.toString();
        formatMessage2 = stripSymbols(formatMessage2);
        System.out.println("Encoded message grouped by 2 characters: " + formatMessage2);
        System.out.println("");



        //Step 8 Encrypt each 4-digit number group using the public key.
        List<Long> encryptedMessage = new ArrayList<>();
        long modulo = n;
        String pubKey = Long.toBinaryString(g);
        String privKey = Long.toBinaryString(h);
        for (int i = 0; i < intMessage.size(); i++) {
            long message_to_encrypt = intMessage.get(i);
            long encryptedElement = encryptOrDecrypt(pubKey, modulo, message_to_encrypt);
            encryptedMessage.add(encryptedElement);
        }
        formatMessage = encryptedMessage.toString();
        formatMessage = stripSymbols(formatMessage);
        System.out.println("Encryption for the grouped digits: " + formatMessage);
        System.out.println("");

        //Step 9: Decrypt step 8 using the private key and get the original text back.
        List<Long> decryptedMessage = new ArrayList<>();
        for (int i = 0; i < intMessage.size(); i++) {
            long message_to_decrypt = encryptedMessage.get(i);
            long decryptedElement = encryptOrDecrypt(privKey, modulo, message_to_decrypt);
            decryptedMessage.add(decryptedElement);
        }
        formatMessage = decryptedMessage.toString();
        formatMessage = stripSymbols(formatMessage);
        System.out.println("Decryption back to encoded message: " + formatMessage);
        System.out.println("");

        // join the message altogether to setup for grouping by 2 digits
        // to convert the list back to characters.
        formatMessage = decryptedMessage.toString();
        formatMessage = stripSymbols(formatMessage);
        formatMessage = formatMessage.replace(" ","");

        //Group the list by 2 digits and insert each new grouping into a new array.
        List<String> groupBy2Digits = new ArrayList<>();
        for (int i = 0; i < formatMessage.length(); i += 2) {
            groupBy2Digits.add(formatMessage.substring(i, Math.min(formatMessage.length(), i + 2)));
        }

        System.out.println("Original Message  = " + message);
        System.out.print("Decrypted Message = ");

        //Print out decrypted message.
        for (int i = 0; i < groupBy2Digits.size(); i++) {
            String c = groupBy2Digits.get(i);
            int character = Integer.parseInt(c) - addSubValue;
            System.out.print(values[character]);
        }
        System.out.println("");
    }
}

