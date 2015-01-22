package fr.poucedor.poucedor.provider;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by loic on 22/01/15.
 */
public class MyRetrofitErrorHandler implements ErrorHandler {
    @Override
    public Throwable handleError(RetrofitError cause) {
        Response r = cause.getResponse();
        if (r != null && r.getStatus() == 401) {
            return new UnauthorizedException(cause);
        }
        return cause;
    }

    public class UnauthorizedException extends Throwable {
        public UnauthorizedException(RetrofitError cause) {
        }
    }
}
