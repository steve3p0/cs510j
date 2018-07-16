package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.AbstractPhoneCall;

/// The Class that manages a phone call
public class PhoneCall extends AbstractPhoneCall
{
    private final String callerNumber;
    private final String calleeNumber;
    private final String startTime;
    private final String endTime;

    public PhoneCall(String callerNum, String calleeNum, String start, String end)
    {
        this.callerNumber = callerNum;
        this.calleeNumber = calleeNum;
        this.startTime = start;
        this.endTime = end;
    }

    @Override
    public String getCaller()
    {
        return this.callerNumber;
    }

    @Override
    public String getCallee()
    {
        return this.calleeNumber;
    }

    @Override
    public String getStartTimeString()
    {
        return this.startTime;
    }

    @Override
    public String getEndTimeString()
    {
        return this.endTime;
    }
}
