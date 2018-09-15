package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.hamcrest.CoreMatchers;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

/**
 * Tests the {@link Project4} class by invoking its main method with various arguments
 * NOTE:
 * In order to run this test, the jetty web server needs to be running with this command:
 * $ mvn jetty:run
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Project4IT_sbraich extends InvokeMainTestCase
{
    private static final String HOSTNAME = "localhost";
    private static final String PORT = System.getProperty("http.port", "8080");

    @Test
    public void test0RemoveAllMappings() throws IOException
    {
        PhoneBillRestClient client = new PhoneBillRestClient(HOSTNAME, Integer.parseInt(PORT));
        client.removeAllDictionaryEntries();
    }

    private String Add3Calls() throws PhoneBillException, ParseException
    {
        String host = "-host";
        String hostname = "localhost";
        String port = "-port";
        String portNumber = "12345";

        // Format Dates
        PrettyPrinter pp = new PrettyPrinter();
        PhoneCall call = new PhoneCall();

        String customer = "Dave";
        String caller = "503-245-2345";
        String callee = "765-389-1273";

        // PHONE CALL #1
        // java -jar target/phonebill.jar
        // -host localhost -port 12345 "Dave" 503-245-2345 765-389-1273 03/02/2018 8:56 am 03/02/2018 10:27 am
        String startDate1 = "03/02/2018";
        String startTime1 = "8:56";
        String startAmPm1 = "am";
        String endDate1 = "03/02/2018";
        String endTime1 = "10:27";
        String endAmPm1 = "am";

        String start1 = pp.PrettyDate(startDate1 + " " + startTime1 + " " + startAmPm1);
        String end1   = pp.PrettyDate(endDate1   + " " + endTime1   + " " + endAmPm1);
        Date startD1 = call.parseDate(start1); // Set start date
        Date endD1  = call.parseDate(end1); // Set end date
        long duration1  = endD1.getTime() - startD1.getTime();
        int diffInMinutes1 = (int) TimeUnit.MILLISECONDS.toMinutes(duration1);

        // PHONE CALL #2
        // java -jar target/phonebill.jar
        // -host localhost -port 12345 "Dave" 503-245-2345 765-389-1273 03/14/2018 8:56 am 03/14/2018 10:27 am
        String startDate2 = "03/14/2018";
        String startTime2 = "10:56";
        String startAmPm2 = "am";
        String endDate2 = "03/14/2018";
        String endTime2 = "11:27";
        String endAmPm2 = "am";

        String start2 = pp.PrettyDate(startDate2 + " " + startTime2 + " " + startAmPm2);
        String end2   = pp.PrettyDate(endDate2   + " " + endTime2   + " " + endAmPm2);
        Date startD2 = call.parseDate(start2); // Set start date
        Date endD2  = call.parseDate(end2); // Set end date
        long duration2  = endD2.getTime() - startD2.getTime();
        int diffInMinutes2 = (int) TimeUnit.MILLISECONDS.toMinutes(duration2);

        // PHONE CALL #3
        // java -jar target/phonebill.jar
        // -host localhost -port 12345 "Dave" 503-245-2345 765-389-1273 03/16/2018 8:56 am 03/16/2018 10:27 am
        String startDate3 = "03/16/2018";
        String startTime3 = "1:15";
        String startAmPm3 = "pm";
        String endDate3 = "03/16/2018";
        String endTime3 = "2:05";
        String endAmPm3 = "pm";

        String start3 = pp.PrettyDate(startDate3 + " " + startTime3 + " " + startAmPm3);
        String end3   = pp.PrettyDate(endDate3   + " " + endTime3   + " " + endAmPm3);
        Date startD3 = call.parseDate(start3); // Set start date
        Date endD3  = call.parseDate(end3); // Set end date
        long duration3  = endD3.getTime() - startD3.getTime();
        int diffInMinutes3 = (int) TimeUnit.MILLISECONDS.toMinutes(duration3);

        // Create expected Pretty Print Output
        long expectetdTotalMinutes = diffInMinutes1 + diffInMinutes2 + diffInMinutes3;
        String expectedPrettyPrint = "Customer: " + customer + "\n"
                + "Total Minutes: " + expectetdTotalMinutes + "\n"
                + "\n"
                + "    Caller         Callee      Minutes      Call Start            Call End\n"
                + "---------------------------------------------------------------------------------\n"
                + " " + caller + "   " + callee  + "     "  + diffInMinutes1 + "   " + start1 + "   " + end1 + "\n"
                + " " + caller + "   " + callee  + "     "  + diffInMinutes2 + "   " + start2 + "   " + end2 + "\n"
                + " " + caller + "   " + callee  + "     "  + diffInMinutes3 + "   " + start3 + "   " + end3 + "\n";

        // Set Expectations
        MainMethodResult result = null;

        result = invokeMain
        (
            Project4.class,
            host, hostname,
            port, portNumber,
            customer, caller, callee,
            startDate1, startTime1, startAmPm1,
            endDate1, endTime1, endAmPm1
        );

        result = invokeMain
        (
            Project4.class,
            host, hostname,
            port, portNumber,
            customer, caller, callee,
            startDate2, startTime2, startAmPm2,
            endDate2, endTime2, endAmPm2
        );

        result = invokeMain
        (
            Project4.class, HOSTNAME, PORT,
            host, hostname,
            port, portNumber,
            customer, caller, callee,
            startDate3, startTime3, startAmPm3,
            endDate3, endTime3, endAmPm3
        );

        return expectedPrettyPrint;
    }

    @Test
    public void testAddPhoneCall() throws PhoneBillException, ParseException
    {
        // java -jar target/phonebill.jar
        // -host localhost -port 12345 "Dave" 503-245-2345 765-389-1273 02/27/2018 8:56 am 02/27/2018 10:27 am

        String host = "-host";
        String hostname = "localhost";
        String port = "-port";
        String portNumber = "12345";

        String customer = "Dave";
        String caller = "503-245-2345";
        String callee = "765-389-1273";
        String startDate = "02/27/2018";
        String startTime = "8:56";
        String startAmPm = "am";
        String endDate = "02/27/2018";
        String endTime = "10:27";
        String endAmPm = "am";

        // Format Dates
        PrettyPrinter pp = new PrettyPrinter();
        PhoneCall call = new PhoneCall();
        String start = pp.PrettyDate(startDate + " " + startTime + " " + startAmPm);
        String end   = pp.PrettyDate(endDate   + " " + endTime   + " " + endAmPm);
        Date startD = call.parseDate(start); // Set start date
        Date endD   = call.parseDate(end); // Set end date
        long duration  = endD.getTime() - startD.getTime();
        int diffInMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(duration);

        // Create expected Pretty Print Output
        int expectetdTotalMinutes = diffInMinutes;
        String expectedPrettyPrint = "Customer: " + customer + "\n"
                + "Total Minutes: " + expectetdTotalMinutes + "\n"
                + "\n"
                + "    Caller         Callee      Minutes      Call Start            Call End\n"
                + "---------------------------------------------------------------------------------\n"
                + " " + caller + "   " + callee  + "      "  + diffInMinutes + "   " + start + "   " + end + "\n";

        // Set Expectations
        MainMethodResult result = null;
        Integer expectedExitCode = 0;
        String expectedStdOut = "StdOut: " + expectedPrettyPrint;
        String expectedStdErr = "StdErr: " + "";

        try
        {
            result = invokeMain
            (
                Project4.class,
                //HOSTNAME, PORT,
                host, hostname,
                port, portNumber,
                customer, caller, callee,
                startDate, startTime, startAmPm,
                endDate, endTime, endAmPm
            );
        }
        finally
        {
            Integer actualExitCode = result.getExitCode();
            String actualStdOut = "StdOut: " + result.getTextWrittenToStandardOut();
            String actualStdErr = "StdErr: " + result.getTextWrittenToStandardError();

            assertEquals(expectedExitCode, actualExitCode);
            assertEquals(expectedStdErr, actualStdErr);
            assertEquals(expectedStdOut, actualStdOut);
        }
    }

    @Test
    public void testSearch() throws PhoneBillException, ParseException
    {
        String option = "-search";
        String customer = "Dave";
        String startDate = "03/01/2018";
        String startTime = "12:00";
        String startAmPm = "am";
        String endDate = "03/31/2018";
        String endTime = "11:59";
        String endAmPm = "pm";

        // Format Dates
        PrettyPrinter pp = new PrettyPrinter();
        PhoneCall call = new PhoneCall();
        String start = pp.PrettyDate(startDate + " " + startTime + " " + startAmPm);
        String end   = pp.PrettyDate(endDate   + " " + endTime   + " " + endAmPm);
        Date startD = call.parseDate(start); // Set start date
        Date endD   = call.parseDate(end); // Set end date
        long duration  = endD.getTime() - startD.getTime();
        int diffInMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(duration);

        // Create expected Pretty Print Output
        String expectedPrettyPrint = Add3Calls();

        // Set Expectations
        MainMethodResult result = null;
        Integer expectedExitCode = 0;
        String expectedStdOut = "StdOut: " + expectedPrettyPrint;
        String expectedStdErr = "StdErr: " + "";

        try
        {
            result = invokeMain
            (
                Project4.class, HOSTNAME, PORT,
                option, customer,
                startDate, startTime, startAmPm,
                endDate, endTime, endAmPm
            );
        }
        finally
        {
            Integer actualExitCode = result.getExitCode();
            String actualStdOut = "StdOut: " + result.getTextWrittenToStandardOut();
            String actualStdErr = "StdErr: " + result.getTextWrittenToStandardError();

            assertThat(actualExitCode, CoreMatchers.equalTo(expectedExitCode));
            assertThat(actualStdOut, CoreMatchers.equalTo(expectedStdOut));
            assertThat(actualStdErr, CoreMatchers.equalTo(expectedStdErr));
        }
    }

    @Test
    public void testPrettyPrint() throws PhoneBillException, ParseException
    {
        String customer = "Dave";

        // Create expected Pretty Print Output
        String expectedPrettyPrint = Add3Calls();

        // Set Expectations
        MainMethodResult result = null;
        Integer expectedExitCode = 0;
        String expectedStdOut = "StdOut: " + expectedPrettyPrint;
        String expectedStdErr = "StdErr: " + "";

        try
        {
            result = invokeMain
            (
                Project4.class, HOSTNAME, PORT,
                customer
            );
        }
        finally
        {
            Integer actualExitCode = result.getExitCode();
            String actualStdOut = "StdOut: " + result.getTextWrittenToStandardOut();
            String actualStdErr = "StdErr: " + result.getTextWrittenToStandardError();

            assertEquals(expectedExitCode, actualExitCode);
            assertEquals(expectedStdErr, actualStdErr);
            assertEquals(expectedStdOut, actualStdOut);
        }
    }
}