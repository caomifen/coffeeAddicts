package com.hallo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static java.lang.Boolean.getBoolean;

public class ToDoListStruct {
    private String title;
    private JSONArray list = new JSONArray();
    private JSONObject item;
    private NotificationStruct notification;

    public static String item_name = "ITEM_NAME";
    public static String item_value = "ITEM_VALUE";
    private String param_title = "TITLE";
    private String param_list = "LIST";
    private String param_notification = "NOTIFICATION";

    public ToDoListStruct(String _title){
        this.title = _title;
        this.list = new JSONArray();
        this.notification = new NotificationStruct();
    }
    public ToDoListStruct(JSONObject todoObject){
        item = todoObject;
        try {
            this.title = item.getString(param_title);
            this.list = item.getJSONArray(param_list);
            this.notification = new NotificationStruct(item.getJSONObject(param_notification));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getTaskStatus(){
        int taskDone = 0;
        try {
            for (int i = 0; i< list.length(); i++){

                JSONObject currentObject = list.getJSONObject(i);
                if( currentObject.getBoolean(item_value) ){
                    taskDone++;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "No Task";
        }

        return Integer.toString(taskDone) + "/" + Integer.toString(list.length()) + " completed";
    }
    public String getTitle(){
        return this.title;
    }
    public void setTitle(String _title){
        this.title = _title;
    }
    public NotificationStruct getNotification(){
        return notification;
    }
    public void setNotification(NotificationStruct _notification){
        notification = _notification;
    }
    public JSONArray getList(){
        return list;
    }

    public void setList(JSONArray newlist){
        this.list = newlist;
    }
    public void addNewItemToList(String name){
        try {
            item = new JSONObject();
            item.put(item_name,  name);
            item.put(item_value, false);
            list.put(item);
        } catch (JSONException e){

        }
    }
    public void addNewItemToList(String name, Boolean value){
        try {
            item = new JSONObject();
            item.put(item_name,  name);
            item.put(item_value, value);
            list.put(item);
        } catch (JSONException e){

        }
    }
    public void updateItemValueByIndex(int index, boolean newValue){
        try {
            item = new JSONObject();
            item = list.getJSONObject(index);
            item.put(item_value, newValue);
        } catch (JSONException e){

        }
    }
    public void updateItemNameByIndex(int index, String newName){
        try {
            item = new JSONObject();
            item = list.getJSONObject(index);
            item.put(item_name, newName);
        } catch (JSONException e){

        }
    }
    public int getTaskCount(){
        return list.length();
    }

    public JSONObject getExportJSONObject(){

        JSONObject export = new JSONObject();
        try {
            export.put(param_title, title);
            export.put(param_list, list);
            export.put(param_notification, notification.getExportJSONObject());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return export;
    }
}