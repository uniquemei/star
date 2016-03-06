package com.fly.jiejing.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fly.jiejing.R;
import com.fly.jiejing.units.Constant;
import com.fly.jiejing.units.UrlUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class MyInviteActivity extends Activity implements View.OnClickListener {
    private ImageView invite_back;
    private RelativeLayout messagePart;
    private RelativeLayout weiboPart;
    private RelativeLayout weiXinpart2;
    private RelativeLayout friendsPart2;
    private final UMSocialService mController = UMServiceFactory
            .getUMSocialService(Constant.DESCRIPTOR);
    private SHARE_MEDIA messagePlatform = SHARE_MEDIA.SMS;//短信
    private SHARE_MEDIA mPlatform = SHARE_MEDIA.SINA;//新浪
    private SHARE_MEDIA wPlatform = SHARE_MEDIA.WEIXIN;//微信
    private SharedPreferences sp;
    private String userPhone;
    private String initCode;//邀请码
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_invite);
        initView();
        userPhone = sp.getString("PHONE_NUMBER", null);
        if (userPhone != null) {
            initCode = userPhone.substring(5);

            // 设置分享的内容
            setShareContent();
        }

        // 添加短信平台
        addSMS();
       // Toast.makeText(getApplicationContext(), "邀请码：" + initCode, Toast.LENGTH_SHORT).show();
        invite_back.setOnClickListener(this);
        messagePart.setOnClickListener(this);
        weiboPart.setOnClickListener(this);
        weiXinpart2.setOnClickListener(this);
        friendsPart2.setOnClickListener(this);
    }

    //信息的初始化
    public void initView() {
        invite_back = (ImageView) findViewById(R.id.invite_back);
        messagePart = (RelativeLayout) findViewById(R.id.messagePart);
        sp = getSharedPreferences(Constant.SP_FILE_NAME, MODE_PRIVATE);
        weiboPart = (RelativeLayout) findViewById(R.id.weiboPart);
        weiXinpart2 = (RelativeLayout) findViewById(R.id.weixinPart2);
        friendsPart2 = (RelativeLayout) findViewById(R.id.friendsPart2);
    }

    /**
     * 添加短信平台</br>
     */
    private void addSMS() {
        // 添加新浪SSO授权
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        // 添加短信
        SmsHandler smsHandler = new SmsHandler();
        smsHandler.addToSocialSDK();
        // 添加微信、微信朋友圈平台
        addWXPlatform();
        // 添加腾讯微博SSO授权
        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

    }

    /**
     * 根据不同的平台设置不同的分享内容</br>
     */
    private void setShareContent() {
        // 设置短信分享内容
        SmsShareContent sms = new SmsShareContent();
        sms.setShareContent("星星家洁，让您的生活和更加干净，输入好友邀请码" + initCode + "获得10元的优惠券。http://t.cn/8siFiZZ");
        // sms.setShareImage(urlImage);
        mController.setShareMedia(sms);
        // 设置分享图片, 参数2为图片的url地址
//        mController.setShareMedia(new UMImage(MyInviteActivity.this,
//                UrlUtils.ROOT_URL+ "images/app1.png"));

        // 配置SSO权限 新浪
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent
                .setShareContent("星星家洁，让您的生活和更加干净，输入好友邀请码" + initCode + "获得10元的优惠券。http://t.cn/8siFiZZ");
        sinaContent.setShareImage(new UMImage(getApplicationContext(), R.mipmap.app1));
        mController.setShareMedia(sinaContent);

        UMImage urlImage = new UMImage(getApplicationContext(),
                UrlUtils.ROOT_URL + "images/app1.png");
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent
                .setShareContent("星星家洁，让您的生活和更加干净，输入好友邀请码" + initCode + "获得10元的优惠券。http://t.cn/8siFiZZ");
        weixinContent.setTitle("星星家洁");
        weixinContent.setTargetUrl("http://t.cn/8siFiZZ");
        weixinContent.setShareMedia(urlImage);
        mController.setShareMedia(weixinContent);
    }

    /**
     * @return
     * @功能描述 : 添加微信平台分享
     */
    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appId = "wxe797f80df8856786";
        String appSecret = "4be04d8f14e3cce0308652a76837f325";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(getApplicationContext(), appId, appSecret);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(getApplicationContext(), appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    /**
     * 直接分享，底层分享接口。如果分享的平台是新浪、腾讯微博、豆瓣、人人，则直接分享，无任何界面弹出； 其它平台分别启动客户端分享</br>
     */
    private void directMShare() {
        mController.directShare(this, messagePlatform, new SocializeListeners.SnsPostListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                String showText = "发送成功";
                if (eCode != StatusCode.ST_CODE_SUCCESSED) {
                    showText = "发送失败 [" + eCode + "]";
                }
                Toast.makeText(MyInviteActivity.this, showText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //新浪微博
    private void directShare() {
        mController.directShare(this, mPlatform, new SocializeListeners.SnsPostListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                String showText = "分享成功";
                if (eCode != StatusCode.ST_CODE_SUCCESSED) {
                    showText = "分享失败 [" + eCode + "]";
                }
                Toast.makeText(MyInviteActivity.this, showText, Toast.LENGTH_SHORT).show();
            }
        });
    }


    //微信
    private void directWShare() {
        mController.directShare(this, wPlatform, new SocializeListeners.SnsPostListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                String showText = "分享成功";
                if (eCode != StatusCode.ST_CODE_SUCCESSED) {
                    showText = "分享失败 [" + eCode + "]";
                }
                Toast.makeText(MyInviteActivity.this, showText, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invite_back:
                finish();
                break;
            case R.id.messagePart:
                if (userPhone != null) {
                    directMShare();
                } else {
                    Toast.makeText(getApplicationContext(), "还没有注册", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.weiboPart:
                if (userPhone != null) {
                    directShare();
                } else {
                    Toast.makeText(getApplicationContext(), "还没有注册", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.weixinPart2:
                if (userPhone != null) {
                    directWShare();
                } else {
                    Toast.makeText(getApplicationContext(), "还没有注册", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.friendsPart2:
                if (userPhone != null) {
                    directWShare();
                } else {
                    Toast.makeText(getApplicationContext(), "还没有注册", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
