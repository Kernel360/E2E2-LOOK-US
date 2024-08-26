package org.example.image.redis.domain.entity;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisHash;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RedisHash("Color") // Redis에 저장될 해시 이름
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ColorEntity implements Serializable {

	@Id
	private String name;

	@Setter
	private Integer r;
	@Setter
	private Integer g;
	@Setter
	private Integer b;

	private Double score;

	// never changed after first build
	private Integer originR;
	private Integer originG;
	private Integer originB;
}
