package com.example.demo.configs;

import com.example.demo.configs.properties.AppProperties;
import com.example.demo.network.services.GiantSmsApiService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class GiantSmsRetroConfig {

    @Autowired
    private AppProperties appProperties;

    private static final int TIMEOUT = 60;

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    OkHttpClient giantSmsApiHttpClient() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(TIMEOUT, TimeUnit.SECONDS);
        httpClient.connectTimeout(TIMEOUT, TimeUnit.SECONDS);

        try {

            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
                                throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
                                throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            httpClient.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            httpClient.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception e) {
            log.warn("Exception while configuring IgnoreSslCertificate" + e, e);
        }

        httpClient.addInterceptor(new okhttp3.Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Request original = chain.request();
                okhttp3.Request.Builder requestBuilder = null;
                try {
                    requestBuilder = original.newBuilder()
                            .header("Authorization", Credentials.basic(appProperties.getGiantSmsApiUsername(),
                                    appProperties.getGiantSmsApiPassword()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                okhttp3.Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
        return httpClient.build();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    Retrofit provideGiantSmsApi() throws IOException {
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        return new Retrofit.Builder()
                .baseUrl(appProperties.getGiantSmsApiBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(giantSmsApiHttpClient())
                .build();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    GiantSmsApiService provideGiantSmsApiService() throws IOException {
        return provideGiantSmsApi().create(GiantSmsApiService.class);
    }
}
