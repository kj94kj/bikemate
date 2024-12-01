package com.example.capstoneMap.rival;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class RivalService {
	
	@Autowired
	private RivalRepository rivalRepository; 
	
	// 예외처리를 추가할 수 있다.
    @Transactional
    public ResponseEntity<RivalDto> setRival(Long userId, Long otherId, Long routeId){
    	   // 1. Rival 엔티티 조회
        Optional<Rival> rivalOptional = rivalRepository.findByUserIdAndRouteId(userId, routeId);

        Rival rival;

        if (rivalOptional.isPresent()) {
            // 2. Rival이 존재할 경우
            rival = rivalOptional.get();
            
            // otherId 리스트에 추가
            if (!rival.getOtherId().contains(otherId)) {
                rival.getOtherId().add(otherId); // 리스트에 추가
            } else {
                return ResponseEntity.badRequest().body(null); // 이미 추가된 경우 처리
            }
        } else {
            // 3. Rival이 존재하지 않을 경우 새로 생성
            rival = new Rival();
            rival.setUserId(userId);
            rival.setRouteId(routeId);

            // 새 otherId 추가
            List<Long> otherIds = new ArrayList<>();
            otherIds.add(otherId);
            rival.setOtherId(otherIds);

            // 저장
            rivalRepository.save(rival);
        }

        // 4. RivalDto로 변환하여 반환
        RivalDto rivalDto = new RivalDto(rival.getId(), rival.getUserId(), rival.getOtherId(), rival.getRouteId());
        return ResponseEntity.ok(rivalDto);
    	
    }
    
    @Transactional
    public ResponseEntity<RivalDto> getRival(@PathVariable("userId") Long userId, @PathVariable("routeId") Long routeId){
       	Rival rival=rivalRepository.findByUserIdAndRouteId(routeId, userId)
    			.orElseThrow(() -> new IllegalArgumentException("User or Route not found"));
    	
       	RivalDto rivalDto=new RivalDto(rival.getId(), rival.getUserId(), rival.getOtherId(), rival.getRouteId());
    
    	return ResponseEntity.ok(rivalDto);
    }
    
    @Transactional
    public ResponseEntity<Void> deleteRival(Long userId, Long otherId, Long routeId) {
        // Rival 조회
        Rival rival = rivalRepository.findByUserIdAndRouteId(userId, routeId)
                .orElseThrow(() -> new IllegalArgumentException("Rival not found"));

        // otherId가 리스트에 있을 경우 삭제
        if (rival.getOtherId().contains(otherId)) {
            rival.getOtherId().remove(otherId); // 리스트에서 제거

            if (rival.getOtherId().isEmpty()) {
                // 리스트가 비어 있으면 Rival 엔티티 삭제
                rivalRepository.delete(rival);
            } else {
                // 리스트가 비어 있지 않으면 업데이트
                rivalRepository.save(rival);
            }
        } else {
            throw new IllegalArgumentException("Rival's otherId not found");
        }

        return ResponseEntity.noContent().build();
    }
}
