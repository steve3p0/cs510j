package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.AbstractPhoneBill;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class PhoneBillTest
{

//    @Before
//    public void setUp() throws Exception {
//    }
//
//    @After
//    public void tearDown() throws Exception {
//    }

    @Test
    public void getCustomer() throws PhoneBillException
    {
        PhoneBill bill = new PhoneBill("Steve", "readonly.txt");

        assertThat(bill.getCustomer(), is("Steve"));
    }

    @Test
    public void addPhoneCall() throws PhoneBillException
    {
        PhoneCall call = new PhoneCall("555-555-5555", "123-123-1234",
                "1/15/2018 7:39 am","1/15/2018 8:39 PM");
        PhoneBill bill = new PhoneBill("Steve");

        bill.addPhoneCall(call);

        Collection<PhoneCall> calls = bill.getPhoneCalls();
        PhoneCall c = calls.iterator().next();

        assertEquals("555-555-5555", c.getCaller());
        assertEquals("123-123-1234", c.getCallee());
        assertEquals("1/15/2018 7:39 AM", c.getStartTimeString());
        assertEquals("1/15/2018 8:39 PM", c.getEndTimeString());
    }

    @Test
    public void getPhoneCalls() throws PhoneBillException
    {
        PhoneCall call = new PhoneCall("555-555-5555", "123-123-1234",
                "1/15/2018 7:39 am","1/15/2018 8:39 PM");
        PhoneBill bill = new PhoneBill("Steve");

        bill.addPhoneCall(call);

        Collection<PhoneCall> calls = bill.getPhoneCalls();
        PhoneCall c = calls.iterator().next();

        assertEquals("555-555-5555", c.getCaller());
        assertEquals("123-123-1234", c.getCallee());
        assertEquals("1/15/2018 7:39 AM", c.getStartTimeString());
        assertEquals("1/15/2018 8:39 PM", c.getEndTimeString());
    }
}