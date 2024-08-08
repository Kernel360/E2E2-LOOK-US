package org.example.post.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.example.post.domain.dto.PaginationDto;
import org.example.post.domain.dto.PostDto;
import org.example.post.domain.entity.HashtagEntity;
import org.example.post.domain.entity.PostEntity;
import org.example.post.domain.enums.PostStatus;
import org.example.post.repository.PostRepository;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.domain.enums.Gender;
import org.example.user.domain.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

class PostServiceTest {

	@Mock
	private PostRepository postRepository;

	@InjectMocks
	private PostService postService;

	private UserEntity user;
	private PostEntity post;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		user = UserEntity.builder()
			.username("testuser")
			.password("password")
			.email("test@example.com")
			.gender(Gender.GENDER_MAN)
			.birth("1990-01-01")
			.nickname("testnickname")
			.role(Role.ROLE_USER)
			.build();

		post = new PostEntity(user, "Test post content", 1L, List.of(
			new HashtagEntity(post, "#test1"),
			new HashtagEntity(post, "#test2")
		));

	}

	@Test
	@DisplayName("게시물 조회 테스트")
	void testSearchAllPostsBySearchCriteria_WithSearchStringAndHashtags() {
		PaginationDto.PaginationDtoRequest paginationRequestDto = new PaginationDto.PaginationDtoRequest(
			0, 10, "postId", "ASC", "#test1", "Test"
		);

		Sort sort = Sort.by("createdAt").descending();
		if (paginationRequestDto.sortDirection().equalsIgnoreCase("ASC")) {
			sort = Sort.by("createdAt").ascending();
		}
		Pageable pageable = PageRequest.of(0, 10, sort);
		List<PostEntity> postList = List.of(post);
		Page<PostEntity> postPage = new PageImpl<>(postList, pageable, postList.size());

		when(postRepository.findAllByPostContentContainingAndHashtags_HashtagContentInAndPostStatus(
			any(String.class), ArgumentMatchers.any(), any(PostStatus.class), any(Pageable.class)
		)).thenReturn(postPage);

		PaginationDto.PaginationDtoResponse response = postService.searchAllPostsBySearchCriteria(
			paginationRequestDto);
		assertEquals(1, response.totalElements());
		assertEquals(1, response.totalPages());
		assertEquals("Test", response.searchString());
		assertEquals(1, response.postResponseDtoList().size());

		PostDto.GetPostDtoResponse postResponse = response.postResponseDtoList().get(0);
		assertEquals(user.getNickname(), postResponse.nickname());
		assertEquals(post.getPostId(), postResponse.postId());
		assertEquals(post.getImageId(), postResponse.imageId());
		assertEquals(post.getPostContent(), postResponse.postContent());
		assertEquals(2, postResponse.hashtagContents().size());
	}

	@Test
	@DisplayName("게시물 조회 실패 테스틑")
	void testSearchAllPostsBySearchCriteria_NoPostsFound() {
		PaginationDto.PaginationDtoRequest paginationRequestDto = new PaginationDto.PaginationDtoRequest(
			0, 10, "postId", "DESC", "#test1", "Test"
		);

		when(postRepository.findAllByPostContentContainingAndHashtags_HashtagContentInAndPostStatus(
			any(String.class), any(List.class), any(PostStatus.class), any(Pageable.class)
		)).thenReturn(null);

		assertThrows(NullPointerException.class, () -> {
			postService.searchAllPostsBySearchCriteria(paginationRequestDto);
		});
	}

}
