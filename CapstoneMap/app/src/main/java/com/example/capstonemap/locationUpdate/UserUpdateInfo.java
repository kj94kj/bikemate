package com.example.capstonemap.locationUpdate;


// 실시간의 좌표 정보와 속도를 받고 화면에 표시하며,
// UserUpdateDto에 루트가 끝나면 갱신하거나 OldUserRecord를 가져오는 클래스
// 아마 userId와 routeId를 밖에서 받을 수 있을 것 같다.

import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;

import com.example.capstonemap.AppContext;
import com.example.capstonemap.R;
import com.example.capstonemap.distance.DistancePolyLine;
import com.example.capstonemap.distance.DistanceTwoLocation;
import com.google.android.gms.maps.model.LatLng;

import android.media.SoundPool;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

    private List<Double[]> locationList = new ArrayList<>();

    //내 기록 순위
    private int ranking;

    //내 경주 순위(ranking과 다른 것 주의)
    private int raceRanking=0;

    //oldMyRecord: 나의 이전기록, oldOtherRecord: 경주하고 있는 기록
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
        if(counter < oldOtherRecord.getLocationList().size() )
            oldLocation=oldOtherRecord.getLocationList().get(counter);
        counter++;
    }

    // 현재 내가 1등인지 2등인지 알려줌.
    public int raceRankingNow(){
        double meDistance = DistancePolyLine.calculateProgress(DistanceTwoLocation.doubleToLatLng(currentLocation), polyline);
        double otherDistance = DistancePolyLine.calculateProgress(DistanceTwoLocation.doubleToLatLng(oldLocation), polyline);

        int rankingNow;

        if(meDistance - otherDistance > 0)
            rankingNow=1;
        else
            rankingNow=2;

        if(raceRanking!=0 && raceRanking !=rankingNow){
            raceRanking=rankingNow;
            raceAlarm(raceRanking);
        }

        return rankingNow;
    }

    public void raceAlarm(int raceRanking){
        Context context = AppContext.getAppContext();
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        SoundPool soundPool = new SoundPool.Builder().setMaxStreams(2).build();
        int soundOne = soundPool.load(context, R.raw.short_beep, 1);
        if(raceRanking == 1){
            vibrator.vibrate(200);
            soundPool.play(soundOne, 1, 1, 0, 0, 1);
        }else if(raceRanking == 2){
            vibrator.vibrate(new long[]{0, 200, 150, 200}, -1); // 패턴: [시작 지연, 진동, 쉬는 시간, 진동]
            soundPool.play(soundOne, 1, 1, 0, 0, 1);
            new Handler().postDelayed(() -> soundPool.play(soundOne, 1, 1, 0, 0, 1), 150);
        }
    }

    public UserUpdateInfo(Long userId, Long routeId){
        this.userId = userId;
        this.routeId = routeId;
    }

    // 경주가 끝나고 내 예전 기록보다 내 현재 기록이 더 빠르면(작으면) 갱신하는 함수
    public void setUserRecordDto(){
        // elapsedTime(밀리초)을 우리가 보는 시간으로 나타내는과정
        LocalDateTime dateTime = Instant.ofEpochMilli(elapsedTime)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String curElapsedTime = dateTime.format(formatter);

        if(oldMyRecord == null){
            System.out.println("현재 기록은 "+curElapsedTime+" 입니다.");
            System.out.println("기록이 저장 됩니다.");
            myRecordDto.setLocationList(locationList);
            myRecordDto.setElapsedTime(elapsedTime);

            // 이거하면 자동으로 랭킹은 저장됨.
            SaveMyRecord.saveMyRecord(myRecordDto, userId, routeId);
        }else{
            LocalDateTime dateTime2 = Instant.ofEpochMilli(oldMyRecord.getElapsedTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String oldElapsedTime = dateTime2.format(formatter2);


            // 이 메시지가 경주가 끝나면 출력되야함.
            System.out.println("현재 기록은 "+curElapsedTime+" 입니다.");
            System.out.println("이전 기록은 "+oldElapsedTime+" 입니다.");

            if(oldMyRecord.getElapsedTime() - elapsedTime > 0){
                System.out.println("기록이 갱신 됩니다.");
                myRecordDto.setLocationList(locationList);
                myRecordDto.setElapsedTime(elapsedTime);

                // 이거하면 자동으로 랭킹은 저장됨.
                SaveMyRecord.saveMyRecord(myRecordDto, userId, routeId);
            }
        }
    }

    public void setLocation(Double[] currentLocation){
        this.currentLocation=currentLocation;
        locationList.add(currentLocation);
    }
}