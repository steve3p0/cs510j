package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.PhoneBillParser;
import org.junit.Test;

import java.io.*;
import java.nio.file.*;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class TextParserTest
{

    @Test
    public void TestParser_Basic() throws ParserException, IOException
    {
        //Steve
        // [Phone call from 123-123-1234 to 123-123-1234 from 1/15/2018 19:39 to 1/15/2018 20:39]

        String filePath = "testparser.txt";
        String customer = "Steve";
        PhoneBill bill1 = new PhoneBill(customer, filePath);
        PhoneCall call1 = new PhoneCall("123-123-1234", "321-321-3210", "1/15/2018 19:39","12/5/2018 7:39");

        bill1.addPhoneCall(call1);

        TextDumper dumper = new TextDumper();
        dumper.dump(bill1);

        File file = new File(filePath);

        try
        {
            TextParser tp = new TextParser(Paths.get(filePath), customer);
            PhoneBill bill = tp.parse();
            Collection<PhoneCall> calls = bill.getPhoneCalls();

            PhoneCall call = calls.iterator().next();

            assertEquals("Steve", bill.getCustomer());
            assertEquals("123-123-1234", call.getCaller());
            assertEquals("321-321-3210", call.getCallee());
            assertEquals("1/15/2018 19:39", call.getStartTimeString());
            assertEquals("12/5/2018 7:39", call.getEndTimeString());
        }
        finally
        {
            file.delete();
        }
    }

    @Test(expected = ParserException.class)
    public void TestParser_Constructor_FileNotFound() throws ParserException
    {
        Path path = Paths.get("filenotfound.txt");
        String customer = "Steve";
        TextParser parser = new TextParser(path, customer);
    }

    @Test(expected = ParserException.class)
    public void TestParser_EmptyFile() throws ParserException, IOException
    {
        //Steve
        // [Phone call from 123-123-1234 to 123-123-1234 from 1/15/2018 19:39 to 1/15/2018 20:39]

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
        String strCall = "[Phone call from 123-123-1234 to 123-123-1234 from 1/15/2018 19:39 to 12/5/2018 7:39]";

        String reDate = "\\d{1,2}/\\d{1,2}/\\d{4}";

        Pattern p = Pattern.compile(reDate);
        Matcher m = p.matcher(strCall);

        m.find();
        String startTime = m.group();
        m.find();
        String endTime = m.group();

        assertEquals("1/15/2018", startTime);
        assertEquals("12/5/2018", endTime);
    }

    @Test
    public void TestDateTime_Matching()
    {
        // Match Datetimes
        String strCall = "[Phone call from 123-123-1234 to 123-123-1234 from 1/15/2018 19:39 to 12/5/2018 7:39]";

        String reDate = "\\d{1,2}/\\d{1,2}/\\d{4}";
        String reTime = "\\d{1,2}:\\d{2}";

        Pattern p = Pattern.compile(reDate + " " + reTime);
        Matcher m = p.matcher(strCall);

        m.find();
        String startTime = m.group();
        m.find();
        String endTime = m.group();

        assertEquals("1/15/2018 19:39", startTime);
        assertEquals("12/5/2018 7:39", endTime);
    }
}