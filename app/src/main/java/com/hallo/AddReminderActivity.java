package com.hallo;

import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Calendar;
import java.util.Date;

public class AddReminderActivity extends Activity {
    ReminderListAdapter ListAdapter;
    PersistentStore store;
    private int mYear,mMonth,mDay,currentH,currentM;
    EditText rName;
    Button addBtn,cancelBtn;
    Calendar calendar;

    CheckedTextView ctv_mon;
    CheckedTextView ctv_tue;
    CheckedTextView ctv_wed;
    CheckedTextView ctv_thu;
    CheckedTextView ctv_fri;
    CheckedTextView ctv_sat;
    CheckedTextView ctv_sun;
    TextView tvNotificationSummary;
    boolean isRepeat = false;
    String reminder_date ="";
    TimePicker tpAddNotification;
    boolean is24Hour_format;
    int reminder_day=0, reminder_month=0, reminder_year=0;

    int position;
    String action;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        action = getIntent().getExtras().get("action").toString();
        store = PersistentStore.getInstance(getApplicationContext());
        rName = (EditText) findViewById(R.id.title);
        addBtn = (Button) findViewById(R.id.submitBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        ctv_mon = findViewById(R.id.repeat_mon);
        ctv_tue = findViewById(R.id.repeat_tue);
        ctv_wed = findViewById(R.id.repeat_wed);
        ctv_thu = findViewById(R.id.repeat_thu);
        ctv_fri = findViewById(R.id.repeat_fri);
        ctv_sat = findViewById(R.id.repeat_sat);
        ctv_sun = findViewById(R.id.repeat_sun);
        setOnClick(ctv_mon);
        setOnClick(ctv_tue);
        setOnClick(ctv_wed);
        setOnClick(ctv_thu);
        setOnClick(ctv_fri);
        setOnClick(ctv_sat);
        setOnClick(ctv_sun);

        tvNotificationSummary = findViewById(R.id.tvAddReminderNotificationSummary);
        tvNotificationSummary.setText("");
        final Switch swtNotification = findViewById(R.id.switchaddReminderNotification);
        ImageView ivCalender = findViewById(R.id.ivAddSelectDate);
        tpAddNotification = findViewById(R.id.tpAddReminder);

        is24Hour_format = DateFormat.is24HourFormat(getApplicationContext());
        if(is24Hour_format){
            tpAddNotification.setIs24HourView(true);
        }

        if(action.equals("edit")){
            try {
                position = Integer.parseInt(getIntent().getExtras().get("index").toString());
                ReminderListStruct rem_struct = new ReminderListStruct(store.getReminderList().getJSONObject(position));
                NotificationStruct not_struct = rem_struct.getNotification();

                rName.setText(rem_struct.getRemName());
                swtNotification.setChecked(not_struct.isNotificationOn());
                tpAddNotification.setHour(not_struct.getHour());
                tpAddNotification.setMinute(not_struct.getMin());
                if(not_struct.isRepeat()){
                    boolean[] isChecked = not_struct.getRepeatDays();
                    ctv_mon.setChecked(isChecked[0]);
                    ctv_tue.setChecked(isChecked[1]);
                    ctv_wed.setChecked(isChecked[2]);
                    ctv_thu.setChecked(isChecked[3]);
                    ctv_fri.setChecked(isChecked[4]);
                    ctv_sat.setChecked(isChecked[5]);
                    ctv_sun.setChecked(isChecked[6]);
                    changeSummaryStatus();

                } else {
                    tvNotificationSummary.setText(not_struct.getDateString());
                    Date d = not_struct.getDate();
                    reminder_day =  Integer.parseInt((String) DateFormat.format("dd",   d));
                    reminder_month = Integer.parseInt((String) DateFormat.format("MM",   d))+1;
                    reminder_year = Integer.parseInt((String) DateFormat.format("yyyy",   d));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ivCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRepeat = false;
                // Get Current Date
                int mYear, mMonth, mDay;
                final Calendar c = Calendar.getInstance();
                if(reminder_day==0 && reminder_month==0 && reminder_year ==0){
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                } else {
                    mYear = reminder_year;
                    mMonth = reminder_month;
                    mDay = reminder_day;
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddReminderActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                reminder_day = dayOfMonth;
                                reminder_month = monthOfYear;
                                reminder_year = year;
                                reminder_date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                tvNotificationSummary.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                resetRepeat();

                            }
                        }, mYear, mMonth, mDay);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                boolean[] repeat = {ctv_mon.isChecked(), ctv_tue.isChecked(), ctv_wed.isChecked(), ctv_thu.isChecked(), ctv_fri.isChecked(), ctv_sat.isChecked(), ctv_sun.isChecked() };
                String AMPM;
                if(is24Hour_format){
                    AMPM = "hrs";
                } else {
                    if(tpAddNotification.getHour()>=12 && tpAddNotification.getHour() <= 23){
                        AMPM = "PM";
                    } else {
                        AMPM = "AM";
                    }
                }
                NotificationStruct notification = new NotificationStruct(swtNotification.isChecked(), isRepeat, reminder_date, tpAddNotification.getHour(), tpAddNotification.getMinute(), repeat, is24Hour_format , AMPM);

                Context mContext = getApplicationContext();
                NotificationScheduler scheduler = new NotificationScheduler(mContext);

                String name = rName.getText().toString();
                ReminderListStruct r_struct= new ReminderListStruct(name, notification);
                PersistentStore store = PersistentStore.getInstance(AddReminderActivity.this);
                if(action.equals("edit")){
                    try {
                        store.saveReminderList(store.getReminderList().put(position, r_struct.getExportJSONObject()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    store.saveReminderList(store.getReminderList().put(r_struct.getExportJSONObject()));
                    int position = store.getReminderList().length()-1;
                }

                if(swtNotification.isChecked()){
                    scheduler.setReminder(mContext, NotificationAlarmReceiver.class, notification.getHour(), notification.getMin(), position, position+100, "reminder");
                } else {
                    scheduler.cancelReminder(mContext, NotificationAlarmReceiver.class, position);

                }
                    finish();

            }
        });
    }

    public void setOnClick(final CheckedTextView ctv){
        isRepeat = true;
        ctv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ctv.isChecked()){
                    ctv.setChecked(false);
                    changeSummaryStatus();
                } else {
                    ctv.setChecked(true);
                    changeSummaryStatus();
                }
            }
        });
    }
    public void changeSummaryStatus(){
        String status="";
        String[] r_str = {"Mon", "Tue", "Wed", "thu", "Fri", "Sat", "Sun"};
        boolean[] repeat = {ctv_mon.isChecked(), ctv_tue.isChecked(), ctv_wed.isChecked(), ctv_thu.isChecked(), ctv_fri.isChecked(), ctv_sat.isChecked(), ctv_sun.isChecked() };
        for(int i=0; i< repeat.length; i++){
            if(repeat[i]){
                if(!status.equals("")){
                    status = status + ", ";
                }
                status = status + r_str[i];
            }
        }
        if(status.equals("")){
            isRepeat = false;
        } else {
            isRepeat = true;
        }
        tvNotificationSummary.setText(status);
    }
    public void resetRepeat(){
        isRepeat = false;
        ctv_mon.setChecked(false);
        ctv_tue.setChecked(false);
        ctv_wed.setChecked(false);
        ctv_thu.setChecked(false);
        ctv_fri.setChecked(false);
        ctv_sat.setChecked(false);
        ctv_sun.setChecked(false);

    }
    public void loadEditValues(){

    }
}
