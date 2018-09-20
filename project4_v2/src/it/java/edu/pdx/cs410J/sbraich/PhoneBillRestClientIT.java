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

        DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
        assertThat(pretty, containsString(start));
        assertThat(pretty, containsString(end));
    }

    @Ignore
    @Test
    public void test4MissingRequiredParameterReturnsPreconditionFailed() throws IOException
    {
        PhoneBillRestClient client = newPhoneBillRestClient();
        HttpRequestHelper.Response response = client.postToMyURL();
        assertThat(response.getContent(), containsString(Messages.missingRequiredParameter("word")));
        assertThat(response.getCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
    }

}
