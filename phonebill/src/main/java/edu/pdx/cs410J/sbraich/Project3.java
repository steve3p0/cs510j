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
        // Refer to one of Dave's classes so that we can be sure it is on the classpath
        try
        {
            Cli cli = new Cli(args);

            if (cli.readme)
            {
                System.out.println(README);
                System.exit(0);
            }

            PhoneCall call = new PhoneCall(cli.callerNumber, cli.calleeNumber, cli.startTime, cli.endTime);

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
            else
            {
                PhoneBill bill = new PhoneBill(cli.customer);
                bill.addPhoneCall(call);
            }

            if (cli.print)
            {
                System.out.println(call.toString());
                System.exit(0);
            }

            System.exit(0);
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