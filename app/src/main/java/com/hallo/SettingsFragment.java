package com.hallo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class SettingsFragment extends Fragment {

    Spinner spinnerctrl;
    Button btn;
    Locale myLocale;

    public static SettingsFragment newInstance(){
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinnerctrl = (Spinner) view.findViewById(R.id.spinner1);
        spinnerctrl.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                if (pos == 1) {

                    Toast.makeText(parent.getContext(),
                            "You have selected English", Toast.LENGTH_SHORT)
                            .show();
                    setLocale("ta");
                } else if (pos == 2) {

                    Toast.makeText(parent.getContext(),
                            "You have selected Chinese", Toast.LENGTH_SHORT)
                            .show();
                    setLocale("hi");
                } else if (pos == 3) {

                    Toast.makeText(parent.getContext(),
                            "You have selected Malay", Toast.LENGTH_SHORT)
                            .show();
                    setLocale("en");
                }

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_layout, container, false);
    }

    public void setLocale(String lang) {

        myLocale = new Locale(lang);
        Context mcontext = getContext();
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(mcontext, SettingsFragment.class);
        startActivity(refresh);
    }

}
