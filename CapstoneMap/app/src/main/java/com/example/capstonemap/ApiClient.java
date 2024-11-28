package com.example.capstonemap;

import com.example.capstonemap.locationUpdate.UserRecordApiService;
import com.example.capstonemap.ranking.RankingApiService;
import com.example.capstonemap.routes.RouteApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://10.0.2.2:8080"; // 로컬 서버 주소
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    //ApiClient 클래스의 ApiService를 쓴다.
    //api 인터페이스 객체이다.
    private static final RouteApiService routeApiService = ApiClient.getClient().create(RouteApiService.class);

    public static RouteApiService getRouteApiService() {
        return routeApiService;
    }

    private static final UserRecordApiService userRecordApiService = ApiClient.getClient().create(UserRecordApiService.class);

    public static UserRecordApiService getUserRecordApiService(){return userRecordApiService;}

    private static final RankingApiService rankingApiservice = ApiClient.getClient().create(RankingApiService.class);

    public static RankingApiService getRankingApiService(){return rankingApiservice;}

}