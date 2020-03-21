package com.example.qd.douyinwu.activity;

import android.app.Activity;
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
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.qd.douyinwu.R;
import com.example.qd.douyinwu.adapter.GoodsListAdapter;
import com.example.qd.douyinwu.been.ChooseGoodsBean;
import com.example.qd.douyinwu.been.ClassicGoodsBean;
import com.example.qd.douyinwu.been.GoodsBean;
import com.example.qd.douyinwu.intef.ResultListener;
import com.example.qd.douyinwu.utils.BaseOkGoUtils;
import com.example.qd.douyinwu.utils.JsonUtils;
import com.example.qd.douyinwu.utils.L;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.qd.douyinwu.constant.HttpConstant.GET_GOODS_LIST;
import static com.example.qd.douyinwu.constant.HttpConstant.UPLOAD_SHORT_VIDEO;

public class UploadVideoActivity extends Activity {
    private LinearLayout llytChooseCover,llytGoods;
    private TextView tvUpload;
    private EditText videoTip;
    private ImageView imgBack,imgCover;
    private RecyclerView recyclerView;
    private GoodsListAdapter goodsListAdapter;
    private String title,videoPath,goodId;
    private String goodsId="";
    private String pic_path="";
    private List<GoodsBean> goodsBeanList = new ArrayList<>();
    private List<ChooseGoodsBean> chooseGoodsBeans = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_uploadvideo);
        videoPath = getIntent().getStringExtra("shortVideoPath");
        llytChooseCover = findViewById(R.id.llytChooseCover);
        recyclerView = findViewById(R.id.goodList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        goodsListAdapter = new GoodsListAdapter(UploadVideoActivity.this,chooseGoodsBeans);
        recyclerView.setAdapter(goodsListAdapter);
        tvUpload = findViewById(R.id.tvCommmit);
        videoTip = findViewById(R.id.edtTitle);
        imgBack = findViewById(R.id.imgBack);
        imgCover = findViewById(R.id.imgCover);
        videoTip = findViewById(R.id.edtTitle);
        llytChooseCover.setOnClickListener(onClickListener);
        tvUpload.setOnClickListener(onClickListener);
        imgBack.setOnClickListener(onClickListener);
        videoTip.addTextChangedListener(textWatcher);
        getGoodList();
        goodsListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                for (int i=0;i<chooseGoodsBeans.size();i++){
                    if (i ==position){
                        chooseGoodsBeans.get(i).setSeleted(true);
                    }else {
                        chooseGoodsBeans.get(i).setSeleted(false);
                    }
                }
            }
        });
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.imgBack:
                    finish();
                    break;
                case R.id.tvCommmit:
                    uploadShortVideo();
                    break;
                case R.id.llytChooseCover:
                    break;

            }
        }
    };
    private void uploadShortVideo(){
        Map<String, Object> map = new HashMap<>();
        map.put("user_id",1);
        map.put("goods_id",goodsListAdapter.getGoodsId());
        map.put("title",title);
        map.put("video_path",videoPath);
        map.put("pic_path",pic_path);
        BaseOkGoUtils.postOkGo(UploadVideoActivity.this,map, UPLOAD_SHORT_VIDEO, new ResultListener() {
            @Override
            public void onSucceeded(Object object) {
                try {
                    L.e("qpf","直播分类获取成功 -- " + object.toString());


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void getGoodList(){
        Map<String, Object> map = new HashMap<>();
        map.put("user_id",1);
        map.put("store_id",1);
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
}
