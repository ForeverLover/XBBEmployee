package com.xbb.la.xbbemployee.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xbb.la.modellibrary.bean.OrderInfo;
import com.xbb.la.modellibrary.config.Task;
import com.xbb.la.modellibrary.utils.StringUtil;
import com.xbb.la.xbbemployee.R;

import java.util.List;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/18 17:34
 * 描述：订单列表数据适配器
 */

public class OrderAdapter extends BaseAdapter {

    private List<OrderInfo> orderInfoList;
    private Context context;

    private Handler mHandler;


    /**
     * 订单列表类型
     */
    private int type;

    private int flag;

    public OrderAdapter(List<OrderInfo> orderInfoList, Context context, int type, Handler mHandler) {
        this.orderInfoList = orderInfoList;
        this.context = context;
        this.type = type;
        this.mHandler = mHandler;
    }

    @Override
    public int getCount() {
        return orderInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.order_list_item, null);
            holder.order_title_tv = (TextView) convertView.findViewById(R.id.order_title_tv);
            holder.order_deal_time_tv = (TextView) convertView.findViewById(R.id.order_deal_time_tv);
            holder.order_car_location_tv = (TextView) convertView.findViewById(R.id.order_car_location_tv);
            holder.order_service_time_tv = (TextView) convertView.findViewById(R.id.order_service_time_tv);
            holder.order_driver_name_tv = (TextView) convertView.findViewById(R.id.order_driver_name_tv);
            holder.order_car_info_tv = (TextView) convertView.findViewById(R.id.order_car_info_tv);
            holder.order_car_type_tv = (TextView) convertView.findViewById(R.id.order_car_type_tv);
            holder.order_plate_no_tv = (TextView) convertView.findViewById(R.id.order_plate_no_tv);
            holder.order_op_tv = (TextView) convertView.findViewById(R.id.order_op_tv);
            holder.order_no_tv = (TextView) convertView.findViewById(R.id.order_no_tv);
            holder.order_op_layout = (RelativeLayout) convertView.findViewById(R.id.order_op_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final OrderInfo orderInfo = orderInfoList.get(position);
        if (orderInfo != null) {
            holder.order_title_tv.setText(orderInfo.getOrderName());
            holder.order_service_time_tv.setText(orderInfo.getServiceTime());
            holder.order_driver_name_tv.setText(orderInfo.getDriverName());
            holder.order_car_info_tv.setText(orderInfo.getCarInfo());
            holder.order_car_type_tv.setText(orderInfo.getCarType());
            holder.order_car_location_tv.setText(orderInfo.getCarLocation());
            holder.order_plate_no_tv.setText(orderInfo.getPlateNo());
            holder.order_no_tv.setText(orderInfo.getOrderNo());
            String str = "";
            switch (type) {
                case 0:
                    str = context.getString(R.string.order_dilivery_deal);
                    break;
                case 1:
                    str = context.getString(R.string.order_accept_deal);
                    break;
                case 2:
                    str = context.getString(R.string.order_finish_deal);
                    break;
            }
            holder.order_deal_time_tv.setText(StringUtil.getDealTime(orderInfo.getDealTime()) + str);
            if (0 == type && StringUtil.isNumeric(orderInfo.getOrderState())) {
                flag = Integer.parseInt(orderInfo.getOrderState());
                switch (flag) {
                    case 0:
                        holder.order_op_tv.setText(context.getString(R.string.order_accept));
                        holder.order_op_layout.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        holder.order_op_tv.setText(context.getString(R.string.order_setOut));
                        holder.order_op_layout.setVisibility(View.VISIBLE);
                        break;
                    default:
                        holder.order_op_layout.setVisibility(View.GONE);
                        break;
                }
            }
            holder.order_op_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("Tag","click-flag:"+flag);
                    Message messages = mHandler.obtainMessage();
                    messages.arg1 = position;
                    messages.obj = orderInfo;
                    if ("0".equals(orderInfo.getOrderState())) {
                        messages.what = Task.ACCEPT;
                    } else if ("1".equals(orderInfo.getOrderState())) {
                        Log.v("Tag","flag=1");
                        messages.what = Task.SET_OUT;
                    }
                    mHandler.sendMessage(messages);
                }
            });
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message messages = mHandler.obtainMessage();
                messages.arg1 = position;
                messages.obj = orderInfo;
               messages.what=100;
                mHandler.sendMessage(messages);

            }
        });
        return convertView;
    }

    class ViewHolder {
        private TextView order_deal_time_tv;
        private TextView order_title_tv;
        private TextView order_service_time_tv;
        private TextView order_car_location_tv;
        private TextView order_driver_name_tv;
        private TextView order_car_info_tv;
        private TextView order_car_type_tv;
        private TextView order_plate_no_tv;
        private TextView order_op_tv;
        private RelativeLayout order_op_layout;
        private TextView order_no_tv;
    }
}
