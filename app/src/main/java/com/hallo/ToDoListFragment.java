package com.hallo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ToDoListFragment extends ListFragment implements AdapterView.OnItemClickListener{
    Context mContext;
    ToDoListBaseAdapter ListAdapter;
    PersistentStore store;
    public static ToDoListFragment newInstance(){
        ToDoListFragment fragment = new ToDoListFragment();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.todolist_layout, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        ListView list = getListView();
        mContext = getActivity();
        store = PersistentStore.getInstance(mContext);
        JSONArray todolist = store.getToDoList();
        ListAdapter = new ToDoListBaseAdapter(mContext, todolist, store);
        if (todolist.length()==0){
            list.setVisibility(View.INVISIBLE);
        }
        list.setAdapter(ListAdapter);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "add clicked", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                AddListDialog dialog = new AddListDialog(mContext, new AddListDialog.OnDialogClickListener(){
                    /**
                     * This method must be Implemented
                     * It will notify and call this method when there is changes to favorite bus stops
                     */
                    @Override
                    public void notifyToDoDataChange() {
                        refreshData();
                    }
                });
                dialog.show();
            }
        });
    }

    public void refreshData(){
        if(getActivity()!= null) {
            JSONArray todolist = store.getToDoList();
            ListAdapter = new ToDoListBaseAdapter(getActivity(), todolist, store);
            if(todolist.length()>0){
                getListView().setVisibility(View.VISIBLE);
            }else{
                getListView().setVisibility(View.INVISIBLE);
            }
            setListAdapter(ListAdapter);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Snackbar.make(view, "onItemClick", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        try {

            JSONArray list = store.getToDoList();
            JSONObject selectedObject = list.getJSONObject(position);
            TaskListDialog tdialog = new TaskListDialog(mContext, position, new ToDoListStruct(selectedObject), store, new TaskListDialog.OnDialogClickListener() {
                @Override
                public void notifyDataChange() {
                    refreshData();
                }
            });
            tdialog.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
