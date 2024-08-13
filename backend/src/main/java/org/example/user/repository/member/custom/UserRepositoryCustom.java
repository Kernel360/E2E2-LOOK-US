package org.example.user.repository.member.custom;

import java.util.List;

import org.example.user.domain.dto.UserDto;
import org.example.user.domain.entity.member.UserEntity;

public interface UserRepositoryCustom {
	Integer postCount(UserEntity userEntity);

	List<UserDto.UserGetPostsResponse> postList(UserEntity userEntity);
}
