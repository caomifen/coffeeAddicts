package com.hallo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Locale;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

public class SettingsFragment extends Fragment {

    Spinner spinnerctrl;
    Button btn;
    Locale myLocale;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public static SettingsFragment newInstance(){
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = getContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE);
        editor = pref.edit();
        if (pref.getString("Language", null) != null) {
            pref.getString("Language", null);
        } else {
            editor.putString("Language", "en");
            editor.apply();
        }
        spinnerctrl = (Spinner) view.findViewById(R.id.spinner1);
        spinnerctrl.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                if (pos == 1) {

                    Toast.makeText(parent.getContext(),
                            "You have selected English", Toast.LENGTH_SHORT)
                            .show();
                    editor.putString("Language", "en");
                    editor.apply();
                    setLocale("en");
                } else if (pos == 2) {

                    Toast.makeText(parent.getContext(),
                            "You have selected Chinese", Toast.LENGTH_SHORT)
                            .show();
                    editor.putString("Language", "zh");
                    editor.apply();
                    setLocale("zh");
                } else if (pos == 3) {

                    Toast.makeText(parent.getContext(),
                            "You have selected Malay", Toast.LENGTH_SHORT)
                            .show();
                    editor.putString("Language", "ms");
                    editor.apply();
                    setLocale("ms");
                }

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });

        LinearLayout llyRestore = view.findViewById(R.id.llyrestore);
        llyRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Restore", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        LinearLayout llyBackup = view.findViewById(R.id.llybackup);
        llyBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Backup", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        LinearLayout llyFeedback = view.findViewById(R.id.llyfeedback);
        llyBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Feedback", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        LinearLayout llyLogout = view.findViewById(R.id.llylogout);
        llyLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context mContext = getContext();
                Snackbar.make(view, "Logout", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                PersistentStore store = PersistentStore.getInstance(mContext);
                //unregister notification
                //NotificationScheduler scheduler = new NotificationScheduler(mContext);
                /*JSONArray reminder = store.getReminderList();
                for (int i =0; i< reminder.length(); i++){
                    NotificationScheduler.cancelReminder(mContext, MainActivity.class, i+100);
                }
                JSONArray todo = store.getToDoList();
                for (int i =0; i< todo.length(); i++){
                    NotificationScheduler.cancelReminder(mContext, MainActivity.class, i);
                }*/
                store.reset();
                ((MainActivity)getActivity()).Logout();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_layout, container, false);
    }
    @SuppressWarnings("deprecation")
    public void setLocale(String lang) {

        myLocale = new Locale(lang);
        Context mcontext = getContext();
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(mcontext, MainActivity.class);
        startActivity(refresh);
    }

}
