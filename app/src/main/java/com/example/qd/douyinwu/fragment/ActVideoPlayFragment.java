package com.example.qd.douyinwu.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.qd.douyinwu.activity.ConfigActivity;
import com.example.qd.douyinwu.activity.VideoRecordActivity;
import com.example.qd.douyinwu.adapter.ChildCommentAdapter;
import com.example.qd.douyinwu.adapter.CommitAdapter;
import com.example.qd.douyinwu.R;
import com.example.qd.douyinwu.adapter.LoadMoreAdapter;
import com.example.qd.douyinwu.been.LoadMoreBean;
import com.example.qd.douyinwu.been.MainComment;
import com.example.qd.douyinwu.been.ShortVideo;
import com.example.qd.douyinwu.been.SunComment;
import com.example.qd.douyinwu.intef.ResultListener;
import com.example.qd.douyinwu.interfaces.OnViewPagerListener;
import com.example.qd.douyinwu.utils.BaseOkGoUtils;
import com.example.qd.douyinwu.utils.GetScreenWinth;
import com.example.qd.douyinwu.utils.JsonUtils;
import com.example.qd.douyinwu.utils.L;
import com.example.qd.douyinwu.utils.MyVideoPlayer;
import com.example.qd.douyinwu.utils.PagerLayoutManager;
import com.example.qd.douyinwu.utils.PermissionChecker;
import com.example.qd.douyinwu.utils.PersonInfoManager;
import com.example.qd.douyinwu.utils.SoftKeyBoardListener;
import com.example.qd.douyinwu.utils.SoftKeyHideShow;
import com.example.qd.douyinwu.utils.ToastUtils;
import com.example.qd.douyinwu.utils.Utils;
import com.example.qd.douyinwu.utils.VideoAdapter;
import com.example.qd.douyinwu.view.Love;
import com.qiniu.pili.droid.shortvideo.PLAuthenticationResultCallback;
import com.qiniu.pili.droid.shortvideo.PLShortVideoEnv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.drakeet.multitype.MultiTypeAdapter;

import static com.example.qd.douyinwu.constant.HttpConstant.CANCEL_COMMENT_LIKE;
import static com.example.qd.douyinwu.constant.HttpConstant.DEL_LIKE;
import static com.example.qd.douyinwu.constant.HttpConstant.GET_SHORTVIDEO_LIST;
import static com.example.qd.douyinwu.constant.HttpConstant.GET_SHORVIDEO_MAIN_COMMENTLIST;
import static com.example.qd.douyinwu.constant.HttpConstant.GET_SHORVIDEO_SUN_COMMENTLIST;
import static com.example.qd.douyinwu.constant.HttpConstant.SET_COMMENT_LIKE;
import static com.example.qd.douyinwu.constant.HttpConstant.SET_LIKE;
import static com.example.qd.douyinwu.constant.HttpConstant.SET_SHORT_VIDEO_COMMENT;
import static com.example.qd.douyinwu.constant.HttpConstant.SET_SHORT_VIDEO_GUANZHU;

public class ActVideoPlayFragment extends Fragment {
    private List<ShortVideo> myData = new ArrayList<>();
    private List<MainComment> mainComments = new ArrayList<>();//短视频主评论列表
    private List<SunComment> sunComments = new ArrayList<>();//短视频子评论列表
    private RecyclerView recyclerView;
    private PagerLayoutManager mLayoutManager;
    private MyVideoPlayer jzVideo;
    private SoftKeyBoardListener softKeyBoardListener;//软键盘监听
    private CommitAdapter commitAdapter;
    private ChildCommentAdapter childcommentAdapter;
    private RecyclerView recyclerViewCommit;
    private TextView tv_allnum;//评论数量
    private RelativeLayout rl_bottom;
    private View commit;
    private TextView tv_shape, tv_shape2, tv_send, tv_context;
    private LinearLayout ll_cancel;
    private RelativeLayout rl_all;
    private EditText et_context;
    private Animation showAction;
    private VideoAdapter adapter;
    private Love like;//点赞
    private boolean visibleUser = true;
    /**
     * 默认从第一个开始播放
     */
    private int positionClick = 0;
    private String temp_c_id = "";
    private int suncommentPos = 0;
    /**
     * 是否可以自动滑动
     * 当现实评论列表，说明用户想评论，不可以自动滑动
     */
    private boolean isScroll = true;
    private ImageView iv_public;

    private int PAGE = 1;
    private int NUM = 35;
    private List<Object> list;
    private MultiTypeAdapter multitypeadapter;
    private View rootView;

    @Override
    public void onResume() {
        super.onResume();
        //home back
        if (jzVideo != null) {
            jzVideo.goOnPlayOnResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // 视频回去的时候要暂停
        ((AudioManager) getContext().getSystemService(
                Context.AUDIO_SERVICE)).requestAudioFocus(
                new AudioManager.OnAudioFocusChangeListener() {
                    @Override
                    public void onAudioFocusChange(int focusChange) {
                    }
                }, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        //home back
//        if (jzVideo != null) {
//            jzVideo.goOnPlayOnPause();
//        }
    }

    public void pause(){
                if (jzVideo != null) {
                    if (jzVideo.isCurrentPlay()){
                        jzVideo.goOnPlayOnPause();
                    }
        }
    }

    public void resume(){
            if (jzVideo != null) {
                jzVideo.goOnPlayOnResume();
            }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView ==null){
            rootView=inflater .inflate(R.layout.act_video_paly  ,container,false) ;
        }
        initView();
        return rootView;
    }

    private void initView(){

        setInit();
        setView();
        getVideoList();
        setSoftKeyBoardListener();
        PLShortVideoEnv.checkAuthentication(getContext().getApplicationContext(), new PLAuthenticationResultCallback() {
            @Override
            public void onAuthorizationResult(int result) {
                if (result == PLAuthenticationResultCallback.UnCheck) {
                    ToastUtils.s(getContext(), "UnCheck");
                } else if (result == PLAuthenticationResultCallback.UnAuthorized) {
                    ToastUtils.s(getContext(), "UnAuthorized");
                } else {
                    ToastUtils.s(getContext(), "Authorized");
                }
            }
        });
    }

    private void setInit() {
        //沉浸状态栏
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //Android 系统5.0一下
        } else {
            //Android 系统5.0一上
            Window window = getActivity().getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(0xff000000);
        }
        list = new ArrayList<>();
    }

    private void setView() {
        rl_bottom = rootView.findViewById(R.id.rl_bottom);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerViewCommit = rootView.findViewById(R.id.recyclerViewCommit);
        tv_allnum = rootView.findViewById(R.id.tv_allnum);
        commit = rootView.findViewById(R.id.commit);
        tv_shape = rootView.findViewById(R.id.tv_shape);
        tv_shape2 = rootView.findViewById(R.id.tv_shape2);
        tv_send = rootView.findViewById(R.id.tv_send);
        tv_context = rootView.findViewById(R.id.tv_context);
        ll_cancel = rootView.findViewById(R.id.ll_cancel);
        rl_all = rootView.findViewById(R.id.rl_all);
        et_context = rootView.findViewById(R.id.et_context);
        myData = new ArrayList<>();

        like = rootView.findViewById(R.id.like);
//        like.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
////                ToastUtils.s(ActVideoPlayFragment.this,"点赞啦");
//                setLike();//找到点赞的视频id
//                return false;
//            }
//        });
        iv_public = rootView.findViewById(R.id.iv_public);
        iv_public.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPermissionOK()){
                    //跳入七牛云发布短视频界面
                    jumpToCaptureActivity();
                }

            }
        });
    }

    private boolean isPermissionOK() {
        PermissionChecker checker = new PermissionChecker(getActivity());
        boolean isPermissionOK = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checker.checkPermission();
        if (!isPermissionOK) {
            ToastUtils.s(getContext(), "Some permissions is not approved !!!");
        }
        return isPermissionOK;
    }


    public void jumpToCaptureActivity() {
        if (null==PersonInfoManager.INSTANCE.getUserBean()){
            ToastUtils.s(Utils.getContext(),"请先登录账号");
        }else {
            Intent intent = new Intent(getActivity(), VideoRecordActivity.class);
            intent.putExtra(VideoRecordActivity.PREVIEW_SIZE_RATIO, ConfigActivity.PREVIEW_SIZE_RATIO_POS);
            intent.putExtra(VideoRecordActivity.PREVIEW_SIZE_LEVEL, ConfigActivity.PREVIEW_SIZE_LEVEL_POS);
            intent.putExtra(VideoRecordActivity.ENCODING_MODE, ConfigActivity.ENCODING_MODE_LEVEL_POS);
            intent.putExtra(VideoRecordActivity.ENCODING_SIZE_LEVEL,ConfigActivity.ENCODING_SIZE_LEVEL_POS);
            intent.putExtra(VideoRecordActivity.ENCODING_BITRATE_LEVEL, ConfigActivity.ENCODING_BITRATE_LEVEL_POS);
            intent.putExtra(VideoRecordActivity.AUDIO_CHANNEL_NUM, ConfigActivity.AUDIO_CHANNEL_NUM_POS);
            startActivity(intent);
        }

    }

    private void getVideoList(){
        Map<String, Object> map = new HashMap<>();
        map.put("page",PAGE);
        map.put("num",NUM);
        map.put("user_id", PersonInfoManager.INSTANCE.getUserId());
        BaseOkGoUtils.postOkGo(getContext(),map, GET_SHORTVIDEO_LIST, new ResultListener() {
            @Override
            public void onSucceeded(Object object) {
                try {
                    L.e("qpf","短视频列表获取成功 -- " + object.toString());
                    myData = JsonUtils.objBeanToList(object, ShortVideo.class);
                    setAdapter();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void cancelLike(){
        Map<String, Object> map = new HashMap<>();
        map.put("user_id",PersonInfoManager.INSTANCE.getUserId());
        map.put("s_id",myData.get(positionClick).getS_id());
        BaseOkGoUtils.postOkGo(getContext(),map, DEL_LIKE, new ResultListener() {
            @Override
            public void onSucceeded(Object object) {
                try {
                    L.e("qpf","取消点赞成功 -- " + object.toString());
                    try {
                        com.alibaba.fastjson.JSONObject lmap = (JSONObject) JSONObject.parse(object.toString());
                        int code = (int) lmap.get("code");
                        String msg = (String) lmap.get("msg");
//                        Toast.makeText(ActVideoPlayFragment.this, msg, 0).show();
                        if(code == 200){
                            //取消点赞红心
                            myData.get(positionClick).setIs_like_video(0);
                            adapter.notifyDataSetChanged();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void cancelCommentLike(String c_id,final int position,final boolean isMainComment){
        Map<String, Object> map = new HashMap<>();
        map.put("user_id",PersonInfoManager.INSTANCE.getUserId());
        map.put("c_id",c_id);
        BaseOkGoUtils.postOkGo(getContext(),map, CANCEL_COMMENT_LIKE, new ResultListener() {
            @Override
            public void onSucceeded(Object object) {
                try {
                    L.e("qpf","取消点赞成功 -- " + object.toString());
                    try {
                        com.alibaba.fastjson.JSONObject lmap = (JSONObject) JSONObject.parse(object.toString());
                        int code = (int) lmap.get("code");
                        String msg = (String) lmap.get("msg");
//                        Toast.makeText(ActVideoPlayFragment.this, msg, 0).show();
                        if(code == 200){
                            if(isMainComment){
                                MainComment comment = (MainComment) list.get(position);
                                comment.setIs_like(2);
                            }else{
                                SunComment comment = (SunComment) list.get(position);
                                comment.setIs_like(2);
                            }
                            multitypeadapter.setItems(list);
                            multitypeadapter.notifyDataSetChanged();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    private void setCommentLike(String c_id,final int position,final boolean isMainComment){
        Map<String, Object> map = new HashMap<>();
        map.put("user_id",PersonInfoManager.INSTANCE.getUserId());
        map.put("s_id",myData.get(positionClick).getS_id());
        map.put("c_id",c_id);
        BaseOkGoUtils.postOkGo(getContext(),map, SET_COMMENT_LIKE, new ResultListener() {
            @Override
            public void onSucceeded(Object object) {
                try {
                    L.e("qpf","点赞成功 -- " + object.toString());
                    try {
                        com.alibaba.fastjson.JSONObject lmap = (JSONObject) JSONObject.parse(object.toString());
                        int code = (int) lmap.get("code");
                        String msg = (String) lmap.get("msg");
//                        Toast.makeText(ActVideoPlayFragment.this, msg, 0).show();
                        if(code == 200){
                            //设置点赞红心
                            if(isMainComment){
                                MainComment comment = (MainComment) list.get(position);
                                comment.setIs_like(1);
                            }else{
                                SunComment comment = (SunComment) list.get(position);
                                comment.setIs_like(1);
                            }
                            multitypeadapter.setItems(list);
                            multitypeadapter.notifyDataSetChanged();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void setLike(){
        Map<String, Object> map = new HashMap<>();
        map.put("user_id",PersonInfoManager.INSTANCE.getUserId());
        map.put("s_id",myData.get(positionClick).getS_id());
        BaseOkGoUtils.postOkGo(getContext(),map, SET_LIKE, new ResultListener() {
            @Override
            public void onSucceeded(Object object) {
                try {
                    L.e("qpf","点赞成功 -- " + object.toString());
                    try {
                        com.alibaba.fastjson.JSONObject lmap = (JSONObject) JSONObject.parse(object.toString());
                        int code = (int) lmap.get("code");
                        String msg = (String) lmap.get("msg");
//                        Toast.makeText(ActVideoPlayFragment.this, msg, 0).show();
                        if(code == 200){
                            //设置点赞红心
                            myData.get(positionClick).setIs_like_video(1);
                            adapter.notifyDataSetChanged();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    private void setAdapter() {
        //设置adapter
        adapter = new VideoAdapter(getActivity(), myData);
        recyclerView.setAdapter(adapter);
        mLayoutManager = new PagerLayoutManager(getContext(), OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        mLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onInitComplete(View view) {
                //点击进入 0
                playVideo(view, false);
            }

            @Override
            public void onPageSelected(int position, boolean isBottom, View view) {
                positionClick = position;
                //滑动选择 1
                playVideo(view, isBottom);
            }

            @Override
            public void onPageRelease(boolean isNext, int position, View view) {
                //暂停上一个播放
//                releaseVideo(view);
            }
        });
        adapter.setOnsetlikelistener(new VideoAdapter.OnSetLikeListener() {
            @Override
            public void isSetLike(boolean isLike) {
                if (null==PersonInfoManager.INSTANCE.getUserBean()){
                    ToastUtils.s(Utils.getContext(),"请先登录账号");
                    return;
                }
                if(!isLike){
                    setLike();//点赞
                }else{
                    cancelLike();//取消点赞
                }
            }

            @Override
            public void guanzhu() {
                if (null==PersonInfoManager.INSTANCE.getUserBean()){
                    ToastUtils.s(Utils.getContext(),"请先登录账号");
                    return;
                }
                setGuanzhu();
            }
        });
        adapter.setOnItemClickListerer(new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String type, View view, View view1, View view2) {
                if (null==PersonInfoManager.INSTANCE.getUserBean()){
                    ToastUtils.s(Utils.getContext(),"请先登录账号");
                    return;
                }
//                if (type.equals("back")) {
//                    //返回
//                    if (jzVideo.backPress()) {
//                        return;
//                    }
//                    finish();
//                } else
                    if (type.equals("commit")) {
                    //评论
                    //先获取评论列表数据  再展开评论
                    getCommentList();
                }
            }
        });
    }

    private int COMMENT_PAGE = 1;

    private void getCommentList(){
        Map<String, Object> map = new HashMap<>();
        map.put("s_id",myData.get(positionClick).getS_id());
        map.put("page",COMMENT_PAGE);
        map.put("num",10);
        BaseOkGoUtils.postOkGo(getContext(),map, GET_SHORVIDEO_MAIN_COMMENTLIST, new ResultListener() {
            @Override
            public void onSucceeded(Object object) {
                try {
                    L.e("qpf","短视频评论列表获取成功 -- " + object.toString());
                    mainComments = JsonUtils.objBeanToList(object, MainComment.class);
                    showCommitDialog();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }



    /**
     * 开始播放 & 监听播放完成
     */
    private void playVideo(View view, boolean isBottom) {
        if (view != null) {
            jzVideo = view.findViewById(R.id.jzVideo);
            if (!visibleUser){
                return;
            }
            jzVideo.startVideo();
            if (isBottom) {
                //到最后一个加载第二页
//                myData.add("http://static.gamemm.com/upload/video/20180914/14039_1536897206.mp4");
//                myData.add("http://static.gamemm.com/upload/video/20180810/14078_1533870358.mp4");
//                myData.add("http://static.gamemm.com/upload/video/20180914/14039_1536897206.mp4");
//                myData.add("http://static.gamemm.com/upload/video/20180810/14078_1533870358.mp4");
//                myData.add("http://static.gamemm.com/upload/video/20180914/14039_1536897206.mp4");
//                myData.add("http://static.gamemm.com/upload/video/20180810/14078_1533870358.mp4");
                adapter.notifyDataSetChanged();
            }
            jzVideo.setFinishListerer(new MyVideoPlayer.OnItemClickListener() {
                @Override
                public void onItemClick() {
                    //播放完成自动播放下一个,用户没有看评论列表可以播放下一个
                    if (isScroll) {
                        smoothMoveToPosition(recyclerView, positionClick++);
                    }
                }

                @Override
                public void onStatePlaying() {
                    if (!visibleUser){
                        jzVideo.goOnPlayOnPause();
                    }
                }
            });
        }
    }

    /**
     * 平滑的滑动到指定位置
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            //滑动指定高度
            mRecyclerView.smoothScrollBy(0, GetScreenWinth.getHeight(getActivity()) -
                    (Resources.getSystem().getDimensionPixelSize(Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"))));
        } else {
            // 第三种可能:跳转位置在最后可见项之后
            mRecyclerView.smoothScrollToPosition(position);
        }
    }

    private int SUNCOMMENT_PAGE = 1;

    private void getSunComment(String c_id,final int position) {
        Map<String, Object> map = new HashMap<>();
        map.put("s_id", myData.get(positionClick).getS_id());
        map.put("c_id", c_id);
        map.put("page",SUNCOMMENT_PAGE);
        map.put("num", 10);
        BaseOkGoUtils.postOkGo(getContext(), map, GET_SHORVIDEO_SUN_COMMENTLIST, new ResultListener() {
            @Override
            public void onSucceeded(Object object) {
                try {
                    L.e("qpf", "短视频吱吱吱评论列表获取成功 -- " + object.toString());
                    sunComments = JsonUtils.objBeanToList(object, SunComment.class);
                    for (SunComment scs:sunComments) {
                        list.add(position,scs);
                    }
                    multitypeadapter.setItems(list);
                    multitypeadapter.notifyDataSetChanged();
//                    multitypeadapter.notifyItemRangeInserted(position,list.size()+1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
    private void setGuanzhu(){
        Map<String, Object> map = new HashMap<>();
        map.put("user_id",PersonInfoManager.INSTANCE.getUserId());
        map.put("like_id",myData.get(positionClick).getUser_id());
        BaseOkGoUtils.postOkGo(getContext(),map, SET_SHORT_VIDEO_GUANZHU, new ResultListener() {
            @Override
            public void onSucceeded(Object object) {
                try {
                    L.e("qpf","关注成功 -- " + object.toString());
                    try {
                        com.alibaba.fastjson.JSONObject lmap = (JSONObject) JSONObject.parse(object.toString());
                        int code = (int) lmap.get("code");
                        String msg = (String) lmap.get("msg");
//                        Toast.makeText(ActVideoPlayFragment.this, msg, 0).show();
                        if(code == 200){
                            //设置已经关注了该主播
                            myData.get(positionClick).setIs_like_host(1);
                            adapter.notifyDataSetChanged();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 评论布局
     */
    public void showCommitDialog() {
        tv_allnum.setText(myData.get(positionClick).getS_comments_num()+"条评论");
//        if (commitAdapter == null) {
        commitAdapter = new CommitAdapter(getContext());
        childcommentAdapter = new ChildCommentAdapter(getContext());
//            recyclerViewCommit.setLayoutManager(new LinearLayoutManager(this));
//            recyclerViewCommit.setAdapter(commitAdapter);
//        } else {
//            commitAdapter.notifyDataSetChanged();
//        }
        multitypeadapter = new MultiTypeAdapter();
        multitypeadapter.register(MainComment.class,commitAdapter);
        multitypeadapter.register(SunComment.class,childcommentAdapter);
        LoadMoreAdapter loadmoreadapter = new LoadMoreAdapter();
        multitypeadapter.register(LoadMoreBean.class,loadmoreadapter);
        list.clear();
        for(MainComment mcs:mainComments){
            list.add(mcs);
            if(!TextUtils.isEmpty(mcs.getC_reply_num()) && Integer.parseInt(mcs.getC_reply_num()) > 0){
                list.add(new LoadMoreBean("展开更多回复"));
            }
        }
        commitAdapter.setmOnItemClickListerer(new CommitAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MainComment item) {
                toggleSoft();
                et_context.setText("");
                et_context.setHint("回复"+item.getNick_name());
                temp_c_id = item.getC_id();
            }
        });
        commitAdapter.setOnCommentLike(new CommitAdapter.OnCommentLike() {
            @Override
            public void commentLike(boolean like,MainComment item, int position) {
                if(like){
                    setCommentLike(item.getC_id(),position,true);
                }else{
                    cancelCommentLike(item.getC_id(),position,true);
                }
            }
        });
        childcommentAdapter.setmOnItemClickListerer(new ChildCommentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(SunComment item,int position) {
                toggleSoft();
                et_context.setText("");
                et_context.setHint("回复"+item.getNick_name());
                temp_c_id = item.getC_id();
                suncommentPos = position;
            }
        });
        childcommentAdapter.setOnCommentLike(new ChildCommentAdapter.OnCommentLike() {
            @Override
            public void commentLike(boolean like, SunComment item, int position) {
                if(like){
                    setCommentLike(item.getC_id(),position,false);
                }else{
                    cancelCommentLike(item.getC_id(),position,false);
                }
            }
        });
//        for (int i=0;i<20;i++){
//            if(i % 2 == 0){
//                list.add(new MainComment());
//            }else{
//                list.add(new MainComment());
//                for (int j=0;j<3;j++){
//                    list.add(new SunComment());
//                }
//                list.add(new LoadMoreBean("加载更多"));
//            }
//        }
        multitypeadapter.setItems(list);
        multitypeadapter.notifyDataSetChanged();
        recyclerViewCommit.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewCommit.setAdapter(multitypeadapter);
        loadmoreadapter.setOnLoadMoreInterface(new LoadMoreAdapter.onLoadMore() {
            @Override
            public void onLoadMore(int position) {
//                for(int i=0;i<5;i++){
//                    list.add(position,new SunComment());
//                }
                getSunComment(mainComments.get(position-1).getC_id(),position);
//                adapter.setItems(list);
//                adapter.notifyItemRangeInserted(position,6);
            }
        });

        //为布局设置显示的动画
        showAction = AnimationUtils.loadAnimation(getContext(), R.anim.actionsheet_dialog_in);
        commit.startAnimation(showAction);

        //显示布局和阴影
        commit.setVisibility(View.VISIBLE);
        tv_shape.setVisibility(View.VISIBLE);
        //隐藏点赞
        like.setVisibility(View.GONE);
        iv_public.setVisibility(View.GONE);
        //不可以滑动
        isScroll = false;

        //关闭评论
        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commit.setVisibility(View.GONE);
                tv_shape.setVisibility(View.GONE);
                like.setVisibility(View.VISIBLE);
                iv_public.setVisibility(View.VISIBLE);
                //可以滑动
                isScroll = true;
            }
        });
        //阴影点击,隐藏评论列表和阴影
        tv_shape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commit.setVisibility(View.GONE);
                tv_shape.setVisibility(View.GONE);
                like.setVisibility(View.VISIBLE);
                iv_public.setVisibility(View.VISIBLE);
                //可以滑动
                isScroll = true;
            }
        });
        //输入评论点击
        tv_context.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoftKeyHideShow.HideShowSoftKey(getContext());//隐藏软键盘
            }
        });
        //第二层阴影点击，隐藏输入评论框和软键盘
        tv_shape2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SoftKeyHideShow.HideShowSoftKey(getContext());//隐藏软键盘
            }
        });
        //发送评论
        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(et_context.getText().toString())){
                    ToastUtils.s(getContext(),"评论内容不能为空");
                    return;
                }
                //发送评论
                sendComment(et_context.getText().toString());
            }
        });
    }

    private void notifyAdapter(String temp_c_id,String content){
        if(TextUtils.isEmpty(temp_c_id)){//表示是主评论 那么评论者就是我
            MainComment comment = new MainComment();
            comment.setC_content(content);
            comment.setUser_id(PersonInfoManager.INSTANCE.getUserId());
            comment.setNick_name(PersonInfoManager.INSTANCE.getUserBean().getNick_name());
            comment.setHead_pic(PersonInfoManager.INSTANCE.getUserBean().getHead_pic());
            comment.setC_reply_num("0");
            comment.setC_time("刚刚");
            list.add(0,comment);
            multitypeadapter.notifyDataSetChanged();
        }else{//如果是子评论
            //找到子评论的位置 遍历list 找到temp_c_id的位置
            SunComment suncomment = new SunComment();
            suncomment.setC_content(content);
            suncomment.setUser_id(PersonInfoManager.INSTANCE.getUserBean().getUser_id());
            suncomment.setNick_name(PersonInfoManager.INSTANCE.getUserBean().getNick_name());
            suncomment.setHead_pic(PersonInfoManager.INSTANCE.getUserBean().getHead_pic());
            suncomment.setC_reply_num("0");
            suncomment.setC_time("刚刚");
            list.add(suncommentPos+1, suncomment);
            multitypeadapter.setItems(list);
            multitypeadapter.notifyDataSetChanged();
        }
    }

    private void sendComment(final String content){
        Map<String, Object> map = new HashMap<>();
        map.put("user_id",PersonInfoManager.INSTANCE.getUserId());
        map.put("c_content",content);
        map.put("s_id",myData.get(positionClick).getS_id());
        map.put("c_pid",temp_c_id);
        BaseOkGoUtils.postOkGo(getContext(),map, SET_SHORT_VIDEO_COMMENT, new ResultListener() {
            @Override
            public void onSucceeded(Object object) {
                try {
                    L.e("qpf","评论成功 -- " + object.toString());
                    try {
                        com.alibaba.fastjson.JSONObject lmap = (JSONObject) JSONObject.parse(object.toString());
                        int code = (int) lmap.get("code");
                        String msg = (String) lmap.get("msg");
                        if(code == 200){
                            Toast.makeText(getContext(), "评论成功", Toast.LENGTH_SHORT).show();
                            SoftKeyHideShow.HideShowSoftKey(getContext());//隐藏软键盘
                            notifyAdapter(temp_c_id,content);//刷新adapter
                        }else{
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            SoftKeyHideShow.HideShowSoftKey(getContext());//隐藏软键盘

                        }
                    }catch (Exception e){
                        Toast.makeText(getContext(), "评论失败", Toast.LENGTH_SHORT).show();
                        SoftKeyHideShow.HideShowSoftKey(getContext());//隐藏软键盘

                        e.printStackTrace();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    private void toggleSoft(){
        //弹起软键盘
        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    /**
     * 软键盘监听
     */
    private void setSoftKeyBoardListener() {
        softKeyBoardListener = new SoftKeyBoardListener(getActivity());
        //软键盘状态监听
        softKeyBoardListener.setListener(new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                //动态设置控件宽高
                ViewGroup.LayoutParams params = rl_bottom.getLayoutParams();
                rl_bottom.setPadding(0, 0, 0, height);
                rl_bottom.setLayoutParams(params);
                //当软键盘显示的时候
                rl_bottom.setVisibility(View.VISIBLE);
                tv_shape2.setVisibility(View.VISIBLE);

                et_context.setFocusable(true);
                et_context.setFocusableInTouchMode(true);
                et_context.setCursorVisible(true);
                et_context.requestFocus();
            }

            @Override
            public void keyBoardHide(int height) {
                //当软键盘隐藏的时候
                rl_bottom.setVisibility(View.GONE);
                tv_shape2.setVisibility(View.GONE);
                et_context.setFocusable(false);
                et_context.setFocusableInTouchMode(false);
                et_context.setCursorVisible(false);
            }
        });
        //设置点击事件,显示软键盘
        et_context.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                toggleSoft();
                et_context.setText("");
                et_context.setHint("发表评论");
                temp_c_id = "";
            }
        });
        //防止EditText点击两次才获取到焦点
        et_context.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
            int flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                flag++;
                if (flag == 2) {
                    flag = 0;//不要忘记这句话
                    //处理逻辑
                    et_context.setFocusable(true);
                    et_context.setFocusableInTouchMode(true);
                    et_context.setCursorVisible(true);
                }
                return false;
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        if (jzVideo != null) {
//            if (jzVideo.backPress()) {
//                return;
//            }
//        }
//        super.onBackPressed();
//    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser){
            visibleUser = true;
        }else {
            visibleUser = false;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }
}
