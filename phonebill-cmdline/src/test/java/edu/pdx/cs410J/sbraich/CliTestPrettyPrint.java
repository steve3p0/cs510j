package edu.pdx.cs410J.sbraich;

import org.junit.Test;

import static org.junit.Assert.*;

public class CliTestPrettyPrint
{
    // Tear Down:
    // Create the 'EXISTING' FILE
//    File dir = new File(Paths.get(filePath).getParent().toString());
//        if (!dir.exists())
//    {
//        Files.createDirectories(Paths.get(filePath).getParent());
//    }
//        Files.write(Paths.get(filePath), setupLines, Charset.forName("UTF-8"));
//
//    // Create the file and the Result Object
//    File f = new File(filePath);
//    MainMethodResult result = null;
//
//        try
//    {
//        result = invokeMain(fileOption, filePath, customer, caller, callee, startDate, startTime, endDate, endTime);
//    }
//        finally
//    {
//        Integer actualExitCode = result.getExitCode();
//        String actualStdOut = "StdOut: " + result.getTextWrittenToStandardOut();
//        String actualStdErr = "StdErr: " + result.getTextWrittenToStandardError();
//
//        assertThat(actualExitCode, equalTo(expectedExitCode));
//        assertThat(actualStdOut, equalTo(expectedStdOut));
//        assertThat(actualStdErr, equalTo(expectedStdErr));
//
//        f.delete();
//    }

    /// PRETTY PRINT ///////////////////////////////////////////////////////////////
    @Test
    public void CliTest_PrettyPrintStdOut_1() throws Exception
    {
        String[] args = {"-pretty", "-",
                         "customer", "503-123-1234", "503-123-1234",
                         "1/15/2018", "9:39", "AM", "12/1/2018", "9:39", "PM"};
        Cli cli = new Cli(args);

        //TODO:  PRETTY
        //

        assertEquals("customer", cli.customer);
        assertEquals("503-123-1234", cli.callerNumber);
        assertEquals("503-123-1234", cli.calleeNumber);
        assertEquals("1/15/2018 9:39 AM", cli.startTime);
        assertEquals("12/1/2018 9:39 PM", cli.endTime);

        assertEquals(false, cli.textFile);
        assertEquals(false, cli.print);
        assertEquals(false, cli.readme);
    }

    @Test
    public void CliTest_PrettyPrintFile_1() throws Exception
    {
        String[] args = {"-pretty", "pretty.txt", "customer", "503-123-1234", "503-123-1234",
                         "1/15/2018", "1:39", "AM", "12/1/2018", "11:39", "PM"};
        Cli cli = new Cli(args);

        //TODO:  PRETTY
        //
        assertEquals("customer", cli.customer);
        assertEquals("503-123-1234", cli.callerNumber);
        assertEquals("503-123-1234", cli.calleeNumber);
        assertEquals("1/15/2018 1:39 AM", cli.startTime);
        assertEquals("12/1/2018 11:39 PM", cli.endTime);
        assertEquals("12/1/2018 11:39 PM", cli.endTime);
        assertEquals("pretty.txt", cli.prettyPath.toString());
        assertNull(cli.filePath);

        assertEquals(true, cli.pretty);
        assertEquals(false, cli.prettyStdout);
        assertEquals(true, cli.prettyFile);

        assertEquals(false, cli.textFile);
        assertEquals(false, cli.print);
        assertEquals(false, cli.readme);
    }

    @Test
    public void CliTest_PrettyPrintStdOut_End() throws Exception
    {
        String[] args = {"customer", "503-123-1234", "503-123-1234",
                         "1/15/2018", "9:39", "am", "12/1/2018", "9:39", "pm",
                         "-pretty", "-"};
        Cli cli = new Cli(args);

        assertEquals("customer", cli.customer);
        assertEquals("503-123-1234", cli.callerNumber);
        assertEquals("503-123-1234", cli.calleeNumber);
        assertEquals("1/15/2018 9:39 am", cli.startTime);
        assertEquals("12/1/2018 9:39 pm", cli.endTime);
        assertNull(cli.prettyPath);
        assertNull(cli.filePath);

        assertEquals(true, cli.pretty);
        assertEquals(true, cli.prettyStdout);
        assertEquals(false, cli.prettyFile);

        assertEquals(false, cli.textFile);
        assertEquals(false, cli.print);
        assertEquals(false, cli.readme);
    }

    @Test
    public void CliTest_PrettyPrintFile_End() throws Exception
    {
        String[] args = {"customer", "503-123-1234", "503-123-1234",
                                     "1/15/2018", "1:39", "AM", "12/1/2018", "10:16", "PM",
                                     "-pretty", "pretty.txt"};
        Cli cli = new Cli(args);

        //TODO:  PRETTY
        //
        assertEquals("customer", cli.customer);
        assertEquals("503-123-1234", cli.callerNumber);
        assertEquals("503-123-1234", cli.calleeNumber);
        assertEquals("1/15/2018 1:39 AM", cli.startTime);
        assertEquals("12/1/2018 10:16 PM", cli.endTime);
        assertEquals("pretty.txt", cli.prettyPath.toString());
        assertNull(cli.filePath);

        assertEquals(true, cli.pretty);
        assertEquals(false, cli.prettyStdout);
        assertEquals(true, cli.prettyFile);

        assertEquals(false, cli.textFile);
        assertEquals(false, cli.print);
        assertEquals(false, cli.readme);
    }

    @Test
    public void CliTest_PrettyPrintStdOut_Middle() throws Exception
    {
        String[] args = {"customer", "503-123-1234", "503-123-1234",
                         "1/15/2018", "9:39", "am", "12/1/2018", "9:39", "pm",
                         "-pretty", "-"};
        Cli cli = new Cli(args);

        assertEquals("customer", cli.customer);
        assertEquals("503-123-1234", cli.callerNumber);
        assertEquals("503-123-1234", cli.calleeNumber);
        assertEquals("1/15/2018 9:39 am", cli.startTime);
        assertEquals("12/1/2018 9:39 pm", cli.endTime);
        assertNull(cli.prettyPath);
        assertNull(cli.filePath);

        assertEquals(true, cli.pretty);
        assertEquals(true, cli.prettyStdout);
        assertEquals(false, cli.prettyFile);

        assertEquals(false, cli.textFile);
        assertEquals(false, cli.print);
        assertEquals(false, cli.readme);
    }

    @Test
    public void CliTest_PrettyPrintFile_Middle() throws Exception
    {
        String[] args = {"customer", "503-123-1234", "503-123-1234",
                         "-pretty", "pretty.txt",
                         "1/15/2018", "1:39", "AM", "12/1/2018", "10:16", "PM"};
        Cli cli = new Cli(args);

        //TODO:  PRETTY
        //
        assertEquals("customer", cli.customer);
        assertEquals("503-123-1234", cli.callerNumber);
        assertEquals("503-123-1234", cli.calleeNumber);
        assertEquals("1/15/2018 1:39 AM", cli.startTime);
        assertEquals("12/1/2018 10:16 PM", cli.endTime);
        assertEquals("pretty.txt", cli.prettyPath.toString());
        assertNull(cli.filePath);

        assertEquals(true, cli.pretty);
        assertEquals(false, cli.prettyStdout);
        assertEquals(true, cli.prettyFile);

        assertEquals(false, cli.textFile);
        assertEquals(false, cli.print);
        assertEquals(false, cli.readme);
    }

    ////////////////////////////////////////////////////////////////////////////////////

    // All the other CLI tests with Pretty Print Sprinkled In: TODO

    /// Positive Tests ///////////////////////////////////////////////////////////////



}

