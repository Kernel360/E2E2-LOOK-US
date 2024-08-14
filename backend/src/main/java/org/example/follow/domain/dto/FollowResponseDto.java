package org.example.follow.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowResponseDto {
	private String nickname;
	private int followersCount;
	private Long profileImageId;
}
