package edu.pdx.cs410J.sbraich;

import org.junit.Test;

import java.io.*;
import java.nio.file.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class TextDumperTest
{
    @Test
    public void TestTextDumper_Basic() throws IOException
    {
        PhoneBill bill = new PhoneBill("Steve", "text.txt");
        PhoneCall call = new PhoneCall("123-123-1234", "123-123-1234", "1/15/2018 19:39","1/15/2018 20:39");

        bill.addPhoneCall(call);

        TextDumper dumper = new TextDumper();
        dumper.dump(bill);
        File file = new File(bill.getFilePath().toString());

        //new File("path/to/file.txt").isFile();

        assertTrue(file.exists());
        assertTrue(!file.isDirectory());

        file.delete();
    }

    @Test(expected = AccessDeniedException.class)
    public void TestTextDumper_ReadOnly() throws IOException
    {
        File f = new File("readonly.txt");
        f.createNewFile();

        try
        {
            f.setReadOnly();

            PhoneBill bill = new PhoneBill("Steve", "readonly.txt");
            PhoneCall call = new PhoneCall("123-123-1234", "123-123-1234", "1/15/2018 19:39", "1/15/2018 20:39");

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

    @Test(expected = NoSuchFileException.class)
    public void TestTextDumper_InvalidPath() throws IOException
    {
        PhoneBill bill = new PhoneBill("Steve", "/nonexistant/readonly.txt");
        PhoneCall call = new PhoneCall("123-123-1234", "123-123-1234", "1/15/2018 19:39", "1/15/2018 20:39");

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