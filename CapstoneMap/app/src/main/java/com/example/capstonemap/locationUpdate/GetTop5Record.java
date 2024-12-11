package com.example.capstonemap.locationUpdate;

import com.example.capstonemap.Racing.Racing;

import java.util.ArrayList;
import java.util.List;

public class GetTop5Record {
    static UserRecordRepository userRecordRepository = new UserRecordRepository();

    public static void getTop5Record(Long userId, Long routeId, OnTop5RecordsFetchedListener listener) {
        userRecordRepository.getTop5Record(userId, routeId,
                // 성공 시 처리
                top5Records -> {
                    listener.onSuccess(top5Records); // 데이터를 콜백으로 전달
                },
                // 실패 시 처리
                () -> {
                    listener.onFailure();
                }
        );
    }

    // 콜백 인터페이스 정의
    public interface OnTop5RecordsFetchedListener {
        void onSuccess(List<UserRecordDto> top5Records);
        void onFailure();
    }
}
