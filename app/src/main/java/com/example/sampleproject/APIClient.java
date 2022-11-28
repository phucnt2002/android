package com.example.sampleproject;

import java.io.IOException;
import java.security.cert.CertificateException;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;


import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static Retrofit retrofit = null;
    private static String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJLcHRXNWJCcTlsRGliY2s5NHI3TldHQVl0SHBrUFI3N1A4V0hMWDVIX1E0In0.eyJleHAiOjE2Njk2OTI4MzYsImlhdCI6MTY2OTYwNjQzNiwiYXV0aF90aW1lIjoxNjY5NjA2NDM2LCJqdGkiOiI4ZmQ3ZDVlNS0yM2UxLTQzZTAtOTFjZC1iMjRhMWNhZjFjNTgiLCJpc3MiOiJodHRwczovLzEwMy4xMjYuMTYxLjE5OS9hdXRoL3JlYWxtcy9tYXN0ZXIiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiMTAxZGQ1MmMtMjNiYS00ZjM4LWExMjQtYjc4MGUxYjVhODFiIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoib3BlbnJlbW90ZSIsInNlc3Npb25fc3RhdGUiOiI2YTY5MDdmOC0wMTc1LTRlMTEtYjAxMy0yYTJkMjAwYjBjYzMiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHBzOi8vbG9jYWxob3N0IiwiaHR0cHM6Ly8xMDMuMTI2LjE2MS4xOTkiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDp1c2VycyIsInJlYWQ6bG9ncyIsInJlYWQ6bWFwIiwicmVhZDpydWxlcyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJzaWQiOiI2YTY5MDdmOC0wMTc1LTRlMTEtYjAxMy0yYTJkMjAwYjBjYzMiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInByZWZlcnJlZF91c2VybmFtZSI6InVzZXIxIn0.lYZpWfasjyz-AqfQdMpaVW6VP1QUpNrikjMPGJGcy5GUjdE1alwYcML6mgEVqZupM-LWT2rloVNnEHs84DOSYxGpCf0WLfRl-hug7OED2h74XjN-xBdpBOYM_c4GCy63jo49wn4CFHD-8vkpiE9_67nLwbxufxlQS9PJpTIpI9qARy7hOM-eg167BsepfbV_SLdE3k7OKbxNmE5i_R_OEoGWA7aFyX03d0l3NE_156fim3ekEk0FiH4kKMZHjSzrKVU-hQR-D0j8LM2vGxv1dAFbqwAJPCUYZxUzhUs635UXb_AEE8gXnCBI-h52wtlsWtO0TGyJ65F9mzKvE5aAgA";
    //    private static String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJLcHRXNWJCcTlsRGliY2s5NHI3TldHQVl0SHBrUFI3N1A4V0hMWDVIX1E0In0.eyJleHAiOjE2Njg0MjIxNDksImlhdCI6MTY2ODMzNTc0OSwiYXV0aF90aW1lIjoxNjY4MzM1NzQ5LCJqdGkiOiI3NDI4YmJjZi1mZTc0LTRhY2MtYjZkZC0zNzRjZWNlZTk5NmUiLCJpc3MiOiJodHRwczovLzEwMy4xMjYuMTYxLjE5OS9hdXRoL3JlYWxtcy9tYXN0ZXIiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiMTAxZGQ1MmMtMjNiYS00ZjM4LWExMjQtYjc4MGUxYjVhODFiIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoib3BlbnJlbW90ZSIsInNlc3Npb25fc3RhdGUiOiJmNWUxMDczNi0wODYwLTRjMmYtYWFhMy02MjlmNTQxNDlmYTEiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHBzOi8vbG9jYWxob3N0IiwiaHR0cHM6Ly8xMDMuMTI2LjE2MS4xOTkiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDp1c2VycyIsInJlYWQ6bG9ncyIsInJlYWQ6bWFwIiwicmVhZDpydWxlcyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJzaWQiOiJmNWUxMDczNi0wODYwLTRjMmYtYWFhMy02MjlmNTQxNDlmYTEiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInByZWZlcnJlZF91c2VybmFtZSI6InVzZXIxIn0.cwOQ9pGkaF7FsCqRYgSo8GulyQeoWbJv1a3eDHZAXhqU6mWpW8tKssNuGu2O1tVihCxaw7IarpOxFoED6Gl61RxE10vuyZfmCTuyTXOSV_D9ZfWWcccQ8E8-e94mEDusqKjmIN7BJVAlE9h7mcMFL6ODlHwwwIeDx6uwgK7YaJfEnvh8y6kv_VFoS3O-ddHp9c0fEc2GGU1KWsQgjJoi-LAF2oOOuxevUwTbeIwSk031-auuJH_NxR6Sv67Xjo2xjG5Kf9HzkwktgN57l2XMmyHkSVU59VgkVWm1wuvmO-QI5mBQDKnbIgbbNNx9yBn1kuAMV8HNIjlBht3KHY1EIg";
//private static String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJLcHRXNWJCcTlsRGliY2s5NHI3TldHQVl0SHBrUFI3N1A4V0hMWDVIX1E0In0.eyJleHAiOjE2NjkwMzAyMTEsImlhdCI6MTY2ODk5OTkwMywiYXV0aF90aW1lIjoxNjY4OTQzODExLCJqdGkiOiI1YTUwNzBhMS1jYTJlLTRhY2QtYjA0ZS02MzJjYzVjNWZmYmUiLCJpc3MiOiJodHRwczovLzEwMy4xMjYuMTYxLjE5OS9hdXRoL3JlYWxtcy9tYXN0ZXIiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiMTAxZGQ1MmMtMjNiYS00ZjM4LWExMjQtYjc4MGUxYjVhODFiIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoib3BlbnJlbW90ZSIsInNlc3Npb25fc3RhdGUiOiIwNGIwNDFlOS0yZGFiLTQ0MTUtYTAxYi03MDAyYjUzN2NhNGYiLCJhY3IiOiIwIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHBzOi8vbG9jYWxob3N0IiwiaHR0cHM6Ly8xMDMuMTI2LjE2MS4xOTkiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDp1c2VycyIsInJlYWQ6bG9ncyIsInJlYWQ6bWFwIiwicmVhZDpydWxlcyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJzaWQiOiIwNGIwNDFlOS0yZGFiLTQ0MTUtYTAxYi03MDAyYjUzN2NhNGYiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInByZWZlcnJlZF91c2VybmFtZSI6InVzZXIxIn0.KQY64rQgGF0fXHzKo8U3ETHH1wURWwi2QN8m2u6pe8wjaaySx6UuK74LxMLalGLwSHXh2ygpe2tgeCNwy1jhxfkAcM_-Dy0-Q3Tk4HaLvrVxcgiEKU15tiCbc7JR5etsRRCS4wlV9eglPcBMNYazwlf-cGQimcyG7Tc19BRjp33G3l2jD1-yAg_gP7m_6yv0Ty_4qYtBjNLCH7QnpCVpVKAeTND-Jr3TuQ9CA-Agxf9mit_SNBFTf8VZMEytexNMKdRPE61VfSDbwTqrm1Aga65cRvpoHKTOhsK6Pv0w6OVU6gVEXjNYfWydul3KRUf597z61umf1pQ8abQkS0mmIQ";
//    private static String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJLcHRXNWJCcTlsRGliY2s5NHI3TldHQVl0SHBrUFI3N1A4V0hMWDVIX1E0In0.eyJleHAiOjE2NjkwMzAyMTEsImlhdCI6MTY2ODk0NzI4NSwiYXV0aF90aW1lIjoxNjY4OTQzODExLCJqdGkiOiJkZTYzMjY5Ni05ZWQ4LTRlYjUtYjFiZi0yZWI3NzM2MjA1ZDQiLCJpc3MiOiJodHRwczovLzEwMy4xMjYuMTYxLjE5OS9hdXRoL3JlYWxtcy9tYXN0ZXIiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiMTAxZGQ1MmMtMjNiYS00ZjM4LWExMjQtYjc4MGUxYjVhODFiIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoib3BlbnJlbW90ZSIsInNlc3Npb25fc3RhdGUiOiIwNGIwNDFlOS0yZGFiLTQ0MTUtYTAxYi03MDAyYjUzN2NhNGYiLCJhY3IiOiIwIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHBzOi8vbG9jYWxob3N0IiwiaHR0cHM6Ly8xMDMuMTI2LjE2MS4xOTkiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDp1c2VycyIsInJlYWQ6bG9ncyIsInJlYWQ6bWFwIiwicmVhZDpydWxlcyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJzaWQiOiIwNGIwNDFlOS0yZGFiLTQ0MTUtYTAxYi03MDAyYjUzN2NhNGYiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInByZWZlcnJlZF91c2VybmFtZSI6InVzZXIxIn0.BbWyrD9jfX5kq7GrJCdmveV7Buw5GDUWk-bmBeAQlx1bmWfldqjVwv38K6Bhn0So7v7gbXXH5J9eWdjHuWBU4G1zcaeh8CxwOlYyqO2eaLnHKCleVssJ2llHie7OpbPbpWmlhC0biPZmZ6XUL9Pilvez4MUbLBapsMP7lb0i1W9tTzA__IyfA4uaLykGq9v9nkQr48ZsCGulIn7RKhEGh2HDHktx8sfszj_M1-LoebODgkzVjbRj6nWsAPtIEe5XqRAOKwg0sWwf96a9ypqGnoD5gGyyDtbc0v-E9jzJK79jcdHW9oOQpbjaGFV7chcFlDOa96RZn2nMgsrnrHJbCQ";
    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            //Log
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);

            //Bear token
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest  = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    return chain.proceed(newRequest);
                }
            });


            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static Retrofit getClient() {
//            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        /*
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

       OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();
*/
        OkHttpClient client = getUnsafeOkHttpClient();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://103.126.161.199")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }
}

