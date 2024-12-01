package com.example.capstonemap.locationUpdate;

import java.util.ArrayList;
import java.util.List;

public class GetTop5Record {
    static UserRecordRepository userRecordRepository = new UserRecordRepository();
    static List<UserRecordDto> top5userRecordDto = new ArrayList<>();

    public static List<UserRecordDto> getTop5Record(Long userId, Long routeId) {
        userRecordRepository.getTop5Record(userId, routeId,
                // 성공 시 routeDtoList에 데이터 저장
                top5Records -> {
                    top5userRecordDto = top5Records;
                    System.out.println("기록이 있습니다.");
                },
                // 실패 시 처리
                () -> {
                    top5userRecordDto = null;
                    System.out.println("기록이 없습니다.");
                }
        );

        return top5userRecordDto;
    }
}
