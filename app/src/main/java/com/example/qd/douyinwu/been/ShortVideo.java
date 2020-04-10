package com.example.qd.douyinwu.been;

public class ShortVideo {
    private String s_id;//	视频id
    private String user_id;//	所属用户主键
    private String nick_name;//	发布视频者昵称
    private String s_like_num;//点赞数量
    private String s_title;//	视频标题
    private String s_desc;//视频简介
    private String s_comments_num;//	评论数量
    private String s_play_num;//	播放数量
    private String s_video_url;//	视频链接
    private String s_time;//	视频发布时间
    private String s_pic_url;//	视频封面链接
    private String goods_id;//没有商品为0, 有商品是为商品主键
    private String goods_name;//商品标题
    private String original_img1;
    private String market_price;
    private String s_share;
    private String head_pic;//	发布者头像链接
    private int is_like_host;//	是否关注当前主播, 0:没关注, 1:已关注
    private int is_like_video;//是否点赞当前视频, 0:没点赞 1: 已点赞

    public void setIs_like_host(int is_like_host) {
        this.is_like_host = is_like_host;
    }

    public int getIs_like_host() {
        return is_like_host;
    }

    public void setIs_like_video(int is_like_video) {
        this.is_like_video = is_like_video;
    }

    public int getIs_like_video() {
        return is_like_video;
    }

    public void setHead_pic(String head_pic) {
        this.head_pic = head_pic;
    }

    public String getHead_pic() {
        return head_pic;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setS_pic_url(String s_pic_url) {
        this.s_pic_url = s_pic_url;
    }

    public String getS_pic_url() {
        return s_pic_url;
    }

    public void setS_time(String s_time) {
        this.s_time = s_time;
    }

    public String getS_time() {
        return s_time;
    }

    public void setS_video_url(String s_video_url) {
        this.s_video_url = s_video_url;
    }

    public String getS_video_url() {
        return s_video_url;
    }

    public void setS_play_num(String s_play_num) {
        this.s_play_num = s_play_num;
    }

    public String getS_play_num() {
        return s_play_num;
    }

    public void setS_comments_num(String s_comments_num) {
        this.s_comments_num = s_comments_num;
    }

    public String getS_comments_num() {
        return s_comments_num;
    }

    public void setS_desc(String s_desc) {
        this.s_desc = s_desc;
    }

    public String getS_desc() {
        return s_desc;
    }

    public void setS_title(String s_title) {
        this.s_title = s_title;
    }

    public String getS_title() {
        return s_title;
    }

    public void setS_like_num(String s_like_num) {
        this.s_like_num = s_like_num;
    }

    public String getS_like_num() {
        return s_like_num;
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

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    public String getS_id() {
        return s_id;
    }

    public String getOriginal_img1() {
        return original_img1;
    }

    public void setOriginal_img1(String original_img1) {
        this.original_img1 = original_img1;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getS_share() {
        return s_share;
    }

    public void setS_share(String s_share) {
        this.s_share = s_share;
    }
}
