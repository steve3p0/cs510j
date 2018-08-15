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
        PhoneBill bill = new PhoneBill("Steve");
        bill.setFilePath("readonly.txt");

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
        assertEquals("01/15/2018 07:39 AM", c.getStartTimeString());
        assertEquals("01/15/2018 08:39 PM", c.getEndTimeString());
    }

    @Test
    public void Test_addPhoneCall_Sort() throws PhoneBillException
    {
        PhoneBill bill = new PhoneBill("Steve");
        PhoneCall call;

        call = new PhoneCall("333-333-3333", "123-123-1234", "3/1/2018 12:01 AM","3/1/2018 12:05 AM");
        bill.addPhoneCall(call);
        call = new PhoneCall("444-444-4444", "123-123-1234", "4/1/2018 12:01 AM","4/1/2018 12:05 AM");
        bill.addPhoneCall(call);
        call = new PhoneCall("111-111-1111", "123-123-1234", "1/1/2018 12:01 AM","1/1/2018 12:05 AM");
        bill.addPhoneCall(call);
        call = new PhoneCall("222-222-2222", "123-123-1234", "2/1/2018 12:01 AM","2/1/2018 12:05 AM");
        bill.addPhoneCall(call);

        Collection<PhoneCall> calls = bill.getPhoneCalls();

        String actual = "";

        for (PhoneCall ph : calls)
        {
            actual += ph.getCaller() + ",";
        }

        String expected = "111-111-1111,222-222-2222,333-333-3333,444-444-4444,";

        assertEquals(expected, actual);
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
        assertEquals("01/15/2018 07:39 AM", c.getStartTimeString());
        assertEquals("01/15/2018 08:39 PM", c.getEndTimeString());
    }
}