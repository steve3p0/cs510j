package edu.pdx.cs410J.sbraich;

import org.junit.Test;

import static org.junit.Assert.*;

public class CliTest
{

    @Test
    public void CliTest() throws Exception
    {
        String[] args = {"Project1", "customer", "503-123-1234", "503-123-1234", "1/15/2018 19:39", "12/1/2018 19:39", "-print", "-README"};

        try
        {
            Cli cli = new Cli(args);
        }
        catch (PhoneBillException e)
        {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void CliTest_Fields() throws Exception
    {
        String[] args = {"Project1", "customer", "503-123-1234", "503-123-1234", "1/15/2018", "19:39", "12/1/2018", "19:39", "-print", "-README"};

        try
        {
            Cli cli = new Cli(args);

            assertEquals(cli.customer, "customer");
            assertEquals(cli.callerNumber, "503-123-1234");
            assertEquals(cli.calleeNumber, "503-123-1234");
            assertEquals(cli.startTime, "1/15/2018 19:39");
            assertEquals(cli.endTime, "12/1/2018 19:39");
        }
        catch (PhoneBillException e)
        {
            System.out.println(e.getMessage());
        }
    }

//    @Test
//    public void IterateTest()
//    {
//        String[] args = {"Project1", "customer", "callerNumber", "calleeNumber", "startTime", "endTime", "-print", "-README"};
//        Cli cli = new Cli(args);
//
//        cli.Iterate();
//    }

//    @Test
//    public void ToString()
//    {
//        String[] args = {"Project1", "customer", "callerNumber", "calleeNumber", "startTime", "endTime", "-print", "-README"};
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

