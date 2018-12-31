package edu.pdx.cs410J.sbraich.client;

import edu.pdx.cs410J.AbstractPhoneBill;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.google.gwt.i18n.client.DateTimeFormat;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * The class that manages a phone bill
 */
public class PhoneBill extends AbstractPhoneBill<PhoneCall>
{
    public String customer;
    public Collection<PhoneCall> calls = new ArrayList<>();

    /** Constructor for PhoneBill
     * No args - requred to be serializable
     * @throws PhoneBillException
     */
    public PhoneBill() throws PhoneBillException
    {
        this.customer = null;
    }

    /**
     * Constructor for PhoneBill - Takes a Customer Name
     * @param customerName String Customer name
     * @throws PhoneBillException Thrown by this.validateCustomerName()
     */
    public PhoneBill(String customerName) throws PhoneBillException
    {
        this.customer = customerName;
        this.validateCustomerName();
    }

    /**
     * Adds a PhoneCall object to a phone bill
     * Overrides addPhoneCall method of AbstractPhoneBill method
     * @param call PhoneCall Object to be added
     */
    @Override
    public void addPhoneCall(PhoneCall call)
    {
        this.calls.add(call);
        Collections.sort((ArrayList)this.calls);
    }

    /**
     * Overrides getPhoneCalls method of AbstractPhoneBill method
     * @return Returns collection of Phonecalls
     */
    @Override
    public Collection<PhoneCall> getPhoneCalls()
    {
        return this.calls;
    }

    /**
     * Gets the Total Minutes of all calls in a phone bill
     * @return int of total minutes of phone bill
     * @throws ParseException throw if the total can't be summed
     */
    public int getTotalMinutes() throws ParseException
    {
        int total = 0;

        for (PhoneCall call : this.calls)
        {
            total += GetDateDiffMinutes(call.getStartTimeString(), call.getEndTimeString());
        }
        return total;
    }

    /** Gets the Customer name of a phone bill
     * @return String Customer Name from the Phone Bill
     */
    public String getCustomer()
    {
        return this.customer;
    }

    /**
     * Set's the customer property
     * @param customer
     */
    public void setCustomer(String customer)
    {
        this.customer = customer;
    }

    /**
     * Gets the different between two dates
     * @param d1 String of the 1st Date
     * @param d2 String of the 2nd Date
     * @return int representing difference between two dates in minutes.
     * @throws ParseException thrown if the dates can't be parsed
     */
    private int GetDateDiffMinutes(String d1, String d2) throws ParseException
    {
        Date startDate = parseDate(d1);
        Date endDate   = parseDate(d2);

        long duration  = endDate.getTime() - startDate.getTime();
        int diffInMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(duration);

        return diffInMinutes;
    }

    /**
     * Parse a date string into a Date type
     * @param s String that is to be parsed
     * @return Date object
     * @throws ParseException thrown if the date can't be parsed
     */
    public Date parseDate(String s) throws ParseException
    {
        String DATE_TIME_FORMAT = "M/d/yyyy h:mm a";
        DateTimeFormat fmt = DateTimeFormat.getFormat(DATE_TIME_FORMAT);
        Date date = fmt.parse(s);

        return date;
    }

    /**
     * Validate a Phone Bill Customer name
     * @throws PhoneBillException thrown if name can't be validated
     */
    private void validateCustomerName() throws PhoneBillException
    {
        if (this.customer == null || this.customer.isEmpty())
        {
            throw new PhoneBillException("Missing customer name");
        }
    }

}
