package com.hallo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class EditListDialog extends Dialog implements DialogInterface.OnClickListener {
    TextView tvNotificationSummary= null;
    CheckedTextView ctv_mon= null, ctv_tue=null, ctv_wed = null, ctv_thu = null, ctv_fri = null, ctv_sat = null, ctv_sun = null;
    Context mContext;
    EditListDialog.OnDialogClickListener mlistener;
    ToDoListStruct todolist;
    int position;
    HashMap<String, Boolean> original_list;
    boolean isRepeat;
    String reminder_date ="";
    int reminder_day=0, reminder_month=0, reminder_year=0;
    TimePicker tpEditNotification;
    boolean is24Hour_format;
    public EditListDialog(@NonNull Context context,int _position, ToDoListStruct _todolist,  EditListDialog.OnDialogClickListener listener) {
        super(context);
        this.mContext = context;
        this.mlistener = listener;
        this.position = _position;
        this.todolist = _todolist;
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
        setContentView(R.layout.editlist_dialog_layout);
        ctv_mon = findViewById(R.id.repeat_mon);
        ctv_tue = findViewById(R.id.repeat_tue);
        ctv_wed = findViewById(R.id.repeat_wed);
        ctv_thu = findViewById(R.id.repeat_thu);
        ctv_fri = findViewById(R.id.repeat_fri);
        ctv_sat = findViewById(R.id.repeat_sat);
        ctv_sun = findViewById(R.id.repeat_sun);

        final EditText et_title = findViewById(R.id.editTextEditListTitle);
        final EditText et_content = findViewById(R.id.editTextEditList);
        final Switch swtNotification = findViewById(R.id.switchEditlistNotification);
        tvNotificationSummary = findViewById(R.id.tvEditListNotificationSummary);
        tpEditNotification = findViewById(R.id.tpEditlist);
        ImageView ivCalender = findViewById(R.id.ivEditSelectDate);

        NotificationStruct notification = todolist.getNotification();
        boolean[] selected = notification.getRepeatDays();
        isRepeat = notification.isRepeat();

        is24Hour_format = DateFormat.is24HourFormat(mContext);
        if(is24Hour_format){
            tpEditNotification.setIs24HourView(true);
        }
        swtNotification.setChecked(notification.isNotificationOn());
        tpEditNotification.setHour(notification.getHour());
        tpEditNotification.setMinute(notification.getMin());
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
                                reminder_month = monthOfYear+ 1;
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
        setOnClick(ctv_mon, selected[0]);
        setOnClick(ctv_tue, selected[1]);
        setOnClick(ctv_wed, selected[2]);
        setOnClick(ctv_thu, selected[3]);
        setOnClick(ctv_fri, selected[4]);
        setOnClick(ctv_sat, selected[5]);
        setOnClick(ctv_sun, selected[6]);

        if(isRepeat){
            changeSummaryStatus();
        } else {
            Date d = notification.getDate();
            tvNotificationSummary.setText((String) DateFormat.format("dd",   d) + "-" + (String) DateFormat.format("MM",   d) + "-" + (String) DateFormat.format("yyyy",   d));
            reminder_day =  Integer.parseInt((String) DateFormat.format("dd",   d));
            reminder_month = Integer.parseInt((String) DateFormat.format("MM",   d))+1;
            reminder_year = Integer.parseInt((String) DateFormat.format("yyyy",   d));
        }

        et_title.setText(todolist.getTitle());
        JSONArray items = todolist.getList();
        String str_list ="";
        original_list = new HashMap<>();
        for(int i =0; i<items.length(); i++){
            try {
                JSONObject current = items.getJSONObject(i);

                original_list.put(current.getString(ToDoListStruct.item_name), current.getBoolean(ToDoListStruct.item_value));
                str_list = str_list + current.getString(ToDoListStruct.item_name)+ "\n";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        et_content.setText(str_list);


        Button cancel = findViewById(R.id.btnEditListCancel);
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
        Button save = findViewById(R.id.btnEditListSave);
        save.setOnClickListener(new View.OnClickListener() {
            /**
             * Handes the saving of favorite bus stop personalised name.
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
                    if(tpEditNotification.getHour()>=12 && tpEditNotification.getHour() <= 23){
                        AMPM = "PM";
                    } else {
                        AMPM = "AM";
                    }
                }
                NotificationStruct notification = new NotificationStruct(swtNotification.isChecked(), isRepeat, reminder_date, tpEditNotification.getHour(), tpEditNotification.getMinute(), repeat, is24Hour_format , AMPM);
                NotificationScheduler scheduler = new NotificationScheduler(mContext);
                if(swtNotification.isChecked()){
                    scheduler.setReminder(mContext, NotificationAlarmReceiver.class, notification.getHour(), notification.getMin(), position, position, "todo");
                } else {
                    scheduler.cancelReminder(mContext, NotificationAlarmReceiver.class, position);

                }
                String title = et_title.getText().toString();
                ToDoListStruct new_todolist= new ToDoListStruct( title );
                new_todolist.setNotification(notification);
                String str_list = et_content.getText().toString();
                String [] name = str_list.split("\n");
                for(int i =0; i< name.length; i++){
                    if (original_list.containsKey(name[i])){
                        new_todolist.addNewItemToList( name[i], original_list.get(name[i]) );
                    } else {
                        new_todolist.addNewItemToList( name[i] );
                    }

                }
                PersistentStore store = PersistentStore.getInstance(mContext);
                JSONArray JSONArray_todolist = store.getToDoList();
                try {
                    JSONArray_todolist.put(position, new_todolist.getExportJSONObject());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                store.saveToDoList(JSONArray_todolist);
                mlistener.notifyToDoDataChange();
                dismiss();
            }
        });



    }
    public void setOnClick(final CheckedTextView ctv, boolean isSet){
        ctv.setChecked(isSet);
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
