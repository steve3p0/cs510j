package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.AbstractPhoneBill;

import java.nio.file.*;
import java.util.Collection;
import java.util.ArrayList;

/// The class that manages a phone bill
public class PhoneBill extends AbstractPhoneBill<PhoneCall>
{
    private String customer;
    private String customerFromFile;
    private Path filePath = null;
    private Collection<PhoneCall> calls = new ArrayList<>();

    /// Constructor for PhoneBill - Takes a Customer Name
    public PhoneBill(String customerName) throws PhoneBillException
    {
        this.customer = customerName;
        this.customerFromFile = null;
        this.filePath = null;

        this.validateCustomerName();
    }

    /// Constructor for PhoneBill - Takes a Customer Name and FilePath
    public PhoneBill(String customerName, String filePath) throws PhoneBillException
    {
        this.customer = customerName;
        this.customerFromFile = null;
        this.filePath = Paths.get(filePath);

        this.validateCustomerName();
    }

    /// Constructor for PhoneBill - Takes a Customer Name and FilePath
    public PhoneBill(String customerFromCli, String customerFromFile, String filePath) throws PhoneBillException
    {
        this.customer = customerFromCli;
        this.customerFromFile = customerFromFile;
        this.filePath = Paths.get(filePath);

        this.compareCustomerNames();
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

    /// Validate File Path
    private void validateCustomerName() throws PhoneBillException
    {
        if (this.customer == null || this.customer.isEmpty())
        {
            throw new PhoneBillException("Missing customer name");
        }
    }

    /// Validates a Customer Name in the PhoneBill text file.
    private void compareCustomerNames() throws PhoneBillException
    {
        this.validateCustomerName();

        if (this.customerFromFile == null || this.customerFromFile.isEmpty())
        {
            throw new PhoneBillException("Customer name in phone bill file '" + this.filePath.toString() + "' is is empty");
        }

        if (!customerFromFile.equals(this.customer))
        {
            String msg = "Customer from command line '" + this.customer + "' does not match customer '" + customerFromFile + "' in file '" + filePath.toString() + "'";
            throw new PhoneBillException(msg);
        }
    }
}
