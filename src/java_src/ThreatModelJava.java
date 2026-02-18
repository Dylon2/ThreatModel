package java_src;

import java.util.Scanner;


/**
 *
 *
 */
public class ThreatModelJava {
    private static final Scanner READ = new Scanner(System.in);

    public static void main(String[] theArgs) {
        //readUserFirstLastName();
        //readTwoInts();
        //readInputFile();
        readPassword();
    }

    // TODO You must make it clear to the user what is expected for input (describe range, acceptable characters, and anything else you feel is important)
    public static void readUserFirstLastName() {
        String firstName = null;
        String lastName = null;

        System.out.print("type first name: ");
        firstName = READ.nextLine();

        System.out.print("type last name: ");
        lastName = READ.nextLine();
    }

    // TODO Once again, make clear to user what is expected. Note that an int can be from -2,147,...,... to 2,147,...,...
    public static void readTwoInts() {
        int firstNumber = 0;
        int secondNumber = 0;

        System.out.print("type an integer: ");
        firstNumber = READ.nextInt();

        System.out.print("type a second integer: ");
        secondNumber = READ.nextInt();
    }

    // TODO Make clear to user what is expected and will be accepted
    public static void readInputFile() {
        String inputFile = null;

        System.out.print("type your input file: ");
        inputFile = READ.nextLine();
    }

    // TODO Make clear to user what is expected and will be accepted
    public static void readOutputFile() {
        String inputFile = null;

        System.out.print("type your input file: ");
        inputFile = READ.nextLine();
    }

    // TODO password should be hashed using a salt and written to a file
    // TODO to validate, grab hash from file and compare it to hash from second user entry for password
    // TODO as long as passwords don't match/follow your password specifications, re-prompt the user
    public static void readPassword() {
        String password = null;

        System.out.print("type your password: ");
        password = READ.nextLine();

        String reEnterPass = null;
        System.out.print("Re-Enter password: ");
        reEnterPass = READ.nextLine();

        System.out.println("Re-Entered pass was: " + password.equals(reEnterPass));
    }

    public static void writeToOutputFile() {

    }
}
