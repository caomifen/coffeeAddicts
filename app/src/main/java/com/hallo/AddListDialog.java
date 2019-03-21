package com.hallo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;


import java.util.Calendar;
import android.text.format.DateFormat;

public class AddListDialog extends Dialog implements DialogInterface.OnClickListener {
    CheckedTextView ctv_mon;
    CheckedTextView ctv_tue;
    CheckedTextView ctv_wed;
    CheckedTextView ctv_thu;
    CheckedTextView ctv_fri;
    CheckedTextView ctv_sat;
    CheckedTextView ctv_sun;
    TextView tvNotificationSummary;
    Context mContext;
    OnDialogClickListener mlistener;
    boolean isRepeat = false;
    String reminder_date ="";
    TimePicker tpAddNotification;
    boolean is24Hour_format;
    int reminder_day=0, reminder_month=0, reminder_year=0;

    public AddListDialog(@NonNull Context context, OnDialogClickListener listener) {
        super(context);
        this.mContext = context;
        this.mlistener = listener;
    }

    public interface OnDialogClickListener {
        void notifyToDoDataChange();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }

    /**
     * called when this class is Instantiated
     * set the UI and initialize the UI components
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.addlist_dialog_layout);
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

        final EditText et_title = findViewById(R.id.editTextAddListTitle);
        final EditText et_content = findViewById(R.id.editTextAddList);
        tvNotificationSummary = findViewById(R.id.tvAddListNotificationSummary);
        final Switch swtNotification = findViewById(R.id.switchaddlistNotification);
        ImageView ivCalender = findViewById(R.id.ivSelectDate);
        tpAddNotification = findViewById(R.id.tpAddlist);

        is24Hour_format = DateFormat.is24HourFormat(mContext);
        if(is24Hour_format){
            tpAddNotification.setIs24HourView(true);
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
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



        Button cancel = findViewById(R.id.btnAddListCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            /**
             * close the pop up
             * @param view UI
             */
            @Override
            public void onClick(View view) {
                dismiss();

            }
        });
        Button save = findViewById(R.id.btnAddListSave);
        save.setOnClickListener(new View.OnClickListener() {
            /**
             * Handes the saving of todo list.
             * notify the observer subscribed to this that data had changed
             * @param v
             */
            @Override
            public void onClick(View v) {
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

                String title = et_title.getText().toString();
                ToDoListStruct todolist= new ToDoListStruct( title );
                todolist.setNotification(notification);
                String str_list = et_content.getText().toString();
                String [] name = str_list.split("\n");
                for(int i =0; i< name.length; i++){
                    todolist.addNewItemToList( name[i] );
                }
                PersistentStore store = PersistentStore.getInstance(mContext);
                store.saveToDoList(store.getToDoList().put(todolist.getExportJSONObject()));
                int position = store.getToDoList().length()-1;
                NotificationScheduler scheduler = new NotificationScheduler(mContext);
                if(swtNotification.isChecked()){
                    scheduler.setReminder(mContext, NotificationAlarmReceiver.class, notification.getHour(), notification.getMin(), position, position, "todo");
                } else {
                    scheduler.cancelReminder(mContext, NotificationAlarmReceiver.class, position);

                }
                mlistener.notifyToDoDataChange();
                dismiss();
            }
        });



    }
    /**
     * set the onclick listener of checkedtextview.
     * @param ctv
     */
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
}
