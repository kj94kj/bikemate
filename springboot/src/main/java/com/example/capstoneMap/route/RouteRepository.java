package com.example.capstoneMap.route;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.capstoneMap.locationUpdate.UserRecord;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
	@Query("SELECT ur FROM Route r JOIN r.userRecords ur WHERE ur.routeId = :routeId AND ur.userId = :userId")
    Optional<UserRecord> findByRouteIdAndUserId(@Param("routeId") Long routeId, @Param("userId") Long userId);
	
	List<Route> findByLengthBetween(Double minLength, Double maxLength);
	
    @Query("SELECT r FROM Route r JOIN r.userRecords ur WHERE ur.userId = :userId")
    List<Route> findRoutesByRecordUserId(@Param("userId") Long userId);
}