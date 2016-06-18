package com.studenttheironyard;

/**
 * Created by hoseasandstrom on 6/16/16.
 */
public class Comment {
    Integer id;
    String author;
    String text;

    public Comment(Integer id, String author, String text) {
        this.id = id;
        this.author = author;
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
