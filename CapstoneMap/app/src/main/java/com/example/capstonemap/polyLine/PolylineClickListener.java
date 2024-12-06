package com.example.capstonemap.polyLine;

import android.content.Context;
import android.widget.Toast;

import com.example.capstonemap.AppContext;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Polyline;

public class PolylineClickListener  implements GoogleMap.OnPolylineClickListener{


    //폴리라인 클릭 리스너
    // 달리기 전 페이지로 가건나하면됨.(가장 최선은 코스 아이템을 상단에 띄우는것)
    // PolylineClickListener 설정, 실행방법
    //PolylineClickListener polylineClickListener = new PolylineClickListener();
    //mMap.setOnPolylineClickListener(polylineClickListener); // 리스너 연결
    @Override
    public void onPolylineClick(Polyline clickedPolyline) {
        Context context = AppContext.getAppContext();
        String message = "Clicked on Polyline with ID: " + clickedPolyline.getId();
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

        clickedPolyline.setColor(0xFF00FF00);
    }
}
