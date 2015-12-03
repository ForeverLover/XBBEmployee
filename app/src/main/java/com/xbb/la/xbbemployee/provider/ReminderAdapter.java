package com.xbb.la.xbbemployee.provider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.xbb.la.modellibrary.bean.Reminder;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.utils.MImageLoader;

import java.util.HashSet;
import java.util.List;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/12 13:25
 * 描述：
 */

public class ReminderAdapter extends BaseAdapter {
    private Context context;
    private List<Reminder> reminderList;

    private HashSet<Reminder> selectReminderSet;

    public ReminderAdapter(Context context, List<Reminder> reminderList) {
        this.context = context;
        this.reminderList = reminderList;
    }

    @Override
    public int getCount() {
        return reminderList.size();
    }

    @Override
    public Object getItem(int position) {
        return reminderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.reminder_list_item, null);
            holder.reminder_item_cb = (CheckBox) convertView.findViewById(R.id.reminder_item_cb);
            holder.reminder_item_content = (TextView) convertView.findViewById(R.id.reminder_item_content);
            holder.reminder_item_img = (ImageView) convertView.findViewById(R.id.reminder_item_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Reminder reminder = reminderList.get(position);
        if (reminder != null) {
            MImageLoader.getInstance(context).displayImageM(reminder.getThumb(),holder.reminder_item_img);
            holder.reminder_item_content.setText(reminder.getReminderText());
            holder.reminder_item_cb.setChecked(reminder.isSelect());
            holder.reminder_item_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (selectReminderSet == null)
                        selectReminderSet = new HashSet<Reminder>();
                    if (isChecked)
                        selectReminderSet.add(reminder);
                    else
                        selectReminderSet.remove(reminder);

                }
            });
        }
        return convertView;
    }

    public HashSet<Reminder> getSelectedReminderList() {
        return selectReminderSet;
    }

    class ViewHolder {
        private CheckBox reminder_item_cb;
        private TextView reminder_item_content;
        private ImageView reminder_item_img;

    }
}
