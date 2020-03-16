package com.example.qd.douyinwu.constant;

public interface HttpConstant {
    String ROOT_URL = "http://slive.sdyilian.top/slapi/short";
    String ROOT_URL1 = "http://slive.sdyilian.top/slapi/alive";
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
}
