package edu.pdx.cs410J.sbraich;

import org.junit.Test;

import java.io.*;
import java.nio.file.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class TextParserTest
{

    @Test
    public void TestParser()
    {
        //assert(false);
        fail("Not impelemented yet");
    }

    @Test
    public void TestTextDumper_Basic() throws IOException
    {
        //assert(false);
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
}