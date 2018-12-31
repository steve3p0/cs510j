package edu.pdx.cs410J.sbraich.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.sbraich.client.PhoneBill;
import edu.pdx.cs410J.sbraich.client.PhoneCall;
import edu.pdx.cs410J.sbraich.client.PhoneBillService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * The server-side implementation of the Phone Bill service
 */
public class PhoneBillServiceImpl extends RemoteServiceServlet implements PhoneBillService
{
    private List bills = new ArrayList<PhoneBill>();

    @Override
    public PhoneBill getDummyPhoneBill()
    {
        PhoneBill phonebill = new PhoneBill();
        phonebill.addPhoneCall(new PhoneCall());
        return phonebill;
    }

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
    public PhoneBill getPhoneBill(String customer) throws IllegalStateException
    {
        for (Object b : bills)
        {
            PhoneBill bill = (PhoneBill) b;
            if (customer.equals(bill.customer))
            {
                return bill;
            }
        }

        throw new IllegalStateException("Customer Not Found: " + customer);
    }

    @Override
    public List<PhoneCall> filterPhoneCalls(String customer, String caller, String callee, Date start, Date end)
    {
        PhoneBill bill = getPhoneBill(customer);
        List<PhoneCall> filteredCalls = new ArrayList(bill.calls);

        if (!isNullOrEmpty(caller))
        {
            filteredCalls = filterPhoneCalls_ByCaller(filteredCalls, caller);
        }

        if (!isNullOrEmpty(callee))
        {
            filteredCalls = filterPhoneCalls_ByCallee(filteredCalls, callee);
        }

        if ((start == null && end != null) || (start != null && end == null))
        {
            throw new IllegalStateException("Search by date requires both start and end dates.");
        }
        else if (start != null && end != null)
        {
            filteredCalls = filterPhoneCalls_ByDate(filteredCalls, start, end);
        }

        return filteredCalls;
    }

    private List<PhoneCall> filterPhoneCalls_ByCaller(List<PhoneCall> calls, String caller)
    {
        List<PhoneCall> filteredCalls = new ArrayList<PhoneCall>();

        for (PhoneCall call : calls)
        {
            if (caller.equals(call.callerNumber))
            {
                filteredCalls.add(call);
            }
        }

        return filteredCalls;
    }

    private List<PhoneCall> filterPhoneCalls_ByCallee(List<PhoneCall> calls, String callee)
    {
        List<PhoneCall> filteredCalls = new ArrayList<PhoneCall>();

        for (PhoneCall call : calls)
        {
            if (callee.equals(call.calleeNumber))
            {
                filteredCalls.add(call);
            }
        }

        return filteredCalls;
    }

    private List<PhoneCall>filterPhoneCalls_ByDate(List<PhoneCall> calls, Date start, Date end)
    {
        List<PhoneCall> filteredCalls = new ArrayList<PhoneCall>();

        for (PhoneCall call : calls)
        {
            // NOTE: Criteria for search:
            // if start date falls within search start and end,
            // it will be returned, regardless of end date
            if (call.getStartTime().after(start) && call.getStartTime().before(end))
            {
                filteredCalls.add(call);
            }
        }

        return filteredCalls;
    }

    @Override
    public List<PhoneBill> getPhoneBills()
    {
        return this.bills;
    }

    @Override
    public void addPhoneBill(String customer)
    {
        PhoneBill bill = new PhoneBill(customer);

        bills.add(bill);
    }

    @Override
    public void addPhoneCall(String customer, PhoneCall call)
    {
        PhoneBill bill = getPhoneBill(customer);
        bill.addPhoneCall(call);
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
