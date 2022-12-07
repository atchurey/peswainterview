package com.example.demo.network.services;

import com.example.demo.dtos.GiantSmsSendSmsPayload;
import com.example.demo.dtos.GiantSmsSendSmsResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GiantSmsApiService {

    @POST("v1/send")
    Call<GiantSmsSendSmsResponse> sendSms(@Body GiantSmsSendSmsPayload payload);

}
