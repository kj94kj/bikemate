package com.example.capstonemap.locationUpdate;

import android.widget.Toast;

import com.example.capstonemap.MapsActivity;

public class GetOldRecord {
    static UserRecordRepository userRecordRepository = new UserRecordRepository();
    static UserRecordDto userRecordDto;

    public static void getMyOldRecord(Long userId, Long routeId) {
        userRecordRepository.getMyOldRecord(userId, routeId,
                // 성공 시 routeDtoList에 데이터 저장
                myOldRecordord -> {
                    MapsActivity.userUpdateInfo.setOldMyRecord(myOldRecordord);
                    System.out.println("기록이 있습니다.");
                },
                // 실패 시 처리
                () -> {
                    MapsActivity.userUpdateInfo.setOldMyRecord(null);
                    System.out.println("기록이 없습니다.");
                }
        );
    }

    public static void getOldRecord(Long userId, Long routeId) {
        userRecordRepository.getOldRecord(userId, routeId,
                // 성공 시 routeDtoList에 데이터 저장
                OldRecordord -> {
                    MapsActivity.userUpdateInfo.setOldOtherRecord(OldRecordord);
                    System.out.println("기록이 있습니다.");
                },
                // 실패 시 처리
                () -> {
                    MapsActivity.userUpdateInfo.setOldOtherRecord(null);
                    System.out.println("기록이 없습니다.");
                }
        );

    }
}
