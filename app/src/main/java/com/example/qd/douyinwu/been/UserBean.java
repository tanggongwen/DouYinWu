package com.example.qd.douyinwu.been;

import java.io.Serializable;

public class UserBean implements Serializable {

    /**
     * user_id : 38
     * account_id : 15858400978
     * mobile : 13047489332
     * address_id : 0
     * reg_time : 0
     * head_pic : http://slivepic.sdyilian.top/pic/1edaae6f216f9e5481a27bbae7c16cac3229.jpg
     * level : 0
     * is_lock : 0
     * nick_name : 萝卜蹲123
     * like_nums : 0
     * like_host : 0
     * store_id : 0
     */

    private String user_id;
    private String account_id;
    private String mobile;
    private String address_id;
    private String reg_time;
    private String head_pic;
    private String level;
    private String is_lock;
    private String nick_name;
    private String like_nums;
    private String like_host;
    private int store_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getReg_time() {
        return reg_time;
    }

    public void setReg_time(String reg_time) {
        this.reg_time = reg_time;
    }

    public String getHead_pic() {
        return head_pic;
    }

    public void setHead_pic(String head_pic) {
        this.head_pic = head_pic;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIs_lock() {
        return is_lock;
    }

    public void setIs_lock(String is_lock) {
        this.is_lock = is_lock;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getLike_nums() {
        return like_nums;
    }

    public void setLike_nums(String like_nums) {
        this.like_nums = like_nums;
    }

    public String getLike_host() {
        return like_host;
    }

    public void setLike_host(String like_host) {
        this.like_host = like_host;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }
}
