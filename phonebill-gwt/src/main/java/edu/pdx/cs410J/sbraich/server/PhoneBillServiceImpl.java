package edu.pdx.cs410J.sbraich.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.sbraich.client.PhoneBill;
import edu.pdx.cs410J.sbraich.client.PhoneCall;
import edu.pdx.cs410J.sbraich.client.PhoneBillService;

import java.util.ArrayList;
import java.util.List;

/**
 * The server-side implementation of the Phone Bill service
 */
public class PhoneBillServiceImpl extends RemoteServiceServlet implements PhoneBillService
{
    private List bills = new ArrayList<PhoneBill>();

    /**
     * Testdata for initial load of Phonbill app
     * @return
     */
    @Override
    public void loadTestData(List<PhoneBill> testdata)
    {
        this.bills = testdata;
    }

    @Override
    public PhoneBill getPhoneBill()
    {
        PhoneBill phonebill = new PhoneBill();
        phonebill.addPhoneCall(new PhoneCall());
        return phonebill;
    }

    @Override
    public List<PhoneBill> getPhoneBills()
    {
        return this.bills;
    }

    @Override
    public void throwUndeclaredException()
    {
        throw new IllegalStateException("Expected undeclared exception");
    }

    @Override
    public void throwDeclaredException() throws IllegalStateException
    {
        throw new IllegalStateException("Expected declared exception");
    }

    /**
     * Log unhandled exceptions to standard error
     *
     * @param unhandled The exception that wasn't handled
     */
    @Override
    protected void doUnexpectedFailure(Throwable unhandled)
    {
        unhandled.printStackTrace(System.err);
        super.doUnexpectedFailure(unhandled);
    }




}
