package com.example.demo.network.repositories;

import com.example.demo.dtos.GiantSmsSendSmsPayload;
import com.example.demo.dtos.GiantSmsSendSmsResponse;
import com.example.demo.network.ApiRequest;
import com.example.demo.network.services.GiantSmsApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import java.util.function.BiConsumer;

@Service
public class GiantSmsApiRepository {

    private static final Logger logger = LoggerFactory.getLogger(GiantSmsApiRepository.class);

    @Autowired
    private ApiRequest apiRequest;
    @Autowired
    private GiantSmsApiService apiService;

    public void sendSms(GiantSmsSendSmsPayload payload, BiConsumer<Object, ApiRequest.Status> result) {
        Call<GiantSmsSendSmsResponse> call = apiService.sendSms(payload);
        apiRequest.fireRequest(call,
                (successCall, o) -> {
                    result.accept(o, ApiRequest.Status.Success);
                }, (errorCall, errorMessage) -> {
                    result.accept(errorMessage, ApiRequest.Status.Error);
                }, (failedCall, exception) -> {
                    result.accept(exception, ApiRequest.Status.Failed);
                });
    }
}
