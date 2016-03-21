package com.nano.web.vo;

/**
 * Created by Administrator on 2015/5/24.
 */
public class ResponseVO<T> {
    private Boolean yesOrNo;
    private T responseObject;
    private String errorMsg;

    public Boolean getYesOrNo() {
        return yesOrNo;
    }

    public void setYesOrNo(Boolean yesOrNo) {
        this.yesOrNo = yesOrNo;
    }

    public T getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(T responseObject) {
        this.responseObject = responseObject;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
