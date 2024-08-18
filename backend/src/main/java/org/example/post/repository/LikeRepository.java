package org.example.post.repository;

import org.example.post.domain.entity.LikeEntity;
import org.example.post.domain.entity.PostEntity;
import org.example.post.repository.custom.LikeRepositoryCustom;
import org.example.user.domain.entity.member.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, Long>, LikeRepositoryCustom {
	LikeEntity findByUserAndPost(UserEntity user, PostEntity post);

	boolean existsByUserAndPost(UserEntity user, PostEntity post);

}
