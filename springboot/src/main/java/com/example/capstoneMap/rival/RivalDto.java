package com.example.capstoneMap.rival;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RivalDto {
	private Long id;
	private Long userId;
	private List<Long> otherId;
	private Long routeId;
	
	public RivalDto(Long userId, List<Long> otherId, Long routeId) {
		this.userId = userId;
		this.otherId = otherId;
		this.routeId = routeId;
 	}
	
	public Rival toEntity() {
		return new Rival(id, userId, otherId, routeId);
	}
}
