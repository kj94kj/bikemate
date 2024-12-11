package com.example.capstonemap;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstonemap.ranking.GetRankings;
import com.example.capstonemap.ranking.RankingDto;

import java.util.List;

public class RecordActivity extends AppCompatActivity {
    public static List<RankingDto> rankingDtoList;
    public static RankingDto myRanking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Long userId = (Long) getIntent().getSerializableExtra("userId");
        Long routeId = (Long) getIntent().getSerializableExtra("routeId");

        ImageButton backArrowButton = findViewById(R.id.back_arrow_button);
        ListView recordListView = findViewById(R.id.record_listView);

        TextView userIdText = findViewById(R.id.userId_text);
        TextView elapsedTimeText = findViewById(R.id.elapsedTime_text);
        TextView rankText = findViewById(R.id.rank_text);

        // 랭킹 리스트 조회
        GetRankings.getRankings(routeId, new GetRankings.RankingsCallback() {
            @Override
            public void onSuccess(List<RankingDto> rankings) {
                runOnUiThread(() -> {
                    if (rankings != null && !rankings.isEmpty()) {
                        RecordAdapter adapter = new RecordAdapter(RecordActivity.this, rankings);
                        recordListView.setAdapter(adapter);
                    }
                });
            }

            @Override
            public void onFailure() {
                runOnUiThread(() -> {
                    Log.e("RecordActivity", "랭킹 조회 실패");
                });
            }
        });

        // 개인 랭킹 조회
        GetRankings.getMyRanking(userId, routeId, new GetRankings.MyRankingCallback() {
            @Override
            public void onSuccess(RankingDto ranking) {
                runOnUiThread(() -> {
                    if (ranking != null) {
                        Long elapsedTime = ranking.getElapsedTime();
                        String curElapsedTime = formatElapsedTime(elapsedTime);

                        userIdText.setText(String.valueOf(userId));
                        elapsedTimeText.setText(curElapsedTime);
                        rankText.setText(ranking.getRank() + "등");

                        Log.d("end", "내기록: " + ranking.getUserId());
                        Log.d("end", "내기록: " + ranking.getElapsedTime());
                    }
                });
            }

            @Override
            public void onFailure() {
                runOnUiThread(() -> {
                    Log.e("RecordActivity", "내 기록 조회 실패");
                });
            }
        });

        backArrowButton.setOnClickListener(v -> onBackPressed());
    }

    // 밀리초를 시간 형식으로 변환
    private String formatElapsedTime(Long elapsedTime) {
        if (elapsedTime == null || elapsedTime == 0) {
            return "99:99:99";
        }
        long hours = elapsedTime / 3600;
        long minutes = (elapsedTime % 3600) / 60;
        long seconds = elapsedTime % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}

