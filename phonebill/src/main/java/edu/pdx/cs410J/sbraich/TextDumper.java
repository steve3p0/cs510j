package edu.pdx.cs410J.sbraich;

import java.util.List;
import java.util.Arrays;
import java.util.Collection;
import java.nio.file.*;
import java.nio.charset.Charset;

import edu.pdx.cs410J.AbstractPhoneBill;
import edu.pdx.cs410J.PhoneBillDumper;
import java.io.IOException;

//public class TextDumper implements PhoneBillDumper<T extends AbstractPhoneBill>
public class TextDumper implements PhoneBillDumper<PhoneBill>
{
    public void dump(PhoneBill bill) throws IOException
    {
        // Check if directory path exists
//        Path dir = bill.getFilePath().getParent();
//        if (!Files.isDirectory(dir)) throw new PhoneBillException("textFile directory path not found: " + dir.toString());
//        Files.isDirectory(dir);
//        Files.exists(dir);
//        Files.isWritable(dir);

        // If file exists - file should be writable
        //Files.exists(bill.getFilePath());
        // Check if directory path is writable
        // check if disk full
        //if (file.length() == 0)

        Collection<PhoneCall> calls = bill.getPhoneCalls();

        calls.toString();
        List<String> lines = Arrays.asList(bill.getCustomer(), calls.toString());

        Files.write(bill.getFilePath(), lines, Charset.forName("UTF-8"));
    }
}

