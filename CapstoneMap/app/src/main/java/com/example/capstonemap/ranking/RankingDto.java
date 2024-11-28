package com.example.capstonemap.ranking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RankingDto {

    private Long id;
    private Long userId;     // 사용자 ID
    private Long routeId;    // 경로 ID
    private int rank;        // 등수
    private long elapsedTime; // 소요 시간 (빠른 순위 조회를 위해 포함)
}
