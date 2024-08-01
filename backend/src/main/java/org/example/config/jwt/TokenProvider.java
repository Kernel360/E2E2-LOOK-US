package org.example.config.jwt;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.example.user.domain.entity.member.UserEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TokenProvider { // 계속해서 토큰을 생성하고 올바른 토큰인지 유효성 검사를 하고, 토큰에서 필요한 정보를 가져오는 클래스 작성.

	private final JwtProperties jwtProperties;

	public String generateToken(UserEntity user, Duration expiredAt) {
		Date now = new Date();
		return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
	}

	// JWT 토큰 생성 메서드
	private String makeToken(Date expiry, UserEntity user) {
		Date now = new Date();

		return Jwts.builder()
			.setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더 typ: JWT
			// 내용 iss : ajufresh@gmail.com(propertise 파일에서 설정한 값)
			.setIssuer(jwtProperties.getIssuer())
			.setIssuedAt(now) // 내용 iat: 현재 시간
			.setExpiration(expiry) // 내용 exp: expiry 멤버 변수값
			.setSubject(user.getUsername()) // 내용 sub: 유저의 이메일
			.claim("id", user.getUserId()) // 클레임 id: 유저 id
			// 서명: 비밀값과 함께 해시값을 HS256 방식으로 암호화
			.signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
			.compact();
	}

	//JWT 토큰 유효성 검증 메서드
	public boolean validToken(String token) {
		try {
			Jwts.parser()
				.setSigningKey(jwtProperties.getSecretKey())
				.parseClaimsJws(token);

			return true;
		} catch (Exception e) { // 복호화 과정에서 에러가 나면 유효하지 않는 토큰
			return false;
		}
	}

	// 토큰 기반으로 인증 정보를 가져오는 메서드
	public Authentication getAuthentication(String token) {
		Claims claims = getClaims(token);
		Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

		return new UsernamePasswordAuthenticationToken(
			new org.springframework.security.core.userdetails.User(claims.getSubject
				(), "", authorities), token, authorities);
	}

	// 토큰 기반으로 유저 ID를 가져오는 메서드
	public Long getUserId(String token) {
		Claims claims = getClaims(token);
		return claims.get("id", Long.class);
	}

	private Claims getClaims(String token) {
		return Jwts.parser() // 클레임 조회
			.setSigningKey(jwtProperties.getSecretKey())
			.parseClaimsJws(token)
			.getBody();
	}
}
