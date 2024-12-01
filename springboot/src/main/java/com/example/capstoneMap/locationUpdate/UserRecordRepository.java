package com.example.capstoneMap.locationUpdate;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRecordRepository extends JpaRepository<UserRecord, Long> {
	List<UserRecord> findByRouteIdOrderByElapsedTimeAsc(Long routeId);
	
	// 리스트의 마지막에 오는게 가장 잘달린 기록임.
    @Query("""
            SELECT ur
            FROM UserRecord ur
            WHERE ur.routeId = :routeId
              AND ur.elapsedTime < (
                  SELECT u.elapsedTime
                  FROM UserRecord u
                  WHERE u.userId = :userId
                    AND u.routeId = :routeId
              )
            ORDER BY ur.elapsedTime DESC
        """)
        List<UserRecord> findTop5ByRouteIdAndElapsedTimeLessThan(@Param("routeId") Long routeId, @Param("userId") Long userId, Pageable pageable);
}
