package cn.edu.fafu.se3166016001.butterflyclassify.Http;

import java.io.IOException;

import cn.edu.fafu.se3166016001.butterflyclassify.AndroidApplication;
import cn.edu.fafu.se3166016001.butterflyclassify.Model.User;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenHeaderInterceptor implements Interceptor {

    private AndroidApplication application = AndroidApplication.getInstance();
    @Override
    public Response intercept(Chain chain) throws IOException {

        User user = application.getUser();
        if(user == null){
            return chain.proceed(chain.request());
        }
        String token = user.getToken();
        Request request = chain.request();
        Request updateRequest = request.newBuilder().header("token",token).build();
        return chain.proceed(updateRequest);
    }
}
