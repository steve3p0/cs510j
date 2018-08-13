package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.PhoneBillParser;
import edu.pdx.cs410J.ParserException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;
import java.io.*;
import java.nio.file.*;

/// TextParser concrete class that reads in Phonebill from a file
public class TextParser implements PhoneBillParser<PhoneBill>
{
    public final Path filePath;
    public final String customerFromCli;

    private String callerNumber;
    private String calleeNumber;
    private String startTime;
    private String endTime;

    /// Constructor for Text Parser - Path and customerName as args
    public TextParser(Path path, String customerName) throws ParserException
    {
        this.filePath = path;
        this.customerFromCli = customerName;

        this.validateFile();
    }

    /// FileExists method called in main
    public Boolean fileExists()
    {
        return Files.exists(this.filePath);
    }

    private void extractPhoneNumbers(String line) throws ParserException
    {
        try
        {
            // Match Phone Numbers
            Pattern pattern = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");
            Matcher matcher = pattern.matcher(line);

            matcher.find();
            this.callerNumber = matcher.group();
            matcher.find();
            this.calleeNumber = matcher.group();
        }
        catch (IllegalStateException e)
        {
            throw new ParserException("Invalid Phone Number: " + line);
        }
    }

    private void extractDateTimes(String line) throws ParserException
    {
        try
        {
            // Match Datetimes
            String reDate = "\\d{1,2}/\\d{1,2}/\\d{4}";
            String reTime = "\\d{1,2}:\\d{2}\\s+(AM|PM)";

            Pattern p = Pattern.compile(reDate + " " + reTime);
            Matcher m = p.matcher(line);

            m.find();
            this.startTime = m.group();
            m.find();
            this.endTime = m.group();
        }
        catch (IllegalStateException e)
        {
            throw new ParserException("Invalid DateTime: " + line);
        }

    }

    /// Overrides parse method of PhoneBillParser method
    @Override
    public PhoneBill parse() throws ParserException
    {
        try
        {
            List<String> lines = this.ReadFile();

            // Get the Customer Name
            String customerFromFile = lines.get(0);
            lines.remove(0);
            PhoneBill bill = new PhoneBill(this.customerFromCli, customerFromFile, filePath.toString());

            for (String line : lines)
            {
                this.extractPhoneNumbers(line);
                this.extractDateTimes(line);

                PhoneCall call = new PhoneCall(this.callerNumber, this.calleeNumber, this.startTime, this.endTime);
                bill.addPhoneCall(call);
            }

            return bill;
        }
        catch (PhoneBillException e)
        {
            throw new ParserException(e.getMessage());
        }
    }

    /// Validates a File Path passed to the TextParser
    private void validateFile() throws ParserException
    {
        if (this.filePath == null)
        {
            throw new ParserException("File path missing");
        }

        // If the file exists - validate the file
        if (Files.exists(this.filePath))
        {
            if (!Files.isRegularFile(this.filePath))
                throw new ParserException("File path is not a file: " + this.filePath.toString());
            if (!Files.exists(this.filePath))
                throw new ParserException("File path doesn't exist: " + this.filePath.toString());
            if (!Files.isWritable(this.filePath))
                throw new ParserException("File path does have write permissions: " + this.filePath.toString());

            File file = new File(this.filePath.toString());
            if (file.length() == 0)
            {
                throw new ParserException("File is empty: " + this.filePath.toString());
            }
        }
        // Else validate the file path
        else
        {
            Path dir = this.filePath.getParent();
            if (dir == null)
            {
                dir = Paths.get(System.getProperty("user.dir"));
            }

            if (!Files.isDirectory(dir)) throw new ParserException("Filepath is invalid: " + dir.toString());
            if (!Files.exists(dir)) throw new ParserException("Directory doesn't exist: " + dir.toString());
            if (!Files.isWritable(dir)) throw new ParserException("Directory does have write permissions: " + dir.toString());
        }
    }

    /// Does the read file i/o heavy lifting
    /// inspired by http://www.avajava.com/tutorials/lessons/how-do-i-read-a-string-from-a-file-line-by-line.html
    private List<String> ReadFile() throws ParserException
    {
        try
        {
            ArrayList<String> lines = new ArrayList<String>();
            Files.lines(this.filePath).forEach(s -> lines.add(s));

            if (lines.size() < 2)
            {
                throw new ParserException("Invalid File Format: Missing PhoneCall line");
            }

            return lines;
        }
        catch (FileNotFoundException e)
        {
            throw new ParserException("File not found: " + this.filePath.toString());
        }
        catch (IOException e)
        {
            throw new ParserException(e.getMessage());
        }
    }
}