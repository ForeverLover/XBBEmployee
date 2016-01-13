package com.xbb.la.xbbemployee.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xbb.la.modellibrary.bean.DIYProduct;
import com.xbb.la.xbbemployee.R;

import java.util.List;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/11 14:58
 * 描述：diy产品
 */

public class DIYProductAdapter extends BaseAdapter {
    private Activity activity;
    private List<DIYProduct> diyProductList;
    private boolean hasChecked;
    private String diyId;

    public DIYProductAdapter(Activity activity, List<DIYProduct> diyProductList) {
        this.activity = activity;
        this.diyProductList = diyProductList;
    }

    public void update(boolean hasChecked) {
        this.hasChecked = hasChecked;
        this.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return diyProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return diyProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.diy_grid_item, null);
            holder = new ViewHolder();
            holder.diy_grid_name = (TextView) convertView.findViewById(R.id.diy_grid_name);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        DIYProduct diyProduct = diyProductList.get(position);
        if (diyProduct != null) {
            if (diyProduct.isSelected()) {
                convertView.setBackgroundResource(R.drawable.btn_solid_s_bg);
                holder.diy_grid_name.setTextColor(activity.getResources().getColor(R.color.white));
            } else if (hasChecked) {
                convertView.setBackgroundResource(R.drawable.transaction_op_bg);
                holder.diy_grid_name.setTextColor(Color.parseColor("#b3b3b3"));
            } else {
                convertView.setBackgroundResource(R.drawable.transaction_op_bg);
                holder.diy_grid_name.setTextColor(activity.getResources().getColor(R.color.main_text_color));
            }
            holder.diy_grid_name.setText(diyProduct.getP_name());
        }
        return convertView;
    }

    class ViewHolder {

        private TextView diy_grid_name;

    }
}
