package com.tju.consultationPlatform.domain;


public class JsonResult {
    private int status;
    private String message;
    private Object content;

    public JsonResult() {
    }

    ;

    public JsonResult(int status) {
        this.status = status;
    }

    public JsonResult(int status, String message, Object content) {
        this.status = status;
        this.message = message;
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Object getContent() {
        return content;
    }


}
