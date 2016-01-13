package com.xbb.la.xbbemployee.ui.activity;

import android.os.Bundle;

import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.config.TitleActivity;

/**
 * 项目:SellerPlatform
 * 作者：Hi-Templar
 * 创建时间：2015/12/17 17:32
 * 描述：$TODO
 */
public class MessageDetailsActivity extends TitleActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_details);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle(R.string.msg_details_title);
    }
}
