package com.xbb.la.modellibrary.bean;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/17 11:37
 * 描述：上传建议
 */

public class UploadRecommand implements Serializable{
    /**
     * 推荐diy产品id
     */
    private String diyID;
    /**
     * 说明图片（1~3张）
     */
    private List<String> recommandPathList;
    private List<File> recommandFileAlbum;
    /**
     * 备注说明
     */
    private String recommandRemark;

    public List<File> getRecommandFileAlbum() {
        return recommandFileAlbum;
    }

    public void setRecommandFileAlbum(List<File> recommandFileAlbum) {
        this.recommandFileAlbum = recommandFileAlbum;
    }

    public String getDiyID() {
        return diyID;
    }

    public void setDiyID(String diyID) {
        this.diyID = diyID;
    }

    public List<String> getRecommandPathList() {
        return recommandPathList;
    }

    public void setRecommandPathList(List<String> recommandPathList) {
        this.recommandPathList = recommandPathList;
    }

    public String getRecommandRemark() {
        return recommandRemark;
    }

    public void setRecommandRemark(String recommandRemark) {
        this.recommandRemark = recommandRemark;
    }
}
