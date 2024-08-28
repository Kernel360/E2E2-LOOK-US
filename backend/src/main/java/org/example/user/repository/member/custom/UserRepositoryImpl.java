package org.example.user.repository.member.custom;

import static org.example.post.domain.entity.QPostEntity.*;

import java.util.List;

import org.example.log.LogExecution;
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
	@LogExecution
	public Integer postCount(UserEntity userEntity) {
		long count = jpaQueryFactory
			.selectFrom(postEntity)
			.where(postEntity.user.eq(userEntity))
			.fetchCount();
		return Math.toIntExact(count);
	}

	@Override
	@LogExecution
	public List<UserDto.UserGetPostsResponse> postList(UserEntity userEntity) {
		List<PostEntity> posts = jpaQueryFactory
			.selectFrom(postEntity)
			.where(postEntity.user.eq(userEntity))
			.fetch();

		return posts.stream()
			.map(postEntity1 -> new UserDto.UserGetPostsResponse(
				postEntity1.getImageLocationId(),
				postEntity1.getPostContent(),
				postEntity1.getHashtagContents(),
				postEntity1.getLikeCount(),
				postEntity1.getPostId()
			))
			.toList();


	}
}
