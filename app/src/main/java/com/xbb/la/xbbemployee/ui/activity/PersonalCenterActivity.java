package com.xbb.la.xbbemployee.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xbb.la.modellibrary.bean.Employee;
import com.xbb.la.modellibrary.bean.ResponseJson;
import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.modellibrary.config.Task;
import com.xbb.la.modellibrary.net.ApiRequest;
import com.xbb.la.modellibrary.net.IApiRequest;
import com.xbb.la.modellibrary.utils.MLog;
import com.xbb.la.modellibrary.utils.ParseUtil;
import com.xbb.la.modellibrary.utils.StringUtil;
import com.xbb.la.modellibrary.utils.SystemUtil;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.config.TitleActivity;
import com.xbb.la.xbbemployee.listener.AnimateFirstDisplayListener;
import com.xbb.la.xbbemployee.utils.MImageLoader;
import com.xbb.la.xbbemployee.utils.SharePreferenceUtil;
import com.xbb.la.xbbemployee.widget.RoundImageView;

import java.io.File;

/**
 * 项目:SellerPlatform
 * 作者：Hi-Templar
 * 创建时间：2015/12/17 17:32
 * 描述：$TODO
 */
public class PersonalCenterActivity extends TitleActivity {
    @ViewInject(R.id.pc_avatar_img)
    private RoundImageView pc_avatar_img;
    @ViewInject(R.id.pc_name_tv)
    private TextView pc_name_tv;
    @ViewInject(R.id.pc_gender_tv)
    private TextView pc_gender_tv;
    @ViewInject(R.id.pc_age_tv)
    private TextView pc_age_tv;
    @ViewInject(R.id.pc_empNo_tv)
    private TextView pc_empNo_tv;
    @ViewInject(R.id.pc_tel_tv)
    private TextView pc_tel_tv;
    @ViewInject(R.id.pc_wx_tv)
    private TextView pc_wx_tv;

    private Employee employee;

    private Bitmap bitmap;
    private String filePath;

    private String gender;
    private String age;
    private String[] sexArray;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalcenter);
    }

    @Override
    public void initData() {
        super.initData();
        employee = SharePreferenceUtil.getInstance().getUserInfo(this);
        sexArray = getResources().getStringArray(R.array.gender);
        apiRequest = new ApiRequest(this);
        uid = SharePreferenceUtil.getInstance().getUserId(this);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle(R.string.menu_mine_text);
        bindViewWithData();

    }

    private void bindViewWithData() {
        if (employee != null) {
            MImageLoader.getInstance(this).displayImageByHalfUrl(employee.getAvatar(), pc_avatar_img, R.mipmap.login_head_default_img, new AnimateFirstDisplayListener());
            pc_age_tv.setText(employee.getAge() + "");
            pc_empNo_tv.setText(employee.getEmpNo());
            pc_gender_tv.setText(sexArray[0]);
            gender = employee.getGender();
            if (StringUtil.isNumeric(employee.getGender()))
                pc_gender_tv.setText(sexArray[Integer.parseInt(employee.getGender())]);
            pc_name_tv.setText(employee.getNickname());
            pc_tel_tv.setText(employee.getTel());
            pc_wx_tv.setText(employee.getWx());
        }
    }


    @OnClick({R.id.pc_name_layout, R.id.pc_gender_layout,
            R.id.pc_age_layout, R.id.pc_empNo_tv, R.id.pc_tel_layout,
            R.id.pc_wx_layout, R.id.pc_alterpwd_layout, R.id.pc_avatar_layout})
    private void updateInfo(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.pc_avatar_layout:
                intent = new Intent(this, SelectPictureActivity.class);
                intent.putExtra("type", 1);
                startActivityForResult(intent, Constant.RequestCode.GET_PORTRAIT);
                break;
            case R.id.pc_name_layout:
                if (employee==null)
                    employee=SharePreferenceUtil.getInstance().getUserInfo(this);
                startActivityForResult(new Intent(this, AlterNicknameActivity.class).putExtra("nickname",employee.getNickname()), Constant.RequestCode.ALTER_NAME);
                break;
            case R.id.pc_gender_layout:
                intent = new Intent(this, SelectActivity.class);
                intent.putExtra("title", getString(R.string.ed_pc_gender_title));
                intent.putExtra("data", sexArray);
                intent.putExtra("index", gender);
                startActivityForResult(intent, Constant.RequestCode.GET_GENDER);
                break;
            case R.id.pc_age_layout:
                intent = new Intent(this, SelectActivity.class);
                intent.putExtra("title", getString(R.string.ed_pc_age_title));
                intent.putExtra("data", StringUtil.getAgeArray());
                startActivityForResult(intent, Constant.RequestCode.GET_AGE);
                break;
            case R.id.pc_empNo_tv:

                break;
            case R.id.pc_tel_layout:

                break;
            case R.id.pc_wx_layout:

                break;
            case R.id.pc_alterpwd_layout:
                startActivityForResult(new Intent(this, AlterpwdActivity.class), Constant.RequestCode.ALTER_PWD);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && data != null) {
            String text = data.getStringExtra("text");
            switch (requestCode) {
                case Constant.RequestCode.ALTER_PWD:
                    break;
                case Constant.RequestCode.ALTER_NAME:
                   pc_name_tv.setText(text);
                    break;
                case Constant.RequestCode.GET_PORTRAIT:
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        bitmap = bundle.getParcelable("data");
                    }
                    if (bitmap != null) {
                        filePath = SystemUtil.getInstance().saveBitmap2file(bitmap);
                        if (!StringUtil.isEmpty(filePath)) {
                            File file = new File(filePath);
                            apiRequest.changeAvatar(uid, file);
                        } else
                            showToast(R.string.pc_alteravatar_failed);


                    }
                    break;
                case Constant.RequestCode.GET_AGE:
                    pc_age_tv.setText(text);
                    age = text;
                    if (!text.equals(employee.getGender()))
                        apiRequest.changeAge(uid, age);

                    break;
                case Constant.RequestCode.GET_GENDER:
                    pc_gender_tv.setText(text);
                    String sex = data.getStringExtra("currentIndex");
                    gender = sex;
                    if (!gender.equals(employee.getGender()))
                        apiRequest.chageGender(uid, gender);
                    break;
            }
        }
    }

    @Override
    public void onSuccess(int taskId, Object... params) {
        super.onSuccess(taskId, params);
        ResponseJson responseJson = (ResponseJson) params[0];
        switch (taskId) {
            case Task.AVATAR_CHANGED:
                pc_avatar_img.setImageBitmap(bitmap);
                String avatar = ParseUtil.getInstance().parseAvatarPath(responseJson.getResult().toString());
                MLog.v("Tag","url-avatar:"+avatar);
                if (!StringUtil.isEmpty(avatar)) {
                    employee.setAvatar(avatar);
                    SharePreferenceUtil.getInstance().saveUserInfo(this, employee);
                    localBroadcastManager.sendBroadcast(new Intent(Constant.IntentAction.AVATAR_CHANGED));
                    showToast(R.string.pc_alteravatar_succeed);
                }
                break;
            case Task.GENDER_CHANGED:
                employee.setGender(gender);
                SharePreferenceUtil.getInstance().saveUserInfo(this, employee);
                showToast(R.string.pc_altersex_succeed);
                break;
            case Task.AGE_CHANGED:
                employee.setAge(age);
                SharePreferenceUtil.getInstance().saveUserInfo(this, employee);
                showToast(R.string.pc_alterage_succeed);
                break;
        }

    }

    @Override
    public void onPrepare(int taskId) {
        super.onPrepare(taskId);
        showLoading();
    }

    @Override
    public void onEnd(int taskId) {
        super.onEnd(taskId);
        dismissLoading();
    }

}
