package com.hallo;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ToDoListTaskBaseAdapter extends BaseAdapter implements View.OnClickListener {
    private JSONArray mlist;
    private Context mContext;
    private ToDoListStruct ToDo_Struct;
    PersistentStore store;
    int tdl_position;
    View.OnClickListener callback;


    @Override
    public void onClick(View view) {

    }


    public ToDoListTaskBaseAdapter(Context context, int pos, ToDoListStruct _todostruct, PersistentStore _store, View.OnClickListener _callback){
        this.mContext = context;
        this.mlist = _todostruct.getList();
        this.ToDo_Struct = _todostruct;
        this.store = _store;
        this.tdl_position = pos;
        this.callback = _callback;
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

    public void removeFromlist(int position, int tdl_position){
        mlist.remove(position);
        updateList(tdl_position);
        notifyDataSetChanged();
    }
    public void updateList(int position){
        ToDo_Struct.setList(mlist);
        JSONArray master_list = store.getToDoList();
        try {
            master_list.put(position, ToDo_Struct.getExportJSONObject());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        store.saveToDoList(master_list);
        this.notifyDataSetChanged();
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
            viewHolder.tdl_position = tdl_position;
            viewHolder.cbTaskList_Name = convertView.findViewById(R.id.cbTaskItem);
            viewHolder.btnTaskList_Remove = convertView.findViewById(R.id.ibTaskRemove);
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
            if(checked){
                result.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAdd));
            }
            viewHolder.cbTaskList_Name.setText(name);
            viewHolder.cbTaskList_Name.setChecked(checked);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        viewHolder.cbTaskList_Name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                try {
                    mlist.getJSONObject(position).put(ToDoListStruct.item_value, b);
                    updateList(viewHolder.tdl_position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(b) {
                    result.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAdd));
                } else {
                    result.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorWhite));
                }
                callback.onClick(result);
            }
        });

        viewHolder.btnTaskList_Remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFromlist(position, viewHolder.tdl_position);
                //Snackbar.make(view, "Deleted '"+ viewHolder.cbTaskList_Name.getText() + "'", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                callback.onClick(result);
            }
        });

        return result;
    }

    /**
     * defines a UI structure of the Listview's row
     */
    static class ViewHolder{
        int position;
        int tdl_position;
        CheckBox cbTaskList_Name;
        ImageView btnTaskList_Remove;


    }
}
