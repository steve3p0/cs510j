package edu.pdx.cs410J.whitlock;

import com.google.common.annotations.VisibleForTesting;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>PhoneBill</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple dictionary of words
 * and their definitions.
 */
public class PhoneBillServlet extends HttpServlet
{
    static final String CUSTOMER_PARAMETER = "customer";
    static final String DEFINITION_PARAMETER = "definition";
    private static final String CALLER_PARAMETER = "caller";
    private static final String CALLEE_PARAMETER = "callee";
    private static final String START_TIME_PARAMETER = "startTime";
    private static final String END_TIME_PARAMETER = "endTime";

    private final Map<String, String> dictionary = new HashMap<>();
    private Map<String, PhoneBill> bills = new HashMap<>();

    /**
     * Handles an HTTP GET request from a client by writing the definition of the
     * word specified in the "word" HTTP parameter to the HTTP response.  If the
     * "word" parameter is not specified, all of the entries in the dictionary
     * are written to the HTTP response.
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );

        String customer = getParameter(CUSTOMER_PARAMETER, request );
        writePrettyPhoneBill(customer, response);
    }

    private void writePrettyPhoneBill(String customer, HttpServletResponse response) throws IOException {
        PhoneBill bill = getPhoneBill(customer);
        if (bill == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);

        } else {
            PrintWriter writer = response.getWriter();
            PrettyPrinter pretty = new PrettyPrinter(writer);
            pretty.dump(bill);
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    /**
     * Handles an HTTP POST request by storing the dictionary entry for the
     * "word" and "definition" request parameters.  It writes the dictionary
     * entry to the HTTP response.
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );

        String customer = getParameter(CUSTOMER_PARAMETER, request );
        if (customer == null) {
            missingRequiredParameter(response, CUSTOMER_PARAMETER);
            return;
        }

        PhoneBill bill = getPhoneBill(customer);
        if (bill == null) {
            bill = new PhoneBill(customer);
            addPhoneBill(bill);
        }

        String caller = getParameter(CALLER_PARAMETER, request);
        String callee = getParameter(CALLEE_PARAMETER, request);
        String startTime = getParameter(START_TIME_PARAMETER, request);
        String endTime = getParameter(END_TIME_PARAMETER, request);

        Date startDate = new Date(Long.parseLong(startTime));
        Date endDate = new Date(Long.parseLong(endTime));

        PhoneCall call = new PhoneCall(caller, callee, startDate, endDate);
        bill.addPhoneCall(call);

        response.setStatus( HttpServletResponse.SC_OK);
    }

    /**
     * Handles an HTTP DELETE request by removing all dictionary entries.  This
     * behavior is exposed for testing purposes only.  It's probably not
     * something that you'd want a real application to expose.
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");

        this.dictionary.clear();

        PrintWriter pw = response.getWriter();
        pw.println(Messages.allDictionaryEntriesDeleted());
        pw.flush();

        response.setStatus(HttpServletResponse.SC_OK);

    }

    /**
     * Writes an error message about a missing parameter to the HTTP response.
     *
     * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
     */
    private void missingRequiredParameter( HttpServletResponse response, String parameterName )
        throws IOException
    {
        String message = Messages.missingRequiredParameter(parameterName);
        response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
    }

    /**
     * Writes the definition of the given word to the HTTP response.
     *
     * The text of the message is formatted with
     * {@link Messages#formatDictionaryEntry(String, String)}
     */
    private void writeDefinition(String word, HttpServletResponse response ) throws IOException
    {
        String definition = this.dictionary.get(word);

        System.out.println("The definition of " + word + " is \"" + definition + "\"");

        PrintWriter pw = response.getWriter();
        pw.println(Messages.formatDictionaryEntry(word, definition));

        pw.flush();

        response.setStatus( HttpServletResponse.SC_OK );
    }

    /**
     * Writes all of the dictionary entries to the HTTP response.
     *
     * The text of the message is formatted with
     * {@link Messages#formatDictionaryEntry(String, String)}
     */
    private void writeAllDictionaryEntries(HttpServletResponse response ) throws IOException
    {
        PrintWriter pw = response.getWriter();
        Messages.formatDictionaryEntries(pw, dictionary);

        System.out.println("Writing all dictionary entries");

        pw.flush();

        response.setStatus( HttpServletResponse.SC_OK );
    }

    /**
     * Returns the value of the HTTP request parameter with the given name.
     *
     * @return <code>null</code> if the value of the parameter is
     *         <code>null</code> or is the empty string
     */
    private String getParameter(String name, HttpServletRequest request) {
      String value = request.getParameter(name);
      if (value == null || "".equals(value)) {
        return null;

      } else {
        return value;
      }
    }

    @VisibleForTesting
    PhoneBill getPhoneBill(String customer) {
        return this.bills.get(customer);
    }

    @VisibleForTesting
    void addPhoneBill(PhoneBill bill) {
        this.bills.put(bill.getCustomer(), bill);
    }
}
