package com.example.capstoneMap.locationUpdate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.capstoneMap.ranking.RankingService;
import com.example.capstoneMap.route.Route;
import com.example.capstoneMap.route.RouteDto;
import com.example.capstoneMap.route.RouteRepository;
import com.example.capstoneMap.user.User;
import com.example.capstoneMap.user.UserRepository;

@Service
public class UserRecordService {
	
	@Autowired
	private UserRecordRepository userRecordRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RouteRepository routeRepository;
    @Autowired
    private RankingService rankingService;
	
    @Transactional
    public ResponseEntity<RouteDto> saveRecord(UserRecordDto userRecordDto, Long userId, Long routeId) {
    	User user=userRepository.findById(userId)
    			.orElseThrow(() -> new IllegalArgumentException("User not found"));
    	
    	Route route=routeRepository.findById(routeId)
    			.orElseThrow(() -> new IllegalArgumentException("Route not found"));
    	
        if (userRecordDto.getId() != null && routeRepository.existsById(userRecordDto.getId())) {
            throw new IllegalArgumentException("Route with ID " + userRecordDto.getId() + " already exists.");
        }
    	
        System.out.println("Dto: " + userRecordDto.toString());
        UserRecord userRecord = userRecordDto.toEntity();
        System.out.println("entity: " + userRecord.toString());
        route.addUserRecord(userRecord);
        routeRepository.save(route);
        
        RouteDto routeDto = new RouteDto(route.getId(), route.getName(), route.getEncodedPath(), route.getLocationList(), route.getUserId(), 
        		route.getStartLocation(), route.getLength());
        // 랭킹 업데이트
        rankingService.updateRankings(userRecordDto.getRouteId());
        
        
        
        return ResponseEntity.ok(routeDto);
    }
	
    
    //일치하는걸 체크하는것도 필요하다고봄
    @Transactional
    public ResponseEntity<UserRecordDto> getMyOldRecord(@PathVariable("userId") Long userId, @PathVariable("routeId") Long routeId){
    	UserRecord userRecord=routeRepository.findByRouteIdAndUserId(routeId, userId)
    			.orElseThrow(() -> new IllegalArgumentException("User not found"));
    	
   	 UserRecordDto userRecordDto=new UserRecordDto(userRecord.getId(), userRecord.getUserId(), userRecord.getRouteId(), userRecord.getElapsedTime(),
      		userRecord.getLocationList());
    
    	return ResponseEntity.ok(userRecordDto);
    }
    
    @Transactional
    public ResponseEntity<UserRecordDto> getOldRecord(@PathVariable("userId") Long userId, @PathVariable("routeId") Long routeId){
    	UserRecord userRecord=routeRepository.findByRouteIdAndUserId(routeId, userId)
    			.orElseThrow(() -> new IllegalArgumentException("User not found"));
    	
    	 UserRecordDto userRecordDto=new UserRecordDto(userRecord.getId(), userRecord.getUserId(), userRecord.getRouteId(), userRecord.getElapsedTime(),
         		userRecord.getLocationList());
    	
    	return ResponseEntity.ok(userRecordDto);
    }
    
    @Transactional
    public ResponseEntity<List<UserRecordDto>> getTop5Record(@PathVariable("userId") Long userId, @PathVariable("routeId") Long routeId){
       
        PageRequest pageable = PageRequest.of(0, 5); 
        List<UserRecord> top5records = userRecordRepository.findTop5ByRouteIdAndElapsedTimeLessThan(routeId, userId, pageable);
        
        List<UserRecordDto> top5recordsDto=top5records.stream()
        .map(top5record -> new UserRecordDto(top5record.getId(), top5record.getUserId(), top5record.getRouteId(), top5record.getElapsedTime(),
        		top5record.getLocationList()
        		))
        .collect(Collectors.toList());
        
        return ResponseEntity.ok(top5recordsDto);
    }
}
