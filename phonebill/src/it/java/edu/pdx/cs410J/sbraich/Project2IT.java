package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.ParserException;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Tests the functionality in the {@link Project2} main class.
 */
public class Project2IT extends InvokeMainTestCase
{

    /**
     * Invokes the main method of {@link Project2} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args)
    {
        return invokeMain( Project2.class, args );
    }

    /**
     * Tests that invoking the main method with no arguments issues an error
     */
    @Test  //(expected = PhoneBillException.class)
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
        assertThat(result.getTextWrittenToStandardOut(), equalTo(Project2.README + "\n"));
        assertThat(result.getTextWrittenToStandardError(), equalTo(""));
    }

    @Test
    public void TestMain_PrintOption()
    {
        String caller = "123-456-7890";
        String callee = "234-567-8901";
        String startDate = "07/04/2018";
        String startTime = "6:24";
        String endDate = "07/04/2018";
        String endTime = "6:48";

        MainMethodResult result =
                invokeMain("-print", "My Customer", caller, callee, startDate, startTime, endDate, endTime);

        assertThat(result.getExitCode(), equalTo(0));
        String phoneCallToString = String.format("Phone call from %s to %s from %s %s to %s %s",
                caller, callee, startDate, startTime, endDate, endTime);
        assertThat(result.getTextWrittenToStandardOut(), equalTo(phoneCallToString + "\n"));
    }

    @Test
    public void TestMain_ParserNoFile_Dumper() throws IOException
    {
        String customer = "My Customer";
        String filePath = "main_parsedump.txt";

        String caller = "123-456-7890";
        String callee = "234-567-8901";
        String startDate = "07/04/2018";
        String startTime = "6:24";
        String endDate = "07/04/2018";
        String endTime = "6:48";

        String phoneCallToString = String.format("Phone call from %s to %s from %s %s to %s %s", caller, callee, startDate, startTime, endDate, endTime);
        String expectedCallFromFile = "[" + phoneCallToString + "]";
        File f = new File(filePath);

        try
        {
            MainMethodResult result = invokeMain("-print", "-textFile", filePath, customer, caller, callee, startDate, startTime, endDate, endTime);
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

    @Test(expected = IllegalArgumentException.class)
    public void TestMain_ParserDumper_NameDifferent() throws IOException, ParserException
    {
        String customer1 = "My Customer 1";
        String customer2 = "My Customer 2";
        String filePath = "diff_customer_names.txt";

        String caller = "123-456-7890";
        String callee = "234-567-8901";
        String startDate = "07/04/2018";
        String startTime = "6:24";
        String endDate = "07/04/2018";
        String endTime = "6:48";

        String phoneCallToString = String.format("Phone call from %s to %s from %s %s to %s %s", caller, callee, startDate, startTime, endDate, endTime);
        String expectedCallFromFile = "[" + phoneCallToString + "]";
        File f = new File(filePath);

        try
        {
            // Setup first textFile call with "My Customer 1"
            MainMethodResult result1 = invokeMain("-textFile", filePath, customer1, caller, callee, startDate, startTime, endDate, endTime);
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
            MainMethodResult result2 = invokeMain("-textFile", filePath, customer2, caller, callee, startDate, startTime, endDate, endTime);
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
        String endDate = "07/04/2018";
        String endTime = "6:48";

        MainMethodResult result =
                invokeMain("My Customer", caller, callee, startDate, startTime, endDate, endTime);

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
        String endDate = "07/04/2018";
        String endTime = "6:48";

        MainMethodResult result =
                invokeMain("My Customer", caller, callee, startDate, startTime, endDate, endTime, "blah");

        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(""));
        assertThat(result.getTextWrittenToStandardError(), equalTo("Too many command line arguments\n"));
    }

}