package com.fly.jiejing.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fly.jiejing.R;
import com.fly.jiejing.request.StringPostRequest;
import com.fly.jiejing.units.Constant;
import com.fly.jiejing.units.MainApplication;
import com.fly.jiejing.units.UrlUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import java.text.SimpleDateFormat;
import java.util.Date;

//分享Activity
public class MyShareActivity extends Activity implements View.OnClickListener {
    private ImageView share_back;
    private RelativeLayout sinaPart;
    private RelativeLayout qqPart;
    private final UMSocialService mController = UMServiceFactory
            .getUMSocialService(Constant.DESCRIPTOR);
    private SHARE_MEDIA mPlatform = SHARE_MEDIA.SINA;
    private SHARE_MEDIA qPlatform = SHARE_MEDIA.QZONE;
    private String user_number;//用户名字
    private SharedPreferences sp;
    private RequestQueue requestQueue;
    private Date now;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_share);
        initData();
        // 配置需要分享的相关平台
        configPlatforms();
        // 设置分享的内容
        setShareContent();
        share_back.setOnClickListener(this);
        sinaPart.setOnClickListener(this);
        qqPart.setOnClickListener(this);
    }

    /**
     * 配置分享平台参数</br>
     */
    private void configPlatforms() {
        // 添加QQ、QZone平台
        addQQQZonePlatform();
        // 添加新浪SSO授权
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        // 添加腾讯微博SSO授权
        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
    }

    /**
     * @return
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     * image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     * 要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     * : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     */
    private void addQQQZonePlatform() {
        String appId = "1104908616";
        String appKey = "CHU9M9VMdOdyDzN4";
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this,
                appId, appKey);
        qqSsoHandler.setTargetUrl("http://t.cn/8siFiZZ");
        qqSsoHandler.addToSocialSDK();
        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, appId, appKey);
        qZoneSsoHandler.addToSocialSDK();
    }

    /**
     * 根据不同的平台设置不同的分享内容</br>
     */
    private void setShareContent() {
        // 配置SSO新浪
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this,
                "1104908616", "CHU9M9VMdOdyDzN4");
        qZoneSsoHandler.addToSocialSDK();
        mController.setShareContent("星星家洁，让您生活的更加干净。http://t.cn/8siFiZZ");
        // 设置分享图片, 参数2为图片的url地址
        mController.setShareMedia(new UMImage(MyShareActivity.this,
                UrlUtils.IMG_URL + "images/app1.png"));
    }

    /**
     * 直接分享，底层分享接口。如果分享的平台是新浪、腾讯微博、豆瓣、人人，则直接分享，无任何界面弹出； 其它平台分别启动客户端分享</br>
     */
    private void directShare() {
        mController.directShare(this, mPlatform, new SocializeListeners.SnsPostListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                String showText = "分享成功";
                addScore();
                if (eCode != StatusCode.ST_CODE_SUCCESSED) {
                    showText = "分享失败 [" + eCode + "]";
                }
                Toast.makeText(MyShareActivity.this, showText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //分享到qq空间
    private void directQQShare() {
        mController.directShare(this, qPlatform, new SocializeListeners.SnsPostListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                String showText = "分享成功";
                if (user_number != null) {
                    //传递数据
//                    SharedPreferences sp=getSharedPreferences(Constant.SP_FILE_NAME, MODE_PRIVATE);
//                    SharedPreferences.Editor editor=sp.edit();
//                    editor.putInt("PHONE_CODE",code+10);
//                    editor.commit();
                    addScore();
                }
                if (eCode != StatusCode.ST_CODE_SUCCESSED) {
                    showText = "分享失败 [" + eCode + "]";
                }
                Toast.makeText(MyShareActivity.this, showText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //数据初始化
    public void initData() {

        requestQueue = MainApplication.getApplication().getQueue();
        share_back = (ImageView) findViewById(R.id.share_back);
        sinaPart = (RelativeLayout) findViewById(R.id.sinaPart);
        qqPart = (RelativeLayout) findViewById(R.id.qqPart);
        sp = getSharedPreferences(Constant.SP_FILE_NAME, MODE_PRIVATE);
        user_number = sp.getString("PHONE_NUMBER", null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_back:
                finish();
                break;
            case R.id.sinaPart:
                directShare();
                break;
            case R.id.qqPart:
                directQQShare();
                break;
        }
    }

    public static String getDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    //分享增加积分
    public void addScore() {
        now = new Date();
        StringPostRequest postRequest = new StringPostRequest(UrlUtils.USER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        postRequest.putParams("action", "add");
        postRequest.putParams("userid", MainApplication.getApplication().getUser().getUser_id());
        postRequest.putParams("date", "" + getDate(now));
        requestQueue.add(postRequest);
    }
}
