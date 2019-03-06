package com.hallo;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ToDoListTaskBaseAdapter extends BaseAdapter {
    private JSONArray mlist;
    private Context mContext;
    private ToDoListStruct ToDo_Struct;
    PersistentStore store;

    public ToDoListTaskBaseAdapter(Context context, ToDoListStruct _todostruct, PersistentStore _store){
        this.mContext = context;
        this.mlist = _todostruct.getList();
        this.ToDo_Struct = _todostruct;
        store = _store;
    }

    @Override
    public int getCount() {
        return mlist.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return mlist.getJSONObject(position);
        } catch (JSONException e){
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void removeFromlist(int position){
        //mlist.remove(position);
        //ToDo_Struct.setList(mlist);

        //store.saveToDoList(store.getToDoList().);
        //this.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ToDoListTaskBaseAdapter.ViewHolder viewHolder;
        final View result;
        if (convertView == null){
            viewHolder = new ToDoListTaskBaseAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.todolisttask_item, null);
            viewHolder.position = position;
            viewHolder.cbTaskList_Name = convertView.findViewById(R.id.cbTaskItem);
            viewHolder.btnTaskList_Edit = convertView.findViewById(R.id.ibTaskRemove);
            viewHolder.btnTaskList_Remove = convertView.findViewById(R.id.ibTaskEdit);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ToDoListTaskBaseAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }
        try{
            JSONObject TaskObject = mlist.getJSONObject(viewHolder.position);
            boolean checked = TaskObject.getBoolean(ToDoListStruct.item_value);
            String name = TaskObject.getString(ToDoListStruct.item_name);

            viewHolder.cbTaskList_Name.setText(name);
            viewHolder.cbTaskList_Name.setChecked(checked);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        viewHolder.btnTaskList_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        viewHolder.btnTaskList_Remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFromlist(position);
                Snackbar.make(view, "Deleted '"+ viewHolder.cbTaskList_Name.getText() + "'", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            }
        });

        return result;
    }

    /**
     * defines a UI structure of the Listview's row
     */
    static class ViewHolder{
        int position;
        CheckBox cbTaskList_Name;
        ImageButton btnTaskList_Edit;
        ImageButton btnTaskList_Remove;


    }
}
