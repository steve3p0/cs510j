package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.PhoneBillDumper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Class that manages the Pretty Printing of a PhoneBill
 */
public class PrettyPrinter implements PhoneBillDumper<PhoneBill>
{
    private String filePath = null;

    /**
     * Pretty prints a PhoneBill object to a file
     * @param bill The phone bill to be written to a file
     * @throws IOException thrown if file I/O errors occur
     */
    public void dump(PhoneBill bill) throws IOException
    {
        try
        {
            List<String> lines = this.formatPrettyPrint(bill);

            Path path = bill.getFilePath();
            this.CreateDirFromFilePath(path);
            this.validateFilePath(path);
            Files.write(path, lines, Charset.forName("UTF-8"));
        }
        catch (ParseException e)
        {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Converts a List of strings that represents a PhoneBill into one string
     * @param bill The Phonebill to be converted to a string
     * @return String representation of a Phonebill
     * @throws ParseException thrown if the dates can't be parsed.
     */
    public String getPrettyPrint(PhoneBill bill) throws ParseException
    {
        List<String> lines = this.formatPrettyPrint(bill);

        return String.join("\n", lines);
    }

    /**
     * Added the customer name, total minutes of phone calls and each
     * phone call to a list of strings.
     * @param bill The PhoneBill to be converted to a string
     * @return List of strings presententing a phone bill
     * @throws ParseException thrown if the dates can't be parsed.
     */
    private List<String> formatPrettyPrint(PhoneBill bill) throws ParseException
    {
        Collection<PhoneCall> calls = bill.getPhoneCalls();

        String customer = "Customer: " + bill.getCustomer();
        String totalMinutes = "Total Minutes: " + bill.getTotalMinutes();

        String header = "    Caller         Callee      Minutes      Call Start            Call End";
        String divider = "---------------------------------------------------------------------------------";

        List<String> lines = new ArrayList<>();
        lines.add(customer);
        lines.add(totalMinutes);
        lines.add("");
        lines.add(header);
        lines.add(divider);

        for (PhoneCall call : calls)
        {
            String line = FormatPrettyPrintedLine(call.getCaller(), call.getCallee(), call.getStartTimeString(), call.getEndTimeString());
            lines.add(line);
        }

        return lines;

    }

    /**
     * Prints a string in a perferred 'pretty' format
     * @param d Date of the string to be formatted.
     * @return String in a pretty format
     * @throws ParseException thrown if date can't be parsed
     */
    private String PrettyDate(String d) throws ParseException
    {
        String PARSE_DATETIME_PATTERN = "M/d/yyyy h:mm a";
        String PRINT_DATETIME_PATTERN = "MM/dd/yyyy hh:mm a";

        SimpleDateFormat sdf = new SimpleDateFormat(PARSE_DATETIME_PATTERN, Locale.US);
        sdf.setLenient(false);
        Date date = sdf.parse(d);

        return new SimpleDateFormat(PRINT_DATETIME_PATTERN).format(date);
    }

    /**
     * Formats a phone call in a pretty format
     * @param caller String representing the caller
     * @param callee String representing the callee
     * @param start String representing the start date/time
     * @param end String representing the end date/time
     * @return Pretty printed string of the call.
     * @throws ParseException thrown if the dates can't be parsed
     */
    private String FormatPrettyPrintedLine(String caller, String callee, String start, String end) throws ParseException
    {
        int minutes = GetDateDiffMinutes(start, end);
        String minFormatted = String.format("%5d", minutes);

        return " " + caller + "   " + callee + "   " + minFormatted + "   " + PrettyDate(start) + "   " + PrettyDate(end);
    }

    /**
     * Get the duration in minutes of a call
     * @param d1 String of start date
     * @param d2 String of end date
     * @return int reprsentating number of minutes of a call
     * @throws ParseException thrown thrown if the dates can't be parsed
     */
    private int GetDateDiffMinutes(String d1, String d2) throws ParseException
    {
        Date startDate = parseDate(d1); // Set start date
        Date endDate   = parseDate(d2); // Set end date

        long duration  = endDate.getTime() - startDate.getTime();

        return (int) TimeUnit.MILLISECONDS.toMinutes(duration);
    }

    /**
     * Parse a string into a date
     * @param s String representation of a date
     * @return Date object
     * @throws ParseException thrown if the date string can't be parsed
     */
    private Date parseDate(String s)  throws ParseException
    {
        String DATE_TIME_FORMAT = "M/d/yyyy h:mm a";

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.US);
        sdf.setLenient(false);
        return sdf.parse(s);
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

