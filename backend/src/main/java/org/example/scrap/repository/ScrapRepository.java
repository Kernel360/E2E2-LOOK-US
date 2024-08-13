package org.example.scrap.repository;

import java.util.Optional;

import org.example.post.domain.entity.PostEntity;
import org.example.scrap.domain.entity.ScrapEntity;
import org.example.user.domain.entity.member.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapRepository extends JpaRepository<ScrapEntity, Long> {

	void deleteByPostAndUser(PostEntity post, UserEntity user);

	Optional<ScrapEntity> findByPost_PostIdAndUser_Email(Long postId, String user);
}
