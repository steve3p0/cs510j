package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.ParserException;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


/// Tests the functionality in the {@link Project3} main class.
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GraderIT extends InvokeMainTestCase
{
    /// Invokes the main method of {@link Project3} with the given arguments.
    private MainMethodResult invokeMain(String... args)
    {
        return invokeMain( Project3.class, args );
    }

    public String PrettyDate(String d)  throws ParseException
    {
        String PARSE_DATETIME_PATTERN = "M/d/yyyy h:mm a";
        String PRINT_DATETIME_PATTERN = "MM/dd/yyyy hh:mm a";

        SimpleDateFormat sdf = new SimpleDateFormat(PARSE_DATETIME_PATTERN, Locale.US);
        sdf.setLenient(false);
        Date date = sdf.parse(d);

        String formattedDate = new SimpleDateFormat(PRINT_DATETIME_PATTERN).format(date);

        return formattedDate;
    }

    @Test
    public void Test01_NoArguments()
    {
        Integer expectedExitCode = 1;
        String expectedStdOut = "";
        String expectedStdErr = "Missing command line arguments";

        MainMethodResult result = invokeMain();

        assertThat(result.getExitCode(), equalTo(expectedExitCode));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(expectedStdOut));
        assertThat(result.getTextWrittenToStandardError(), containsString(expectedStdErr));
    }

    @Test
    public void Test02_YourReadme()
    {
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
    @Test
    public void Test03_CallerPhoneNumberContainsNonIntegers()
    {
        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich-x.txt";
        String customer = "Test3";
        String caller = "ABC-123-4567";
        String callee = "123-456-7890";
        String startDate = "03/03/2018";
        String startTime = "12:00";
        String startAMPM = "AM";
        String endDate = "03/03/2018";
        String endTime = "4:00";
        String endAMPM = "PM";

        Integer expectedExitCode = 1;
        String expectedStdOut = "StdOut: " + "";
        String expectedStdErr = "StdErr: '" + caller + "' is not a valid phone number" + "\n";

        File f = new File(filePath);
        MainMethodResult result = null;

        try
        {
            result = invokeMain(fileOption, filePath, customer, caller, callee,
                    startDate, startTime, startAMPM, endDate, endTime, endAMPM);
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
    @Test
    public void Test04_StartTimeIsMalformatted()
    {
        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich-x.txt";
        String customer = "Test4";
        String caller = "123-456-7890";
        String callee = "234-567-8901";
        String startDate = "03/03/2018";
        String startTime = "12:XX";
        String startAMPM = "AM";
        String endDate = "03/03/2018";
        String endTime = "4:00";
        String endAMPM = "PM";

        Integer expectedExitCode = 1;
        String expectedStdOut = "StdOut: ";
        String expectedStdErr = "StdErr: " + "'" + startDate + " " + startTime + " " + startAMPM + "' is not a valid date/time"
                + " in the format of 'M/d/yyyy h:mm a'" + "\n";
        File f = new File(filePath);
        MainMethodResult result = null;

        try
        {
            result = invokeMain(fileOption, filePath, customer, caller, callee,
                    startDate, startTime, startAMPM, endDate, endTime, endAMPM);
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
    @Test
    public void Test05_EndTimeIsMalformatted()
    {
        // Input Argument Options
        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich-x.txt";

        // Input Argument Paramenters
        String customer = "Test5";
        String caller = "123-456-7890";
        String callee = "234-567-8901";
        String startDate = "03/03/2018";
        String startTime = "12:00";
        String startAMPM = "AM";
        String endDate = "01/04/20/1";
        String endTime = "4:00";
        String endAMPM = "PM";

        // Expected Output
        Integer expectedExitCode = 1;
        String expectedStdOut = "StdOut: ";
        String expectedStdErr = "StdErr: " + "'" + endDate + " " + endTime + " " + endAMPM
                + "' is not a valid date/time in the format of 'M/d/yyyy h:mm a'\n";

        File f = new File(filePath);
        MainMethodResult result = null;

        try
        {
            result = invokeMain(fileOption, filePath, customer, caller, callee,
                            startDate, startTime, startAMPM, endDate, endTime, endAMPM);
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

    @Test
    public void Test06_UnknownCommandLineArgument()
    {
        // Arguments
        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich-x.txt";
        String customer = "Test6";
        String caller = "123-456-7890";
        String callee = "234-567-8901";
        String startDate = "03/03/2018";
        String startTime = "12:00";
        String startAMPM = "AM";
        String endDate = "01/04/2018";
        String endTime = "4:00";
        String endAMPM = "PM";

        // Extra Arguments
        String extraArg = "fred";

        // Expected Arguments
        Integer expectedExitCode = 1;
        String expectedStdOut = "StdOut: " + "";
        String expectedStdErr = "StdErr: " + "Too many command line arguments" + "\n";

        // File and Result Object
        File f = new File(filePath);
        MainMethodResult result = null;

        try
        {
            result = invokeMain(fileOption, filePath, customer, caller, callee,
                    startDate, startTime, startAMPM, endDate, endTime, endAMPM, extraArg);
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
    @Test
    public void Test07_StartingNewPhoneBillFile() throws ParseException
    {
        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich.txt";
        String printOption = "-print";

        String customer = "Project3";
        String caller = "123-456-7890";
        String callee = "234-567-8901";
        String startDate = "1/7/2018";
        String startTime = "7:00";
        String startAMPM = "AM";;
        String endDate = "1/17/2018";
        String endTime = "5:00";
        String endAMPM = "PM";

        String start = PrettyDate(startDate + " " + startTime + " " + startAMPM);
        String end   = PrettyDate(endDate + " " + endTime + " " + endAMPM);

        // Expected Results
        String expectedCallFromFile = String.format("Phone call from %s to %s from %s to %s", caller, callee, start, end);

        Integer expectedExitCode = 0;
        String expectedStdOut = "StdOut: " + expectedCallFromFile + "\n";
        String expectedStdErr = "StdErr: " + "";

        File f = new File(filePath);
        MainMethodResult result = null;

        try
        {
            result = invokeMain(fileOption, filePath, printOption, customer, caller, callee,
                    startDate, startTime, startAMPM, endDate, endTime, endAMPM);
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
    @Test
    public void Test08_UsingAnExistingPhoneBillFile() throws IOException, NoSuchFileException, ParseException
    {
        // Setup Variables
        String setupCustomer = "Project3";
        String setupCaller = "123-456-7890";
        String setupCallee = "234-567-8901";
        String setupStartDate = "1/7/2018";
        String setupStartTime = "07:00";
        String setupStartAMPM = "AM";;
        String setupEndDate = "1/17/2018";
        String setupEndTime = "5:00";
        String setupEndAMPM = "PM";

        String setupStart = PrettyDate(setupStartDate + " " + setupStartTime + " " + setupStartAMPM);
        String setupEnd   = PrettyDate(setupEndDate + " " + setupEndTime + " " + setupEndAMPM);

        // Expected Results
        String setupLine = String.format("Phone call from %s to %s from %s to %s", setupCaller, setupCallee, setupStart, setupEnd);
        List<String> setupLines = Arrays.asList(setupCustomer, "[" + setupLine + "]");

        // Input Arguments
        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich.txt";
        String printOption = "-print";
        String customer = "Project3";
        String caller = "123-456-7890";
        String callee = "456-789-0123";
        String startDate = "1/7/2018";
        String startTime = "7:00";
        String startAMPM = "AM";;
        String endDate = "1/17/2018";
        String endTime = "5:00";
        String endAMPM = "PM";

        String start = PrettyDate(startDate + " " + startTime + " " + startAMPM);
        String end   = PrettyDate(endDate + " " + endTime + " " + endAMPM);

        // Expected Results
        String expectedCallFromFile = String.format("Phone call from %s to %s from %s to %s", caller, callee, start, end);

        // Expected Outputs
        String expectedStdOut = "StdOut: " + expectedCallFromFile + "\n";
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
            result = invokeMain(fileOption, filePath, printOption, customer, caller, callee,
                    startDate, startTime, startAMPM, endDate, endTime, endAMPM);
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
    @Test
    public void Test09_DifferentCustomerName() throws IOException, NoSuchFileException
    {
        // Setup Variables
        String setupCustomer = "Project3";
        String setupCaller = "123-456-7890";
        String setupCallee = "456-789-0123";
        String setupStartDate = "1/7/2018";
        String setupStartTime = "7:00";
        String setupStartAMPM = "AM";
        String setupEndDate = "1/17/2018";
        String setupEndTime = "5:00";
        String setupEndAMPM = "PM";

        String setupLine = String.format("Phone call from %s to %s from %s %s %s to %s %s %s",
                setupCaller, setupCallee,
                setupStartDate, setupStartTime, setupStartAMPM, setupEndDate, setupEndTime, setupEndAMPM);
        List<String> setupLines = Arrays.asList(setupCustomer, "[" + setupLine + "]");

        // Input Arguments
        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich.txt";
        String customer = "DIFFERENT";
        String caller = "123-456-7890";
        String callee = "789-012-3456";
        String startDate = "1/7/2018";
        String startTime = "7:00";
        String startAMPM = "AM";
        String endDate = "1/17/2018";
        String endTime = "5:00";
        String endAMPM = "PM";

        // Expected Output
        Integer expectedExitCode = 1;
        String expectedStdOut = "StdOut: " + "";
        String expectedStdErr = "StdErr: " + "Customer from command line '" + customer + "' does not match customer '" + setupCustomer + "' in file '" + filePath.toString() + "'\n";

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
            result = invokeMain(fileOption, filePath, customer, caller, callee,
                    startDate, startTime, startAMPM, endDate, endTime, endAMPM);

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
    @Test
    public void Test10_MalformattedTextFile() throws IOException, NoSuchFileException
    {
        // Setup Variables
        String setupCustomer = "Acme Corp";

        String setupCall = "2r9sadvas";
        String setupExtraLine1 = "lcv913r";
        String setupExtraLine2 = "2r7o9av";

        List<String> setupLines = Arrays.asList(setupCustomer, setupCall, setupExtraLine1, setupExtraLine2);

        // Input Arguments
        String customer = "Acme Corp";

        String fileOption = "-textFile";
        String filePath = "sbraich/bogus.txt";

        String caller = "123-456-7890";
        String callee = "385-284-2342";
        String startDate = "1/7/2018";
        String startTime = "7:00";
        String startAMPM = "AM";;
        String endDate = "1/17/2018";
        String endTime = "5:00";
        String endAMPM = "PM";

        // Expected Return Output
        Integer expectedExitCode = 1;
        String expectedStdOut = "StdOut: " + "";
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
            result = invokeMain(fileOption, filePath, customer, caller, callee,
                    startDate, startTime, startAMPM, endDate, endTime, endAMPM);
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
    @Test
    public void Test11_FileWithInvalidYear() throws IOException
    {
        // Setup Variables
        String setupCustomer = "Acme Corp";

        String setupCaller = "123-456-7890";
        String setupCallee = "789-012-3456";
        String setupStartDate = "01/09/XXXX";
        String setupStartTime = "09:00";
        String setupStartAMPM = "AM";
        String setupEndDate = "02/04/XXXX";
        String setupEndTime = "4:00";
        String setupEndAMPM = "PM";

        String setupCall = String.format("Phone call from %s to %s from %s %s %s to %s %s %s",
                setupCaller, setupCallee,
                setupStartDate, setupStartTime, setupStartAMPM, setupEndDate, setupEndTime, setupEndAMPM);

        List<String> setupLines = Arrays.asList(setupCustomer, setupCall);

        // Input Arguments
        String customer = "Acme Corp";

        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich-bad-year.txt";

        String caller = "123-456-7890";
        String callee = "385-284-2342";
        String startDate = "1/7/2018";
        String startTime = "7:00";
        String startAMPM = "AM";;
        String endDate = "1/17/2018";
        String endTime = "5:00";
        String endAMPM = "PM";

        // Expected Return Output
        Integer expectedExitCode = 1;
        String expectedStdOut = "StdOut: " + "";
        String expectedStdErr = "StdErr: " + "Invalid DateTime: " +
                String.format("Phone call from %s to %s from %s %s %s to %s %s %s\n",
                        setupCaller, setupCallee,
                        setupStartDate, setupStartTime, setupStartAMPM, setupEndDate, setupEndTime, setupEndAMPM);

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
            result = invokeMain(fileOption, filePath, customer, caller, callee,
                    startDate, startTime, startAMPM, endDate, endTime, endAMPM);
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