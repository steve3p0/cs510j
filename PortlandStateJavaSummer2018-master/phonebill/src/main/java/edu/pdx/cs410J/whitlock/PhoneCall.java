package edu.pdx.cs410J.whitlock;

import edu.pdx.cs410J.AbstractPhoneCall;

import java.text.DateFormat;
import java.util.Date;

public class PhoneCall extends AbstractPhoneCall {
  private final String callerNumber;
  private final String calleeNumber;
  private final Date startTime;
  private final Date endTime;

  public PhoneCall(String callerNumber, String calleeNumber, Date startTime, Date endTime) {
    this.callerNumber = callerNumber;
    this.calleeNumber = calleeNumber;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  @Override
  public String getCaller() {
    return this.callerNumber;
  }

  @Override
  public String getCallee() {
    return this.calleeNumber;
  }

  @Override
  public String getStartTimeString() {
    return formatDate(this.startTime);
  }

  private String formatDate(Date date) {
    return DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(date);
  }

  @Override
  public String getEndTimeString() {
    return formatDate(this.endTime);
  }

  @Override
  public Date getStartTime() {
    return this.startTime;
  }

  @Override
  public Date getEndTime() {
    return this.endTime;
  }
}
