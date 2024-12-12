package com.example.capstonemap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstonemap.routes.RouteDto;
import com.example.capstonemap.user.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// !! 원래는 자기 기록이있는걸 가져와야함
// putEaxtra를 분기로 주자.
public class RouteManagementActivity extends AppCompatActivity {
    Intent intentRM;
    Intent intentRR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_management);

        ImageButton backArrowButton = findViewById(R.id.back_arrow_button);
        ListView listView = findViewById(R.id.route_list_view);

        intentRM = new Intent(RouteManagementActivity.this, MapsActivity.class);
        intentRR = new Intent(RouteManagementActivity.this, RecordActivity.class);

        ArrayList<RouteDto> boundedRouteDto = (ArrayList<RouteDto>) getIntent().getSerializableExtra("userIdRecordDtoList");
        ArrayList<RouteDto> allRouteDto = (ArrayList<RouteDto>) getIntent().getSerializableExtra("allRouteDtoList");
        Long userId = (Long) getIntent().getSerializableExtra("userId");

        if (boundedRouteDto != null && allRouteDto != null) {
            Map<Long, RouteDto> routeMap = Stream.concat(boundedRouteDto.stream(), allRouteDto.stream())
                    .collect(Collectors.toMap(
                            RouteDto::getId,   // Key: RouteDto의 ID
                            route -> route,    // Value: RouteDto 객체
                            (existing, replacement) -> existing // 중복 시 기존 값 유지
                    ));

            // Map에서 중복 제거된 RouteDto를 List로 변환
            List<RouteDto> union = new ArrayList<>(routeMap.values());

            RouteDtoAdapter adapter = new RouteDtoAdapter(this, union, new RouteDtoAdapter.OnRouteActionListener() {
                @Override
                public void onRouteClick(RouteDto route) {
                    intentRM.putExtra("source", "RouteManagementActivity");
                    intentRM.putExtra("racingRoute", route);
                    startActivity(intentRM);
                    Toast.makeText(RouteManagementActivity.this, route.getName() + " 를 선택합니다.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onRecordButtonClick(RouteDto route) {
                    intentRR.putExtra("userId", userId);
                    intentRR.putExtra("routeId", route.getId());
                    startActivity(intentRR);
                    Toast.makeText(RouteManagementActivity.this, route.getName() + " 기록창 열기", Toast.LENGTH_SHORT).show();
                }
            });

            listView.setAdapter(adapter);
        }

        backArrowButton.setOnClickListener(v -> onBackPressed());
    }
}