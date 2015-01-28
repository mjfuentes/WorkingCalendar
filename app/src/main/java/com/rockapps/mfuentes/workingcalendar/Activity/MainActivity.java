package com.rockapps.mfuentes.workingcalendar.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.rockapps.mfuentes.workingcalendar.R;
import com.rockapps.mfuentes.workingcalendar.WizardActivity;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class MainActivity extends Activity implements View.OnClickListener {
    private static final String tag = "SimpleCalendarViewActivity";
    private Button currentMonth;
    private ImageView prevMonth;
    private ImageView nextMonth;
    private GridView calendarView;
    private GridCellAdapter adapter;
    private Calendar _calendar;
    private int month, year;
    private final DateFormat dateFormatter = new DateFormat();
    private static final String dateTemplate = "MMMM yyyy";
    private static String START_DATE = "start_date";
    static final String WORK_DAYS = "work_days";
    static final String REST_DAYS = "rest_days";
    static final String USER_NAME = "username";
    private String startDateString;
    private DateTime startDate;
    private int workDays;
    private int restDays;
    private int current_month;
    private String username;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_calendar_view);
        JodaTimeAndroid.init(this);
        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        startDateString = settings.getString(START_DATE,"");
        username = settings.getString(USER_NAME,"");
        String[] dateParts = startDateString.split("-");
        startDate = new DateTime(Integer.valueOf(dateParts[2]),Integer.valueOf(dateParts[1]),Integer.valueOf(dateParts[0]),0,0,0,0);
        workDays = settings.getInt(WORK_DAYS, 0);
        restDays = settings.getInt(REST_DAYS,0);
        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH) + 1;
        current_month = Integer.valueOf(month);
        year = _calendar.get(Calendar.YEAR);
        Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: " + year);
        Boolean first_time = getIntent().getBooleanExtra("FIRST_TIME",false);
        TextView welcomeTextView= (TextView) findViewById(R.id.welcome_text);
        String welcomeText = (first_time) ? "Bienvenido!" : "Hola " + username + "!";
        welcomeTextView.setText(welcomeText);
        prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
        prevMonth.setOnClickListener(this);

        nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
        nextMonth.setOnClickListener(this);

        calendarView = (GridView) this.findViewById(R.id.calendar);

        // Initialised
        adapter = new GridCellAdapter(getApplicationContext(), R.id.calendar_day_gridcell, month, year);
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
        currentMonth = (Button) this.findViewById(R.id.currentMonth);
        currentMonth.setText(adapter.getMonthAsString(month) + " " + _calendar.get(Calendar.YEAR));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(getApplicationContext(), StartupWizardActivity.class);
                intent.putExtra("RECONFIGURE", true);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void setGridCellAdapterToDate(int month, int year)
    {
        adapter = new GridCellAdapter(getApplicationContext(), R.id.calendar_day_gridcell, month, year);
        _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
        currentMonth.setText(adapter.getMonthAsString(month) + " " + _calendar.get(Calendar.YEAR));
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
    }

    private Boolean isWorkDay(int day, int month, int year){
        DateTime newDate = new DateTime(year,month,day,0,0,0,0);
        int totalDays = workDays + restDays;
        int days = Days.daysBetween(startDate, newDate).getDays() + 1;
        if (days >= 0) {
            int result =  days % totalDays;
            if ((result <= workDays) && (result >0))  {
                return true;
            }
        }
        return false;

    }

    @Override
    public void onClick(View v)
    {
        if (v == prevMonth)
        {
            if (month <= 1)
            {
                month = 12;
                year--;
            }
            else
            {
                month--;
            }
            Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
            setGridCellAdapterToDate(month, year);
        }
        if (v == nextMonth)
        {
            if (month > 11)
            {
                month = 1;
                year++;
            }
            else
            {
                month++;
            }
            Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
            setGridCellAdapterToDate(month, year);
        }

    }

    @Override
    public void onDestroy()
    {
        Log.d(tag, "Destroying View ...");
        super.onDestroy();
    }

    public class GridCellAdapter extends BaseAdapter implements View.OnClickListener
    {
        private static final String tag = "GridCellAdapter";
        private final Context _context;

        private final List<String> list;
        private static final int DAY_OFFSET = 1;
        private final String[] weekdays = new String[]{"Dom", "Lun", "Mar", "Mie", "Jue", "Vie", "Sab"};
        private final String[] months = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private Button gridcell;
        private TextView num_events_per_day;
        private final HashMap eventsPerMonthMap;
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");

        // Days in Current Month
        public GridCellAdapter(Context context, int textViewResourceId, int month, int year)
        {
            super();
            this._context = context;
            this.list = new ArrayList<String>();

            Log.d(tag, "==> Passed in Date FOR Month: " + month + " " + "Year: " + year);
            Calendar calendar = Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
            Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
            Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
            Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

            // Print Month
            printMonth(month, year);

            // Find Number of Events
            eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
        }
        public String getMonthAsString(int i)
        {
            return months[i-1];
        }

        private String getWeekDayAsString(int i)
        {
            return weekdays[i];
        }

        private int getNumberOfDaysOfMonth(int i)
        {
            return daysOfMonth[i - 1];
        }

        public String getItem(int position)
        {
            return list.get(position);
        }

        @Override
        public int getCount()
        {
            return list.size();
        }


        private void printMonth(int mm, int yy)
        {
            Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
            // The number of days to leave blank at
            // the start of this month.
            int trailingSpaces = 0;
            int leadSpaces = 0;
            int daysInPrevMonth = 0;
            int prevMonth = 0;
            int prevYear = 0;
            int nextMonth = 0;
            int nextYear = 0;

            int currentMonth = mm;
            String currentMonthName = getMonthAsString(currentMonth);
            daysInMonth = getNumberOfDaysOfMonth(currentMonth);

            Log.d(tag, "Current Month: " + " " + currentMonthName + " having " + daysInMonth + " days.");

            // Gregorian Calendar : MINUS 1, set to FIRST OF MONTH
            GregorianCalendar cal = new GregorianCalendar(yy, currentMonth - 1, 1);
            Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

            if (currentMonth == 12)
            {
                prevMonth = currentMonth - 1;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 1;
                prevYear = yy;
                nextYear = yy + 1;
                Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
            }
            else if (currentMonth == 1)
            {
                prevMonth = 12;
                prevYear = yy - 1;
                nextYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                nextMonth = 2;
                Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
            }
            else
            {
                prevMonth = currentMonth - 1;
                nextMonth = currentMonth + 1;
                nextYear = yy;
                prevYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
            }

            // Compute how much to leave before before the first day of the
            // month.
            // getDay() returns 0 for Sunday.
            trailingSpaces = cal.get(Calendar.DAY_OF_WEEK) - 2;
            if (trailingSpaces < 0){
                trailingSpaces += 7;
            }
            int currentWeekDay = trailingSpaces;
            Log.d(tag, "Week Day:" + currentWeekDay + " is " + getWeekDayAsString(currentWeekDay));
            Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
            Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

            if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 2)
            {
                ++daysInMonth;
            }

            // Trailing Month days
            for (int i = 0; i < trailingSpaces; i++)
            {
                Log.d(tag, "PREV MONTH:= " + prevMonth + " => " + getMonthAsString(prevMonth) + " " + String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i));
//                if (isWorkDay((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i,prevMonth,prevYear)) {
//                    list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-YELLOW" + "-" + getMonthAsString(prevMonth) + "-" + prevYear);
//                }
//                else {
                    list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-" + getMonthAsString(prevMonth) + "-" + prevYear);
//                }
            }

            // Current Month Days
            for (int i = 1; i <= daysInMonth; i++)
            {
                Log.d(currentMonthName, String.valueOf(i) + " " + getMonthAsString(currentMonth) + " " + yy);
                if (i == getCurrentDayOfMonth() && (current_month == currentMonth))
                {
                    if (isWorkDay(i,currentMonth,yy)) {
                        list.add(String.valueOf(i) + "-MAGENTA" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    }
                    else {
                        list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    }
                }
                else
                {
                    if (isWorkDay(i,currentMonth,yy)) {
                        list.add(String.valueOf(i) + "-RED" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    }
                    else {
                        list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    }
                }
            }

            // Leading Month days
            for (int i = 0; i < list.size() % 7; i++)
            {
                Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
                list.add(String.valueOf(i + 1) + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
            }
        }


        private HashMap findNumberOfEventsPerMonth(int year, int month)
        {
            HashMap map = new HashMap<String, Integer>();
            return map;
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View row = convertView;
            if (row == null)
            {
                LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.calendar_day_gridcell, parent, false);
            }

            // Get a reference to the Day gridcell
            gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
            gridcell.setOnClickListener(this);

            // ACCOUNT FOR SPACING

            Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
            String[] day_color = list.get(position).split("-");
            String theday = day_color[0];
            String themonth = day_color[2];
            String theyear = day_color[3];
            if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null))
            {
                if (eventsPerMonthMap.containsKey(theday))
                {
                    num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
                    Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
                    num_events_per_day.setText(numEvents.toString());
                }
            }

            // Set the Day GridCell
            gridcell.setText(theday);
            gridcell.setTag(theday + "-" + themonth + "-" + theyear);
            Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-" + theyear);

            if (day_color[1].equals("GREY"))
            {
                gridcell.setTextColor(Color.rgb(127,127,127));
            }
            if (day_color[1].equals("RED"))
            {
                gridcell.setTextColor(Color.rgb(244,10,90));
            }
            if (day_color[1].equals("YELLOW"))
            {
                gridcell.setTextColor(Color.YELLOW);
            }
            if (day_color[1].equals("WHITE"))
            {
                gridcell.setTextColor(Color.WHITE);
            }
            if (day_color[1].equals("MAGENTA"))
            {
                gridcell.setTextColor(Color.rgb(255,0,0));
            }
            if (day_color[1].equals("BLUE"))
            {
                gridcell.setTextColor(Color.rgb(25,41,103));
            }
            return row;
        }
        @Override
        public void onClick(View view)
        {
            String date_month_year = (String) view.getTag();
            try
            {
                Date parsedDate = dateFormatter.parse(date_month_year);
                Log.d(tag, "Parsed Date: " + parsedDate.toString());

            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        }

        public int getCurrentDayOfMonth()
        {
            return currentDayOfMonth;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth)
        {
            this.currentDayOfMonth = currentDayOfMonth;
        }
        public void setCurrentWeekDay(int currentWeekDay)
        {
            this.currentWeekDay = currentWeekDay;
        }
        public int getCurrentWeekDay()
        {
            return currentWeekDay;
        }
    }
}
