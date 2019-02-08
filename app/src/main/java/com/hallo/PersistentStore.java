package com.hallo;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class PersistentStore {
    private static Context mContext;
    private static final PersistentStore INSTANCE = new PersistentStore();
    private PersistentStore(){

    }
    public static PersistentStore getInstance(Context context){
        mContext = context;
        return INSTANCE;
    }

    public String getPhoneNumber() {
        File file = new File(mContext.getFilesDir().getParent(),"User.txt");
        //Read text from file
        try {
            //check total bus stop count is the same
            StringBuilder total = new StringBuilder();
            FileInputStream fis = new FileInputStream(file);
            int numRead =0;
            byte[] bytes = new byte[fis.available()];
            while ((numRead = fis.read(bytes)) >= 0) {
                total.append(new String(bytes, 0, numRead));
            }
            return total.toString();
            //return new JSONArray(); //uncomment this line, run and like a b/s will clear off favorite.txt
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("readBusStopfile", "I/O error");
            return "";
        }
    }

    public void writePhoneNumber(String PhoneNumber) {
        File path = new File(mContext.getFilesDir().getParent());
        try{
            Writer output = null;
            //create directory if not exist
            if (!path.exists()) {
                path.mkdir();
            }
            File fw = new File(path, "User.txt");
            output = new BufferedWriter(new FileWriter(fw, false));
            output.write(PhoneNumber);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
