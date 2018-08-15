package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.AbstractPhoneBill;

import java.nio.file.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/// The class that manages a phone bill
public class PhoneBill extends AbstractPhoneBill<PhoneCall>
{
    private String customer;
    private Path filePath = null;
    private Collection<PhoneCall> calls = new ArrayList<>();

    /// Constructor for PhoneBill - Takes a Customer Name
    public PhoneBill(String customerName) throws PhoneBillException
    {
        this.customer = customerName;
        this.filePath = null;

        this.validateCustomerName();
    }

    /// Overrides addPhoneCall method of AbstractPhoneBill method
    @Override
    public void addPhoneCall(PhoneCall call)
    {
        this.calls.add(call);
        Collections.sort((ArrayList)this.calls);
    }

    /// Overrides getPhoneCalls method of AbstractPhoneBill method
    @Override
    public Collection<PhoneCall> getPhoneCalls()
    {
        return this.calls;
    }

    public int getTotalMinutes() throws ParseException
    {
        int total = 0;

        for (PhoneCall call : this.calls)
        {
            total += GetDateDiffMinutes(call.getStartTimeString(), call.getEndTimeString());
        }
        return total;
    }

    /// Gets the Customer Name from the Phone Bill
    public String getCustomer()
    {
        return this.customer;
    }

    public void setFilePath( String filePath)
    {
        this.filePath = Paths.get(filePath);
    }

    /// Gets the File Path of the Phone Bill
    public Path getFilePath()
    {
        return this.filePath;
    }

    /// Validates a Customer Name in the PhoneBill text file.
    public void compareCustomerNames(String customerNameInFile) throws PhoneBillException
    {
        this.validateCustomerName();

        if (customerNameInFile == null || customerNameInFile.isEmpty())
        {
            throw new PhoneBillException("Customer name in phone bill file '" + this.filePath.toString() + "' is is empty");
        }

        if (!customerNameInFile.equals(this.customer))
        {
            String msg = "Customer from command line '" + this.customer + "' does not match customer '" + customerNameInFile + "' in file '" + filePath.toString() + "'";
            throw new PhoneBillException(msg);
        }
    }

    //////  PRIVATE METHODS /////////////////////////////

    private int GetDateDiffMinutes(String d1, String d2) throws ParseException
    {
        Date startDate = parseDate(d1);
        Date endDate   = parseDate(d2);

        long duration  = endDate.getTime() - startDate.getTime();
        int diffInMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(duration);

        return diffInMinutes;
    }

    private Date parseDate(String s) throws ParseException
    {
        String DATE_TIME_FORMAT = "M/d/yyyy h:mm a";

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.US);
        sdf.setLenient(false);
        Date date = sdf.parse(s);

        return date;
    }

    /// Validate Customer Name
    private void validateCustomerName() throws PhoneBillException
    {
        if (this.customer == null || this.customer.isEmpty())
        {
            throw new PhoneBillException("Missing customer name");
        }
    }
}
