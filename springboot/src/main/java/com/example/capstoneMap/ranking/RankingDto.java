package com.example.capstoneMap.ranking;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RankingDto {
    private Long id;
    private Long userId;    
    private Long routeId;        
    private Long elapsedTime;
    private int rank;    

}
