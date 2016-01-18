package com.xbb.la.xbbemployee.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xbb.la.modellibrary.bean.Message;
import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.modellibrary.utils.MLog;
import com.xbb.la.modellibrary.utils.StringUtil;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.ui.activity.MessageDetailsActivity;
import com.xbb.la.xbbemployee.widget.universaladapter.UniversalBaseAdapter;
import com.xbb.la.xbbemployee.widget.universaladapter.ViewHolder;

import java.util.List;
import java.util.Objects;

/**
 * 项目:SellerPlatform
 * 作者：Hi-Templar
 * 创建时间：2015/12/17 17:32
 * 描述：$TODO
 */
public class MessageAdapter<T> extends UniversalBaseAdapter {
    private List<Message> datas;

    public MessageAdapter(Context context, List<Message> datas, int layoutId) {
        super(context, datas, layoutId);
        this.datas = datas;
    }

    @Override
    protected void dealWithView(final Context context, ViewHolder holder, Object o) {
        final Message msg = (Message) o;
        if (msg != null) {
            TextView title_tv = holder.getView(R.id.msg_from_tv);
            TextView time_tv = holder.getView(R.id.msg_time_tv);
            TextView content_tv = holder.getView(R.id.msg_content_tv);
            ImageView mark_img = holder.getView(R.id.msg_mark_img);
            if (msg.isRead()) {
                mark_img.setImageResource(R.mipmap.msg_normal_img);
                content_tv.setTextColor(Color.parseColor("#898989"));
            } else {
                mark_img.setImageResource(R.mipmap.msg_new_img);
                content_tv.setTextColor(context.getResources().getColor(R.color.main_text_color));
            }
            content_tv.setText(msg.getContent());
            time_tv.setText(StringUtil.getDealTimeByStamp(msg.getTime()));
            title_tv.setText(msg.getTitle());
            if (ViewHolder.mPostion == datas.size() - 1)
                holder.getView(R.id.msglist_split_img).setVisibility(View.GONE);
            /*else
                holder.getView(R.id.msglist_split_img).setVisibility(View.VISIBLE);*/
        }

    }
}
