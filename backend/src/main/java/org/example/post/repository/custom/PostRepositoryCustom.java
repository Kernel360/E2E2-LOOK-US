package org.example.post.repository.custom;

import org.example.post.domain.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
	Page<PostDto.PostDtoResponse> search(
		PostSearchCondition searchCondition, Pageable pageable
	);
}
