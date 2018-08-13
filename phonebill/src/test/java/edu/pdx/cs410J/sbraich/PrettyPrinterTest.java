package edu.pdx.cs410J.sbraich;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.ref.Reference;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class PrettyPrinterTest
{

    @Before
    public void setUp() throws Exception
    {
    }

    @After
    public void tearDown() throws Exception
    {
    }

    private Date parseDate(String s)  throws ParseException
    {
        String DATE_TIME_FORMAT = "M/d/yyyy h:mm a";

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.US);
        sdf.setLenient(false);
        Date date = sdf.parse(s);

        return date;
    }

    private String AddMinutesToDate(String s, int minutes) throws ParseException
    {
        String DATE_TIME_FORMAT = "M/d/yyyy h:mm a";

        Date date = parseDate(s);

        // Convert to localDateTime to add minutes
        // LocalDateTime << Instant << Date
        Instant startInstant = Instant.ofEpochMilli(date.getTime());
        LocalDateTime startLdt = LocalDateTime.ofInstant(startInstant, ZoneOffset.UTC);
        LocalDateTime endLdt = startLdt.plusMinutes(minutes);

        // Now convert it to a Date type
        Instant endInstant = endLdt.toInstant(ZoneOffset.UTC);
        Date endDate = Date.from(endInstant);


        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.US);
        String endDateString = sdf.format(endDate);

        return endDateString;
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

    private int GetDateDiffMinutes(String d1, String d2) throws ParseException
    {
        Date startDate = parseDate(d1);; // Set start date
        Date endDate   = parseDate(d2); // Set end date

        long duration  = endDate.getTime() - startDate.getTime();
        int diffInMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(duration);

        return diffInMinutes;
    }

    private List<String> AddPrettyPrintedLine(List<String> lines, String caller, String callee, String start, String end) throws ParseException
    {
        int minutes = GetDateDiffMinutes(start, end);
        String minFormatted = String.format("%5d", minutes);

        String line = " " + caller + "   " + callee + "   " + minFormatted + "   " + PrettyDate(start) + "   " + PrettyDate(end);
        lines.add(line);

        return lines;
    }

    private PhoneBill BuildPrettyPrintedTestLine(List<String> lines, PhoneBill bill, String caller, String callee, String start, int minutes) throws PhoneBillException, ParseException
    {
        String end = AddMinutesToDate(start, minutes);

        lines = AddPrettyPrintedLine(lines, caller, callee, start, end);

        PhoneCall call = new PhoneCall(caller, callee, start, end);
        bill.addPhoneCall(call);

        return bill;
    }

    @Test
    public void getPrettyPrint_Basic() throws PhoneBillException, IOException, ParseException
    {
        String customer = "Steve";
        String caller = "123-123-1234";

        PhoneBill bill = new PhoneBill(customer);

        List<String> expectedLines = new ArrayList<String>();

        expectedLines.add("Customer: " + customer);
        expectedLines.add("");
        expectedLines.add("    Caller         Callee      Minutes      Call Start            Call End");
        expectedLines.add("---------------------------------------------------------------------------------");

        bill = BuildPrettyPrintedTestLine(expectedLines, bill, caller, "111-111-1111", "01/15/2018 07:30 am", 5);
        bill = BuildPrettyPrintedTestLine(expectedLines, bill, caller, "222-222-2222", "08/01/2018 03:10 pm", 10);
        bill = BuildPrettyPrintedTestLine(expectedLines, bill, caller, "333-333-3333", "09/15/2018 09:30 am", 125);
        bill = BuildPrettyPrintedTestLine(expectedLines, bill, caller, "444-444-4444", "10/01/2018 12:01 pm", 1440);
        bill = BuildPrettyPrintedTestLine(expectedLines, bill, caller, "555-555-5555", "12/15/2018 11:05 am", 14400);

        // Array of String of what Pretty Print file should look like
        List<String> hardcodedLines = new ArrayList<String>();
        hardcodedLines.add("Customer: " + customer);
        hardcodedLines.add("Total Minutes: 15980");
        hardcodedLines.add("");
        hardcodedLines.add("    Caller         Callee      Minutes      Call Start            Call End");
        hardcodedLines.add("---------------------------------------------------------------------------------");
        hardcodedLines.add(" 123-123-1234   111-111-1111       5   01/15/2018 07:30 AM   01/15/2018 07:35 AM");
        hardcodedLines.add(" 123-123-1234   222-222-2222      10   08/01/2018 03:10 PM   08/01/2018 03:20 PM");
        hardcodedLines.add(" 123-123-1234   333-333-3333     125   09/15/2018 09:30 AM   09/15/2018 11:35 AM");
        hardcodedLines.add(" 123-123-1234   444-444-4444    1440   10/01/2018 12:01 PM   10/02/2018 12:01 PM");
        hardcodedLines.add(" 123-123-1234   555-555-5555   14400   12/15/2018 11:05 AM   12/25/2018 11:05 AM");

        String totalMinutes = "Total Minutes: " + bill.getTotalMinutes();
        expectedLines.add(1, totalMinutes);

        PrettyPrinter pretty = new PrettyPrinter();
        String prettyOutput = pretty.getPrettyPrint(bill);

        String expectedOutput = String.join("\n", expectedLines);
        String hardcodedOutput = String.join("\n", hardcodedLines);

        assertEquals(expectedOutput, prettyOutput);
        assertEquals(hardcodedOutput, prettyOutput);
    }

    @Test
    public void getPrettyPrint_SortTest_Uniq() throws PhoneBillException, IOException, ParseException
    {
        String customer = "Steve";
        String caller = "123-123-1234";

        PhoneBill bill = new PhoneBill(customer);

        List<String> expectedLines = new ArrayList<String>();

        expectedLines.add("Customer: " + customer);
        expectedLines.add("");
        expectedLines.add("    Caller         Callee      Minutes      Call Start            Call End");
        expectedLines.add("---------------------------------------------------------------------------------");

        bill = BuildPrettyPrintedTestLine(expectedLines, bill, caller, "444-444-4444", "04/01/2018 12:01 pm", 1440);
        bill = BuildPrettyPrintedTestLine(expectedLines, bill, caller, "333-333-3333", "03/01/2018 09:30 am", 125);
        bill = BuildPrettyPrintedTestLine(expectedLines, bill, caller, "555-555-5555", "05/01/2018 11:05 am", 14400);
        bill = BuildPrettyPrintedTestLine(expectedLines, bill, caller, "111-111-1111", "01/01/2018 07:30 am", 5);
        bill = BuildPrettyPrintedTestLine(expectedLines, bill, caller, "222-222-2222", "02/01/2018 03:10 pm", 10);

        // Array of String of what Pretty Print file should look like
        List<String> hardcodedLines = new ArrayList<String>();
        hardcodedLines.add("Customer: " + customer);
        hardcodedLines.add("Total Minutes: 15980");
        hardcodedLines.add("");
        hardcodedLines.add("    Caller         Callee      Minutes      Call Start            Call End");
        hardcodedLines.add("---------------------------------------------------------------------------------");
        hardcodedLines.add(" 123-123-1234   111-111-1111       5   01/01/2018 07:30 AM   01/01/2018 07:35 AM");
        hardcodedLines.add(" 123-123-1234   222-222-2222      10   02/01/2018 03:10 PM   02/01/2018 03:20 PM");
        hardcodedLines.add(" 123-123-1234   333-333-3333     125   03/01/2018 09:30 AM   03/01/2018 11:35 AM");
        hardcodedLines.add(" 123-123-1234   444-444-4444    1440   04/01/2018 12:01 PM   04/02/2018 12:01 PM");
        hardcodedLines.add(" 123-123-1234   555-555-5555   14400   05/01/2018 11:05 AM   05/11/2018 11:05 AM");

        String totalMinutes = "Total Minutes: " + bill.getTotalMinutes();
        expectedLines.add(1, totalMinutes);

        PrettyPrinter pretty = new PrettyPrinter();
        String prettyOutput = pretty.getPrettyPrint(bill);

        String expectedOutput = String.join("\n", expectedLines);
        String hardcodedOutput = String.join("\n", hardcodedLines);

        assertNotEquals(expectedOutput, prettyOutput);
        assertEquals(hardcodedOutput, prettyOutput);
    }

    @Test
    public void getPrettyPrint_SortTest_Dupes() throws PhoneBillException, IOException, ParseException
    {
        String customer = "Steve";
        String caller = "123-123-1234";

        PhoneBill bill = new PhoneBill(customer);

        List<String> expectedLines = new ArrayList<String>();

        expectedLines.add("Customer: " + customer);
        expectedLines.add("");
        expectedLines.add("    Caller         Callee      Minutes      Call Start            Call End");
        expectedLines.add("---------------------------------------------------------------------------------");

        bill = BuildPrettyPrintedTestLine(expectedLines, bill, caller, "444-444-4444", "04/01/2018 12:01 pm", 1440);
        bill = BuildPrettyPrintedTestLine(expectedLines, bill, caller, "555-555-5555", "05/01/2018 11:05 am", 14400);
        bill = BuildPrettyPrintedTestLine(expectedLines, bill, caller, "222-222-2222", "01/01/2018 03:10 pm", 10);
        bill = BuildPrettyPrintedTestLine(expectedLines, bill, caller, "111-111-1111", "01/01/2018 07:30 am", 5);
        bill = BuildPrettyPrintedTestLine(expectedLines, bill, caller, "333-333-3333", "03/01/2018 09:30 am", 125);

        // Array of String of what Pretty Print file should look like
        List<String> hardcodedLines = new ArrayList<String>();
        hardcodedLines.add("Customer: " + customer);
        hardcodedLines.add("Total Minutes: 15980");
        hardcodedLines.add("");
        hardcodedLines.add("    Caller         Callee      Minutes      Call Start            Call End");
        hardcodedLines.add("---------------------------------------------------------------------------------");
        hardcodedLines.add(" 123-123-1234   111-111-1111       5   01/01/2018 07:30 AM   01/01/2018 07:35 AM");
        hardcodedLines.add(" 123-123-1234   222-222-2222      10   01/01/2018 03:10 PM   01/01/2018 03:20 PM");
        hardcodedLines.add(" 123-123-1234   333-333-3333     125   03/01/2018 09:30 AM   03/01/2018 11:35 AM");
        hardcodedLines.add(" 123-123-1234   444-444-4444    1440   04/01/2018 12:01 PM   04/02/2018 12:01 PM");
        hardcodedLines.add(" 123-123-1234   555-555-5555   14400   05/01/2018 11:05 AM   05/11/2018 11:05 AM");

        String totalMinutes = "Total Minutes: " + bill.getTotalMinutes();
        expectedLines.add(1, totalMinutes);

        PrettyPrinter pretty = new PrettyPrinter();
        String prettyOutput = pretty.getPrettyPrint(bill);

        String expectedOutput = String.join("\n", expectedLines);
        String hardcodedOutput = String.join("\n", hardcodedLines);

        assertNotEquals(expectedOutput, prettyOutput);
        assertEquals(hardcodedOutput, prettyOutput);
    }

    @Test
    public void PrettyPrintedDump_Basic() throws PhoneBillException, IOException, ParseException
    {
        // TODO: USE FILE MOCKING!!!
        String customer = "Steve";
        String filePath = "prettyprint_test.txt";
        String caller = "123-123-1234";

        PhoneBill bill = new PhoneBill(customer, filePath);

        File file = new File(bill.getFilePath().toString());

        try
        {
            List<String> expectedLines = new ArrayList<String>();

            expectedLines.add("Customer: " + customer);
            expectedLines.add("");
            expectedLines.add("    Caller         Callee      Minutes      Call Start            Call End");
            expectedLines.add("---------------------------------------------------------------------------------");

            bill = BuildPrettyPrintedTestLine(expectedLines, bill, caller, "111-111-1111", "01/15/2018 07:30 am", 5);
            bill = BuildPrettyPrintedTestLine(expectedLines, bill, caller, "222-222-2222", "08/01/2018 03:10 pm", 10);
            bill = BuildPrettyPrintedTestLine(expectedLines, bill, caller, "333-333-3333", "09/15/2018 09:30 am", 125);
            bill = BuildPrettyPrintedTestLine(expectedLines, bill, caller, "444-444-4444", "10/01/2018 12:01 pm", 1440);
            bill = BuildPrettyPrintedTestLine(expectedLines, bill, caller, "555-555-5555", "12/15/2018 11:05 am", 14400);

            // Array of String of what Pretty Print file should look like
            List<String> hardcodedLines = new ArrayList<String>();
            hardcodedLines.add("Customer: " + customer);
            hardcodedLines.add("Total Minutes: 15980");
            hardcodedLines.add("");
            hardcodedLines.add("    Caller         Callee      Minutes      Call Start            Call End");
            hardcodedLines.add("---------------------------------------------------------------------------------");
            hardcodedLines.add(" 123-123-1234   111-111-1111       5   01/15/2018 07:30 AM   01/15/2018 07:35 AM");
            hardcodedLines.add(" 123-123-1234   222-222-2222      10   08/01/2018 03:10 PM   08/01/2018 03:20 PM");
            hardcodedLines.add(" 123-123-1234   333-333-3333     125   09/15/2018 09:30 AM   09/15/2018 11:35 AM");
            hardcodedLines.add(" 123-123-1234   444-444-4444    1440   10/01/2018 12:01 PM   10/02/2018 12:01 PM");
            hardcodedLines.add(" 123-123-1234   555-555-5555   14400   12/15/2018 11:05 AM   12/25/2018 11:05 AM");

            String totalMinutes = "Total Minutes: " + bill.getTotalMinutes();
            expectedLines.add(1, totalMinutes);

            String expectedOutput = String.join("\n", expectedLines);
            String hardcodedOutput = String.join("\n", hardcodedLines);

            PrettyPrinter pretty = new PrettyPrinter();
            pretty.dump(bill);

            assertTrue(file.exists());
            assertTrue(!file.isDirectory());

            List<String> outputLines = Files.readAllLines(file.toPath(), Charset.defaultCharset() );

            String prettyOutput = String.join("\n", outputLines);

            assertEquals(expectedOutput, prettyOutput);
            assertEquals(hardcodedOutput, prettyOutput);
        }
        finally
        {
            file.delete();
        }

    }

    public  boolean equalLists(List<String> a, List<String> b)
    {
        // Check for sizes and nulls

        if (a == null && b == null) return true;


        if ((a == null && b!= null) || (a != null && b== null) || (a.size() != b.size()))
        {
            return false;
        }

        return a.equals(b);
    }

    @Test(expected = IOException.class)
    public void PrettyPrinter_ReadOnly() throws PhoneBillException, IOException
    {
        File f = new File("readonly.txt");
        f.createNewFile();

        try
        {
            f.setReadOnly();

            PhoneBill bill = new PhoneBill("Steve", "readonly.txt");
            PhoneCall call = new PhoneCall("123-123-1234", "123-123-1234",
                    "1/15/2018 7:39 AM", "1/15/2018 8:39 PM");

            bill.addPhoneCall(call);

            PrettyPrinter pretty = new PrettyPrinter();
            pretty.dump(bill);
        }
        finally
        {
            f.setWritable(true);
            f.delete();
        }
    }

    @Test(expected = IOException.class)
    public void PrettyPrinter_InvalidPath() throws PhoneBillException, IOException
    {
        PhoneBill bill = new PhoneBill("Steve", "/nonexistant/readonly.txt");
        PhoneCall call = new PhoneCall("123-123-1234", "123-123-1234",
                "1/15/2018 11:39 AM", "1/15/2018 10:39 pm");

        bill.addPhoneCall(call);

        PrettyPrinter pretty = new PrettyPrinter();
        pretty.dump(bill);
    }
}