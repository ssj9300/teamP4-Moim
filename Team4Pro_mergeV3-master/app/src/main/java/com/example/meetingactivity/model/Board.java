package com.example.meetingactivity.model;

import java.io.Serializable;

public class Board implements Serializable {
    private int listnum;          //게시글 번호
    private String id;             //작성자 아이디
    private int moimcode;         //글이 작성된 모임 코드
    private String subject;       //제목
    private String content;       //내용
    private String filename;      //저장된 사진 이름
    private String thumb;         //사진의 썸네일 이름
    private String editdate;     //작성/수정된 날짜
    private int lev;             //공지 등의 분류 번호(레벨)

    public Board() {
    }

    public Board(int listnum, String id, int moimcode, String subject, String content, String filename, String thumb, String editdate, int lev) {
        this.listnum = listnum;
        this.id = id;
        this.moimcode = moimcode;
        this.subject = subject;
        this.content = content;
        this.filename = filename;
        this.thumb = thumb;
        this.editdate = editdate;
        this.lev = lev;
    }

    public int getListnum() {
        return listnum;
    }

    public void setListnum(int listnum) {
        this.listnum = listnum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMoimcode() {
        return moimcode;
    }

    public void setMoimcode(int moimcode) {
        this.moimcode = moimcode;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getEditdate() {
        return editdate;
    }

    public void setEditdate(String editdate) {
        this.editdate = editdate;
    }

    public int getLev() {
        return lev;
    }

    public void setLev(int lev) {
        this.lev = lev;
    }
}
