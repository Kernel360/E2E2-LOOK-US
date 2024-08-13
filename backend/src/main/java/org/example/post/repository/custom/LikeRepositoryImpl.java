package org.example.post.repository.custom;

import static org.example.post.domain.entity.QLikeEntity.*;

import org.example.post.domain.entity.PostEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class LikeRepositoryImpl implements LikeRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public LikeRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	public Integer likeCount(PostEntity post) {
		long count = queryFactory
			.selectFrom(likeEntity)
			.where(likeEntity.post.eq(post))
			.fetchCount();

		return Math.toIntExact(count);
	}
}
