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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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

    private String PrettyDate(String d)  throws ParseException
    {
        String PARSE_DATETIME_PATTERN = "M/d/yyyy h:mm a";
        String PRINT_DATETIME_PATTERN = "MM/dd/yyyy hh:mm a";

        SimpleDateFormat sdf = new SimpleDateFormat(PARSE_DATETIME_PATTERN, Locale.US);
        sdf.setLenient(false);
        Date date = sdf.parse(d);

        String formattedDate = new SimpleDateFormat(PRINT_DATETIME_PATTERN).format(date);

        return formattedDate;
    }

    private Date parseDate(String s) throws ParseException
    {
        String DATE_TIME_FORMAT = "M/d/yyyy h:mm a";

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.US);
        sdf.setLenient(false);
        Date date = sdf.parse(s);

        return date;
    }

    // What can you do from main?
    //
    //  Either:
    //    1. Show README
    //          OR...
    //    2. Add Phone Call
    //
    //  If you chose #2, there are options:
    //    A. Pretty Print to STDOUT
    //    B. Pretty Print to a FILE
    //    C. Write output to a FILE
    //    D. Print a description of the new phone call to STDOUT



    // $ -textFile sbraich/sbraich.txt -print Project3 123-456-7890 234-567-9081 01/07/2018 07:00 01/17/2018 17:00
    @Test
    public void Test07_NewPhoneBillFile_withFilePretty() throws ParseException
    {
        String pretty = "-pretty";
        String prettyStdout = "-";

        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich-PrettyNew.txt";

        String printOption = "-print";

        String customer = "Project3";
        String caller = "123-456-7890";
        String callee = "234-567-8901";
        String startDate = "01/07/2018";
        String startTime = "07:00";
        String startAMPM = "AM";;
        String endDate = "01/17/2018";
        String endTime = "05:00";
        String endAMPM = "PM";

        // Get Duration
        String start = PrettyDate(startDate + " " + startTime + " " + startAMPM);
        String end = PrettyDate(endDate + " " + endTime + " " + endAMPM);
        Date d1 = parseDate(start); // Set start date
        Date d2   = parseDate(end); // Set end date
        long duration  = d2.getTime() - d1.getTime();
        int diffInMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(duration);

        // Expected Output

        String stdoutPretty =
                  "Customer: " + customer + "\n"
                + "Total Minutes: 15000\n"
                + "\n"
                + "    Caller         Callee      Minutes      Call Start            Call End\n"
                + "---------------------------------------------------------------------------------\n"
                + " " + caller + "   " + callee  + "   " + diffInMinutes + "   " + start + "   " + end + "\n";

        String stdoutCall = "Phone call from 123-456-7890 to 234-567-8901 from 01/07/2018 07:00 AM to 01/17/2018 05:00 PM\n";

        String expectedStdOut = "StdOut: " + stdoutPretty + stdoutCall;
        String expectedStdErr = "StdErr: " + "";
        Integer expectedExitCode = 0;


        File f = new File(filePath);
        MainMethodResult result = null;

        try
        {
            result = invokeMain
            (
                pretty, prettyStdout,
                fileOption, filePath,
                printOption,
                customer, caller, callee,
                startDate, startTime, startAMPM, endDate, endTime, endAMPM
            );
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
}