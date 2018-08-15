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
    protected Boolean pretty;
    protected Boolean prettyFile;
    protected Boolean prettyStdout;
    protected Boolean print;
    protected Boolean readme;
    protected Boolean textFile;
    protected Path filePath;
    protected Path prettyPath;

    /// Constructor for CLI - Processes Command Line Arguments
    public Cli(String[] args) throws PhoneBillException
    {
        this.pretty = Arrays.stream(args).anyMatch(s -> s.equals("-pretty"));
        this.prettyStdout = Arrays.stream(args).anyMatch(s -> s.equals("-"));
        this.prettyFile = (this.pretty && !this.prettyStdout);

        this.readme = Arrays.stream(args).anyMatch(s -> s.equals("-README"));
        this.print = Arrays.stream(args).anyMatch(s -> s.equals("-print"));
        this.textFile = Arrays.stream(args).anyMatch(s -> s.equals("-textFile"));

        arguments = new ArrayList<String>(Arrays.asList(args));

        int options = 0;


        if (this.pretty)
        {
            options++;
            int i = arguments.indexOf("-pretty");

            if (i + 1 > arguments.size() - 1)
            {
                throw new PhoneBillException("Missing Pretty filepath argument");
            }

            if (this.prettyFile)
            {
                options++;
                this.prettyPath = Paths.get(arguments.get(i + 1));
                arguments.remove(this.prettyPath.toString());
            }

            if (this.prettyStdout)
            {
                options++;
                arguments.remove("-");
            }
        }

        if (this.textFile)
        {
            options++;
            options++;

            int i = arguments.indexOf("-textFile");

            if (i + 1 > arguments.size() - 1)
            {
                throw new PhoneBillException("Missing Text filepath argument");
            }

            this.filePath = Paths.get(arguments.get(i + 1));
            arguments.remove(this.filePath.toString());
        }


        if (this.print)
        {
            options++;
        }

        if (this.readme)
        {
            if (arguments.size() != 1)
            {
                throw new PhoneBillException("Missing command line arguments");
            }

            return;
        }

        // Remove options if they are in the arguments - we already set are class members
        arguments.remove("-pretty");
        arguments.remove("-");
        arguments.remove("-prettyFile");

        arguments.remove("-textFile");
        arguments.remove("-print");
        arguments.remove("-README");

        if (arguments.size() == 9)
        {
            this.customer = arguments.get(0);
            this.callerNumber = arguments.get(1);
            this.calleeNumber = arguments.get(2);
            this.startTime = arguments.get(3) + " " + arguments.get(4) + " " + arguments.get(5);
            this.endTime = arguments.get(6) + " " + arguments.get(7) + " " + arguments.get(8);
        }
        else if (arguments.size() < 9)
        {
            throw new PhoneBillException("Missing command line arguments");
        }
        else if (arguments.size() > 9)
        {
            throw new PhoneBillException("Too many command line arguments");
        }
    }
}
