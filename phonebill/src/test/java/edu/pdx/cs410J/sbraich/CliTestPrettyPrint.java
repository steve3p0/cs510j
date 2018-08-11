package edu.pdx.cs410J.sbraich;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
        assertEquals("1/15/2018 9:16 am", cli.startTime);
        assertEquals("12/1/2018 10:16 pm", cli.endTime);

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
        assertEquals("1/15/2018 9:16 AM", cli.startTime);
        assertEquals("12/1/2018 10:16 PM", cli.endTime);

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

        assertEquals(false, cli.textFile);
        assertEquals(false, cli.print);
        assertEquals(false, cli.readme);
    }

    ////////////////////////////////////////////////////////////////////////////////////

    // All the other CLI tests with Pretty Print Sprinkled In: TODO

    /// Positive Tests ///////////////////////////////////////////////////////////////
    @Test
    public void CliTest_Basic() throws Exception
    {
        fail("Not tested");
        String[] args = {"customer", "503-123-1234", "503-123-1234",
                "1/15/2018", "9:16", "AM", "12/1/2018", "10:16", "PM"};
        Cli cli = new Cli(args);

        assertEquals("customer", cli.customer);
        assertEquals("503-123-1234", cli.callerNumber);
        assertEquals("503-123-1234", cli.calleeNumber);
        assertEquals("1/15/2018 9:16 AM", cli.startTime);
        assertEquals("12/1/2018 10:16 PM", cli.endTime);

        assertEquals(false, cli.textFile);
        assertEquals(false, cli.print);
        assertEquals(false, cli.readme);
    }

    /// Edge Cases
    @Test(expected = PhoneBillException.class)
    public void CliTest_TooMany() throws Exception
    {
        fail("Not tested");
        String[] args = {"customer", "503-123-1234", "503-123-1234", "01/02/2018", "9:16", "AM", "12/1/2018", "11:16", "PM", "EXTRA"};
        Cli cli = new Cli(args);
    }

    @Test(expected = PhoneBillException.class)
    public void CliTest_TooFew1() throws Exception
    {
        fail("Not tested");
        String[] args = {"Project3"};
        Cli cli = new Cli(args);
    }

    @Test(expected = PhoneBillException.class)
    public void CliTest_TooFew2() throws Exception
    {
        fail("Not tested");
        String[] args = {"customer"};
        Cli cli = new Cli(args);
    }

    @Test(expected = PhoneBillException.class)
    public void CliTest_TooFew7() throws Exception
    {
        fail("Not tested");
        String[] args = {"customer", "503-123-1234", "503-123-1234", "1/15/2018", "9:16", "pm", "12/1/2018"};
        Cli cli = new Cli(args);
    }

    // Print Tests

    @Test
    public void CliTest_PrintFirst() throws Exception
    {
        fail("Not tested");
        String[] args = {"-print", "customer", "503-123-1234", "503-123-1234", "1/15/2018", "9:16", "AM", "12/1/2018", "12:16", "PM"};
        Cli cli = new Cli(args);

        assertEquals("customer", cli.customer);
        assertEquals("503-123-1234", cli.callerNumber);
        assertEquals("503-123-1234", cli.calleeNumber);
        assertEquals("1/15/2018 9:16 AM", cli.startTime);
        assertEquals("12/1/2018 12:16 PM", cli.endTime);

        assertEquals(false, cli.textFile);
        assertEquals(true, cli.print);
        assertEquals(false, cli.readme);
    }

    @Test
    public void CliTest_PrintLast() throws Exception
    {
        fail("Not tested");
        String[] args = {"customer", "503-123-1234", "503-123-1234", "1/15/2018", "9:16", "pm", "12/1/2018", "1:16", "am", "-print"};
        Cli cli = new Cli(args);

        assertEquals("customer", cli.customer);
        assertEquals("503-123-1234", cli.callerNumber);
        assertEquals("503-123-1234", cli.calleeNumber);
        assertEquals("1/15/2018 9:16 AM", cli.startTime);
        assertEquals("12/1/2018 1:16 PM", cli.endTime);

        assertEquals(false, cli.textFile);
        assertEquals(true, cli.print);
        assertEquals(false, cli.readme);
    }

    @Test
    public void CliTest_PrintMiddle() throws Exception
    {
        fail("Not tested");
        String[] args = {"customer", "503-123-1234", "503-123-1234", "-print", "01/15/2018", "07:16", "pm", "12/1/2018", "09:16", "pm"};
        Cli cli = new Cli(args);

        assertEquals("customer", cli.customer);
        assertEquals("503-123-1234", cli.callerNumber);
        assertEquals("503-123-1234", cli.calleeNumber);
        assertEquals("1/15/2018 7:16 PM", cli.startTime);
        assertEquals("12/1/2018 9:16 PM", cli.endTime);

        assertEquals(false, cli.textFile);
        assertEquals(true, cli.print);
        assertEquals(false, cli.readme);
    }

    // ReadMe Tests ///////////////////////////////////////////////////////////////

    @Test
    public void CliTest_ReadMeOnly() throws Exception
    {
        fail("Not tested");

        String[] args = {"-README"};
        Cli cli = new Cli(args);

        assertEquals(false, cli.textFile);
        assertEquals(false, cli.print);
        assertEquals(true, cli.readme);
    }

    @Test(expected = PhoneBillException.class)
    public void CliTest_ReadMeFirst() throws Exception
    {
        fail("Not tested");

        String[] args = {"-README", "customer", "503-123-1234", "503-123-1234",
                "01/15/2018", "9:16", "pm", "12/1/2018", "03:16", "pm", "-print"};
        Cli cli = new Cli(args);
    }

    @Test(expected = PhoneBillException.class)
    public void CliTest_ReadMeLast() throws Exception
    {
        fail("Not tested");

        String[] args = {"customer", "503-123-1234", "503-123-1234",
                "1/15/2018", "09:16", "AM", "12/1/2018", "9:16", "pm", "-print", "-README"};
        Cli cli = new Cli(args);
    }

    @Test(expected = PhoneBillException.class)
    public void CliTest_ReadMeMiddle() throws Exception
    {
        fail("Not tested");

        String[] args = {"customer", "503-123-1234", "503-123-1234", "-README",
                "1/15/2018", "9:16", "pm", "12/1/2018", "9:16", "pm", "-print"};
        Cli cli = new Cli(args);
    }

    // Text File Tests ///////////////////////////////////////////////////////////////
    @Test
    public void CliTest_TextFile_First() throws Exception
    {
        fail("Not tested");

        String[] args = {"-textFile", "text.txt", "customer", "503-123-1234", "503-123-1234",
                "1/15/2018", "9:16", "pm", "12/1/2018", "9:16", "pm"};
        Cli cli = new Cli(args);

        assertEquals("customer", cli.customer);
        assertEquals("503-123-1234", cli.callerNumber);
        assertEquals("503-123-1234", cli.calleeNumber);
        assertEquals("1/15/2018 9:16 PM", cli.startTime);
        assertEquals("12/1/2018 9:16 PM", cli.endTime);

        assertEquals(false, cli.print);
        assertEquals(false, cli.readme);

        assertEquals(true, cli.textFile);
        assertEquals("text.txt", cli.filePath.toString());
    }

    @Test
    public void CliTest_TextFile_Last() throws Exception
    {
        fail("Not tested");

        String[] args = {"customer", "503-123-1234", "503-123-1234",
                "1/15/2018", "9:39", "AM", "12/1/2018", "9:39", "PM", "-textFile", "text.txt"};
        Cli cli = new Cli(args);

        assertEquals("customer", cli.customer);
        assertEquals("503-123-1234", cli.callerNumber);
        assertEquals("503-123-1234", cli.calleeNumber);
        assertEquals("1/15/2018 9:39 AM", cli.startTime);
        assertEquals("12/1/2018 9:39 PM", cli.endTime);

        assertEquals(false, cli.print);
        assertEquals(false, cli.readme);

        assertEquals(true, cli.textFile);
        assertEquals("text.txt", cli.filePath.toString());
    }

    @Test
    public void CliTest_TextFile_Middle() throws Exception
    {
        fail("Not tested");

        String[] args = {"customer", "503-123-1234", "503-123-1234", "-textFile", "text.txt",
                "1/15/2018", "09:39", "am", "12/1/2018", "09:39", "pm"};
        Cli cli = new Cli(args);

        assertEquals("customer", cli.customer);
        assertEquals("503-123-1234", cli.callerNumber);
        assertEquals("503-123-1234", cli.calleeNumber);
        assertEquals("1/15/2018 9:39 AM", cli.startTime);
        assertEquals("12/1/2018 9:39 PM", cli.endTime);

        assertEquals(false, cli.print);
        assertEquals(false, cli.readme);

        assertEquals(true, cli.textFile);
        assertEquals("text.txt", cli.filePath.toString());
    }

    @Test(expected = PhoneBillException.class)
    public void CliTest_TextFileWithAllArgs() throws Exception
    {
        fail("Not tested");

        String[] args = {"-textFile", "text.txt", "customer", "503-123-1234", "503-123-1234",
                "1/15/2018", "11:16", "am", "12/1/2018", "9:16", "pm", "-print", "-README"};
        Cli cli = new Cli(args);
    }

    @Test(expected = PhoneBillException.class)
    public void CliTest_TextFile_NoPath() throws Exception
    {
        fail("Not tested");

        String[] args = {"customer", "503-123-1234", "503-123-1234", "1/15/2018",
                "3:16", "am", "12/1/2018", "12:16", "pm", "-textFile"};
        Cli cli = new Cli(args);

        fail("Not tested yet");
    }


}

