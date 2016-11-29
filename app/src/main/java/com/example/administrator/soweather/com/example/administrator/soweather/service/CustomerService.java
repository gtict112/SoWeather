package com.example.administrator.soweather.com.example.administrator.soweather.service;
import com.example.administrator.soweather.com.example.administrator.soweather.mode.Result;
import com.example.administrator.soweather.com.example.administrator.soweather.utils.ResponseListenter;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/10.
 */

public class CustomerService {
    private final OkHttpClient client = new OkHttpClient();
    private Result<String> result = new Result<String>();

    public CustomerService() {
    }

    public Result<String> getCustomerService(String url, String droph, final ResponseListenter<String> a) {
        url = url + droph;
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.setErrorMessage(e.toString());
                result.setSuccess(false);
                try {
                    a.onReceive(result);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Result<String> res = new Result<String>();
               //{"text":"我不会说英语的啦，你还是说中文吧。","code":100000}
                try {
                    JSONObject jsonObjecty = new JSONObject(response.body().string());
                    System.out.print(jsonObjecty);
                    if (jsonObjecty.optString("code").equals("100000")) {
                        res.setSuccess(true);
                        res.setData(jsonObjecty.optString("text"));
                    }
                    else{
                        res.setSuccess(false);
                        res.setErrorMessage("发送失败,请稍后再试");
                    }
                } catch (JSONException e) {
                    res.setSuccess(false);
                    res.setErrorMessage("发送失败,请稍后再试");
                    e.printStackTrace();
                }
                try {
                    if (res.isSuccess()) {
                        a.onReceive(res);
                    } else {
                        a.onReceive(res.setErrorMessage("获取数据失败"));
                    }
                } catch (Exception e1) {
                    res.setSuccess(false);
                    res.setErrorMessage("解析数据失败");
                    e1.printStackTrace();
                }
            }
        });
        return null;
    }
}
