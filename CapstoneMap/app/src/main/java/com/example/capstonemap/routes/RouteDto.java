package com.example.capstonemap.routes;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteDto {
    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @SerializedName("encodedPath")
    private String encodedPath;

    @SerializedName("locationList")
    private ArrayList<Double[]> locationList;

    @SerializedName("userId")
    private Long userId;

    @SerializedName("startLocation")
    private Double[] startLocation;

    @SerializedName("length")
    private Double length;

    /* 나중에 추가할거
    루트길이, 시작점, 루트 이름 <- 나중에 지어주는 칸을 만들어야함 등등
     */

    public RouteDto(String name, String encodedPath, ArrayList<Double[]> locationList, Double[] startLocation, Double length){
        this.name = name;
        this.encodedPath = encodedPath;
        this.locationList = locationList;
        this.startLocation = startLocation;
        this.length = length;
    }

}
