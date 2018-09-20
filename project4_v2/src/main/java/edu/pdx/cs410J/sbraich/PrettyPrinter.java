package edu.pdx.cs410J.sbraich;

import edu.pdx.cs410J.PhoneBillDumper;

import java.io.IOException;
import java.io.PrintWriter;

public class PrettyPrinter implements PhoneBillDumper<PhoneBill> {
  private final PrintWriter writer;

  public PrettyPrinter(PrintWriter writer) {
    this.writer = writer;
  }

  @Override
  public void dump(PhoneBill bill) throws IOException {
    writer.println(bill.getCustomer());
    bill.getPhoneCalls().forEach((call) -> writer.println(call.toString()));
  }
}
