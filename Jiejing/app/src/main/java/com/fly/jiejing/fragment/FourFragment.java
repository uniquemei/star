package com.fly.jiejing.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fly.jiejing.R;
import com.fly.jiejing.activity.ExchangeActivity;
import com.fly.jiejing.activity.MyCommonPlaceActivity;
import com.fly.jiejing.activity.MyCommonUserActivity;
import com.fly.jiejing.activity.MyInviteActivity;
import com.fly.jiejing.activity.MyMoreFunctionActivity;
import com.fly.jiejing.activity.MyOrderActivity;
import com.fly.jiejing.activity.MyOrderCompleteAct;
import com.fly.jiejing.activity.MyShareActivity;
import com.fly.jiejing.activity.MySuggestionActivity;
import com.fly.jiejing.activity.UserLoginActivity;
import com.fly.jiejing.activity.UserLoginSuccsessActivity;
import com.fly.jiejing.activity.db.DBHelper;
import com.fly.jiejing.entity.User;
import com.fly.jiejing.request.ImageLoaderUtil;
import com.fly.jiejing.request.StringPostRequest;
import com.fly.jiejing.units.Constant;
import com.fly.jiejing.units.FileUitlity;
import com.fly.jiejing.units.MainApplication;
import com.fly.jiejing.units.UrlUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class FourFragment extends Fragment implements View.OnClickListener {
    private FrameLayout two;
    private Intent intent;
    private TextView user_name;
    private String phoneNumber;
    private SharedPreferences sp;
    private ImageView user_head;
    String photoPath;
    private int code;//积分
    private TextView jifen;
    private TextView coupon;//优惠券
    private TextView user_balance;//账户余额

    private RequestQueue requestQueue;
    private User guser;

    public static final int TAKE_FROM_CAMERE = 1;// 拍照
    public static final int TAKE_FROM_GALLEY = 2;// 从相册取
    public static final int RESULT_PHOTO = 3;//

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.four_layout, null);
        jifen = (TextView) view.findViewById(R.id.integral);
        two = (FrameLayout) view.findViewById(R.id.two);
        two.setOnClickListener(this);
        user_name = (TextView) view.findViewById(R.id.user_name);

        coupon = (TextView) view.findViewById(R.id.coupon);//优惠券

        user_head = (ImageView) view.findViewById(R.id.user_head);
        user_balance = (TextView) view.findViewById(R.id.user_balance);
        requestQueue = MainApplication.getApplication().getQueue();
        sp = getActivity().getSharedPreferences(Constant.SP_FILE_NAME, getActivity().MODE_PRIVATE);

        view.findViewById(R.id.order).setOnClickListener(this);
        view.findViewById(R.id.commen_user).setOnClickListener(this);
        view.findViewById(R.id.commen_address).setOnClickListener(this);
        view.findViewById(R.id.share).setOnClickListener(this);
        view.findViewById(R.id.invite).setOnClickListener(this);
        view.findViewById(R.id.suggestion).setOnClickListener(this);
        view.findViewById(R.id.more).setOnClickListener(this);
        view.findViewById(R.id.exchange).setOnClickListener(this);
        view.findViewById(R.id.completOrder).setOnClickListener(this);
        user_head.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.two:
                //调试，完成后改为UserLoginActivity
                if (phoneNumber == null) {
                    intent = new Intent(getActivity(), UserLoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), UserLoginSuccsessActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.order:
                if (phoneNumber == null) {
                    intent = new Intent(getActivity(), UserLoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), MyOrderActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.completOrder:
                if (phoneNumber == null) {
                    intent = new Intent(getActivity(), UserLoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), MyOrderCompleteAct.class);
                    startActivity(intent);
                }
                break;
            case R.id.commen_user:
                if (phoneNumber == null) {
                    intent = new Intent(getActivity(), UserLoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), MyCommonUserActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.commen_address:
                if (phoneNumber == null) {
                    intent = new Intent(getActivity(), UserLoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), MyCommonPlaceActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.share:
                if (phoneNumber == null) {
                    intent = new Intent(getActivity(), UserLoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), MyShareActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.invite:
                if (phoneNumber == null) {
                    intent = new Intent(getActivity(), UserLoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), MyInviteActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.suggestion:
                if (phoneNumber == null) {
                    intent = new Intent(getActivity(), UserLoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), MySuggestionActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.more:
                intent = new Intent(getActivity(), MyMoreFunctionActivity.class);
                startActivity(intent);
                break;
            case R.id.exchange:
                if (phoneNumber == null) {
                    intent = new Intent(getActivity(), UserLoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), ExchangeActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.user_head:
                if (phoneNumber == null) {
                    intent = new Intent(getActivity(), UserLoginActivity.class);
                    startActivity(intent);
                } else {
                    //设置popWindow的布局
                    View popWindow = LayoutInflater.from(getActivity()).inflate(R.layout.people_head_popwindow, null);
                    popWindow.findViewById(R.id.paizhao).setOnClickListener(this);
                    popWindow.findViewById(R.id.choose_img).setOnClickListener(this);
                    //设置popWindow出现时后面变暗
                    final WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                    lp.alpha = 0.8f;
                    getActivity().getWindow().setAttributes(lp);

                    final PopupWindow window = new PopupWindow(popWindow, ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
//                window.setBackgroundDrawable(getActivity().getBaseContext()
//                        .getResources().getDrawable(android.R.color.white));

                    // 点击别处pop不能消失
                    window.setFocusable(false);
                    window.setOutsideTouchable(true);
                    window.setBackgroundDrawable(new ColorDrawable());

                    popWindow.findViewById(R.id.quxiao).setOnClickListener(
                            new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    window.dismiss();

                                }
                            });

                    window.setAnimationStyle(R.style.popwindow_animal_style);
                    window.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                    //popWindow消失时透明的的设置
                    window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            lp.alpha = 1.0f;//透明度
                            getActivity().getWindow().setAttributes(lp);
                            //请求获取数据
                        }
                    });
                }

                break;
            case R.id.paizhao:
                // 打开照相机
                intent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                File parent = FileUitlity.getInstance(getActivity()).makeDir("jre_head");
                photoPath = parent.getPath() + File.separator
                        + System.currentTimeMillis() + ".png";
                //photoPath=subS(photoPath1);

                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(photoPath)));
                startActivityForResult(intent, TAKE_FROM_CAMERE);
                break;
            case R.id.choose_img:
                // 打开相册
                intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, TAKE_FROM_GALLEY);
                break;
            default:
                break;
        }
    }

    // 照片的选择
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != getActivity().RESULT_OK)
            return;
        switch (requestCode) {
            case TAKE_FROM_CAMERE:
                if (null != photoPath) {
                    Log.i("photoPath", photoPath);
                    startPhotoZoom(Uri.fromFile(new File(photoPath)));

                }
                break;
            case TAKE_FROM_GALLEY:
                Cursor cursor = getActivity().getContentResolver().query(data.getData(),
                        new String[]{MediaStore.Images.Media.DATA}, null, null,
                        null);
                cursor.moveToFirst();
                photoPath = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();
                startPhotoZoom(Uri.fromFile(new File(photoPath)));
                // imag.setImageURI(Uri.fromFile(new File(photoPath)));
                break;
            case RESULT_PHOTO:
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    final Bitmap b = bundle.getParcelable("data");
                    user_head.setImageBitmap(b);
                    // 询问是否需要上传照片
                    AlertDialog.Builder bulid = new AlertDialog.Builder(getActivity());
                    bulid.setTitle("提示信息").setMessage("上传图片？");
                    bulid.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {
                                    user_head.setImageBitmap(b);
                                    updatePhoto(convertBitmap2String(b));
                                    arg0.dismiss();
                                }
                            });
                    bulid.setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                }
                            });
                    AlertDialog alert = bulid.create();
                    alert.setCancelable(false);
                    alert.show();
                    // 如果是开始上传，那就将照片上传
                }
                break;
            default:
                break;
        }
    }

    // 将头像转换成base64
    public String convertBitmap2String(Bitmap b) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bf = out.toByteArray();
        byte[] stringByte = Base64.encode(bf, Base64.DEFAULT);

        return new String(stringByte);

    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_PHOTO);
    }

    @Override
    public void onStart() {
        super.onStart();
        phoneNumber = sp.getString("PHONE_NUMBER", null);
        if (phoneNumber != null && phoneNumber.length() > 0) {
            user_name.setText(phoneNumber.substring(0, 7) + "****");
            initData();
        } else {   //          Toast.makeText(getActivity(), "还没有注册", Toast.LENGTH_SHORT).show();
            user_name.setText("马上注册，享受优惠");
            user_balance.setText("0");
            jifen.setText("0");
            coupon.setText("0");
            user_head.setImageResource(R.mipmap.user_center_icon);
        }
    }

    //查找当前用户的所有信息，包括积分、优惠券、余额、头像等
    public void initData() {
        StringPostRequest postRequest = new StringPostRequest(UrlUtils.USER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                List<User> gData = gson.fromJson(s, new TypeToken<ArrayList<User>>() {
                }.getType());
                if (gData != null && gData.size() > 0) {
                    User user = gData.get(0);
                    user_balance.setText(user.getBalance());
                    jifen.setText(user.getScore());
                    coupon.setText(user.getCoupon_num() + "");
                    //获得图片
                    String stringUrl = UrlUtils.ROOT_URL + user.getPic();
                    ImageLoaderUtil.display(stringUrl, user_head);
                    //存入本地数据库
//                    Dao<User,Integer> userDao= DBHelper.getInstance(getActivity()).getUserDao();
//                    try {
//                        userDao.create(user);
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "连接服务器失败", Toast.LENGTH_SHORT).show();
            }
        });
        postRequest.putParams("action", "select");
        postRequest.putParams("userid", phoneNumber);
        MainApplication.getApplication().getQueue().add(postRequest);
    }

    //将用户头像上传
    public void updatePhoto(final String photoStr) {
        StringPostRequest postRequest = new StringPostRequest(UrlUtils.USER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.equals("0")) {
                    Toast.makeText(getActivity(), "上传失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "连接服务器失败", Toast.LENGTH_SHORT).show();
            }
        });
        postRequest.putParams("action", "pic");
        postRequest.putParams("userid", phoneNumber);
        postRequest.putParams("pic", photoStr);
        MainApplication.getApplication().getQueue().add(postRequest);
    }
}
