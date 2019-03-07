package com.hallo;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;

public class AddReminderActivity extends Activity {

    private int mYear,mMonth,mDay,currentH,currentM;
    ProgressDialog progressDialog;
    EditText rName;
    Button dateBtn,timeBtn,addBtn;
    TextView dateTxt,timeTxt;
    Calendar calendar;
    TimePickerDialog timePickerDialog;
    String amPm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        rName = (EditText) findViewById(R.id.title);
        dateBtn = (Button) findViewById(R.id.btn_date);
        timeBtn = (Button) findViewById(R.id.btn_time);
        addBtn = (Button) findViewById(R.id.submitBtn);
        dateTxt = (TextView) findViewById(R.id.in_date);
        timeTxt = (TextView) findViewById(R.id.in_time);

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(dateBtn);
            }
        });

        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(timeBtn);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                progressDialog.setMessage("Please Wait, We are Storing Your Reminder on the Server");
                progressDialog.show();

                progressDialog.dismiss();
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
}
