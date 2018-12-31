package edu.pdx.cs410J.sbraich.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.Collection;
import java.util.Date;
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

    /**
     * Gets all phone bills from the server
     * @return
     */
    public List<PhoneBill> getPhoneBills();

    /**
     * Adds a phone bill to the server
     * @param customer
     */
    public void addPhoneBill(String customer);

    /**
     * Adds a phone call to the server
     * @param customer
     * @param call
     */
    public void addPhoneCall(String customer, PhoneCall call);

    /**
     * Filters Phone Calls
     * @param customer
     * @param caller
     * @param callee
     * @param start
     * @param end
     * @return
     */
    public List<PhoneCall> filterPhoneCalls(String customer, String caller, String callee, Date start, Date end);

    /**
     * Always throws an undeclared exception so that we can see GWT handles it.
     */
    void throwUndeclaredException();

    /**
     * Always throws a declared exception so that we can see GWT handles it.
     */
    void throwDeclaredException() throws IllegalStateException;

}
