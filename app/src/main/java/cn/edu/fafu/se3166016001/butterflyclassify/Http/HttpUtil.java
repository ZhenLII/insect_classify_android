package cn.edu.fafu.se3166016001.butterflyclassify.Http;

import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {
    private static OkHttpClient client = null;

    public static OkHttpClient getInstance() {
        if (client == null) {
            synchronized (HttpUtil.class) {
                if (client == null){
                    client = new OkHttpClient().newBuilder().addInterceptor(new TokenHeaderInterceptor())
                            .connectTimeout(20,TimeUnit.DAYS.SECONDS)
                            .readTimeout(20,TimeUnit.DAYS.SECONDS)
                            .build();

                }
            }
        }
        return client;
    }
    /**
     * Get请求
     *
     * @param url
     * @param callback
     */
    public static void doGet(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = getInstance().newCall(request);
        call.enqueue(callback);
    }
    /**
     * Post请求发送键值对数据
     *
     * @param url
     * @param mapParams
     * @param callback
     */
    public static void doPost(String url, Map<String, String> mapParams, Callback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : mapParams.keySet()) {
            builder.add(key, mapParams.get(key));
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        Call call = getInstance().newCall(request);
        call.enqueue(callback);
    }
    /**
     * Post请求发送JSON数据
     *
     * @param url
     * @param jsonParams
     * @param callback
     */
    public static void doPost(String url, String jsonParams, Callback callback) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8")
                , jsonParams);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = getInstance().newCall(request);
        call.enqueue(callback);
    }

    /**
     * Post请求发送数据及文件数据
     *
     * @param url
     * @param mapParams
     * @param filePaths
     * @param callback
     */
    public static void doPost(String url, Map<String, String> mapParams, List<String> filePaths, Callback callback) {
        MediaType MutilPart_Form_Data = MediaType.parse("multipart/form-data; charset=utf-8");
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        for (String key : mapParams.keySet()) {
            String str = mapParams.get(key);
            requestBodyBuilder.addFormDataPart(key,str );
        }
        if(filePaths !=null && filePaths.size() > 0) {
            for (int i = 0; i < filePaths.size(); i++) {
                File file = new File(filePaths.get(i));
                requestBodyBuilder.addFormDataPart("files", file.getName(), RequestBody.create(MutilPart_Form_Data, new File(filePaths.get(i))));
            }
        }
        RequestBody requestBody = requestBodyBuilder.build();
        Request request = new Request.Builder()
                .url( url)
                .post(requestBody)
                .build();

        Call call = getInstance().newCall(request);
        call.enqueue(callback);
    }
}
