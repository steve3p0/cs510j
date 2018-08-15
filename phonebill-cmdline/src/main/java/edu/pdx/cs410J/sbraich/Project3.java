package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.ParserException;

import java.io.IOException;

/**
 * The main class for the CS510J Phone Bill Project
 */
public class Project3
{
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
     * Main function for Project 3
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
            // 2. Add Phone Call
            else
            {
                PhoneBill bill = new PhoneBill(cli.customer);

                // Check if this bill has a file that exists with it
                // Load it into the PhoneBill object if it does
                if (cli.textFile)
                {
                    bill.setFilePath(cli.filePath.toString());
                    TextParser parser = new TextParser(cli.filePath, bill.getCustomer());

                    if (parser.fileExists())
                    {
                        //System.out.println("parser.fileExists()");
                        bill = parser.parse();
                    }
                }

                // Add the new PhoneCall
                PhoneCall call = new PhoneCall(cli.callerNumber, cli.calleeNumber, cli.startTime, cli.endTime);
                bill.addPhoneCall(call);

                // Option C: Write output to a FILE
                if (cli.textFile)
                {
                    TextDumper dumper = new TextDumper();
                    dumper.dump(bill);
                }

                // OPTIONS:

                // Option A: Pretty Print to STDOUT
                if (cli.prettyStdout)
                {
                    PrettyPrinter pretty = new PrettyPrinter();
                    String stdout = pretty.getPrettyPrint(bill);

                    System.out.println(stdout);
                }

                // Option B: Pretty Print to a FILE
                else if (cli.prettyFile)
                {
                    PrettyPrinter pretty = new PrettyPrinter();
                    bill.setFilePath(cli.prettyPath.toString());
                    pretty.dump(bill);
                }

                // Option D. Print a description of the new phone call to STDOUT
                if (cli.print)
                {
                    System.out.println(call.toString());
                    System.exit(0);
                }

                System.exit(0);
            }
        }
        catch (PhoneBillException e)
        {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        catch (ParserException e)
        {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}