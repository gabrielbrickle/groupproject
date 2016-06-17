package com.studenttheironyard;

/**
 * Created by hoseasandstrom on 6/16/16.
 */
public class Comment {
    Integer commentId;
    String author;
    String text;

    public Comment(Integer commentId, String author, String text) {
        this.commentId = commentId;
        this.author = author;
        this.text = text;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
