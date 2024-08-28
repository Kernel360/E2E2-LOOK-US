package org.example.post.repository.custom;

import static org.example.post.domain.entity.QCategoryEntity.*;
import static org.example.post.domain.entity.QHashtagEntity.*;
import static org.example.post.domain.entity.QPostEntity.*;
import static org.example.user.domain.entity.member.QUserEntity.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.example.config.log.LogExecution;
import org.example.post.domain.dto.PostDto;
import org.example.post.domain.dto.PostStatsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostRepositoryImpl implements PostRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public PostRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	@LogExecution
	public Page<PostDto.PostDtoResponse> search(PostSearchCondition searchCondition, Pageable pageable) {

		BooleanBuilder builder = new BooleanBuilder();
		Predicate postContentCondition = postContentContains(searchCondition.getPostContent());
		Predicate hashtagContentCondition = hashtagContentEq(searchCondition.getHashtags());
		Predicate categoryContentCondition = categoryContentEq(searchCondition.getCategory());

		if (postContentCondition != null) {
			builder.and(postContentCondition);
		}
		if (hashtagContentCondition != null) {
			builder.and(hashtagContentCondition);
		}
		if (categoryContentCondition != null) {
			builder.and(categoryContentCondition);
		}

		long total = queryFactory
			.select(postEntity.postId.countDistinct())
			.from(postEntity)
			.leftJoin(postEntity.user, userEntity).fetchJoin()
			// .leftJoin(postEntity.hashtags, hashtagEntity)
			.where(builder)
			.fetchCount();

		List<Tuple> results = queryFactory
			.select(
				userEntity.nickname,
				postEntity.postId,
				hashtagEntity.hashtagContent,
				postEntity.imageLocationId,
				postEntity.likeCount,
				postEntity.hits,
				postEntity.createdAt,
				categoryEntity.categoryContent
			).distinct()
			.from(postEntity)
			.leftJoin(postEntity.user, userEntity)
			.leftJoin(postEntity.hashtags, hashtagEntity)
			.leftJoin(postEntity.categories, categoryEntity)
			.where(builder)
			.fetch();

		// Process results to group by postId
		Map<Long, PostDto.PostDtoResponse> postDtoMap = new HashMap<>();

		for (Tuple tuple : results) {
			String nickname = tuple.get(userEntity.nickname);
			Long postId = tuple.get(postEntity.postId);
			Long imageLocationId = tuple.get(postEntity.imageLocationId);
			String hashtagContent = tuple.get(hashtagEntity.hashtagContent);
			String categoryContent = tuple.get(categoryEntity.categoryContent);
			LocalDateTime createdAt = tuple.get(postEntity.createdAt);
			int likeCount = Optional.ofNullable(tuple.get(postEntity.likeCount)).orElse(0);
			int hits = Optional.ofNullable(tuple.get(postEntity.hits)).orElse(0);

			PostDto.PostDtoResponse dto = postDtoMap.computeIfAbsent(postId, id ->
				new PostDto.PostDtoResponse(nickname, id, imageLocationId, new ArrayList<>(), splitString(categoryContent, ","), likeCount, hits, createdAt)
			);

			if (hashtagContent != null && !dto.hashtags().contains(hashtagContent)) {
				dto.hashtags().add(hashtagContent);
			}
		}

		List<PostDto.PostDtoResponse> content = new ArrayList<>(postDtoMap.values());

		Sort sort = pageable.getSort();

		List<PostDto.PostDtoResponse> sortedPosts = null;
		if (sort.isSorted()) {
			sortedPosts = sortPosts(sort, content, pageable);
		}
		log.info("page " + pageable.getPageNumber());
		return new PageImpl<>(sortedPosts, pageable, total);
	}

	@Override
	@LogExecution
	@Transactional
	public int updateView(Long postId) {
		return Math.toIntExact(queryFactory
			.update(postEntity)
			.set(postEntity.hits, postEntity.hits.add(1))
			.set(postEntity.todayHits, postEntity.todayHits.add(1))  // 오늘의 조회수도 증가
			.where(postEntity.postId.eq(postId))
			.execute());
	}


	@Override
	@LogExecution
	public List<PostStatsDto> findPostStatsByType() {
		return queryFactory
			.select(Projections.constructor(PostStatsDto.class,
				postEntity.postStatus,
				postEntity.hits.sum(),
				postEntity.likeCount.sum()))
			.from(postEntity)
			.groupBy(postEntity.postStatus)
			.fetch();
	}

	private Predicate postContentContains(String postContent) {
		if (postContent == null || postContent.isBlank()) {
			return null;
		}
		return postEntity.postContent.contains(postContent);
	}

	private Predicate hashtagContentEq(String hashtagContentList) {
		if (hashtagContentList == null || hashtagContentList.isBlank()) {
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

	private Predicate categoryContentEq(String categoryContent) {
		if (categoryContent == null || categoryContent.isBlank()) {
			return null;
		}
		return categoryEntity.categoryContent.eq(categoryContent);
	}

	private List<String> splitString(String str, String delimiter) {
		if (str == null || str.isBlank()) {
			return Collections.emptyList();
		}
		return Arrays.stream(str.split(delimiter))
			.filter(s -> !s.isEmpty())
			.collect(Collectors.toList());
	}

	@LogExecution
	public static List<PostDto.PostDtoResponse> sortPosts(Sort sort, List<PostDto.PostDtoResponse> content,
		Pageable pageable) {
		final Map<String, Function<PostDto.PostDtoResponse, Comparable>> COMPARATORS = new HashMap<>();
		COMPARATORS.put("createdAt", PostDto.PostDtoResponse::createdAt);

		Comparator<PostDto.PostDtoResponse> comparator = null;

		for (Sort.Order order : sort) {    // TODO: sort 조건 추가 입력 가능 1. createdAt, 2. ?
			Comparator<PostDto.PostDtoResponse> newComparator;

			newComparator = Comparator.comparing(COMPARATORS.get(order.getProperty()));

			if (order.isDescending()) {
				newComparator = newComparator.reversed();
			}

			comparator = comparator == null ? newComparator : comparator.thenComparing(newComparator);
		}

		if (comparator != null) {
			content.sort(comparator);
		}

		return content.stream()
			.skip(pageable.getOffset())
			.limit(pageable.getPageSize())
			.collect(Collectors.toList());
	}

}
