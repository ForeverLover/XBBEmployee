package com.xbb.la.modellibrary.bean;

import java.io.Serializable;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/11 11:01
 * 描述：diy产品信息
 */

public class DIYProduct implements Serializable{
    private String id;
    private String p_ximg;
    private String p_wimg;
    private String p_name;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getP_ximg() {
        return p_ximg;
    }

    public void setP_ximg(String p_ximg) {
        this.p_ximg = p_ximg;
    }

    public String getP_wimg() {
        return p_wimg;
    }

    public void setP_wimg(String p_wimg) {
        this.p_wimg = p_wimg;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }
}
