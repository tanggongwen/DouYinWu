package com.example.qd.douyinwu.been;

import java.io.Serializable;
import java.util.List;

public class LiveStateBean implements Serializable {


    /**
     * id : 130
     * room : 10111
     * starttime : 已开播1小时58分钟
     * cover : http://q6llv17wr.bkt.clouddn.com/pic/16a627bb2bcae44c1dc395acc3f9a6188655.png
     * title : 直播标题
     * isclose : 1
     * peoplenum : 57
     * host_id : 1
     * play_url : rtmp://slives.sdyilian.top/live/10111?txSecret=2b514d1e038898f57796af287d193966&txTime=5e7b532e
     * goods : [{"goods_name":"商品名称","goods_id":"1","original_img1":"https://selfbt.xxbke.com/gzh/xx-WoPi=T.33.jpg"}]
     * is_like_host : 0
     */

    private String id;
    private String room;
    private String starttime;
    private String cover;
    private String title;
    private String isclose;
    private int peoplenum;
    private String host_id;
    private String play_url;
    private int is_like_host;
    private List<GoodsBean> goods;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsclose() {
        return isclose;
    }

    public void setIsclose(String isclose) {
        this.isclose = isclose;
    }

    public int getPeoplenum() {
        return peoplenum;
    }

    public void setPeoplenum(int peoplenum) {
        this.peoplenum = peoplenum;
    }

    public String getHost_id() {
        return host_id;
    }

    public void setHost_id(String host_id) {
        this.host_id = host_id;
    }

    public String getPlay_url() {
        return play_url;
    }

    public void setPlay_url(String play_url) {
        this.play_url = play_url;
    }

    public int getIs_like_host() {
        return is_like_host;
    }

    public void setIs_like_host(int is_like_host) {
        this.is_like_host = is_like_host;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

}
