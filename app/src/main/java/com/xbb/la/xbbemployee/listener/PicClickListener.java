package com.xbb.la.xbbemployee.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.xbb.la.xbbemployee.ui.BigImageActivity;

import java.util.ArrayList;

/**
 * Description: 大图浏览监听器
 * User: Templar
 * Date: 2015-08-21
 * Time: 19:51
 * FIXME
 */
public class PicClickListener implements View.OnClickListener {
    private int select;
    private Context mContext;
    private ArrayList<String> picUrls;

    public PicClickListener(Context mContext, int select, ArrayList<String> picUrls) {
        this.select = select;
        this.picUrls = picUrls;
        this.mContext = mContext;
    }

    @Override
    public void onClick(View arg0) {
        if (picUrls != null && !picUrls.isEmpty()) {
            Intent intent = new Intent(mContext,
                    BigImageActivity.class);
            intent.putStringArrayListExtra("img_pics", picUrls);
            intent.putExtra("pos", select);
            mContext.startActivity(intent);
        }
    }
}
