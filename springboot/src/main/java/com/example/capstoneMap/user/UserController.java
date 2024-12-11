package com.example.capstoneMap.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.capstoneMap.route.RouteDto;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://10.0.2.2:8080") 
public class UserController {
	
	@Autowired
	private UserService userService;
	
	  @PostMapping("/users")
	    public ResponseEntity<UserDto> saveLogin(@RequestBody UserDto userDto) {
	        return userService.saveLogin(userDto);
	   }
	  
	  @PostMapping("/login")
	  public ResponseEntity<Map<String, Object>> login(@RequestBody UserDto userDto) {
	      boolean isAuthenticated = userService.authenticate(userDto);
	      Map<String, Object> response = new HashMap<>();

	      if (isAuthenticated) {
	          Long userId = userService.getUserId(userDto); // ID를 가져오는 메서드 호출
	          response.put("message", "로그인 성공");
	          response.put("id", userId);
	          return ResponseEntity.ok(response); // 200 OK
	      } else {
	          response.put("message", "로그인 실패");
	          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response); // 401 Unauthorized
	      }
	  }
}
