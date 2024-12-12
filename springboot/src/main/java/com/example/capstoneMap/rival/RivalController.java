package com.example.capstoneMap.rival;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.capstoneMap.locationUpdate.UserRecordDto;
import com.example.capstoneMap.locationUpdate.UserRecordService;
import com.example.capstoneMap.route.RouteDto;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://bikemate.herokuapp.com") 
public class RivalController {
	
	@Autowired
	RivalService rivalService;
	
    @PutMapping("/users/{userId}/{otherId}/{routeId}")
    public ResponseEntity<RivalDto> setRival(@PathVariable("usserId") Long userId, 
    		@PathVariable("otherId") Long otherId, @PathVariable("routeId") Long routeId) {
        return rivalService.setRival(userId, otherId, routeId);
    }
    
    @GetMapping("/users/{userId}/{routeId}")
    public ResponseEntity<RivalDto> getRival(@PathVariable("userId") Long userId, @PathVariable("routeId") Long routeId){
    	return rivalService.getRival(userId, routeId);
    }
    
    @DeleteMapping("/users/{userId}/{otherId}/{routeId}")
    public ResponseEntity<Void> deleteRival(@PathVariable("userId") Long userId, @PathVariable("otherId") Long otherId, @PathVariable("routeId") Long routeId){
    	return rivalService.deleteRival(userId, otherId, routeId);
    }
    
}
