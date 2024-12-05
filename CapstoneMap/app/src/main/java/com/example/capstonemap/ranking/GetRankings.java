package com.example.capstonemap.ranking;

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

    private static List<RankingDto> getRankings(Long routeId) {
        rankingRepository.getRankings(
                routeId,
                rankings -> {
                    rankingDtoList.clear();
                    rankingDtoList.addAll(rankings);
                    System.out.println("모든 랭킹이 rankingDtoList에 저장되었습니다.");
                },
                () -> {
                    System.out.println("랭킹 조회에 실패했습니다.");
                }
        );

        return rankingDtoList;
    }

    private static RankingDto getMyRanking(Long userId, Long routeId) {
        rankingRepository.getMyRanking( userId, routeId,
                // 성공 시 routeDtoList에 데이터 저장
                ranking-> {
                    myRankingDto = ranking;
                    System.out.println("랭킹이 있습니다.");
                },
                // 실패 시 처리
                () -> {
                    myRankingDto=null;
                    System.out.println("랭킹이 없습니다.");
                }
        );

        return myRankingDto;
    }

}
