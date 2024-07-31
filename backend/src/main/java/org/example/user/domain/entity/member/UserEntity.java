package org.example.user.domain.entity.member;

import java.util.Collection;
import java.util.List;

import org.example.user.domain.entity.BaseEntity;
import org.example.user.domain.enums.Gender;
import org.example.user.domain.enums.Role;
import org.example.user.domain.enums.UserStatus;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@ToString
@Entity
public class UserEntity extends BaseEntity implements UserDetails { // UserDetails를 상속받아 인증 객체로 사용

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", nullable = false, unique = true)
	private long userId;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;    // TODO: encryption

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "gender", nullable = false)
	private Gender gender;

	@Column(name = "birth", nullable = false)
	private String birth;    // TODO: validation

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "nickname", nullable = false, unique = true)
	private String nickname;    // TODO: 글자 수 제한 validation 필요할 수도?

	@Column(name = "insta_id", nullable = false, unique = true)
	private String instaId;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "role", nullable = false)    // TODO: 기본값 추가 가능
	private Role role;

	@Column(name = "profile_image")
	private String profileImage;    // TODO: 이미지 자체를 엔티티로 선언해서 타입, 크기 등을 관리해야 할 수도..?

	@Column(name = "user_status", nullable = false)
	@ColumnDefault("UserStatus.USER_STATUS_SIGNED_IN")    // TODO: 제대로 초기화 되는지 확인 필요
	private UserStatus userStatus = UserStatus.USER_STATUS_SIGNED_IN;

    /*
		@Column(name = "user_created_at", nullable = false)
		private LocalDateTime userCreatedAt;    // TODO: 단위 조절 필요

		@Column(name = "user_updated_at", nullable = false)
		private LocalDateTime userUpdatedAt;*/

	@Builder
	public UserEntity(String email, String password, Gender gender, String birth, String username,
		String nickname, String instaId, Role role, String profileImage, UserStatus userStatus) {
		this.email = email;
		this.password = password;
		this.gender = gender;
		this.birth = birth;
		this.username = username;
		this.nickname = nickname;
		this.instaId = instaId;
		this.role = role;
		this.profileImage = profileImage;
		this.userStatus = userStatus;
	}

	//사용자 이름 변경
	public UserEntity update(String username) {
		this.username = username;
		return this;
	}

	@Override // 권한 반환
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("user"));
	}

	// 사용자의 id를 반환(고유한 값)
	@Override
	public String getUsername() {
		return nickname;
	}

	public String getEmail() {
		return email;
	}

	public Role getRole() {
		return role;
	}

	// 사용자의 패스워드 반환
	@Override
	public String getPassword() {
		return password;
	}

	// 계정 만료 여부 반환
	@Override
	public boolean isAccountNonExpired() {
		// 만료 되었는지 확인하는 로직
		return true; // true -> 만료되지 않았음
	}

	// 계정 잠금 여부 반환
	@Override
	public boolean isAccountNonLocked() {
		// 계정 잠금되었는지 확인하는 로직
		return true; // true -> 잠금되지 않았음
	}

	// 패스워드의 만료 여부 반환
	@Override
	public boolean isCredentialsNonExpired() {
		// 패스워드가 만료되었는지 확인하는 로직
		return true; //true -> 만료되지 않음
	}

	// 계정 사용 가능 여부 반환
	@Override
	public boolean isEnabled() {
		// 계정이 사용가능한지 확인하는 로직
		return true; // true -> 사용 가능
	}
}
