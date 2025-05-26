package com.maneger.appbanhang.retrofit;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
public class Authorization implements Interceptor {
    private String authorToken;
    public Authorization(String authorToken) {
        this.authorToken = authorToken;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request orignal = chain.request();
        Request.Builder builder = orignal.newBuilder()
                .header("Authorization", "Bearer "+authorToken)
                .method(orignal.method(), orignal.body());
        Request request = builder.build();
        return chain.proceed(request);
    }
}
