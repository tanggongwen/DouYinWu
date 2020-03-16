package com.example.qd.douyinwu.been;

public class MainComment {
    private String c_id;//评论主键
    private String c_content;
    private String c_is_author;//是否作者, 1:作者回复 2:非作者回复
    private int c_like_nums;
    private String c_reply_num;
    private String s_id;
    private String c_time;
    private String user_id;//	发表评论用户主键
    private String nick_name;
    private String head_pic;//发表评论用户头像
    private int is_like;//1用户点过赞 2未点赞

    public void setIs_like(int is_like) {
        this.is_like = is_like;
    }

    public int getIs_like() {
        return is_like;
    }

    public void setHead_pic(String head_pic) {
        this.head_pic = head_pic;
    }

    public String getHead_pic() {
        return head_pic;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setC_time(String c_time) {
        this.c_time = c_time;
    }

    public String getC_time() {
        return c_time;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    public String getS_id() {
        return s_id;
    }

    public void setC_reply_num(String c_reply_num) {
        this.c_reply_num = c_reply_num;
    }

    public String getC_reply_num() {
        return c_reply_num;
    }

    public void setC_like_nums(int c_like_nums) {
        this.c_like_nums = c_like_nums;
    }

    public int getC_like_nums() {
        return c_like_nums;
    }

    public void setC_is_author(String c_is_author) {
        this.c_is_author = c_is_author;
    }

    public String getC_is_author() {
        return c_is_author;
    }

    public void setC_content(String c_content) {
        this.c_content = c_content;
    }

    public String getC_content() {
        return c_content;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getC_id() {
        return c_id;
    }
}
