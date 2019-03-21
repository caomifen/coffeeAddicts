package com.hallo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static java.lang.Boolean.getBoolean;

public class ReminderListStruct {
    private String name;
    private NotificationStruct notification;

    private String param_rname = "REMINDER";
    private String param_notification = "NOTIFICATION";

    public ReminderListStruct(String _title, NotificationStruct notification){
        this.name = _title;
        this.notification = notification;
    }

    public ReminderListStruct(JSONObject remObj){
        try {
            this.name = remObj.getString(param_rname);
            this.notification = new NotificationStruct(remObj.getJSONObject(param_notification));
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

    public NotificationStruct getNotification() {
        return notification;
    }
    public void setNotification(NotificationStruct notification){
        this.notification = notification;
    }

    public JSONObject getExportJSONObject(){

        JSONObject export = new JSONObject();
        try {
            export.put(param_rname, name);
            export.put(param_notification, notification.getExportJSONObject());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return export;
    }
}