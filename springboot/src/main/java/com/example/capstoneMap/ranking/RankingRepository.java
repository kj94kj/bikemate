package com.example.capstoneMap.ranking;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {
	void deleteByRouteId(Long routeId);
	
    List<Ranking> findByRouteId(Long routeId);
    Ranking findByUserIdAndRouteId(Long userId, Long routeId);
}
