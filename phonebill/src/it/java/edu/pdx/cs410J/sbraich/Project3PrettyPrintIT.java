package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.ParserException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests the functionality in the {@link Project3} main class.
 */
public class Project3PrettyPrintIT extends InvokeMainTestCase
{

     /// Invokes the main method of {@link Project3} with the given arguments.
    private MainMethodResult invokeMain(String... args)
    {
        return invokeMain( Project3.class, args );
    }

    private Date parseDate(String s)  throws ParseException
    {
        String DATE_TIME_FORMAT = "M/d/yyyy h:mm a";

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.US);
        sdf.setLenient(false);
        Date date = sdf.parse(s);

        return date;
    }

    @Test
    public void TestMain_PrettyPrintOption_STDOUT() throws ParseException
    {
        String pretty = "-pretty";
        String prettyStdout = "-";

        String customer = "Acme Corp";
        String caller = "123-456-7890";
        String callee = "234-567-8901";

        String startDate = "07/04/2018";
        String startTime = "06:24";
        String startAMPM = "AM";

        String endDate = "07/04/2018";
        String endTime = "06:48";
        String endAMPM = "PM";

        String start = startDate + " " + startTime + " " + startAMPM;
        String end = endDate + " " + endTime + " " + endAMPM;

        Date d1 = parseDate(start); // Set start date
        Date d2   = parseDate(end); // Set end date

        long duration  = d2.getTime() - d1.getTime();
        int diffInMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(duration);

        MainMethodResult result =
                invokeMain(pretty, prettyStdout, customer, caller, callee,
                        startDate, startTime, startAMPM, endDate, endTime, endAMPM);

        Integer expectedExitCode = 0;
        String expectedStdOut = "Customer: " + customer + "\n"
                + "Total Minutes: 744\n"
                + "\n"
                + "    Caller         Callee      Minutes      Call Start            Call End\n"
                + "---------------------------------------------------------------------------------\n"
                + " " + caller + "   " + callee  + "     " + diffInMinutes + "   " + start + "   " + end + "\n";
        String expectedStdErr = "";

        int actualExitCode = result.getExitCode();
        String actualStdOut = result.getTextWrittenToStandardOut();
        String actualStdErr = result.getTextWrittenToStandardError();

        assertThat(actualExitCode, equalTo(expectedExitCode));
        assertEquals(actualStdOut, expectedStdOut);
        assertEquals(actualStdErr, expectedStdErr);
    }

    @Test
    public void TestMain_PrettyPrintOption_FILE() throws  IOException, ParseException
    {
        String pretty = "-pretty";
        String prettyFile = "pretty_IT.txt";

        String customer = "Acme Corp";
        String caller = "123-456-7890";
        String callee = "234-567-8901";

        String startDate = "07/04/2018";
        String startTime = "06:24";
        String startAMPM = "AM";

        String endDate = "07/04/2018";
        String endTime = "06:48";
        String endAMPM = "PM";

        String start = startDate + " " + startTime + " " + startAMPM;
        String end = endDate + " " + endTime + " " + endAMPM;

        Date d1 = parseDate(start); // Set start date
        Date d2   = parseDate(end); // Set end date

        long duration  = d2.getTime() - d1.getTime();
        int diffInMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(duration);
        File file = new File(prettyFile);

        MainMethodResult result = null;

        try
        {
            result = invokeMain(pretty, prettyFile, customer, caller, callee,
                    startDate, startTime, startAMPM, endDate, endTime, endAMPM);

            assertThat(result.getExitCode(), equalTo(0));

            String expectedOutput = "Customer: " + customer + "\n"
                    + "Total Minutes: 744\n"
                    + "\n"
                    + "    Caller         Callee      Minutes      Call Start            Call End\n"
                    + "---------------------------------------------------------------------------------\n"
                    + " " + caller + "   " + callee + "     " + diffInMinutes + "   " + start + "   " + end;

            List<String> outputLines = Files.readAllLines(file.toPath(), Charset.defaultCharset() );
            String prettyOutput = String.join("\n", outputLines);

            assertThat(result.getExitCode(), equalTo(0));

            assertTrue(file.exists());
            assertTrue(!file.isDirectory());
            assertEquals(expectedOutput, prettyOutput);
        }
        finally
        {
            file.delete();
        }
    }


    @Test
    public void TestMain_CliArgs_ExtraArgs()
    {

        String customer = "My Customer";
        String caller = "123-456-7890";
        String callee = "234-567-8901";

        String startDate = "7/4/2018";
        String startTime = "6:24";
        String startAMPM = "AM";
        String endDate = "7/4/2018";
        String endTime = "6:48";
        String endAMPM = "PM";

        MainMethodResult result =
                invokeMain("-pretty", "-", customer, caller, callee,
                        startDate, startTime, startAMPM, endDate, endTime, endAMPM, "blah");

        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo("Too many command line arguments\n"));
    }



}