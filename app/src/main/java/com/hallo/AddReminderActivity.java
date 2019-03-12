package com.hallo;

import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.Calendar;

public class AddReminderActivity extends ListActivity {
    ReminderListAdapter ListAdapter;
    PersistentStore store;
    private int mYear,mMonth,mDay,currentH,currentM;
    int dcount;
    int tcount;
    ProgressDialog progressDialog;
    EditText rName;
    Button dateBtn,timeBtn,addBtn,cancelBtn;
    TextView dateTxt,timeTxt;
    Calendar calendar;
    TimePickerDialog timePickerDialog;
    String amPm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dcount = 0;
        tcount = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        rName = (EditText) findViewById(R.id.title);
        dateBtn = (Button) findViewById(R.id.btn_date);
        timeBtn = (Button) findViewById(R.id.btn_time);
        addBtn = (Button) findViewById(R.id.submitBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        dateTxt = (TextView) findViewById(R.id.in_date);
        timeTxt = (TextView) findViewById(R.id.in_time);

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dcount != 0)
                {
                    dcount = 0;
                }
                setDate(dateBtn);
                dcount++;
            }
        });

        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tcount != 0)
                {
                    tcount = 0;
                }
                setTime(timeBtn);
                tcount ++;
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
            public void onClick(View v)
            {
                if((dcount != 1) || (tcount != 1)) {
                    if (dcount != 1) {
                        Toast.makeText(AddReminderActivity.this,
                                "Please enter DATE", Toast.LENGTH_LONG).show();
                    } else if (tcount != 1) {
                        Toast.makeText(AddReminderActivity.this,
                                "Please enter TIME", Toast.LENGTH_LONG).show();
                    }
                }

                else if(rName.getText().toString().isEmpty())
                {
                    Toast.makeText(AddReminderActivity.this,
                            "Please enter Reminder Name", Toast.LENGTH_LONG).show();
                }

                else if((dcount == 1) &&(tcount == 1) && (!rName.getText().toString().isEmpty())){
                    addReminderData();
                    refreshReminderData();
                    finish();
                }
            }
        });
    }

    public void setDate(View v) {

        if (v == dateBtn) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            dateTxt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);

            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        }
    }

    public void setTime(View v)
    {
        if(v == timeBtn)
        {
            calendar = Calendar.getInstance();
            currentH = calendar.get(Calendar.HOUR_OF_DAY);
            currentM = calendar.get(Calendar.MINUTE);

            timePickerDialog = new TimePickerDialog(AddReminderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                    if (hourOfDay >= 12) {
                        amPm = "PM";
                    } else {
                        amPm = "AM";
                    }
                    timeTxt.setText(String.format("%02d:%02d", hourOfDay, minutes) );


                }
            }, currentH, currentM, false);

            timePickerDialog.show();

        }
    }

    public void refreshReminderData(){
        if(this != null) {
            store = PersistentStore.getInstance(this);
            JSONArray remList = store.getReminderList();
            ListAdapter = new ReminderListAdapter(this, remList, store);
            if(remList.length()>0){
                getListView().setVisibility(View.VISIBLE);
            }else{
                getListView().setVisibility(View.INVISIBLE);
            }
            setListAdapter(ListAdapter);
        }
    }

    public void addReminderData()
    {
        String name = rName.getText().toString();
        String date = dateTxt.getText().toString();
        String time = timeTxt.getText().toString();
        String repeat = "Monday";
        ReminderListStruct rList= new ReminderListStruct( name,date,time,"Monday" );
        PersistentStore store = PersistentStore.getInstance(AddReminderActivity.this);
        store.saveReminderList(store.getReminderList().put(rList.getExportJSONObject()));
    }
}
