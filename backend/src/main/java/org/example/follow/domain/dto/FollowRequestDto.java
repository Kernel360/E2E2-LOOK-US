package org.example.follow.domain.dto;

import org.example.follow.domain.enums.FollowStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowRequestDto {
	private String nickname;
	private FollowStatus followStatus;
}
