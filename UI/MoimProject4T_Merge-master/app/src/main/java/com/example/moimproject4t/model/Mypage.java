package com.example.moimproject4t.model;

import java.io.Serializable;

public class Mypage implements Serializable {
    private int moimcode; //  number primary key,    -- 모임 코드
    private String loca; //  varchar2(100),             -- 위치
    private String moimname; //  varchar2(1000),        -- 모임명
    private String prod; //  varchar2(4000),            -- 모임소개
    private String pic; //  varchar2(500),              -- 사진
    private String color; //  varchar2(100)             -- 색상



    @Override
    public String toString() {
        return "Mypage{" +
                "moimcode=" + moimcode +
                ", loca='" + loca + '\'' +
                ", moimname='" + moimname + '\'' +
                ", prod='" + prod + '\'' +
                ", pic='" + pic + '\'' +
                ", color='" + color + '\'' +
                '}';
    }


    public Mypage(int moimcode, String loca, String moimname, String prod, String pic, String color) {
        this.moimcode = moimcode;
        this.loca = loca;
        this.moimname = moimname;
        this.prod = prod;
        this.pic = pic;
        this.color = color;
    }

    public Mypage() {
    }

    public int getMoimcode() {
        return moimcode;
    }

    public void setMoimcode(int moimcode) {
        this.moimcode = moimcode;
    }

    public String getLoca() {
        return loca;
    }

    public void setLoca(String loca) {
        this.loca = loca;
    }

    public String getMoimname() {
        return moimname;
    }

    public void setMoimname(String moimname) {
        this.moimname = moimname;
    }

    public String getProd() {
        return prod;
    }

    public void setProd(String prod) {
        this.prod = prod;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
