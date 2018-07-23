package edu.pdx.cs410J.sbraich;


import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.io.*;
import java.util.Date;
import java.util.Arrays;
import java.util.ArrayList;
import java.nio.file.*;

/// The class that manages the command line interface
public class Cli
{
    public ArrayList<String> arguments;

    public String customer;
    public String callerNumber;
    public String calleeNumber;
    public String startTime;
    public String endTime;

    // Optional Arguments
    public Boolean print;
    public Boolean readme;
    public Boolean textFile;
    public Path filePath;

    public void validatePath() throws PhoneBillException
    {
        File file = new File(this.filePath.toString());
        if (!file.isDirectory())
        {
            throw new PhoneBillException("Filepath is invalid: " + this.filePath.toString());
        }
    }

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

        arguments.remove("Project2");
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
            throw new PhoneBillException("Missing command line argments");
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

        this.Validate();
    }

    private Boolean Validate() throws PhoneBillException
    {
        if (this.customer == null || this.customer.isEmpty())
        {
            throw new PhoneBillException("Missing customer name");
        }

        this.ValidatePhoneNumber(this.callerNumber);
        this.ValidatePhoneNumber(this.calleeNumber);

        this.ValidateDate(this.startTime);
        this.ValidateDate(this.endTime);
        return true;
    }

    private void ValidateDate(String date) throws PhoneBillException
    {
        //1/15/2018 19:39
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            format.parse(date);
        }
        catch (ParseException e)
        {
            throw new PhoneBillException("'" + date +"' is not a valid date/time.");
        }
    }

    private void ValidatePhoneNumber(String phoneNumber) throws PhoneBillException
    {
        //nnn-nnn-nnnn
        //(?:\d{3}-){2}\d{4}
        //String pattern = "^/d(?:-/d{3}){3}/d$";
        String pattern = "[0-9]{3}-[0-9]{3}-[0-9]{4}";

        if (!phoneNumber.matches(pattern))
        {
            throw new PhoneBillException("'" + phoneNumber +"' is not a valid phone number.");
        }
    }

    public void Iterate()
    {
        //Project1 customer callerNumber calleeNumber startTime endTime -print -README

        for (String arg : arguments)
        {
            System.out.println(arg);
        }

    }

    public String ToString()
    {
        //Project1 customer callerNumber calleeNumber startTime endTime -print -README

        String show = "customer: " + customer +
                      "\ncallerNumber: " + callerNumber +
                      "\ncalleeNumber: " + calleeNumber +
                      "\nstartTime: " + startTime +
                      "\nendTime: " + endTime;
        return show;
    }
}
