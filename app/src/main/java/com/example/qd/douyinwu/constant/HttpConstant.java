package com.example.qd.douyinwu.constant;

public interface HttpConstant {
    String ROOT_URL = "http://slive.sdyilian.top/slapi/short";
    String ROOT_URL1 = "http://slive.sdyilian.top/slapi/alive";
    String ROOT_URL2 = "http://slive.sdyilian.top/slapi/goods";
    String ROOT_URL3 = "http://slive.sdyilian.top/slapi/public";
    //获取短视频主页列表
    String GET_SHORTVIDEO_LIST = ROOT_URL + "/index";
    //短视频点赞
    String SET_LIKE = ROOT_URL+"/like";
    //短视频取消点赞
    String DEL_LIKE = ROOT_URL+"/del_like";
    //短视频主评论列表
    String GET_SHORVIDEO_MAIN_COMMENTLIST = ROOT_URL+"/comments_list";
    //短视频子评论列表
    String GET_SHORVIDEO_SUN_COMMENTLIST = ROOT_URL+"/comments_child_list";
    //短视频发表评论
    String SET_SHORT_VIDEO_COMMENT = ROOT_URL+"/video_comments";
    //短视频关注视频发布者
    String SET_SHORT_VIDEO_GUANZHU = ROOT_URL + "/like_host";
    //设置评论点赞
    String SET_COMMENT_LIKE = ROOT_URL + "/video_comments_like_num";
    //取消评论点赞
    String CANCEL_COMMENT_LIKE = ROOT_URL + "/del_comments_like";
    //获取直播分类
    String GET_ZHIBO_TYPE = ROOT_URL1 + "/gettype";
    //获取直播列表
    String GET_ZHIBO_LITS = ROOT_URL1 + "/live_list";
    //获取商品列表
    String GET_GOODS_LIST = ROOT_URL2+"/host_goods_list";

    String UPLOAD_SHORT_VIDEO = ROOT_URL+"/deal_with";

    String UPLOAD = ROOT_URL3+"/upload";

    String PLAYALIVE = ROOT_URL1+"/playalive";

    String START_LIVE = ROOT_URL1+"/live_start";

    String CLOSE_LIVE = ROOT_URL1+"/close_alive";
}
