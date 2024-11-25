package com.example.capstonemap.distance;

import static com.example.capstonemap.distance.DistanceTwoLocation.calculateDistance;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.List;

public class DistancePolyLine {
    // 폴리라인 상의 누적 거리 계산
    // 현재 매개변수가 encodedString을 가져와서 decode하는 방식과 List<LatLng> 을 가져오는 방식 2가지가 있는데
    // 매번 decode 하기보단 List<LatLng>을 가져오는 방식임
    // user 패키지의 GetUserRoutes 클래스의 selectRoute함수를 써서 List<LatLng>으로 저장하자

    public static List<LatLng> encodedToLatLng(String encodedString){
        return PolyUtil.decode(encodedString);
    }

    public static double calculateProgress(LatLng currentLocation, List<LatLng> polyline) {

        // 2. 현재 위치가 폴리라인 상에 있는지 확인
        // 여기부분을 바꿀 수 있음. 범위같은거 지정해서
        if (!PolyUtil.isLocationOnPath(currentLocation, polyline, true, 10)) {
            System.out.println("현재 위치가 경로 근처에 없습니다.");
            return -1; // 경로에서 벗어난 경우
        }

        // 3. 현재 위치의 폴리라인 상 가장 가까운 인덱스 찾기
        int closestIndex = PolyUtil.locationIndexOnPath(currentLocation, polyline, true, 10);

        // 4. 누적 거리 계산
        double totalDistance = 0;
        for (int i = 0; i < closestIndex; i++) {
            totalDistance += calculateDistance(polyline.get(i), polyline.get(i + 1));
        }

        // 마지막 세그먼트의 현재 위치까지의 거리 추가
        LatLng start = polyline.get(closestIndex);
        LatLng end = polyline.get(closestIndex + 1);
        totalDistance += calculateDistanceToSegment(currentLocation, start, end);

        return totalDistance;
    }

    // 현재 위치와 세그먼트 간의 투영 거리 계산
    private static double calculateDistanceToSegment(LatLng point, LatLng segmentStart, LatLng segmentEnd) {
        // PolyUtil.distanceToLine 메서드 활용
        return PolyUtil.distanceToLine(point, segmentStart, segmentEnd);
    }
}
