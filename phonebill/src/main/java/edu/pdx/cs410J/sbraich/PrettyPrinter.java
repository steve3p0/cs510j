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

/// PRETTY PRINTER

/// public class TextDumper implements PhoneBillDumper<T extends AbstractPhoneBill>
public class PrettyPrinter implements PhoneBillDumper<PhoneBill>
{
    /// Impelements dump method of PhoneBillDumper method
    public void dump(PhoneBill bill) throws IOException
    {
        try
        {
            Collection<PhoneCall> calls = bill.getPhoneCalls();

            Path path = bill.getFilePath();
            String callStr = calls.toString();
            String customer = "Customer: " + bill.getCustomer();
            String totalMinutes = "Total Minutes: " + bill.getTotalMinutes();

            String header = "    Caller         Callee      Minutes      Call Start            Call End";
            String divider = "---------------------------------------------------------------------------------";

            List<String> lines = new ArrayList<String>();
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

            this.CreateDirFromFilePath(path);
            this.validateFilePath(path);
            Files.write(path, lines, Charset.forName("UTF-8"));
        }
        catch (ParseException e)
        {
            throw new IOException(e.getMessage());
        }
    }

    private String PrettyDate(String d)  throws ParseException
    {
        String PARSE_DATETIME_PATTERN = "M/d/yyyy h:mm a";
        String PRINT_DATETIME_PATTERN = "MM/dd/yyyy hh:mm a";

        SimpleDateFormat sdf = new SimpleDateFormat(PARSE_DATETIME_PATTERN, Locale.US);
        sdf.setLenient(false);
        Date date = sdf.parse(d);

        String formattedDate = new SimpleDateFormat(PRINT_DATETIME_PATTERN).format(date);

        return formattedDate;
    }

    private String FormatPrettyPrintedLine(String caller, String callee, String start, String end) throws ParseException
    {
        int minutes = GetDateDiffMinutes(start, end);
        String minFormatted = String.format("%5d", minutes);

        String line = " " + caller + "   " + callee + "   " + minFormatted + "   " + PrettyDate(start) + "   " + PrettyDate(end);

        return line;
    }

    private int GetDateDiffMinutes(String d1, String d2) throws ParseException
    {
        Date startDate = parseDate(d1);; // Set start date
        Date endDate   = parseDate(d2); // Set end date

        long duration  = endDate.getTime() - startDate.getTime();

        //long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        int diffInMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(duration);
        //long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);

        return diffInMinutes;
    }

    private Date parseDate(String s)  throws ParseException
    {
        String DATE_TIME_FORMAT = "M/d/yyyy h:mm a";

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.US);
        sdf.setLenient(false);
        Date date = sdf.parse(s);

        return date;
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

