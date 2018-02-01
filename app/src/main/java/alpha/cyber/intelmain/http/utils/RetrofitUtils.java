package alpha.cyber.intelmain.http.utils;


import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import alpha.cyber.intelmain.http.BaseURL;
import alpha.cyber.intelmain.http.ResponseConverterFactory;
import alpha.cyber.intelmain.util.AppSharedPreference;
import alpha.cyber.intelmain.util.Log;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by huxin on 16/6/12.
 */
public class RetrofitUtils {

    private static Retrofit instance;
    private static Retrofit pageInstance;

    public static <T> T createService(Class<T> clazz){
        if(null==instance){
            synchronized (RetrofitUtils.class){
                if(null==instance){
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                    OkHttpClient client = new OkHttpClient.Builder()
                            .addNetworkInterceptor(new StethoInterceptor())
                            .addInterceptor(new TokenInterceptor())
                            .addInterceptor(logging)
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .build();

                    instance = new Retrofit.Builder()
                            .baseUrl(BaseURL.getBaseURL())
                            .addConverterFactory(ResponseConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(client)
                            .build();
                }
            }
        }

        return instance.create(clazz);
    }

    public static <T> T createPageService(Class<T> clazz, int numInPage, int page){
        if(null==pageInstance){
            synchronized (RetrofitUtils.class){
                if(null==pageInstance){
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                    OkHttpClient client = new OkHttpClient.Builder()
                            .addNetworkInterceptor(new StethoInterceptor())
                            .addInterceptor(new TokenPageInterceptor(numInPage,page))
                            .addInterceptor(logging)
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .build();

                    pageInstance = new Retrofit.Builder()
                            .baseUrl(BaseURL.getBaseURL())
                            .addConverterFactory(ResponseConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(client)
                            .build();
                }
            }
        }

        return pageInstance.create(clazz);
    }

    public static <T> T createNoTokenService(Class<T> clazz){
        if(null==instance){
            synchronized (RetrofitUtils.class){
                if(null==instance){
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY);

                    OkHttpClient client = new OkHttpClient.Builder()
                            .addNetworkInterceptor(new StethoInterceptor())
                            .addInterceptor(logging)
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .build();

                    instance = new Retrofit.Builder()
                            .baseUrl(BaseURL.getBaseURL())
                            .addConverterFactory(ResponseConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(client)
                            .build();
                }
            }
        }

        return instance.create(clazz);
    }

    private static class TokenInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder newBuilder = chain.request().newBuilder();
            HttpUrl url = chain.request().url();//TODO
//            newBuilder.url(url+"?token="+ AppSharedPreference.getInstance().getLoginToken());
            Log.v("the self_token is "+ AppSharedPreference.getInstance().getLoginToken());
            return chain.proceed(newBuilder.build());
        }
    }

    private static class TokenPageInterceptor implements Interceptor {

        public TokenPageInterceptor(int num,int page){
            numInPage = num;
            pageIndex = page;
        }

        int numInPage;
        int pageIndex;

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder newBuilder = chain.request().newBuilder();
            HttpUrl url = chain.request().url();
            //newBuilder.url(url+"?token="+ AppSharedPreference.getInstance().getLoginToken());
            String lastUrl = url+"?pageSize="+numInPage+"&pageIndex="+pageIndex+"&token="+ AppSharedPreference.getInstance().getLoginToken();
            //String lastUrl = url+"?pageSize="+numInPage+"&pageIndex="+pageIndex+"&token="+ "aa94edd9-59fd-4e51-87df-af2fd7b3a31d";
            Log.v(lastUrl);
            newBuilder.url(lastUrl);
            //newBuilder.url(url+"?token="+ "aa94edd9-59fd-4e51-87df-af2fd7b3a31d");
            return chain.proceed(newBuilder.build());
        }
    }
}
