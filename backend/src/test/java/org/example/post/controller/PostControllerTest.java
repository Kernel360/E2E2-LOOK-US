package org.example.post.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.when;

import org.example.post.domain.dto.PaginationDto;
import org.example.post.domain.dto.PostDto;
import org.example.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PostService postService;

	@Test
	@DisplayName("[GET] 게시글 조회 테스트")
	void getAllPosts() throws Exception {
		List<PostDto.GetPostDtoResponse> postList = List.of(
			new PostDto.GetPostDtoResponse(
				"nick", 1L, 1L, "Post Content 1", List.of("#tag1"), 10, LocalDateTime.now(), LocalDateTime.now()
			),
			new PostDto.GetPostDtoResponse(
				"test", 2L, 2L, "Post Content 2", List.of("#tag2"), 20, LocalDateTime.now(), LocalDateTime.now()
			)
		);

		PaginationDto.PaginationDtoResponse paginationResponseDto = new PaginationDto.PaginationDtoResponse(
			0, 10, 2, 1, "search", postList
		);

		when(postService.getAllPostsOrderedBySortStrategy(ArgumentMatchers.any()))
			.thenReturn(paginationResponseDto);

		ResultActions result = mockMvc.perform(get("/posts")
			.param("searchHashtags", "#tag1")
			.param("searchString", "search")
			.param("sortField", "createdAt")
			.param("sortDirection", "DESC")
			.param("page", "0")
			.param("size", "10")
			.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.page").value(0))
			.andExpect(jsonPath("$.size").value(10))
			.andExpect(jsonPath("$.totalElements").value(2))
			.andExpect(jsonPath("$.totalPages").value(1))
			.andExpect(jsonPath("$.searchString").value("search"))
			.andExpect(jsonPath("$.postResponseDtoList[0].postId").value(1))
			.andExpect(jsonPath("$.postResponseDtoList[0].postContent").value("Post Content 1"))
			.andExpect(jsonPath("$.postResponseDtoList[0].hashtagContents[0]").value("#tag1"))
			.andExpect(jsonPath("$.postResponseDtoList[1].postId").value(2))
			.andExpect(jsonPath("$.postResponseDtoList[1].postContent").value("Post Content 2"))
			.andExpect(jsonPath("$.postResponseDtoList[1].hashtagContents[0]").value("#tag2"));
	}
}
