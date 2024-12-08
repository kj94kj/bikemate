package com.example.capstoneMap.locationUpdate;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserRecord {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private Long userId;
	@Column
	private Long routeId;
	
	private Long elapsedTime;
	
    @Column(columnDefinition = "LONGTEXT")
	private String locationListJson;
	
    @Transient
    @Column(columnDefinition = "LONGTEXT")
	private List<Double[]> locationList;
	
	public UserRecord(Long id, Long elapsedTime, String locationListJson) {
		this.id=id;
		this.elapsedTime=elapsedTime;
		this.locationListJson=locationListJson;
	}
	
	public UserRecord(Long id, Long userId, Long routeId, Long elapsedTime, String locationListJson) {
		this.id=id;
		this.userId = userId;
		this.routeId = routeId;
		this.elapsedTime=elapsedTime;
		this.locationListJson=locationListJson;
	}
	
    // 엔티티가 데이터베이스에서 로드될 때마다 loadLocationList()가 자동으로 호출되서 locationList에 저장됨
    @PostLoad
    private void loadLocationList() {
        if (locationListJson != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                locationList = mapper.readValue(locationListJson, new TypeReference<List<Double[]>>() {});
            } catch (Exception e) {
                e.printStackTrace();
                locationList = new ArrayList<>();
            }
        }
    }
}
