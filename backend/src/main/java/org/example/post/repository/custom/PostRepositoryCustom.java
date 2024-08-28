package org.example.post.repository.custom;

import java.util.List;

import org.example.post.domain.dto.PostDto;
import org.example.post.domain.dto.PostStatsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
	Page<PostDto.PostDtoResponse> search(
		PostSearchCondition searchCondition, Pageable pageable
	);

	int updateView(Long postId);

	List<PostStatsDto> findPostStatsByType();


}
