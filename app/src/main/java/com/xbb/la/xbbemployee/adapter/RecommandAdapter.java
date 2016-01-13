package com.xbb.la.xbbemployee.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xbb.la.modellibrary.bean.DIYProduct;
import com.xbb.la.modellibrary.bean.Recommand;
import com.xbb.la.modellibrary.utils.StringUtil;
import com.xbb.la.modellibrary.utils.SystemUtil;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.utils.MImageLoader;

import java.util.List;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/11 15:54
 * 描述：建议列表数据适配器
 */

public class RecommandAdapter extends BaseAdapter {
    private Context context;
    private List<Recommand> recommandList;
    private WindowManager wm;
    private DisplayMetrics dm;
    private LinearLayout.LayoutParams params;

    public RecommandAdapter(Context context, List<Recommand> recommandList) {
        this.context = context;
        this.recommandList = recommandList;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
    }

    @Override
    public int getCount() {
        return recommandList.size();
    }

    @Override
    public Object getItem(int position) {
        return recommandList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.recommand_list_item, null);
            holder.recommand_describe_img = (ImageView) convertView.findViewById(R.id.recommand_describe_img);
            holder.recommand_describe_img_one = (ImageView) convertView.findViewById(R.id.recommand_describe_img_one);
            holder.recommand_describe_img_two = (ImageView) convertView.findViewById(R.id.recommand_describe_img_two);
            holder.recommand_describe_img_three = (ImageView) convertView.findViewById(R.id.recommand_describe_img_three);
            holder.recommand_describe_layout = (LinearLayout) convertView.findViewById(R.id.recommand_describe_layout);
            holder.recommand_remark_layout = (LinearLayout) convertView.findViewById(R.id.recommand_remark_layout);
            holder.recommand_remark_content = (TextView) convertView.findViewById(R.id.recommand_remark_content);
            holder.recommand_diy_name = (TextView) convertView.findViewById(R.id.recommand_diy_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Recommand recommand = recommandList.get(position);
        if (recommand != null) {
            DIYProduct diyProduct = recommand.getSelectedDIYProduct();
            if (diyProduct != null) {
                holder.recommand_diy_name.setText(diyProduct.getP_name());
                MImageLoader.getInstance(context).displayImageM(recommand.getSelectedDIYProduct().getP_ximg(), holder.recommand_describe_img);
                Log.v("Tag", "diy-img:(width,height)---->(" + holder.recommand_describe_img.getWidth() + "," + holder.recommand_describe_img.getHeight());
            }
            List<String> select = recommand.getIntroAlbum();
            if (select != null && !select.isEmpty()) {
                holder.recommand_describe_layout.setVisibility(View.VISIBLE);
                for (int i = 0; i < select.size(); i++) {
                    Bitmap bitmap = SystemUtil.getInstance().getBitmapFromFile(select.get(i));
                    bitmap = bitmap != null ? bitmap : BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
                    switch (i) {
                        case 0:
                            holder.recommand_describe_img_one.setImageBitmap(bitmap);
                            break;
                        case 1:
                            holder.recommand_describe_img_two.setImageBitmap(bitmap);
                            break;
                        case 2:
                            holder.recommand_describe_img_three.setImageBitmap(bitmap);
                            break;
                    }
                }
            } else
                holder.recommand_describe_layout.setVisibility(View.INVISIBLE);
            String remark = recommand.getRemark();
            if (StringUtil.isEmpty(remark))
                holder.recommand_remark_layout.setVisibility(View.GONE);
            else {
                holder.recommand_remark_layout.setVisibility(View.VISIBLE);
                holder.recommand_remark_content.setText(remark);
            }


        }
        return convertView;
    }


    class ViewHolder {
        private LinearLayout recommand_describe_layout;
        private LinearLayout recommand_remark_layout;

        private ImageView recommand_describe_img_three;
        private ImageView recommand_describe_img_two;
        private ImageView recommand_describe_img_one;

        private ImageView recommand_describe_img;
        private TextView recommand_remark_content;
        private TextView recommand_diy_name;
    }
}
