package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.ParserException;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;


/// Tests the functionality in the {@link Project2} main class.
public class GraderIT extends InvokeMainTestCase
{
    /// Invokes the main method of {@link Project2} with the given arguments.
    private MainMethodResult invokeMain(String... args)
    {
        return invokeMain( Project2.class, args );
    }

    @Test  //(expected = PhoneBillException.class)
    public void Test1_NoArguments()
    {
        MainMethodResult result = invokeMain();
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing command line arguments\n"));
    }

    @Test  //(expected = PhoneBillException.class)
    public void Test2_YourReadme()
    {
        MainMethodResult result = invokeMain("-README");
        assertThat(result.getExitCode(), equalTo(0));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(Project2.README + "\n"));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    // -textFile sbraich/sbraich-x.txt Test3 ABC-123-4567 123-456-7890 03/03/2018 12:00 03/03/2018 16:00
    @Test //(expected = PhoneBillException.class)
    public void Test3_CallerPhoneNumberContainsNonIntegers()
    {
        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich-x.txt";
        String customer = "Test3";
        String caller = "ABC-123-4567";
        String callee = "123-456-7890";
        String startDate = "03/03/2018";
        String startTime = "12:00";
        String endDate = "03/03/2018";
        String endTime = "16:00";

        //String expectedStdOut = String.format("Phone call from %s to %s from %s %s to %s %s", caller, callee, startDate, startTime, endDate, endTime);
        Integer expectedExitCode = 1;
        String expectedStdOut = "";
        String expectedStdErr = "'" + caller + "' is not a valid phone number" + "\n";
        File f = new File(filePath);
        MainMethodResult result = null;

        try
        {
            result = invokeMain(fileOption, filePath, customer, caller, callee, startDate, startTime, endDate, endTime);
        }
        finally
        {
            Integer actualExitCode = result.getExitCode();
            String actualStdOut = result.getTextWrittenToStandardOut();
            String actualStdErr = result.getTextWrittenToStandardError();

            assertThat(actualExitCode, equalTo(expectedExitCode));
            assertThat(actualStdOut, equalTo(expectedStdOut));
            assertThat(actualStdErr, equalTo(expectedStdErr));

            f.delete();
        }
    }

    // -textFile sbraich/sbraich-x.txt Test4 123-456-7890 234-567-8901 03/03/2018 12:XX 03/03/2018 16:00
    @Test //(expected = PhoneBillException.class)
    public void Test4_StartTimeIsMalformatted()
    {
        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich-x.txt";
        String customer = "Test4";
        String caller = "123-456-7890";
        String callee = "234-567-8901";
        String startDate = "03/03/2018";
        String startTime = "12:XX";
        String endDate = "03/03/2018";
        String endTime = "16:00";

        //String expectedStdOut = String.format("Phone call from %s to %s from %s %s to %s %s", caller, callee, startDate, startTime, endDate, endTime);
        Integer expectedExitCode = 1;
        String expectedStdOut = "";
        String expectedStdErr = "'" + startDate + " " + startTime + "' is not a valid date/time." + "\n";
        File f = new File(filePath);
        MainMethodResult result = null;

        try
        {
            result = invokeMain(fileOption, filePath, customer, caller, callee, startDate, startTime, endDate, endTime);
        }
        finally
        {
            Integer actualExitCode = result.getExitCode();
            String actualStdOut = result.getTextWrittenToStandardOut();
            String actualStdErr = result.getTextWrittenToStandardError();

            assertThat(actualExitCode, equalTo(expectedExitCode));
            assertThat(actualStdOut, equalTo(expectedStdOut));
            assertThat(actualStdErr, equalTo(expectedStdErr));

            f.delete();
        }
    }

    // $ -textFile sbraich/sbraich-x.txt Test5 123-456-7890 234-567-8901 03/03/2018 12:00 01/04/20/1 16:00
    @Test //(expected = PhoneBillException.class)
    public void Test5_EndTimeIsMalformatted()
    {
        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich-x.txt";
        String customer = "Test4";
        String caller = "123-456-7890";
        String callee = "234-567-8901";
        String startDate = "03/03/2018";
        String startTime = "12:XX";
        String endDate = "01/04/20/1";
        String endTime = "16:00";

        //String expectedStdOut = String.format("Phone call from %s to %s from %s %s to %s %s", caller, callee, startDate, startTime, endDate, endTime);
        Integer expectedExitCode = 1;
        String expectedStdOut = "";
        String expectedStdErr = "'" + endDate + " " + endTime + "' is not a valid date/time." + "\n";
        File f = new File(filePath);
        MainMethodResult result = null;

        try
        {
            result = invokeMain(fileOption, filePath, customer, caller, callee, startDate, startTime, endDate, endTime);
        }
        finally
        {
            Integer actualExitCode = result.getExitCode();
            String actualStdOut = result.getTextWrittenToStandardOut();
            String actualStdErr = result.getTextWrittenToStandardError();

            assertThat(actualExitCode, equalTo(expectedExitCode));
            assertThat(actualStdOut, equalTo(expectedStdOut));
            assertThat(actualStdErr, equalTo(expectedStdErr));

            f.delete();
        }
    }

    // $ -textFile sbraich/sbraich-x.txt Test6 123-456-7890 234-567-8901 03/03/2018 12:00 04/04/2018 16:00 fred
    @Test //(expected = PhoneBillException.class)
    public void Test6_UnknownCommandLineArgument()
    {
        // Arguments
        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich-x.txt";
        String customer = "Test6";
        String caller = "123-456-7890";
        String callee = "234-567-8901";
        String startDate = "03/03/2018";
        String startTime = "12:00";
        String endDate = "04/04/2018";
        String endTime = "16:00";

        // Extra Arguments
        String extraArg = "fred";

        // Expected Arguments
        //String expectedStdOut = String.format("Phone call from %s to %s from %s %s to %s %s", caller, callee, startDate, startTime, endDate, endTime);
        Integer expectedExitCode = 1;
        String expectedStdOut = "";
        String expectedStdErr = "Too many command line arguments" + "\n";

        // File and Result Object
        File f = new File(filePath);
        MainMethodResult result = null;

        try
        {
            result = invokeMain(fileOption, filePath, customer, caller, callee, startDate, startTime, endDate, endTime, extraArg);
        }
        finally
        {
            Integer actualExitCode = result.getExitCode();
            String actualStdOut = result.getTextWrittenToStandardOut();
            String actualStdErr = result.getTextWrittenToStandardError();

            assertThat(actualExitCode, equalTo(expectedExitCode));
            assertThat(actualStdOut, equalTo(expectedStdOut));
            assertThat(actualStdErr, equalTo(expectedStdErr));

            f.delete();
        }
    }

    // $ -textFile sbraich/sbraich.txt -print Project2 123-456-7890 234-567-9081 01/07/2018 07:00 01/17/2018 17:00
    @Test //(expected = PhoneBillException.class)
    public void Test7_StartingNewPhoneBillFile()
    {
        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich.txt";
        String printOption = "-print";
        String customer = "Project2";
        String caller = "123-456-7890";
        String callee = "234-567-8901";
        String startDate = "01/07/2018";
        String startTime = "07:00";
        String endDate = "01/17/2018";
        String endTime = "17:00";

        Integer expectedExitCode = 1;
        String expectedStdOut = String.format("Phone call from %s to %s from %s %s to %s %s", caller, callee, startDate, startTime, endDate, endTime);
        String expectedStdErr = "";
        File f = new File(filePath);
        MainMethodResult result = null;

        try
        {
            result = invokeMain(fileOption, filePath, printOption, customer, caller, callee, startDate, startTime, endDate, endTime);
        }
        finally
        {
            Integer actualExitCode = result.getExitCode();
            String actualStdOut = result.getTextWrittenToStandardOut();
            String actualStdErr = result.getTextWrittenToStandardError();

            assertThat(actualExitCode, equalTo(expectedExitCode));
            assertThat(actualStdOut, equalTo(expectedStdOut));
            assertThat(actualStdErr, equalTo(expectedStdErr));

            f.delete();
        }
    }

    // Test 8 Setup
    // $ -textFile sbraich/sbraich.txt -print Project2 123-456-7890 234-567-9081 01/07/2018 07:00 01/17/2018 17:00
    public void Test8_Setup()
    {
        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich.txt";
        String printOption = "-print";
        String customer = "Project2";
        String caller = "123-456-7890";
        String callee = "234-567-8901";
        String startDate = "01/07/2018";
        String startTime = "07:00";
        String endDate = "01/17/2018";
        String endTime = "17:00";

        Integer expectedExitCode = 1;
        String expectedStdOut = String.format("Phone call from %s to %s from %s %s to %s %s", caller, callee, startDate, startTime, endDate, endTime);
        String expectedStdErr = "";
        File f = new File(filePath);
        MainMethodResult result = null;

        try
        {
            result = invokeMain(fileOption, filePath, printOption, customer, caller, callee, startDate, startTime, endDate, endTime);
        }
        finally
        {
            Integer actualExitCode = result.getExitCode();
            String actualStdOut = result.getTextWrittenToStandardOut();
            String actualStdErr = result.getTextWrittenToStandardError();

            assertThat(actualExitCode, equalTo(expectedExitCode));
            assertThat(actualStdOut, equalTo(expectedStdOut));
            assertThat(actualStdErr, equalTo(expectedStdErr));
        }
    }

    // $ -textFile sbraich/sbraich.txt -print Project2 123-456-7890 456-789-0123 01/08/2018 08:00 01/08/2018 18:00
    @Test //(expected = PhoneBillException.class)
    public void Test8_UsingAnExistingPhoneBillFile()
    {
        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich.txt";
        String printOption = "-print";
        String customer = "Project2";
        String caller = "123-456-7890";
        String callee = "456-789-0123";
        String startDate = "01/08/2018";
        String startTime = "08:00";
        String endDate = "01/08/2018";
        String endTime = "18:00";

        Integer expectedExitCode = 1;
        String expectedStdOut = String.format("Phone call from %s to %s from %s %s to %s %s", caller, callee, startDate, startTime, endDate, endTime);
        String expectedStdErr = "";
        File f = new File(filePath);
        MainMethodResult result = null;

        try
        {
            Test8_Setup();
            result = invokeMain(fileOption, filePath, printOption, customer, caller, callee, startDate, startTime, endDate, endTime);
        }
        finally
        {
            Integer actualExitCode = result.getExitCode();
            String actualStdOut = result.getTextWrittenToStandardOut();
            String actualStdErr = result.getTextWrittenToStandardError();

            assertThat(actualExitCode, equalTo(expectedExitCode));
            assertThat(actualStdOut, equalTo(expectedStdOut));
            assertThat(actualStdErr, equalTo(expectedStdErr));

            f.delete();
        }
    }

    // $ -textFile sbraich/sbraich.txt DIFFERENT 123-456-7890 789-012-3456 01/09/2018 09:00 02/04/2018 16:00
    @Test //(expected = PhoneBillException.class)
    public void Test9_DifferentCustomerName()
    {
        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich.txt";
        String customer = "DIFFERENT";
        String caller = "123-456-7890";
        String callee = "789-012-3456";
        String startDate = "01/09/2018";
        String startTime = "09:00";
        String endDate = "02/04/2018";
        String endTime = "16:00";

        Integer expectedExitCode = 1;
        String expectedStdOut = String.format("Phone call from %s to %s from %s %s to %s %s", caller, callee, startDate, startTime, endDate, endTime);
        String expectedStdErr = "";
        File f = new File(filePath);
        MainMethodResult result = null;

        try
        {
            Test8_Setup();
            result = invokeMain(fileOption, filePath, customer, caller, callee, startDate, startTime, endDate, endTime);
        }
        finally
        {
            Integer actualExitCode = result.getExitCode();
            String actualStdOut = result.getTextWrittenToStandardOut();
            String actualStdErr = result.getTextWrittenToStandardError();

            assertThat(actualExitCode, equalTo(expectedExitCode));
            assertThat(actualStdOut, equalTo(expectedStdOut));
            assertThat(actualStdErr, equalTo(expectedStdErr));

            f.delete();
        }
    }

    // $ -textFile sbraich/bogus.txt Project2 123-456-7890 385-284-2342 01/10/2018 10:00 01/20/2018 20:00
    @Test //(expected = PhoneBillException.class)
    public void Test10_MalformattedTextFile() throws IOException
    {
        // Setup Variables
        String setupLine1 = "asdfsadfa";
        String setupLine2 = "2r9sadvas";
        String setupLine3 = "lcv913r";
        String setupLine4 = "2r7o9av";

        List<String> setupLines = Arrays.asList(setupLine1, setupLine2, setupLine3, setupLine4);

        // Input Arguments
        String fileOption = "-textFile";
        String filePath = "sbraich/bogus.txt";
        String customer = "Project2";
        String caller = "123-456-7890";
        String callee = "385-284-2342";
        String startDate = "01/10/2018";
        String startTime = "10:00";
        String endDate = "01/20/2018";
        String endTime = "20:00";

        // Expected Return Output
        Integer expectedExitCode = 1;
        String expectedStdOut = String.format("Phone call from %s to %s from %s %s to %s %s", caller, callee, startDate, startTime, endDate, endTime);
        String expectedStdErr = "";

        File f = new File(filePath);
        Files.write(Paths.get(filePath), setupLines, Charset.forName("UTF-8"));

        MainMethodResult result = null;

        try
        {
            Test8_Setup();
            result = invokeMain(fileOption, filePath, customer, caller, callee, startDate, startTime, endDate, endTime);
        }
        finally
        {
            Integer actualExitCode = result.getExitCode();
            String actualStdOut = result.getTextWrittenToStandardOut();
            String actualStdErr = result.getTextWrittenToStandardError();

            assertThat(actualExitCode, equalTo(expectedExitCode));
            assertThat(actualStdOut, equalTo(expectedStdOut));
            assertThat(actualStdErr, equalTo(expectedStdErr));

            f.delete();
        }
    }

    // $ -textFile sbraich/sbraich-bad-year.txt Project2 123-456-7890 385-284-2342 01/11/2018 11:00 01/11/2018 11:30
    @Test //(expected = PhoneBillException.class)
    public void Test11_FileWithInvalidYear() throws IOException
    {
        // Setup Variables
        String setupLine1 = "DIFFERENT";
        String setupLine2 = "[Phone call from 123-456-7890 to 789-012-3456 from 01/09/XXXX 09:00 to 02/04/XXXX 16:00]";

        List<String> setupLines = Arrays.asList(setupLine1, setupLine2);

        // Input Arguments
        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich-bad-year.txt";
        String customer = "Project2";
        String caller = "123-456-7890";
        String callee = "385-284-2342";
        String startDate = "01/11/2018";
        String startTime = "11:00";
        String endDate = "01/11/2018";
        String endTime = "11:30";

        // Expected Return Output
        Integer expectedExitCode = 1;
        String expectedStdOut = String.format("Phone call from %s to %s from %s %s to %s %s", caller, callee, startDate, startTime, endDate, endTime);
        String expectedStdErr = "";

        File f = new File(filePath);
        Files.write(Paths.get(filePath), setupLines, Charset.forName("UTF-8"));

        MainMethodResult result = null;

        try
        {
            Test8_Setup();
            result = invokeMain(fileOption, filePath, customer, caller, callee, startDate, startTime, endDate, endTime);
        }
        finally
        {
            Integer actualExitCode = result.getExitCode();
            String actualStdOut = result.getTextWrittenToStandardOut();
            String actualStdErr = result.getTextWrittenToStandardError();

            assertThat(actualExitCode, equalTo(expectedExitCode));
            assertThat(actualStdOut, equalTo(expectedStdOut));
            assertThat(actualStdErr, equalTo(expectedStdErr));

            f.delete();
        }
    }
}