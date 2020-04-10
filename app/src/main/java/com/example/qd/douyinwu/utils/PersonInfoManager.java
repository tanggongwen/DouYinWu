package com.example.qd.douyinwu.utils;


import com.example.qd.douyinwu.been.UserBean;
import com.qiniu.android.utils.StringUtils;

public enum PersonInfoManager {
    INSTANCE;
    private UserBean userBean;






    public UserBean getUserBean() {
        return GsonUtil.GsonToBean(SPUtils.getInstance().getString(SpConstants.KEY_USER,""), UserBean.class);
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
        SPUtils.getInstance().put(SpConstants.KEY_USER,GsonUtil.GsonString(userBean));
    }


    public String getUserId(){
        if (null!=getUserBean()){
            return getUserBean().getUser_id();
        }else {
            return "";
        }
    }

    public String getStoreId(){
        if (null!=getUserBean()&& StringUtils.isNullOrEmpty(getUserBean().getStore_id()+"")){
            return getUserBean().getStore_id()+"";
        }else {
            return "";
        }
    }



}
