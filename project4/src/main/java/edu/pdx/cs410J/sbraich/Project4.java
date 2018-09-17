package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.ParserException;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

/**
 * The main class that parses the command line and communicates with the
 * Phone Bill server using REST.
 */
public class Project4 {

    public static final String MISSING_ARGS = "Missing command line arguments";

    static final String README =
            "\nProject 3 README by Steve Braich for CS510J Summer 2018\n" +
                    "This project continues Project 2, implementing dates, sorting and a pretty print feature.\n\n";
    static final String USAGE =
            "usage: java edu.pdx.cs410J.<login-id>.Project3 [options] <args>\n" +
                    "  args are (in this order):\n" +
                    "\tcustomer        Person whose phone bill we’re modeling\n" +
                    "\tcallerNumber    Phone number of caller\n" +
                    "\tcalleeNumber    Phone number of person who was called\n" +
                    "\tstartTime       Date and time (am/pm) call began\n" +
                    "\tendTime         Date and time (am/pm) call ended\n" +
                    "  options are (options may appear in any order):\n" +
                    "\t-pretty file    Pretty print the phone bill to a text file\n" +
                    "\t                or standard out (file -).\n" +
                    "\t-textFile file  Where to read/write the phone bill\n" +
                    "\t-print          Prints a description of the new phone call\n" +
                    "\t-README         Prints a README for this project and exits\n\n";

    /**
     * Main function for Project 4
     * @param args Takes in arguments from the command line
     * @throws Exception Can throw PhonebillException, ParseException, IOException
     */
    public static void main(String[] args) throws Exception
    {
        try
        {
            Cli cli = new Cli(args);

            PhoneBillRestClient client = new PhoneBillRestClient(cli.hostname, cli.portNumber);

            // 1. Show README and exit
            if (cli.readme)
            {
                System.out.println(README + USAGE);
                System.exit(0);
            }
            // 2. Search
            else if (cli.search)
            {
                System.out.println("Search Not implemented");
                System.exit(0);
            }
            // 3. Print all Calls for a Customer
            else if (cli.printCustomer)
            {

                System.exit(0);
            }
            // 4. Add Phone Call
            else
            {
                PhoneBill bill = new PhoneBill(cli.customer);

                // Add the new PhoneCall
                PhoneCall call = new PhoneCall(cli.callerNumber, cli.calleeNumber, cli.startTime, cli.endTime);
                bill.addPhoneCall(call);

                // 3. Print
                if (cli.print)
                {
                    PrettyPrinter pretty = new PrettyPrinter();
                    System.out.println(pretty.getPrettyPrint(bill));
                }

                System.exit(0);
            }
        }
        catch (PhoneBillException e)
        {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Makes sure that the give response has the expected HTTP status code
     * @param code The expected status code
     * @param response The response from the server
     */
    private static void checkResponseCode( int code, HttpRequestHelper.Response response )
    {
        if (response.getCode() != code)
        {
            error(String.format("Expected HTTP code %d, got code %d.\n\n%s", code,
                                response.getCode(), response.getContent()));
        }
    }

    private static void error( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);

        System.exit(1);
    }

    /**
     * Prints usage information for this program and exits
     * @param message An error message to print
     */
    private static void usage( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);
        err.println();
        err.println("usage: java Project4 host port [word] [definition]");
        err.println("  host         Host of web server");
        err.println("  port         Port of web server");
        err.println("  word         Word in dictionary");
        err.println("  definition   Definition of word");
        err.println();
        err.println("This simple program posts words and their definitions");
        err.println("to the server.");
        err.println("If no definition is specified, then the word's definition");
        err.println("is printed.");
        err.println("If no word is specified, all dictionary entries are printed");
        err.println();

        System.exit(1);
    }
}