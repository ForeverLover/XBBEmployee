package com.xbb.la.modellibrary.bean;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/6 11:50
 * 描述：返回数据格式
 */

public class ResponseJson {
    /**
     * 返回成功标志
     * true/false
     */
    private int  code;
    /**
     * 返回提示信息
     */
    private String msg;
    /**
     * 返回数据
     */
    private Object result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
