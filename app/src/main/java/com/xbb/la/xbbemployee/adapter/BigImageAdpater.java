package com.xbb.la.xbbemployee.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.modellibrary.utils.StringUtil;
import com.xbb.la.xbbemployee.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/27 17:24
 * 描述：
 */

public class BigImageAdpater extends PagerAdapter {

    private Activity mContext;

    private ArrayList<String> pathList = null;

    private ImageLoader imgload = null;


    public BigImageAdpater(Activity context, ArrayList<String> pathList) {
        this.mContext = context;
        imgload = ImageLoader.getInstance();
        this.pathList = pathList;
    }

    public void setPathList(ArrayList<String> pathList) {
        this.pathList = pathList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return pathList == null ? 0 : pathList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View image = LayoutInflater.from(mContext).inflate(
                R.layout.item_pager_image, view, false);
        assert image != null;
        PhotoView img = (PhotoView) image.findViewById(R.id.image);
        img.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View view, float x, float y) {
                // TODO Auto-generated method stub
                mContext.finish();
            }
        });

        final ProgressBar spinner = (ProgressBar) image
                .findViewById(R.id.loading);
        String path = pathList.get(position);
        spinner.setVisibility(View.VISIBLE);
        if (!StringUtil.isEmpty(path)) {
            InputStream is = null;
            try {
                if (Constant.Path.ADD_PIC_Path.equals(path))
                    is = mContext.getClass().getClassLoader().getResourceAsStream("assets/mate_add_pics.png");
                else
                    is = new FileInputStream(path);
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inTempStorage = new byte[100 * 1024];

                opts.inPreferredConfig = Bitmap.Config.RGB_565;

                opts.inPurgeable = true;

                opts.inSampleSize = 4;

                opts.inInputShareable = true;
                Bitmap bitmap = BitmapFactory.decodeStream(is, null, opts);
                if (bitmap != null) {
                    img.setImageBitmap(bitmap);
                    spinner.setVisibility(View.GONE);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        view.addView(image, 0);
        return image;
    }
}
