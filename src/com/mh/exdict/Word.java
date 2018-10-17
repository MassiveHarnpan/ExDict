package com.mh.exdict;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Word {

    //private int id;
    private String text = "";
    private List<Desc> descs = new ArrayList<>(2);

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Desc getDesc(int index) {
        return descs.get(index);
    }

    public int descCount() {
        return descs.size();
    }

    public void addDesc(Desc desc) {
        descs.add(desc);
    }

    public void delDesc(Desc desc) {
        descs.remove(desc);
    }

    public Desc delDesc(int index) {
        return descs.remove(index);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(text);
        if (descs.size()!=0)
            builder.append(" - ");
        Iterator<Desc> itr = descs.iterator();
        while (itr.hasNext()) {
            builder.append(itr.next().toString());
            if (itr.hasNext()) {
                builder.append(' ');
            }
        }
        return builder.toString();
    }
}
