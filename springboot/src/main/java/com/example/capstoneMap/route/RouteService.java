package com.example.capstoneMap.route;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.capstoneMap.user.User;
import com.example.capstoneMap.user.UserDto;
import com.example.capstoneMap.user.UserRepository;

@Service
public class RouteService {
	
	@Autowired
	private RouteRepository routeRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	//폴리라인 그린걸 저장하는 함수, 유저가 Id를 통해 저장할 수 있음.
    @Transactional
    public ResponseEntity<UserDto> saveRoute(RouteDto routeDto, Long userId) {
    	User user=userRepository.findById(userId)
    			.orElseThrow(() -> new IllegalArgumentException("User not found"));
    	
        // Route ID를 통해 중복 확인
        if (routeDto.getId() != null && routeRepository.existsById(routeDto.getId())) {
            throw new IllegalArgumentException("Route with ID " + routeDto.getId() + " already exists.");
        }
    	
        System.out.println("Dto: " + routeDto.toString());
        Route route = routeDto.toEntity();
        System.out.println("entity: " + route.toString());
        user.addRoute(route);
        userRepository.save(user);
        
        List<Route> userRoutes = user.getRoutes();
        List<RouteDto> userRoutesDtos = userRoutes.stream()
                .map(userRoute -> new RouteDto(userRoute.getId(), userRoute.getName(), userRoute.getEncodedPath(), 
                		userRoute.getLocationList(), userRoute.getUserId(), userRoute.getStartLocation(), userRoute.getLength()))
                .collect(Collectors.toList());
        
        UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getPassword(), userRoutesDtos);
        
        return ResponseEntity.ok(userDto);
    }
    
    // 모든 루트를 조회하는 함수
    @Transactional
    public ResponseEntity<List<RouteDto>> getAllRoutes(){
    	List<Route> routes=routeRepository.findAll();   
    	
        List<RouteDto> routeDtos = routes.stream()
                .map(route -> new RouteDto(route.getId(), route.getName(), route.getEncodedPath(), route.getLocationList(), route.getUserId(), route.getStartLocation(), 
                		route.getLength()))
                .collect(Collectors.toList());
    	
    	return ResponseEntity.ok(routeDtos);
    	}
    
    @Transactional
    public ResponseEntity<List<RouteDto>> getUserRoutes(Long userId){
    	User user=userRepository.findById(userId)
    			.orElseThrow(() -> new IllegalArgumentException("User not found"));
    	
    	List<Route> userRoutes=user.getRoutes();
    	
    	
        List<RouteDto> userRouteDtos = userRoutes.stream()
                .map(route -> new RouteDto(route.getId(), route.getName(), route.getEncodedPath(), route.getLocationList(), route.getUserId(), 
                		route.getStartLocation(), route.getLength()))
                .collect(Collectors.toList());
    	
    	return ResponseEntity.ok(userRouteDtos);
    	}
    
    @Transactional
    public ResponseEntity<String> deleteRoute(Long userId, Long routeId){
    	
    	User user=userRepository.findById(userId)
    			.orElseThrow(() -> new IllegalArgumentException("User not found"));
    	
    	Boolean isOwned=user.isOwned(routeId, userId);
    	user.removeRouteById(routeId);
    	
    	if(isOwned) {
    		routeRepository.deleteById(routeId);
    	}	
    	
    	return ResponseEntity.ok("Route deleted successfully"); 
    }
    
    @Transactional
    public ResponseEntity<List<RouteDto>> getLengthRoutes(Double minLength, Double maxLength){
    	
    	List<Route> lengthRoutes=routeRepository.findByLengthBetween(minLength, maxLength);
    	
    	
        List<RouteDto> lengthRouteDtos = lengthRoutes.stream()
                .map(route -> new RouteDto(route.getId(), route.getName(), route.getEncodedPath(), route.getLocationList(), route.getUserId(), 
                		route.getStartLocation(), route.getLength()))
                .collect(Collectors.toList());
    	
    	return ResponseEntity.ok(lengthRouteDtos);
    	}
    
    @Transactional
    public ResponseEntity<List<RouteDto>> getRoutesByRecordUserId(@PathVariable Long userId){
    	 List<Route> recordUserIdRoutes = routeRepository.findRoutesByRecordUserId(userId);
    	 
         List<RouteDto> recordUserIdRouteDtos = recordUserIdRoutes.stream()
                 .map(route -> new RouteDto(route.getId(), route.getName(), route.getEncodedPath(), route.getLocationList(), route.getUserId(), 
                 		route.getStartLocation(), route.getLength()))
                 .collect(Collectors.toList());
         
         return ResponseEntity.ok(recordUserIdRouteDtos);
    }
}
