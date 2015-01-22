package fr.poucedor.poucedor.provider;

import android.util.Log;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 * Created by loic on 09/01/15.
 */
public interface PoucedorService {
    @GET("/api/v1/teams/simple")
    void teams(@Header("x-access-token") String authorization, Callback<List<Team>> callback);

    @POST("/api/v1/login")
    LoginResponse login(@Body LoginRequest request);

}
