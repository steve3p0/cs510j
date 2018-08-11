package edu.pdx.cs410J.sbraich;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/// Unit tests for the {@link PhoneCall} class.
public class PhoneCallTest {

    @Test
    public void getStartTimeString_PositiveTest() throws PhoneBillException
    {
        PhoneCall call = new PhoneCall("123-123-1234", "555-555-5555", 
                                           "4/27/2018 11:39 am","5/15/2018 08:39 pm");

        assertThat(call.getStartTimeString(), is("4/27/2018 11:39 AM"));
        assertThat(call.getEndTimeString(), is("5/15/2018 8:39 PM"));
    }

    @Test
    public void initiallyAllPhoneCallsHaveTheSameCallee() throws PhoneBillException
    {
        PhoneCall call = new PhoneCall("555-555-5555", "123-123-1234", 
                                           "1/15/2018 7:39 am","1/15/2018 8:39 pm");
        
        assertThat(call.getCaller(), containsString("555-555-5555"));
        assertThat(call.getCallee(), containsString("123-123-1234"));
    }

    // Date and and Time Tests

    // Invalid Year

    @Test(expected = PhoneBillException.class)
    public void PhoneCallInvalidYear1() throws Exception
    {
        PhoneCall call = new PhoneCall("503-123-1234", "503-123-1234",
                                           "1/15/201 07:39 AM", "12/1/2018 11:39 am");
    }

    @Test(expected = PhoneBillException.class)
    public void PhoneCallInvalidYear2() throws Exception
    {
        PhoneCall call = new PhoneCall("503-123-1234", "503-123-1234",
                                           "1/15/18 7:39 PM", "12/1/2018 03:39 PM");
    }
    
    @Test(expected = PhoneBillException.class)
    public void PhoneCallInvalidYear3() throws Exception
    {
        PhoneCall call = new PhoneCall("503-123-1234", "503-123-1234",
                                           "1/15/1 07:39 AM", "12/1/2018 11:39 AM");
    }
    
    @Test(expected = PhoneBillException.class)
    public void PhoneCallInvalidYear4() throws Exception
    {
        PhoneCall call = new PhoneCall("503-123-1234", "503-123-1234",
                                           "1/15/20180 7:39 am", "12/1/2018 3:39 pm");
    }
    
    @Test(expected = PhoneBillException.class)
    public void PhoneCallInvalidYear5() throws Exception
    {
        PhoneCall call = new PhoneCall("503-123-1234", "503-123-1234",
                                           "1/15/ 12:39 AM", "12/1/2018 11:39 PM");
    }
    

}
