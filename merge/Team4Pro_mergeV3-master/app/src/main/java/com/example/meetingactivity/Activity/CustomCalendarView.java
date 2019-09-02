package com.example.meetingactivity.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.meetingactivity.R;
import com.example.meetingactivity.model.Events;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomCalendarView extends LinearLayout {
    ImageButton nextButton, prevButton;
    TextView CurrentDate;
    GridView gridView;
    private static final int MAX_CALENDAR_DAYS = 42;
    Calendar calendar = Calendar.getInstance(Locale.KOREAN);
    Context context;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM", Locale.KOREAN);
    SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.KOREAN);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.KOREAN);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.KOREAN);
    SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);

    MyGridAdapter myGridAdapter;
//    AlertDialog alertDialog;
    List<Date> dates = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();
    DBOpenHelper dbOpenHelper;
    private Bundle arguments;

    public CustomCalendarView(Context context) {
        super(context);
    }

    public CustomCalendarView(final Context context, AttributeSet attrs) {

        super(context, attrs);
        this.context = context;
        IntializeLayout();
        SetUpCalendar();

        prevButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                SetUpCalendar();
            }
        });

        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                SetUpCalendar();
            }
        });

//        달력 이벤트 처리
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final View detailDialog = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_dialog, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setView(detailDialog);
//                달력은 6 x 7로써 배열에 들어가 있다 그러므로 position을 통해서 0 ~ 41까지 하는데 이를 요일을 알아내는 방법
                Log.d("[test1-1]", "일자 클릭");
                int week = (position % 7) + 1;
//        요일을 뿌려주기 위해서 사용
                String dayOfWeek = "";
                switch (week) {
                    case 1:
                        dayOfWeek = "일요일";
                        break;
                    case 2:
                        dayOfWeek = "월요일";
                        break;
                    case 3:
                        dayOfWeek = "화요일";
                        break;
                    case 4:
                        dayOfWeek = "수요일";
                        break;
                    case 5:
                        dayOfWeek = "목요일";
                        break;
                    case 6:
                        dayOfWeek = "금요일";
                        break;
                    case 7:
                        dayOfWeek = "토요일";
                        break;
                }
                final String date = eventDateFormat.format(dates.get(position));
                final String month = monthFormat.format(dates.get(position));
                final String year = yearFormat.format(dates.get(position));
                final String dayOfMonth = dayFormat.format(dates.get(position));

                // 날짜 객체 초기화
                FloatingActionButton Detail_add = detailDialog.findViewById(R.id.Detail_add);
                TextView Detail_day = detailDialog.findViewById(R.id.Detail_day);
                TextView Detail_year = detailDialog.findViewById(R.id.Detail_year);
                TextView Detail_month = detailDialog.findViewById(R.id.Detail_month);
                TextView Detail_week = detailDialog.findViewById(R.id.Detail_week);

                // 날짜 객체 설정
                Detail_day.setText(dayOfMonth + "일");
                Detail_year.setText(year + "년");
                Detail_month.setText(month + "월");
                Detail_week.setText(dayOfWeek);

                Detail_add.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 화면 전환
                        Intent intent = new Intent(context, Calendar_WriteActivity.class);
                        context.startActivity(intent);
                    }
                });
                builder.show();
            }
        });
    }

    private ArrayList<Events> CollectEvenetByDate(String date) {
        ArrayList<Events> arrayList = new ArrayList<>();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEvents(date, database);
        while (cursor.moveToNext()) {
            String event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT));
            String time = cursor.getString(cursor.getColumnIndex(DBStructure.TIME));
            String Date = cursor.getString(cursor.getColumnIndex(DBStructure.DATE));
            String month = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH));
            String year = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR));
            Events events = new Events(event, time, Date, month, year);
            arrayList.add(events);
        }
        cursor.close();
        dbOpenHelper.close();
        return arrayList;
    }

    public CustomCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void SaveEnvet(String event, String time, String date, String month, String year) {
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event, time, date, month, year, database);
        dbOpenHelper.close();
    }

    private void IntializeLayout() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);
        nextButton = view.findViewById(R.id.nextBtn);
        prevButton = view.findViewById(R.id.previousBtn);
        CurrentDate = view.findViewById(R.id.current_Date);
        gridView = view.findViewById(R.id.gridview);
    }

    private void SetUpCalendar() {
        String currwntDate = dateFormat.format(calendar.getTime());
        CurrentDate.setText(currwntDate);
        dates.clear();
        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int FirstDayofMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayofMonth);
        CollectEvenetsPerMonth(monthFormat.format(calendar.getTime()), yearFormat.format(calendar.getTime()));

        while (dates.size() < MAX_CALENDAR_DAYS) {
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        myGridAdapter = new MyGridAdapter(context, dates, calendar, eventsList);
        gridView.setAdapter(myGridAdapter);
    }

    private void CollectEvenetsPerMonth(String Month, String Year) {
        eventsList.clear();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEventssperMonth(Month, Year, database);
        while (cursor.moveToNext()) {
            String event = cursor.getString(cursor.getColumnIndex(DBStructure.EVENT));
            String time = cursor.getString(cursor.getColumnIndex(DBStructure.TIME));
            String date = cursor.getString(cursor.getColumnIndex(DBStructure.DATE));
            String month = cursor.getString(cursor.getColumnIndex(DBStructure.MONTH));
            String year = cursor.getString(cursor.getColumnIndex(DBStructure.YEAR));
            Events events = new Events(event, time, date, month, year);
            eventsList.add(events);
        }
        cursor.close();
        dbOpenHelper.close();
    }
}
