package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.AbstractPhoneCall;

import java.util.Date;
import java.text.SimpleDateFormat;

/// The Class that manages a phone call
public class PhoneCall extends AbstractPhoneCall
{
    private final String callerNumber;
    private final String calleeNumber;
    private final String startTime;
    private final String endTime;

    /// Constructor for PhoneCall = Takes numbers and datetimes as args
    public PhoneCall(String callerNum, String calleeNum, String start, String end)
    {
        this.callerNumber = callerNum;
        this.calleeNumber = calleeNum;
        this.startTime = start;
        this.endTime = end;
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
}
