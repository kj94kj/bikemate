package com.example.capstonemap.Racing;

import android.util.Log;

import com.example.capstonemap.MapsActivity;
import com.example.capstonemap.locationUpdate.GetOldRecord;
import com.example.capstonemap.locationUpdate.GetTop5Record;
import com.example.capstonemap.locationUpdate.UserRecordApiService;
import com.example.capstonemap.locationUpdate.UserRecordDto;
import com.example.capstonemap.locationUpdate.UserRecordRepository;
import com.example.capstonemap.locationUpdate.UserUpdateInfo;
import com.example.capstonemap.rival.GetRival;
import com.example.capstonemap.rival.RivalDto;
import com.example.capstonemap.routes.RouteDto;

import java.util.ArrayList;
import java.util.List;

// SetOldRecord에서 내 기록을 일단 추가시키고 함.
// 라이벌 모드를 추가시켜야함.
// 0 = 아무하고도 안달림, 1 = 내 이전기록하고 달림,
public class Racing {
    public static List<UserRecordDto> top5Record = new ArrayList<>();

    // 아무하고도 안달릴 때
    public static int noOneRacing(UserUpdateInfo userUpdateInfo){

        return 0;
    }

    // 나의 옛날기록과 레이싱
    public static int myRacing(UserUpdateInfo userUpdateInfo){
        Log.d("myRacing", "1st");

        Log.d("myRacing", "fffee");

        if(userUpdateInfo.getOldMyRecord() == null){
            Log.d("myRacing", "2st");
            System.out.println("oldMyRecord 없음");
            return -1;
        }else{
            Log.d("myRacing", "3st");
            userUpdateInfo.setOldOtherRecord(userUpdateInfo.getOldMyRecord());
        }

        return 1;
    }


    // 랜덤한 기록과 레이싱, 내 기록이 없을 때, 모든 기록이 없을때, 내 기록 밖에 없을 때?
    public static void randomRacing(UserUpdateInfo userUpdateInfo, OnRacingCompleteListener listener) {
        GetTop5Record.getTop5Record(userUpdateInfo.getUserId(), userUpdateInfo.getRouteId(), new GetTop5Record.OnTop5RecordsFetchedListener() {
            @Override
            public void onSuccess(List<UserRecordDto> top5Records) {
                if (top5Records == null || top5Records.isEmpty()) {
                    listener.onFailure("Top 5 기록 없음.");
                    return;
                }

                if (userUpdateInfo.getOldMyRecord() == null) {
                    listener.onFailure("내 이전 기록 없음.");
                    return;
                }

                // 가장 마지막 기록 사용
                userUpdateInfo.setOldOtherRecord(top5Records.get(top5Records.size() - 1));
                listener.onSuccess(2); // 성공 콜백으로 결과 반환

                MapsActivity.racingModeCheck = 2;
            }

            @Override
            public void onFailure() {
                listener.onFailure("기록 가져오기 실패.");
            }
        });
    }

    // Racing 완료 시 호출될 콜백 인터페이스
    public interface OnRacingCompleteListener {
        void onSuccess(int result);
        void onFailure(String errorMessage);
    }

    // 라이벌하고 레이싱
    public static int rivalRacing(UserUpdateInfo userUpdateInfo, Long otherId){
        RivalDto rivalDto=GetRival.getRival(userUpdateInfo.getUserId(), userUpdateInfo.getRouteId());

        if(rivalDto.getOtherId().contains(otherId)){
            GetOldRecord.getOldRecord(otherId, userUpdateInfo.getRouteId());
        }else{
            System.out.println("rival이 없음");
            return -1;
        }

        return 3;
    }

}
