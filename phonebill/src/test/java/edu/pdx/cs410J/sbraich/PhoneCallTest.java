package edu.pdx.cs410J.sbraich;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/// Unit tests for the {@link PhoneCall} class.
public class PhoneCallTest {

  @Test
  public void getStartTimeStringNeedsToBeImplemented() throws PhoneBillException
  {
    PhoneCall call = new PhoneCall("123-123-1234", "555-555-5555", "1/15/2018 19:39","1/15/2018 20:39");

    assertThat(call.getStartTimeString(), is("1/15/2018 19:39"));
  }

  @Test
  public void initiallyAllPhoneCallsHaveTheSameCallee() throws PhoneBillException
  {
    PhoneCall call = new PhoneCall("555-555-5555", "123-123-1234", "1/15/2018 19:39","1/15/2018 20:39");

    assertThat(call.getCallee(), containsString("123-123-1234"));
  }

  // Date and and Time Tests

  // Invalid Year

  @Test(expected = PhoneBillException.class)
  public void PhoneCallInvalidYear1() throws Exception
  {
      PhoneCall call = new PhoneCall("503-123-1234", "503-123-1234", "1/15/201 19:39", "12/1/2018 19:39");
  }

  @Test(expected = PhoneBillException.class)
  public void PhoneCallInvalidYear2() throws Exception
  {
      PhoneCall call = new PhoneCall("503-123-1234", "503-123-1234", "1/15/20 19:39", "12/1/2018 19:39");
  }

  @Test(expected = PhoneBillException.class)
  public void PhoneCallInvalidYear3() throws Exception
  {
      PhoneCall call = new PhoneCall("503-123-1234", "503-123-1234", "1/15/1 19:39", "12/1/2018 19:39");
  }

  @Test(expected = PhoneBillException.class)
  public void PhoneCallInvalidYear4() throws Exception
  {
    PhoneCall call = new PhoneCall("503-123-1234", "503-123-1234", "1/15/20180 19:39", "12/1/2018 19:39");
  }

  @Test(expected = PhoneBillException.class)
  public void PhoneCallInvalidYear5() throws Exception
  {
    PhoneCall call = new PhoneCall("503-123-1234", "503-123-1234", "1/15/ 19:39", "12/1/2018 19:39");
  }

  // Invalid Month

  @Test(expected = PhoneBillException.class)
  public void PhoneCallInvalidMonth1() throws Exception
  {
    PhoneCall call = new PhoneCall("503-123-1234", "503-123-1234", "15/15/2018 19:39", "12/1/2018 19:39");
  }


  @Test(expected = PhoneBillException.class)
  public void PhoneCallInvalidMonth2() throws Exception
  {
    PhoneCall call = new PhoneCall("503-123-1234", "503-123-1234", "115/15/2018 19:39", "12/1/2018 19:39");
  }


  @Test(expected = PhoneBillException.class)
  public void PhoneCallInvalidMonth3() throws Exception
  {
    PhoneCall call = new PhoneCall("503-123-1234", "503-123-1234", "/15/2018 19:39", "12/1/2018 19:39");
  }

  // Invalid Day Tests

  @Test(expected = PhoneBillException.class)
  public void PhoneCallInvalidDay1() throws Exception
  {
    PhoneCall call = new PhoneCall("503-123-1234", "503-123-1234", "1/40/2018 19:39", "12/1/2018 19:39");
  }

  @Test(expected = PhoneBillException.class)
  public void PhoneCallInvalidDay2() throws Exception
  {
    PhoneCall call = new PhoneCall("503-123-1234", "503-123-1234", "1/140/2018 19:39", "12/1/2018 19:39");
  }

  @Test(expected = PhoneBillException.class)
  public void PhoneCallInvalidDay3() throws Exception
  {
    PhoneCall call = new PhoneCall( "503-123-1234", "503-123-1234", "2/30/2018 19:39", "12/1/2018 19:39");
  }

  @Test(expected = PhoneBillException.class)
  public void PhoneCallInvalidDay4() throws Exception
  {
    PhoneCall call = new PhoneCall("503-123-1234", "503-123-1234", "1/0/2018 19:39", "12/1/2018 19:39");
  }

  @Test(expected = PhoneBillException.class)
  public void PhoneCallInvalidDay5() throws Exception
  {
    PhoneCall call = new PhoneCall("503-123-1234", "503-123-1234", "1//2018 19:39", "12/1/2018 19:39");
  }
}
