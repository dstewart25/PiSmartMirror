import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import sun.java2d.pipe.SpanShapeRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

// blah

public class GoogleCalendar extends JFrame {
    // Holding names of events, start times, and end times
    private static Map eventMap = new LinkedHashMap();
    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = CalendarQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    private static void getCalendarDetails() throws IOException, GeneralSecurityException, ParseException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = service.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();
        if (items.isEmpty()) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            for (Event event : items) {
                String eventName = event.getSummary();
                Date eventTimes[] = new Date[2];
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date start = sdf.parse(event.getStart().getDateTime().toString());
                Date end = sdf.parse(event.getEnd().getDateTime().toString());
                if (start == null) {
                    start = sdf.parse(event.getStart().getDate().toString());
                }
                if (end == null) {
                    end = sdf.parse(event.getEnd().getDate().toString());
                }
                eventTimes[0] = start;
                eventTimes[1] = end;
                eventMap.put(eventName, eventTimes);
                System.out.printf("%s (%s), (%s)\n", event.getSummary(), start.toString(), end.toString());
            }
        }
    }

    private JPanel calendarPanel;
    private JTable calendarTable;
    private DefaultTableModel calendarTableModel;
    private JLabel calendarLabel;

    private void initialize() {
        // Getting calendar events and times
        try {
            getCalendarDetails();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }

        // Setting up panel to hold calendar information
        calendarPanel = new JPanel();
        calendarPanel.setBackground(Color.BLACK);

        // Setting up label for top of calendar
        calendarLabel = new JLabel("Your Calendar");
        calendarLabel.setFont(new Font("Advent Pro", Font.PLAIN, 30));
        calendarLabel.setForeground(Color.LIGHT_GRAY);

        // Setting up table for calendar
        populateTable();
        calendarTable = new JTable(calendarTableModel);
        //calendarTable.getColumnModel().setColumnMargin(20);
        calendarTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        calendarTable.setPreferredSize(new Dimension(500, 300));
        calendarTable.setTableHeader(null);
        calendarTable.setBorder(null);
        calendarTable.setGridColor(Color.BLACK);
        calendarTable.setBackground(Color.BLACK);
        calendarTable.setForeground(Color.LIGHT_GRAY);
        calendarTable.setBorder(BorderFactory.createEmptyBorder(40,0,0,0));
        //calendarTable.setRowHeight(calendarTable.getRowHeight() + 40);
        //calendarTable.setFont(new Font("Serif", Font.BOLD, 36));

        calendarPanel.add(calendarLabel);
        calendarPanel.add(calendarTable);
        add(calendarPanel);
    }

    private void populateTable() {
        String[] column = new String[] {"Event", "Day", "Start Time", "End Time"};
        Object[][] data = new Object[10][4];

        int index = 0;
        Iterator it = eventMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Date[] bothDates = (Date[]) pair.getValue();
            Date startDate = bothDates[0];
            Date endDate = bothDates[1];

            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE MMM dd");
            SimpleDateFormat startDateFormat = new SimpleDateFormat("h:mm a");
            SimpleDateFormat endDateFormat = new SimpleDateFormat("h:mm a");

            data[index][0] = pair.getKey();
            data[index][1] = dayFormat.format(startDate);
            data[index][2] = startDateFormat.format(startDate);
            data[index][3] = endDateFormat.format(endDate);

            index++;
        }
        calendarTableModel = new DefaultTableModel(data, column);
    }

    public GoogleCalendar() {
        initialize();
    }

    public static void main(String args[]) {
        GoogleCalendar googleCalendarPanel = new GoogleCalendar();
        googleCalendarPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        googleCalendarPanel.setSize(600,400);
        googleCalendarPanel.setVisible(true);
    }
}
