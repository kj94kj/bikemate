package com.example.capstonemap.locationUpdate;


// 실시간의 좌표 정보와 속도를 받고 화면에 표시하며,
// UserUpdateDto에 루트가 끝나면 갱신하거나 OldUserRecord를 가져오는 클래스
// 아마 userId와 routeId를 밖에서 받을 수 있을 것 같다.

import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;

import com.example.capstonemap.AppContext;
import com.example.capstonemap.MapsActivity;
import com.example.capstonemap.R;
import com.example.capstonemap.distance.DistancePolyLine;
import com.example.capstonemap.distance.DistanceTwoLocation;
import com.example.capstonemap.polyLine.PolyLine;
import com.example.capstonemap.routes.RouteDto;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import android.media.SoundPool;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

    private RouteDto racingDto;

    //oldMyRecord: 나의 이전기록, oldOtherRecord: 경주하고 있는 기록
    private UserRecordDto myRecordDto = new UserRecordDto();   // 여기바꿈.
    private UserRecordDto oldMyRecord = null;
    private UserRecordDto oldOtherRecord = null;

    // 현재 유저의 시작점부터 거리, 기록 유저의 시작점부터 거리를 계산해서 넣어줄거임
    private double currentDistance = 0;
    private double oldDistance = 0;

    // 현재 달리고있는 폴리라인의 List<LatLng>을 set해야함.
    private List<LatLng> polyline;

    public static String finish;

    Context context = MapsActivity.getAppContext();
    SoundPool soundPool = new SoundPool.Builder().setMaxStreams(2).build();
    int soundOne = soundPool.load(context, R.raw.short_beep, 1);
    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);



    // 기록 유저의 n초마다의 위치를 set하기 위해 만듦
    // 끝점때문에 로직을 수정해야할수도있음.
    public void setOldLocation(){
        if(counter < oldOtherRecord.getLocationList().size() ){
            oldLocation=oldOtherRecord.getLocationList().get(counter);
            counter++;
        }else{
            // 정확히 루트 끝점이 아닌거같음.
            LatLng finalLocation = polyline.get(polyline.size()-1);
            oldLocation[0]=racingDto.getLocationList().get(1)[0];
            oldLocation[1]=racingDto.getLocationList().get(1)[1];
        }
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

        if(meDistance==-1 || otherDistance==-1){
            rankingNow=raceRanking;
        }

        if(counter >= oldOtherRecord.getLocationList().size())
            rankingNow=2;

        if(counter > 1 && raceRanking !=rankingNow){
            raceRanking=rankingNow;
            raceAlarm(raceRanking);
        }

        return rankingNow;
    }

    public void raceAlarm(int raceRanking){
        if(raceRanking == 1){
            vibrator.vibrate(200);
            soundPool.play(soundOne, 1, 1, 0, 0, 1);
            System.out.println("진동소리 1등");
            Toast.makeText(MapsActivity.getAppContext(), "1등 입니다.", Toast.LENGTH_SHORT).show();
        }else if(raceRanking == 2){
            vibrator.vibrate(new long[]{0, 200, 150, 200}, -1); // 패턴: [시작 지연, 진동, 쉬는 시간, 진동]
            soundPool.play(soundOne, 1, 1, 0, 0, 1);
            new Handler().postDelayed(() -> soundPool.play(soundOne, 1, 1, 0, 0, 1), 300);
            System.out.println("진동소리2등");
            Toast.makeText(MapsActivity.getAppContext(), "2등 입니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public UserUpdateInfo(Long userId, Long routeId){
        this.userId = userId;
        this.routeId = routeId;
    }

    // 경주가 끝나고 내 예전 기록보다 내 현재 기록이 더 빠르면(작으면) 갱신하는 함수
    public void setUserRecordDto(){
        String curElapsedTime="00:00:00";
        // elapsedTime(밀리초)을 우리가 보는 시간으로 나타내는과정
        if (elapsedTime == 0) {

        }else{
            long hours = elapsedTime / 3600;
            long minutes = (elapsedTime % 3600) / 60;
            long seconds = elapsedTime % 60;

            curElapsedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }

        if(oldMyRecord == null){
            Log.d("end", "현재 기록은 "+curElapsedTime+" 입니다.");
            Log.d("end", "기록이 저장 됩니다.");

            String a = "현재 기록은 "+curElapsedTime+" 입니다.";
            String b = "\n기록이 저장 됩니다.";
            myRecordDto.setUserId(userId);
            myRecordDto.setRouteId(routeId);
            myRecordDto.setLocationList(locationList);
            myRecordDto.setElapsedTime(elapsedTime);

            Log.d("end", "현재 id은 "+userId+" 입니다.");


            // 이거하면 자동으로 랭킹은 저장됨.
            SaveMyRecord.saveMyRecord(myRecordDto, userId, routeId);

            finish = a+b;
        }else{
            String oldElapsedTime = "00:00:00";

            if (oldMyRecord.getElapsedTime() == 0) {

            }else{
                long hours = oldMyRecord.getElapsedTime() / 3600;
                long minutes = (oldMyRecord.getElapsedTime() % 3600) / 60;
                long seconds = oldMyRecord.getElapsedTime() % 60;

                oldElapsedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            }

            // 이 메시지가 경주가 끝나면 출력되야함.
            Log.d("end", "현재 기록은 "+curElapsedTime+" 입니다.");
            Log.d("end", "다른 기록은 "+oldElapsedTime+" 입니다.");

            String a="현재 기록은 "+curElapsedTime+" 입니다.";
            String b="\n다른 기록은 "+oldElapsedTime+" 입니다.";
            String sin ="\n";

            if(oldMyRecord.getElapsedTime() - elapsedTime > 0){
                Log.d("sin", "기록이 갱신 됩니다.");
                sin="기록이 갱신 됩니다.";
                myRecordDto.setUserId(userId);
                myRecordDto.setRouteId(routeId);
                myRecordDto.setLocationList(locationList);
                myRecordDto.setElapsedTime(elapsedTime);

                // 이거하면 자동으로 랭킹은 저장됨.
                SaveMyRecord.saveMyRecord(myRecordDto, userId, routeId);
            }
            finish = a+b+sin;

        }
    }

    public void setLocation(Double[] currentLocation){
        this.currentLocation=currentLocation;
        locationList.add(currentLocation);
    }

    public void setRacingDto(RouteDto routeDto){
        racingDto = routeDto;
        polyline = PolyLine.decodePolyline(racingDto.getEncodedPath());
    }

    public void showCenteredSnackbar(View parentView, String message) {
        Snackbar snackbar = Snackbar.make(parentView, message, Snackbar.LENGTH_LONG);

        // Snackbar의 View 가져오기
        View snackbarView = snackbar.getView();

        // Snackbar를 중앙으로 이동
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
        params.gravity = Gravity.CENTER;
        snackbarView.setLayoutParams(params);

        snackbar.show();
    }
}