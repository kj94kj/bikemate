package com.example.capstoneMap.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
	

    @Autowired
    private UserRepository userRepository;
	
    @Transactional
    public ResponseEntity<UserDto> saveLogin(UserDto userDto){
    	User user = userDto.toEntity();
    	userRepository.save(user);
    	
    	return ResponseEntity.ok(userDto);
    }
    
    public boolean authenticate(UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername());
        return user != null && user.getPassword().equals(userDto.getPassword()); // 간단한 검증
    }
    
    public Long getUserId(UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername());
        if (user != null) {
            return user.getId();
        } else {
            return null;
        }
    }
}
