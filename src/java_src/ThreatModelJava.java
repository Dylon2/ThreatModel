package java_src;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;
import java.util.Date;


/**
 *  Takes various inputs from user and attempts
 *  to defend against as many attacks as possible.
 *
 * @author Devin Arroyo, Dylon Bernal
 */
public class ThreatModelJava {

    private static final String ERROR_LOG = "error_log.txt";
    private static final String PASSWORD_FILE = "password_hash.txt";

    public static void main(String[] args) {
        Scanner consoleScanner = new Scanner(System.in);

        //System.out.println("--- A Special Week of regex ---");
        // Because, y'know, John Umamusume?

        String firstName = "";
        while (true) {
            System.out.print("Enter First Name (1-50 chars, letters only): ");
            firstName = consoleScanner.nextLine().trim();
            if (isValidName(firstName)) break;
            System.out.println("Invalid input. Use 1-50 alphabetic characters.");
        }

        String lastName = "";
        while (true) {
            System.out.print("Enter Last Name (1-50 chars, letters only): ");
            lastName = consoleScanner.nextLine().trim();
            if (isValidName(lastName)) break;
            System.out.println("Invalid input. Use 1-50 alphabetic characters.");
        }

        int numberOne = 0;
        int numberTwo = 0;
        while(true) {
            int tempNum1 = 0;
            while (true) {
                System.out.print("Enter First Integer (-2,147,483,648 to 2,147,483,647): ");
                String input = consoleScanner.nextLine();
                if (isValidInt(input)) {
                    tempNum1 = Integer.parseInt(input);
                    break;
                }
                System.out.println("Invalid range. Must be a 4-byte signed integer.");
            }

            int tempNum2 = 0;
            while (true) {
                System.out.print("Enter Second Integer: ");
                String input = consoleScanner.nextLine();
                if (isValidInt(input)) {
                    tempNum2 = Integer.parseInt(input);
                    break;
                }
                System.out.println("Invalid range.");
            }

            long tempSum = ((long) tempNum1) + tempNum2;
            long tempProduct = ((long) tempNum1) * tempNum2;
            if (isNotValidSumAndProductOfTwoNumbers(tempSum, tempProduct)) {
                System.out.println("Invalid. Result of product/addition of two numbers " +
                        "is not within specified number range");
            } else {
                numberOne = tempNum1;
                numberTwo = tempNum2;
                break;
            }
        }

        String inputFileName = "";
        while (true) {
            System.out.print("Enter Input Filename (e.g., input.txt): ");
            inputFileName = consoleScanner.nextLine().trim();
            if (isValidFileName(inputFileName)) break;
            System.out.println("Invalid format. Do not use slashes or colons.");
        }

        String outputFileName = "";
        while (true) {
            System.out.print("Enter Output Filename: ");
            outputFileName = consoleScanner.nextLine().trim();
            if (isValidFileName(outputFileName) && !outputFileName.equals(inputFileName)
                    && !outputFileName.equals(PASSWORD_FILE) && !outputFileName.equals(ERROR_LOG)) {
                break;
            }
            System.out.println("Invalid output name (cannot be the same as input or system files).");
        }

        while (true) {
            System.out.print("Create Password (min 6 chars (even \"qwerty\" is fine)): ");
            String inputPassword = consoleScanner.nextLine();
            if (inputPassword.length() < 6) {
                System.out.println("Password too short. Lika Tama-chan");
                continue;
            }

            savePassword(inputPassword);

            System.out.print("Re-enter Password to verify: ");
            String verifyPassword = consoleScanner.nextLine();

            if (verifyPasswordFromFile(verifyPassword)) {
                System.out.println("Password verified against stored hash.");
                break;
            } else {
                System.out.println("Verification failed. Try again.");
            }
        }

        processOutput(firstName, lastName, numberOne, numberTwo, inputFileName, outputFileName);
        System.out.println("\nProcess complete. Results written to: " + outputFileName);

        consoleScanner.close();
    }

    /**
     *  Tests the inputted name to make sure it is not null
     *  and has from 1 to 50 characters of a-z or A-Z
     *  only
     *
     * @param name the inputted name
     * @return true inputted name is valid, else false
     */
    public static boolean isValidName(String name) {
        return name != null && name.matches("^[a-zA-Z]{1,50}$");
    }

    /**
     *  Tests if the number inputted is an actual number
     *
     * @param input the number input
     * @return  true if input is a number else false
     */
    public static boolean isValidInt(String input) {
        if (input == null || !input.matches("^-?\\d+$")) return false;
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     *  Tests for overflow of two integers when taking their
     *  product or addition.
     *
     * @param theSum the sum of two integers
     * @param theProduct the product of two integers
     * @return true if addition or product of two integers overflows
     */
    public static boolean isNotValidSumAndProductOfTwoNumbers(final long theSum, final long theProduct) {
        return (theSum < Integer.MIN_VALUE || theSum > Integer.MAX_VALUE)
                || (theProduct < Integer.MIN_VALUE || theProduct > Integer.MAX_VALUE);
    }

    /**
     *  Tests if the inputted file name contains no
     *  slashes, colons, or spaces.
     *
     * @param name the file name
     * @return true if file name passes regex and is not null, else false
     */
    public static boolean isValidFileName(String name) {
        return name != null && name.matches("^[^/\\\\: ]+$");
    }

    /**
     *  Saves password to a file.
     *
     * @param password the inputted password
     */
    private static void savePassword(String password) {
        try (PrintStream out = new PrintStream(new FileOutputStream(PASSWORD_FILE))) {
            byte[] salt = new byte[16];
            new SecureRandom().nextBytes(salt);
            String hash = hashWithSalt(password, salt);
            out.println(bytesToHex(salt) + ":" + hash);
        } catch (Exception e) {
            logError("Password Storage Error: " + e.getMessage());
        }
    }

    /**
     *  Checks if re-entered password matches one saved
     *
     * @param attempt the re-entered password
     * @return true if attempt matched password from file, else false
     */
    private static boolean verifyPasswordFromFile(String attempt) {
        try (Scanner fileScanner = new Scanner(new File(PASSWORD_FILE))) {
            if (!fileScanner.hasNextLine()) return false;
            String[] parts = fileScanner.nextLine().split(":");
            byte[] salt = hexToBytes(parts[0]);
            String storedHash = parts[1];
            return storedHash.equals(hashWithSalt(attempt, salt));
        } catch (Exception e) {
            logError("Verification Error: " + e.getMessage());
            return false;
        }
    }

    /**
     *  Salt and hashes the inputted password
     *
     * @param pass the inputted password
     * @param salt the generated salt
     * @return true the hashed password
     * @throws NoSuchAlgorithmException when hashing algorithm does not exist
     */
    private static String hashWithSalt(String pass, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        return bytesToHex(md.digest(pass.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     *  writes the users inputs for first and last name, two integers and their sum and product,
     *  the contents input file all to the output file provided by user.
     *
     * @param f the first name
     * @param l the last name
     * @param n1 the first number
     * @param n2 the second number
     * @param inPath the input file
     * @param outPath the output file
     */
    private static void processOutput(String f, String l, int n1, int n2, String inPath, String outPath) {
        try (PrintStream out = new PrintStream(new FileOutputStream(outPath))) {
            out.println("First Name: " + f);
            out.println("Last Name: " + l);
            out.println("First Integer: " + n1);
            out.println("Second Integer: " + n2);

            long sum = (long) n1 + (long) n2;
            long product = (long) n1 * (long) n2;

            out.println("Sum: " + sum);
            out.println("Product: " + product);

            out.println("Input File Name: " + inPath);
            out.println("--- Input file contents ---");

            File inFile = new File(inPath);
            if (inFile.exists()) {
                try (Scanner fileScanner = new Scanner(inFile)) {
                    while (fileScanner.hasNextLine()) {
                        out.println(fileScanner.nextLine());
                    }
                }
            } else {
                out.println("[Input file not found]");
            }
        } catch (IOException e) {
            logError("Output Error: " + e.getMessage());
        }
    }

    /**
     *  Converts bytes to hex.
     *
     * @param bytes the bytes
     * @return the bytes to hex as string
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    /**
     *  Converts hex to bytes.
     *
     * @param s the hex as a string
     * @return the hex converted to byte array
     */
    private static byte[] hexToBytes(String s) {
        byte[] data = new byte[s.length() / 2];
        for (int i = 0; i < s.length(); i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     *  Logs any error that occurs when input is invalid from user
     *  or other operations go wrong like trying open a input file that
     *  does not exist
     *
     * @param msg the error message
     */
    private static void logError(String msg) {
        try (PrintStream errOut = new PrintStream(new FileOutputStream(ERROR_LOG, true))) {
            errOut.println(new Date() + " - " + msg);
        } catch (IOException ignored) {
        }
    }
}