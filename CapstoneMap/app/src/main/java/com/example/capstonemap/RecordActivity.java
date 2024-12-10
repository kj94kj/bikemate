package com.example.capstonemap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstonemap.ranking.GetRankings;
import com.example.capstonemap.ranking.RankingDto;
import com.example.capstonemap.routes.RouteDto;

import java.util.ArrayList;
import java.util.List;

// 랭크를 조회해야함.
public class RecordActivity extends AppCompatActivity{
    Intent intentRR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        intentRR = new Intent(RecordActivity.this, RouteManagementActivity.class);


        Long userId =(Long)getIntent().getSerializableExtra("userId");
        Long routeId=(Long)getIntent().getSerializableExtra("routeId");

        TextView myRankingView = findViewById(R.id.my_ranking);
        TextView noRankingMessage = findViewById(R.id.no_ranking_message);
        LinearLayout rankingListLayout = findViewById(R.id.ranking_list);

        List<RankingDto> rankingDtoList = GetRankings.getRankings(routeId);
        RankingDto myRanking = GetRankings.getMyRanking(userId, routeId);

        // 기록이 없을 경우 처리
        if ((rankingDtoList == null || rankingDtoList.isEmpty()) && myRanking == null) {
            noRankingMessage.setVisibility(View.VISIBLE);
            return;
        }

        // 나의 기록이 있을 경우 표시
        if (myRanking != null) {
            myRankingView.setVisibility(View.VISIBLE);
            myRankingView.setText("My Ranking: " + myRanking.getRank() + "\nTime: " + myRanking.getElapsedTime());
        }

        // 전체 기록 표시
        if (rankingDtoList != null && !rankingDtoList.isEmpty()) {
            for (RankingDto ranking : rankingDtoList) {
                TextView rankingView = new TextView(this);
                rankingView.setText(
                        "Rank: " + ranking.getRank() + " | User: " + ranking.getUserId() + " | Time: " + ranking.getElapsedTime()
                );
                rankingView.setTextSize(16);
                rankingView.setPadding(8, 8, 8, 8);
                rankingListLayout.addView(rankingView);
            }
        }

    }
    // getIntent로 받기만하면됨.
    // 자기 유저id와 routeId가 넘어와야함.
}
