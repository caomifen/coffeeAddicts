package com.hallo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationStruct {
    private boolean isNotificationOn = false;
    private boolean isRepeat= false;
    private Date date = null;
    private int hour = 0; //0 to 24, depends on time format
    private int min = 0; //0 to 60
    private boolean[] repeatDays = {false, false, false, false, false, false, false}; // index 1 is monday
    private boolean is24HourFormat = false; //12 or 24
    private String ampm = "AM"; //AM or PM

    public static String PARAM_ISNOTIFICATION = "IS_NOTIFICATION";
    public static String PARAM_ISREPEAT = "IS_REPEAT";
    public static String PARAM_DATE = "DATE";
    public static String PARAM_HOUR = "HOUR";
    public static String PARAM_MINUTE = "MIN";
    public static String PARAM_REPEAT_DAYS = "REPEAT_DAYS";
    public static String PARAM_TIME_FORMAT = "TIME_FORMAT";
    public static String PARAM_AM_PM = "AM_PM";


    public NotificationStruct(){
        //default const
    }
    public NotificationStruct(boolean _isNotificationOn, boolean _isRepeat, String _date, int _hour, int _min, boolean[] _repeatDays, boolean _timeFormat, String _ampm){
        isNotificationOn = _isNotificationOn;
        isRepeat = _isRepeat;
        date = getDateFromString(_date);
        hour = _hour;
        min = _min;
        repeatDays = _repeatDays;
        is24HourFormat = _timeFormat;
        ampm = _ampm;
    }

    public NotificationStruct(JSONObject Notification){
        try {
            isNotificationOn = Notification.getBoolean(PARAM_ISNOTIFICATION);
            isRepeat = Notification.getBoolean(PARAM_ISREPEAT);
            String str_date = Notification.getString(PARAM_DATE);
            if(str_date.equals("") || str_date.equals(null)){
                date = null;
            } else {
                date = getDateFromString(Notification.getString(PARAM_DATE));
            }
            hour = Notification.getInt(PARAM_HOUR);
            min = Notification.getInt(PARAM_MINUTE);
            repeatDays = getRepeatDaysFromJSONObject(Notification.getJSONObject(PARAM_REPEAT_DAYS));
            is24HourFormat = Notification.getBoolean(PARAM_TIME_FORMAT);
            ampm = Notification.getString(PARAM_AM_PM);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getExportJSONObject(){
        JSONObject export = new JSONObject();
        try {
            export.put(PARAM_ISNOTIFICATION, isNotificationOn);
            export.put(PARAM_ISREPEAT, isRepeat);
            if(date.equals(null)){
                export.put(PARAM_DATE, "");
            } else{
                export.put(PARAM_DATE, getDateString());
            }
            export.put(PARAM_HOUR, hour);
            export.put(PARAM_MINUTE, min);
            export.put(PARAM_REPEAT_DAYS, getRepeatDaysJSONObject());
            export.put(PARAM_TIME_FORMAT, is24HourFormat);
            export.put(PARAM_AM_PM, ampm);

            return export;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Date getDateFromString(String Date){
        Date date_formatted = new Date();
        try {
            date_formatted=new SimpleDateFormat("dd-MM-yyyy").parse(Date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date_formatted;
    }

    public String getDateString(){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat.format(date);

        return strDate;
    }

    public boolean[] getRepeatDaysFromJSONObject(JSONObject repeat_days){
        try {
            boolean[] rd = {repeat_days.getBoolean("MON"),
                    repeat_days.getBoolean("TUE"),
                    repeat_days.getBoolean("WED"),
                    repeat_days.getBoolean("THU"),
                    repeat_days.getBoolean("FRI"),
                    repeat_days.getBoolean("SAT"),
                    repeat_days.getBoolean("SUN")};
            return rd;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getRepeatDaysJSONObject(){
        try {
            JSONObject rd = new JSONObject();
            rd.put("MON", repeatDays[0]);
            rd.put("TUE", repeatDays[1]);
            rd.put("WED", repeatDays[2]);
            rd.put("THU", repeatDays[3]);
            rd.put("FRI", repeatDays[4]);
            rd.put("SAT", repeatDays[5]);
            rd.put("SUN", repeatDays[6]);

            return rd;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getFormattedTimeString(){
        int hr = hour;
        if(is24HourFormat){
            return String.format("%02d", hr) +":" + String.format("%02d", min) + ampm;
        } else {
            if(hr > 12){
                hr -= 12;
            }
            if(hr==0){
                hr = 12;
            }
            return String.format("%02d", hr) +":" + String.format("%02d", min) + ampm;
        }
    }

    public void setNotificationOn(boolean notificationOn) {
        isNotificationOn = notificationOn;
    }

    public void setRepeat(boolean repeat) {
        isRepeat = repeat;
    }

    public boolean isNotificationOn() {
        return isNotificationOn;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public Date getDate() {
        return date;
    }

    public int getHour() {
        return hour;
    }

    public int getMin() {
        return min;
    }

    public boolean[] getRepeatDays() {
        return repeatDays;
    }

    public boolean isIs24HourFormat() {
        return is24HourFormat;
    }

    public String getAmpm() {
        return ampm;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setRepeatDays(boolean[] repeatDays) {
        this.repeatDays = repeatDays;
    }

    public void setIs24HourFormat(boolean is24HourFormat) {
        this.is24HourFormat = is24HourFormat;
    }

    public void setAmpm(String ampm) {
        this.ampm = ampm;
    }
}
