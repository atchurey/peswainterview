package com.example.demo.network;

import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import java.util.function.BiConsumer;

@Component
public class ApiRequest {

    public <T> void fireRequest(Call<T> call,
                                BiConsumer<Call<T>, Object> onSuccess,
                                BiConsumer<Call<T>, Object> onError,
                                BiConsumer<Call<T>, Exception> onFailure) {
        try {

            Response response = call.execute();

            if (response.isSuccessful()) {
                onSuccess.accept(call, response.body());
            } else {
                if (response.errorBody() != null)
                    onError.accept(call, response.errorBody().string());
            }
        } catch (Exception e) {
            e.printStackTrace();
            onFailure.accept(call, e);
        }
    }

    public enum Status {Success, Error, Failed}
}
