package com.example.capstonemap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstonemap.routes.RouteDto;

import java.util.ArrayList;
import java.util.List;

// 원래는 자기 기록이있는걸 가져와야함
public class RouteManagementActivity extends AppCompatActivity {
    Intent intentRM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_management); // 레이아웃 파일 연결

        LinearLayout container = findViewById(R.id.container);

        intentRM = new Intent(RouteManagementActivity.this, MapsActivity.class);
        intentRM.putExtra("source", "RouteManagementActivity");

        ArrayList<RouteDto> boundedRouteDto = (ArrayList<RouteDto>) getIntent().getSerializableExtra("arrayBoundRouteDtoList");

        if (boundedRouteDto != null){
            for (RouteDto route : boundedRouteDto) {


                String name = route.getName();
                double length = route.getLength();

                // 동적 TextView 생성
                TextView textView = new TextView(this);
                textView.setText("Name: " + name + ", Length: " + length);
                textView.setTextSize(18);
                textView.setPadding(10, 10, 10, 10);

                // 클릭 이벤트 추가
                textView.setOnClickListener(v -> {
                    // 클릭 이벤트 처리
                    intentRM.putExtra("racingRoute", route);
                    startActivity(intentRM);
                    Toast.makeText(this, name + " clicked!", Toast.LENGTH_SHORT).show();
                });

                // TextView를 LinearLayout에 추가
                container.addView(textView);

            }
        }
    }
}
