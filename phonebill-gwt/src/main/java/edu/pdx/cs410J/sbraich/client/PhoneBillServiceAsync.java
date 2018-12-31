package edu.pdx.cs410J.sbraich.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Date;
import java.util.List;

/**
 * The client-side interface to the phone bill service
 */
public interface PhoneBillServiceAsync
{
    void getDummyPhoneBill(AsyncCallback<PhoneBill> async);

    /**
     * Load the app with test data
     */
    void loadTestData(List<PhoneBill> testdata, AsyncCallback<Void> async);

    void getPhoneBills(AsyncCallback<List<PhoneBill>> async);

    /**
     * Return the current date/time on the server
     */
    void getPhoneBill(String customer, AsyncCallback<PhoneBill> async);

    void addPhoneBill(String customer, AsyncCallback<Void> async);

    void addPhoneCall(String customer, PhoneCall call, AsyncCallback<Void> async);

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
