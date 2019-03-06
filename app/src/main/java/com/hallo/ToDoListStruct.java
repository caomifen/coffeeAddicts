package com.hallo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static java.lang.Boolean.getBoolean;

public class ToDoListStruct {
    private String title;
    private JSONArray list;
    private JSONObject item;
    public static String item_name = "ITEM_NAME";
    public static String item_value = "ITEM_VALUE";
    private String param_title = "TITLE";
    private String param_list = "LIST";

    public ToDoListStruct(String _title){
        this.title = _title;
        this.list = new JSONArray();
    }
    public ToDoListStruct(JSONObject todoObject){
        item = todoObject;
        try {
            this.title = item.getString(param_title);
            this.list = item.getJSONArray(param_list);
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
        }

        return Integer.toString(taskDone) + "/" + Integer.toString(list.length()) + " completed";
    }
    public String getTitle(){
        return this.title;
    }
    public void setTitle(String _title){
        this.title = _title;
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return export;
    }
}