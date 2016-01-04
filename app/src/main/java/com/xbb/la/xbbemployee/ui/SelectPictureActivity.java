package com.xbb.la.xbbemployee.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.modellibrary.utils.StringUtil;
import com.xbb.la.xbbemployee.R;
import com.xbb.la.xbbemployee.config.BaseActivity;
import com.xbb.la.modellibrary.utils.SystemUtil;

import java.io.File;
import java.util.Date;

public class SelectPictureActivity extends BaseActivity {
    /***
     * 使用照相机拍照获取图片
     */
    private static final int SELECT_PIC_BY_TACK_PHOTO = 1;
    /***
     * 使用相册中的图片
     */
    private static final int SELECT_PIC_BY_PICK_PHOTO = 2;
    /***
     * 裁剪图片
     */
    private static final int CROP_PHOTO = 3;

    @ViewInject(R.id.pic_photo_layout)
    private RelativeLayout pic_photo_layout;
    @ViewInject(R.id.take_photo_layout)
    private RelativeLayout take_photo_layout;
    /**
     * 获取到的图片路径
     */
    private String picPath;

    private Intent lastIntent;

    private Uri photoUri;

    private int type;

    private Bitmap bitmap;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_picture);
    }



    @Override
    public void initData() {
        lastIntent = new Intent();
        type = getIntent().getIntExtra("type", 2);
        take_photo_layout.setOnClickListener(this);
        pic_photo_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.pic_photo_layout:
                pickPhoto();
                break;
            case R.id.take_photo_layout:
                takePhoto();
                break;
        }
    }

    /**
     * 拍照获取图片
     */
    private void takePhoto() {

        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            SystemUtil.creatDir2SDCard(Constant.Path.PHOTO_SYS_PATH);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
            filePath = Constant.Path.PHOTO_SYS_PATH + DateFormat.format("yyyy-MM-dd-hh-mm-ss", new Date()) + ".jpg";
            photoUri = Uri.fromFile(new File(filePath));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            File file = new File(Constant.Path.PHOTO_SYS_PATH);
            startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
        } else {
            showToast(getString(R.string.no_sdcard));
        }
    }

    /***
     * 从相册中取图片
     */
    private void pickPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CROP_PHOTO) {
            if (data != null) {
                Bundle extras = data.getExtras();
                lastIntent.putExtras(extras);
                setResult(RESULT_OK, lastIntent);
                finish();
            }
        } else if (resultCode == Activity.RESULT_OK) {
            doPhoto(requestCode, data);
        }
    }

    /**
     * 选择图片后，获取图片的路径
     *
     * @param requestCode
     * @param data
     */
    private void doPhoto(int requestCode, Intent data) {
        if (requestCode == SELECT_PIC_BY_PICK_PHOTO) // 从相册取图片，有些手机有异常情况，请注意
        {
            if (data == null) {
                showToast(getString(R.string.pic_error));
                return;
            }
            photoUri = data.getData();
            if (photoUri == null) {
                showToast(getString(R.string.pic_error));
                return;
            }
        } else if (requestCode == SELECT_PIC_BY_TACK_PHOTO) {


//            Object object = null;
            if (data != null) {
                if (data.getData() != null) {
                    Cursor cursor = this.getContentResolver().query(
                            data.getData(), null, null, null, null);
                    photoUri = data.getData();
                    if (cursor.moveToFirst()) {
                        filePath = cursor.getString(cursor
                                .getColumnIndex("_data"));// 获取绝对路径
                    }
                    cursor.close();
                } else {// 三星 小米(小米手机不会自动存储DCIM... 这点让哥又爱又恨...)
//                    object = (Bitmap) (data.getExtras() == null ? null : data
//                            .getExtras().get("data"));
                }
            }


            // 直接强转报错 这个主要是为了去高宽比例
//            bitmap = object == null ? null : (Bitmap) object;
            filePath = !StringUtil.isEmpty(filePath) ? filePath
                    : getRealFilePath();
        }
        if (photoUri != null) {
            if (type == 2) {
                String[] pojo = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(photoUri, pojo, null, null, null);
                if (cursor == null) {
                    if (!StringUtil.isEmpty(filePath)) {
                        lastIntent.putExtra(Constant.IntentAction.KEY_PHOTO_PATH, filePath);
                        setResult(RESULT_OK, lastIntent);
                        finish();
                    } else {

                        showToast(getString(R.string.no_pic));
                    }
                    return;
                }
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    picPath = cursor.getString(columnIndex);
                }
                if (cursor != null) {
                    cursor.close();
                }
                if (picPath != null && picPath.endsWith(".png") || picPath.endsWith(".PNG") || picPath.endsWith(".jpg") || picPath.endsWith(".JPG")) {
                    lastIntent.putExtra(Constant.IntentAction.KEY_PHOTO_PATH, picPath);
                    setResult(RESULT_OK, lastIntent);
                    finish();
                }
            } else if (type == 1) {
                startPhotoZoom(photoUri);
            }
        }
    }

    /**
     * 获取系统默认存储真实文件路径 两个路径 一个是最后一张所拍图片路径
     * (由于设置了存储路径所以DCIM中会存储两张一模一样的图片，所以在此获取两张图片路径以便于缩放处理后全部删除)
     *
     * @param
     * @return
     */

    protected String getRealFilePath() {
        String filePath = "";
        Cursor cursor = this.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Images.Media.DATE_MODIFIED + " desc");
        Uri mImageUri = null;
        Uri mUri = Uri.parse("content://media/external/images/media");
        if (cursor.moveToNext()) {
            /**
             * _data：文件的绝对路径 Media.DATA='_data'
             */
            int ringtoneID = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            photoUri = Uri.withAppendedPath(mUri, "" + ringtoneID);
            filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        }
        return filePath;
    }

    /**
     * 裁剪图片
     *
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        Log.v("photo", "uri:" + uri);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_PHOTO);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null) {
            bitmap.recycle();
        }
    }
}
