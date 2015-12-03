package com.xbb.la.modellibrary.bean;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/17 11:35
 * 描述：上传任务数据元
 */

public class UploadTransaction implements Serializable{
    private String employeeId;
    private String orderId;
    /**
     * 洗车前车身照（4张）
     */
    private List<String> startNormalPathList;
    private List<File> startNormalFileAlbum;
    /**
     * 洗车前划痕照（n张）
     */
    private List<String> startDamagePathList;
    private List<File> StartDamageFileAlbum;
    /**
     * 洗车后划痕照（n张）
     */
    private List<String> endDamagePathList;
    private List<File> endDamageFileAlbum;
    /**
     * 洗车后车身照（n张）
     */
    private List<String> endNormalPathList;
    private List<File> endNormalFileAlbum;
    /**
     * 建议列表
     */
    private List<UploadRecommand> uploadRecommandList;
    /**
     * 开始作业时间
     */
    private String startTime;
    /**
     * 结束作业时间
     */
    private String endTime;

    public List<File> getStartNormalFileAlbum() {
        return startNormalFileAlbum;
    }

    public void setStartNormalFileAlbum(List<File> startNormalFileAlbum) {
        this.startNormalFileAlbum = startNormalFileAlbum;
    }

    public List<File> getStartDamageFileAlbum() {
        return StartDamageFileAlbum;
    }

    public void setStartDamageFileAlbum(List<File> startDamageFileAlbum) {
        StartDamageFileAlbum = startDamageFileAlbum;
    }

    public List<File> getEndDamageFileAlbum() {
        return endDamageFileAlbum;
    }

    public void setEndDamageFileAlbum(List<File> endDamageFileAlbum) {
        this.endDamageFileAlbum = endDamageFileAlbum;
    }

    public List<File> getEndNormalFileAlbum() {
        return endNormalFileAlbum;
    }

    public void setEndNormalFileAlbum(List<File> endNormalFileAlbum) {
        this.endNormalFileAlbum = endNormalFileAlbum;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<String> getStartNormalPathList() {
        return startNormalPathList;
    }

    public void setStartNormalPathList(List<String> startNormalPathList) {
        this.startNormalPathList = startNormalPathList;
    }

    public List<String> getStartDamagePathList() {
        return startDamagePathList;
    }

    public void setStartDamagePathList(List<String> startDamagePathList) {
        this.startDamagePathList = startDamagePathList;
    }

    public List<String> getEndDamagePathList() {
        return endDamagePathList;
    }

    public void setEndDamagePathList(List<String> endDamagePathList) {
        this.endDamagePathList = endDamagePathList;
    }

    public List<String> getEndNormalPathList() {
        return endNormalPathList;
    }

    public void setEndNormalPathList(List<String> endNormalPathList) {
        this.endNormalPathList = endNormalPathList;
    }

    public List<UploadRecommand> getUploadRecommandList() {
        return uploadRecommandList;
    }

    public void setUploadRecommandList(List<UploadRecommand> uploadRecommandList) {
        this.uploadRecommandList = uploadRecommandList;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
