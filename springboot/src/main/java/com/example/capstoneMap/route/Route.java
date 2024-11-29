package com.example.capstoneMap.route;

import java.util.ArrayList;
import java.util.List;

import com.example.capstoneMap.locationUpdate.UserRecord;
import com.example.capstoneMap.user.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.*;
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
@Table(name = "routes")
public class Route {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@Column(columnDefinition = "TEXT")
	private String encodedPath;
	private String locationListJson;
	
	@Column
	private Long userId;
	
	private Double[] startLocation;
	
	private Double length;
	
	
	// 유저 아이디 안받는 버전
	public Route(Long id, String name, String encodedPath, String locationListJson, Double[] startLocation, Double length) {
		this.id=id;
		this.name=name;
		this.encodedPath=encodedPath;
		this.locationListJson=locationListJson;
		this.startLocation=startLocation;
		this.length = length;
	}
	
    //mappedBy = 연관주인객체, CascadeType.ALL=이게 수정되면 다 수정됨, FetchType.LAZY= 필요할때만 호출됨
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="routeId")
    private List<UserRecord> userRecords=new ArrayList<>();
    
	public Route(Long id, String name, String encodedPath, String locationListJson, Double[] startLocation, List<UserRecord> userRecords, Double length) {
		this.id=id;
		this.name=name;
		this.encodedPath=encodedPath;
		this.locationListJson=locationListJson;
		this.startLocation=startLocation;
		this.userRecords=userRecords;
		this.length = length;
	}
	
    // 기록을 추가하면 Route 엔티티의 userRecords에도 추가가됨
    public void addUserRecord(UserRecord userRecord) {
    	if(userRecords==null) {
    		userRecords=new ArrayList<UserRecord>();
    	}
    	
        userRecords.add(userRecord);
    }
    
    @Transient
    private List<Double[]> locationList;
	
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
