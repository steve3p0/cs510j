package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.AbstractPhoneBill;

import java.nio.file.*;
import java.util.Collection;
import java.util.ArrayList;

/// The class that manages a phone bill
public class PhoneBill extends AbstractPhoneBill<PhoneCall>
{
    private String customer;
    private Path filePath;
    private Collection<PhoneCall> calls = new ArrayList<>();

    /// Constructor for PhoneBill - Takes a Customer Name
    public PhoneBill(String customerName)
    {
        this.customer = customerName;
        this.filePath = null;
    }

    /// Constructor for PhoneBill - Takes a Customer Name and FilePath
    public PhoneBill(String customerName, String filePath)
    {
        this.customer = customerName;
        this.filePath = Paths.get(filePath);
    }

    /// Overrides addPhoneCall method of AbstractPhoneBill method
    @Override
    public void addPhoneCall(PhoneCall call)
    {
        this.calls.add(call);
    }

    /// Overrides getPhoneCalls method of AbstractPhoneBill method
    @Override
    public Collection<PhoneCall> getPhoneCalls()
    {
        return this.calls;
    }

    /// Gets the Customer Name from the Phone Bill
    public String getCustomer()
    {
        return this.customer;
    }

    /// Gets the File Path of the Phone Bill
    public Path getFilePath()
    {
        return this.filePath;
    }
}
