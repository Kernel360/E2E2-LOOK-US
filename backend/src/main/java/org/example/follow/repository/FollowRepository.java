package org.example.follow.repository;

import java.util.List;
import java.util.Optional;

import org.example.follow.domain.entity.Follow;
import org.example.user.domain.entity.member.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

	List<Follow> findByFromUser(UserEntity fromUser);

	List<Follow> findByToUser(UserEntity toUser);

	void deleteByFromUserAndToUser(UserEntity fromUser, UserEntity toUser);

	Optional<Follow> findByFromUserAndToUser(UserEntity fromUser, UserEntity toUser);
}
