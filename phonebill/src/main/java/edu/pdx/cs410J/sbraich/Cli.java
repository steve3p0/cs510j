package edu.pdx.cs410J.sbraich;

import java.util.Arrays;
import java.util.ArrayList;
import java.nio.file.*;

/// The class that manages the command line interface
public class Cli
{
    private ArrayList<String> arguments;

    protected String customer;
    protected String callerNumber;
    protected String calleeNumber;
    protected String startTime;
    protected String endTime;

    // Optional Arguments
    protected Boolean print;
    protected Boolean readme;
    protected Boolean textFile;
    protected Path filePath;

    /// Constructor for CLI - Processes Command Line Arguments
    public Cli(String[] args) throws PhoneBillException
    {
        this.readme = Arrays.stream(args).anyMatch(s -> s.equals("-README"));
        this.print = Arrays.stream(args).anyMatch(s -> s.equals("-print"));
        this.textFile = Arrays.stream(args).anyMatch(s -> s.equals("-textFile"));

        arguments = new ArrayList<String>(Arrays.asList(args));

        if (this.textFile)
        {
            int i = arguments.indexOf("-textFile");

            if (i + 1 > arguments.size() - 1)
            {
                throw new PhoneBillException("Missing Text filepath argument");
            }

            this.filePath = Paths.get(arguments.get(i + 1));
            arguments.remove(this.filePath.toString());
        }

        // Remove options if they are in the arguments - we already set are class members
        arguments.remove("-textFile");
        arguments.remove("-print");
        arguments.remove("-README");

        if (this.readme)
        {
            if (arguments.size() != 0)
            {
                throw new PhoneBillException("Too many command line arguments");
            }
            return;
        }

        if (arguments.size() < 7)
        {
            throw new PhoneBillException("Missing command line arguments");
        }

        if (arguments.size() > 7)
        {
            throw new PhoneBillException("Too many command line arguments");
        }

        this.customer = arguments.get(0);
        this.callerNumber = arguments.get(1);
        this.calleeNumber = arguments.get(2);
        this.startTime = arguments.get(3) + " " + arguments.get(4);
        this.endTime = arguments.get(5) + " " + arguments.get(6);
    }
}
