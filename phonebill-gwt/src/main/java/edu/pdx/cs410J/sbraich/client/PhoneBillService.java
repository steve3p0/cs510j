package edu.pdx.cs410J.sbraich.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

/**
 * A GWT remote service that returns a dummy Phone Bill
 */
@RemoteServiceRelativePath("phoneBill")
public interface PhoneBillService extends RemoteService {

  /**
   * Load the app with test data
   */
  public void loadTestData(List<PhoneBill> testdata);

  /**
   * Returns the a dummy Phone Bill
   */
  public PhoneBill getPhoneBill();

  public List<PhoneBill> getPhoneBills();

  /**
   * Always throws an undeclared exception so that we can see GWT handles it.
   */
  void throwUndeclaredException();

  /**
   * Always throws a declared exception so that we can see GWT handles it.
   */
  void throwDeclaredException() throws IllegalStateException;

}
