package edu.pdx.cs410J.sbraich;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/// Unit tests for the {@link PhoneCall} class.
public class PhoneCallDateTimeTest
{
    // Positive Tests: Date and and Time
    @Test
    public void getStartTimeString_PositiveTest1() throws PhoneBillException
    {
        PhoneCall call = new PhoneCall("555-555-5555", "123-123-1234",
                "1/15/2018 12:39 am","1/15/2018 08:39 pm");

        assertThat(call.getStartTimeString(), is("01/15/2018 12:39 AM"));
        assertThat(call.getEndTimeString(),   is("01/15/2018 08:39 PM"));
    }

    @Test
    public void getStartTimeString_PositiveTest2() throws PhoneBillException
    {
        PhoneCall call = new PhoneCall("555-555-5555", "123-123-1234",
                "1/15/2018 7:39 am","1/15/2018 8:39 PM");

        assertThat(call.getStartTimeString(), is("01/15/2018 07:39 AM"));
        assertThat(call.getEndTimeString(),   is("01/15/2018 08:39 PM"));
    }

    @Test
    public void getStartTimeString_PositiveTest3() throws PhoneBillException
    {
        PhoneCall call = new PhoneCall("555-555-5555", "123-123-1234",
                "1/15/2018 12:39 am","1/15/2018 10:39 pm");

        assertThat(call.getStartTimeString(), is("01/15/2018 12:39 AM"));
        assertThat(call.getEndTimeString(),   is("01/15/2018 10:39 PM"));
    }

    @Test
    public void getStartTimeString_PositiveTest4() throws PhoneBillException
    {
        PhoneCall call = new PhoneCall("555-555-5555", "123-123-1234",
                "1/15/2018 7:39 am","1/15/2018 8:39 PM");

        assertThat(call.getStartTimeString(), is("01/15/2018 07:39 AM"));
        assertThat(call.getEndTimeString(),   is("01/15/2018 08:39 PM"));
    }

    @Test
    public void getStartTimeString_ValidDateTimes() throws PhoneBillException
    {
        PhoneCall call1 = new PhoneCall("555-555-5555", "123-123-1234",
                "1/15/2018 07:39 am","1/15/2018 08:39 pm");
        assertThat(call1.getStartTimeString(), is("01/15/2018 07:39 AM"));
        assertThat(call1.getEndTimeString(),   is("01/15/2018 08:39 PM"));

        PhoneCall call2 = new PhoneCall("555-555-5555", "123-123-1234",
                "1/15/2018 7:39 am","1/15/2018 8:39 pm");
        assertThat(call2.getStartTimeString(), is("01/15/2018 07:39 AM"));
        assertThat(call2.getEndTimeString(),   is("01/15/2018 08:39 PM"));

        PhoneCall call3 = new PhoneCall("555-555-5555", "123-123-1234",
                "1/15/2018 7:39 AM","1/15/2018 8:39 PM");
        assertThat(call3.getStartTimeString(), is("01/15/2018 07:39 AM"));
        assertThat(call3.getEndTimeString(),   is("01/15/2018 08:39 PM"));

        PhoneCall call4 = new PhoneCall("555-555-5555", "123-123-1234",
                "1/15/2018 12:39 am","1/15/2018 10:39 pm");
        assertThat(call4.getStartTimeString(), is("01/15/2018 12:39 AM"));
        assertThat(call4.getEndTimeString(),   is("01/15/2018 10:39 PM"));

        //1/15/2018 12:39 AM
        PhoneCall call5 = new PhoneCall("555-555-5555", "123-123-1234",
                "1/15/2018 12:39 AM","1/15/2018 10:39 PM");
        assertThat(call5.getStartTimeString(), is("01/15/2018 12:39 AM"));
        assertThat(call5.getEndTimeString(),   is("01/15/2018 10:39 PM"));

        PhoneCall call6 = new PhoneCall("555-555-5555", "123-123-1234",
                "12/1/2018 9:39 AM","12/1/2018 08:39 PM");
        assertThat(call6.getStartTimeString(), is("12/01/2018 09:39 AM"));
        assertThat(call6.getEndTimeString(),   is("12/01/2018 08:39 PM"));
    }

    @Test(expected = PhoneBillException.class)
    public void getStartTimeString_StartEqualToEnd() throws PhoneBillException
    {

        PhoneCall call7 = new PhoneCall("555-555-5555", "123-123-1234",
                "12/1/2018 11:39 pm","12/1/2018 11:39 PM");
        assertThat(call7.getStartTimeString(), is("12/01/2018 11:39 PM"));
        assertThat(call7.getEndTimeString(),   is("12/01/2018 11:39 PM"));

    }

    @Test(expected = PhoneBillException.class)
    public void getStartTimeString_StartAfterEnd() throws PhoneBillException
    {

        PhoneCall call7 = new PhoneCall("555-555-5555", "123-123-1234",
                "12/1/2018 11:39 pm","12/1/2018 11:39 PM");
        assertThat(call7.getStartTimeString(), is("12/02/2018 11:39 PM"));
        assertThat(call7.getEndTimeString(),   is("12/01/2018 11:39 PM"));

    }


    /// Edge Cases ///////////////////////////////////////////////////////////////////////////

    @Test(expected = PhoneBillException.class)
    public void getStartTimeString_Edge1() throws PhoneBillException
    {
        PhoneCall call = new PhoneCall("555-555-5555", "123-123-1234",
                "1/15/2018 7:39am","1/15/2018 8:39PM");
    }

    @Test(expected = PhoneBillException.class)
    public void getStartTimeString_Edge2() throws PhoneBillException
    {
        PhoneCall call = new PhoneCall("555-555-5555", "123-123-1234",
                "1/15/2018 7:39am","1/15/2018 8:39pm");
    }


    /// Invalid AM/PM ///////////////////////////////////////////////////////////////////////////

    @Test(expected = PhoneBillException.class)
    public void PhoneCallInvalidTime_NoAmPm1() throws Exception
    {
        PhoneCall call = new PhoneCall("555-555-5555","503-123-1234",
                "1/15/2018 07:39", "12/1/2018 11:39");
    }

    @Test(expected = PhoneBillException.class)
    public void PhoneCallInvalidTime_NoAmPm2() throws Exception
    {
        PhoneCall call = new PhoneCall("555-555-5555","503-123-1234",
                "1/15/2018 17:39", "12/1/2018 13:39");
    }

    @Test(expected = PhoneBillException.class)
    public void PhoneCallInvalidTime_AmPmX() throws Exception
    {
        PhoneCall call = new PhoneCall("555-555-5555","503-123-1234",
                "1/15/2018 7:39 XX", "12/1/2018 3:39 X");
    }

    @Test(expected = PhoneBillException.class)
    public void PhoneCallInvalidTime_AP() throws Exception
    {
        PhoneCall call = new PhoneCall("555-555-5555","503-123-1234",
                "1/15/2018 7:39 a", "12/1/2018 13:39 M");
    }

    /// Invalid Hour ///////////////////////////////////////////////////////////////////////////

    @Test(expected = PhoneBillException.class)
    public void PhoneCallInvalidHour_X() throws Exception
    {
        PhoneCall call = new PhoneCall("555-555-5555","503-123-1234",
                "1/15/2018 X:39 AM", "12/1/2018 XX:39 PM");
    }

    @Test(expected = PhoneBillException.class)
    public void PhoneCallInvalidHour_TooLarge1() throws Exception
    {
        PhoneCall call = new PhoneCall("555-555-5555","503-123-1234",
                "1/15/2018 13:39 AM", "12/1/2018 23:39 PM");
    }


    @Test(expected = PhoneBillException.class)
    public void PhoneCallInvalidHour_00() throws Exception
    {
        PhoneCall call = new PhoneCall("555-555-5555","503-123-1234",
                "1/15/2018 00:39 AM", "12/1/2018 0:39 AM");
    }

    @Test(expected = PhoneBillException.class)
    public void PhoneCallInvalidHour_Missing1() throws Exception
    {
        PhoneCall call = new PhoneCall("555-555-5555","503-123-1234",
                "1/15/2018 :39 AM", "12/1/2018 :39 AM");
    }

    @Test(expected = PhoneBillException.class)
    public void PhoneCallInvalidHour_Missing2() throws Exception
    {
        PhoneCall call = new PhoneCall("555-555-5555","503-123-1234",
                "1/15/2018 39 AM", "12/1/2018 39 AM");
    }

    /// Invalid Minute ///////////////////////////////////////////////////////////////////////////

    @Test(expected = PhoneBillException.class)
    public void PhoneCallInvalidMinute_X() throws Exception
    {
        PhoneCall call = new PhoneCall("555-555-5555","503-123-1234",
                "1/15/2018 07:X9 AM", "12/1/2018 XX:3X PM");
    }

    @Test(expected = PhoneBillException.class)
    public void PhoneCallInvalidMinute_TooLarge1() throws Exception
    {
        PhoneCall call = new PhoneCall("555-555-5555","503-123-1234",
                "1/15/2018 7:60 AM", "12/1/2018 11:99 PM");
    }

    @Test(expected = PhoneBillException.class)
    public void PhoneCallInvalidMinute_TooLarge2() throws Exception
    {
        PhoneCall call = new PhoneCall("555-555-5555","503-123-1234",
                "1/15/2018 7:123 AM", "12/1/2018 11:100 PM");
    }

    @Test(expected = PhoneBillException.class)
    public void PhoneCallInvalidMinute_Missing1() throws Exception
    {
        PhoneCall call = new PhoneCall("555-555-5555","503-123-1234",
                "1/15/2018 7:9 AM", "12/1/2018 12:3 AM");
    }

    @Test(expected = PhoneBillException.class)
    public void PhoneCallInvalidMinute_Missing2() throws Exception
    {
        PhoneCall call = new PhoneCall("555-555-5555","503-123-1234",
                "1/15/2018 7: AM", "12/1/2018 12: AM");
    }



    /// Invalid Month //////////////////////////////////////////////////////////////////////////

    @Test(expected = PhoneBillException.class)
    public void PhoneCallInvalidMonth1() throws Exception
    {
        PhoneCall call = new PhoneCall("503-123-1234", "503-123-1234",
                "15/15/2018 7:39 am", "12/1/2018 9:39 AM");
    }


    @Test(expected = PhoneBillException.class)
    public void PhoneCallInvalidMonth2() throws Exception
    {
        PhoneCall call = new PhoneCall("503-123-1234", "503-123-1234",
                "115/15/2018 07:39 pm", "12/1/2018 08:39 PM");
    }


    @Test(expected = PhoneBillException.class)
    public void PhoneCallInvalidMonth3() throws Exception
    {
        PhoneCall call = new PhoneCall("503-123-1234", "503-123-1234",
                "/15/2018 7:39 PM", "12/1/2018 11:39 pm");
    }

    // Invalid Day Tests /////////////////////////////////////////////////////////////////////

    @Test(expected = PhoneBillException.class)
    public void PhoneCallInvalidDay1() throws Exception
    {
        PhoneCall call = new PhoneCall("503-123-1234", "503-123-1234",
                "1/40/2018 07:39 AM", "12/1/2018 11:39 PM");
    }

    @Test(expected = PhoneBillException.class)
    public void PhoneCallInvalidDay2() throws Exception
    {
        PhoneCall call = new PhoneCall("503-123-1234", "503-123-1234",
                "1/140/2018 7:39 pm", "12/1/2018 11:39 pm");
    }

    @Test(expected = PhoneBillException.class)
    public void PhoneCallInvalidDay3() throws Exception
    {
        PhoneCall call = new PhoneCall( "503-123-1234", "503-123-1234",
                "2/30/2018 12:39 am", "12/1/2018 11:39 AM");
    }

    @Test(expected = PhoneBillException.class)
    public void PhoneCallInvalidDay4() throws Exception
    {
        PhoneCall call = new PhoneCall("503-123-1234", "503-123-1234",
                "1/0/2018 10:39 AM", "12/1/2018 11:39 pm");
    }

    @Test(expected = PhoneBillException.class)
    public void PhoneCallInvalidDay5() throws Exception
    {
        PhoneCall call = new PhoneCall("503-123-1234", "503-123-1234",
                "1//2018 07:39 pm", "12/1/2018   11:39 PM");
    }
}
