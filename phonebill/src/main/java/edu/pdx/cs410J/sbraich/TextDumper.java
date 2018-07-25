package edu.pdx.cs410J.sbraich;

import java.util.List;
import java.util.Arrays;
import java.util.Collection;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.io.IOException;
import java.io.File;



import edu.pdx.cs410J.PhoneBillDumper;

/// public class TextDumper implements PhoneBillDumper<T extends AbstractPhoneBill>
public class TextDumper implements PhoneBillDumper<PhoneBill>
{
    /// Impelements dump method of PhoneBillDumper method
    public void dump(PhoneBill bill) throws IOException
    {
        Collection<PhoneCall> calls = bill.getPhoneCalls();

        Path path = bill.getFilePath();
        String callStr = calls.toString();
        String customer = bill.getCustomer();
        List<String> lines = Arrays.asList(customer, callStr);

        this.CreateDirFromFilePath(path);
        Files.write(path, lines, Charset.forName("UTF-8"));
    }

    private void CreateDirFromFilePath(Path path) throws IOException
    {
        Path parentDir = path.getParent();
        File dir = new File(parentDir.toString());
        if (!dir.exists())
        {
            Files.createDirectories(path);
        }
    }
}

