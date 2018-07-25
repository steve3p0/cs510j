package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.AbstractPhoneCall;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.time.LocalDate;

/// The Class that manages a phone call
public class PhoneCall extends AbstractPhoneCall
{
    private final String callerNumber;
    private final String calleeNumber;
    private final String startTime;
    private final String endTime;

    /// Constructor for PhoneCall = Takes numbers and datetimes as args
    public PhoneCall(String callerNum, String calleeNum, String start, String end) throws PhoneBillException
    {
        this.callerNumber = callerNum;
        this.calleeNumber = calleeNum;
        this.startTime = start;
        this.endTime = end;

        this.validate();
    }

    /// Overrides getCaller method of AbstractPhoneCall method
    @Override
    public String getCaller()
    {
        return this.callerNumber;
    }

    /// Overrides getCallee method of AbstractPhoneCall method
    @Override
    public String getCallee()
    {
        return this.calleeNumber;
    }

    /// Overrides getStartTimeString method of AbstractPhoneCall method
    @Override
    public String getStartTimeString()
    {
        return this.startTime;
    }

    /// Overrides getEndTimeString method of AbstractPhoneCall method
    @Override
    public String getEndTimeString()
    {
        return this.endTime;
    }

    public Date getStartTime() //throws PhoneBillException
    {
        try
        {
            Date date = new SimpleDateFormat("d/M/yyyy H:mm").parse(this.startTime);

            return date;
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            return null;
            //throw new PhoneBillException(e.getMessage());
        }
    }

    public Date getEndTime() //throws PhoneBillException
    {
        try
        {
            Date date = new SimpleDateFormat("d/M/yyyy H:mm").parse(this.endTime);

            return date;
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
            return null;
            //throw new PhoneBillException(e.getMessage());
        }
    }

    // Validation Methods
    /// Validate Method for PhoneCall - Calls all private validate methods
    private void validate() throws PhoneBillException
    {
        this.validatePhoneNumber(this.callerNumber);
        this.validatePhoneNumber(this.calleeNumber);

        this.validateDate(this.startTime);
        this.validateDate(this.endTime);
    }

    /// Validate Date
    public void validateDate(String datetime) throws PhoneBillException
    {
        //1/15/2018 19:39
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("M/d/yyyy HH:mm");
            format.setLenient(false);
            Date date = format.parse(datetime);

            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int year  = localDate.getYear();

            if (year < 1000 || year > 2099)
            {
                throw new PhoneBillException("'" + datetime +"' is not a valid date/time");
            }

        }
        catch (ParseException e)
        {
            throw new PhoneBillException("'" + datetime +"' is not a valid date/time");
        }
    }

    /// Validate Phone Number
    public void validatePhoneNumber(String phoneNumber) throws PhoneBillException
    {
        //nnn-nnn-nnnn
        //(?:\d{3}-){2}\d{4}
        //String pattern = "^/d(?:-/d{3}){3}/d$";
        String pattern = "[0-9]{3}-[0-9]{3}-[0-9]{4}";

        if (!phoneNumber.matches(pattern))
        {
            throw new PhoneBillException("'" + phoneNumber +"' is not a valid phone number");
        }
    }
}
