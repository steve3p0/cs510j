package edu.pdx.cs410J.sbraich;

import java.util.List;
import java.util.Arrays;
import java.util.Collection;
import java.nio.file.*;
import java.nio.charset.Charset;
import java.io.IOException;

import edu.pdx.cs410J.AbstractPhoneBill;
import edu.pdx.cs410J.PhoneBillDumper;

//public class TextDumper implements PhoneBillDumper<T extends AbstractPhoneBill>
public class TextDumper implements PhoneBillDumper<PhoneBill>
{
    public void dump(PhoneBill bill) throws IOException
    {
        Collection<PhoneCall> calls = bill.getPhoneCalls();

        calls.toString();
        List<String> lines = Arrays.asList(bill.getCustomer(), calls.toString());

        Files.write(bill.getFilePath(), lines, Charset.forName("UTF-8"));
    }
}

