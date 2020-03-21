package com.example.qd.douyinwu.been;

import java.io.Serializable;

public class GoodsBean implements Serializable {

    /**
     * goods_id : 1
     * goods_name : 商品名称
     * original_img1 : https://selfbt.xxbke.com/gzh/xx-WoPi=T.33.jpg
     */

    private String goods_id;
    private String goods_name;
    private String original_img1;


    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getOriginal_img1() {
        return original_img1;
    }

    public void setOriginal_img1(String original_img1) {
        this.original_img1 = original_img1;
    }


}
