package com.hallo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.util.Log;
import android.view.MenuItem;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static String PARAM_PHONE_NUMBER = "PHONE_NUMBER";
    public static String PARAM_REMEMBER_PHONE_NUMBER = "REMEMBER_PHONE_NUMBER";
    Context mcontext;
    Fragment selectedFragment = null;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Locale myLocale;
    String language;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcontext = getApplicationContext();
        pref = getApplicationContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE);
        editor = pref.edit();
        if (pref.getString("Language","") != null) {
            language = pref.getString("Language", null);
        } else {
            editor.putString("Language", "en");
            editor.apply();
            language = "en";
        }
        myLocale = new Locale(language);
        Resources res = getResources();
        // Change locale settings in the apps
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(myLocale);
        res.updateConfiguration(conf, dm);

        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        PersistentStore store = PersistentStore.getInstance(mcontext);

        if(store.getPhoneNumber() == ""){
            Intent intent = new Intent(mcontext, SignupActivity.class);
            startActivity(intent);
        } else {
            //load home page
            if(savedInstanceState == null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, HomeFragment.newInstance());
                transaction.commit();
            }
        }
    }

    protected void displayFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = HomeFragment.newInstance();
                    displayFragment(selectedFragment);
                    return true;
                case R.id.navigation_reminder:
                    selectedFragment = ReminderFragment.newInstance();
                    displayFragment(selectedFragment);
                    return true;
                case R.id.navigation_todo:
                    selectedFragment = ToDoListFragment.newInstance();
                    displayFragment(selectedFragment);
                    return true;
                case R.id.navigation_settings:
                    selectedFragment = SettingsFragment.newInstance();
                    displayFragment(selectedFragment);
                    return true;
            }
            return false;
        }
    };

}
