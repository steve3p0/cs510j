package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.ParserException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Tests the functionality in the {@link Project3} main class.
 */
public class Project3IT extends InvokeMainTestCase
{
    ///Invokes the main method of {@link Project3} with the given arguments.
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

    /// Tests that invoking the main method with no arguments issues an error
    @Test
    public void TestMain_NoCommandLineArguments()
    {
        MainMethodResult result = invokeMain();
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing command line arguments\n"));
    }

    public void TestMain_ReadmeOptionOnly()
    {
        MainMethodResult result = invokeMain("-README");
        assertThat(result.getExitCode(), equalTo(0));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(Project3.README + "\n"));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    @Test
    public void TestMain_PrintOption() throws ParseException
    {
        String caller = "123-456-7890";
        String callee = "234-567-8901";

        String startDate = "7/4/2018";
        String startTime = "6:24";
        String startAMPM = "AM";
        String endDate = "7/4/2018";
        String endTime = "6:48";
        String endAMPM = "PM";

        String start = PrettyDate(startDate + " " + startTime + " " + startAMPM);
        String end   = PrettyDate(endDate + " " + endTime + " " + endAMPM);

        // Expected Results
        String phoneCallToString = String.format("Phone call from %s to %s from %s to %s", caller, callee, start,end);

        MainMethodResult result = invokeMain("-print", "My Customer", caller, callee,
                        startDate, startTime, startAMPM, endDate, endTime, endAMPM);

        assertThat(result.getExitCode(), equalTo(0));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(phoneCallToString + "\n"));
    }

    @Test
    public void TestMain_ParserNoFile_Dumper() throws IOException, ParseException
    {
        String customer = "My Customer";

        String filePathOption = "-textFile";
        String filePath = "main_parsedump.txt";
        String printOption = "-print";

        String caller = "123-456-7890";
        String callee = "234-567-8901";
        String startDate = "7/4/2018";
        String startTime = "12:24";
        String startAMPM = "AM";
        String endDate = "7/4/2018";
        String endTime = "12:48";
        String endAMPM = "PM";

        String start = PrettyDate(startDate + " " + startTime + " " + startAMPM);
        String end   = PrettyDate(endDate + " " + endTime + " " + endAMPM);

        // Expected Results
        String phoneCallToString = String.format("Phone call from %s to %s from %s to %s", caller, callee, start,end);

        String expectedCallFromFile = "[" + phoneCallToString + "]";
        File f = new File(filePath);

        try
        {
            MainMethodResult result = invokeMain(printOption, filePathOption, filePath, customer, caller, callee,
                    startDate, startTime, startAMPM, endDate, endTime, endAMPM);
            assertThat(result.getExitCode(), equalTo(0));
            assert(f.exists());
            assert(f.length() > 0);

            ArrayList<String> lines = new ArrayList<String>();
            Files.lines(Paths.get(filePath)).forEach(s -> lines.add(s));

            String customerFile = lines.get(0);
            String callFile = lines.get(1);

            assertEquals(customer, customerFile);
            assertEquals(expectedCallFromFile, callFile);

            assertThat(result.getTextWrittenToStandardOut(), equalTo(phoneCallToString + "\n"));
        }
        finally
        {
            f.delete();
        }
    }

    @Test
    public void TestMain_ParserDumper_NameDifferent() throws IOException, ParseException
    {
        String printOption = "-print";
        String textFileOption = "-textFile";
        String filePath = "diff_customer_names.txt";

        String customer1 = "My Customer 1";
        String customer2 = "My Customer 2";

        String caller = "123-456-7890";
        String callee = "234-567-8901";
        String startDate = "7/4/2018";
        String startTime = "6:24";
        String startAMPM = "AM";
        String endDate = "7/4/2018";
        String endTime = "6:48";
        String endAMPM = "PM";

        File f = new File(filePath);

        try
        {
            String start = PrettyDate(startDate + " " + startTime + " " + startAMPM);
            String end   = PrettyDate(endDate + " " + endTime + " " + endAMPM);

            // Expected Results
            String expectedCallFromFile = "[" + String.format("Phone call from %s to %s from %s to %s", caller, callee, start,end) + "]";

            // Setup first textFile call with "My Customer 1"
            MainMethodResult result1 = invokeMain(
                    textFileOption, filePath, printOption,  customer1, caller, callee,
                    startDate, startTime, startAMPM, endDate, endTime, endAMPM);

            assertThat(result1.getExitCode(), equalTo(0));
            assert(f.exists());
            assert(f.length() > 0);

            ArrayList<String> lines = new ArrayList<String>();
            Files.lines(Paths.get(filePath)).forEach(s -> lines.add(s));

            String customerFile = lines.get(0);
            String callFile = lines.get(1);

            assertEquals(customer1, customerFile);
            assertEquals(expectedCallFromFile, callFile);

            // Setup second call with "My Customer 2"
            MainMethodResult result2 = invokeMain(
                    textFileOption, filePath, printOption, customer2, caller, callee,
                    startDate, startTime, startAMPM, endDate, endTime, endAMPM);
        }
        finally
        {
            f.delete();
        }
    }

    @Test
    public void TestMain_CliArgs_PhoneCallOnly()
    {
        String caller = "123-456-7890";
        String callee = "234-567-8901";

        String startDate = "07/04/2018";
        String startTime = "6:24";
        String startAMPM = "AM";
        String endDate = "07/04/2018";
        String endTime = "6:48";
        String endAMPM = "PM";

        MainMethodResult result =
                invokeMain("My Customer", caller, callee,
                        startDate, startTime, startAMPM, endDate, endTime, endAMPM);

        assertThat(result.getExitCode(), equalTo(0));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    @Test
    public void TestMain_CliArgs_ExtraArgs()
    {
        String caller = "123-456-7890";
        String callee = "234-567-8901";

        String startDate = "07/04/2018";
        String startTime = "6:24";
        String startAMPM = "AM";
        String endDate = "07/04/2018";
        String endTime = "6:48";
        String endAMPM = "PM";

        MainMethodResult result =
                invokeMain("My Customer", caller, callee,
                        startDate, startTime, startAMPM, endDate, endTime, endAMPM, "blah");

        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo("Too many command line arguments\n"));
    }
}