package com.example.capstoneMap.ranking;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Ranking {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private Long userId;
	@Column
	private Long routeId;
	private Long elapsedTime;
	private int rank;
	
	public Ranking(Long userId, Long routeId, Long elapsedTime, int rank) {
		this.userId = userId;
		this.routeId = routeId;
		this.elapsedTime = elapsedTime;
		this.rank = rank;
		}
}
