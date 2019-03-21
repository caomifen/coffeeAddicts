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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
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
            viewHolder.ivToDoListItem_Edit = convertView.findViewById(R.id.iv_ToDoList_Item_Edit);
            viewHolder.tvToDoListItem_Tasks = convertView.findViewById(R.id.tvToDoListItemTasks);
            viewHolder.ivToDoListItem_delete = convertView.findViewById(R.id.ivToDoListItem_Delete);
            viewHolder.llyRepeatHolder = convertView.findViewById(R.id.llyRepeatholder);
            viewHolder.swtToDoListItem_Notification = convertView.findViewById(R.id.switchlistItemNotification);
            viewHolder.tvMon = convertView.findViewById(R.id.tvMon);
            viewHolder.tvTue = convertView.findViewById(R.id.tvTue);
            viewHolder.tvWed = convertView.findViewById(R.id.tvWed);
            viewHolder.tvThu = convertView.findViewById(R.id.tvThu);
            viewHolder.tvFri = convertView.findViewById(R.id.tvFri);
            viewHolder.tvSat = convertView.findViewById(R.id.tvSat);
            viewHolder.tvSun = convertView.findViewById(R.id.tvSun);
            viewHolder.tvToDoListItem_Notification_Time = convertView.findViewById(R.id.tvlistItemTime);

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        try{
            JSONObject TaskObject = mlist.getJSONObject(viewHolder.position);
            ToDoListStruct todo = new ToDoListStruct(TaskObject);
            viewHolder.obj = todo;

            NotificationStruct noti = todo.getNotification();
            viewHolder.tvToDoListItem_Title.setText(todo.getTitle());
            viewHolder.tvToDoListItem_Tasks.setText(todo.getTaskStatus());
            viewHolder.swtToDoListItem_Notification.setChecked(noti.isNotificationOn());

            boolean[] selected = noti.getRepeatDays();
            viewHolder.selected = selected;
            if(!noti.isRepeat()){
                viewHolder.llyRepeatHolder.setVisibility(View.INVISIBLE);
                viewHolder.tvToDoListItem_Notification_Time.setText(noti.getDateString() + " at " + noti.getFormattedTimeString());
            } else {
                viewHolder.llyRepeatHolder.setVisibility(View.VISIBLE);
                viewHolder.tvToDoListItem_Notification_Time.setText(noti.getFormattedTimeString());
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
        if(viewHolder.swtToDoListItem_Notification.isChecked()){
            viewHolder.tvToDoListItem_Notification_Time.setTextColor(ContextCompat.getColor(mContext, android.R.color.black));
            setDay(viewHolder.tvMon, viewHolder.selected[0]);
            setDay(viewHolder.tvTue, viewHolder.selected[1]);
            setDay(viewHolder.tvWed, viewHolder.selected[2]);
            setDay(viewHolder.tvThu, viewHolder.selected[3]);
            setDay(viewHolder.tvFri, viewHolder.selected[4]);
            setDay(viewHolder.tvSat, viewHolder.selected[5]);
            setDay(viewHolder.tvSun, viewHolder.selected[6]);
        } else {
            viewHolder.tvToDoListItem_Notification_Time.setTextColor(ContextCompat.getColor(mContext, android.R.color.darker_gray));
            setDay(viewHolder.tvMon, false);
            setDay(viewHolder.tvTue, false);
            setDay(viewHolder.tvWed, false);
            setDay(viewHolder.tvThu, false);
            setDay(viewHolder.tvFri, false);
            setDay(viewHolder.tvSat, false);
            setDay(viewHolder.tvSun, false);
        }
        viewHolder.swtToDoListItem_Notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                NotificationScheduler scheduler = new NotificationScheduler(mContext);
                if(b){
                    NotificationStruct notification = viewHolder.obj.getNotification();
                    scheduler.setReminder(mContext, NotificationAlarmReceiver.class, notification.getHour(), notification.getMin(), viewHolder.position, viewHolder.position, "todo");
                    notification.setNotificationOn(b);
                    ToDoListStruct tdlstruct = viewHolder.obj;
                    tdlstruct.setNotification(notification);
                    try {
                        mlist.put(viewHolder.position, tdlstruct.getExportJSONObject());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    viewHolder.obj = tdlstruct;
                    store.saveToDoList(mlist);
                    notifyDataSetChanged();
                    viewHolder.tvToDoListItem_Notification_Time.setTextColor(ContextCompat.getColor(mContext, android.R.color.black));
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
                    ToDoListStruct tdlstruct = viewHolder.obj;
                    tdlstruct.setNotification(notification);
                    try {
                        mlist.put(viewHolder.position, tdlstruct.getExportJSONObject());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    viewHolder.obj = tdlstruct;
                    store.saveToDoList(mlist);
                    notifyDataSetChanged();

                    scheduler.cancelReminder(mContext, NotificationAlarmReceiver.class, viewHolder.position);
                    viewHolder.tvToDoListItem_Notification_Time.setTextColor(ContextCompat.getColor(mContext, android.R.color.darker_gray));
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
        viewHolder.ivToDoListItem_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationScheduler scheduler = new NotificationScheduler(mContext);
                scheduler.cancelReminder(mContext, NotificationAlarmReceiver.class, position);
                removeFromlist(position);
                Snackbar.make(view, "Deleted "+ viewHolder.tvToDoListItem_Title.getText() + " list", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                if(store.getToDoList().length()==0) {
                    parent.setVisibility(View.INVISIBLE);
                }

            }
        });

        viewHolder.ivToDoListItem_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditListDialog dialog = new EditListDialog(mContext, viewHolder.position, viewHolder.obj, new EditListDialog.OnDialogClickListener() {
                    @Override
                    public void notifyToDoDataChange() {
                        mlist = store.getToDoList();
                        notifyDataSetChanged();
                    }
                });
                dialog.show();
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
        boolean[] selected;
        ToDoListStruct obj;
        TextView tvToDoListItem_Title;
        TextView tvToDoListItem_Tasks;
        ImageView ivToDoListItem_delete;
        ImageView ivToDoListItem_Edit;
        LinearLayout llyRepeatHolder;
        Switch swtToDoListItem_Notification;
        TextView tvToDoListItem_Notification_Time;
        TextView tvMon;
        TextView tvTue;
        TextView tvWed;
        TextView tvThu;
        TextView tvFri;
        TextView tvSat;
        TextView tvSun;




    }
}