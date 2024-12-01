package com.example.capstonemap.rival;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RivalDto {
    private Long id;
    private Long userId;
    private List<Long> otherId;
    private Long routeId;

    public RivalDto(Long userId, List<Long> otherId, Long routeId){
        this.userId = userId;
        this.otherId = otherId;
        this.routeId = routeId;
    }
}
