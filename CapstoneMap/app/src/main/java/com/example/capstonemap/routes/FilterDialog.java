package com.example.capstonemap.routes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.capstonemap.R;
import com.google.android.material.slider.RangeSlider;

import java.util.List;

// 현재 이 클래스는 rangeslider를 이용한 length(길이범위 정하기 임)
public class FilterDialog {
    public interface FilterDialogListener {
        void onFilterApplied(int minLength, int maxLength);
    }

    public static void showSeekBarDialog(Context context, FilterDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = View.inflate(context, R.layout.dialog_filter_seekbar, null);
        builder.setView(dialogView);

        SeekBar minSeekBar = dialogView.findViewById(R.id.min_seekbar);
        SeekBar maxSeekBar = dialogView.findViewById(R.id.max_seekbar);
        TextView rangeValueText = dialogView.findViewById(R.id.range_value_text);

        minSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int maxValue = maxSeekBar.getProgress();
                if (progress > maxValue) {
                    minSeekBar.setProgress(maxValue); // 최소값이 최대값을 넘지 않도록 제한
                }
                rangeValueText.setText("Range: " + minSeekBar.getProgress() + " km - " + maxSeekBar.getProgress() + " km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        maxSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int minValue = minSeekBar.getProgress();
                if (progress < minValue) {
                    maxSeekBar.setProgress(minValue); // 최대값이 최소값보다 작지 않도록 제한
                }
                rangeValueText.setText("Range: " + minSeekBar.getProgress() + " km - " + maxSeekBar.getProgress() + " km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        dialogView.findViewById(R.id.apply_button).setOnClickListener(v -> {
            int minLength = minSeekBar.getProgress()*1000;
            int maxLength = maxSeekBar.getProgress()*1000;
            listener.onFilterApplied(minLength, maxLength);
        });

        dialogView.findViewById(R.id.cancel_button).setOnClickListener(v -> {
            AlertDialog dialog = builder.create();
            dialog.dismiss();
        });

        builder.create().show();
    }
}
