package com.example.capstonemap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.capstonemap.routes.RouteDto;

import java.util.List;

public class RouteDtoAdapter extends BaseAdapter {
    private Context context;
    private List<RouteDto> routes;
    private OnRouteActionListener listener;

    public interface OnRouteActionListener {
        void onRouteClick(RouteDto route);
        void onRecordButtonClick(RouteDto route);
    }

    public RouteDtoAdapter(Context context, List<RouteDto> routes, OnRouteActionListener listener) {
        this.context = context;
        this.routes = routes;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return routes.size();
    }

    @Override
    public Object getItem(int position) {
        return routes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_route, parent, false);
        }

        RouteDto route = routes.get(position);

        TextView titleTextView = convertView.findViewById(R.id.route_title);
        TextView lengthTextView = convertView.findViewById(R.id.route_length);
        Button recordButton = convertView.findViewById(R.id.record_button);

        double tempLength = route.getLength();
        int itempLength = (int)tempLength;
        double dtempLength =(double) itempLength / 1000;

        titleTextView.setText("Name: " + route.getName());
        lengthTextView.setText("Length: " + dtempLength + " km");

        View itemContent = convertView.findViewById(R.id.ComplexTextLayout);
        itemContent.setOnClickListener(v -> listener.onRouteClick(route));

        recordButton.setOnClickListener(v -> listener.onRecordButtonClick(route));


        return convertView;
    }
}