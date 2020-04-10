package com.example.qd.douyinwu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.qd.douyinwu.R;
import com.example.qd.douyinwu.adapter.GoodsListAdapter;
import com.example.qd.douyinwu.been.ChooseGoodsBean;
import com.example.qd.douyinwu.been.ClassicGoodsBean;
import com.example.qd.douyinwu.been.FileBean;
import com.example.qd.douyinwu.been.GoodsBean;
import com.example.qd.douyinwu.intef.ResultListener;
import com.example.qd.douyinwu.utils.BaseOkGoUtils;
import com.example.qd.douyinwu.utils.JsonUtils;
import com.example.qd.douyinwu.utils.L;
import com.example.qd.douyinwu.utils.PersonInfoManager;
import com.example.qd.douyinwu.utils.ToastUtils;
import com.google.gson.Gson;
import com.lidong.photopicker.SelectModel;
import com.lidong.photopicker.intent.PhotoPickerIntent;
import com.qiniu.android.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.qd.douyinwu.constant.HttpConstant.GET_GOODS_LIST;
import static com.example.qd.douyinwu.constant.HttpConstant.UPLOAD;
import static com.example.qd.douyinwu.constant.HttpConstant.UPLOAD_SHORT_VIDEO;
import static com.lidong.photopicker.PhotoPickerActivity.EXTRA_RESULT;

public class UploadVideoActivity extends Activity {
    private static final int REQUEST_CAMERA_CODE = 1001 ;
    private LinearLayout llytChooseCover,llytGoods;
    private TextView tvUpload;
    private EditText videoTip;
    private ImageView imgBack,imgCover;
    private RecyclerView recyclerView;
    private GoodsListAdapter goodsListAdapter;
    private String title,videoPath,goodId;
    private String goodsId="";
    private List<GoodsBean> goodsBeanList = new ArrayList<>();
    private List<ChooseGoodsBean> chooseGoodsBeans = new ArrayList<>();
    private ArrayList<String> imagePaths = new ArrayList<>();
    private RelativeLayout uploadTip;
    private String seletedGoodsId = "";
    private String uploadCoverPath="";
    private String uploadVideoPath="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_uploadvideo);
        videoPath = getIntent().getStringExtra("shortVideoPath");
        llytChooseCover = findViewById(R.id.llytChooseCover);
        recyclerView = findViewById(R.id.goodList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        goodsListAdapter = new GoodsListAdapter(UploadVideoActivity.this, chooseGoodsBeans, new GoodsListAdapter.ItemClick() {
            @Override
            public void onClickItem(ChooseGoodsBean chooseGoodsBean) {
                for (int i=0;i<chooseGoodsBeans.size();i++){
                    if (chooseGoodsBean.getGoods_id() .equals(chooseGoodsBeans.get(i).getGoods_id())){
                        chooseGoodsBeans.get(i).setSeleted(true);
                        seletedGoodsId = chooseGoodsBean.getGoods_id();
                    }else {
                        chooseGoodsBeans.get(i).setSeleted(false);
                    }
                }
                goodsListAdapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(goodsListAdapter);
        tvUpload = findViewById(R.id.tvCommmit);
        videoTip = findViewById(R.id.edtTitle);
        imgBack = findViewById(R.id.imgBack);
        imgCover = findViewById(R.id.imgCover);
        videoTip = findViewById(R.id.edtTitle);
        uploadTip = findViewById(R.id.rlytUploading);
        llytChooseCover.setOnClickListener(onClickListener);
        tvUpload.setOnClickListener(onClickListener);
        imgBack.setOnClickListener(onClickListener);
        videoTip.addTextChangedListener(textWatcher);
        getGoodList();

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imgBack:
                    finish();
                    break;
                case R.id.tvCommmit:
                    if (null!=imagePaths&&imagePaths.size()>0){
                        uploadCoverFile(imagePaths.get(0));
                    }else {
                        ToastUtils.s(getApplicationContext(),"请选择封面");
                    }
                    break;
                case R.id.llytChooseCover:
                    seletedCover();
                    break;

            }
        }
    };
    private void uploadShortVideo(){

        Map<String, Object> map = new HashMap<>();
        if (!StringUtils.isNullOrEmpty(uploadCoverPath)){
            map.put("pic_path",uploadCoverPath);
        }else {
            ToastUtils.s(getApplicationContext(),"封面图为选择");
            return;
        }
        if (StringUtils.isNullOrEmpty(title)){
            ToastUtils.s(getApplicationContext(),"视频标题未填写");
            return;
        }else {
            map.put("title",title);
        }
        map.put("user_id", PersonInfoManager.INSTANCE.getUserId());
        map.put("goods_id",seletedGoodsId);
        map.put("video_path",uploadVideoPath);
        BaseOkGoUtils.postOkGo(UploadVideoActivity.this,map, UPLOAD_SHORT_VIDEO, new ResultListener() {
            @Override
            public void onSucceeded(Object object) {
                try {
                    ToastUtils.s(getApplicationContext(),"视频发布成功, 后台审核后即可查看");
                    uploadTip.setVisibility(View.GONE);
                    finish();

                }catch (Exception e){
                    uploadTip.setVisibility(View.GONE);
                    ToastUtils.s(getApplicationContext(),"视频发布失败");
                    e.printStackTrace();
                }
            }
        });
    }

    private void uploadCoverFile(String filePath){
        if (StringUtils.isNullOrEmpty(filePath)) {
            return;
        }
        uploadTip.setVisibility(View.VISIBLE);
        Map<String, Object> map = new HashMap<>();
        map.put("user_id",PersonInfoManager.INSTANCE.getUserId());
        BaseOkGoUtils.postOkGoWithFile(UploadVideoActivity.this,map, UPLOAD,filePath, new ResultListener() {
            @Override
            public void onSucceeded(Object object) {
                try {
                    uploadCoverPath = (String) object;
                    uploadVideoFile(videoPath);

                }catch (Exception e){
                    uploadTip.setVisibility(View.GONE);
                    ToastUtils.s(getApplicationContext(),"视频发布失败");
                    e.printStackTrace();
                }
            }
        });
    }


    private void uploadVideoFile(String filePath){
        if (StringUtils.isNullOrEmpty(filePath)) {
            uploadTip.setVisibility(View.GONE);
            ToastUtils.s(getApplicationContext(),"视频发布失败");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("user_id",PersonInfoManager.INSTANCE.getUserId());
        BaseOkGoUtils.postOkGoWithFile(UploadVideoActivity.this,map, UPLOAD,filePath, new ResultListener() {
            @Override
            public void onSucceeded(Object object) {
                try {
                    uploadVideoPath = (String) object;
                   uploadShortVideo();

                }catch (Exception e){
                    uploadTip.setVisibility(View.GONE);
                    ToastUtils.s(getApplicationContext(),"视频发布失败");
                    e.printStackTrace();
                }
            }
        });
    }

    public void getGoodList(){
        Map<String, Object> map = new HashMap<>();
        map.put("user_id",PersonInfoManager.INSTANCE.getUserId());
        map.put("store_id",PersonInfoManager.INSTANCE.getStoreId());
        BaseOkGoUtils.postOkGo(UploadVideoActivity.this,map, GET_GOODS_LIST, new ResultListener() {
            @Override
            public void onSucceeded(Object object) {
                try {
                    L.e("qpf","直播分类获取成功 -- " + object.toString());
                    ClassicGoodsBean goodsBean = JsonUtils.parseJsonWithGson(object,ClassicGoodsBean.class);
                    goodsBeanList = goodsBean.getList();
                    for (GoodsBean bean:goodsBeanList){
                        ChooseGoodsBean chooseGoodsBean = new ChooseGoodsBean();
                        chooseGoodsBean.setGoods_id(bean.getGoods_id());
                        chooseGoodsBean.setSeleted(false);
                        chooseGoodsBean.setGoods_name(bean.getGoods_name());
                        chooseGoodsBean.setOriginal_img1(bean.getOriginal_img1());
                        chooseGoodsBeans.add(chooseGoodsBean);
                    }
                    goodsListAdapter.setNewData(chooseGoodsBeans);
                    goodsListAdapter.notifyDataSetChanged();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    private void seletedCover(){
        PhotoPickerIntent intent = new PhotoPickerIntent(UploadVideoActivity.this);
        intent.setSelectModel(SelectModel.SINGLE);
        intent.setShowCarema(false); // 是否显示拍照
        intent.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
        startActivityForResult(intent, REQUEST_CAMERA_CODE);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            title = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA_CODE){
            if (null!=data){
                imagePaths = data.getStringArrayListExtra(EXTRA_RESULT);
                if (null!=imagePaths&&imagePaths.size()>0){
                    Glide.with(this).load(imagePaths.get(0)).into(imgCover);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
