package com.xbb.la.modellibrary.bean;

import java.io.Serializable;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/12 11:30
 * 描述：温馨提示
 */

public class Reminder implements Serializable{
    private String id;
    private String reminderText;
    private String thumb;
    private String title;
    private boolean select;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReminderText() {
        return reminderText;
    }

    public void setReminderText(String reminderText) {
        this.reminderText = reminderText;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
