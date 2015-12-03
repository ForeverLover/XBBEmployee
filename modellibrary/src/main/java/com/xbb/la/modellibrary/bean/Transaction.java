package com.xbb.la.modellibrary.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/9 16:31
 * 描述：业务
 */

public class Transaction implements Serializable{
    private String orderId;
    private String employeeId;
    private String normalAlbumBefore;
    private String damageAlbumBefore;
    private String normalAlbumAfter;
    private String damageAlbumAfter;
    private String recommand;
    private String suggestion;
    private String startTime;
    private String endTime;

    private String recommandId;
    private DIYProduct selectedDIYProduct;
    private List<String> introAlbum;
    private String remark;

    private int currentStep;

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }

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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getNormalAlbumBefore() {
        return normalAlbumBefore;
    }

    public void setNormalAlbumBefore(String normalAlbumBefore) {
        this.normalAlbumBefore = normalAlbumBefore;
    }

    public String getDamageAlbumBefore() {
        return damageAlbumBefore;
    }

    public void setDamageAlbumBefore(String damageAlbumBefore) {
        this.damageAlbumBefore = damageAlbumBefore;
    }

    public String getNormalAlbumAfter() {
        return normalAlbumAfter;
    }

    public void setNormalAlbumAfter(String normalAlbumAfter) {
        this.normalAlbumAfter = normalAlbumAfter;
    }

    public String getDamageAlbumAfter() {
        return damageAlbumAfter;
    }

    public void setDamageAlbumAfter(String damageAlbumAfter) {
        this.damageAlbumAfter = damageAlbumAfter;
    }

    public String getRecommand() {
        return recommand;
    }

    public void setRecommand(String recommand) {
        this.recommand = recommand;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }
}
