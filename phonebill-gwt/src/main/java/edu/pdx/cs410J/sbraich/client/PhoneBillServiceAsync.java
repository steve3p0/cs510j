package edu.pdx.cs410J.sbraich.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Date;
import java.util.List;

/**
 * The client-side interface to the phone bill service
 */
public interface PhoneBillServiceAsync
{
    /**
     * Test function
     * @param async
     */
    void getDummyPhoneBill(AsyncCallback<PhoneBill> async);

    /**
     * Load the app with test data
     */
    void loadTestData(List<PhoneBill> testdata, AsyncCallback<Void> async);

    /**
     * Gets all phone bills from the server
     * @param async
     */
    void getPhoneBills(AsyncCallback<List<PhoneBill>> async);

    /**
     * Return the current date/time on the server
     */
    void getPhoneBill(String customer, AsyncCallback<PhoneBill> async);

    /**
     * Adds a Phone Bill to the server
     * @param customer
     * @param async
     */
    void addPhoneBill(String customer, AsyncCallback<Void> async);

    /**
     * Adds a Phonel CAll to the server
     * @param customer
     * @param call
     * @param async
     */
    void addPhoneCall(String customer, PhoneCall call, AsyncCallback<Void> async);

    /**
     * Filters PhoneCalls based on parameter criteria
     * @param customer
     * @param caller
     * @param callee
     * @param start
     * @param end
     * @param async
     */
    void filterPhoneCalls(String customer, String caller, String callee, Date start, Date end, AsyncCallback<List<PhoneCall>> async);

    /**
     * Always throws an exception so that we can see how to handle uncaught
     * exceptions in GWT.
     */
    void throwUndeclaredException(AsyncCallback<Void> async);

    /**
     * Always throws a declared exception so that we can see GWT handles it.
     */
    void throwDeclaredException(AsyncCallback<Void> async);

}
