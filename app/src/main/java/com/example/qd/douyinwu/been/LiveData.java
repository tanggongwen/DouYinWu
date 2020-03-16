package com.example.qd.douyinwu.been;

public class LiveData {
    private String id;//直播间主键
    private String status;//直播状态 0未在直播 1 直播中 (不需要考虑该参数)
    private String room;//房间号
    private String cover;//直播封面图路径
    private String title;//直播标题
    private String isrecommend;//是否为推荐直播, 0否1是
    private String is_like_host;//	是否已关注当前直播 0否1是
    private String head_pic;//直播主播头像
    private String nick_name;//直播主播昵称
    private String level;//1主播2商家
    private String play_url;//直播链接

    public void setPlay_url(String play_url) {
        this.play_url = play_url;
    }

    public String getPlay_url() {
        return play_url;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }

    public void setHead_pic(String head_pic) {
        this.head_pic = head_pic;
    }

    public String getHead_pic() {
        return head_pic;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public void setIsrecommend(String isrecommend) {
        this.isrecommend = isrecommend;
    }

    public String getIsrecommend() {
        return isrecommend;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCover() {
        return cover;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getRoom() {
        return room;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIs_like_host(String is_like_host) {
        this.is_like_host = is_like_host;
    }

    public String getIs_like_host() {
        return is_like_host;
    }

    public String getId() {
        return id;
    }
}
