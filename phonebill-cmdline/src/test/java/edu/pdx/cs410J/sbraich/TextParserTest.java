package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.PhoneBillParser;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class TextParserTest
{

    @Test
    public void TestParser_Multiple() throws PhoneBillException, ParserException, IOException
    {
        // [Phone call from 123-123-1234 to 123-123-1234 from 1/15/2018 7:39 am to 12/5/2018 11:05 pm]
        String customer = "Steve";
        String filePath = "testparser.txt";

        File file = new File(filePath);

        try
        {
            PhoneCall call1 = new PhoneCall("111-111-1111", "123-123-1234", "1/1/2018 12:01 AM", "1/1/2018 12:05 AM");
            PhoneCall call2 = new PhoneCall("222-222-2222", "123-123-1234", "2/1/2018 12:01 AM", "2/1/2018 12:05 AM");
            PhoneCall call3 = new PhoneCall("333-333-3333", "123-123-1234", "3/1/2018 12:01 AM", "3/1/2018 12:05 AM");
            PhoneCall call4 = new PhoneCall("444-444-4444", "123-123-1234", "4/1/2018 12:01 AM", "4/1/2018 12:05 AM");

            PhoneBill billDumper = new PhoneBill(customer);
            billDumper.setFilePath(filePath);
            billDumper.addPhoneCall(call3);
            billDumper.addPhoneCall(call1);
            billDumper.addPhoneCall(call4);
            billDumper.addPhoneCall(call2);

            TextDumper dumper = new TextDumper();
            dumper.dump(billDumper);

            TextParser tp = new TextParser(Paths.get(filePath), customer);
            PhoneBill billParser = tp.parse();

            String actual = "";
            for (PhoneCall ph : billParser.getPhoneCalls())
            {
                actual += ph.getCaller() + ",";
            }

            String expected = "111-111-1111,222-222-2222,333-333-3333,444-444-4444,";

            assertEquals(expected, actual);
        }
        finally
        {
            file.delete();
        }
    }


    @Test
    public void TestParser_Basic() throws PhoneBillException, ParserException, IOException
    {
        //Steve
        // [Phone call from 123-123-1234 to 123-123-1234 from 1/15/2018 7:39 am to 12/5/2018 11:05 pm]

        String customer = "Steve";
        String filePath = "testparser.txt";

        File file = new File(filePath);

        try
        {
            PhoneBill bill1 = new PhoneBill(customer);
            bill1.setFilePath(filePath);
            PhoneCall call1 = new PhoneCall("123-123-1234", "321-321-3210",
                                                "1/15/2018 7:39 am","12/5/2018 11:05 pm");

            bill1.addPhoneCall(call1);

            TextDumper dumper = new TextDumper();
            dumper.dump(bill1);

            TextParser tp = new TextParser(Paths.get(filePath), customer);
            PhoneBill bill = tp.parse();
            Collection<PhoneCall> calls = bill.getPhoneCalls();

            PhoneCall call = calls.iterator().next();

            assertEquals("Steve", bill.getCustomer());
            assertEquals("123-123-1234", call.getCaller());
            assertEquals("321-321-3210", call.getCallee());
            assertEquals("01/15/2018 07:39 AM", call.getStartTimeString());
            assertEquals("12/05/2018 11:05 PM", call.getEndTimeString());
        }
        finally
        {
            file.delete();
        }
    }

    @Test(expected = ParserException.class)
    public void TestParser_MalformattedFile1() throws ParserException, IOException
    {
        String callStr = "[Phone call from to from 1/15/2018 7:39 AM to 1/15/2018 10:39 PM]";
        String filePath = "malformatted.txt";
        String customer = "Steve";

        File file = new File(filePath);

        List<String> lines = Arrays.asList("Steve", callStr);

        Files.write(Paths.get(filePath), lines, Charset.forName("UTF-8"));

        try
        {
            TextParser tp = new TextParser(Paths.get(filePath), customer);
            PhoneBill bill = tp.parse();
        }
        finally
        {
            file.delete();
        }
    }

    @Test(expected = ParserException.class)
    public void TestParser_MalformattedFile2() throws ParserException, IOException
    {
        String callStr = "[Phone call from to from 1/15/2018 11:39 AM to 1/15/2018 8:39 PM]";
        String filePath = "malformatted.txt";
        String customer = "";

        File file = new File(filePath);

        List<String> lines = Arrays.asList(customer, callStr);

        Files.write(Paths.get(filePath), lines, Charset.forName("UTF-8"));

        try
        {
            TextParser tp = new TextParser(Paths.get(filePath), customer);
            PhoneBill bill = tp.parse();
        }
        finally
        {
            file.delete();
        }
    }

    @Test(expected = ParserException.class)
    public void TestParser_MalformattedFile3() throws ParserException, IOException
    {
        String filePath = "malformatted.txt";
        String customer = "Steve";

        File file = new File(filePath);

        List<String> lines = Arrays.asList(customer);

        Files.write(Paths.get(filePath), lines, Charset.forName("UTF-8"));

        try
        {
            TextParser tp = new TextParser(Paths.get(filePath), "Steve");
            PhoneBill bill = tp.parse();
        }
        finally
        {
            file.delete();
        }
    }

    @Test(expected = ParserException.class)
    public void TestParser_MalformattedFile4() throws ParserException, IOException
    {
        String callStr = "fsthdf435ydfghdf";
        String filePath = "malformatted.txt";
        String customer = "Steve";

        File file = new File(filePath);

        List<String> lines = Arrays.asList(customer, callStr);

        Files.write(Paths.get(filePath), lines, Charset.forName("UTF-8"));

        try
        {
            TextParser tp = new TextParser(Paths.get(filePath), customer);
            PhoneBill bill = tp.parse();
        }
        finally
        {
            file.delete();
        }
    }

    @Test(expected = ParserException.class)
    public void TestParser_DiffCustomers() throws PhoneBillException, ParserException, IOException
    {
        //Steve
        // [Phone call from 123-123-1234 to 123-123-1234 from 1/15/2018 19:39 AM to 1/15/2018 8:39 PM]

        String filePath = "testparser.txt";
        String customer1 = "Steve";
        String customer2 = "Not Steve";

        PhoneBill bill1 = new PhoneBill(customer1);
        bill1.setFilePath(filePath);
        PhoneCall call1 = new PhoneCall("123-123-1234", "321-321-3210",
                                            "1/15/2018 7:39 AM","12/5/2018 8:39 PM");

        bill1.addPhoneCall(call1);

        TextDumper dumper = new TextDumper();
        dumper.dump(bill1);

        File file = new File(filePath);

        try
        {
            TextParser tp = new TextParser(Paths.get(filePath), customer2);
            PhoneBill bill = tp.parse();
        }
        finally
        {
            file.delete();
        }
    }

//    @Test(expected = ParserException.class)
//    public void TestParser_Constructor_FileNotFound() throws ParserException
//    {
//        Path path = Paths.get("filenotfound.txt");
//        String customer = "Steve";
//        TextParser parser = new TextParser(path, customer);
//    }

    @Test(expected = ParserException.class)
    public void TestParser_EmptyFile() throws ParserException, IOException
    {
        //Steve
        // [Phone call from 123-123-1234 to 123-123-1234 from 1/15/2018 19:39 AM to 1/15/2018 8:39 PM]

        String filePath = "empty.txt";
        String customer = "Steve";

        File file = new File(filePath);
        file.createNewFile();

        try
        {
            TextParser tp = new TextParser(Paths.get(filePath), customer);
        }
        finally
        {
            file.delete();
        }
    }

    // These are tests to figure out date and time matching

    @Test
    public void TestDate_Matching()
    {
        // Match Datetimes
        String strCall = "[Phone call from 123-123-1234 to 123-123-1234 from 1/15/2018 7:39 AM to 12/5/2018 7:39 PM]";

        String reDate = "\\d{1,2}/\\d{1,2}/\\d{4}";
        String reTime = "\\d{1,2}:\\d{2}\\s+(AM|PM)";

        Pattern p = Pattern.compile(reDate + " " + reTime);
        Matcher m = p.matcher(strCall);

        m.find();
        String startTime = m.group();
        m.find();
        String endTime = m.group();

        assertEquals("1/15/2018 7:39 AM", startTime);
        assertEquals("12/5/2018 7:39 PM", endTime);
    }

    @Test
    public void TestDateTime_Matching()
    {
        // Match Datetimes
        String strCall = "[Phone call from 123-123-1234 to 123-123-1234 from 1/15/2018 7:39 AM to 12/5/2018 7:39 PM]";

        String reDate = "\\d{1,2}/\\d{1,2}/\\d{4}";
        String reTime = "\\d{1,2}:\\d{2}\\s+(AM|PM)";

        Pattern p = Pattern.compile(reDate + " " + reTime);
        Matcher m = p.matcher(strCall);

        m.find();
        String startTime = m.group();
        m.find();
        String endTime = m.group();

        assertEquals("1/15/2018 7:39 AM", startTime);
        assertEquals("12/5/2018 7:39 PM", endTime);
    }
}