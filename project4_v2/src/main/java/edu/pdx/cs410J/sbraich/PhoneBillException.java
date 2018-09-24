package edu.pdx.cs410J.sbraich;

/**
 * PhoneBill Project Custom Exception class
 */
public class PhoneBillException extends RuntimeException //Exception
{
    /**
     * Constructor for PhoneBill Exception Class
     * @param s String of the message to be reported
     */
    public PhoneBillException(String s)
    {
        // Call constructor of parent Exception
        super(s);
    }
}
