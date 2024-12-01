package com.example.capstoneMap.rival;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RivalRepository extends JpaRepository<Rival, Long>{
	Optional<Rival> findByUserIdAndRouteId(Long userId, Long routeId);
}
