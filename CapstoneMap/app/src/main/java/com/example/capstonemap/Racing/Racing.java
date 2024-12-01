package com.example.capstonemap.Racing;

import com.example.capstonemap.locationUpdate.GetTop5Record;
import com.example.capstonemap.locationUpdate.UserRecordApiService;
import com.example.capstonemap.locationUpdate.UserRecordDto;
import com.example.capstonemap.locationUpdate.UserRecordRepository;
import com.example.capstonemap.locationUpdate.UserUpdateInfo;

import java.util.List;

// SetOldRecord에서 내 기록을 일단 추가시키고 함.
// 라이벌 모드를 추가시켜야함.
// 0 = 아무하고도 안달림, 1 = 내 이전기록하고 달림,
public class Racing {

    // 아무하고도 안달릴 때
    public static int noOneRacing(UserUpdateInfo userUpdateInfo){

        return 0;
    }

    // 나의 옛날기록과 레이싱
    public static int myRacing(UserUpdateInfo userUpdateInfo){

        if(userUpdateInfo.getOldMyRecord() == null){
            System.out.println("oldMyRecord 없음");
            return -1;
        }else{
            userUpdateInfo.setOldOtherRecord(userUpdateInfo.getOldMyRecord());
        }

        return 1;
    }


    // 랜덤한 기록과 레이싱, 내 기록이 없을 때, 모든 기록이 없을때, 내 기록 밖에 없을 때?
    public static int randomRacing(UserUpdateInfo userUpdateInfo){
        List<UserRecordDto> top5Record = GetTop5Record.getTop5Record(userUpdateInfo.getUserId(), userUpdateInfo.getRouteId());

        if(userUpdateInfo.getOldMyRecord() == null){
            System.out.println("oldMyRecord 없음");
            return -1;
        }

        if(top5Record == null){
            System.out.println("top5 기록 없음(기록이 0개 또는 내것만 있음).");
            return -1;
        }

        userUpdateInfo.setOldOtherRecord(top5Record.get(top5Record.size()-1));

        return 2;
    }

    // 라이벌하고 레이싱
    public static int rivalRacing(UserUpdateInfo userUpdateInfo){
        return 3;
    }

}
