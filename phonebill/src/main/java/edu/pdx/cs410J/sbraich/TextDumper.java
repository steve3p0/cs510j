package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.AbstractPhoneBill;
import edu.pdx.cs410J.PhoneBillDumper;
import java.io.IOException;

//public class TextDumper implements PhoneBillDumper<T extends AbstractPhoneBill>
public class TextDumper implements PhoneBillDumper<PhoneBill>
{
    public void dump(PhoneBill bill) throws IOException
    {
        throw new UnsupportedOperationException("Not implemented");
    }
}

