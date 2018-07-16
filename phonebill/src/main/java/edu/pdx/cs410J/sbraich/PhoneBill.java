package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.AbstractPhoneBill;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

// The class that manages a phone bill
public class PhoneBill extends AbstractPhoneBill<PhoneCall>
{

    private final String customer;
    private Collection<PhoneCall> calls = new ArrayList<>();

    public PhoneBill(String customerName)
    {
        this.customer = customerName;
    }

    @Override
    public String getCustomer()
    {
        return this.customer;
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
}
