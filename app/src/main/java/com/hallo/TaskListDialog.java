package com.hallo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

public class TaskListDialog extends Dialog implements DialogInterface.OnClickListener {
        Context mContext;
        OnDialogClickListener mlistener;
        ToDoListStruct toDoListStruct;
        PersistentStore store;
        int position;

public TaskListDialog(@NonNull Context context,int pos, ToDoListStruct todo_struct, PersistentStore _store, OnDialogClickListener listener) {
        super(context);
        this.mContext = context;
        this.mlistener = listener;
        this.toDoListStruct = todo_struct;
        this.store = _store;
        this.position = pos;
        }

public interface OnDialogClickListener {
    void notifyDataChange();
}

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }

    /**
     * called when this class is Instantiated
     * set the UI and initialize the UI components
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tasklistdialog_layout);
        TextView tv_title = findViewById(R.id.tvtodolistviewtitle);
        tv_title.setText(toDoListStruct.getTitle());
        ListView lvTask = findViewById(R.id.lvTasks);
        ToDoListTaskBaseAdapter TaskAdapter = new ToDoListTaskBaseAdapter(mContext, position, toDoListStruct, store, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlistener.notifyDataChange();
            }
        });
        lvTask.setAdapter(TaskAdapter);

        Button cancel = findViewById(R.id.btnCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            /**
             * close the pop up
             * @param view UI
             */
            @Override
            public void onClick(View view) {
                dismiss();

            }
        });



    }
}
