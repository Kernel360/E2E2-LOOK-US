package org.example.user.repository.member;

import java.util.Optional;

import org.example.user.domain.entity.member.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username); // username 사용자 정보를 가져옴
}
