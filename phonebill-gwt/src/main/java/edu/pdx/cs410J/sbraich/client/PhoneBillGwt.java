package edu.pdx.cs410J.sbraich.client;

import com.google.common.annotations.VisibleForTesting;
//import com.google.common.collect.Iterables;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.BRElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

//import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;

import com.google.gwt.user.cellview.client.*;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
//import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.user.client.ui.Label;

import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.extras.datepicker.client.ui.DatePicker;
import org.gwtbootstrap3.extras.datetimepicker.client.ui.DateTimePicker;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;


/**
 * A basic GWT class that makes sure that we can send an Phone Bill back from the server
 */
public class PhoneBillGwt implements EntryPoint
{
    private final Alerter alerter;
    private final PhoneBillServiceAsync phoneBillService;
    private final Logger logger;

    private ListBox billsListBox = new ListBox();
    private CellTable<PhoneCall> callsTable = new CellTable<PhoneCall>();
    //private DataGrid<PhoneCall> callsTable = new DataGrid<PhoneCall>();
    private List bills = new ArrayList<PhoneBill>();

    //@VisibleForTesting
    Button showPhoneBillButton;

    @VisibleForTesting
    Button showUndeclaredExceptionButton;

    @VisibleForTesting
    Button showDeclaredExceptionButton;

    @VisibleForTesting
    Button showClientSideExceptionButton;

    public PhoneBillGwt()
    {
        this(new Alerter()
        {
            @Override
            public void alert(String message)
            {
                Window.alert(message);
            }
        });
    }

    @VisibleForTesting
    PhoneBillGwt(Alerter alerter)
    {
        this.alerter = alerter;
        this.phoneBillService = GWT.create(PhoneBillService.class);
        this.logger = Logger.getLogger("phoneBill");
        Logger.getLogger("").setLevel(Level.INFO);  // Quiet down the default logging
    }

    private void alertOnException(Throwable throwable)
    {
        Throwable unwrapped = unwrapUmbrellaException(throwable);
        StringBuilder sb = new StringBuilder();
        sb.append(unwrapped.toString());
        sb.append('\n');

        for (StackTraceElement element : unwrapped.getStackTrace())
        {
            sb.append("  at ");
            sb.append(element.toString());
            sb.append('\n');
        }

        this.alerter.alert(sb.toString());
    }

    private Throwable unwrapUmbrellaException(Throwable throwable)
    {
        if (throwable instanceof UmbrellaException)
        {
            UmbrellaException umbrella = (UmbrellaException) throwable;
            if (umbrella.getCauses().size() == 1)
            {
                return unwrapUmbrellaException(umbrella.getCauses().iterator().next());
            }

        }

        return throwable;
    }

    private void throwClientSideException()
    {
        logger.info("About to throw a client-side exception");
        throw new IllegalStateException("Expected exception on the client side");
    }

    private void showUndeclaredException()
    {
        logger.info("Calling throwUndeclaredException");
        phoneBillService.throwUndeclaredException(new AsyncCallback<Void>()
        {
            @Override
            public void onFailure(Throwable ex)
            {
                alertOnException(ex);
            }

            @Override
            public void onSuccess(Void aVoid)
            {
                alerter.alert("This shouldn't happen");
            }
        });
    }

    private void showDeclaredException()
    {
        logger.info("Calling throwDeclaredException");
        phoneBillService.throwDeclaredException(new AsyncCallback<Void>()
        {
            @Override
            public void onFailure(Throwable ex)
            {
                alertOnException(ex);
            }

            @Override
            public void onSuccess(Void aVoid)
            {
                alerter.alert("This shouldn't happen");
            }
        });
    }

    private CellTable<PhoneCall> showGrid()
    {
        CellTable<PhoneCall> table = new CellTable<PhoneCall>();
        table.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);

        // Add a text column to show the Caller Number.
        TextColumn<PhoneCall> callerColumn = new TextColumn<PhoneCall>()
        {
            @Override
            public String getValue(PhoneCall object)
            {
                return object.callerNumber;
            }
        };
        table.addColumn(callerColumn, "Caller");

        // Add a text column to show the Callee Number.
        TextColumn<PhoneCall> calleeColumn = new TextColumn<PhoneCall>()
        {
            @Override
            public String getValue(PhoneCall object)
            {
                return object.calleeNumber;
            }
        };
        table.addColumn(calleeColumn, "Callee");

        String DATE_TIME_FORMAT = "M/d/yyyy h:mm a";
        DateTimeFormat fmt = DateTimeFormat.getFormat(DATE_TIME_FORMAT);
        DateCell dateCell = new DateCell(fmt);
        Column<PhoneCall, Date> startColumn = new Column<PhoneCall, Date>(dateCell)
        {
            @Override
            public Date getValue(PhoneCall object)
            {
                return object.startTime;
            }
        };
        table.addColumn(startColumn, "Start Time");

        Column<PhoneCall, Date> endColumn = new Column<PhoneCall, Date>(dateCell)
        {
            @Override
            public Date getValue(PhoneCall object)
            {
                return object.endTime;
            }
        };
        table.addColumn(endColumn, "End Time");

        // Add a selection model to handle user selection.
        final SingleSelectionModel<PhoneCall> selectionModel = new SingleSelectionModel<PhoneCall>();
        table.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler()
        {
            public void onSelectionChange(SelectionChangeEvent event)
            {
                PhoneCall selected = selectionModel.getSelectedObject();
                if (selected != null)
                {
                    Window.alert("You selected: " + selected.callerNumber);
                }
            }
        });

        //table.setHeight("350px");

        return table;
    }

    @Override
    public void onModuleLoad()
    {
        setUpUncaughtExceptionHandler();

        // The UncaughtExceptionHandler won't catch exceptions during module load
        // So, you have to set up the UI after module load...
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand()
        {
            @Override
            public void execute()
            {
                setupUI();
            }
        });
    }

    private void loadBillsListBox()
    {
        phoneBillService.getPhoneBills(new AsyncCallback<List<PhoneBill>>()
        {
            @Override
            public void onFailure(Throwable throwable)
            {
                alerter.alert("phoneBillService.getPhoneBills FAILED");
            }

            @Override
            public void onSuccess(List<PhoneBill> list)
            {
                billsListBox.clear();

                bills = list;
                for (Object b : bills)
                {
                    PhoneBill bill = (PhoneBill) b;
                    billsListBox.addItem(bill.customer);
                }
            }
        });
    }

    private void loadTestPhoneBills()
    {
        // First Bill: Luke Skywalker
        PhoneBill bill1 = new PhoneBill("Luke Skywalker");

        PhoneCall bill1_call1 = new PhoneCall("318-467-8383", "503-867-5309", "12/01/2018 3:34 PM", "12/01/2018 3:37 PM");
        PhoneCall bill1_call2 = new PhoneCall("318-467-8383", "503-555-1212", "12/02/2018 1:12 PM", "12/02/2018 1:17 PM");
        PhoneCall bill1_call3 = new PhoneCall("318-467-8383", "503-730-5753", "12/03/2018 10:46 PM", "12/03/2018 10:55 PM");
        PhoneCall bill1_call4 = new PhoneCall("318-467-8383", "503-867-5309", "12/04/2018 11:03 PM", "12/05/2018 11:28 PM");
        PhoneCall bill1_call5 = new PhoneCall("318-467-8383", "503-555-1212", "12/05/2018 9:07 AM", "12/05/2018 9:08 AM");
        PhoneCall bill1_call6 = new PhoneCall("318-467-8383", "503-730-5753", "12/06/2018 12:33 PM", "12/06/2018 12:37 PM");
        PhoneCall bill1_call7 = new PhoneCall("318-467-8383", "503-555-1212", "12/07/2018 6:25 PM", "12/07/2018 6:26 PM");
        PhoneCall bill1_call8 = new PhoneCall("318-467-8383", "503-867-5309", "12/09/2018 5:29 PM", "12/09/2018 5:38 PM");
        PhoneCall bill1_call9 = new PhoneCall("318-467-8383", "503-730-5753", "12/09/2018 6:05 PM", "12/09/2018 6:17 PM");
        PhoneCall bill1_call10 = new PhoneCall("318-467-8383", "503-867-5309", "12/09/2018 6:19 PM", "12/09/2018 6:37 PM");

        bill1.addPhoneCall(bill1_call1);
        bill1.addPhoneCall(bill1_call2);
        bill1.addPhoneCall(bill1_call3);
        bill1.addPhoneCall(bill1_call4);
        bill1.addPhoneCall(bill1_call5);
        bill1.addPhoneCall(bill1_call6);
        bill1.addPhoneCall(bill1_call7);
        bill1.addPhoneCall(bill1_call8);
        bill1.addPhoneCall(bill1_call9);
        bill1.addPhoneCall(bill1_call10);

        // 2nd Bill: Mara Jade
        PhoneBill bill2 = new PhoneBill("Mara Jade");

        PhoneCall bill2_call1 = new PhoneCall("503-867-5309", "318-467-8383", "12/01/2018 1:11 AM", "12/01/2018 1:21 AM");
        PhoneCall bill2_call2 = new PhoneCall("503-867-5309", "503-730-5753", "12/03/2018 3:33 AM", "12/03/2018 3:38 AM");
        PhoneCall bill2_call3 = new PhoneCall("503-867-5309", "503-555-1212", "12/05/2018 5:05 AM", "12/05/2018 5:13 AM");
        PhoneCall bill2_call4 = new PhoneCall("503-867-5309", "503-555-1212", "12/07/2018 7:25 AM", "12/07/2018 7:26 AM");
        PhoneCall bill2_call5 = new PhoneCall("503-867-5309", "503-730-5753", "12/09/2018 9:09 AM", "12/09/2018 9:17 PM");

        bill2.addPhoneCall(bill2_call1);
        bill2.addPhoneCall(bill2_call2);
        bill2.addPhoneCall(bill2_call3);
        bill2.addPhoneCall(bill2_call4);
        bill2.addPhoneCall(bill2_call5);

        // 3rd Bill: Lando
        PhoneBill bill3 = new PhoneBill("Lando Calrissian");

        PhoneCall bill3_call1 = new PhoneCall("408-555-1234", "503-867-5309", "11/28/2018 6:19 PM", "12/09/2018 6:37 PM");
        PhoneCall bill3_call2 = new PhoneCall("408-555-1234", "503-555-1212", "12/02/2018 2:22 AM", "12/02/2018 2:27 AM");
        PhoneCall bill3_call3 = new PhoneCall("408-555-1234", "503-867-5309", "12/04/2018 4:44 PM", "12/05/2018 4:54 PM");
        PhoneCall bill3_call4 = new PhoneCall("408-555-1234", "503-867-5309", "12/19/2018 5:29 AM", "12/09/2018 5:38 AM");
        PhoneCall bill3_call5 = new PhoneCall("408-555-1234", "503-730-5753", "12/21/2018 6:05 PM", "12/09/2018 6:17 PM");
        PhoneCall bill3_call6 = new PhoneCall("408-555-1234", "503-730-5753", "12/22/2018 12:33 AM", "12/06/2018 12:37 AM");
        PhoneCall bill3_call7 = new PhoneCall("408-555-1234", "503-555-1212", "12/24/2018 6:25 PM", "12/07/2018 6:26 PM");

        bill3.addPhoneCall(bill3_call1);
        bill3.addPhoneCall(bill3_call2);
        bill3.addPhoneCall(bill3_call3);
        bill3.addPhoneCall(bill3_call4);
        bill3.addPhoneCall(bill3_call5);
        bill3.addPhoneCall(bill3_call6);
        bill3.addPhoneCall(bill3_call7);

        // 4th Bill: Han Solo
        PhoneBill bill4 = new PhoneBill("Han Solo");

        PhoneCall bill4_call1 = new PhoneCall("999-999-9999", "503-555-1212", "12/05/2018 5:55 AM", "12/05/2018 5:59 AM");
        PhoneCall bill4_call2 = new PhoneCall("999-999-9999", "503-555-1212", "12/07/2018 7:27 PM", "12/07/2018 7:36 PM");
        PhoneCall bill4_call3 = new PhoneCall("999-999-9999", "503-867-5309", "12/09/2018 9:29 PM", "12/09/2018 9:38 PM");
        PhoneCall bill4_call4 = new PhoneCall("999-999-9999", "503-730-5753", "12/11/2018 11:11 PM", "12/11/2018 11:22 PM");
        PhoneCall bill4_call5 = new PhoneCall("999-999-9999", "503-867-5309", "12/13/2018 1:13 PM", "12/13/2018 1:27 PM");
        PhoneCall bill4_call6 = new PhoneCall("999-999-9999", "503-867-5309", "12/15/2018 3:15 PM", "12/15/2018 3:17 PM");

        bill4.addPhoneCall(bill4_call1);
        bill4.addPhoneCall(bill4_call2);
        bill4.addPhoneCall(bill4_call3);
        bill4.addPhoneCall(bill4_call4);
        bill4.addPhoneCall(bill4_call5);
        bill4.addPhoneCall(bill4_call6);

        // 5th Bill: Princess Leia
        PhoneBill bill5 = new PhoneBill("Princess Leia");

        PhoneCall bill5_call1 = new PhoneCall("503-555-2187", "503-867-5309", "12/01/2018 3:34 PM", "12/01/2018 3:37 PM");
        PhoneCall bill5_call2 = new PhoneCall("503-555-2187", "503-555-1212", "12/02/2018 1:12 PM", "12/02/2018 1:17 PM");
        PhoneCall bill5_call3 = new PhoneCall("503-555-2187", "503-730-5753", "12/03/2018 10:46 PM", "12/03/2018 10:55 PM");
        PhoneCall bill5_call4 = new PhoneCall("503-555-2187", "503-867-5309", "12/04/2018 11:03 PM", "12/05/2018 11:28 PM");
        PhoneCall bill5_call5 = new PhoneCall("503-555-2187", "503-555-1212", "12/05/2018 9:07 AM", "12/05/2018 9:08 AM");
        PhoneCall bill5_call6 = new PhoneCall("503-555-2187", "503-730-5753", "12/06/2018 12:33 PM", "12/06/2018 12:37 PM");
        PhoneCall bill5_call7 = new PhoneCall("503-555-2187", "503-555-1212", "12/07/2018 6:25 PM", "12/07/2018 6:26 PM");
        PhoneCall bill5_call8 = new PhoneCall("503-555-2187", "503-867-5309", "12/09/2018 5:29 PM", "12/09/2018 5:38 PM");
        PhoneCall bill5_call9 = new PhoneCall("503-555-2187", "503-730-5753", "12/09/2018 6:05 PM", "12/09/2018 6:17 PM");
        PhoneCall bill5_call10 = new PhoneCall("503-555-2187", "503-867-5309", "12/09/2018 6:19 PM", "12/09/2018 6:37 PM");

        bill5.addPhoneCall(bill5_call1);
        bill5.addPhoneCall(bill5_call2);
        bill5.addPhoneCall(bill5_call3);
        bill5.addPhoneCall(bill5_call4);
        bill5.addPhoneCall(bill5_call5);
        bill5.addPhoneCall(bill5_call6);
        bill5.addPhoneCall(bill5_call7);
        bill5.addPhoneCall(bill5_call8);
        bill5.addPhoneCall(bill5_call9);
        bill5.addPhoneCall(bill5_call10);

        //List billList = new ArrayList<PhoneBill>();
        this.bills.add(bill1);
        this.bills.add(bill2);
        this.bills.add(bill3);
        this.bills.add(bill4);
        this.bills.add(bill5);
    }

    private void loadTestData()
    {
        loadTestPhoneBills();

        phoneBillService.loadTestData(this.bills, new AsyncCallback<Void>()
        {
            @Override
            public void onFailure(Throwable throwable)
            {
                String msg = throwable.toString();
                alerter.alert("phoneBillService.loadTestData FAILED: " + msg);
            }

            @Override
            public void onSuccess(Void v)
            {
                loadBillsListBox();
            }
        });
    }

    private void addCustomer_onclick(String customer)
    {
        phoneBillService.addPhoneBill(customer, new AsyncCallback<Void>()
        {
            @Override
            public void onFailure(Throwable throwable)
            {
                String msg = throwable.toString();
                alerter.alert("phoneBillService.addPhoneBill FAILED: " + msg);
            }

            @Override
            public void onSuccess(Void v)
            {
                loadBillsListBox();

                int size = billsListBox.getItemCount();
                billsListBox.setSelectedIndex(size - 1);
            }
        });
    }

    private void addPhoneCall_onclick(String customer, PhoneCall call)
    {
        phoneBillService.addPhoneCall(customer, call, new AsyncCallback<Void>()
        {
            @Override
            public void onFailure(Throwable throwable)
            {
                String msg = throwable.toString();
                alerter.alert("phoneBillService.addPhoneCall FAILED: " + msg);
            }

            @Override
            public void onSuccess(Void v)
            {
                customerListbox_onchange(customer);
            }
        });
    }

    private void searchButton_onclick(String customer, String caller, String callee, Date start, Date end)
    {
        loadTestPhoneBills();

        phoneBillService.filterPhoneCalls(customer, caller, callee, start, end, new AsyncCallback<List<PhoneCall>>()
        {
            @Override
            public void onFailure(Throwable throwable)
            {
                String msg = throwable.toString();
                alerter.alert("phoneBillService.filterPhoneCalls FAILED: " + msg);
            }

            @Override
            public void onSuccess(List<PhoneCall> list)
            {
                //alerter.alert("selectedBill.customer: (" + selectedBill.customer + ")");

                // Set the total row count. This isn't strictly necessary, but it affects
                // paging calculations, so its good habit to keep the row count up to date.
                callsTable.setRowCount(list.size(), true);

                // Push the data into the widget.
                callsTable.setRowData(0, list);
            }
        });
    }

    private void customerListbox_onchange(String customer)
    {
        phoneBillService.getPhoneBill(customer, new AsyncCallback<PhoneBill>()
        {
            @Override
            public void onFailure(Throwable throwable)
            {
                String msg = throwable.toString();
                alerter.alert("phoneBillService.getPhoneBill FAILED: " + msg);
            }

            @Override
            public void onSuccess(PhoneBill selectedBill)
            {
                //alerter.alert("selectedBill.customer: (" + selectedBill.customer + ")");

                // Set the total row count. This isn't strictly necessary, but it affects
                // paging calculations, so its good habit to keep the row count up to date.
                callsTable.setRowCount(selectedBill.getPhoneCalls().size(), true);

                List<PhoneCall> calls = new ArrayList<>(selectedBill.calls);

                // Push the data into the widget.
                callsTable.setRowData(0, calls);
            }
        });

        //return bill;
    }

    private void setupUI()
    {
        RootPanel rootPanel = RootPanel.get();
        HorizontalPanel horizontalPanel = new HorizontalPanel();

        // Left Panels
        FlowPanel leftFlowPanel = new FlowPanel();
        VerticalPanel leftVerticalPanel = new VerticalPanel();
        leftVerticalPanel.add(leftFlowPanel);

        // Right Panels
        FlexTable rightTable1 = new FlexTable();
        FlexTable rightTable2 = new FlexTable();
        //HorizontalPanel rightHorizontalPanel1 = new HorizontalPanel();
        //HorizontalPanel rightHorizontalPanel2 = new HorizontalPanel();
        VerticalPanel rightVerticalPanel = new VerticalPanel();
        ScrollPanel scrollPanel = new ScrollPanel();
        //scrollPanel.setHeight("300px");
        //scrollPanel.setAlwaysShowScrollBars(true);

        rightVerticalPanel.add(rightTable1);
        rightVerticalPanel.add(rightTable2);
        //rightVerticalPanel.add(rightHorizontalPanel1);
        //rightVerticalPanel.add(rightHorizontalPanel2);
        rightVerticalPanel.add(scrollPanel);

        // Root Panel
        horizontalPanel.add(leftVerticalPanel);
        horizontalPanel.add(rightVerticalPanel);
        rootPanel.add(horizontalPanel);

        //////////////////////////////////////
        TextBox customerTexBox = new TextBox();
        customerTexBox.setWidth("125px");
        Button addCustomerButton = new Button();
        addCustomerButton.setText("Add Bill");
        leftFlowPanel.add(customerTexBox);
        leftFlowPanel.add(new InlineHTML(" "));
        leftFlowPanel.add(addCustomerButton);
        leftFlowPanel.add(new InlineHTML(" "));
        leftFlowPanel.add(new InlineHTML("<br>"));

        addCustomerButton.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent clickEvent)
            {
                String customer = customerTexBox.getText();

                addCustomer_onclick(customer);
            }
        });

        //////////////////////////////////////

        // Add ChangeHandler to dropDownList
        billsListBox.addChangeHandler(new ChangeHandler()
        {
            @Override
            public void onChange(ChangeEvent event)
            {
                String customer = billsListBox.getSelectedItemText();

                //alerter.alert("billsListBox.getSelectedItemText(): (" + customer + ")");
                // Show the initial Bill in the PhoneCalls grid
                customerListbox_onchange(customer);
            }
        });

        loadTestData();

        billsListBox.setVisibleItemCount(15);
        billsListBox.setWidth("200px");
        leftVerticalPanel.getElement().appendChild(DOM.createElement(BRElement.TAG));
        leftVerticalPanel.add(billsListBox);

        billsListBox.setSelectedIndex(0);

        // Right Panel
        TextBox callerTexBox = new TextBox();
        callerTexBox.setWidth("100px");
        TextBox calleeTexBox = new TextBox();
        calleeTexBox.setWidth("100px");

        // Date and Times
        DateTimePicker startTimePicker = new DateTimePicker();
        startTimePicker.setFormat("mm/dd/yyyy HH:ii P");
        startTimePicker.setWidth("150px");

        DateTimePicker endTimePicker = new DateTimePicker();
        endTimePicker.setFormat("mm/dd/yyyy HH:ii P");
        endTimePicker.setWidth("150px");

        ///////////////////////////////////////

        Button addPhoneCallButton = new Button();
        addPhoneCallButton.setText("Add Call");

        addPhoneCallButton.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent clickEvent)
            {
                String customer = billsListBox.getSelectedItemText();
                String caller = callerTexBox.getText();
                String callee = calleeTexBox.getText();
                Date start = startTimePicker.getValue();
                Date end = endTimePicker.getValue();

                PhoneCall call = new PhoneCall(caller, callee, start, end);

                addPhoneCall_onclick(customer, call);

                callerTexBox.setText("");
                calleeTexBox.setText("");
                startTimePicker.setValue(null);
                endTimePicker.setValue(null);

            }
        });

        rightTable1.setText(0, 0, "Caller");
        rightTable1.setWidget(1,0, callerTexBox);
        rightTable1.setText(0, 1, "Callee");
        rightTable1.setWidget(1,1, calleeTexBox);
        rightTable1.setText(0, 2, "Start Time");
        rightTable1.setWidget(1,2, startTimePicker);
        rightTable1.setText(0, 3, "End Time");
        rightTable1.setWidget(1,3, endTimePicker);

        //rightTable1.getFlexCellFormatter().setRowSpan(0, 4, 2);
        rightTable1.setWidget(1,4, addPhoneCallButton);

        ///////////////////////////////////////////////////////////////
        // Search Fields
        TextBox searchCallerTexBox = new TextBox();
        searchCallerTexBox.setWidth("100px");
        TextBox searchCalleeTexBox = new TextBox();
        searchCalleeTexBox.setWidth("100px");

        // Search Date and Times
        DatePicker searchStartDatePicker = new DatePicker();
        searchStartDatePicker.setFormat("m/d/yyyy");
        searchStartDatePicker.setWidth("150px");
        searchStartDatePicker.setAutoClose(true);

        DatePicker searchEndDatePicker = new DatePicker();
        searchEndDatePicker.setFormat("m/d/yyyy");
        searchEndDatePicker.setWidth("150px");
        searchEndDatePicker.setAutoClose(true);

        Button searchButton = new Button();
        searchButton.setText("Search");

        searchButton.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent clickEvent)
            {
                String customer = billsListBox.getSelectedItemText();
                String caller = searchCallerTexBox.getText();
                String callee = searchCalleeTexBox.getText();
                Date start = searchStartDatePicker.getValue();
                Date end = searchEndDatePicker.getValue();

                if ((start == null && end != null) ||
                    (start != null && end == null))
                {
                    throw new PhoneBillException("Search by date requires both start and end dates.");
                }

                searchButton_onclick(customer, caller, callee, start, end);
            }
        });

        rightTable2.setText(0, 0, "Caller");
        rightTable2.setWidget(1,0, searchCallerTexBox);
        rightTable2.setText(0, 1, "Callee");
        rightTable2.setWidget(1,1, searchCalleeTexBox);
        rightTable2.setText(0, 2, "Start Time");
        rightTable2.setWidget(1,2, searchStartDatePicker);
        rightTable2.setText(0, 3, "End Time");
        rightTable2.setWidget(1,3, searchEndDatePicker);

        //rightTable2.getFlexCellFormatter().setRowSpan(0, 4, 2);
        rightTable2.setWidget(1,4, searchButton);

        /////////////////////////////////////////

        callsTable = showGrid();

        // Show the initial Bill in the PhoneCalls grid
        PhoneBill initialBill = (PhoneBill) this.bills.get(0);

        // Set the total row count. This isn't strictly necessary, but it affects
        // paging calculations, so its good habit to keep the row count up to date.
        callsTable.setRowCount(initialBill.getPhoneCalls().size(), true);

        List<PhoneCall> initialCalls = new ArrayList<>(initialBill.calls);

        // Push the data into the widget.
        callsTable.setRowData(0, initialCalls);

        scrollPanel.add(callsTable);
    }

    private void setUpUncaughtExceptionHandler()
    {
        GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler()
        {
            @Override
            public void onUncaughtException(Throwable throwable)
            {
                alertOnException(throwable);
            }
        });
    }

    @VisibleForTesting
    interface Alerter
    {
        void alert(String message);
    }

}
