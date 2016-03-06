package yu.cleaner.util;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/5.
 */
public class StringPostRequest extends StringRequest {

  
    // 重构函数利用POST请求
    public StringPostRequest(String url, Response.Listener<String> listener,
                             Response.ErrorListener errorListener) {
        super(Request.Method.POST, url, listener, errorListener);
        initMap();
    }

    private Map<String, String> params;

    @Override
    // 用于POST请求查找数据（自动调用）
    protected Map<String, String> getParams() throws AuthFailureError {
        // TODO Auto-generated method stub
        return params;
    }

    // 初始化数据
    private void initMap() {
        // TODO Auto-generated method stub
        params = new HashMap<String, String>();

    }

    // 往里面放数据
    public void putParams(String key, String value) {
        // TODO Auto-generated method stub
        this.params.put(key, value);
    }
}
