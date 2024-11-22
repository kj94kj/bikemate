package com.example.capstoneMap.route;

import java.util.ArrayList;
import java.util.List;

import com.example.capstoneMap.user.User;
import com.example.capstoneMap.user.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RouteDto {
	//이름과 시작점 길이를 담는거 추가하기
	private Long id;
	private String name;
	private String encodedPath;
	private List<Double[]> locationList;
	private Long userId;
	private Double[] startLocation;
	
	
	public RouteDto(String name, String encodedPath ,List<Double[]> locationList, Double[] startLocation) {
	    this.name = name;
	    this.encodedPath = encodedPath;
	    this.locationList = locationList; 
	    this.startLocation = startLocation;
	}
	
	public RouteDto(String name, String encodedPath ,List<Double[]> locationList, Long userId, Double[] startLocation) {
	    this.name = name;
	    this.encodedPath = encodedPath;
	    this.locationList = locationList; 
	    this.userId=userId;
	    this.startLocation=startLocation;
	}
	
	public Route toEntity() {
		return new Route(id, name, encodedPath, convertLocationListToJson(locationList), startLocation);
	}
	
	
	private String convertLocationListToJson(List<Double[]> locationList) {
	    try {
	        ObjectMapper mapper = new ObjectMapper();
	        String json = mapper.writeValueAsString(locationList);
	        System.out.println("Converted JSON: " + json); // 변환된 JSON 로그 출력
	        return json;
	    } catch (Exception e) {
	    	System.out.println("Converted JSON: error");
	        e.printStackTrace();
	        return "[]";  // 오류 발생 시 빈 JSON 배열 반환
	    }
	}
}
