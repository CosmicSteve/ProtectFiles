/*
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

    public static List<Long> encodeMessage(String message) {
        //Step 6 get message from user and encode message.
        //  All characters in the values array are equal to their position in the list + 3.
        //  In otherwords, values[position] = position + addSubValue;
        //Join the converted message altogether to setup for grouping by 4 digits
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
        List<Long> encodedMessage = new ArrayList<>();
        for (int i = 0; i < groupBy4Digits.size(); i++) {
            encodedMessage.add(Long.parseLong(groupBy4Digits.get(i)));
        }
        String formatMessage2 = encodedMessage.toString();
        formatMessage2 = stripSymbols(formatMessage2);
        //System.out.println("Digital message grouping 2 characters: " + formatMessage2);
        System.out.println("");
        return encodedMessage;
    }

    // This function can either decrypt or encrypt a message of type long given
    // either a publickey or private key that has been converted to a binary string.
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
        System.out.println("Private key to decrypt your files = " + h);


        //Step 5 find g so that [g]phi * [h]phi = [1]phi
        long[] funcValues = extendedGCD(h, phi); //get Public key using extended GCD
        long g = funcValues[1]; // previous statement returned array where funcValues[1] = j in formula
        while (g < 0) { // if g is negative, we have to add to retrieve the correct value, since modulo cannot
            g += phi;                                                                      // be negative
        }
        System.out.println("Public key for encryption = " + g);

        long modulo = n;
        String pubKey = Long.toBinaryString(g);
        String privKey = Long.toBinaryString(h);

	//Trying to encrypt!
        try{
            // Initializing buffers, readers/writers, and variables
            Reader fileReader = new FileReader("file.txt");
            BufferedReader br = new BufferedReader(fileReader);
            String s;
            int lineCount = 0;

            // Loop to count the number of lines in the document
            while ((s = br.readLine()) != null) {
                lineCount++;
            }
            fileReader.close();

            // Now that we know how many lines there are, create a new array to store
            // the encrypted text
            String[] newDocument = new String[lineCount];
            fileReader = new FileReader("file.txt");
            br = new BufferedReader(fileReader);
            // Reread the document, this time line by line encrypting the document and
            // storing its information in an array to be written back

            for (int index = 0; (s = br.readLine()) != null && index <= lineCount; index++) {
                List<Long> encodedMessage = encodeMessage(s);
                List<Long> encryptedMessage = new ArrayList<>();
                for (int i = 0; i < encodedMessage.size(); i++) {
                    long messageToEncrypt = encodedMessage.get(i);
                    //System.out.println("Message to Encrypt: " + messageToEncrypt);
                    long encryptedCharacter = encryptOrDecrypt(pubKey, modulo, messageToEncrypt);
                    encryptedMessage.add(encryptedCharacter);
                }
                s = encryptedMessage.toString();
                s = stripSymbols(s);
                newDocument[index] = s;
            }
            fileReader.close();

            //System.out.println(Arrays.toString(newDocument));
            //System.out.println(lineCount);

            // We now have an array of contiguous encrypted lines and may now call a writer
            // to exclusively write the lines back in sequence

            Writer fileWriter = new FileWriter("file.txt");
            BufferedWriter bw = new BufferedWriter(fileWriter);

            for (int index = 0; index < lineCount; index++) {
                s = newDocument[index];
                bw.write(s);
                bw.newLine();
                bw.flush();
            }

            System.out.println("Files successfully read, encrypted, and rewritten!");
        }
        catch(IOException e) {
            System.out.println("File Error Caught");
        }



    }
}

