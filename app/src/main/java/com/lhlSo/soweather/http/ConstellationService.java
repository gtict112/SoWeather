package com.lhlSo.soweather.http;

import com.lhlSo.soweather.bean.Constellation;
import com.lhlSo.soweather.bean.Result;
import com.lhlSo.soweather.utils.ResponseListenter;
import com.lhlSo.soweather.utils.ResponseProcessUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/21.
 */

public class ConstellationService {
    private final OkHttpClient client = new OkHttpClient();
    private Result result = new Result();

    public ConstellationService() {
    }

    /**
     * 星座运势
     */
    public void getConstellationService(final ResponseListenter<Constellation> a, final String consName, String type) {
        String Key = "57b13080621ff4e71fc2695e051b8a07";
        String url = "http://web.juhe.cn:8080/constellation/getAll?" + "consName=" + consName + "&type=" + type +
                "&key=" + Key;
        Request request = new Request.Builder()
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
                final Result<Constellation> res = ResponseProcessUtil.getConstellation(response);
                try {
                    if (res.isSuccess()) {
                        a.onReceive(res);
                    } else {
                        res.setSuccess(false);
                        res.setErrorMessage("获取星座运势失败");
                        a.onReceive(res);
                    }
                } catch (Exception e1) {
                    res.setSuccess(false);
                    res.setErrorMessage("获取星座运势异常");
                    e1.printStackTrace();
                }
            }
        });
    }
}