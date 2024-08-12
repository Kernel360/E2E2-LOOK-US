package org.example.post.repository.custom;

import static org.apache.logging.log4j.util.Strings.*;
import static org.example.post.domain.entity.QHashtagEntity.*;
import static org.example.post.domain.entity.QPostEntity.*;
import static org.example.user.domain.entity.member.QUserEntity.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.post.domain.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
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

		BooleanBuilder builder = new BooleanBuilder();
		Predicate postContentCondition = postContentContains(searchCondition.getPostContent());
		Predicate hashtagContentCondition = hashtagContentEq(searchCondition.getHashtags());

		if (postContentCondition != null) {
			builder.and(postContentCondition);
		}
		if (hashtagContentCondition != null) {
			builder.and(hashtagContentCondition);
		}

		long total = queryFactory
			.select(postEntity.postId.countDistinct())
			.from(postEntity)
			.leftJoin(postEntity.user, userEntity).fetchJoin()
			// .leftJoin(postEntity.hashtags, hashtagEntity)
			.where(builder)
			.fetchCount();

		List<Tuple> results = queryFactory
			.select(userEntity.nickname, postEntity.postId, postEntity.imageId, hashtagEntity.hashtagContent).distinct()
			.from(postEntity)
			.leftJoin(postEntity.user, userEntity)
			.leftJoin(postEntity.hashtags, hashtagEntity)
			.where(builder)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		// Process results to group by postId
		Map<Long, PostDto.PostDtoResponse> postDtoMap = new HashMap<>();

		for (Tuple tuple : results) {
			String nickname = tuple.get(userEntity.nickname);
			Long postId = tuple.get(postEntity.postId);
			Long imageId = tuple.get(postEntity.imageId);
			String hashtagContent = tuple.get(hashtagEntity.hashtagContent);

			PostDto.PostDtoResponse dto = postDtoMap.computeIfAbsent(postId, id ->
				new PostDto.PostDtoResponse(nickname, id, imageId, new ArrayList<>())
			);

			if (hashtagContent != null && !dto.hashtags().contains(hashtagContent)) {
				dto.hashtags().add(hashtagContent);
			}
		}

		List<PostDto.PostDtoResponse> content = new ArrayList<>(postDtoMap.values());

		return new PageImpl<>(content, pageable, total);
	}

	private Predicate postContentContains(String postContent) {
		if (isEmpty(postContent)) {
			return null;
		}
		return postEntity.postContent.contains(postContent);
	}

	private Predicate hashtagContentEq(String hashtagContentList) {
		if (isEmpty(hashtagContentList)) {
			return null;
		}

		BooleanBuilder hBuilder = new BooleanBuilder();

		List<String> hashtags = splitString(hashtagContentList, "#");

		for (String hashtag : hashtags) {
			if (!hashtag.isEmpty()) {
				hBuilder.and(postEntity.hashtags.any().hashtagContent.eq(hashtag));
			}
		}
		return hBuilder;
	}

	public List<String> splitString(String str, String delimiter) {
		if (isEmpty(str)) {
			return List.of();
		}
		return List.of(str.split(delimiter));
	}

}
