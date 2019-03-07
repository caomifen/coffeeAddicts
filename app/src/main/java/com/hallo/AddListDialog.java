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

public class AddListDialog extends Dialog implements DialogInterface.OnClickListener {
    Context mContext;
    OnDialogClickListener mlistener;
    public AddListDialog(@NonNull Context context, OnDialogClickListener listener) {
        super(context);
        this.mContext = context;
        this.mlistener = listener;
    }

    public interface OnDialogClickListener {
        void notifyToDoDataChange();
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
        setContentView(R.layout.addlist_dialog_layout);

        final EditText et_title = findViewById(R.id.editTextAddListTitle);
        final EditText et_content = findViewById(R.id.editTextAddList);
        Button cancel = findViewById(R.id.btnAddListCancel);
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
        Button save = findViewById(R.id.btnAddListSave);
        save.setOnClickListener(new View.OnClickListener() {
            /**
             * Handes the saving of favorite bus stop personalised name.
             * notify the observer subscribed to this that data had changed
             * @param v
             */
            @Override
            public void onClick(View v) {
                String title = et_title.getText().toString();
                ToDoListStruct todolist= new ToDoListStruct( title );
                String str_list = et_content.getText().toString();
                String [] name = str_list.split("\n");
                for(int i =0; i< name.length; i++){
                    todolist.addNewItemToList( name[i] );
                }
                PersistentStore store = PersistentStore.getInstance(mContext);
                store.saveToDoList(store.getToDoList().put(todolist.getExportJSONObject()));
                mlistener.notifyToDoDataChange();
                dismiss();
            }
        });



    }
}
