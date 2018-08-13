package edu.pdx.cs410J.sbraich;

import org.junit.Test;

import static org.junit.Assert.*;

public class CliTest
{
    /// Positive Tests ///////////////////////////////////////////////////////////////
    @Test
    public void CliTest_Basic() throws Exception
    {
        String[] args = {"customer", "503-123-1234", "503-123-1234",
                                     "1/15/2018", "9:16", "AM", "12/1/2018", "10:16", "PM"};
        Cli cli = new Cli(args);

        assertEquals("customer", cli.customer);
        assertEquals("503-123-1234", cli.callerNumber);
        assertEquals("503-123-1234", cli.calleeNumber);
        assertEquals("1/15/2018 9:16 AM", cli.startTime);
        assertEquals("12/1/2018 10:16 PM", cli.endTime);
        assertNull(cli.prettyPath);

        assertEquals(false, cli.pretty);
        assertEquals(false, cli.prettyStdout);
        assertEquals(false, cli.prettyFile);

        assertEquals(false, cli.textFile);
        assertEquals(false, cli.print);
        assertEquals(false, cli.readme);
    }

    /// Edge Cases
    @Test(expected = PhoneBillException.class)
    public void CliTest_TooMany() throws Exception
    {
        String[] args = {"customer", "503-123-1234", "503-123-1234", "01/02/2018", "9:16", "AM", "12/1/2018", "11:16", "PM", "EXTRA"};
        Cli cli = new Cli(args);
    }

    @Test(expected = PhoneBillException.class)
    public void CliTest_TooFew1() throws Exception
    {
        String[] args = {"Project3"};
        Cli cli = new Cli(args);
    }

    @Test(expected = PhoneBillException.class)
    public void CliTest_TooFew2() throws Exception
    {
        String[] args = {"customer"};
        Cli cli = new Cli(args);
    }

    @Test(expected = PhoneBillException.class)
    public void CliTest_TooFew7() throws Exception
    {
        String[] args = {"customer", "503-123-1234", "503-123-1234", "1/15/2018", "9:16", "pm", "12/1/2018"};
        Cli cli = new Cli(args);
    }

    // Print Tests

    @Test
    public void CliTest_PrintFirst() throws Exception
    {
        String[] args = {"-print", "customer", "503-123-1234", "503-123-1234", "1/15/2018", "9:16", "AM", "12/1/2018", "12:16", "PM"};
        Cli cli = new Cli(args);

        assertEquals("customer", cli.customer);
        assertEquals("503-123-1234", cli.callerNumber);
        assertEquals("503-123-1234", cli.calleeNumber);
        assertEquals("1/15/2018 9:16 AM", cli.startTime);
        assertEquals("12/1/2018 12:16 PM", cli.endTime);
        assertNull(cli.prettyPath);
        assertNull(cli.filePath);

        assertEquals(false, cli.pretty);
        assertEquals(false, cli.prettyStdout);
        assertEquals(false, cli.prettyFile);

        assertEquals(false, cli.textFile);
        assertEquals(true, cli.print);
        assertEquals(false, cli.readme);
    }
    
    @Test
    public void CliTest_PrintLast() throws Exception
    {
        String[] args = {"customer", "503-123-1234", "503-123-1234", "1/15/2018", "9:16", "pm", "12/1/2018", "1:16", "am", "-print"};
        Cli cli = new Cli(args);

        assertEquals("customer", cli.customer);
        assertEquals("503-123-1234", cli.callerNumber);
        assertEquals("503-123-1234", cli.calleeNumber);
        assertEquals("1/15/2018 9:16 pm", cli.startTime);
        assertEquals("12/1/2018 1:16 am", cli.endTime);
        assertNull(cli.prettyPath);
        assertNull(cli.filePath);

        assertEquals(false, cli.pretty);
        assertEquals(false, cli.prettyStdout);
        assertEquals(false, cli.prettyFile);

        assertEquals(false, cli.textFile);
        assertEquals(true, cli.print);
        assertEquals(false, cli.readme); 
    }

    @Test
    public void CliTest_PrintMiddle() throws Exception
    {
        String[] args = {"customer", "503-123-1234", "503-123-1234", "-print", "01/15/2018", "07:16", "pm", "12/1/2018", "09:16", "pm"};
        Cli cli = new Cli(args);

        assertEquals("customer", cli.customer);
        assertEquals("503-123-1234", cli.callerNumber);
        assertEquals("503-123-1234", cli.calleeNumber);
        assertEquals("01/15/2018 07:16 pm", cli.startTime);
        assertEquals("12/1/2018 09:16 pm", cli.endTime);
        assertNull(cli.prettyPath);
        assertNull(cli.filePath);

        assertEquals(false, cli.pretty);
        assertEquals(false, cli.prettyStdout);
        assertEquals(false, cli.prettyFile);

        assertEquals(false, cli.textFile);
        assertEquals(true, cli.print);
        assertEquals(false, cli.readme);
    }

    // ReadMe Tests ///////////////////////////////////////////////////////////////

    @Test
    public void CliTest_ReadMeOnly() throws Exception
    {
        String[] args = {"-README"};
        Cli cli = new Cli(args);

        assertEquals(null, cli.customer);
        assertEquals(null, cli.callerNumber);
        assertEquals(null, cli.calleeNumber);
        assertEquals(null, cli.startTime);
        assertEquals(null, cli.endTime);
        assertNull(cli.prettyPath);
        assertNull(cli.filePath);

        assertEquals(false, cli.pretty);
        assertEquals(false, cli.prettyStdout);
        assertEquals(false, cli.prettyFile);

        assertEquals(false, cli.textFile);
        assertEquals(false, cli.print);
        assertEquals(true, cli.readme);
    }

    @Test(expected = PhoneBillException.class)
    public void CliTest_ReadMeFirst() throws Exception
    {
        String[] args = {"-README", "customer", "503-123-1234", "503-123-1234",
                         "01/15/2018", "9:16", "pm", "12/1/2018", "03:16", "pm", "-print"};
        Cli cli = new Cli(args);
    }

    @Test(expected = PhoneBillException.class)
    public void CliTest_ReadMeLast() throws Exception
    {
        String[] args = {"customer", "503-123-1234", "503-123-1234",
                         "1/15/2018", "09:16", "AM", "12/1/2018", "9:16", "pm", "-print", "-README"};
        Cli cli = new Cli(args);
    }

    @Test(expected = PhoneBillException.class)
    public void CliTest_ReadMeMiddle() throws Exception
    {
        String[] args = {"customer", "503-123-1234", "503-123-1234", "-README",
                         "1/15/2018", "9:16", "pm", "12/1/2018", "9:16", "pm", "-print"};
        Cli cli = new Cli(args);
    }

    // Text File Tests ///////////////////////////////////////////////////////////////
    @Test
    public void CliTest_TextFile_First() throws Exception
    {
        String[] args = {"-textFile", "text.txt", "customer", "503-123-1234", "503-123-1234",
                         "1/15/2018", "9:16", "pm", "12/1/2018", "9:16", "pm"};
        Cli cli = new Cli(args);

        assertEquals("customer", cli.customer);
        assertEquals("503-123-1234", cli.callerNumber);
        assertEquals("503-123-1234", cli.calleeNumber);
        assertEquals("1/15/2018 9:16 pm", cli.startTime);
        assertEquals("12/1/2018 9:16 pm", cli.endTime);
        assertNull(cli.prettyPath);
        assertEquals("text.txt", cli.filePath.toString());

        assertEquals(false, cli.pretty);
        assertEquals(false, cli.prettyStdout);
        assertEquals(false, cli.prettyFile);

        assertEquals(true, cli.textFile);
        assertEquals(false, cli.print);
        assertEquals(false, cli.readme);
    }

    @Test
    public void CliTest_TextFile_Last() throws Exception
    {
        String[] args = {"customer", "503-123-1234", "503-123-1234",
                                     "1/15/2018", "9:39", "AM", "12/1/2018", "9:39", "PM", "-textFile", "text.txt"};
        Cli cli = new Cli(args);

        assertEquals("customer", cli.customer);
        assertEquals("503-123-1234", cli.callerNumber);
        assertEquals("503-123-1234", cli.calleeNumber);
        assertEquals("1/15/2018 9:39 AM", cli.startTime);
        assertEquals("12/1/2018 9:39 PM", cli.endTime);
        assertNull(cli.prettyPath);
        assertEquals("text.txt", cli.filePath.toString());

        assertEquals(false, cli.pretty);
        assertEquals(false, cli.prettyStdout);
        assertEquals(false, cli.prettyFile);

        assertEquals(true, cli.textFile);
        assertEquals(false, cli.print);
        assertEquals(false, cli.readme);

    }

    @Test
    public void CliTest_TextFile_Middle() throws Exception
    {
        String[] args = {"customer", "503-123-1234", "503-123-1234", "-textFile", "text.txt",
                                     "1/15/2018", "09:39", "am", "12/1/2018", "09:39", "pm"};
        Cli cli = new Cli(args);

        assertEquals("customer", cli.customer);
        assertEquals("503-123-1234", cli.callerNumber);
        assertEquals("503-123-1234", cli.calleeNumber);
        assertEquals("1/15/2018 09:39 am", cli.startTime);
        assertEquals("12/1/2018 09:39 pm", cli.endTime);
        assertNull(cli.prettyPath);
        assertEquals("text.txt", cli.filePath.toString());

        assertEquals(false, cli.pretty);
        assertEquals(false, cli.prettyStdout);
        assertEquals(false, cli.prettyFile);

        assertEquals(true, cli.textFile);
        assertEquals(false, cli.print);
        assertEquals(false, cli.readme);
    }

    @Test(expected = PhoneBillException.class)
    public void CliTest_TextFileWithAllArgs() throws Exception
    {
        String[] args = {"-textFile", "text.txt", "customer", "503-123-1234", "503-123-1234",
                         "1/15/2018", "11:16", "am", "12/1/2018", "9:16", "pm", "-print", "-README"};
        Cli cli = new Cli(args);
    }

    @Test(expected = PhoneBillException.class)
    public void CliTest_TextFile_NoPath() throws Exception
    {
        String[] args = {"customer", "503-123-1234", "503-123-1234", "1/15/2018",
                         "3:16", "am", "12/1/2018", "12:16", "pm", "-textFile"};
        Cli cli = new Cli(args);

        fail("Not tested yet");
    }

//    @Test
//    public void IterateTest()
//    {
//        String[] args = {"customer", "callerNumber", "calleeNumber", "startTime", "endTime", "-print", "-README"};
//        Cli cli = new Cli(args);
//
//        cli.Iterate();
//    }

//    @Test
//    public void ToString()
//    {
//        String[] args = {"customer", "callerNumber", "calleeNumber", "startTime", "endTime", "-print", "-README"};
//        Cli cli = new Cli(args);
//
//        String expect = "Executable: " + args[0] +
//                "\ncustomer: " + args[1] +
//                "\ncallerNumber: " + args[2] +
//                "\ncalleeNumber: " + args[3] +
//                "\nstartTime: " + args[4] +
//                "\nendTime: " + args[5] +
//                "\nprint: " + args[6] +
//                "\nREADME: " + args[7];
//
//        String result = cli.ToString();
//
//        assertEquals(expect, result);
//
//        //System.out.println(str);
//
//    }
}

