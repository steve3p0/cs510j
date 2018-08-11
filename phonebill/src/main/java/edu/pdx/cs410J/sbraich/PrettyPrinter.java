package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.PhoneBillDumper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/// public class TextDumper implements PhoneBillDumper<T extends AbstractPhoneBill>
public class PrettyPrinter implements PhoneBillDumper<PhoneBill>
{
    /// Impelements dump method of PhoneBillDumper method
    public void dump(PhoneBill bill) throws IOException
    {
        Collection<PhoneCall> calls = bill.getPhoneCalls();

        Path path = bill.getFilePath();
        String callStr = calls.toString();
        String customer = "Customer: " + bill.getCustomer();
        List<String> lines = Arrays.asList(customer, callStr);

        this.CreateDirFromFilePath(path);
        this.validateFilePath(path);
        Files.write(path, lines, Charset.forName("UTF-8"));
    }

    private void CreateDirFromFilePath(Path path) throws IOException
    {
        Path parentDir = path.getParent();

        if (parentDir == null)
        {
            parentDir = Paths.get(System.getProperty("user.dir"));
        }

        File dir = new File(parentDir.toString());
        if (!dir.exists())
        {
            Files.createDirectories(parentDir);
        }
    }

    /// Validate File Path
    private void validateFilePath(Path path) throws IOException
    {
        Path dir = path.getParent();
        if (dir == null)
        {
            dir = Paths.get(System.getProperty("user.dir"));
        }

        if (!Files.isDirectory(dir)) throw new IOException("Filepath is invalid: " + dir.toString());
        if (!Files.exists(dir)) throw new IOException("Directory doesn't exist: " + dir.toString());
        if (!Files.isWritable(dir)) throw new IOException("Directory does have write permissions: " + dir.toString());

        if (Files.exists(path) && !Files.isWritable(path))
        {
            throw new IOException("File does have write permissions: " + path.toString());
        }
    }
}

