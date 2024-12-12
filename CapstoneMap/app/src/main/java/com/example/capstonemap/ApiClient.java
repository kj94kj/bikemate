package com.example.capstonemap;

import com.example.capstonemap.locationUpdate.UserRecordApiService;
import com.example.capstonemap.ranking.RankingApiService;
import com.example.capstonemap.rival.RivalApiService;
import com.example.capstonemap.routes.RouteApiService;
import com.example.capstonemap.user.UserApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://bikemate-475106cb94fc.herokuapp.com"; // 로컬 서버 주소
    // "http://10.0.2.2:8080"
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

    private static final RivalApiService rivalApiService = ApiClient.getClient().create(RivalApiService.class);

    public static RivalApiService getRivalApiService(){return rivalApiService;}

    private static final UserApiService userApiService = ApiClient.getClient().create(UserApiService.class);

    public static UserApiService getUserApiService(){return userApiService;}

}