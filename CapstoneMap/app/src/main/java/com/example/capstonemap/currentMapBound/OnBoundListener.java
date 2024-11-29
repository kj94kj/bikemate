package com.example.capstonemap.currentMapBound;

import com.example.capstonemap.routes.RouteDto;

import java.util.List;

public interface OnBoundListener {
    void onBound(List<RouteDto> boundRouteDtoList);
}
