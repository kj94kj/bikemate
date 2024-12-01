package com.example.capstonemap.rival;

import com.example.capstonemap.locationUpdate.UserRecordDto;
import com.example.capstonemap.routes.RouteDto;
import com.example.capstonemap.routes.RouteRepository;

import java.util.ArrayList;
import java.util.List;

public class GetRival {
    static RivalRepository rivalRepository=new RivalRepository();
    static RivalDto rivalDto = new RivalDto();

    public static RivalDto getRival(Long userId, Long routeId) {
        rivalRepository.getRival(userId, routeId,
                // 성공 시 routeDtoList에 데이터 저장
                rival -> {
                    rivalDto=rival;
                    System.out.println("라이벌이 있습니다.");
                },
                // 실패 시 처리
                () -> {
                    rivalDto=null;
                    System.out.println("라이벌이 없습니다.");
                }
        );

        return rivalDto;
    }
}
