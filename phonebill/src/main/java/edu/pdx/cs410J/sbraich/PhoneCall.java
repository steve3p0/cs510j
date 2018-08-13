package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.AbstractPhoneCall;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.ZoneId;
import java.time.LocalDate;

/// The Class that manages a phone call
// public class PhoneCall extends AbstractPhoneCall
// public class Animal implements Comparable<Animal>
// public class PhoneCall extends AbstractPhoneCall implements Comparable<PhoneCall>
public class PhoneCall extends AbstractPhoneCall implements Comparable<PhoneCall>
{
    private static String DATE_TIME_FORMAT = "M/d/yyyy h:mm a";
    private static String DATE_TIME_REGEX = "(\\d{1,2}\\/){1}(\\d{1,2}\\/){1}\\d{4}\\s+(\\d{1,2}:\\d{2}\\s+)(am|pm|AM|PM)";

    private final String callerNumber;
    private final String calleeNumber;

    private final Date startTime;
    private final Date endTime;

//    @Override
//    public int compareTo(PhoneCall o)
//    {
//        int compareTime = startTime.compareTo(o.getStartTime());
//
//        if (compareTime != 0)
//        {
//            return compareTime;
//        }
//
//        int compareCaller = callerNumber.compareTo(o.getCaller());
//
//        if (compareCaller != 0)
//        {
//            return compareCaller;
//        }
//
//        return 0;
//    }

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

    /// Constructor for PhoneCall = Takes numbers and datetimes as args
    public PhoneCall(String callerNum, String calleeNum, String start, String end) throws PhoneBillException
    {
        this.callerNumber = callerNum;
        this.calleeNumber = calleeNum;
        this.startTime = parseDate(start);
        this.endTime = parseDate(end);

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
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        String s = sdf.format(this.startTime);

        return s;
    }

    /// Overrides getEndTimeString method of AbstractPhoneCall method
    @Override
    public String getEndTimeString()
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
        String s = sdf.format(this.endTime);

        return s;
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

    public boolean validateDateTime(String s)
    {
        Pattern pattern = Pattern.compile(DATE_TIME_REGEX);
        Matcher matcher = pattern.matcher(s);

        return matcher.find();
    }

    /// Validate Date
    public void validateDate(Date datetime) throws PhoneBillException
    {
        LocalDate localDate = datetime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year  = localDate.getYear();

        if (year < 1000 || year > 2099)
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

    //// Private Methods ///////////////////////

    private Date parseDate(String s) throws PhoneBillException
    {
        try
        {
            if (!validateDateTime(s))
            {
                throw new PhoneBillException("'" + s + "' is not a valid date/time in the format of '" + DATE_TIME_FORMAT + "'");
            }

            SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.US);
            sdf.setLenient(false);
            Date date = sdf.parse(s);

            return date;
        }
        catch (java.time.format.DateTimeParseException e)
        {
            throw new PhoneBillException("'" + s + "' is not a valid date/time in the format of '" + DATE_TIME_FORMAT + "'");
        }
        catch (ParseException e)
        {
            throw new PhoneBillException("'" + s + "' is not a valid date/time in the format of '" + DATE_TIME_FORMAT + "'");
        }
    }

    /// Are these methods even used?

    public Date getStartTime()
    {
        return this.startTime;
    }

    public Date getEndTime()
    {
        return this.endTime;
    }
}
