package org.example.user.repository.member;

import java.util.Optional;

import org.example.user.domain.entity.member.UserEntity;
import org.example.user.repository.member.custom.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import com.querydsl.jpa.impl.JPAQueryFactory;

public interface UserRepository extends JpaRepository<UserEntity, Long>, UserRepositoryCustom {

    Optional<UserEntity> findByEmail(String email); // username 사용자 정보를 가져옴

}
