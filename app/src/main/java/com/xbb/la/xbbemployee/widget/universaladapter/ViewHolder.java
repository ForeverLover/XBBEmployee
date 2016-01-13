package com.xbb.la.xbbemployee.widget.universaladapter;

import android.content.Context;
import android.opengl.Visibility;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 项目:Paradise
 * 作者：Hi-Templar
 * 创建时间：2015/12/26 13:12
 * 描述：普通类型通用数据缓存
 */
public class ViewHolder {
    private SparseArray<View> mViews = null;
    public static  int mPostion;
    private View mConvertView;

    public ViewHolder(Context context, ViewGroup parent, int layoutId, int positon) {

        this.mPostion = positon;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int postion, int layoutId) {
        if (convertView == null)
            return new ViewHolder(context, parent, layoutId, postion);
        else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            mPostion=postion;
            return holder;
        }
    }

    /**
     * 通过viewid获取空间
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {

        return mConvertView;
    }

    public ViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public ViewHolder setImgResource(int viewId, int resId) {
        ImageView iv = getView(viewId);
        iv.setImageResource(resId);

        return this;
    }

    public ViewHolder setViewVisiblity(int viewId,int visiblity){
        getView(viewId).setVisibility(visiblity);
        return this;
    }
}
