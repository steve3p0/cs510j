package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.After;
import org.junit.Before;
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
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/// Tests the functionality in the {@link Project3} main class.
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GraderIT_Resubmit extends InvokeMainTestCase
{
//    @Before
//    public void setUp() throws Exception
//    {
////        File f = new File("sbraich/sbraich.txt");
////        Files.deleteIfExists(f.toPath());
//    }
//
//    @After
//    public void tearDown() throws Exception
//    {
//
//    }

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

    private Date parseDate(String s)  throws ParseException
    {
        String DATE_TIME_FORMAT = "M/d/yyyy h:mm a";

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.US);
        sdf.setLenient(false);
        Date date = sdf.parse(s);

        return date;
    }

    /// Invokes the main method of {@link Project3} with the given arguments.
    private MainMethodResult invokeMain(String... args)
    {
        return invokeMain( Project3.class, args );
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
        String readmeOutput = Project3.README + Project3.USAGE;

        Integer expectedExitCode = 0;
        String expectedStdOut = "StdOut: " + readmeOutput + "\n";
        String expectedStdErr = "StdErr: " + "";

        MainMethodResult result = invokeMain(readmeOption);

        Integer actualExitCode = result.getExitCode();
        String actualStdOut = "StdOut: " + result.getTextWrittenToStandardOut();
        String actualStdErr = "StdErr: " + result.getTextWrittenToStandardError();

        // Make Assertions
        assertThat(actualExitCode, equalTo(expectedExitCode));
        assertThat(actualStdOut, equalTo(expectedStdOut));
        assertThat(actualStdErr, equalTo(expectedStdErr));
    }

    // -textFile sbraich/sbraich.txt Project3 123-456-7890 234-567-8901 01/03/2018 11:00 am 01/03/2018 1:00 pm
    @Test
    public void Test03_Starting_NewPhoneBillFile() throws IOException, ParseException
    {
        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich.txt";
        String customer = "Project3";
        String caller = "123-456-7890";
        String callee = "234-567-8901";
        String startDate = "01/03/2018";
        String startTime = "11:00";
        String startAMPM = "am";
        String endDate = "01/03/2018";
        String endTime = "1:00";
        String endAMPM = "pm";

        File f = new File(filePath);
        Files.deleteIfExists(f.toPath());

        String start = PrettyDate(startDate + " " + startTime + " " + startAMPM);
        String end   = PrettyDate(endDate + " " + endTime + " " + endAMPM);

        // Expected Results
        String expectedCallFromFile = "[" + String.format("Phone call from %s to %s from %s to %s", caller, callee, start,end) + "]";

        Integer expectedExitCode = 0;
        String expectedStdOut = "StdOut: " + "";
        String expectedStdErr = "StdErr: " + "";

        //File f = new File(filePath);
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

            assert(f.exists());
            assert(f.length() > 0);

            assertThat(actualExitCode, equalTo(expectedExitCode));
            assertThat(actualStdOut, equalTo(expectedStdOut));
            assertThat(actualStdErr, equalTo(expectedStdErr));

            ArrayList<String> lines = new ArrayList<String>();
            Files.lines(Paths.get(filePath)).forEach(s -> lines.add(s));

            String customerFile = lines.get(0);
            String callFile = lines.get(1);

            assertEquals(customer, customerFile);
            assertEquals(expectedCallFromFile, callFile);

            //f.delete();
        }
    }

    // -textFile sbraich/sbraich.txt Project3 123-456-7890 345-134-6134 01/04/2018 10:00 am 01/04/2018 11:30 am
    @Test
    public void Test04_UsingAnExistingPhoneBillFile() throws IOException, ParseException
    {
        // Create the existing file with one phone call
        String fileOption1 = "-textFile";
        String filePath1 = "sbraich/sbraich.txt";
        String customer1 = "Project3";
        String caller1 = "123-456-7890";
        String callee1 = "234-567-8901";
        String startDate1 = "01/03/2018";
        String startTime1 = "11:00";
        String startAMPM1 = "am";
        String endDate1 = "01/03/2018";
        String endTime1 = "1:00";
        String endAMPM1 = "pm";

        String start1 = PrettyDate(startDate1 + " " + startTime1 + " " + startAMPM1);
        String end1   = PrettyDate(endDate1 + " " + endTime1 + " " + endAMPM1);

        String setupLine = String.format("Phone call from %s to %s from %s to %s", caller1, callee1,  start1, end1);
        List<String> setupLines = Arrays.asList(customer1, "[" + setupLine + "]");

        // Create the 'EXISTING FILE
        File dir = new File(Paths.get(filePath1).getParent().toString());
        if (!dir.exists())
        {
            Files.createDirectories(Paths.get(filePath1).getParent());
        }
        Files.write(Paths.get(filePath1), setupLines, Charset.forName("UTF-8"));

        // -textFile sbraich/sbraich.txt Project3 123-456-7890 234-567-8901 01/03/2018 11:00 am 01/03/2018 1:00 pm
        String expectedCall1_FromFile = "[" + String.format("Phone call from %s to %s from %s to %s", caller1, callee1, start1, end1) + "]";

        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich.txt";
        String customer = "Project3";
        String caller = "123-456-7890";
        String callee = "345-134-6134";
        String startDate = "01/04/2018";
        String startTime = "10:00";
        String startAMPM = "am";
        String endDate = "01/04/2018";
        String endTime = "11:30";
        String endAMPM = "am";

        String start = PrettyDate(startDate + " " + startTime + " " + startAMPM);
        String end   = PrettyDate(endDate + " " + endTime + " " + endAMPM);

        // Expected Results
        String expectedCall2_FromFile = "[" + String.format("Phone call from %s to %s from %s to %s", caller, callee, start,end) + "]";
        // -textFile sbraich/sbraich.txt Project3 123-456-7890 345-134-6134 01/04/2018 10:00 am 01/04/2018 11:30 am


        Integer expectedExitCode = 0;
        String expectedStdOut = "StdOut: " + "";
        String expectedStdErr = "StdErr: " + "";

        File f = new File(filePath);
        MainMethodResult result = null;

        try
        {
            result = invokeMain(fileOption, filePath, customer, caller, callee,
                    startDate, startTime, startAMPM, endDate, endTime, endAMPM);

            Integer actualExitCode = result.getExitCode();
            String actualStdOut = "StdOut: " + result.getTextWrittenToStandardOut();
            String actualStdErr = "StdErr: " + result.getTextWrittenToStandardError();

            assert(f.exists());

            assertThat(actualExitCode, equalTo(expectedExitCode));
            //assertThat(actualStdOut, equalTo(expectedStdOut));
            assertThat(actualStdErr, equalTo(expectedStdErr));

            ArrayList<String> lines = new ArrayList<String>();
            Files.lines(Paths.get(filePath)).forEach(s -> lines.add(s));

            String customerFile = lines.get(0);
            String call1File = lines.get(1);
            String call2File = lines.get(2);

            assertEquals(customer, customerFile);
            assertEquals(expectedCall1_FromFile, call1File);
            assertEquals(expectedCall2_FromFile, call2File);
        }
        finally
        {
            f.delete();
        }
    }

    @Test
    public void Test05_DifferentCustomerName() throws IOException, NoSuchFileException, ParseException
    {
        // -textFile sbraich/sbraich.txt Project3 123-456-7890 234-567-8901 01/03/2018 11:00 am 01/03/2018 1:00 pm

        // Setup Variables
        String setupCustomer = "Project3";
        String setupCaller = "123-456-7890";
        String setupCallee = "234-567-8901";
        String setupStartDate = "01/03/2018";
        String setupStartTime = "11:00";
        String setupStartAMPM = "am";
        String setupEndDate = "01/03/2018";
        String setupEndTime = "1:00";
        String setupEndAMPM = "pm";

        String setupStart = PrettyDate(setupStartDate + " " + setupStartTime + " " + setupStartAMPM);
        String setupEnd   = PrettyDate(setupEndDate + " " + setupEndTime + " " + setupEndAMPM);

        String setupLine = String.format("Phone call from %s to %s from %s to %s", setupCaller, setupCallee, setupStart, setupEnd);
        List<String> setupLines = Arrays.asList(setupCustomer, "[" + setupLine + "]");

        //-textFile sbraich/sbraich.txt DIFFERENT 123-456-7890 213-124-6311 01/05/2018 5:00 am 01/05/2018 6:14 am
        // Input Arguments
        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich.txt";
        String customer = "DIFFERENT";
        String caller = "123-456-7890";
        String callee = "213-124-6311";
        String startDate = "01/05/2018";
        String startTime = "5:00";
        String startAMPM = "am";
        String endDate = "01/05/2018";
        String endTime = "6:14";
        String endAMPM = "am";

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

            Integer actualExitCode = result.getExitCode();
            String actualStdOut = "StdOut: " + result.getTextWrittenToStandardOut();
            String actualStdErr = "StdErr: " + result.getTextWrittenToStandardError();

            assertThat(actualExitCode, equalTo(expectedExitCode));
            assertThat(actualStdOut, equalTo(expectedStdOut));
            assertThat(actualStdErr, equalTo(expectedStdErr));
        }
        finally
        {
            f.delete();
        }
    }

    //-textFile sbraich/sbraich.txt Project3 123-456-7890 153-234-2521 01/07/2018 7:00 am 01/ZZ/2018 7:00 pm
    @Test
    public void Test06_EndTimeIsMalformatted()
    {
        // Input Argument Options
        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich-x.txt";

        // Input Argument Paramenters
        String customer = "Project3";
        String caller = "123-456-7890";
        String callee = "153-234-2521";
        String startDate = "01/07/2018";
        String startTime = "7:00";
        String startAMPM = "am";
        String endDate = "01/ZZ/2018";
        String endTime = "7:00";
        String endAMPM = "pm";

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

    //-textFile sbraich/sbraich.txt -pretty - Project3 123-456-7890 124-351-4234 12/08/2018 8:00 am 12/08/2018 8:15 am
    @Test
    public void Test07_AddingAnotherPhoneCallSorting() throws IOException, ParseException
    {
        // Create the existing file with one phone call
        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich-3.txt";
        String pretty = "-pretty";
        String prettyStdout = "-";

        String customer = "Project3";
        String caller = "123-456-7890";
        String callee = "124-351-4234";
        String startDate = "12/08/2018";
        String startTime = "8:00";
        String startAMPM = "am";
        String endDate = "12/08/2018";
        String endTime = "8:15";
        String endAMPM = "am";

        String start = PrettyDate(startDate + " " + startTime + " " + startAMPM);
        String end   = PrettyDate(endDate + " " + endTime + " " + endAMPM);

        Date startD = parseDate(start); // Set start date
        Date endD   = parseDate(end); // Set end date

        long duration  = endD.getTime() - startD.getTime();
        int diffInMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(duration);

        String expectedNewCall = "[" + String.format("Phone call from %s to %s from %s to %s", caller, callee,  start, end) + "]";

        // Call 1:
        //-textFile sbraich/sbraich.txt Project3 123-456-7890 234-567-8901 01/03/2018 11:00 am 01/03/2018 1:00 pm
        String customer1 = "Project3";
        String caller1 = "123-456-7890";
        String callee1 = "234-567-8901";
        String startDate1 = "1/3/2018";
        String startTime1 = "11:00";
        String startAMPM1 = "am";
        String endDate1 = "1/3/2018";
        String endTime1 = "1:00";
        String endAMPM1 = "pm";

        String start1 = PrettyDate(startDate1 + " " + startTime1 + " " + startAMPM1);
        String end1   = PrettyDate(endDate1   + " " + endTime1   + " " + endAMPM1);

        Date startD1 = parseDate(start1); // Set start date
        Date endD1   = parseDate(end1); // Set end date

        long duration1  = endD1.getTime() - startD1.getTime();
        int diffInMinutes1 = (int) TimeUnit.MILLISECONDS.toMinutes(duration1);

        String setupCall1 = String.format("[Phone call from %s to %s from %s to %s]", caller1, callee1, start1, end1);

        // Call 2:
        //-textFile sbraich/sbraich.txt Project3 123-456-7890 345-134-6134 01/04/2018 10:00 am 01/04/2018 11:30 am
        String customer2 = "Project3";
        String caller2 = "123-456-7890";
        String callee2 = "345-134-6134";
        String startDate2 = "01/04/2018";
        String startTime2 = "10:00";
        String startAMPM2 = "am";
        String endDate2 = "01/04/2018";
        String endTime2 = "11:30";
        String endAMPM2 = "am";

        String start2 = PrettyDate(startDate2 + " " + startTime2 + " " + startAMPM2);
        String end2   = PrettyDate(endDate2   + " " + endTime2   + " " + endAMPM2);

        Date startD2 = parseDate(start2); // Set start date
        Date endD2   = parseDate(end2);   // Set end date

        long duration2  = endD2.getTime() - startD2.getTime();
        int diffInMinutes2 = (int) TimeUnit.MILLISECONDS.toMinutes(duration2);

        String setupCall2 = String.format("[Phone call from %s to %s from %s to %s]", caller2, callee2, start2, end2);

        // Create existing file
        List<String> setupLines = new ArrayList<String>();
        setupLines.add(customer1);
        setupLines.add(setupCall1);
        setupLines.add(setupCall2);

        File dir = new File(Paths.get(filePath).getParent().toString());
        if (!dir.exists())
        {
            Files.createDirectories(Paths.get(filePath).getParent());
        }
        Files.write(Paths.get(filePath), setupLines, Charset.forName("UTF-8"));


        // Create expected Pretty Print Output
        int expectetdTotalMinutes = diffInMinutes1 + diffInMinutes2 + diffInMinutes;
        String expectedPrettyPrint = "Customer: " + customer + "\n"
                + "Total Minutes: " + expectetdTotalMinutes + "\n"
                + "\n"
                + "    Caller         Callee      Minutes      Call Start            Call End\n"
                + "---------------------------------------------------------------------------------\n"
                + " " + caller1 + "   " + callee1  + "     "  + diffInMinutes1 + "   " + start1 + "   " + end1 + "\n"
                + " " + caller2 + "   " + callee2  + "      " + diffInMinutes2 + "   " + start2 + "   " + end2 + "\n"
                + " " + caller  + "   " + callee   + "      " + diffInMinutes  + "   " + start  + "   " + end  + "\n";


        Integer expectedExitCode = 0;
        String expectedStdOut = "StdOut: " + expectedPrettyPrint;
        String expectedStdErr = "StdErr: " + "";

        File f = new File(filePath);
        MainMethodResult result = null;

        try
        {
            //-textFile sbraich/sbraich.txt -pretty -
            // Project3 123-456-7890 124-351-4234 
            // 12/08/2018 8:00 am 12/08/2018 8:15 am
            result = invokeMain(fileOption, filePath, pretty, prettyStdout,
                    customer, caller, callee,
                    startDate, startTime, startAMPM, endDate, endTime, endAMPM);

            Integer actualExitCode = result.getExitCode();
            String actualStdOut = "StdOut: " + result.getTextWrittenToStandardOut();
            String actualStdErr = "StdErr: " + result.getTextWrittenToStandardError();

            assert(f.exists());

            assertThat(actualExitCode, equalTo(expectedExitCode));
            assertThat(actualStdOut, equalTo(expectedStdOut));
            assertThat(actualStdErr, equalTo(expectedStdErr));

            ArrayList<String> lines = new ArrayList<String>();
            Files.lines(Paths.get(filePath)).forEach(s -> lines.add(s));

            String customerFile = lines.get(0);
            String call1fromFile = lines.get(1);
            String call2fromFile = lines.get(2);
            String newCall = lines.get(3);

            assertEquals(customer, lines.get(0));
            assertEquals(setupCall1, lines.get(1));
            assertEquals(setupCall2, lines.get(2));
            assertEquals(expectedNewCall, lines.get(3));
        }
        finally
        {
            f.delete();
        }
    }

    //-textFile sbraich/sbraich.txt -pretty - Project3 123-456-7890 134-124-6234 01/09/2018 9:00 am 01/09/2018 12:34 pm
    @Test
    public void Test08_PrettyPrintingStdOut_withTextFile() throws ParseException
    {
        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich-8.txt";

        String pretty = "-pretty";
        String prettyStdout = "-";

        String customer = "Project3";
        String caller = "123-456-7890";
        String callee = "134-124-6234";
        String startDate = "01/09/2018";
        String startTime = "9:00";
        String startAMPM = "am";;
        String endDate = "01/09/2018";
        String endTime = "12:34";
        String endAMPM = "pm";

        // Get Duration
        String start = PrettyDate(startDate + " " + startTime + " " + startAMPM);
        String end = PrettyDate(endDate + " " + endTime + " " + endAMPM);
        Date d1 = parseDate(start); // Set start date
        Date d2   = parseDate(end); // Set end date
        long duration  = d2.getTime() - d1.getTime();
        int diffInMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(duration);
        String minFormatted = String.format("%5d", diffInMinutes);

        // Expected Output
        String stdoutPretty =
                "Customer: " + customer + "\n"
                        + "Total Minutes: " + diffInMinutes + "\n"
                        + "\n"
                        + "    Caller         Callee      Minutes      Call Start            Call End\n"
                        + "---------------------------------------------------------------------------------\n"
                        + " " + caller + "   " + callee  + "   " + minFormatted + "   " + start + "   " + end + "\n";

        String expectedStdOut = "StdOut: " + stdoutPretty;
        String expectedStdErr = "StdErr: " + "";
        Integer expectedExitCode = 0;

        File f = new File(filePath);
        MainMethodResult result = null;

        try
        {
            //-textFile sbraich/sbraich.txt -pretty - Project3 123-456-7890 134-124-6234 01/09/2018 9:00 am 01/09/2018 12:34 pm
            result = invokeMain
            (
                fileOption, filePath,
                pretty, prettyStdout,
                customer, caller, callee,
                startDate, startTime, startAMPM, endDate, endTime, endAMPM
            );

            String actualStdOut = "StdOut: " + result.getTextWrittenToStandardOut();
            String actualStdErr = "StdErr: " + result.getTextWrittenToStandardError();
            Integer actualExitCode = result.getExitCode();

            assertThat(actualStdOut, equalTo(expectedStdOut));
            assertThat(actualStdErr, equalTo(expectedStdErr));
            assertThat(actualExitCode, equalTo(expectedExitCode));
        }
        finally
        {
            f.delete();
        }
    }

    // -textFile sbraich/sbraich.txt -pretty sbraich/sbraich-pretty.txt Project3 123-456-7890 452-234-2125 01/10/2018 10:00 am 01/10/2018 3:45 pm
    @Test
    public void Test09_PrettyPrintOption_FILE() throws  IOException, ParseException
    {
        String fileOption = "-textFile";
        String filePath = "sbraich/sbraich-9.txt";

        String pretty = "-pretty";
        String prettyFile = "sbraich/pretty-9.txt";

        String customer = "Project3";
        String caller = "123-456-7890";
        String callee = "452-234-2125";

        String startDate = "01/10/2018";
        String startTime = "10:00";
        String startAMPM = "am";

        String endDate = "01/10/2018";
        String endTime = "3:45";
        String endAMPM = "pm";

        String start = PrettyDate(startDate + " " + startTime + " " + startAMPM);
        String end = PrettyDate(endDate + " " + endTime + " " + endAMPM);

        Date d1 = parseDate(start);
        Date d2   = parseDate(end);

        long duration  = d2.getTime() - d1.getTime();
        int diffInMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(duration);
        String minFormatted = String.format("%5d", diffInMinutes);

        File tFile = new File(filePath);
        File pFile = new File(prettyFile);

        MainMethodResult result = null;

        try
        {
            // -textFile sbraich/sbraich.txt -pretty sbraich/sbraich-pretty.txt Project3 123-456-7890 452-234-2125 01/10/2018 10:00 am 01/10/2018 3:45 pm
            result = invokeMain
            (
                fileOption, filePath,
                pretty, prettyFile,
                customer, caller, callee,
                startDate, startTime, startAMPM, endDate, endTime, endAMPM
            );

            assertTrue(tFile.exists());
            assertTrue(pFile.exists());

            // Expected Output
            String expectedOutput =
                    "Customer: " + customer + "\n"
                            + "Total Minutes: " + diffInMinutes + "\n"
                            + "\n"
                            + "    Caller         Callee      Minutes      Call Start            Call End\n"
                            + "---------------------------------------------------------------------------------\n"
                            + " " + caller + "   " + callee  + "   " + minFormatted + "   " + start + "   " + end;

            String expectedStdOut = "StdOut: " + "";
            String expectedStdErr = "StdErr: " + "";
            Integer expectedExitCode = 0;

            // Actual Output
            List<String> outputLines = Files.readAllLines(pFile.toPath(), Charset.defaultCharset() );
            String prettyOutput = String.join("\n", outputLines);

            Integer actualExitCode = result.getExitCode();
            String actualStdOut = "StdOut: " + result.getTextWrittenToStandardOut();
            String actualStdErr = "StdErr: " + result.getTextWrittenToStandardError();

            // Make Assertions
            assertEquals(expectedOutput, prettyOutput);
            assertThat(actualStdOut, equalTo(expectedStdOut));
            assertThat(actualStdErr, equalTo(expectedStdErr));
            assertThat(actualExitCode, equalTo(expectedExitCode));
        }
        finally
        {
            tFile.delete();
            pFile.delete();
        }
    }

    // Test10 123-456-7890 452-234-2125 01/10/2018 10:00 pm 01/10/2018 3:45 pm
    @Test
    public void Test10_StartDate_After_EndDate() throws IOException, ParseException
    {
        String customer = "Test10";
        String caller = "123-456-7890";
        String callee = "452-234-2125";
        String startDate = "01/10/2018";
        String startTime = "10:00";
        String startAMPM = "pm";
        String endDate = "01/10/2018";
        String endTime = "3:45";
        String endAMPM = "pm";

        String start = PrettyDate(startDate + " " + startTime + " " + startAMPM);
        String end   = PrettyDate(endDate + " " + endTime + " " + endAMPM);

        // Expected Results
        String expectedOutput = "The start date '" + start +"' must come before the end date " + "'" + end + "'";

        Integer expectedExitCode = 1;
        String expectedStdOut = "StdOut: " + "";
        String expectedStdErr = "StdErr: " + expectedOutput + "\n";

        //File f = new File(filePath);
        MainMethodResult result = null;

        try
        {
            result = invokeMain
            (
                customer,
                caller, callee,
                startDate, startTime, startAMPM,
                endDate, endTime, endAMPM
            );

        }
        finally
        {
            Integer actualExitCode = result.getExitCode();
            String actualStdOut = "StdOut: " + result.getTextWrittenToStandardOut();
            String actualStdErr = "StdErr: " + result.getTextWrittenToStandardError();

            assertThat(actualExitCode, equalTo(expectedExitCode));
            assertThat(actualStdOut, equalTo(expectedStdOut));
            assertThat(actualStdErr, equalTo(expectedStdErr));
        }
    }

    /////////////////////////////////////////////////////////////

}