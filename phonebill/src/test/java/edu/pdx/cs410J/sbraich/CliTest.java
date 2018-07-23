package edu.pdx.cs410J.sbraich;

import org.junit.Test;

import static org.junit.Assert.*;

public class CliTest
{
    @Test
    public void CliTest_Basic() throws Exception
    {
        String[] args = {"Project2", "customer", "503-123-1234", "503-123-1234", "1/15/2018", "19:39", "12/1/2018", "19:39"};
        Cli cli = new Cli(args);

        assertEquals("customer", cli.customer);
        assertEquals("503-123-1234", cli.callerNumber);
        assertEquals("503-123-1234", cli.calleeNumber);
        assertEquals("1/15/2018 19:39", cli.startTime);
        assertEquals("12/1/2018 19:39", cli.endTime);

        assertEquals(false, cli.textFile);
        assertEquals(false, cli.print);
        assertEquals(false, cli.readme);
    }

    @Test(expected = PhoneBillException.class)
    public void CliTest_TooMany() throws Exception
    {
        String[] args = {"Project2", "customer", "503-123-1234", "503-123-1234", "1/15/2018", "19:39", "12/1/2018", "19:39", "EXTRA"};
        Cli cli = new Cli(args);
    }

    @Test(expected = PhoneBillException.class)
    public void CliTest_TooFew1() throws Exception
    {
        String[] args = {"Project2"};
        Cli cli = new Cli(args);
    }

    @Test(expected = PhoneBillException.class)
    public void CliTest_TooFew2() throws Exception
    {
        String[] args = {"Project2", "customer"};
        Cli cli = new Cli(args);
    }

    @Test(expected = PhoneBillException.class)
    public void CliTest_TooFew7() throws Exception
    {
        String[] args = {"Project2", "customer", "503-123-1234", "503-123-1234", "1/15/2018", "19:39", "12/1/2018"};
        Cli cli = new Cli(args);
    }

    @Test
    public void CliTest_PrintFirst() throws Exception
    {
        String[] args = {"Project2", "-print", "customer", "503-123-1234", "503-123-1234", "1/15/2018", "19:39", "12/1/2018", "19:39"};
        Cli cli = new Cli(args);

        assertEquals("customer", cli.customer);
        assertEquals("503-123-1234", cli.callerNumber);
        assertEquals("503-123-1234", cli.calleeNumber);
        assertEquals("1/15/2018 19:39", cli.startTime);
        assertEquals("12/1/2018 19:39", cli.endTime);

        assertEquals(false, cli.textFile);
        assertEquals(true, cli.print);
        assertEquals(false, cli.readme);
    }
    
    @Test
    public void CliTest_PrintLast() throws Exception
    {
        String[] args = {"Project2", "customer", "503-123-1234", "503-123-1234", "1/15/2018", "19:39", "12/1/2018", "19:39", "-print"};
        Cli cli = new Cli(args);

        assertEquals("customer", cli.customer);
        assertEquals("503-123-1234", cli.callerNumber);
        assertEquals("503-123-1234", cli.calleeNumber);
        assertEquals("1/15/2018 19:39", cli.startTime);
        assertEquals("12/1/2018 19:39", cli.endTime);

        assertEquals(false, cli.textFile);
        assertEquals(true, cli.print);
        assertEquals(false, cli.readme); 
    }

    @Test
    public void CliTest_PrintMiddle() throws Exception
    {
        String[] args = {"Project2", "customer", "503-123-1234", "503-123-1234", "-print", "1/15/2018", "19:39", "12/1/2018", "19:39"};
        Cli cli = new Cli(args);

        assertEquals("customer", cli.customer);
        assertEquals("503-123-1234", cli.callerNumber);
        assertEquals("503-123-1234", cli.calleeNumber);
        assertEquals("1/15/2018 19:39", cli.startTime);
        assertEquals("12/1/2018 19:39", cli.endTime);

        assertEquals(false, cli.textFile);
        assertEquals(true, cli.print);
        assertEquals(false, cli.readme);
    }
    
    
    @Test
    public void CliTest_ReadMeOnly() throws Exception
    {
        String[] args = {"Project2", "-README"};
        Cli cli = new Cli(args);

        assertEquals(false, cli.textFile);
        assertEquals(false, cli.print);
        assertEquals(true, cli.readme);
    }

    @Test(expected = PhoneBillException.class)
    public void CliTest_ReadMeFirst() throws Exception
    {
        String[] args = {"Project2", "-README", "customer", "503-123-1234", "503-123-1234", "1/15/2018", "19:39", "12/1/2018", "19:39", "-print"};
        Cli cli = new Cli(args);
    }

    @Test(expected = PhoneBillException.class)
    public void CliTest_ReadMeLast() throws Exception
    {
        String[] args = {"Project2", "customer", "503-123-1234", "503-123-1234", "1/15/2018", "19:39", "12/1/2018", "19:39", "-print", "-README"};
        Cli cli = new Cli(args);
    }

    @Test(expected = PhoneBillException.class)
    public void CliTest_ReadMeMiddle() throws Exception
    {
        String[] args = {"Project2", "customer", "503-123-1234", "503-123-1234", "-README", "1/15/2018", "19:39", "12/1/2018", "19:39", "-print"};
        Cli cli = new Cli(args);
    }
    
    @Test
    public void CliTest_TextFile_First() throws Exception
    {
        String[] args = {"Project2", "-textFile", "text.txt", "customer", "503-123-1234", "503-123-1234", "1/15/2018", "19:39", "12/1/2018", "19:39"};
        Cli cli = new Cli(args);

        assertEquals("customer", cli.customer);
        assertEquals("503-123-1234", cli.callerNumber);
        assertEquals("503-123-1234", cli.calleeNumber);
        assertEquals("1/15/2018 19:39", cli.startTime);
        assertEquals("12/1/2018 19:39", cli.endTime);

        assertEquals(false, cli.print);
        assertEquals(false, cli.readme);
        
        assertEquals(true, cli.textFile);
        assertEquals("text.txt", cli.filePath.toString());
    }

    @Test
    public void CliTest_TextFile_Last() throws Exception
    {
        String[] args = {"Project2", "customer", "503-123-1234", "503-123-1234", "1/15/2018", "19:39", "12/1/2018", "19:39", "-textFile", "text.txt"};
        Cli cli = new Cli(args);

        assertEquals("customer", cli.customer);
        assertEquals("503-123-1234", cli.callerNumber);
        assertEquals("503-123-1234", cli.calleeNumber);
        assertEquals("1/15/2018 19:39", cli.startTime);
        assertEquals("12/1/2018 19:39", cli.endTime);

        assertEquals(false, cli.print);
        assertEquals(false, cli.readme);

        assertEquals(true, cli.textFile);
        assertEquals("text.txt", cli.filePath.toString());
    }

    @Test
    public void CliTest_TextFile_Middle() throws Exception
    {
        String[] args = {"Project2", "customer", "503-123-1234", "503-123-1234", "-textFile", "text.txt", "1/15/2018", "19:39", "12/1/2018", "19:39"};
        Cli cli = new Cli(args);

        assertEquals("customer", cli.customer);
        assertEquals("503-123-1234", cli.callerNumber);
        assertEquals("503-123-1234", cli.calleeNumber);
        assertEquals("1/15/2018 19:39", cli.startTime);
        assertEquals("12/1/2018 19:39", cli.endTime);

        assertEquals(false, cli.print);
        assertEquals(false, cli.readme);

        assertEquals(true, cli.textFile);
        assertEquals("text.txt", cli.filePath.toString());
    }

    @Test(expected = PhoneBillException.class)
    public void CliTest_TextFileWithAllArgs() throws Exception
    {
        String[] args = {"Project2", "-textFile", "text.txt", "customer", "503-123-1234", "503-123-1234", "1/15/2018", "19:39", "12/1/2018", "19:39", "-print", "-README"};
        Cli cli = new Cli(args);
    }

    @Test(expected = PhoneBillException.class)
    public void CliTest_TextFile_NoPath() throws Exception
    {
        String[] args = {"Project2", "customer", "503-123-1234", "503-123-1234", "1/15/2018", "19:39", "12/1/2018", "19:39", "-textFile"};
        Cli cli = new Cli(args);

        fail("Not tested yet");
    }

//    @Test
//    public void IterateTest()
//    {
//        String[] args = {"Project2", "customer", "callerNumber", "calleeNumber", "startTime", "endTime", "-print", "-README"};
//        Cli cli = new Cli(args);
//
//        cli.Iterate();
//    }

//    @Test
//    public void ToString()
//    {
//        String[] args = {"Project2", "customer", "callerNumber", "calleeNumber", "startTime", "endTime", "-print", "-README"};
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

