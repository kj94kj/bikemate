package com.example.capstoneMap.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceJWT implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }
    
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
}