package com.hallo;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
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
            viewHolder.tv_rem_name = convertView.findViewById(R.id.ReminderTitle);
            viewHolder.iv_rem_delete = convertView.findViewById(R.id.Reminder_Delete);
            viewHolder.llyRepeatHolder = convertView.findViewById(R.id.llyRepeatholder);
            viewHolder.swtRemItem_Notification = convertView.findViewById(R.id.switchReminderItemNotification);
            viewHolder.tvRemItem_Notification_Time = convertView.findViewById(R.id.tvReminderItemTime);
            viewHolder.tvMon = convertView.findViewById(R.id.tvMon);
            viewHolder.tvTue = convertView.findViewById(R.id.tvTue);
            viewHolder.tvWed = convertView.findViewById(R.id.tvWed);
            viewHolder.tvThu = convertView.findViewById(R.id.tvThu);
            viewHolder.tvFri = convertView.findViewById(R.id.tvFri);
            viewHolder.tvSat = convertView.findViewById(R.id.tvSat);
            viewHolder.tvSun = convertView.findViewById(R.id.tvSun);

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
            NotificationStruct notification = rem.getNotification();
            viewHolder.swtRemItem_Notification.setChecked(notification.isNotificationOn());
            boolean[] selected = notification.getRepeatDays();
            viewHolder.selected = selected;
            viewHolder.tv_rem_name.setText(rem.getRemName());

            if(!notification.isRepeat()){
                viewHolder.llyRepeatHolder.setVisibility(View.INVISIBLE);
                viewHolder.tvRemItem_Notification_Time.setText(notification.getDateString() + " at " + notification.getFormattedTimeString());
            } else {
                viewHolder.llyRepeatHolder.setVisibility(View.VISIBLE);
                viewHolder.tvRemItem_Notification_Time.setText(notification.getFormattedTimeString());
                setDay(viewHolder.tvMon, selected[0]);
                setDay(viewHolder.tvTue, selected[1]);
                setDay(viewHolder.tvWed, selected[2]);
                setDay(viewHolder.tvThu, selected[3]);
                setDay(viewHolder.tvFri, selected[4]);
                setDay(viewHolder.tvSat, selected[5]);
                setDay(viewHolder.tvSun, selected[6]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        viewHolder.swtRemItem_Notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                NotificationScheduler scheduler = new NotificationScheduler(mContext);
                if(b){
                    NotificationStruct notification = viewHolder.obj.getNotification();
                    scheduler.setReminder(mContext, NotificationAlarmReceiver.class, notification.getHour(), notification.getMin(), viewHolder.position, viewHolder.position +100, "reminder");
                    notification.setNotificationOn(b);
                    ReminderListStruct rem_struct = viewHolder.obj;
                    rem_struct.setNotification(notification);
                    try {
                        mlist.put(viewHolder.position, rem_struct.getExportJSONObject());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    viewHolder.obj = rem_struct;
                    store.saveReminderList(mlist);
                    notifyDataSetChanged();
                    viewHolder.tvRemItem_Notification_Time.setTextColor(ContextCompat.getColor(mContext, android.R.color.black));
                    setDay(viewHolder.tvMon, viewHolder.selected[0]);
                    setDay(viewHolder.tvTue, viewHolder.selected[1]);
                    setDay(viewHolder.tvWed, viewHolder.selected[2]);
                    setDay(viewHolder.tvThu, viewHolder.selected[3]);
                    setDay(viewHolder.tvFri, viewHolder.selected[4]);
                    setDay(viewHolder.tvSat, viewHolder.selected[5]);
                    setDay(viewHolder.tvSun, viewHolder.selected[6]);
                } else {
                    NotificationStruct notification = viewHolder.obj.getNotification();
                    notification.setNotificationOn(false);
                    ReminderListStruct rem_struct = viewHolder.obj;
                    rem_struct.setNotification(notification);
                    try {
                        mlist.put(viewHolder.position, rem_struct.getExportJSONObject());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    viewHolder.obj = rem_struct;
                    store.saveReminderList(mlist);
                    notifyDataSetChanged();

                    scheduler.cancelReminder(mContext, NotificationAlarmReceiver.class, viewHolder.position+100);
                    viewHolder.tvRemItem_Notification_Time.setTextColor(ContextCompat.getColor(mContext, android.R.color.darker_gray));
                    setDay(viewHolder.tvMon, false);
                    setDay(viewHolder.tvTue, false);
                    setDay(viewHolder.tvWed, false);
                    setDay(viewHolder.tvThu, false);
                    setDay(viewHolder.tvFri, false);
                    setDay(viewHolder.tvSat, false);
                    setDay(viewHolder.tvSun, false);
                }
            }
        });
        viewHolder.iv_rem_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationScheduler scheduler = new NotificationScheduler(mContext);
                scheduler.cancelReminder(mContext, NotificationAlarmReceiver.class, viewHolder.position+100);
                removeFromlist(position);
                Snackbar.make(view, "Deleted "+ viewHolder.tv_rem_name.getText() + " Reminder", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                if(store.getReminderList().length()==0) {
                    parent.setVisibility(View.INVISIBLE);
                }

            }
        });

        return result;
    }
    public void setDay(TextView tv, boolean selected){
        if(selected){
            tv.setTextColor(ContextCompat.getColor(mContext, android.R.color.black));
        } else {
            tv.setTextColor(ContextCompat.getColor(mContext, android.R.color.darker_gray));
        }
    }

    /**
     * defines a UI structure of the Listview's row
     */
    static class ViewHolder{
        int position;
        ReminderListStruct obj;
        TextView tv_rem_name;
        ImageView iv_rem_delete;
        boolean[] selected;
        //ImageView iv_Rem_Edit;
        LinearLayout llyRepeatHolder;
        Switch swtRemItem_Notification;
        TextView tvRemItem_Notification_Time;
        TextView tvMon;
        TextView tvTue;
        TextView tvWed;
        TextView tvThu;
        TextView tvFri;
        TextView tvSat;
        TextView tvSun;


    }
}
