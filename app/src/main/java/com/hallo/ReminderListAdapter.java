package com.hallo;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReminderListAdapter extends BaseAdapter {
    private JSONArray mlist;
    private Context mContext;
    PersistentStore store;

    public ReminderListAdapter(Context context, JSONArray list, PersistentStore _store){
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
        store.saveReminderList(mlist);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ReminderListAdapter.ViewHolder viewHolder;
        final View result;
        if (convertView == null){
            viewHolder = new ReminderListAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.reminderlist_items, null);
            viewHolder.position = position;
            viewHolder.rem_name = convertView.findViewById(R.id.ReminderTitle);
            viewHolder.rem_date = convertView.findViewById(R.id.ReminderDate);
            viewHolder.rem_time = convertView.findViewById(R.id.ReminderTime);
            viewHolder.rem_repeat = convertView.findViewById(R.id.ReminderRepeat);
            viewHolder.rem_delete = convertView.findViewById(R.id.Reminder_Delete);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ReminderListAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }
        try{
            JSONObject TaskObject = mlist.getJSONObject(viewHolder.position);
            ReminderListStruct rem = new ReminderListStruct(TaskObject);
            viewHolder.obj = rem;

            viewHolder.rem_name.setText(rem.getRemName());
            viewHolder.rem_date.setText(rem.getRemDate());
            viewHolder.rem_time.setText(rem.getRemTime());
            viewHolder.rem_repeat.setText(rem.getRemRepeat());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        viewHolder.rem_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFromlist(position);
                Snackbar.make(view, "Deleted "+ viewHolder.rem_name.getText() + " Reminder", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                if(store.getReminderList().length()==0) {
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
        ReminderListStruct obj;
        TextView rem_name;
        TextView rem_date;
        TextView rem_time;
        TextView rem_repeat;
        ImageView rem_delete;


    }
}
