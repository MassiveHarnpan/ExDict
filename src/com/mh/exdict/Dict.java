package com.mh.exdict;

import java.util.*;

//词典主类
public class Dict {


    private String name= "";
    private String intro = "";

    private List<Word> words = new ArrayList<>();



    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name==null)
            name = "";
        this.name = name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        if (intro==null)
            intro = "";
        this.intro = intro;
    }


    public Word getWord(int index) {
        return words.get(index);
    }

    public int wordCount() {
        return words.size();
    }

    public boolean addWord(Word word) {
        return words.add(word);
    }

    public boolean delWord(Word word) {
        return words.remove(word);
    }

    public Word delWord(int index) {
        return words.remove(index);
    }



    public List<Word> search(String query, boolean searchWord, boolean searchDescContent, boolean searchDescPart) {
        List<Word> result = new ArrayList<>();
        if (!(searchWord && searchDescContent && searchDescPart)) {
            return result;
        }
        for (int i = 0; i < words.size(); i++) {
            Word word = words.get(i);
            if (searchDescContent && word.getText().contains(query)) {
                result.add(word);
                continue;
            }
            for (int j = 0; j < word.descCount(); j++) {
                Desc desc = word.getDesc(i);
                if (searchDescContent && desc.getContent().contains(query)) {
                    result.add(word);
                    break;
                }
                if (searchDescPart && desc.getPart().contains(query)) {
                    result.add(word);
                    break;
                }
            }
        }
        return result;
    }



}
