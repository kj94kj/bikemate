package com.example.capstoneMap.locationUpdate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.capstoneMap.route.Route;
import com.example.capstoneMap.route.RouteDto;
import com.example.capstoneMap.user.User;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://bikemate.herokuapp.com") 
public class UserRecordController {
	
	@Autowired
	UserRecordService userRecordService;
	
	//PathVariable {}안에 값을 받아옴, RequestParam 내가 {} 값을 전달
    @PostMapping("/users/{userId}/{routeId}/userUpdates")
    public ResponseEntity<RouteDto> saveRoute(@RequestBody UserRecordDto userRecordDto, 
    		@PathVariable("userId") Long userId, @PathVariable("routeId") Long routeId) {
        return userRecordService.saveRecord(userRecordDto, userId, routeId);
    }
    
    @GetMapping("/users/{userId}/{routeId}/myOldRecord")
    public ResponseEntity<UserRecordDto> getMyOldRecord(@PathVariable("userId") Long userId, @PathVariable("routeId") Long routeId){
    	return userRecordService.getMyOldRecord(userId, routeId);
    }
    
    @GetMapping("/users/{userId}/{routeId}/oldRecord")
    public ResponseEntity<UserRecordDto> getOldRecord(@PathVariable("userId") Long userId, @PathVariable("routeId") Long routeId){
    	return userRecordService.getOldRecord(userId, routeId);
    }
    
    @GetMapping("/users/{userId}/{routeId}/top5Record")
    public ResponseEntity<List<UserRecordDto>> getTop5Record(@PathVariable("userId") Long userId, @PathVariable("routeId") Long routeId){
    	return userRecordService.getTop5Record(userId, routeId);
    }
    
}
