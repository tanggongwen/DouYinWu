package com.example.qd.douyinwu.been;

import java.io.Serializable;
import java.util.List;

public class ClassicGoodsBean implements Serializable {
    private String num;
    private List<GoodsBean> list;

    public List<GoodsBean> getList() {
        return list;
    }

    public void setList(List<GoodsBean> list) {
        this.list = list;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
