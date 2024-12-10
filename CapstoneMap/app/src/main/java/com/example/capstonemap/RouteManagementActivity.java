package com.example.capstonemap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstonemap.routes.RouteDto;
import com.example.capstonemap.user.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// !! 원래는 자기 기록이있는걸 가져와야함
// putEaxtra를 분기로 주자.
public class RouteManagementActivity extends AppCompatActivity {
    Intent intentRM;
    Intent intentRR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_management);// 레이아웃 파일 연결

        LinearLayout container = findViewById(R.id.container);

        ImageButton backArrowButton = findViewById(R.id.back_arrow_button);

        ImageButton createCourseButton = findViewById(R.id.create_course_button);

        intentRM = new Intent(RouteManagementActivity.this, MapsActivity.class);
        // intentRM.putExtra("source", "RouteManagementActivity");

        intentRR = new Intent(RouteManagementActivity.this, RecordActivity.class);

        // 사실 boundRouteDto아님
        // 여기 12.10
        ArrayList<RouteDto> boundedRouteDto = (ArrayList<RouteDto>) getIntent().getSerializableExtra("userIdRecordDtoList");
        ArrayList<RouteDto> allRouteDto = (ArrayList<RouteDto>) getIntent().getSerializableExtra("allRouteDtoList");
        Long userId = (Long) getIntent().getSerializableExtra("userId");

        if (boundedRouteDto != null && allRouteDto != null) {
            // boundedRouteDto에서 id만 추출하여 Set으로 변환
            Set<Long> boundedIds = boundedRouteDto.stream()
                    .map(RouteDto::getId)
                    .collect(Collectors.toSet());

            // allRouteDto에서 boundedIds에 포함된 RouteDto만 필터링
            List<RouteDto> intersection = allRouteDto.stream()
                    .filter(route -> boundedIds.contains(route.getId()))
                    .collect(Collectors.toList());


            if (intersection != null) {
                for (RouteDto route : intersection) {

                    LinearLayout layout = new LinearLayout(this);
                    layout.setOrientation(LinearLayout.HORIZONTAL);
                    layout.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    layout.setPadding(16, 16, 16, 16);

                    String name = route.getName();
                    double length = route.getLength();

                    // 동적 TextView 생성
                    TextView textView = new TextView(this);
                    textView.setText("Name: " + name + "\nLength: " + length);
                    textView.setTextSize(18);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1f
                    ));
                    textView.setSingleLine(false); // 한 줄 제한 비활성화
                    textView.setEllipsize(null); // 생략(...) 방지
                    textView.setMaxLines(Integer.MAX_VALUE); // 최대 줄 수 제한 제거

                    // 클릭 이벤트 추가
                    textView.setOnClickListener(v -> {
                        // 코스 준비상태로 넘어감.

                        intentRM.putExtra("source", "RouteManagementActivity");
                        // 클릭 이벤트 처리
                        intentRM.putExtra("racingRoute", route);
                        startActivity(intentRM);
                        Toast.makeText(this, name + " clicked!", Toast.LENGTH_SHORT).show();
                    });

                    Button button = new Button(this);
                    button.setText("기록창");
                    LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, // 버튼 너비는 내용만큼
                            LinearLayout.LayoutParams.WRAP_CONTENT  // 버튼 높이는 내용만큼
                    );
                    button.setLayoutParams(buttonParams);
                    button.setOnClickListener(v -> {
                        intentRR.putExtra("userId", userId);
                        intentRR.putExtra("routeId", route.getId());
                        startActivity(intentRR);
                        // Button 클릭 이벤트 처리
                        Toast.makeText(this, name + " 기록창 열기", Toast.LENGTH_SHORT).show();
                    });

                    // TextView와 Button을 레이아웃에 추가
                    layout.addView(textView);
                    layout.addView(button);

                    // TextView를 LinearLayout에 추가
                    container.addView(layout);

                }
            }

            backArrowButton.setOnClickListener(v -> {
                // 뒤로 가기 버튼 클릭 시 동작
                onBackPressed(); // 시스템의 뒤로가기 동작 수행
            });

        /*createCourseButton.setOnClickListener(v -> {
            // 버튼 클릭 시 동작

            intentRM.putExtra("routeCreation", "RouteManagementActivity");
            startActivity(intentRM);

            // Toast 메시지로 사용자에게 피드백 제공
        });*/

        }
    }
}
