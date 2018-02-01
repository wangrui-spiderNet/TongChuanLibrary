package alpha.cyber.intelmain.http;

import java.net.SocketTimeoutException;

import alpha.cyber.intelmain.MyApplication;
import alpha.cyber.intelmain.util.NetworkUtils;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by huxin on 16/6/13.
 */
public abstract class DefaultSubscriber<T> extends Subscriber<T> {

    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        Throwable throwable = e;
        //获取最根源的异常
        while (throwable.getCause() != null) {
            e = throwable;
            throwable = throwable.getCause();
        }
        //无网络连接
        if (!NetworkUtils.isNetworkAvailable(MyApplication.getInstance())
                || e instanceof SocketTimeoutException) {
            onFailure(BaseURL.APP_EXCEPTION_HTTP_TIMEOUT, e.getMessage());
        } else if (e instanceof BusinessException) {
            //业务异常
            BusinessException exception = (BusinessException) e;
            onFailure(exception.getCode(), exception.getMessage());
        } else if (e instanceof HttpException) {
            //网络连接异常
            HttpException httpException = (HttpException) e;
            onFailure(String.valueOf(httpException.code()),
                    httpException.getMessage());
        } else {
            //其他异常
            onFailure(BaseURL.APP_EXCEPTION_HTTP_OTHER, e.getMessage());
        }
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    public abstract void onSuccess(T t);

    public abstract void onFailure(String errorCode, String errorMessage);
}
