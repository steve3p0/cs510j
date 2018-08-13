package edu.pdx.cs410J.sbraich;

import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class TextDumperTest
{
    @Test
    public void TestTextDumper_MultipleCalls() throws PhoneBillException, IOException //, edu.pdx.cs410J.ParserException
    {
        String customer = "Steve";
        String filePath = "text.txt";

        PhoneBill bill = new PhoneBill(customer, filePath);

        File file = new File(bill.getFilePath().toString());

        try
        {
            // TODO: USE MOCKING!!!

            String callFormat = "[Phone call from %s to %s from %s to %s]";

            PhoneCall call1 = new PhoneCall("111-111-1111", "123-123-1234", "1/1/2018 12:01 AM", "1/1/2018 12:05 AM");
            PhoneCall call2 = new PhoneCall("222-222-2222", "123-123-1234", "2/1/2018 12:01 AM", "2/1/2018 12:05 AM");
            PhoneCall call3 = new PhoneCall("333-333-3333", "123-123-1234", "3/1/2018 12:01 AM", "3/1/2018 12:05 AM");
            PhoneCall call4 = new PhoneCall("444-444-4444", "123-123-1234", "4/1/2018 12:01 AM", "4/1/2018 12:05 AM");

            String str1 = String.format(callFormat, call1.getCaller(), call1.getCallee(), call1.getStartTimeString(), call1.getEndTimeString());
            String str2 = String.format(callFormat, call2.getCaller(), call2.getCallee(), call2.getStartTimeString(), call2.getEndTimeString());
            String str3 = String.format(callFormat, call3.getCaller(), call3.getCallee(), call3.getStartTimeString(), call3.getEndTimeString());
            String str4 = String.format(callFormat, call4.getCaller(), call4.getCallee(), call4.getStartTimeString(), call4.getEndTimeString());

            bill.addPhoneCall(call3);
            bill.addPhoneCall(call1);
            bill.addPhoneCall(call4);
            bill.addPhoneCall(call2);

            List<String> expectedLines = new ArrayList<String>();
            expectedLines.add(customer);
            expectedLines.add(str1);
            expectedLines.add(str2);
            expectedLines.add(str3);
            expectedLines.add(str4);

            TextDumper dumper = new TextDumper();
            dumper.dump(bill);

            assertTrue(file.exists());
            assertTrue(!file.isDirectory());

            List<String> outputLines = Files.readAllLines(file.toPath(), Charset.defaultCharset());

            String expectedOutput = String.join("\n", expectedLines);
            String actualOutput = String.join("\n", outputLines);

            assertEquals(expectedOutput, actualOutput);
        }
        finally
        {
            file.delete();
        }

    }

    @Test
    public void TestTextDumper_Basic() throws PhoneBillException, IOException //, edu.pdx.cs410J.ParserException
    {
        // TODO: USE MOCKING!!!

        PhoneBill bill = new PhoneBill("Steve", "text.txt");
        PhoneCall call = new PhoneCall("123-123-1234", "123-123-1234",
                "1/15/2018 07:39 am","1/15/2018 08:39 pm");

        bill.addPhoneCall(call);

        TextDumper dumper = new TextDumper();
        dumper.dump(bill);
        File file = new File(bill.getFilePath().toString());

        assertTrue(file.exists());
        assertTrue(!file.isDirectory());

        TextParserTest tp = new TextParserTest();

        // MOCK THIS!!!
        //tp.TestParser_Basic();

        file.delete();
    }

    @Test(expected = IOException.class)
    public void TestTextDumper_ReadOnly() throws PhoneBillException, IOException
    {
        File f = new File("readonly.txt");
        f.createNewFile();

        try
        {
            f.setReadOnly();

            PhoneBill bill = new PhoneBill("Steve", "readonly.txt");
            PhoneCall call = new PhoneCall("123-123-1234", "123-123-1234",
                                               "1/15/2018 7:39 AM", "1/15/2018 8:39 PM");

            bill.addPhoneCall(call);

            TextDumper dumper = new TextDumper();
            dumper.dump(bill);
        }
        finally
        {
            f.setWritable(true);
            f.delete();
        }
    }

    @Test(expected = IOException.class)
    public void TestTextDumper_InvalidPath() throws PhoneBillException, IOException
    {
        PhoneBill bill = new PhoneBill("Steve", "/nonexistant/readonly.txt");
        PhoneCall call = new PhoneCall("123-123-1234", "123-123-1234",
                                           "1/15/2018 11:39 AM", "1/15/2018 10:39 pm");

        bill.addPhoneCall(call);

        TextDumper dumper = new TextDumper();
        dumper.dump(bill);
    }

//    @Test  (expected = PhoneBillException.class)
//    public void TestTextDumper_Empty() throws IOException
//    {
//        File f = new File("empty.txt");
//        f.createNewFile();
//
//        try
//        {
//            PhoneBill bill = new PhoneBill("Steve", "empty.txt");
//            PhoneCall call = new PhoneCall("123-123-1234", "123-123-1234", "1/15/2018 19:39","1/15/2018 20:39");
//
//            bill.addPhoneCall(call);
//
//            TextDumper dumper = new TextDumper();
//            dumper.dump(bill);
//        }
//        finally
//        {
//            f.setWritable(true);
//            f.delete();
//        }
//    }
}