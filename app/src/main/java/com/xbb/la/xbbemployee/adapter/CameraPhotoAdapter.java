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
import android.widget.RelativeLayout;

import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.modellibrary.utils.StringUtil;
import com.xbb.la.xbbemployee.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/9 11:06
 * 描述：本地拍照图片
 */

public class CameraPhotoAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> imgList;
    private WindowManager wm;
    private int select;
    private DisplayMetrics dm;
    private int num;

    private boolean editable;

    /**
     * @param mContext
     * @param imgList
     * @param select
     * @param num      一行的张数
     */
    public CameraPhotoAdapter(Context mContext, List<String> imgList, int select, int num) {
        this.mContext = mContext;
        this.imgList = imgList;
        this.select = select;
        this.num = num;
        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        this.editable = false;
    }

    public void editAlbum(boolean editable) {
        this.editable = editable;
        notifyDataSetChanged();
    }

    public List<String> getDataSet() {
        return this.imgList;
    }

    @Override
    public int getCount() {
        return imgList.size();
    }

    @Override
    public Object getItem(int position) {
        return imgList.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.camera_grid_item, null);
            holder.attachement_item = (ImageView) convertView.findViewById(R.id.camera_img_item);
            holder.camera_delete_img = (ImageView) convertView.findViewById(R.id.camera_delete_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.camera_delete_img.setVisibility(View.GONE);
        }
        int width = (dm.widthPixels - 35) / num;
        int height = 0;
        switch (select) {
            case 1:
                height = 4 * width / 3;
                break;
            case 2:
                height = width;
                break;
        }
        if (height == 0) {
            height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        holder.attachement_item.setLayoutParams(params);
        String path = imgList.get(position);
        InputStream is = null;
        if (!StringUtil.isEmpty(path)) {
            try {
                if (Constant.Path.ADD_PIC_Path.equals(path)) {
                   /* is = mContext.getClass().getClassLoader().getResourceAsStream("assets/add.png");*/
                    holder.attachement_item.setScaleType(ImageView.ScaleType.CENTER);
                    holder.attachement_item.setImageResource(R.mipmap.add);
                } else {
                    is = new FileInputStream(path);
                    if (editable)
                        holder.camera_delete_img.setVisibility(View.VISIBLE);
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inTempStorage = new byte[100 * 1024];

                    opts.inPreferredConfig = Bitmap.Config.RGB_565;

                    opts.inPurgeable = true;

                    opts.inSampleSize = 4;

                    opts.inInputShareable = true;
                    Bitmap bitmap = BitmapFactory.decodeStream(is, null, opts);
                    Log.v("Tag", "is==null?" + (is == null));
                    if (bitmap != null) {
                        holder.attachement_item.setScaleType(ImageView.ScaleType.FIT_XY);
                        holder.attachement_item.setImageBitmap(bitmap);
                        Log.v("Tag", "bitmap is not null");
                    } else {
                        Log.v("Tag", "bitmap is null");

                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.v("Tag", "path:" + path + "msg:" + e.getMessage());
            }


        }
        holder.camera_delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgList.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView attachement_item;
        ImageView camera_delete_img;

    }
}
