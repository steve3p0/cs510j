package edu.pdx.cs410J.sbraich;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for the {@link PhoneCall} class.
 */
public class PhoneCallTest {

  @Test(expected = UnsupportedOperationException.class)
  public void getStartTimeStringNeedsToBeImplemented()
  {
    PhoneCall call = new PhoneCall("123-123-1234", "123-123-1234", "1/15/2018 19:39","1/15/2018 20:39");

    call.getStartTimeString();
  }

  @Test
  public void initiallyAllPhoneCallsHaveTheSameCallee()
  {
    PhoneCall call = new PhoneCall("123-123-1234", "123-123-1234", "1/15/2018 19:39","1/15/2018 20:39");

    assertThat(call.getCallee(), containsString("not implemented"));
  }

  @Test
  public void forProject1ItIsOkayIfGetStartTimeReturnsNull()
  {
    PhoneCall call = new PhoneCall("123-123-1234", "123-123-1234", "1/15/2018 19:39","1/15/2018 20:39");

    assertThat(call.getStartTime(), is(nullValue()));
  }
  
}
