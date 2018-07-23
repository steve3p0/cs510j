package edu.pdx.cs410J.sbraich;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.*;
import java.util.Arrays;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.Charset;

import edu.pdx.cs410J.AbstractPhoneBill;
import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.PhoneBillParser;

public class TextParser implements PhoneBillParser<PhoneBill>
{
    public final Path filePath;

    public TextParser(Path path) throws ParserException
    {
        this.filePath = path;
        this.validateParserPath();
    }

    // inspired by http://www.avajava.com/tutorials/lessons/how-do-i-read-a-string-from-a-file-line-by-line.html
    public PhoneBill parse() throws ParserException
    {
        //StringBuffer buffer = this.ReadFile();

        // Steve
        // [Phone call from 123-123-1234 to 123-123-1234 from 1/15/2018 19:39 to 1/15/2018 20:39]

        List<String> lines = this.ReadFile();

        // Get the Customer Name
        PhoneBill bill = new PhoneBill(lines.get(0));

        // Get the Phone call string
        String strCall = lines.get(1);

        // Match Phone Numbers
        Pattern pattern = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");
        Matcher matcher = pattern.matcher(strCall);

        matcher.find();
        String callerNumber = matcher.group();
        matcher.find();
        String calleeNumber = matcher.group();

        // Match Datetimes
        String reDate = "\\d{1,2}/\\d{1,2}/\\d{4}";
        String reTime = "\\d{1,2}:\\d{2}";

        Pattern p = Pattern.compile(reDate + " " + reTime);
        Matcher m = p.matcher(strCall);

        m.find();
        String startTime = m.group();
        m.find();
        String endTime = m.group();

        PhoneCall call = new PhoneCall(callerNumber, calleeNumber, startTime, endTime);
        bill.addPhoneCall(call);

        return bill;
    }

    private void validateParserPath() throws ParserException
    {
        if (this.filePath == null)
        {
            throw new ParserException("File path missing");
        }

        if (!Files.isRegularFile(this.filePath)) throw new ParserException("File path is not a file: " + this.filePath.toString());
        if (!Files.exists(this.filePath)) throw new ParserException("File path doesn't exist: " + this.filePath.toString());
        if (!Files.isWritable(this.filePath)) throw new ParserException("File path does have write permissions: " + this.filePath.toString());
    }

    private List<String> ReadFile() throws ParserException
    {
        try
        {
            ArrayList<String> lines = new ArrayList<String>();

            Files.lines(this.filePath).forEach(s -> lines.add(s));

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