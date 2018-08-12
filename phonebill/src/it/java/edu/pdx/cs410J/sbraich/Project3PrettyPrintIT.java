package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.ParserException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests the functionality in the {@link Project3} main class.
 */
public class Project3PrettyPrintIT extends InvokeMainTestCase
{

    /**
     * Invokes the main method of {@link Project3} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args)
    {
        return invokeMain( Project3.class, args );
    }

    @Test
    public void TestMain_PrettyPrintOption()
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
                startDate, startTime, startAMPM, endDate, endTime, endAMPM);

        assertThat(result.getExitCode(), equalTo(0));

        String stdout = "Customer: " + customer + "\n"
                      + "Total Minutes: 15980\n"
                      + "\n"
                      + "    Caller         Callee      Minutes      Call Start            Call End\n"
                      + "---------------------------------------------------------------------------------\n"
                      + " 123-123-1234   111-111-1111       5   01/15/2018 07:30 AM   01/15/2018 07:35 AM\n";

        assertThat(result.getTextWrittenToStandardOut(), equalTo(stdout));
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