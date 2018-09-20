package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.web.HttpRequestHelper;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNot.not;

/**
 * Integration test that tests the REST calls made by {@link PhoneBillRestClient}
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PhoneBillRestClientIT
{
    private static final String HOSTNAME = "localhost";
    private static final String PORT = System.getProperty("http.port", "8080");

    private PhoneBillRestClient newPhoneBillRestClient()
    {
        int port = Integer.parseInt(PORT);
        return new PhoneBillRestClient(HOSTNAME, port);
    }

    @Test
    public void test0RemoveAllPhoneBills() throws IOException
    {
        PhoneBillRestClient client = newPhoneBillRestClient();
        client.removeAllPhoneBills();
    }

    @Test(expected = NoSuchPhoneBillException.class)
    public void test1EmptyServerThrowsNoSuchPhoneBillException() throws IOException
    {
        PhoneBillRestClient client = newPhoneBillRestClient();
        client.getPrettyPhoneBill("No such customer");
    }

    @Test
    public void test2AddOnePhoneCall() throws IOException, PhoneBillException
    {
        PhoneBillRestClient client = newPhoneBillRestClient();
        String callerNumber = "123-456-7890";
        String calleeNumber = "234-567-8901";
        String start = "9/20/2018 7:15 AM";
        String end = "9/20/2018 7:30 AM";

        PhoneCall phoneCall = new PhoneCall(callerNumber, calleeNumber, start, end);

        String customer = "Customer";
        client.addPhoneCall(customer, phoneCall);

        String pretty = client.getPrettyPhoneBill(customer);
        assertThat(pretty, containsString(customer));
        assertThat(pretty, containsString(callerNumber));
        assertThat(pretty, containsString(calleeNumber));

        assertThat(pretty, containsString(start));
        assertThat(pretty, containsString(end));
    }

    @Test(expected = PhoneBillException.class)
    public void test_MalformedTime() throws IOException, PhoneBillException
    {
        PhoneBillRestClient client = newPhoneBillRestClient();
        String callerNumber = "123-456-7890";
        String calleeNumber = "234-567-8901";
        String start = "9/20/20180 7:15 AM";
        String end = "9/20/2018 7:30 AM";

        PhoneCall phoneCall = new PhoneCall(callerNumber, calleeNumber, start, end);

        String customer = "Customer";
        client.addPhoneCall(customer, phoneCall);

        String pretty = client.getPrettyPhoneBill(customer);
    }

    @Test
    public void testSearch() throws IOException, PhoneBillException
    {
        String startSearch = "9/19/2018 12:00 AM";
        String endSearch = "9/23/2018 11:59 PM";

        PhoneBillRestClient client = newPhoneBillRestClient();
        client.removeAllPhoneBills();

        String callerNumber1 = "111-111-1111";
        String calleeNumber1 = "101-101-1010";
        String start1 = "9/20/2018 7:15 AM";
        String end1 = "9/20/2018 7:30 AM";

        PhoneCall call1 = new PhoneCall(callerNumber1, calleeNumber1, start1, end1);

        String callerNumber2 = "222-222-2222";
        String calleeNumber2 = "202-202-2222";
        String start2 = "9/21/2018 7:15 AM";
        String end2 = "9/21/2018 7:30 AM";

        PhoneCall call2 = new PhoneCall(callerNumber2, calleeNumber2, start2, end2);

        String callerNumber3 = "333-333-3333";
        String calleeNumber3 = "303-303-3030";
        String start3 = "9/25/2018 7:15 AM";
        String end3 = "9/25/2018 7:30 AM";

        PhoneCall call3 = new PhoneCall(callerNumber3, calleeNumber3, start3, end3);

        String customer = "Customer";
        client.addPhoneCall(customer, call1);
        client.addPhoneCall(customer, call2);
        client.addPhoneCall(customer, call3);

        String pretty = client.searchPhoneCalls(customer, startSearch, endSearch);
        assertThat(pretty, containsString(customer));
        assertThat(pretty, containsString(callerNumber1));
        assertThat(pretty, containsString(calleeNumber1));
        assertThat(pretty, containsString(start1));
        assertThat(pretty, containsString(end1));
        assertThat(pretty, containsString(callerNumber2));
        assertThat(pretty, containsString(calleeNumber2));
        assertThat(pretty, containsString(start2));
        assertThat(pretty, containsString(end2));

        assertThat(pretty, not(containsString(callerNumber3)));
        assertThat(pretty, not(containsString(calleeNumber3)));
        assertThat(pretty, not(containsString(start3)));
        assertThat(pretty, not(containsString(end3)));
    }

    @Test
    public void test4MissingRequiredParameterReturnsPreconditionFailed() throws IOException
    {
        PhoneBillRestClient client = newPhoneBillRestClient();
        HttpRequestHelper.Response response = client.postToMyURL();
        assertThat(response.getContent(), containsString(Messages.missingRequiredParameter("customer")));
        assertThat(response.getCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
    }
}
