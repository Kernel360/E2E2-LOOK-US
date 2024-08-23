package org.example.post.repository.custom;

import org.example.post.domain.entity.PostEntity;

public interface LikeRepositoryCustom {
	Integer likeCount(PostEntity post);

	void deleteAllByPost(PostEntity post);
}
