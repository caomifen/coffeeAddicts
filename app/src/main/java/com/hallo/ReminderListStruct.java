package com.hallo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static java.lang.Boolean.getBoolean;

public class ReminderListStruct {
    private String name,date,time,repeat;
    private JSONObject item;
    private String param_rname = "Reminder 1";
    private String param_date = "01-01-2019";
    private String param_time = "00:00:00";
    private String param_repeat = "Monday";

    public ReminderListStruct(String _title, String _date, String _time, String _repeat){
        this.name = _title;
        this.date = _date;
        this.time = _time;
        this.repeat = _repeat;
    }

    public ReminderListStruct(JSONObject remObj){
        item = remObj;
        try {
            this.name = item.getString(param_rname);
            this.date = item.getString(param_date);
            this.time = item.getString(param_time);
            this.repeat = item.getString(param_repeat);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getRemName(){
        return this.name;
    }
    public void setRemName(String _name){
        this.name = _name;
    }

    public String getRemDate(){
        return this.date;
    }
    public void setRemDate(String _date){
        this.date = _date;
    }

    public String getRemTime(){
        return this.time;
    }
    public void setRemTime(String _time){
        this.time = _time;
    }

    public String getRemRepeat(){
        return this.repeat;
    }
    public void setRemRepeat(String _repeat){
        this.repeat = _repeat;
    }

    public JSONObject getExportJSONObject(){

        JSONObject export = new JSONObject();
        try {
            export.put(param_rname, name);
            export.put(param_date, date);
            export.put(param_time, time);
            export.put(param_repeat, repeat);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return export;
    }
}