package com.example.capstoneMap.locationUpdate;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRecordRepository extends JpaRepository<UserRecord, Long> {
	List<UserRecord> findByRouteIdOrderByElapsedTimeAsc(Long routeId);
}
