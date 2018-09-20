package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.io.PrintStream;

/**
 * The main class that parses the command line and communicates with the
 * Phone Bill server using REST.
 */
public class Project4
{

    static final String README =
            "\nProject 4 README by Steve Braich for CS510J Summer 2018\n" +
                    "This project continues Project 3, exposing it as a web service.\n\n";
    static final String USAGE =
            "usage: java edu.pdx.cs410J.<login-id>.Project4 [options] <args>\n" +
                    "  args are (in this order):\n" +
                    "\tcustomer        Person whose phone bill we’re modeling\n" +
                    "\tcallerNumber    Phone number of caller\n" +
                    "\tcalleeNumber    Phone number of person who was called\n" +
                    "\tstartTime       Date and time (am/pm) call began\n" +
                    "\tendTime         Date and time (am/pm) call ended\n" +
                    "  options are (options may appear in any order):\n" +
                    "\t-host hostname  Host computer on which the server runs\n" +
                    "\t-port port      Port on which the server is listening\n" +
                    "\t-search         Phone calls should be searched for\n" +
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

            // 1. Show README and exit
            if (cli.readme)
            {
                System.out.println(README + USAGE);
                System.exit(0);
            }
            // 2. Search
            else if (cli.search)
            {
                PhoneBillRestClient client = new PhoneBillRestClient(cli.hostname, cli.portNumber);

                String message = client.searchPhoneCalls(cli.customer, cli.startTime, cli.endTime);
                System.out.println(message);
                System.exit(0);
            }
            // 3. Get all calls for customer
            else if (cli.customer != null &&
                    cli.callerNumber == null && cli.calleeNumber == null &&
                    cli.startTime == null && cli.endTime == null)
            {
                PhoneBillRestClient client = new PhoneBillRestClient(cli.hostname, cli.portNumber);
                String message = client.getPrettyPhoneBill(cli.customer);
                System.out.println(message);
                System.exit(0);
            }
            // 4. Add Phone Call
            else
            {
                PhoneBillRestClient client = new PhoneBillRestClient(cli.hostname, cli.portNumber);

                PhoneCall call = new PhoneCall(cli.callerNumber, cli.calleeNumber, cli.startTime, cli.endTime);
                client.addPhoneCall(cli.customer, call);

                if (cli.print)
                {
                    System.out.println(call.toString());
                }
                System.exit(0);
            }
        }
        catch (NoSuchPhoneBillException e)
        {
            System.out.println(e.getMessage());
            System.exit(0);
        }
        catch (PhoneBillException e)
        {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}