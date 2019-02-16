package com.hallo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.util.Log;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    public static String PARAM_PHONE_NUMBER = "PHONE_NUMBER";
    public static String PARAM_REMEMBER_PHONE_NUMBER = "REMEMBER_PHONE_NUMBER";
    Context mcontext;
    Fragment selectedFragment = null;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mcontext = getApplicationContext();
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
}
