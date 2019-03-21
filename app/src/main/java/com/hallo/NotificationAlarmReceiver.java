package com.hallo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;

public class NotificationAlarmReceiver extends BroadcastReceiver {
    String TAG = "AlarmReceiver";
    PersistentStore store;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                store = PersistentStore.getInstance(context);
                //NotificationScheduling.setReminder(context, NotificationAlarmReceiver.class, localData.get_hour(), localData.get_min());
                return;
            }
        }
        store = PersistentStore.getInstance(context);
        Log.d(TAG, "onReceive: ");
        Bundle b = intent.getExtras();
        int index = Integer.parseInt(b.get("index").toString());
        int r_id = Integer.parseInt(b.get("r_id").toString());
        String type = b.get("type").toString();
        //get content
        if(type.equals("todo")){
            try {
                ToDoListStruct td_struct = new ToDoListStruct( store.getToDoList().getJSONObject(index) );

                //Trigger the notification
                NotificationScheduler.showNotification(context, MainActivity.class,
                        "Reminder for: '"+ td_struct.getTitle()+"'", td_struct.getTaskStatus(), r_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                ReminderListStruct rem_struct = new ReminderListStruct( store.getReminderList().getJSONObject(index) );

                //Trigger the notification
                NotificationScheduler.showReminder(context, MainActivity.class,
                        "Reminder for: '"+ rem_struct.getRemName() +"'", r_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }
}
