package org.example.post.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.example.post.domain.dto.reqeust.PaginationRequestDto;
import org.example.post.domain.dto.response.PaginationResponseDto;
import org.example.post.domain.entity.PostEntity;
import org.example.post.domain.enums.PostStatus;
import org.example.post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


// 테스트 하려면 PostEntity @Setter 추가하고 Accesslevel -> public 으로 변경 필요
// TimeTrackableEntity도 @Setter 필요
@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
	@Mock
	private PostRepository postRepository;

	@InjectMocks
	private PostService postService;

	private PostEntity post1;
	private PostEntity post2;
	private PostEntity post3;

	@BeforeEach
	public void setUp() {
		post1 = new PostEntity();
		post1.setPostId(1L);
		post1.setPostContent("This is a test post");
		post1.setCreatedAt(LocalDateTime.now().minusDays(1));

		post2 = new PostEntity();
		post2.setPostId(2L);
		post2.setPostContent("Another test post content");
		post2.setCreatedAt(LocalDateTime.now().minusDays(2));

		post3 = new PostEntity();
		post3.setPostId(3L);
		post3.setPostContent("Yet another test post");
		post3.setCreatedAt(LocalDateTime.now());
	}

	@Test
	public void whenGetAllPostsOrderedBySortStrategy_thenPostsShouldBeFound() {
		PaginationRequestDto paginationRequestDto = new PaginationRequestDto();
		paginationRequestDto.setPage(0);
		paginationRequestDto.setSize(10);
		paginationRequestDto.setSortField("createdAt");
		paginationRequestDto.setSortDirection("DESC");
		paginationRequestDto.setSearchString("test");

		Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

		List<PostEntity> posts = Arrays.asList(post1, post2, post3);
		Page<PostEntity> postPage = new PageImpl<>(posts, pageable, posts.size());

		when(postRepository.findAllByPostContentContainingAndPostStatus("test", PostStatus.PUBLISHED, pageable)).thenReturn(postPage);

		ResponseEntity<PaginationResponseDto> response = postService.getAllPostsOrderedBySortStrategy(
			paginationRequestDto);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		PaginationResponseDto paginationResponseDto = response.getBody();
		assertThat(paginationResponseDto).isNotNull();
		assertThat(paginationResponseDto.getTotalElements()).isEqualTo(3);
		assertThat(paginationResponseDto.getPostResponseDtoList().size()).isEqualTo(3);
	}

}
