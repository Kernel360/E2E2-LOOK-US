package org.example.post.repository.custom;

import static org.apache.logging.log4j.util.Strings.*;
import static org.example.post.domain.entity.QPostEntity.postEntity;
import static org.example.user.domain.entity.member.QUserEntity.userEntity;

import java.util.List;

import org.example.post.domain.dto.PostDto;
import org.example.post.domain.dto.QPostDto_PostDtoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class PostRepositoryImpl implements PostRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public PostRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	public Page<PostDto.PostDtoResponse> search(PostSearchCondition searchCondition, Pageable pageable) {
		// pageableString postContent;
		// List<String> hashtags;
		// if (postSearchCondition.getPostContent() != null) {
		// 	postContent = postSearchCondition.getPostContent();
		// }
		// if(postSearchCondition.getHashtags() != null) {
		// 	hashtags = postSearchCondition.getHashtags();
		// }

		List<PostDto.PostDtoResponse> content = queryFactory
			.select(new QPostDto_PostDtoResponse(
				userEntity.nickname,
				postEntity.postId,
				postEntity.imageId
			))
			.from(postEntity)
			.leftJoin(postEntity.user, userEntity)
			.where(postContentEq(searchCondition.getPostContent()))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		long total = queryFactory
			.select(postEntity)
			.from(postEntity)
			.leftJoin(postEntity.user, userEntity)
			.where(postContentEq(searchCondition.getPostContent()))
			.fetchCount();

		return new PageImpl<>(content, pageable, total);

	}

	private Predicate postContentEq(String postContent) {
		if (isEmpty(postContent)) {
			return null;
			// throw new IllegalArgumentException("postContent is empty");
		}
		return postEntity.postContent.eq(postContent);
	}
}
