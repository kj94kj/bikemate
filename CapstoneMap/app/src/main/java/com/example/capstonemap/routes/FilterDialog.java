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
        TextView minValueText = dialogView.findViewById(R.id.min_value_text);
        TextView maxValueText = dialogView.findViewById(R.id.max_value_text);

        minSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                minValueText.setText("Min Value: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        maxSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                maxValueText.setText("Max Value: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        dialogView.findViewById(R.id.apply_button).setOnClickListener(v -> {
            int minLength = minSeekBar.getProgress();
            int maxLength = maxSeekBar.getProgress();
            listener.onFilterApplied(minLength, maxLength);
        });

        dialogView.findViewById(R.id.cancel_button).setOnClickListener(v -> {
            AlertDialog dialog = builder.create();
            dialog.dismiss();
        });

        builder.create().show();
    }
}
