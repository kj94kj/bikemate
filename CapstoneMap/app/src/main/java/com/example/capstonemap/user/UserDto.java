package com.example.capstonemap.user;

import com.example.capstonemap.routes.RouteDto;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class UserDto {
    @SerializedName("id")
    private Long id;
    private String username;
    private String password;
    private List<RouteDto> userRoutes= new ArrayList<>();

    public UserDto(String username, String password){
        this.username=username;
        this.password=password;
    }

    public UserDto(Long id, String username, String password){
        this.id=id;
        this.username=username;
        this.password=password;
    }
}
