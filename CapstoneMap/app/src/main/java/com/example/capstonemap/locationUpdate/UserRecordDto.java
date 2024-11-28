package com.example.capstonemap.locationUpdate;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 코스를 완주 하고 나서 값을 전달 하는 클래스

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserRecordDto {
    private Long id;
    private Long userId;
    private Long routeId;
    private long elapsedTime;
    private List<Double[]> locationList;

    public UserRecordDto(Long userId, Long routeId){
        this.userId = userId;
        this.routeId = routeId;
    }
}
