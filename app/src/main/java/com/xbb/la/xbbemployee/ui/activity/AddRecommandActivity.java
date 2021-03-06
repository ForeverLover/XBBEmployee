package com.xbb.la.xbbemployee.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.xbb.la.modellibrary.bean.DIYProduct;
import com.xbb.la.modellibrary.bean.Recommand;
import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.modellibrary.utils.MLog;
import com.xbb.la.modellibrary.utils.StringUtil;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.adapter.CameraPhotoAdapter;
import com.xbb.la.xbbemployee.adapter.DIYProductAdapter;
import com.xbb.la.xbbemployee.config.BaseActivity;
import com.xbb.la.xbbemployee.config.TitleActivity;
import com.xbb.la.xbbemployee.listener.AnimateFirstDisplayListener;
import com.xbb.la.xbbemployee.provider.DBHelperMethod;
import com.xbb.la.xbbemployee.utils.MImageLoader;
import com.xbb.la.xbbemployee.widget.CustomGridView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/11 11:30
 * 描述：添加建议页面
 */

public class AddRecommandActivity extends TitleActivity {
    @ViewInject(R.id.selected_diy_img)
    private ImageView selected_diy_img;
    @ViewInject(R.id.select_diy_name)
    private TextView select_diy_name;
    @ViewInject(R.id.diy_group_gv)
    private CustomGridView diy_group_gv;
    @ViewInject(R.id.diy_describe_gv)
    private CustomGridView diy_describe_gv;
    @ViewInject(R.id.add_remark_et)
    private EditText add_remark_et;
    @ViewInject(R.id.add_remark_textNum)
    private TextView add_remark_textNum;

    @ViewInject(R.id.edit_btn)
    private TextView edit_btn;
    @ViewInject(R.id.preview_btn)
    private TextView preview_btn;

    @ViewInject(R.id.add_ensure_btn)
    private Button add_ensure_btn;

    private List<DIYProduct> diyProductList;
    private DIYProductAdapter diyProductAdapter;

    private final int ADD_PHOTO = 1001;
    private boolean flag = false;
    private boolean isFinished;
    private int pointIndex;

    private List<String> introAlbum;

    private DIYProduct selectedDIY;
    private List<String> lastAlbum;

    private String remark;

    private String path /*= "file:///android_asset/mate_add_pics"*/;
    private CameraPhotoAdapter introAdapter;

    private int textSum = 150;

    private AlertDialog exitDialog;

    private boolean editable;

    private Recommand sourceRecommand;

    private Recommand recommand;

    /**
     * 1-添加建议
     * 2-修改建议
     */
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recommand);
    }

    @Override
    public void initData() {
        type = getIntent().getIntExtra("type", 1);
        diyProductList = DBHelperMethod.getInstance().getLocalDIYProductList();
        path = Constant.Path.ADD_PIC_Path;
        editable = true;
        if (diyProductList != null && !diyProductList.isEmpty()) {
            diyProductAdapter = new DIYProductAdapter(this, diyProductList);
            diy_group_gv.setAdapter(diyProductAdapter);
        }
        if (2 == type) {
            setTitle(getString(R.string.recommand_update_lable));
            sourceRecommand = (Recommand) getIntent().getSerializableExtra("recommand");
            if (sourceRecommand != null) {
                selectedDIY = sourceRecommand.getSelectedDIYProduct();
                remark = sourceRecommand.getRemark();
                select_diy_name.setText(selectedDIY.getP_name());
                add_remark_et.setText(remark);
                if (introAlbum == null)
                    introAlbum = new ArrayList<String>();
                introAlbum.addAll(sourceRecommand.getIntroAlbum());
                if (introAlbum.isEmpty() || introAlbum.size() < 3) {
                    introAlbum.add(path);
                    isFinished = false;
                } else
                    isFinished = true;
                for (DIYProduct diyProduct : diyProductList) {
                    if (selectedDIY.getId().equals(diyProduct.getId()))
                        diyProduct.setSelected(true);
                }
                diyProductAdapter.update(true);
                MImageLoader.getInstance(this).displayImageByHalfUrl(selectedDIY.getP_ximg(), selected_diy_img, R.mipmap.diy_img_error, new AnimateFirstDisplayListener());
            }
        } else {
            setTitle(getString(R.string.recommand_add_lable));
            introAlbum = new ArrayList<String>();
            if (!StringUtil.isEmpty(path)) {
                introAlbum.add(path);
            }
        }

        if (introAlbum != null && !introAlbum.isEmpty()) {
            introAdapter = new CameraPhotoAdapter(this, introAlbum, 2, 3);
            diy_describe_gv.setAdapter(introAdapter);
        }
        diy_describe_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pointIndex = position;
                if (introAlbum != null && introAlbum.size() < 3) {
                    if (position == introAlbum.size() - 1) {
                        flag = true;
                    } else
                        flag = false;
                } else if (introAlbum != null && introAlbum.size() == 3 && !isFinished) {
                    if (position == introAlbum.size() - 1) {
                        flag = true;
                    } else
                        flag = false;
                } else {
                    flag = false;
                }
                Intent intent = new Intent(AddRecommandActivity.this, SelectPictureActivity.class);
                intent.putExtra("type", 2);
                startActivityForResult(intent, ADD_PHOTO);
            }
        });
        diy_group_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DIYProduct target = diyProductList.get(position);
                if (selectedDIY == null) {
                    select_diy_name.setText(target.getP_name());
                    target.setSelected(true);
                    selectedDIY = target;
                    diyProductAdapter.update(true);
                    MImageLoader.getInstance(AddRecommandActivity.this).displayImageByHalfUrl(target.getP_ximg(), selected_diy_img, R.mipmap.diy_img_error, new AnimateFirstDisplayListener());
                } else {
                    if (selectedDIY.getId().equals(target.getId())) {
                        target.setSelected(false);
                        selectedDIY = null;
                        select_diy_name.setText("");
                        selected_diy_img.setImageResource(R.mipmap.no_diy_selected);
                        diyProductAdapter.update(false);
                    }
                }
            }
        });

        add_remark_et.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            public void afterTextChanged(Editable s) {
                int number = s.length();
                add_remark_textNum.setText("" + number);
                selectionStart = add_remark_et.getSelectionStart();
                selectionEnd = add_remark_et.getSelectionEnd();
                if (temp.length() > textSum) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    add_remark_et.setText(s);
                    add_remark_et.setSelection(tempSelection);//设置光标在最后
//                    showToast(getString(R.string.feedback_content_long));
                }
            }
        });
        add_ensure_btn.setOnClickListener(this);
        edit_btn.setOnClickListener(this);
        preview_btn.setOnClickListener(this);

    }


    @Override
    protected void onClickTitleLeft(View v) {
        doFinish();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_ensure_btn:
                if (selectedDIY == null) {
                    showToast(getString(R.string.recommand_diy_null));
                    return;
                }
                MLog.v("Tag", "judge diy");
                if (!isFinished) {
                    if (introAlbum.size() < 2) {
                        return;
                    }
                    introAlbum.remove(introAlbum.size() - 1);
//                    lastAlbum = introAlbum.subList(0, introAlbum.size() - 1);
                }
                MLog.v("Tag", "init album succeed");
                if (lastAlbum == null)
                    lastAlbum = new ArrayList<>();
                lastAlbum.addAll(introAlbum);
                remark = add_remark_et.getText() != null ? add_remark_et.getText().toString() : "";
                if (recommand == null)
                    recommand = new Recommand();
                recommand.setIntroAlbum(lastAlbum);
                recommand.setRemark(remark);
                recommand.setSelectedDIYProduct(selectedDIY);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("recommand", recommand);
                if (2 == type)
                    returnIntent.putExtra("change", hasChangedRecommand());
                setResult(RESULT_OK, returnIntent);
                hideSoftInputView();
                finish();
                MLog.v("Tag", "recommand succeed");
                break;
            case R.id.preview_btn:
                showAlbum(introAlbum);

                break;
            case R.id.edit_btn:
                if (introAdapter != null) {
                    introAdapter.editAlbum(editable);
                    if (editable) {
                        edit_btn.setText(getString(R.string.label_edit_finish));
                    } else {
                        edit_btn.setText(getString(R.string.label_edit));
                        introAlbum = introAdapter.getDataSet();
                    }
                    editable = !editable;
                }
                MLog.v("Tag", "isfinished" + isFinished + " size:" + introAlbum.size());
                if (isFinished && introAlbum.size() < 3) {
                    isFinished = false;
                    introAlbum.add(path);
                }
                break;
        }
    }

    private void showAlbum(List<String> dataList) {

        ArrayList<String> picList = new ArrayList<String>();
        if (dataList.contains(Constant.Path.ADD_PIC_Path) && dataList.size() > 0)
            picList.addAll(dataList.subList(0, dataList.size() - 1));
        else
            picList.addAll(dataList);
        if (picList == null || picList.isEmpty()) {
            showToast(getString(R.string.tost_no_pic));
            return;
        }
        Intent intent = new Intent(this,
                BigImageActivity.class);

        intent.putStringArrayListExtra("img_pics", picList);
        intent.putExtra("pos", 0);
        startActivity(intent);

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
                        if (introAlbum != null) {
                            if (flag) {
                                flag = false;
                                switch (introAlbum.size() - 1) {
                                    case 0:
                                        introAlbum.add(0, path);
                                        break;
                                    case 1:
                                        introAlbum.add(1, path);
                                        break;
                                    case 2:
                                        introAlbum.add(2, path);
                                        introAlbum.remove(3);
                                        isFinished = true;
                                        break;
                                    default:
                                        showToast(getString(R.string.car_too_more));
                                        break;
                                }
                            } else {
                                switch (pointIndex) {
                                    case 0:
                                        introAlbum.set(0, path);
                                        break;
                                    case 1:
                                        introAlbum.set(1, path);
                                        break;
                                    case 2:
                                        introAlbum.set(2, path);
                                        break;
                                }
                            }
                            introAdapter = new CameraPhotoAdapter(this, introAlbum, 2, 3);
                            diy_describe_gv.setAdapter(introAdapter);
                        }
                        break;
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
        if (selectedDIY == null && (!isFinished && introAlbum.size() == 1) && StringUtil.isEmpty(remark)) {
            hideSoftInputView();
            finish();
        } else {
            new AlertDialog.Builder(this).setTitle(getString(R.string.dialog_title_tip)).setMessage(getString(R.string.dialog_content_tip)).setNegativeButton(getString(R.string.dialog_cancel_tip), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setPositiveButton(getString(R.string.dialog_ensure_tip), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    hideSoftInputView();
                }
            }).create().show();
        }
    }

    private boolean hasChangedRecommand() {
        if (sourceRecommand == null || recommand == null)
            return false;
        if (!sourceRecommand.getSelectedDIYProduct().getId().equals(recommand.getSelectedDIYProduct().getId()) ||
                !StringUtil.compareStringList(sourceRecommand.getIntroAlbum(), recommand.getIntroAlbum()) || !sourceRecommand.getRemark().equals(recommand.getRemark())) {
            return true;
        }
        return false;
    }
}
