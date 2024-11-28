package com.example.capstoneMap.locationUpdate;

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
@CrossOrigin(origins = "http://10.0.2.2:8080") 
public class UserRecordController {
	
	@Autowired
	UserRecordService userRecordService;
	
	//PathVariable {}안에 값을 받아옴, RequestParam 내가 {} 값을 전달
    @PostMapping("/users/{userId}/{routeId}/userUpdates")
    public ResponseEntity<Route> saveRoute(@RequestBody UserRecordDto userRecordDto, 
    		@PathVariable("userId") Long userId, @PathVariable("routeId") Long routeId) {
        return userRecordService.saveRecord(userRecordDto, userId, routeId);
    }
    
    @GetMapping("/users/{userId}/{routeId}/myOldRecord")
    public ResponseEntity<UserRecord> getMyOldRecord(@PathVariable("userId") Long userId, @PathVariable("routeId") Long routeId){
    	return userRecordService.getMyOldRecord(userId, routeId);
    }
    
    @GetMapping("/users/{userId}/{routeId}/oldRecord")
    public ResponseEntity<UserRecord> getOldRecord(@PathVariable("userId") Long userId, @PathVariable("routeId") Long routeId){
    	return userRecordService.getOldRecord(userId, routeId);
    }
}
