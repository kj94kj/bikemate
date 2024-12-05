package com.example.capstoneMap.ranking;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.capstoneMap.locationUpdate.UserRecord;
import com.example.capstoneMap.locationUpdate.UserRecordRepository;
import com.example.capstoneMap.route.Route;
import com.example.capstoneMap.route.RouteDto;
import com.example.capstoneMap.route.RouteRepository;

@Service
public class RankingService {
	@Autowired
    private UserRecordRepository userRecordRepository;
	
    @Autowired
    private RankingRepository rankingRepository;

    public void updateRankings(Long routeId) {
        // 1. 모든 기록 가져오기
        List<UserRecord> records = userRecordRepository.findByRouteIdOrderByElapsedTimeAsc(routeId);

        // 2. 기존 랭킹 삭제
        rankingRepository.deleteByRouteId(routeId);

        // 3. 새로운 랭킹 계산 및 저장
        int rank = 1;
        List<Ranking> rankings = new ArrayList<>();
        for (UserRecord record : records) {
            Ranking ranking = new Ranking(record.getUserId(), record.getRouteId(), record.getElapsedTime(), rank++);
        	
            rankings.add(ranking);
        }

        // 4. 랭킹 저장
        rankingRepository.saveAll(rankings);
    }

	public ResponseEntity<List<RankingDto>> getRankings(Long routeId) {
		
		
    	List<Ranking> rankings=rankingRepository.findByRouteId(routeId);   
    	
        List<RankingDto> rankingDtos = rankings.stream()
                .map(ranking -> new RankingDto(ranking.getId(), ranking.getUserId(), ranking.getRouteId(),ranking.getElapsedTime(),
                		ranking.getRank()))
                        .collect(Collectors.toList());
    	
    	return ResponseEntity.ok(rankingDtos);

	}

	public ResponseEntity<RankingDto> getMyRanking(Long userId, Long routeId) {
		
		Ranking ranking = rankingRepository.findByUserIdAndRouteId(userId, routeId);
		RankingDto rankingDto = new RankingDto(ranking.getId(), ranking.getUserId(), ranking.getRouteId(),ranking.getElapsedTime(),
        		ranking.getRank());
		
		return ResponseEntity.ok(rankingDto);
	}

}
