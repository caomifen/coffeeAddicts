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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EditListDialog extends Dialog implements DialogInterface.OnClickListener {
    Context mContext;
    EditListDialog.OnDialogClickListener mlistener;
    ToDoListStruct todolist;
    int position;
    HashMap<String, Boolean> original_list;
    public EditListDialog(@NonNull Context context,int _position, ToDoListStruct _todolist,  EditListDialog.OnDialogClickListener listener) {
        super(context);
        this.mContext = context;
        this.mlistener = listener;
        this.position = _position;
        this.todolist = _todolist;
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
        setContentView(R.layout.editlist_dialog_layout);

        final EditText et_title = findViewById(R.id.editTextEditListTitle);
        final EditText et_content = findViewById(R.id.editTextEditList);
        et_title.setText(todolist.getTitle());
        JSONArray items = todolist.getList();
        String str_list ="";
        original_list = new HashMap<>();
        for(int i =0; i<items.length(); i++){
            try {
                JSONObject current = items.getJSONObject(i);

                original_list.put(current.getString(ToDoListStruct.item_name), current.getBoolean(ToDoListStruct.item_value));
                str_list = str_list + current.getString(ToDoListStruct.item_name)+ "\n";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        et_content.setText(str_list);


        Button cancel = findViewById(R.id.btnEditListCancel);
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
        Button save = findViewById(R.id.btnEditListSave);
        save.setOnClickListener(new View.OnClickListener() {
            /**
             * Handes the saving of favorite bus stop personalised name.
             * notify the observer subscribed to this that data had changed
             * @param v
             */
            @Override
            public void onClick(View v) {
                String title = et_title.getText().toString();
                ToDoListStruct new_todolist= new ToDoListStruct( title );
                String str_list = et_content.getText().toString();
                String [] name = str_list.split("\n");
                for(int i =0; i< name.length; i++){
                    if (original_list.containsKey(name[i])){
                        new_todolist.addNewItemToList( name[i], original_list.get(name[i]) );
                    } else {
                        new_todolist.addNewItemToList( name[i] );
                    }

                }
                PersistentStore store = PersistentStore.getInstance(mContext);
                JSONArray JSONArray_todolist = store.getToDoList();
                try {
                    JSONArray_todolist.put(position, new_todolist.getExportJSONObject());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                store.saveToDoList(JSONArray_todolist);
                mlistener.notifyToDoDataChange();
                dismiss();
            }
        });



    }
}
