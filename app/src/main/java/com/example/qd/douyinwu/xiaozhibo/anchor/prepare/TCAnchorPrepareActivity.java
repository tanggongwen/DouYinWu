package com.example.qd.douyinwu.xiaozhibo.anchor.prepare;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.donkingliang.labels.LabelsView;
import com.example.qd.douyinwu.R;
import com.example.qd.douyinwu.activity.ActZhiBoList;
import com.example.qd.douyinwu.activity.UploadVideoActivity;
import com.example.qd.douyinwu.adapter.GoodsListAdapter;
import com.example.qd.douyinwu.been.ChooseGoodsBean;
import com.example.qd.douyinwu.been.ClassicGoodsBean;
import com.example.qd.douyinwu.been.GoodsBean;
import com.example.qd.douyinwu.been.LivePushBean;
import com.example.qd.douyinwu.been.LiveTypeBeen;
import com.example.qd.douyinwu.intef.ResultListener;
import com.example.qd.douyinwu.utils.BaseOkGoUtils;
import com.example.qd.douyinwu.utils.JsonUtils;
import com.example.qd.douyinwu.utils.L;
import com.example.qd.douyinwu.utils.PersonInfoManager;
import com.example.qd.douyinwu.utils.ToastUtils;
import com.example.qd.douyinwu.utils.Utils;
import com.example.qd.douyinwu.xiaozhibo.anchor.TCCameraAnchorActivity;
import com.example.qd.douyinwu.xiaozhibo.audience.TCCustomSwitch;
import com.example.qd.douyinwu.xiaozhibo.common.net.TCHTTPMgr;
import com.example.qd.douyinwu.xiaozhibo.common.upload.TCUploadHelper;
import com.example.qd.douyinwu.xiaozhibo.common.utils.TCConstants;
import com.example.qd.douyinwu.xiaozhibo.common.utils.TCUtils;
import com.example.qd.douyinwu.xiaozhibo.login.TCUserMgr;
import com.qiniu.android.utils.StringUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.qd.douyinwu.constant.HttpConstant.GET_GOODS_LIST;
import static com.example.qd.douyinwu.constant.HttpConstant.GET_ZHIBO_TYPE;
import static com.example.qd.douyinwu.constant.HttpConstant.START_LIVE;
import static com.example.qd.douyinwu.constant.HttpConstant.UPLOAD;

/**
 * Module:   TCAnchorPrepareActivity
 * <p>
 * Function: 主播开播设置页面
 * <p>
 * 1. 设置直播封面
 * <p>
 * 2. 设置直播标题
 * <p>
 * 3. 设置个人定位
 * <p>
 * 4. 设置摄像头推流或屏幕录制推流
 * <p>
 * 5. 设置分享到微信、微博、QQ等
 */
public class TCAnchorPrepareActivity extends Activity implements View.OnClickListener, TCUploadHelper.OnUploadListener, TCLocationHelper.OnLocationListener, RadioGroup.OnCheckedChangeListener {
    private static final String TAG = TCAnchorPrepareActivity.class.getSimpleName();
//    private static final int CAPTURE_IMAGE_CAMERA = 100;    // 封面：发起拍照
//    private static final int IMAGE_STORE = 200;             // 封面：选择图库
//    private static final int CROP_CHOOSE = 10;              // 封面：裁剪
    private static final int REQUEST_CODE_CHOOSE =2001 ;

    private TextView mTvReturn;      // 返回
    private TextView mTvPublish;     // 开始直播
    private TextView mTvPicTip;      // 封面提示
    private TextView mTvLocation;    // 显示定位的地址
    private EditText mTvTitle,mNoti;       // 直播标题
    private Dialog mPicChsDialog;  // 图片选择弹窗
    private ImageView mIvCover;       // 图片封面
    private TCCustomSwitch mSwitchLocate;  // 发起定位的按钮
    private RadioGroup mRGRecordType;  // 推流类型：摄像头推流或屏幕录制推流
    private int                             mRecordType = TCConstants.RECORD_TYPE_CAMERA;   // 默认摄像头推流

    private boolean                          mUploadingCover = false;           // 当前是否正在上传图片
    private boolean                          mPermission = false;               // 是否已经授权
    private TCUploadHelper                   mUploadHelper;                     // COS 存储封面图的工具类
    private LabelsView liveType;
    private List<LiveTypeBeen> mData;
    private String currentLiveTypeId= "";
    private String goodsId="";
    private String curretnCoverPath="";
    private String localCoverPath="";
    private TextView tvLoading;

    private List<GoodsBean> goodsBeanList = new ArrayList<>();
    private List<ChooseGoodsBean> chooseGoodsBeans = new ArrayList<>();

    private GoodsListAdapter goodsListAdapter;

    private RecyclerView recyclerView;

    private String seletedGoodsId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anchor_prepare);
        mUploadHelper = new TCUploadHelper(this, this);

        mTvTitle = findViewById(R.id.anchor_tv_title);
        mNoti = findViewById(R.id.anchor_tv_noti);
        mTvReturn = (TextView) findViewById(R.id.anchor_btn_cancel);
        mTvPicTip = (TextView) findViewById(R.id.anchor_pic_tips);
        mTvPublish = (TextView) findViewById(R.id.anchor_btn_publish);
        mIvCover = (ImageView) findViewById(R.id.anchor_btn_cover);
        mTvLocation = (TextView) findViewById(R.id.anchor_tv_location);
        mSwitchLocate = (TCCustomSwitch) findViewById(R.id.anchor_btn_location);
        tvLoading = findViewById(R.id.loading);
        mRGRecordType = (RadioGroup) findViewById(R.id.anchor_rg_record_type);
        liveType = findViewById(R.id.liveTypes);
        liveType.setOnLabelSelectChangeListener(onLabelSelectChangeListener);
        mIvCover.setOnClickListener(this);
        mTvReturn.setOnClickListener(this);
        mTvPublish.setOnClickListener(this);
        mSwitchLocate.setOnClickListener(this);
        mRGRecordType.setOnCheckedChangeListener(this);
        recyclerView = findViewById(R.id.goodList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        goodsListAdapter = new GoodsListAdapter(TCAnchorPrepareActivity.this, chooseGoodsBeans, new GoodsListAdapter.ItemClick() {
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
        mPermission = checkPublishPermission();
//        initPhotoDialog();
        getGoodList();
        initCover();
        getZhiboListType();
    }

    /**
     *     /////////////////////////////////////////////////////////////////////////////////
     *     //
     *     //                      控件初始化相关
     *     //
     *     /////////////////////////////////////////////////////////////////////////////////
     */

    /**
     * 初始化封面图
     */
    private void initCover() {
        String strCover = TCUserMgr.getInstance().getCoverPic();
        if (!TextUtils.isEmpty(strCover)) {
            RequestManager req = Glide.with(this);
            req.load(strCover).into(mIvCover);
            mTvPicTip.setVisibility(View.GONE);
        } else {
            mIvCover.setImageResource(R.drawable.publish_background);
        }
    }


    private void getZhiboListType(){
        Map<String, Object> map = new HashMap<>();
        BaseOkGoUtils.postOkGo(TCAnchorPrepareActivity.this,map, GET_ZHIBO_TYPE, new ResultListener() {
            @Override
            public void onSucceeded(Object object) {
                try {
                    L.e("qpf","直播分类获取成功 -- " + object.toString());
                    mData = JsonUtils.objBeanToList(object, LiveTypeBeen.class);
                    liveType.setLabels(mData, new LabelsView.LabelTextProvider<LiveTypeBeen>() {
                        @Override
                        public CharSequence getLabelText(TextView label, int position, LiveTypeBeen data) {
                            return data.getT_name();
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void getGoodList(){
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", PersonInfoManager.INSTANCE.getUserId());
        map.put("store_id",PersonInfoManager.INSTANCE.getStoreId());
        BaseOkGoUtils.postOkGo(TCAnchorPrepareActivity.this,map, GET_GOODS_LIST, new ResultListener() {
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

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }



    private LabelsView.OnLabelSelectChangeListener onLabelSelectChangeListener = new LabelsView.OnLabelSelectChangeListener() {
        @Override
        public void onLabelSelectChange(TextView label, Object data, boolean isSelect, int position) {
            if (isSelect){
                currentLiveTypeId = mData.get(position).getT_id();
            }
        }
    };






    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.anchor_btn_cancel:
                finish();
                break;
            case R.id.anchor_btn_publish:
                //trim避免空格字符串
                if (TextUtils.isEmpty(mTvTitle.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "请输入直播标题", Toast.LENGTH_SHORT).show();
                } else if (TCUtils.getCharacterNum(mTvTitle.getText().toString()) > TCConstants.TV_TITLE_MAX_LEN) {
                    Toast.makeText(getApplicationContext(), "直播标题过长 ,最大长度为" + TCConstants.TV_TITLE_MAX_LEN / 2, Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(mNoti.getText().toString().trim())) {
                Toast.makeText(getApplicationContext(), "请输入直播公告", Toast.LENGTH_SHORT).show();
            } else if (TCUtils.getCharacterNum(mNoti.getText().toString()) > TCConstants.TV_TITLE_MAX_LEN) {
                Toast.makeText(getApplicationContext(), "直播标题过长 ,最大长度为" + TCConstants.TV_NOTI_MAX_LEN , Toast.LENGTH_SHORT).show();
            }
            else if (!TCUtils.isNetworkAvailable(this)) {
                    Toast.makeText(getApplicationContext(), "当前网络环境不能发布直播", Toast.LENGTH_SHORT).show();
                } else {
                tvLoading.setVisibility(View.VISIBLE);
                    uploadCoverFile(localCoverPath);
//                    startPublish();
                }
                break;
            case R.id.anchor_btn_cover:
                chooseImage(true);
                break;
            case R.id.anchor_btn_location:
                if (mSwitchLocate.getChecked()) {
                    mSwitchLocate.setChecked(false, true);
                    mTvLocation.setText(R.string.text_live_close_lbs);
                } else {
                    mSwitchLocate.setChecked(true, true);
                    mTvLocation.setText(R.string.text_live_location);
                    // 发起定位
                    if (TCLocationHelper.checkLocationPermission(this)) {
                        if (!TCLocationHelper.getMyLocation(this, this)) {
                            mTvLocation.setText(getString(R.string.text_live_lbs_fail));
                            mSwitchLocate.setChecked(false, false);
                        }
                    }
                }
                break;
        }
    }

    /**
     * 发起推流
     *
     */
    private void startPublish(String roomId,String room,String pushUrl) {
        Intent intent = null;
        if (mRecordType == TCConstants.RECORD_TYPE_SCREEN) {
            //录屏
//            intent = new Intent(this, TCScreenAnchorActivity.class);
        } else {
            intent = new Intent(this, TCCameraAnchorActivity.class);
        }

        if (intent != null) {
            tvLoading.setVisibility(View.GONE);
            intent.putExtra(TCConstants.ROOM_TITLE,
                    TextUtils.isEmpty(mTvTitle.getText().toString()) ? TCUserMgr.getInstance().getNickname() : mTvTitle.getText().toString());
            intent.putExtra(TCConstants.USER_ID, PersonInfoManager.INSTANCE.getUserId());
            intent.putExtra(TCConstants.USER_NICK, PersonInfoManager.INSTANCE.getUserBean().getNick_name());
            intent.putExtra(TCConstants.USER_HEADPIC, TCUserMgr.getInstance().getAvatar());
            intent.putExtra(TCConstants.COVER_PIC, curretnCoverPath);
            intent.putExtra(TCConstants.ROOM_ID,roomId);
            intent.putExtra(TCConstants.ROOM,room);
            intent.putExtra(TCConstants.PUSHURL,pushUrl);
            intent.putExtra(TCConstants.USER_LOC,
                    mTvLocation.getText().toString().equals(getString(R.string.text_live_lbs_fail)) ||
                            mTvLocation.getText().toString().equals(getString(R.string.text_live_location)) ?
                            getString(R.string.text_live_close_lbs) : mTvLocation.getText().toString());
            startActivity(intent);
            finish();
        }
        tvLoading.setVisibility(View.GONE);
    }



    public void startLive(){
        Map<String, Object> map = new HashMap<>();
        map.put("user_id",PersonInfoManager.INSTANCE.getUserId());
        map.put("cover",curretnCoverPath);
        map.put("title",mTvTitle.getText().toString());
        map.put("notice",mNoti.getText().toString());
        map.put("t_id",currentLiveTypeId);
        map.put("goods_id",goodsId);
            BaseOkGoUtils.postOkGo(TCAnchorPrepareActivity.this,map, START_LIVE, new ResultListener() {
                @Override
                public void onSucceeded(Object object) {
                    try {
                        LivePushBean livePushBean = JsonUtils.parseJsonWithGson(object,LivePushBean.class);
                        startPublish(livePushBean.getId(),livePushBean.getRoom(),livePushBean.getPush_url());

                    }catch (Exception e){

                        e.printStackTrace();
                    }
                }

                @Override
                public void onErr(String e) {
                    tvLoading.setVisibility(View.GONE);
                }

                @Override
                public void onFailed(String content) {
                    tvLoading.setVisibility(View.GONE);
                }
            });
    }


    @SuppressLint("CheckResult")
    private void chooseImage(final boolean needTakePhoto){
        Matisse.from(TCAnchorPrepareActivity.this)
                .choose(MimeType.ofImage())//图片类型
                .countable(true)//true:选中后显示数字;false:选中后显示对号
                .maxSelectable(1)//可选的最大数
                .capture(needTakePhoto)//选择照片时，是否显示拍照
                .captureStrategy(new CaptureStrategy(true, "com.example.qd.douyinwu"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                .imageEngine(new GlideEngine())//图片加载引擎
                .forResult(REQUEST_CODE_CHOOSE);//
    }


    /**
     *     /////////////////////////////////////////////////////////////////////////////////
     *     //
     *     //                      定位相关
     *     //
     *     /////////////////////////////////////////////////////////////////////////////////
     */
    /**
     * 定位结果的回调
     *
     * @param code
     * @param lat1
     * @param long1
     * @param location
     */
    @Override
    public void onLocationChanged(int code, double lat1, double long1, String location) {
        if (mSwitchLocate.getChecked()) {
            if (0 == code) {
                mTvLocation.setText(location);
                setLocation(location);
            } else {
                mTvLocation.setText(getString(R.string.text_live_lbs_fail));
            }
        } else {
            setLocation("");
        }
    }

    /**
     * 上传定位的结果，设置到开播的信息
     *
     * @param location
     */
    private void setLocation(String location) {
        TCUserMgr.getInstance().setLocation(location, new TCHTTPMgr.Callback() {
            @Override
            public void onSuccess(JSONObject data) {

            }

            @Override
            public void onFailure(int code, final String msg) {
                TCAnchorPrepareActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "设置位置失败 " + msg, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }



    /**
     *     /////////////////////////////////////////////////////////////////////////////////
     *     //
     *     //                      封面图相关
     *     //
     *     /////////////////////////////////////////////////////////////////////////////////
     */

    /**
     * 选择封面图：选择 -> 裁剪 -> 上传到COS
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
//                case CAPTURE_IMAGE_CAMERA:
//                    cropPhoto(mSourceFileUri);
//                    break;
//                case IMAGE_STORE:
//                    String path = TCUtils.getPath(this, data.getData());
//                    if (null != path) {
//                        Log.d(TAG, "cropPhoto->path:" + path);
//                        File file = new File(path);
//                        cropPhoto(Uri.fromFile(file));
//                    }
//                    break;
//                case CROP_CHOOSE:
//                    mUploadingCover = true;
//                    mTvPicTip.setVisibility(View.GONE);
//                    // 上传到 COS
//                    mUploadHelper.uploadPic(mCropFileUri.getPath());
//                    break;
                case REQUEST_CODE_CHOOSE:
                    mUploadingCover = true;
                    mTvPicTip.setVisibility(View.GONE);
                    List<Uri> result = Matisse.obtainResult(data);
                    if (result.size()>0){
                        // 上传到 COS
                        localCoverPath = Utils.getRealPathFromURI(getApplicationContext(),result.get(0));
                        Glide.with(TCAnchorPrepareActivity.this).load(result.get(0)).into(mIvCover);
                    }
                    break;

            }
        }

    }

    private void uploadCoverFile(String filePath){
        if (StringUtils.isNullOrEmpty(filePath)) {
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("user_id",PersonInfoManager.INSTANCE.getUserId());
        BaseOkGoUtils.postOkGoWithFile(TCAnchorPrepareActivity.this,map, UPLOAD,filePath, new ResultListener() {
            @Override
            public void onSucceeded(Object object) {
                try {
                    curretnCoverPath = (String) object;
                    startLive();
                }catch (Exception e){
                    ToastUtils.s(getApplicationContext(),"视频发布失败");
                    e.printStackTrace();
                }
            }
        });
    }










    /**
     *     /////////////////////////////////////////////////////////////////////////////////
     *     //
     *     //                      动态权限检查相关
     *     //
     *     /////////////////////////////////////////////////////////////////////////////////
     */

    private boolean checkPublishPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(TCAnchorPrepareActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(TCAnchorPrepareActivity.this, Manifest.permission.READ_PHONE_STATE)) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(TCAnchorPrepareActivity.this,
                        permissions.toArray(new String[0]),
                        TCConstants.WRITE_PERMISSION_REQ_CODE);
                return false;
            }
        }

        return true;
    }

    private boolean checkScrRecordPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case TCConstants.LOCATION_PERMISSION_REQ_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!TCLocationHelper.getMyLocation(this, this)) {
                        mTvLocation.setText(getString(R.string.text_live_lbs_fail));
                        mSwitchLocate.setChecked(false, false);
                    }
                }
                break;
            case TCConstants.WRITE_PERMISSION_REQ_CODE:
                for (int ret : grantResults) {
                    if (ret != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                mPermission = true;
                break;
            case TCConstants.CAMERA_PERMISSION_REQ_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseImage(true);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.anchor_rb_record_camera:
                mRecordType = TCConstants.RECORD_TYPE_CAMERA;
                break;
            case R.id.anchor_rb_record_screen:
                if (!checkScrRecordPermission()) {
                    Toast.makeText(getApplicationContext(), "当前安卓系统版本过低，仅支持5.0及以上系统", Toast.LENGTH_SHORT).show();
                    mRGRecordType.check(R.id.anchor_rb_record_camera);
                    return;
                }
                try {
                    TCUtils.checkFloatWindowPermission(TCAnchorPrepareActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mRecordType = TCConstants.RECORD_TYPE_SCREEN;
                break;
            default:
                break;
        }
    }

    @Override
    public void onUploadResult(int code, String url) {

    }
}
