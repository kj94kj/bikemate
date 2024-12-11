package com.example.capstonemap.ranking;

import com.example.capstonemap.RecordActivity;
import com.example.capstonemap.routes.RouteDto;
import com.example.capstonemap.routes.RouteRepository;

import java.util.ArrayList;
import java.util.List;

//루트id를 받고 랭킹을 조회할 수 있다.
//rankingDtoList의 속성을 표시해서 랭킹을 표시하면됨.


public class GetRankings {
    static RankingRepository rankingRepository=new RankingRepository();
    static List<RankingDto> rankingDtoList= new ArrayList<>();
    static RankingDto myRankingDto = new RankingDto();

    public static void getRankings(Long routeId, RankingsCallback callback) {
        rankingRepository.getRankings(
                routeId,
                rankings -> {
                    callback.onSuccess(rankings);
                    System.out.println("모든 랭킹이 rankingDtoList에 저장되었습니다.");
                },
                () -> {
                    callback.onFailure();
                    System.out.println("랭킹 조회에 실패했습니다.");
                }
        );
    }

    public static void getMyRanking(Long userId, Long routeId, MyRankingCallback callback) {
        rankingRepository.getMyRanking(
                userId, routeId,
                ranking -> {
                    callback.onSuccess(ranking);
                    System.out.println("랭킹이 있습니다.");
                },
                () -> {
                    callback.onFailure();
                    System.out.println("랭킹 조회 실패.");
                }
        );
    }

    // 콜백 인터페이스 정의
    public interface RankingsCallback {
        void onSuccess(List<RankingDto> rankings);
        void onFailure();
    }

    public interface MyRankingCallback {
        void onSuccess(RankingDto ranking);
        void onFailure();
    }

}
