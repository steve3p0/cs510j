package edu.pdx.cs410J.sbraich.client;

import edu.pdx.cs410J.AbstractPhoneCall;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.regexp.shared.MatchResult;

import java.util.Date;

/**
 * The Class that manages a phone call
 */
public class PhoneCall extends AbstractPhoneCall implements Comparable<PhoneCall>
{
    private static String DATE_TIME_FORMAT = "M/d/yyyy h:mm a";
    private static String DATE_TIME_REGEX = "(\\d{1,2}/){1}(\\d{1,2}/){1}\\d{4}\\s+(\\d{1,2}:\\d{2}\\s+)(am|pm|AM|PM)";

    public String callerNumber;
    public String calleeNumber;

    public Date startTime;
    public Date endTime;

    /** Constructor for PhoneCall
     * No args - requred to be serializable
     */
    public PhoneCall()
    {
        this.callerNumber = null;
        this.calleeNumber = null;
        this.startTime = null;
        this.endTime = null;
    }

    /**
     * Constructor for PhoneCall = Takes numbers and datetimes as args
     * @param callerNum The caller phonenumber
     * @param calleeNum The call-EE phonenumber
     * @param start Start Date/Time of the Call
     * @param end End Date/Time of the Call
     * @throws PhoneBillException Thrown by this.validate()
     */
    public PhoneCall(String callerNum, String calleeNum, String start, String end) throws PhoneBillException
    {
        this.callerNumber = callerNum;
        this.calleeNumber = calleeNum;
        this.startTime = parseDate(start);
        this.endTime = parseDate(end);

        this.validate();
    }

    /**
     * Constructor for PhoneCall: This one uses Date types
     * @param callerNum
     * @param calleeNum
     * @param start
     * @param end
     */
    public PhoneCall(String callerNum, String calleeNum, Date start, Date end)
    {
        this.callerNumber = callerNum;
        this.calleeNumber = calleeNum;
        this.startTime = start;
        this.endTime = end;

        this.validate();
    }

    /**
     * This method is used by my Phonebill Sort method to compare phonecalls
     * Implements compareTo interface java.lang.Comparable
     * @param o
     * @return
     */
    @Override
    public int compareTo(PhoneCall o)
    {
        int time = startTime.compareTo(o.getStartTime());

        if (time != 0)
        {
            return time;
        }

        int caller = callerNumber.compareTo(o.getCaller());

        if (caller != 0)
        {
            return caller;
        }

        return 0;
    }

    /**
     * Overrides getCaller method of AbstractPhoneCall method
     * @return String of the caller's phone number
     */
    @Override
    public String getCaller()
    {
        return this.callerNumber;
    }

    /**
     * Overrides getCallee method of AbstractPhoneCall method
     * @return String of the callee phone number
     */
    @Override
    public String getCallee()
    {
        return this.calleeNumber;
    }

    /**
     * Converts the Start Date object to a String
     * @return String of startTime
     */
    @Override
    public String getStartTimeString()
    {
        String DATE_TIME_FORMAT = "M/d/yyyy h:mm a";
        DateTimeFormat fmt = DateTimeFormat.getFormat(DATE_TIME_FORMAT);

        return fmt.format(this.startTime);
    }

    /**
     * Converts the End Date object to a String
     * @return String of endTime
     */
    @Override
    public String getEndTimeString()
    {
        String DATE_TIME_FORMAT = "M/d/yyyy h:mm a";
        DateTimeFormat fmt = DateTimeFormat.getFormat(DATE_TIME_FORMAT);

        return fmt.format(this.endTime);
    }

    // Validation Methods

    /**
     * Validate Method for PhoneCall - Calls all private validate methods
     * @throws PhoneBillException thrown by validation of class propoerties
     */
    private void validate() throws PhoneBillException
    {
        this.validatePhoneNumber(this.callerNumber);
        this.validatePhoneNumber(this.calleeNumber);

        //this.validateDate(this.startTime);
        //this.validateDate(this.endTime);
    }

    /**
     * Validates a Date/Time String
     * @param s String representing a date/time
     * @return Returns true if the date is valie
     */
    private boolean validateDateTime(String s)
    {
        RegExp regExp = RegExp.compile(DATE_TIME_REGEX);
        MatchResult matcher = regExp.exec(s);

        return matcher != null;
    }

    /**
     * Validates a Phone number
     * @param phoneNumber String representing a phone number
     * @throws PhoneBillException Thrown if the phone number pattern is not matched
     */
    private void validatePhoneNumber(String phoneNumber) throws PhoneBillException
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

    //// Private Methods ///////////////////////

    /**
     * Parses a string into a Date
     * @param s String representation of a date
     * @return Date object
     * @throws PhoneBillException if date can't be parsed
     */
    private Date parseDate(String s) throws PhoneBillException
    {
        String DATE_TIME_FORMAT = "M/d/yyyy h:mm a";
        DateTimeFormat fmt = DateTimeFormat.getFormat(DATE_TIME_FORMAT);
        Date date = fmt.parse(s);

        return date;
    }

    /**
     * Gets the Start Date/Time
     * @return Date Ojbect
     */
    public Date getStartTime()
    {
        return this.startTime;
    }

    /**
     * Gets the End Date/Time
     * @return Date Ojbect
     */
    public Date getEndTime()
    {
        return this.endTime;
    }
}
