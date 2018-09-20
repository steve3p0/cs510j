package edu.pdx.cs410J.sbraich;

public class NoSuchPhoneBillException extends RuntimeException
{
    //private final String customerName;

    public NoSuchPhoneBillException(String s)
    {
        //this.customerName = customerName;

        super(s);
    }
}
