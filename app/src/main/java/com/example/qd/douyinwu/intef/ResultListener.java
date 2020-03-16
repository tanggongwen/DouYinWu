package com.example.qd.douyinwu.intef;


import com.example.qd.douyinwu.utils.L;

/**
 * Created by icebox12 on 2017/a/13.
 */

public abstract class ResultListener {
    public abstract void onSucceeded(Object object);
    public void onSucceededToBaseBeen(Object object){}
    public void onFailed(String content){
        L.e("qpf","错误 onFailed -- " + content);

    }
    public void onErr(String e){
//        MyToast.show("错误：" + e);
        L.e("qpf","错误 -- " + e);
    }
}
