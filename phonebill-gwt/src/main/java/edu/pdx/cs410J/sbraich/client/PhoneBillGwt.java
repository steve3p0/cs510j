package edu.pdx.cs410J.sbraich.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import com.google.gwt.user.client.ui.RootPanel;

import com.google.gwt.user.cellview.client.*;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
//import com.google.gwt.user.cellview.client.
//SingleSelectionModel

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;
import java.util.*;


/**
 * A basic GWT class that makes sure that we can send an Phone Bill back from the server
 */
public class PhoneBillGwt implements EntryPoint
{
    private final Alerter alerter;
    private final PhoneBillServiceAsync phoneBillService;
    private final Logger logger;


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

    private void addWidgets(VerticalPanel panel)
    {
        showPhoneBillButton = new Button("Show Phone Bill");
        showPhoneBillButton.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent clickEvent)
            {
                showPhoneBill();
            }
        });

        showUndeclaredExceptionButton = new Button("Show undeclared exception");
        showUndeclaredExceptionButton.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent clickEvent)
            {
                showUndeclaredException();
            }
        });

        showDeclaredExceptionButton = new Button("Show declared exception");
        showDeclaredExceptionButton.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent clickEvent)
            {
                showDeclaredException();
            }
        });

        showClientSideExceptionButton = new Button("Show client-side exception");
        showClientSideExceptionButton.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent clickEvent)
            {
                throwClientSideException();
            }
        });

        panel.add(showPhoneBillButton);
        panel.add(showUndeclaredExceptionButton);
        panel.add(showDeclaredExceptionButton);
        panel.add(showClientSideExceptionButton);
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

    private void showPhoneBill()
    {
        logger.info("Calling getPhoneBill");
        phoneBillService.getPhoneBill(new AsyncCallback<PhoneBill>()
        {

            @Override
            public void onFailure(Throwable ex)
            {
                alertOnException(ex);
            }

            @Override
            public void onSuccess(PhoneBill phoneBill)
            {
                StringBuilder sb = new StringBuilder(phoneBill.toString());
                Collection<PhoneCall> calls = phoneBill.getPhoneCalls();
                for (PhoneCall call : calls)
                {
                    sb.append(call);
                    sb.append("\n");
                }
                alerter.alert(sb.toString());
            }
        });
    }

    private List testPhoneBills(PhoneBill bill)
    {
        PhoneCall call1 = new PhoneCall("318-467-8383", "503-867-5309", "12/01/2018 3:34 PM", "12/01/2018 3:37 PM" );
        PhoneCall call2 = new PhoneCall("318-467-8383", "503-555-1212", "12/02/2018 1:12 PM", "12/02/2018 1:17 PM" );
        PhoneCall call3 = new PhoneCall("318-467-8383", "503-730-5753", "12/03/2018 10:46 PM", "12/03/2018 10:55 PM" );
        PhoneCall call4 = new PhoneCall("318-467-8383", "503-867-5309", "12/04/2018 11:03 PM", "12/05/2018 11:28 PM" );
        PhoneCall call5 = new PhoneCall("318-467-8383", "503-555-1212", "12/05/2018 9:07 AM", "12/05/2018 9:08 PM" );
        PhoneCall call6 = new PhoneCall("318-467-8383", "503-730-5753", "12/06/2018 12:33 PM", "12/06/2018 12:37 PM" );
        PhoneCall call7 = new PhoneCall("318-467-8383", "503-555-1212", "12/07/2018 6:25 PM", "12/07/2018 6:26 PM" );
        PhoneCall call8 = new PhoneCall("318-467-8383", "503-867-5309", "12/09/2018 5:29 PM", "12/09/2018 5:38 PM" );
        PhoneCall call9 = new PhoneCall("318-467-8383", "503-730-5753", "12/09/2018 6:05 PM", "12/09/2018 6:17 PM" );
        PhoneCall call10 = new PhoneCall("318-467-8383", "503-867-5309", "12/09/2018 6:19 PM", "12/09/2018 6:37 PM" );

        bill.addPhoneCall(call1);
        bill.addPhoneCall(call2);
        bill.addPhoneCall(call3);
        bill.addPhoneCall(call4);
        bill.addPhoneCall(call5);
        bill.addPhoneCall(call6);
        bill.addPhoneCall(call7);
        bill.addPhoneCall(call8);
        bill.addPhoneCall(call9);
        bill.addPhoneCall(call10);

        List list = new ArrayList<>(bill.calls);

        return list;
    }

    private CellTable<PhoneCall> loadGrid()
    {
        PhoneBill bill = new PhoneBill();
        bill.setCustomer("Luke Skywalker");
        List list = testPhoneBills(bill);


        // http://www.gwtproject.org/javadoc/latest/com/google/gwt/user/cellview/client/DataGrid.html
        // Create a CellTable.
        CellTable<PhoneCall> table = new CellTable<PhoneCall>();
        table.setKeyboardSelectionPolicy(HasKeyboardSelectionPolicy.KeyboardSelectionPolicy.ENABLED);

        // Add a text column to show the Caller Number.
        TextColumn<PhoneCall> callerColumn = new TextColumn<PhoneCall>() {
            @Override
            public String getValue(PhoneCall object) {
                return object.callerNumber;
            }
        };
        table.addColumn(callerColumn, "Caller");

        // Add a text column to show the Callee Number.
        TextColumn<PhoneCall> calleeColumn = new TextColumn<PhoneCall>() {
            @Override
            public String getValue(PhoneCall object) {
                return object.calleeNumber;
            }
        };
        table.addColumn(calleeColumn, "Callee");

        // Add a date column to show the birthday.
        //DateCell dateCell = new DateCell();
        //"M/d/yyyy h:mm a"
        String DATE_TIME_FORMAT = "M/d/yyyy h:mm a";
        DateTimeFormat fmt = DateTimeFormat.getFormat(DATE_TIME_FORMAT);
        DateCell dateCell = new DateCell(fmt);
        Column<PhoneCall, Date> startColumn = new Column<PhoneCall, Date>(dateCell) {
            @Override
            public Date getValue(PhoneCall object) {
                return object.startTime;
            }
        };
        table.addColumn(startColumn, "Start Time");

        // Add a date column to show the birthday.
        //DateCell dateCell = new DateCell();
        Column<PhoneCall, Date> endColumn = new Column<PhoneCall, Date>(dateCell) {
            @Override
            public Date getValue(PhoneCall object) {
                return object.endTime;
            }
        };
        table.addColumn(endColumn, "End Time");

        // Add a selection model to handle user selection.
        final SingleSelectionModel<PhoneCall> selectionModel = new SingleSelectionModel<PhoneCall>();
        table.setSelectionModel(selectionModel);
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            public void onSelectionChange(SelectionChangeEvent event) {
                PhoneCall selected = selectionModel.getSelectedObject();
                if (selected != null) {
                    Window.alert("You selected: " + selected.callerNumber);
                }
            }
        });

        // Set the total row count. This isn't strictly necessary, but it affects
        // paging calculations, so its good habit to keep the row count up to date.
        table.setRowCount(bill.getPhoneCalls().size(), true);

        // Push the data into the widget.
        table.setRowData(0, list);

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


    private void setupUI()
    {
        RootPanel rootPanel = RootPanel.get();
        HorizontalPanel hPanel = new HorizontalPanel();
        VerticalPanel lPanel = new VerticalPanel();
        VerticalPanel rPanel = new VerticalPanel();

        //VerticalPanel panel = new VerticalPanel();
        hPanel.add(lPanel);
        hPanel.add(rPanel);
        rootPanel.add(hPanel);

        addWidgets(lPanel);

        CellTable<PhoneCall> table = loadGrid();
        rPanel.add(table);

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
