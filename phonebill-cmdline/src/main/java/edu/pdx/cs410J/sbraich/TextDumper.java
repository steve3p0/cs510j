package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.PhoneBillDumper;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collection;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.io.IOException;
import java.io.File;

/**
 * Class that manages persisting phone billing data to a text file
 */
public class TextDumper implements PhoneBillDumper<PhoneBill>
{
    /// Impelements dump method of PhoneBillDumper method

    /**
     * Writes Phonebill and PhoneCall data to a text file
     * @param bill PhoneBill object to write to a file
     * @throws IOException thrown if PhoneBill write fails
     */
    public void dump(PhoneBill bill) throws IOException
    {
        Collection<PhoneCall> calls = bill.getPhoneCalls();

        Path path = bill.getFilePath();
        String callStr = this.toCallsString(bill);
        String customer = bill.getCustomer();
        List<String> lines = Arrays.asList(customer, callStr);

        this.CreateDirFromFilePath(path);
        this.validateFilePath(path);

        Files.write(path, lines, Charset.forName("UTF-8"));
    }

    /**
     * Converts a collection of PhoneCall objects to a string
     * @param bill Phonebill that is converted to a string
     * @return String representation of all phonecalls in a phone bill
     */
    private String toCallsString(PhoneBill bill)
    {
        List<String> callList = new ArrayList<>();
        String callFormat = "[Phone call from %s to %s from %s to %s]";

        for (PhoneCall call : bill.getPhoneCalls())
        {
            String s = String.format(callFormat, call.getCaller(), call.getCallee(), call.getStartTimeString(), call.getEndTimeString());
            callList.add(s);
        }

        return String.join("\n", callList);
    }

    /**
     * Creates Directory of the PhoneBill filepath, if it doesn't exist
     * @param path The path used to create the directory
     * @throws IOException thrown if the directory can't be created
     */
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

    /**
     * Validates a File Path
     * @param path Path type object to be validated
     * @throws IOException thrown if the filepath object can't be validated.
     */
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

