package com.hallo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReminderFragment extends ListFragment implements AdapterView.OnItemClickListener {
    Context mContext;
    ReminderListAdapter ListAdapter;
    PersistentStore store;
    public static ReminderFragment newInstance(){
        ReminderFragment fragment = new ReminderFragment();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.reminder_layout,container,false);
        return view;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        ListView list = getListView();
        mContext = getActivity();
        store = PersistentStore.getInstance(mContext);
        JSONArray remList = store.getReminderList();
        ListAdapter = new ReminderListAdapter(mContext, remList, store);
        if (remList.length()==0){
            list.setVisibility(View.INVISIBLE);
        }
        list.setAdapter(ListAdapter);
        FloatingActionButton addBtn = (FloatingActionButton) view.findViewById(R.id.addBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReminder();
            }
        });
    }

    private void addReminder()
    {
        Intent i = new Intent(getActivity().getBaseContext(),
                AddReminderActivity.class);

        getActivity().startActivity(i);
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Snackbar.make(view, "onItemClick", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

}
