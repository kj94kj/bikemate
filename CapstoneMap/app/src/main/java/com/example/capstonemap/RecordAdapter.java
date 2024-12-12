package com.example.capstonemap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.capstonemap.ranking.RankingDto;

import java.util.List;

public class RecordAdapter extends BaseAdapter {
    private Context context;
    private List<RankingDto> rankingList;

    public RecordAdapter(Context context, List<RankingDto> rankingList) {
        this.context = context;
        this.rankingList = rankingList;
    }

    @Override
    public int getCount() {
        return rankingList.size();
    }

    @Override
    public Object getItem(int position) {
        return rankingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_record, parent, false);
        }

        RankingDto ranking = rankingList.get(position);

        TextView userIdText = convertView.findViewById(R.id.userId_text);
        TextView elapsedTimeText = convertView.findViewById(R.id.elapsedTime_text);
        TextView rankText = convertView.findViewById(R.id.rank_text);

        Long elapsedTime= ranking.getElapsedTime();

        String curElapsedTime="99:99:99";
        // elapsedTime(밀리초)을 우리가 보는 시간으로 나타내는과정
        if (elapsedTime == 0) {

        }else{
            long hours = elapsedTime / 3600;
            long minutes = (elapsedTime % 3600) / 60;
            long seconds = elapsedTime % 60;

            curElapsedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }

        userIdText.setText("username"+String.valueOf(ranking.getUserId()));
        elapsedTimeText.setText(curElapsedTime);
        rankText.setText(ranking.getRank() + "등");

        return convertView;
    }
}

