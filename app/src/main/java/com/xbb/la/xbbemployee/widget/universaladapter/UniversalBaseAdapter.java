package com.xbb.la.xbbemployee.widget.universaladapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.blueware.com.google.gson.internal.T;
import com.xbb.la.modellibrary.bean.Employee;

import java.util.List;

/**
 * 项目:Paradise
 * 作者：Hi-Templar
 * 创建时间：2015/12/26 13:12
 * 描述：普通类型通用数据适配器
 */
public abstract class UniversalBaseAdapter<T>  extends BaseAdapter {
    private Context mContext;
    private List<T> mDatas;
    private int mLayoutId;

    public UniversalBaseAdapter(Context context, List<T> datas, int layoutId) {
        this.mContext = context;
        this.mDatas = datas;
        this.mLayoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder(mContext, parent, mLayoutId, position);
        dealWithView(mContext,holder, getItem(position));
        return holder.getConvertView();
    }

    protected abstract void dealWithView(Context context,ViewHolder holder, T t);
}
