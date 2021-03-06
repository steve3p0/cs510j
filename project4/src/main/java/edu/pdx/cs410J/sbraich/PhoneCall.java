package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.AbstractPhoneCall;
import edu.pdx.cs410J.ParserException;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.ZoneId;
import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * The Class that manages a phone call
 */
public class PhoneCall extends AbstractPhoneCall implements Comparable<PhoneCall>
{
    private static String PARSE_DATETIME_PATTERN = "M/d/yyyy h:mm a";
    private static String PRINT_DATETIME_PATTERN = "MM/dd/yyyy hh:mm a";

    public static final String DATE_FORMAT_STRING = "M/dd/yyyy h:mm a";
    public static final DateTimeFormatter DATE_FORMAT = ofPattern(DATE_FORMAT_STRING);

    private static String DATE_TIME_REGEX = "(\\d{1,2}/){1}(\\d{1,2}/){1}\\d{4}\\s+(\\d{1,2}:\\d{2}\\s+)(am|pm|AM|PM)";

    private String callerNumber;
    private String calleeNumber;

    private Date startTime;
    private Date endTime;

    public PhoneCall()
    {

    }

    /**
     * Constructor for PhoneCall = Takes numbers and datetimes as args
     * @param callerNum The caller phonenumber
     * @param calleeNum The call-EE phonenumber
     * @param start Start Date/Time of the Call
     * @param end End Date/Time of the Call
     * @throws PhoneBillException Thrown by this.validate()
     */
    public PhoneCall(String callerNum, String calleeNum, String start, String end) throws ParserException
    {
        this.callerNumber = callerNum;
        this.calleeNumber = calleeNum;
        this.startTime = parseDate(start);
        this.endTime = parseDate(end);

        this.validate();
    }

    /**
     * This method is used by my Phonebill Sort method to compare phonecalls
     * Implements compareTo interface java.lang.Comparable
     * @param call PhoneCall
     * @return int representing time different between start of two calls
     */
    @Override
    public int compareTo(PhoneCall call)
    {
        int time = startTime.compareTo(call.getStartTime());

        if (time != 0)
        {
            return time;
        }

        int caller = callerNumber.compareTo(call.getCaller());

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
        SimpleDateFormat sdf = new SimpleDateFormat(PRINT_DATETIME_PATTERN);
        return sdf.format(this.startTime);
    }

    /**
     * Converts the End Date object to a String
     * @return String of endTime
     */
    @Override
    public String getEndTimeString()
    {
        SimpleDateFormat sdf = new SimpleDateFormat(PRINT_DATETIME_PATTERN);
        return sdf.format(this.endTime);
    }

    // Validation Methods

    /**
     * Validate Method for PhoneCall - Calls all private validate methods
     * @throws ParserException thrown by validation of class propoerties
     */
    private void validate() throws ParserException
    {
        this.validatePhoneNumber(this.callerNumber);
        this.validatePhoneNumber(this.calleeNumber);

        this.validateDate(this.startTime);
        this.validateDate(this.endTime);

        this.validateStartBeforeEnd();
    }

    /**
     * Validate that a start date comes before end date
     * @throws ParserException thrown if start <= end
     */
    private void validateStartBeforeEnd() throws ParserException
    {
        long duration  = this.endTime.getTime() - this.startTime.getTime();

        if (duration <= 0)
        {
            throw new ParserException("The start date '" + this.getStartTimeString() +"' must come before the end date "
                    + "'" + this.getEndTimeString() + "'");
        }
    }

    /**
     * Validates a Date/Time String
     * @param s String representing a date/time
     * @return Returns true if the date is valie
     */
    private boolean validateDateTime(String s)
    {
        Pattern pattern = Pattern.compile(DATE_TIME_REGEX);
        Matcher matcher = pattern.matcher(s);

        return matcher.find();
    }

    /// Validate Date

    /**
     * Validates a Date - Makes sure the year date is at least 4 digits and not greater than 2099
     * @param datetime Date object of the date to be validated
     * @throws ParserException thrown if the year is off
     */
    private void validateDate(Date datetime) throws ParserException
    {
        LocalDate localDate = datetime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year  = localDate.getYear();

        if (year < 1000 || year > 2099)
        {
            throw new ParserException("'" + datetime +"' is not a valid date/time");
        }
    }

    /**
     * Validates a Phone number
     * @param phoneNumber String representing a phone number
     * @throws ParserException Thrown if the phone number pattern is not matched
     */
    private void validatePhoneNumber(String phoneNumber) throws ParserException
    {
        //nnn-nnn-nnnn
        //(?:\d{3}-){2}\d{4}
        //String pattern = "^/d(?:-/d{3}){3}/d$";
        String pattern = "[0-9]{3}-[0-9]{3}-[0-9]{4}";

        if (!phoneNumber.matches(pattern))
        {
            throw new ParserException("'" + phoneNumber +"' is not a valid phone number");
        }
    }

    //// Private Methods ///////////////////////

    /**
     * Parses a string into a Date
     * @param s String representation of a date
     * @return Date object
     * @throws PhoneBillException if date can't be parsed
     */
    public Date parseDate(String s) throws ParserException
    {
        try
        {
            if (!validateDateTime(s))
            {
                throw new ParserException("'" + s + "' is not a valid date/time in the format of '" + PARSE_DATETIME_PATTERN + "'");
            }

            SimpleDateFormat sdf = new SimpleDateFormat(PARSE_DATETIME_PATTERN, Locale.US);
            sdf.setLenient(false);

            return sdf.parse(s);
        }
        catch (java.time.format.DateTimeParseException e)
        {
            throw new ParserException("DateTimeParseException: '" + s + "' is not a valid date/time in the format of '" + PARSE_DATETIME_PATTERN + "'");
        }
        catch (ParseException e)
        {
            throw new ParserException("ParseException: '" + s + "' is not a valid date/time in the format of '" + PARSE_DATETIME_PATTERN + "'");
        }
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
