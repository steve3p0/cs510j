package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.AbstractPhoneBill;

import java.nio.file.*;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

// The class that manages a phone bill
public class PhoneBill extends AbstractPhoneBill<PhoneCall>
{
    private String customer;
    private Path filePath;
    private Collection<PhoneCall> calls = new ArrayList<>();

    public PhoneBill(String customerName)
    {
        this.customer = customerName;
        this.filePath = null;
    }

    public PhoneBill(String customerName, String filePath)
    {
        this.customer = customerName;
        this.filePath = Paths.get(filePath);
    }

    @Override
    public void addPhoneCall(PhoneCall call)
    {
        this.calls.add(call);
    }

    @Override
    public Collection<PhoneCall> getPhoneCalls()
    {
        return this.calls;
    }


    public String getCustomer()
    {
        return this.customer;
    }

    public Path getFilePath()
    {
        return this.filePath;
    }
}
