package org.example.follow.repository;

import java.util.List;
import java.util.Optional;

import org.example.follow.domain.entity.Follow;
import org.example.user.domain.entity.member.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepository extends JpaRepository<Follow, Long> {
	List<Follow> findByFromUser(UserEntity fromUser);
	List<Follow> findByToUser(UserEntity toUser);
	void deleteFollowByFromUser(UserEntity fromUser);
	@Query("select f from Follow f where f.fromUser = :from and f.toUser = :to")
	Optional<Follow> findFollow(@Param("from") UserEntity fromUser, @Param("to") UserEntity toUser);
}
