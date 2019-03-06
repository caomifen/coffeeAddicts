package com.hallo;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ToDoListBaseAdapter extends BaseAdapter {
    private JSONArray mlist;
    private Context mContext;
    PersistentStore store;

    public ToDoListBaseAdapter(Context context, JSONArray list, PersistentStore _store){
        this.mContext = context;
        this.mlist = list;
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
        mlist.remove(position);
        store.saveToDoList(mlist);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;
        final View result;
        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.todolist_item, null);
            viewHolder.position = position;
            viewHolder.tvToDoListItem_Title = convertView.findViewById(R.id.tvToDoListItemTitle);
            viewHolder.tvToDoListItem_Tasks = convertView.findViewById(R.id.tvToDoListItemTasks);
            viewHolder.ivToDoListItem_delete = convertView.findViewById(R.id.ivToDoListItem_Delete);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        try{
            JSONObject TaskObject = mlist.getJSONObject(viewHolder.position);
            ToDoListStruct todo = new ToDoListStruct(TaskObject);

            viewHolder.tvToDoListItem_Title.setText(todo.getTitle());
            viewHolder.tvToDoListItem_Tasks.setText(todo.getTaskStatus());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        viewHolder.ivToDoListItem_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFromlist(position);
                Snackbar.make(view, "Deleted "+ viewHolder.tvToDoListItem_Title.getText() + " list", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                if(store.getToDoList().length()==0) {
                    parent.setVisibility(View.INVISIBLE);
                }

            }
        });

        return result;
    }

    /**
     * defines a UI structure of the Listview's row
     */
    static class ViewHolder{
        int position;
        TextView tvToDoListItem_Title;
        TextView tvToDoListItem_Tasks;
        ImageView ivToDoListItem_delete;


    }
}