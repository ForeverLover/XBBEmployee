package com.xbb.la.xbbemployee.ui.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.xbb.la.modellibrary.bean.Transaction;
import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.modellibrary.utils.MLog;
import com.xbb.la.modellibrary.utils.StringUtil;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.adapter.CameraPhotoAdapter;
import com.xbb.la.xbbemployee.config.TitleActivity;
import com.xbb.la.xbbemployee.provider.DBHelperMethod;
import com.xbb.la.xbbemployee.receiver.FinisActivityReceiver;
import com.xbb.la.xbbemployee.utils.SharePreferenceUtil;
import com.xbb.la.xbbemployee.widget.CustomGridView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/6 16:20
 * 描述：结束作业
 */

public class TransactionEndActivity extends TitleActivity {
    private IntentFilter intentFilter;
    private BroadcastReceiver finishReceiver;

    @ViewInject(R.id.end_car_normal_gv)
    private CustomGridView end_car_normal_gv;
    @ViewInject(R.id.end_car_damage_gv)
    private CustomGridView end_car_damage_gv;
    @ViewInject(R.id.end_car_nextBtn)
    private Button end_car_nextBtn;

    @ViewInject(R.id.body_edit_btn)
    private TextView body_edit_btn;
    @ViewInject(R.id.body_preview_btn)
    private TextView body_preview_btn;
    @ViewInject(R.id.nick_edit_btn)
    private TextView nick_edit_btn;
    @ViewInject(R.id.nick_preview_btn)
    private TextView nick_preview_btn;

    private List<String> endNormalList;
    private List<String> endDamageList;
    private CameraPhotoAdapter normalAdapter;
    private CameraPhotoAdapter damageAdapter;
    private int normalPointIndex;
    private boolean normalFlag = false;
    private boolean normalIsFinished;
    private int damagePointIndex;
    private boolean damageFlag = false;

    private final int ADD_PHOTO = 1001;

    /**
     * 当前操作类型
     * 1-车身照
     * 2-划痕照
     */
    private int type;

    /**
     * 1-添加开始业务数据
     * 2-修改开始业务数据
     */
    private int select;

    private Transaction transaction;

    private String TAG = getClass().getSimpleName();
    private String path /*= "file:///android_asset/mate_add_pics"*/;
    private String orderId;

    private boolean normalEditable;
    private boolean damageEditable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_end);
    }


    @Override
    public void initData() {
        setTitle(getString(R.string.transaction_end));
        intentFilter = new IntentFilter(Constant.IntentAction.TRANSACTION_TO_FINISH);
        finishReceiver = new FinisActivityReceiver(this);
        localBroadcastManager.registerReceiver(finishReceiver, intentFilter);

//        initList();
        path = Constant.Path.ADD_PIC_Path;
        normalEditable = true;
        damageEditable = true;
        userId = SharePreferenceUtil.getInstance().getUserId(this);
        orderId = getIntent().getStringExtra(Constant.IntentVariable.ORDER_ID);
        select = getIntent().getIntExtra("select", 1);
//        transaction=DBHelperMethod.getInstance().getTransaction(orderId,userId);
        if (1 == select)
            initList();
        else
            getLocalList();
        end_car_nextBtn.setOnClickListener(this);

        body_preview_btn.setOnClickListener(this);
        body_edit_btn.setOnClickListener(this);
        nick_edit_btn.setOnClickListener(this);
        nick_preview_btn.setOnClickListener(this);

        end_car_normal_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                type = 1;
                normalPointIndex = position;
                if (endNormalList != null && endNormalList.size() < 4) {
                    if (position == endNormalList.size() - 1) {
                        normalFlag = true;
                    } else
                    normalFlag = false;
                } else if (endNormalList != null && endNormalList.size() == 4 && !normalIsFinished) {
                    normalFlag = true;
                } else {
                    normalFlag = false;
                }
                Intent intent = new Intent(TransactionEndActivity.this, SelectPictureActivity.class);
                intent.putExtra("type", 2);
                startActivityForResult(intent, ADD_PHOTO);
            }
        });
        end_car_damage_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                type = 2;
                damagePointIndex = position;
                if (damagePointIndex == endDamageList.size() - 1) {
                    damageFlag = true;
                } else {
                    damageFlag = false;
                }
                Intent intent = new Intent(TransactionEndActivity.this, SelectPictureActivity.class);
                intent.putExtra("type", 2);
                startActivityForResult(intent, ADD_PHOTO);

            }
        });
    }

    private void getLocalList() {
        transaction = DBHelperMethod.getInstance().getTransaction(orderId, userId);
        if (transaction != null) {
            endDamageList = StringUtil.covertStringToStringList(transaction.getDamageAlbumAfter());
            endNormalList = StringUtil.covertStringToStringList(transaction.getNormalAlbumAfter());
            if (!StringUtil.isEmpty(path)) {
                if (endDamageList == null)
                    endDamageList = new ArrayList<String>();
                if (endNormalList == null)
                    endNormalList = new ArrayList<String>();
                if (endNormalList.isEmpty() || endNormalList.size() < 4)
                    endNormalList.add(path);
                else
                    normalIsFinished = true;
                endDamageList.add(path);
            }
            if (endNormalList != null && !endNormalList.isEmpty()) {
                normalAdapter = new CameraPhotoAdapter(this, endNormalList, 2, 4);
                end_car_normal_gv.setAdapter(normalAdapter);
            }
            if (endDamageList != null && !endDamageList.isEmpty()) {
                damageAdapter = new CameraPhotoAdapter(this, endDamageList, 2, 4);
                end_car_damage_gv.setAdapter(damageAdapter);
            }
        }
    }

    private void initList() {
        endDamageList = new ArrayList<String>();
        endNormalList = new ArrayList<String>();
        if (!StringUtil.isEmpty(path)) {
            endDamageList.add(path);
            endNormalList.add(path);
        }
        if (endNormalList != null && !endNormalList.isEmpty()) {
            normalAdapter = new CameraPhotoAdapter(this, endNormalList, 2, 4);
            end_car_normal_gv.setAdapter(normalAdapter);
        }
        if (endDamageList != null && !endDamageList.isEmpty()) {
            damageAdapter = new CameraPhotoAdapter(this, endDamageList, 2, 4);
            end_car_damage_gv.setAdapter(damageAdapter);
        }
    }

    @Override
    protected void onClickTitleLeft(View v) {
        doFinish();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.end_car_nextBtn:
                if (!normalIsFinished) {
                    showToast(getString(R.string.car_too_more));
                    return;
                }
                if (transaction == null)
                    transaction = new Transaction();
                transaction.setNormalAlbumAfter(StringUtil.covertStringListToString(endNormalList));
                if (endDamageList != null && endDamageList.size() > 1)
                    transaction.setDamageAlbumAfter(StringUtil.covertStringListToString(endDamageList.subList(0, endDamageList.size() - 1)));
                else
                    transaction.setDamageAlbumAfter("");
                transaction.setEmployeeId(userId);
                transaction.setOrderId(orderId);
                boolean flag;
                if (1 == select)
                    flag = DBHelperMethod.getInstance().insertTransaction(2, transaction);
                else
                    flag = DBHelperMethod.getInstance().updateTransaction(2, transaction, false);
                if (flag) {
                    Intent intent = new Intent(Constant.IntentAction.TRANSACTION_TO_FINISH);
                    intent.putExtra("step", 2);
                    intent.putExtra("insert", 1 == select);
                    localBroadcastManager.sendBroadcast(intent);
                } else {
                    showToast(getString(R.string.local_cache_failed));
                }
               /* if (flag)
                    startActivity(new Intent(this, TransactionRecommandActivity.class).putExtra("orderId", orderId));
                else
                    showToast(getString(R.string.local_cache_failed));*/
                break;
            case R.id.body_edit_btn:
                if (normalAdapter != null) {
                    normalAdapter.editAlbum(normalEditable);
                    if (normalEditable) {
                        body_edit_btn.setText(getString(R.string.label_edit_finish));
                    } else {
                        body_edit_btn.setText(getString(R.string.label_edit));
                        endNormalList = normalAdapter.getDataSet();
                    }
                    normalEditable = !normalEditable;
                }
                if (normalIsFinished && endNormalList.size() < 4) {
                    normalIsFinished = false;
                    endNormalList.add(path);
                }
                break;
            case R.id.body_preview_btn:
                showAlbum(endNormalList);
                break;
            case R.id.nick_edit_btn:
                if (damageAdapter != null) {
                    damageAdapter.editAlbum(damageEditable);
                    if (damageEditable) {
                        nick_edit_btn.setText(getString(R.string.label_edit_finish));
                    } else {
                        endDamageList = damageAdapter.getDataSet();
                        nick_edit_btn.setText(getString(R.string.label_edit));
                    }
                    damageEditable = !damageEditable;
                }
                break;
            case R.id.nick_preview_btn:
                showAlbum(endDamageList);
                break;
        }
    }

    private void showAlbum(List<String> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            showToast(getString(R.string.tost_no_pic));
            return;
        }
        ArrayList<String> picList = new ArrayList<String>();
        if (dataList.contains(Constant.Path.ADD_PIC_Path) && dataList.size() > 1)
            picList.addAll(dataList.subList(0, dataList.size() - 1));
        else
            picList.addAll(dataList);
        Intent intent = new Intent(this,
                BigImageActivity.class);

        intent.putStringArrayListExtra("img_pics", picList);
        intent.putExtra("pos", 0);
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(finishReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {

                case ADD_PHOTO:
                    Bundle bundle = data.getExtras();
                    String path = data.getStringExtra(Constant.IntentAction.KEY_PHOTO_PATH);
                    Bitmap bitmap = null;
                    if (!StringUtil.isEmpty(path)) {
                        File file = new File(path);
                        MLog.i("Tag", "fileSize=" + file.length());
                        double picSize = file.length() / (1024 * 1024);
                        if (picSize >= 3) {
                            showToast(getString(R.string.green_pic_size));
                            return;
                        }
                    }
                   /* if (bundle != null) {
                        bitmap = bundle.getParcelable("data");
                    }*/
                    if (!StringUtil.isEmpty(path)) {
                        switch (type) {
                            case 1:
                                if (endNormalList != null) {
                                    if (normalFlag) {
                                        normalFlag = false;
                                        switch (endNormalList.size() - 1) {
                                            case 0:
                                                endNormalList.add(0, path);
                                                break;
                                            case 1:
                                                endNormalList.add(1, path);
                                                break;
                                            case 2:
                                                endNormalList.add(2, path);
                                                break;
                                            case 3:
                                                endNormalList.add(3, path);
                                                endNormalList.remove(4);
                                                normalIsFinished = true;
                                                break;
                                            default:
                                                showToast(getString(R.string.car_too_more));
                                                break;
                                        }
                                    } else {
                                        switch (normalPointIndex) {
                                            case 0:
                                                endNormalList.set(0, path);
                                                break;
                                            case 1:
                                                endNormalList.set(1, path);
                                                break;
                                            case 2:
                                                endNormalList.set(2, path);
                                                break;
                                            case 3:
                                                endNormalList.set(3, path);
                                                break;
                                        }
                                    }
                                    normalAdapter = new CameraPhotoAdapter(this, endNormalList, 2, 4);
                                    end_car_normal_gv.setAdapter(normalAdapter);
                                }
                                break;
                            case 2:
                                if (endDamageList != null) {
                                    if (damageFlag) {
                                        damageFlag = false;
                                        endDamageList.add(damagePointIndex, path);
                                    } else {
                                        endDamageList.set(damagePointIndex, path);
                                    }
                                    damageAdapter = new CameraPhotoAdapter(this, endDamageList, 2, 4);
                                    end_car_damage_gv.setAdapter(damageAdapter);
                                }
                                break;
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            doFinish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void doFinish() {
        if (endNormalList == null || endNormalList.isEmpty() || (endNormalList != null && (endDamageList == null || endDamageList.isEmpty()))) {
            finish();
        } else {
            if (1 == select)
                new AlertDialog.Builder(this).setTitle(getString(R.string.dialog_title_tip)).setMessage(getString(R.string.dialog_content_tip)).setNegativeButton(getString(R.string.dialog_cancel_tip), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton(getString(R.string.dialog_ensure_tip), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create().show();
            else
                finish();
        }
    }
}
