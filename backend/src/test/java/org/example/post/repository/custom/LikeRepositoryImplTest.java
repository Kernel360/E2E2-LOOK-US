/*
package org.example.post.repository.custom;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.example.post.domain.entity.LikeEntity;
import org.example.post.domain.entity.PostEntity;
import org.example.post.domain.entity.QLikeEntity;
import org.example.user.domain.entity.member.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class LikeRepositoryImplTest {

	@PersistenceContext
	private EntityManager entityManager;

	private LikeRepositoryImpl likeRepository;
	private JPAQueryFactory queryFactory;

	@BeforeEach
	public void setUp() {
		queryFactory = new JPAQueryFactory(entityManager);
		likeRepository = new LikeRepositoryImpl(entityManager);
	}

	@Test
	public void testLikeCount() {
		// Given
		UserEntity user = UserEntity.builder().username("testuser").build();
		entityManager.persist(user);

		PostEntity post = PostEntity.builder()
			.user(user)
			.postContent("Test post content")
			.imageLocationId(1L)
			.likeCount(0)
			.build();
		entityManager.persist(post);

		LikeEntity like1 = LikeEntity.builder().post(post).build();
		LikeEntity like2 = LikeEntity.builder().post(post).build();
		entityManager.persist(like1);
		entityManager.persist(like2);

		// When
		Integer likeCount = likeRepository.likeCount(post);

		// Then
		assertThat(likeCount).isEqualTo(2);
	}

	@Test
	public void testDeleteAllByPost() {
		// Given
		UserEntity user = UserEntity.builder().username("testuser").build();
		entityManager.persist(user);

		PostEntity post = PostEntity.builder()
			.user(user)
			.postContent("Test post content")
			.imageLocationId(1L)
			.likeCount(0)
			.build();
		entityManager.persist(post);

		LikeEntity like1 = LikeEntity.builder().post(post).build();
		LikeEntity like2 = LikeEntity.builder().post(post).build();
		entityManager.persist(like1);
		entityManager.persist(like2);

		// When
		likeRepository.deleteAllByPost(post);

		// Then
		List<LikeEntity> likes = queryFactory.selectFrom(QLikeEntity.likeEntity)
			.where(QLikeEntity.likeEntity.post.eq(post))
			.fetch();

		assertThat(likes).isEmpty();
		// Verify if the `removedAt` field is updated
		entityManager.refresh(like1);
		entityManager.refresh(like2);
		assertThat(like1.getRemovedAt()).isNotNull();
		assertThat(like2.getRemovedAt()).isNotNull();
	}
}
*/
