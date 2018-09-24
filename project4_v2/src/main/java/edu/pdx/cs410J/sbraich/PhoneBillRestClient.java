package edu.pdx.cs410J.sbraich;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;

import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * A helper class for accessing the rest client.  Note that this class provides
 * an example of how to make gets and posts to a URL.  You'll need to change it
 * to do something other than just send dictionary entries.
 */
public class PhoneBillRestClient extends HttpRequestHelper
{
    private static final String WEB_APP = "phonebill";
    private static final String SERVLET = "calls";

    private final String url;

    /**
     * Creates a client to the Phone Bil REST service running on the given host and port
     * @param hostName The name of the host
     * @param port The port
     */
    public PhoneBillRestClient( String hostName, int port )
    {
        this.url = String.format( "http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET );
    }

    /**
     * Returns all dictionary entries from the server
     * @param customerName
     */
    public String getPrettyPhoneBill(String customerName) throws IOException
    {
        Response response = get(this.url, "customer", customerName);

        throwExceptionIfNotOkayHttpStatus(response);

        return response.getContent();
    }

    /**
     * Adds a phone call
     * @param customerName
     * @param call
     * @throws IOException
     */
    public void addPhoneCall(String customerName, PhoneCall call) throws IOException
    {
        String[] postParameters =
        {
            "customer", customerName,
            "caller", call.getCaller(),
            "callee", call.getCallee(),
            "startTime", call.getStartTimeString(),
            "endTime", call.getEndTimeString(),
        };

        Response response = postToMyURL(postParameters);
        throwExceptionIfNotOkayHttpStatus(response);
    }

    /**
     * Search phone calls between startTimeAndDate and endTimeAndDate
     * @param customer
     * @param startTimeAndDate
     * @param endTimeAndDate
     * @return
     * @throws IOException
     */
    public String searchPhoneCalls(String customer, String startTimeAndDate, String endTimeAndDate) throws IOException
    {
        Response response = get(this.url,
                "customer", customer,
                "startTime", startTimeAndDate,
                "endTime", endTimeAndDate);
        throwExceptionIfNotOkayHttpStatus(response);
        return response.getContent();
    }

    /**
     * Make an HTTP POST call
     * @param phoneCallInformation
     * @return
     * @throws IOException
     */
    @VisibleForTesting
    Response postToMyURL(String... phoneCallInformation) throws IOException
    {
        try
        {
            return post(this.url, phoneCallInformation);
        }
        catch (java.net.ConnectException e)
        {
            //Response r = new Response();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Remove all phone bills from hashmap
     * @throws IOException
     */
    public void removeAllPhoneBills() throws IOException
    {
        try
        {
            Response response = delete(this.url);
            throwExceptionIfNotOkayHttpStatus(response);
        }
        catch (java.net.ConnectException e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Throw an Exception if the HTTP Status is not OK
     * @param response
     * @return
     */
    private Response throwExceptionIfNotOkayHttpStatus(Response response)
    {
        int code = response.getCode();
        if (code == HTTP_NOT_FOUND)
        {
            String customer = response.getContent();
            throw new NoSuchPhoneBillException(customer);
        }
        else if (code != HTTP_OK)
        {
            throw new PhoneBillRestException(code);
        }
        return response;
    }

    /**
     * Class for PhoneBillRestException
     */
    private class PhoneBillRestException extends RuntimeException
    {
        /**
         * Constructor for PhoneBillRestException
         * @param httpStatusCode
         */
        public PhoneBillRestException(int httpStatusCode)
        {
            super("Got an HTTP Status Code of " + httpStatusCode);
        }
    }

}
