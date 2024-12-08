package com.example.capstoneMap.locationUpdate;

import java.util.List;

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
public class UserRecordDto {
	private Long id;
	private Long userId;
	private Long routeId;
	private Long elapsedTime;
	private List<Double[]> locationList;
	
	public UserRecordDto(Long userId, Long routeId, Long elapsedTime, List<Double[]> locationList) {
		this.userId=userId;
		this.routeId=routeId;
		this.elapsedTime=elapsedTime;
		this.locationList=locationList;
	}
	
	public UserRecordDto( long elapsedTime, List<Double[]> locationList) {
		this.elapsedTime=elapsedTime;
		this.locationList=locationList;
	}
	
	public UserRecord toEntity() {
		return new UserRecord(id, userId, routeId, elapsedTime, convertLocationListToJson(locationList));
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
