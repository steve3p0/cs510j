package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.ParserException;

import java.io.IOException;

/// The main class for the CS410J Phone Bill Project
public class Project3
{
    static final String README = "This is a PhoneBill app. Blah Blah.";

    /// Expecting command line arguments
    /// We have moved most of the command line arg logic into its own class
    public static void main(String[] args) throws Exception
    {
        // What can you do from main?
        //
        //  Either:
        //    1. Show README
        //          OR...
        //    2. Add Phone Call
        //
        //  If you chose #2, there are options:
        //    A. Pretty Print to STDOUT
        //    B. Pretty Print to a FILE
        //    C. Write output to a FILE
        //    D. Print a description of the new phone call to STDOUT

        try
        {
            Cli cli = new Cli(args);

            // 1. Show README and exit
            if (cli.readme)
            {
                System.out.println(README);
                System.exit(0);
            }
            // 2. Add Phone Call
            else
            {
                PhoneCall call = new PhoneCall(cli.callerNumber, cli.calleeNumber, cli.startTime, cli.endTime);

                // OPTIONS:

                // Option A: Pretty Print to STDOUT
                if (cli.prettyStdout)
                {
                    PhoneBill bill = new PhoneBill(cli.customer);
                    bill.addPhoneCall(call);

                    PrettyPrinter pretty = new PrettyPrinter();
                    String stdout = pretty.getPrettyPrint(bill);

                    System.out.println(stdout);
                }
                // Option B: Pretty Print to a FILE
                else if (cli.prettyFile)
                {
                    PhoneBill bill = new PhoneBill(cli.customer, cli.filePath.toString());
                    bill.addPhoneCall(call);

                    PrettyPrinter pretty = new PrettyPrinter();
                    pretty.dump(bill);
                }

                // Option C: Write output to a FILE
                if (cli.textFile)
                {
                    PhoneBill readbill = new PhoneBill(cli.customer, cli.filePath.toString());

                    TextParser parser = new TextParser(cli.filePath, readbill.getCustomer());
                    if (parser.fileExists())
                    {
                        readbill = parser.parse();
                    }

                    PhoneBill writeBill = new PhoneBill(cli.customer, cli.filePath.toString());
                    writeBill.addPhoneCall(call);
                    TextDumper dumper = new TextDumper();
                    dumper.dump(writeBill);
                }
                // NOT Option B: Don't Write output to a FILE
                else
                {
                    PhoneBill bill = new PhoneBill(cli.customer);
                    bill.addPhoneCall(call);
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