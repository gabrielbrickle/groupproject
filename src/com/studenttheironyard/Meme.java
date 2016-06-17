package com.studenttheironyard;

import java.util.ArrayList;

/**
 * Created by hoseasandstrom on 6/16/16.
 */
public class Meme {
    Integer memeId;
    String url;
    Integer upVote;
    Integer downVote;
    ArrayList<Comment> memeComments = new ArrayList<>();

    public Meme(Integer memeId, String memeName, Integer upVote, Integer downVote) {
        this.memeId = memeId;
        this.url = memeName;
        this.upVote = upVote;
        this.downVote = downVote;
    }

    public Meme() {
    }

    public Integer getMemeId() {
        return memeId;
    }

    public void setMemeId(Integer memeId) {
        this.memeId = memeId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getUpVote() {
        return upVote;
    }

    public void setUpVote(Integer upVote) {
        this.upVote = upVote;
    }

    public Integer getDownVote() {
        return downVote;
    }

    public void setDownVote(Integer downVote) {
        this.downVote = downVote;
    }
}
