package edu.pdx.cs410J.sbraich;

import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;

import static edu.pdx.cs410J.sbraich.PhoneBillServlet.CUSTOMER_PARAMETER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.*;

/**
 * A unit test for the {@link PhoneBillServlet}.  It uses mockito to
 * provide mock http requests and responses.
 */
public class PhoneBillServletTest
{
    @Test
    public void initiallyServletContainsNoPhoneBills() throws ServletException, IOException
    {
        PhoneBillServlet servlet = new PhoneBillServlet();
        
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        PrintWriter mockPrintWriter = mock(PrintWriter.class);

        when(mockResponse.getWriter()).thenReturn(mockPrintWriter);

        when(mockRequest.getParameter(CUSTOMER_PARAMETER)).thenReturn("Customer");

        servlet.doGet(mockRequest, mockResponse);

        verify(mockResponse).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    public void addPhoneBill() throws ServletException, IOException
    {
        PhoneBillServlet servlet = new PhoneBillServlet();

        String customer = "Customer";
        String caller = "123-456-8901";
        String callee = "234-567-1234";
        String startTime = "9/20/2018 7:15 AM";
        String endTime = "9/20/2018 7:30 AM";

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getParameter("customer")).thenReturn(customer);
        when(mockRequest.getParameter("caller")).thenReturn(caller);
        when(mockRequest.getParameter("callee")).thenReturn(callee);
        when(mockRequest.getParameter("startTime")).thenReturn(startTime);
        when(mockRequest.getParameter("endTime")).thenReturn(endTime);

        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        PrintWriter mockPrintWriter = mock(PrintWriter.class);

        when(mockResponse.getWriter()).thenReturn(mockPrintWriter);

        servlet.doPost(mockRequest, mockResponse);

        verify(mockResponse).setStatus(HttpServletResponse.SC_OK);

        PhoneBill bill = servlet.getPhoneBill(customer);
        assertThat(bill, not(nullValue()));
        assertThat(bill.getCustomer(), equalTo(customer));

        Collection<PhoneCall> calls = bill.getPhoneCalls();
        assertThat(calls.size(), equalTo(1));

        PhoneCall call = calls.iterator().next();
        assertThat(call.getCaller(), equalTo(caller));
        assertThat(call.getCallee(), equalTo(callee));
        assertThat(call.getStartTime(), equalTo(new Date(startTime)));
        assertThat(call.getEndTime(), equalTo(new Date(endTime)));
    }

    @Test
    public void getReturnsPrettyPhoneBill() throws IOException, ServletException
    {
        PhoneBillServlet servlet = new PhoneBillServlet();

        String customer = "Customer";
        String callerNumber = "123-456-7890";
        String calleeNumber = "234-567-8901";
        String start = "9/20/2018 7:15 AM";
        String end = "9/20/2018 7:30 AM";

        PhoneBill bill = new PhoneBill(customer);
        PhoneCall call = new PhoneCall(callerNumber, calleeNumber, start, end);
        bill.addPhoneCall(call);
        servlet.addPhoneBill(bill);

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        PrintWriter mockPrintWriter = mock(PrintWriter.class);

        when(mockResponse.getWriter()).thenReturn(mockPrintWriter);

        when(mockRequest.getParameter(CUSTOMER_PARAMETER)).thenReturn("Customer");

        servlet.doGet(mockRequest, mockResponse);

        verify(mockResponse).setStatus(HttpServletResponse.SC_OK);
        verify(mockPrintWriter).println(customer);
        verify(mockPrintWriter).println(call.toString());
    }
}
