package org.example.post.repository.custom;

import static org.example.post.domain.entity.QLikeEntity.*;

import java.time.LocalDateTime;

import org.example.log.LogExecution;
import org.example.post.domain.entity.PostEntity;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class LikeRepositoryImpl implements LikeRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public LikeRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	@LogExecution
	public Integer likeCount(PostEntity post) {
		long count = queryFactory
			.selectFrom(likeEntity)
			.where(likeEntity.post.eq(post))
			.fetchCount();

		return Math.toIntExact(count);
	}

	@Transactional
	@Override
	@LogExecution
	public void deleteAllByPost(PostEntity post) {


		queryFactory.update(likeEntity)
			.set(likeEntity.removedAt, LocalDateTime.now())  // removed_at 필드를 현재 시간으로 업데이트
			.where(likeEntity.post.eq(post))  // 특정 PostEntity에 해당하는 모든 LikeEntity를 대상으로 함
			.execute();
	}
}
