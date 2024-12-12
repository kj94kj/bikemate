package com.example.capstoneMap.ranking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.capstoneMap.route.RouteDto;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://bikemate.herokuapp.com") 
public class RankingController {
	@Autowired
    private RankingService rankingService;
	
	//PathVariable {}안에 값을 받아옴, RequestParam 내가 {} 값을 전달
	
    @GetMapping("/{routeId}/rankings")
    public ResponseEntity<List<RankingDto>> getRankings(@PathVariable("routeId") Long routeId){
    	return rankingService.getRankings(routeId);
    }
    
    @GetMapping("/{userId}/{routeId}/rankings")
    public ResponseEntity<RankingDto> getMyRanking(@PathVariable("userId") Long userId, @PathVariable("routeId") Long routeId){
    	return rankingService.getMyRanking(userId, routeId);
    }
}
