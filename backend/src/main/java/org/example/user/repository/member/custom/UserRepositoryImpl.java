package org.example.user.repository.member.custom;

import static org.example.post.domain.entity.QPostEntity.*;

import java.util.List;
import java.util.stream.Collectors;

import org.example.post.domain.dto.PostDto;
import org.example.post.domain.entity.HashtagEntity;
import org.example.post.domain.entity.PostEntity;
import org.example.user.domain.dto.UserDto;
import org.example.user.domain.entity.member.UserEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class UserRepositoryImpl implements UserRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	public UserRepositoryImpl(EntityManager entityManager) {
		this.jpaQueryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	public Integer postCount(UserEntity userEntity) {
		long count = jpaQueryFactory
			.selectFrom(postEntity)
			.where(postEntity.user.eq(userEntity))
			.fetchCount();
		return Math.toIntExact(count);
	}

	@Override
	public List<UserDto.UserGetPostsResponse> postList(UserEntity userEntity) {
		List<PostEntity> posts = jpaQueryFactory
			.selectFrom(postEntity)
			.where(postEntity.user.eq(userEntity))
			.fetch();

		return posts.stream()
			.map(postEntity1 -> new UserDto.UserGetPostsResponse(
				postEntity1.getImageId(),
				postEntity1.getPostContent(),
				postEntity1.getHashtagContents(),
				postEntity1.getLikeCount()
			))
			.toList();


	}
}
