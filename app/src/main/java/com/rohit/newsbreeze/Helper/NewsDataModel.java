package com.rohit.newsbreeze.Helper;

public class NewsDataModel {
    private String Image, HeadLine, Description, Date, Author, Source, Content;
    private int Pos;

    public NewsDataModel(String image, String headLine, String description, String date, String author, String source, String content, int pos) {
        Image = image;
        HeadLine = headLine;
        Description = description;
        Date = date;
        Author = author;
        Source = source;
        Content = content;
        Pos = pos;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getHeadLine() {
        return HeadLine;
    }

    public void setHeadLine(String headLine) {
        HeadLine = headLine;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getSource() {
        return Source;
    }

    public void setSource(String source) {
        Source = source;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getPos() {
        return Pos;
    }

    public void setPos(int pos) {
        Pos = pos;
    }
}
