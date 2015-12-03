package com.xbb.la.modellibrary.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/11 15:46
 * 描述：建议
 */

public class Recommand implements Serializable {
    private String recommandId;
    private DIYProduct selectedDIYProduct;
    private List<String> introAlbum;
    private String remark;

    public String getRecommandId() {
        return recommandId;
    }

    public void setRecommandId(String recommandId) {
        this.recommandId = recommandId;
    }

    public DIYProduct getSelectedDIYProduct() {
        return selectedDIYProduct;
    }

    public void setSelectedDIYProduct(DIYProduct selectedDIYProduct) {
        this.selectedDIYProduct = selectedDIYProduct;
    }

    public List<String> getIntroAlbum() {
        return introAlbum;
    }

    public void setIntroAlbum(List<String> introAlbum) {
        this.introAlbum = introAlbum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
