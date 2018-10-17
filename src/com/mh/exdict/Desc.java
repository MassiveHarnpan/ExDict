package com.mh.exdict;


//对词条的解释
public class Desc {

    //private int id;
    private String part = "";
    private String content = "";

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        if (part == null) {
            part = "";
        }
        this.part = part;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if (content==null) {
            content = "";
        }
        this.content = content;
    }

    @Override
    public String toString() {
        return part + ". " + content;
    }
}
