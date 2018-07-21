package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.AbstractPhoneBill;


/// The main class for the CS410J Phone Bill Project
public class Project2
{
    static final String README = "This is a PhoneBill app. Blah Blah.";

    /// Expecting command line arguments
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
            PhoneBill bill = new PhoneBill(cli.customer);
            bill.addPhoneCall(call);

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
    }


}