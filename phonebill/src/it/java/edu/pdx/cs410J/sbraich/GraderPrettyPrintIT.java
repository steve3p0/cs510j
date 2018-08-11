package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;


/// Tests the functionality in the {@link Project3} main class.
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GraderPrettyPrintIT extends InvokeMainTestCase
{
    /// Invokes the main method of {@link Project3} with the given arguments.
    private MainMethodResult invokeMain(String... args)
    {
        return invokeMain( Project3.class, args );
    }

    @Test  //(expected = PhoneBillException.class)
    public void Test01_NoArguments()
    {
        fail("Not tested");

        Integer expectedExitCode = 1;
        String expectedStdOut = "";
        String expectedStdErr = "Missing command line arguments";

        MainMethodResult result = invokeMain();

        assertThat(result.getExitCode(), equalTo(expectedExitCode));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(expectedStdOut));
        assertThat(result.getTextWrittenToStandardError(), containsString(expectedStdErr));
    }

    @Test  //(expected = PhoneBillException.class)
    public void Test02_YourReadme()
    {
        fail("Not tested");

        String readmeOption = "-README";

        Integer expectedExitCode = 0;
        String expectedStdOut = Project3.README + "\n";
        String expectedStdErr = "";

        MainMethodResult result = invokeMain(readmeOption);

        assertThat(result.getExitCode(), equalTo(expectedExitCode));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(expectedStdOut));
        assertThat(result.getTextWrittenToStandardError(), equalTo(expectedStdErr));
    }

    // -textFile sbraich/sbraich-x.txt Test3 ABC-123-4567 123-456-7890 03/03/2018 12:00 03/03/2018 16:00
    @Test //(expected = PhoneBillException.class)
    public void Test03_CallerPhoneNumberContainsNonIntegers()
    {
        fail("Not tested");

        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich-x.txt";
        String customer = "Test3";
        String caller = "ABC-123-4567";
        String callee = "123-456-7890";
        String startDate = "03/03/2018";
        String startTime = "12:00 AM";
        String endDate = "03/03/2018";
        String endTime = "4:00 PM";

        //String expectedStdOut = String.format("Phone call from %s to %s from %s %s to %s %s", caller, callee, startDate, startTime, endDate, endTime);
        Integer expectedExitCode = 1;
        String expectedStdOut = "StdOut: " + "";
        String expectedStdErr = "StdErr: '" + caller + "' is not a valid phone number" + "\n";
        File f = new File(filePath);
        MainMethodResult result = null;

        try
        {
            result = invokeMain(fileOption, filePath, customer, caller, callee, startDate, startTime, endDate, endTime);
        }
        finally
        {
            Integer actualExitCode = result.getExitCode();
            String actualStdOut = "StdOut: " + result.getTextWrittenToStandardOut();
            String actualStdErr = "StdErr: " + result.getTextWrittenToStandardError();

            assertThat(actualExitCode, equalTo(expectedExitCode));
            assertThat(actualStdOut, equalTo(expectedStdOut));
            assertThat(actualStdErr, equalTo(expectedStdErr));

            f.delete();
        }
    }

    // -textFile sbraich/sbraich-x.txt Test4 123-456-7890 234-567-8901 03/03/2018 12:XX 03/03/2018 16:00
    @Test //(expected = PhoneBillException.class)
    public void Test04_StartTimeIsMalformatted()
    {
        fail("Not tested");

        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich-x.txt";
        String customer = "Test4";
        String caller = "123-456-7890";
        String callee = "234-567-8901";
        String startDate = "03/03/2018";
        String startTime = "12:XX AM";
        String endDate = "03/03/2018";
        String endTime = "4:00 PM";

        //String expectedStdOut = String.format("Phone call from %s to %s from %s %s to %s %s", caller, callee, startDate, startTime, endDate, endTime);
        Integer expectedExitCode = 1;
        String expectedStdOut = "StdOut: ";
        String expectedStdErr = "StdErr: " + "'" + startDate + " " + startTime + "' is not a valid date/time" + "\n";
        File f = new File(filePath);
        MainMethodResult result = null;

        try
        {
            result = invokeMain(fileOption, filePath, customer, caller, callee, startDate, startTime, endDate, endTime);
        }
        finally
        {
            Integer actualExitCode = result.getExitCode();
            String actualStdOut = "StdOut: " + result.getTextWrittenToStandardOut();
            String actualStdErr = "StdErr: " + result.getTextWrittenToStandardError();

            assertThat(actualExitCode, equalTo(expectedExitCode));
            assertThat(actualStdOut, equalTo(expectedStdOut));
            assertThat(actualStdErr, equalTo(expectedStdErr));

            f.delete();
        }
    }

    // $ -textFile sbraich/sbraich-x.txt Test5 123-456-7890 234-567-8901 03/03/2018 12:00 01/04/20/1 16:00
    @Test //(expected = PhoneBillException.class)
    public void Test05_EndTimeIsMalformatted()
    {
        fail("Not tested");

        // Input Argument Options
        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich-x.txt";

        // Input Argument Paramenters
        String customer = "Test5";
        String caller = "123-456-7890";
        String callee = "234-567-8901";
        String startDate = "03/03/2018";
        String startTime = "12:00 AM";
        String endDate = "01/04/20/1";
        String endTime = "4:00 PM";

        // Expected Output
        Integer expectedExitCode = 1;
        String expectedStdOut = "StdOut: ";
        String expectedStdErr = "StdErr: " + "'" + endDate + " " + endTime + "' is not a valid date/time" + "\n";

        File f = new File(filePath);
        MainMethodResult result = null;

        try
        {
            result = invokeMain(fileOption, filePath, customer, caller, callee, startDate, startTime, endDate, endTime);
        }
        finally
        {
            Integer actualExitCode = result.getExitCode();
            String actualStdErr = "StdErr: " + result.getTextWrittenToStandardError();
            String actualStdOut = "StdOut: " + result.getTextWrittenToStandardOut();

            assertThat(actualExitCode, equalTo(expectedExitCode));
            assertThat(actualStdOut, equalTo(expectedStdOut));
            assertThat(actualStdErr, equalTo(expectedStdErr));

            f.delete();
        }
    }

    // $ -textFile sbraich/sbraich-x.txt Test6 123-456-7890 234-567-8901 03/03/2018 12:00 04/04/2018 16:00 fred
    @Test //(expected = PhoneBillException.class)
    public void Test06_UnknownCommandLineArgument()
    {
        fail("Not tested");

        // Arguments
        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich-x.txt";
        String customer = "Test6";
        String caller = "123-456-7890";
        String callee = "234-567-8901";
        String startDate = "03/03/2018";
        String startTime = "12:00 AM";
        String endDate = "04/04/2018";
        String endTime = "4:00 PM";

        // Extra Arguments
        String extraArg = "fred";

        // Expected Arguments
        //String expectedStdOut = String.format("Phone call from %s to %s from %s %s to %s %s", caller, callee, startDate, startTime, endDate, endTime);
        Integer expectedExitCode = 1;
        String expectedStdOut = "StdOut: " + "";
        String expectedStdErr = "StdErr: " + "Too many command line arguments" + "\n";

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
            String actualStdOut = "StdOut: " + result.getTextWrittenToStandardOut();
            String actualStdErr = "StdErr: " + result.getTextWrittenToStandardError();

            assertThat(actualExitCode, equalTo(expectedExitCode));
            assertThat(actualStdOut, equalTo(expectedStdOut));
            assertThat(actualStdErr, equalTo(expectedStdErr));

            f.delete();
        }
    }

    // $ -textFile sbraich/sbraich.txt -print Project3 123-456-7890 234-567-9081 01/07/2018 07:00 01/17/2018 17:00
    @Test //(expected = PhoneBillException.class)
    public void Test07_StartingNewPhoneBillFile()
    {
        fail("Not tested");

        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich.txt";
        String printOption = "-print";
        String customer = "Project3";
        String caller = "123-456-7890";
        String callee = "234-567-8901";
        String startDate = "01/07/2018";
        String startTime = "07:00 AM";
        String endDate = "01/17/2018";
        String endTime = "5:00 PM";

        Integer expectedExitCode = 0;
        String expectedStdOut = "StdOut: " + String.format("Phone call from %s to %s from %s %s to %s %s", caller, callee, startDate, startTime, endDate, endTime) + "\n";
        String expectedStdErr = "StdErr: " + "";
        File f = new File(filePath);
        MainMethodResult result = null;

        try
        {
            result = invokeMain(fileOption, filePath, printOption, customer, caller, callee, startDate, startTime, endDate, endTime);
        }
        finally
        {
            String actualStdOut = "StdOut: " + result.getTextWrittenToStandardOut();
            String actualStdErr = "StdErr: " + result.getTextWrittenToStandardError();
            Integer actualExitCode = result.getExitCode();

            assertThat(actualStdOut, equalTo(expectedStdOut));
            assertThat(actualStdErr, equalTo(expectedStdErr));
            assertThat(actualExitCode, equalTo(expectedExitCode));

            f.delete();
        }
    }

    // $ -textFile sbraich/sbraich.txt -print Project3 123-456-7890 456-789-0123 01/08/2018 08:00 01/08/2018 18:00
    @Test //(expected = PhoneBillException.class)
    public void Test08_UsingAnExistingPhoneBillFile() throws IOException, NoSuchFileException
    {
        fail("Not tested");

        // Setup Variables
        String setupCustomer = "Project3";
        String setupCaller = "123-456-7890";
        String setupCallee = "234-567-8901";
        String setupStartDate = "01/07/2018 am";
        String setupStartTime = "07:00";
        String setupEndDate = "01/17/2018 pm";
        String setupEndTime = "5:00";

        String setupLine = String.format("Phone call from %s to %s from %s %s to %s %s", setupCaller, setupCallee, setupStartDate, setupStartTime, setupEndDate, setupEndTime);
        List<String> setupLines = Arrays.asList(setupCustomer, "[" + setupLine + "]");

        // Input Arguments
        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich.txt";
        String printOption = "-print";
        String customer = "Project3";
        String caller = "123-456-7890";
        String callee = "456-789-0123";
        String startDate = "01/08/2018";
        String startTime = "08:00 am";
        String endDate = "01/08/2018";
        String endTime = "06:00 pm";

        // Expected Outputs
        String expectedStdOut = "StdOut: " + String.format("Phone call from %s to %s from %s %s to %s %s", caller, callee, startDate, startTime, endDate, endTime) + "\n";
        String expectedStdErr = "StdErr: " + "";
        Integer expectedExitCode = 0;

        // Create the 'EXISTING FILE
        File dir = new File(Paths.get(filePath).getParent().toString());
        if (!dir.exists())
        {
            Files.createDirectories(Paths.get(filePath).getParent());
        }
        Files.write(Paths.get(filePath), setupLines, Charset.forName("UTF-8"));

        // Create the file and the Result Object
        File f = new File(filePath);
        MainMethodResult result = null;

        try
        {
            //Test08_Setup();
            result = invokeMain(fileOption, filePath, printOption, customer, caller, callee, startDate, startTime, endDate, endTime);
        }
        finally
        {
            String actualStdOut = "StdOut: " + result.getTextWrittenToStandardOut();
            String actualStdErr = "StdErr: " + result.getTextWrittenToStandardError();
            Integer actualExitCode = result.getExitCode();

            assertThat(actualStdOut, equalTo(expectedStdOut));
            assertThat(actualStdErr, equalTo(expectedStdErr));
            assertThat(actualExitCode, equalTo(expectedExitCode));

            f.delete();
        }
    }

    // $ -textFile sbraich/sbraich.txt DIFFERENT 123-456-7890 789-012-3456 01/09/2018 09:00 02/04/2018 16:00
    @Test //(expected = PhoneBillException.class)
    public void Test09_DifferentCustomerName() throws IOException, NoSuchFileException
    {
        fail("Not tested");

        // Setup Variables
        String setupCustomer = "Project3";
        String setupCaller = "123-456-7890";
        String setupCallee = "456-789-0123";
        String setupStartDate = "01/08/2018";
        String setupStartTime = "08:00 am";
        String setupEndDate = "01/08/2018";
        String setupEndTime = "6:00 pm";

        String setupLine = String.format("Phone call from %s to %s from %s %s to %s %s", setupCaller, setupCallee, setupStartDate, setupStartTime, setupEndDate, setupEndTime);
        List<String> setupLines = Arrays.asList(setupCustomer, "[" + setupLine + "]");

        // Input Arguments
        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich.txt";
        String customer = "DIFFERENT";
        String caller = "123-456-7890";
        String callee = "789-012-3456";
        String startDate = "01/09/2018";
        String startTime = "09:00 am";
        String endDate = "02/04/2018";
        String endTime = "4:00 pm";

        // Expected Output
        Integer expectedExitCode = 1;
        //String expectedStdOut = String.format("Phone call from %s to %s from %s %s to %s %s", caller, callee, startDate, startTime, endDate, endTime);
        String expectedStdOut = "StdOut: " + "";
        String expectedStdErr = "StdErr: " + "Customer from command line '" + customer + "' does not match customer '" + setupCustomer + "' in file '" + filePath.toString() + "'" + "\n";

        // Create the 'EXISTING' FILE
        File dir = new File(Paths.get(filePath).getParent().toString());
        if (!dir.exists())
        {
            Files.createDirectories(Paths.get(filePath).getParent());
        }
        Files.write(Paths.get(filePath), setupLines, Charset.forName("UTF-8"));

        // Create the file and the Result Object
        File f = new File(filePath);
        MainMethodResult result = null;

        try
        {
            result = invokeMain(fileOption, filePath, customer, caller, callee, startDate, startTime, endDate, endTime);
        }
        finally
        {
            Integer actualExitCode = result.getExitCode();
            String actualStdOut = "StdOut: " + result.getTextWrittenToStandardOut();
            String actualStdErr = "StdErr: " + result.getTextWrittenToStandardError();

            assertThat(actualExitCode, equalTo(expectedExitCode));
            assertThat(actualStdOut, equalTo(expectedStdOut));
            assertThat(actualStdErr, equalTo(expectedStdErr));

            f.delete();
        }
    }

    // $ -textFile sbraich/bogus.txt Project3 123-456-7890 385-284-2342 01/10/2018 10:00 01/20/2018 20:00
    @Test //(expected = PhoneBillException.class)
    public void Test10_MalformattedTextFile() throws IOException, NoSuchFileException
    {
        fail("Not tested");

        // Setup Variables
        String setupCustomer = "asdfsadfa";
        String setupCall = "2r9sadvas";
        String setupExtraLine1 = "lcv913r";
        String setupExtraLine2 = "2r7o9av";

        List<String> setupLines = Arrays.asList(setupCustomer, setupCall, setupExtraLine1, setupExtraLine2);

        // Input Arguments
        String fileOption = "-textFile";
        String filePath = "sbraich/bogus.txt";
        String customer = "Project3";
        String caller = "123-456-7890";
        String callee = "385-284-2342";
        String startDate = "01/10/2018";
        String startTime = "10:00 am";
        String endDate = "01/20/2018";
        String endTime = "8:00 pm";

        // Expected Return Output
        Integer expectedExitCode = 1;
        //String expectedStdOut = String.format("Phone call from %s to %s from %s %s to %s %s", caller, callee, startDate, startTime, endDate, endTime);
        String expectedStdOut = "StdOut: " + "";
        //String expectedStdErr = "StdErr: " + "Customer from command line '" + customer + "' does not match customer '" + setupCustomer + "' in file '" + filePath.toString() + "'" + "\n";
        String expectedStdErr = "StdErr: " + "Invalid Phone Number: " + setupCall + "\n";

        // Make sure the directory path exists
        File dir = new File(Paths.get(filePath).getParent().toString());
        if (!dir.exists())
        {
            Files.createDirectories(Paths.get(filePath).getParent());
        }
        Files.write(Paths.get(filePath), setupLines, Charset.forName("UTF-8"));

        // Create the file and the Result Object
        File f = new File(filePath);
        MainMethodResult result = null;

        try
        {
            result = invokeMain(fileOption, filePath, customer, caller, callee, startDate, startTime, endDate, endTime);
        }
        finally
        {
            Integer actualExitCode = result.getExitCode();
            String actualStdOut = "StdOut: " + result.getTextWrittenToStandardOut();
            String actualStdErr = "StdErr: " + result.getTextWrittenToStandardError();

            assertThat(actualExitCode, equalTo(expectedExitCode));
            assertThat(actualStdOut, equalTo(expectedStdOut));
            assertThat(actualStdErr, equalTo(expectedStdErr));

            f.delete();
        }
    }

    // $ -textFile sbraich/sbraich-bad-year.txt Project3 123-456-7890 385-284-2342 01/11/2018 11:00 01/11/2018 11:30
    // DIFFERENT
    // [Phone call from 123-456-7890 to 789-012-3456 from 01/09/XXXX 09:00 to 02/04/XXXX 16:00]
    @Test //(expected = PhoneBillException.class)
    public void Test11_FileWithInvalidYear() throws IOException
    {
        fail("Not tested");

        // Setup Variables
        String setupCustomer = "DIFFERENT";
        String setupCaller = "123-456-7890";
        String setupCallee = "789-012-3456";
        String setupStartDate = "01/09/XXXX";
        String setupStartTime = "09:00 am";
        String setupEndDate = "02/04/XXXX";
        String setupEndTime = "4:00 pm";

        String setupCall = String.format("Phone call from %s to %s from %s %s to %s %s",
                setupCaller, setupCallee, setupStartDate, setupStartTime, setupEndDate, setupEndTime);

        List<String> setupLines = Arrays.asList(setupCustomer, setupCall);

        // Input Arguments
        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich-bad-year.txt";
        String customer = "Project3";
        String caller = "123-456-7890";
        String callee = "385-284-2342";
        String startDate = "01/11/2018";
        String startTime = "11:00 am";
        String endDate = "01/11/2018";
        String endTime = "11:30 pm";

        // Expected Return Output
        Integer expectedExitCode = 1;
        String expectedStdOut = "StdOut: " + "";
        //String expectedStdErr = "StdErr: " + "'" + setupStartDate + " " + setupStartTime + "' is not a valid date/time" + "\n";

        String expectedStdErr = "StdErr: " + "Invalid DateTime: " +
                String.format("Phone call from %s to %s from %s %s to %s %s",
                setupCaller, setupCallee, setupStartDate, setupStartTime, setupEndDate, setupEndTime) + "\n";

        //String expectedStdErr = "StdErr: " + "'" + setupStartDate + " " + setupStartTime + "' is not a valid date/time" + "\n";
        //Invalid DateTime: Phone call from 123-456-7890 to 789-012-3456 from 01/09/XXXX 09:00 to 02/04/XXXX 16:00

        // Make sure the directory path exists
        File dir = new File(Paths.get(filePath).getParent().toString());
        if (!dir.exists())
        {
            Files.createDirectories(Paths.get(filePath).getParent());
        }
        Files.write(Paths.get(filePath), setupLines, Charset.forName("UTF-8"));

        // Create the file and the Result Object
        File f = new File(filePath);
        MainMethodResult result = null;

        try
        {
            result = invokeMain(fileOption, filePath, customer, caller, callee, startDate, startTime, endDate, endTime);
        }
        finally
        {
            Integer actualExitCode = result.getExitCode();
            String actualStdOut = "StdOut: " + result.getTextWrittenToStandardOut();
            String actualStdErr = "StdErr: " + result.getTextWrittenToStandardError();

            assertThat(actualExitCode, equalTo(expectedExitCode));
            assertThat(actualStdOut, equalTo(expectedStdOut));
            assertThat(actualStdErr, equalTo(expectedStdErr));

            f.delete();
        }
    }
}