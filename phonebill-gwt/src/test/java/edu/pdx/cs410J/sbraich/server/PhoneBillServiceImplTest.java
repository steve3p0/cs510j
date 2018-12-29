package edu.pdx.cs410J.sbraich.server;

import edu.pdx.cs410J.sbraich.client.PhoneBill;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PhoneBillServiceImplTest
{

    @Test
    public void serviceReturnsExpectedPhoneBill()
    {
        PhoneBillServiceImpl service = new PhoneBillServiceImpl();
        PhoneBill bill = service.getDummyPhoneBill();
        assertThat(bill.getPhoneCalls().size(), equalTo(1));
    }
}
