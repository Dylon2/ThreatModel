import java_src.ThreatModelJava;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;


public class ThreatModelJava_Test {
    private static final String COLOSSAL_NUMBER = "9223372036854775807922337203685477580792233720368547758079223372036" +
            "85477580792233720368547758079223372036854775807922337203685477580792233720368547758079223372036854775807" +
            "922337203685477580792233720368547758079223372036854775807922337203685477580792233720368547758079223372036" +
            "854775807922337203685477580792233720368547758079223372036854775807922337203685477580792233720368547758079" +
            "22337203685477580792233720368547758079223372036854775807" +
            "9223372036854775807922337203685477580792233720368547758079223372036" +
            "85477580792233720368547758079223372036854775807922337203685477580792233720368547758079223372036854775807" +
            "922337203685477580792233720368547758079223372036854775807922337203685477580792233720368547758079223372036" +
            "854775807922337203685477580792233720368547758079223372036854775807922337203685477580792233720368547758079" +
            "22337203685477580792233720368547758079223372036854775807" +
            "9223372036854775807922337203685477580792233720368547758079223372036" +
            "85477580792233720368547758079223372036854775807922337203685477580792233720368547758079223372036854775807" +
            "922337203685477580792233720368547758079223372036854775807922337203685477580792233720368547758079223372036" +
            "854775807922337203685477580792233720368547758079223372036854775807922337203685477580792233720368547758079" +
            "22337203685477580792233720368547758079223372036854775807";

    @Test
    public void test_Name_Cannot_Be_Empty() {
        assertFalse("Empty String should not be accepted", ThreatModelJava.isValidFileName(""));
    }

    @Test
    public void test_Name_Character_Amount_Boundaries() {
        assertTrue("Minimum should 1 character", ThreatModelJava.isValidFileName("a"));
        assertTrue("Maximum should 50 characters",
                ThreatModelJava.isValidFileName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
    }

    @Test
    public void test_Name_Cannot_Accept_Only_Letter_Characters() {
        assertFalse("only characters allowed are a-z or A-Z",
                ThreatModelJava.isValidFileName("ko931f23kf32k09-=s;pfdc;x/.xz;~`"));
    }

    @Test
    public void test_Integer_Cannot_Be_Massive() {
        assertFalse("Number must be within range of 4 Bytes signed", ThreatModelJava.isValidInt(COLOSSAL_NUMBER));
    }

    @Test
    public void test_Integer_Accepts_Max_And_Min_For_4_Bytes() {
        assertTrue("Input must allow 2147483647", ThreatModelJava.isValidInt("2147483647"));
        assertTrue("Input must allow -2147483648", ThreatModelJava.isValidInt("-2147483648"));
    }

    @Test
    public void test_Integer_Does_Not_Accept_Non_Digit_Characters() {
        assertFalse("Integer can only contain digits 0-9",
                ThreatModelJava.isValidInt("12[]adsll]21][fa'sv/.c-=89/*-*+"));
    }

    @Test
    public void test_Two_Integer_Inputs_Is_True_When_Addition_Result_Overflows() {
        assertTrue("Result out of range of 4 bytes",
                ThreatModelJava.isNotValidSumAndProductOfTwoNumbers(
                        ((long) 2147483647) + 1,
                        ((long) 2147483647) * 1));
        assertTrue("Result out of range of 4 bytes",
                ThreatModelJava.isNotValidSumAndProductOfTwoNumbers(
                        ((long) -2147483648) - 1,
                        ((long) -2147483648) * 1));
    }

    @Test
    public void test_Two_Integer_Inputs_Is_True_When_Product_Result_Overflows() {
        assertTrue("Result out of range of 4 bytes",
                ThreatModelJava.isNotValidSumAndProductOfTwoNumbers(
                        ((long) 2147483647) + -2147483648,
                        ((long) 2147483647) * -2147483648));
    }

    @Test
    public void test_Two_Integer_Inputs_Is_False_When_Result_Is_In_Range_Of_4_Bytes() {
        assertFalse("Result of 2 + 2 and 2 * 2 should be in range of 4 bytes",
                ThreatModelJava.isNotValidSumAndProductOfTwoNumbers(
                        ((long) 2) + 2,
                        ((long) 2) * 2));
    }

    @Test
    public void test_Input_File_Name_Contains_No_Slashes_Colons_Or_Spaces() {
        assertFalse("input file name may not contain slashes, colons, or spaces",
                ThreatModelJava.isValidFileName("  C:\\Windows\\System32\\input.txt "));
    }

    @Test
    public void test_Input_File_Name_Can_Be_Accept_Multiple_Types_Of_File_Extensions() {
        assertTrue("input file name may be like input.txt, input.java, input.c etc.",
                ThreatModelJava.isValidFileName("input.txt"));
        assertTrue("input file name may be like input.txt, input.java, input.c etc.",
                ThreatModelJava.isValidFileName("input.java"));
        assertTrue("input file name may be like input.txt, input.java, input.c etc.",
                ThreatModelJava.isValidFileName("input.c"));

    }

    @Test
    public void test_Output_File_Name_Contains_No_Slashes_Colons_Or_Spaces() {
        assertFalse("input file name may not contain slashes, colons, or spaces",
                ThreatModelJava.isValidFileName("  C:\\Windows\\System32\\output.txt "));
    }

    @Test
    public void test_Output_File_Name_Contains_No_S() {
        assertTrue("output file name may be like output.txt, output.java, output.c etc.",
                ThreatModelJava.isValidFileName("output.txt"));
        assertTrue("output file name may be like output.txt, output.java, output.c etc.",
                ThreatModelJava.isValidFileName("output.java"));
        assertTrue("output file name may be like output.txt, output.java, output.c etc.",
                ThreatModelJava.isValidFileName("input.c"));
    }

    @Test
    void test() {
        System.out.println("");
    }
}
