package com.example.capstonemap.locationUpdate;


// 실시간의 좌표 정보와 속도를 받고 화면에 표시하며,
// UserUpdateDto에 루트가 끝나면 갱신하거나 OldUserRecord를 가져오는 클래스
// 아마 userId와 routeId를 밖에서 받을 수 있을 것 같다.

import com.example.capstonemap.distance.DistancePolyLine;
import com.example.capstonemap.distance.DistanceTwoLocation;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateInfo {
    private Long userId;
    private Long routeId;

    private boolean isRacing = false;
    private boolean isOut = false;

    // 카운터에 맞춰서 이전 기록의 좌표를 가져옴
    private int counter=0;

    private long startedTime=0;
    private long elapsedTime=0;

    private double speed=0;

    // oldLocation은 고스트의 좌표를 말함.
    private Double[] currentLocation;
    private Double[] oldLocation;

    // 고스트와 나와의 거리
    private double distance;

    private List<Double[]> locationList;

    private int ranking;

    private UserRecordDto myRecordDto = new UserRecordDto(userId, routeId);
    private UserRecordDto oldMyRecord;
    private UserRecordDto oldOtherRecord;

    // 현재 유저의 시작점부터 거리, 기록 유저의 시작점부터 거리를 계산해서 넣어줄거임
    private double currentDistance = 0;
    private double oldDistance = 0;

    // 현재 달리고있는 폴리라인의 List<LatLng>을 set해야함.
    private List<LatLng> polyline;



    // 기록 유저의 n초마다의 위치를 set하기 위해 만듦
    public void setOldLocation(){
        oldLocation=oldMyRecord.getLocationList().get(counter);
        counter++;
    }

    public int rankingNow(){
        double meDistance = DistancePolyLine.calculateProgress(DistanceTwoLocation.doubleToLatLng(currentLocation), polyline);
        double otherDistance = DistancePolyLine.calculateProgress(DistanceTwoLocation.doubleToLatLng(currentLocation), polyline);

        if(meDistance - otherDistance > 0)
            return 1;
        else
            return 2;
    }

    public UserUpdateInfo(Long userId, Long routeId){
        this.userId = userId;
        this.routeId = routeId;
    }

    public void setUserRecordDto(){
            if(oldMyRecord.getElapsedTime() - elapsedTime > 0){
                myRecordDto.setLocationList(locationList);
                myRecordDto.setElapsedTime(elapsedTime);

                SaveMyRecord.saveMyRecord(myRecordDto, userId, routeId);
        }
    }

    public void setLocation(Double[] currentLocation){
        this.currentLocation=currentLocation;
        locationList.add(currentLocation);
    }
}