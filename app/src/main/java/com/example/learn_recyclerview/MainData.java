package com.example.learn_recyclerview;

public class MainData {
    private int iv_proflie;
    private String tv_name;
    private String tv_content;


    public MainData(int iv_proflie, String tv_name, String tv_content) {
        this.iv_proflie = iv_proflie;
        this.tv_name = tv_name;
        this.tv_content = tv_content;
    }
    public MainData() {};

    public int getIv_proflie() {
        return iv_proflie;
    }
    public String getTv_name(){
        return tv_name;
    }
    public String getTv_content(){
        return tv_content;
    }

    public void setIv_proflie(int iv_proflie) {
        this.iv_proflie = iv_proflie;
    }

    public void setTv_content(String tv_content) {
        this.tv_content = tv_content;
    }

    public void setTv_name(String tv_name) {
        this.tv_name = tv_name;
    }
}
