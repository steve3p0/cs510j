package edu.pdx.cs410J.sbraich.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

/**
 * A GWT remote service that returns a dummy Phone Bill
 */
@RemoteServiceRelativePath("phoneBill")
public interface PhoneBillService extends RemoteService
{
    /**
     * Returns the a dummy Phone Bill
     */
    public PhoneBill getDummyPhoneBill();

    /**
     * Load the app with test data
     */
    public void loadTestData(List<PhoneBill> testdata);

    /**
     * Returns a phone bill that matches customer name
     */
    public PhoneBill getPhoneBill(String customer);

    public List<PhoneBill> getPhoneBills();

    public void addPhoneBill(String customer);

    public void addPhoneCall(String customer, PhoneCall call);

    /**
     * Always throws an undeclared exception so that we can see GWT handles it.
     */
    void throwUndeclaredException();

    /**
     * Always throws a declared exception so that we can see GWT handles it.
     */
    void throwDeclaredException() throws IllegalStateException;

}
