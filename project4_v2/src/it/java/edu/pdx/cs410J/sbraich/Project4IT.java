package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNot.not;

/**
 * Tests the {@link Project4} class by invoking its main method with various arguments
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Project4IT extends InvokeMainTestCase
{
    private static final String HOSTNAME = "localhost";
    private static final String PORT = System.getProperty("http.port", "8080");

    @Test
    public void testNoCommandLineArguments()
    {
        MainMethodResult result = invokeMain(Project4.class);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing"));
    }

    @Test
    public void testMissingSomeCommandLineArgs() throws IOException
    {
        PhoneBillRestClient client = new PhoneBillRestClient(HOSTNAME, Integer.parseInt(PORT));
        client.removeAllPhoneBills();

        MainMethodResult result = invokeMain(Project4.class, "-host", HOSTNAME, "-port", PORT);
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getTextWrittenToStandardError(), containsString("Missing"));
    }

    @Test
    public void testREADME()
    {
        MainMethodResult result = invokeMain(Project4.class, "-host", HOSTNAME, "-port", PORT, "-README");
        assertThat(result.getTextWrittenToStandardError(), result.getExitCode(), equalTo(0));
        assertThat(result.getTextWrittenToStandardOut(), equalTo(Project4.README + Project4.USAGE + "\n"));
    }

    @Test
    public void testNonExistantCustomer() throws IOException
    {
        PhoneBillRestClient client = new PhoneBillRestClient(HOSTNAME, Integer.parseInt(PORT));
        client.removeAllPhoneBills();

        MainMethodResult result = invokeMain(Project4.class, "-host", HOSTNAME, "-port", PORT, "CustomerZZZ");
        assertThat(result.getTextWrittenToStandardError(), result.getExitCode(), equalTo(0));
        String out = result.getTextWrittenToStandardOut();
        assertThat(out, out, containsString("Not Found"));
    }

    @Test
    public void testAddOnePhoneCall() throws IOException
    {
        PhoneBillRestClient client = new PhoneBillRestClient(HOSTNAME, Integer.parseInt(PORT));
        client.removeAllPhoneBills();

        String customer = "Customer";
        String callerNumber = "123-456-7890";
        String calleeNumber = "234-567-8901";
        String startDate = "9/20/2018";
        String startTime = "7:15";
        String startAmPm = "AM";
        String endDate = "9/20/2018";
        String endTime = "7:30";
        String endtAmPm = "AM";

        MainMethodResult resultAdd = invokeMain
        (
            Project4.class,"-host", HOSTNAME, "-port", PORT,
            customer, callerNumber, calleeNumber,
            startDate, startTime, startAmPm,
            endDate, endTime, endtAmPm
        );
        int exitCodeAdd = resultAdd.getExitCode();
        assertThat(exitCodeAdd, equalTo(0));

        MainMethodResult resultCheck = invokeMain(Project4.class, "-host", HOSTNAME, "-port", PORT, customer);
        int exitCodeCheck = resultCheck.getExitCode();
        assertThat(resultCheck.getTextWrittenToStandardError(), exitCodeCheck, equalTo(0));

        String out = resultCheck.getTextWrittenToStandardOut();

        assertThat(out, containsString(customer));
        assertThat(out, containsString(callerNumber));
        assertThat(out, containsString(calleeNumber));
        assertThat(out, containsString(startDate + " " + startTime + " " + startAmPm));
        assertThat(out, containsString(endDate + " " + endTime + " " + endtAmPm));
    }

    @Test
    public void testGetAllForCustomer() throws IOException
    {
        PhoneBillRestClient client = new PhoneBillRestClient(HOSTNAME, Integer.parseInt(PORT));
        client.removeAllPhoneBills();

        String customer = "Customer";
        String callerNumber = "123-456-7890";
        String calleeNumber = "234-567-8901";
        String startDate = "9/20/2018";
        String startTime = "7:15";
        String startAmPm = "AM";
        String endDate = "9/20/2018";
        String endTime = "7:30";
        String endtAmPm = "AM";

        MainMethodResult resultAdd = invokeMain
        (
                Project4.class,"-host", HOSTNAME, "-port", PORT,
                customer, callerNumber, calleeNumber,
                startDate, startTime, startAmPm,
                endDate, endTime, endtAmPm
        );
        int exitCodeAdd = resultAdd.getExitCode();
        assertThat(exitCodeAdd, equalTo(0));

        MainMethodResult resultCheck = invokeMain(Project4.class, "-host", HOSTNAME, "-port", PORT, customer);
        int exitCodeCheck = resultCheck.getExitCode();
        assertThat(resultCheck.getTextWrittenToStandardError(), exitCodeCheck, equalTo(0));

        String out = resultCheck.getTextWrittenToStandardOut();

        assertThat(out, containsString(customer));
        assertThat(out, containsString(callerNumber));
        assertThat(out, containsString(calleeNumber));
        assertThat(out, containsString(startDate + " " + startTime + " " + startAmPm));
        assertThat(out, containsString(endDate + " " + endTime + " " + endtAmPm));
    }

    @Test
    public void testSearch() throws IOException
    {
        String startSearchDate = "9/19/2018";
        String startSearchTime = "12:00";
        String startSearchAmPm = "AM";

        String endSearchDate = "9/23/2018";
        String endSearchTime = "11:59";
        String endSearchAmPm = "PM";

        String customer = "Customer";

        PhoneBillRestClient client = new PhoneBillRestClient(HOSTNAME, Integer.parseInt(PORT));
        client.removeAllPhoneBills();

        String callerNumber1 = "111-111-1111";
        String calleeNumber1 = "101-101-1010";
        String startDate1 = "9/20/2018";
        String startTime1 = "7:15";
        String startAmPm1 = "AM";
        String endDate1 = "9/20/2018";
        String endTime1 = "7:30";
        String endtAmPm1 = "AM";

        MainMethodResult resultAdd1 = invokeMain
        (
            Project4.class,"-host", HOSTNAME, "-port", PORT,
            customer, callerNumber1, calleeNumber1,
            startDate1, startTime1, startAmPm1,
            endDate1, endTime1, endtAmPm1
        );
        int exitCodeAdd1 = resultAdd1.getExitCode();
        assertThat(exitCodeAdd1, equalTo(0));

        String callerNumber2 = "222-222-2222";
        String calleeNumber2 = "202-202-2222";
        String startDate2 = "9/21/2018";
        String startTime2 = "7:15";
        String startAmPm2 = "AM";
        String endDate2 = "9/21/2018";
        String endTime2 = "7:30";
        String endtAmPm2 = "AM";

        MainMethodResult resultAdd2 = invokeMain
        (
            Project4.class,"-host", HOSTNAME, "-port", PORT,
            customer, callerNumber2, calleeNumber2,
            startDate2, startTime2, startAmPm2,
            endDate2, endTime2, endtAmPm2
        );
        int exitCodeAdd2 = resultAdd2.getExitCode();
        assertThat(exitCodeAdd2, equalTo(0));

        String callerNumber3 = "333-333-3333";
        String calleeNumber3 = "303-303-3030";
        String startDate3 = "9/25/2018";
        String startTime3 = "7:15";
        String startAmPm3 = "AM";
        String endDate3 = "9/25/2018";
        String endTime3 = "7:30";
        String endtAmPm3 = "AM";

        MainMethodResult resultAdd3 = invokeMain
        (
            Project4.class,"-host", HOSTNAME, "-port", PORT,
            customer, callerNumber3, calleeNumber3,
            startDate3, startTime3, startAmPm3,
            endDate3, endTime3, endtAmPm3
        );
        int exitCodeAdd3 = resultAdd3.getExitCode();
        assertThat(exitCodeAdd3, equalTo(0));


        MainMethodResult resultSearch = invokeMain
        (
            Project4.class, "-host", HOSTNAME, "-port", PORT, "-search",
            customer,
            startSearchDate, startSearchTime, startSearchAmPm,
            endSearchDate, endSearchTime, endSearchAmPm
        );

        int exitCodeCheck = resultSearch.getExitCode();
        assertThat(resultSearch.getTextWrittenToStandardError(), exitCodeCheck, equalTo(0));

        String out = resultSearch.getTextWrittenToStandardOut();

        assertThat(out, containsString(customer));
        assertThat(out, containsString(callerNumber1));
        assertThat(out, containsString(calleeNumber1));
        assertThat(out, containsString(startDate1 + " " + startTime1 + " " + startAmPm1));
        assertThat(out, containsString(endDate1 + " " + endTime1 + " " + endtAmPm1));

        assertThat(out, containsString(callerNumber2));
        assertThat(out, containsString(calleeNumber2));
        assertThat(out, containsString(startDate2 + " " + startTime2 + " " + startAmPm2));
        assertThat(out, containsString(endDate2 + " " + endTime2 + " " + endtAmPm2));

        assertThat(out, not(containsString(callerNumber3)));
        assertThat(out, not(containsString(calleeNumber3)));
        assertThat(out, not(containsString(startDate3 + " " + startTime3 + " " + startAmPm3)));
        assertThat(out, not(containsString(endDate3 + " " + endTime3 + " " + endtAmPm3)));
    }


    @Test
    public void testDeleteAllPhoneBills() throws IOException
    {
        PhoneBillRestClient client = new PhoneBillRestClient(HOSTNAME, Integer.parseInt(PORT));
        client.removeAllPhoneBills();

        String customer = "Customer";
        String callerNumber = "123-456-7890";
        String calleeNumber = "234-567-8901";
        String startDate = "9/20/2018";
        String startTime = "7:15";
        String startAmPm = "AM";
        String endDate = "9/20/2018";
        String endTime = "7:30";
        String endtAmPm = "AM";

        MainMethodResult resultAdd = invokeMain
        (
            Project4.class,"-host", HOSTNAME, "-port", PORT,
            customer, callerNumber, calleeNumber,
            startDate, startTime, startAmPm,
            endDate, endTime, endtAmPm
        );
        int exitCodeAdd = resultAdd.getExitCode();
        assertThat(exitCodeAdd, equalTo(0));

        client.removeAllPhoneBills();

        MainMethodResult resultCheck = invokeMain(Project4.class, "-host", HOSTNAME, "-port", PORT, customer);
        int exitCodeCheck = resultCheck.getExitCode();
        assertThat(resultCheck.getTextWrittenToStandardError(), exitCodeCheck, equalTo(0));

        String out = resultCheck.getTextWrittenToStandardOut();

        assertThat(out, not(containsString(customer)));
        assertThat(out, not(containsString(callerNumber)));
        assertThat(out, not(containsString(calleeNumber)));
        assertThat(out, not(containsString(startDate + " " + startTime + " " + startAmPm)));
        assertThat(out, not(containsString(endDate + " " + endTime + " " + endtAmPm)));
    }

}