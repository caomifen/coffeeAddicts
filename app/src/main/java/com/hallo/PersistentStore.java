package com.hallo;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
/**
 * This class defines methods that are responsible for handling the local storage data of the app
 * This is a singleton class.
 */
public class PersistentStore {
    private static Context mContext;
    private static final PersistentStore INSTANCE = new PersistentStore();
    private static final String UserStore = "User.txt";
    private static final String ToDoListStore = "ToDoList.txt";
    private static final String ReminderListStore = "ReminderList.txt";

    private static String param_phone_number = "Phone_number";
    private static String param_remember_me = "Remember_me";

    private static JSONArray ToDoList;
    private static JSONArray ReminderList;
    private static JSONObject User; //User has 2 fields with keys: param_phone_number and param_remember_me

    /**
     * Default class constructor
     */
    private PersistentStore(){

    }

    public static PersistentStore getInstance(Context context){
        mContext = context;
        try {
            User = LoadObjectStoreFile(UserStore);
            if(User.length()==0){
                User.put(param_phone_number, "");
                User.put(param_remember_me, "0");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ToDoList = LoadArrayStoreFile(ToDoListStore);
        ReminderList = LoadArrayStoreFile(ReminderListStore);
        return INSTANCE;
    }

    //User related function
    public void saveUser(String PhoneNumber, String remember) {
        try {
            User.put(param_phone_number, PhoneNumber);
            User.put(param_remember_me, remember);
            SaveStoreFile(User, UserStore);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
    public String getPhoneNumber(){
        try {
            return User.getString(param_phone_number);
        } catch (JSONException e){
            e.printStackTrace();
            return "";
        }
    }
    public boolean isRememberMe(){
        try {
            String rem = User.getString(param_remember_me);
            if(rem.equals("1")){
                return true;
            } else {
                return false;
            }
        } catch (JSONException e){
            e.printStackTrace();
            return false;
        }
    }

    //ToDoList related functions
    public JSONArray getToDoList(){
        return ToDoList;
    }
    public void saveToDoList(JSONArray newList){
        SaveStoreFile(newList, ToDoListStore);
        ToDoList = newList;
    }

    //ReminderList functions
    public JSONArray getReminderList(){
        return ReminderList;
    }
    public void saveReminderList(JSONArray newList){
        SaveStoreFile(newList, ReminderListStore);
        ReminderList = newList;
    }

    //others/general functions
    public void reset(){
        saveUser("", "0");
        saveToDoList(new JSONArray());
        saveReminderList(new JSONArray());
    }

    //General read/write logic
    private static JSONArray LoadArrayStoreFile(String filename){
        File file = new File(mContext.getFilesDir().getParent(), filename);
        //Read text from file
        try {
            StringBuilder total = new StringBuilder();
            FileInputStream fis = new FileInputStream(file);
            int numRead =0;
            byte[] bytes = new byte[fis.available()];
            while ((numRead = fis.read(bytes)) >= 0) {
                total.append(new String(bytes, 0, numRead));
            }
            return new JSONArray(total.toString());

        } catch (IOException e) {
            e.printStackTrace();
            Log.i("LoadArrayStoreFile: "+ filename, "I/O error");
            createEmptyStoreFile(new JSONArray(), filename);
            return new JSONArray();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("LoadArrayStoreFile: "+ filename, "JSON Exception");
            return new JSONArray();
        }
    }
    private static JSONObject LoadObjectStoreFile(String filename){
        File file = new File(mContext.getFilesDir().getParent(), filename);
        //Read text from file
        try {
            StringBuilder total = new StringBuilder();
            FileInputStream fis = new FileInputStream(file);
            int numRead =0;
            byte[] bytes = new byte[fis.available()];
            while ((numRead = fis.read(bytes)) >= 0) {
                total.append(new String(bytes, 0, numRead));
            }
            return new JSONObject(total.toString());

        } catch (IOException e) {
            e.printStackTrace();
            Log.i("LoadObjectStoreFile: "+ filename, "I/O error");
            createEmptyStoreFile(new JSONObject(), filename);
            return new JSONObject();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("LoadObjectStoreFile: "+ filename, "JSON Exception");
            return new JSONObject();
        }
    }

    private void SaveStoreFile(JSONArray object, String filename) {
        File path = new File(mContext.getFilesDir().getParent());
        try{
            Writer output = null;
            //create directory if not exist
            if (!path.exists()) {
                path.mkdir();
            }
            File fw = new File(path, filename);
            output = new BufferedWriter(new FileWriter(fw, false));
            output.write(object.toString());
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void SaveStoreFile(JSONObject array, String filename) {
        File path = new File(mContext.getFilesDir().getParent());
        try{
            Writer output = null;
            //create directory if not exist
            if (!path.exists()) {
                path.mkdir();
            }
            File fw = new File(path, filename);
            output = new BufferedWriter(new FileWriter(fw, false));
            output.write(array.toString());
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create an empty store file with an empty JSONArray
     * notify the observer subscribed to this that data had changed
     * @param filename
     */
    public static void createEmptyStoreFile(JSONArray empty_file, String filename){
        File path = new File(mContext.getFilesDir().getParent());
        //create directory if not exist
        if (!path.exists()) {
            path.mkdir();
        }
        try{
            File fw = new File(path, filename);
            Writer output = new BufferedWriter(new FileWriter(fw, false));
            output.write(empty_file.toString());
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create an empty store file with an empty JSONArray
     * @param filename
     */
    public static void createEmptyStoreFile(JSONObject empty_file, String filename){
        File path = new File(mContext.getFilesDir().getParent());
        //create directory if not exist
        if (!path.exists()) {
            path.mkdir();
        }
        try{
            File fw = new File(path, filename);
            Writer output = new BufferedWriter(new FileWriter(fw, false));
            output.write(empty_file.toString());
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
