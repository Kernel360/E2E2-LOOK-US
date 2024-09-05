package org.example.user.controller.member;

import java.time.Duration;

import org.example.config.jwt.TokenProvider;
import org.example.config.log.LogExecution;
import org.example.user.domain.dto.UserDto;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.service.member.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 *  This page is for Admin Page.
 */
@Controller
@RequiredArgsConstructor
public class UserViewController {

	private final UserService userService;
	private final TokenProvider tokenProvider;

	@GetMapping("/")
	public String hello() {
		return "index";
	}

	// NOTE: admin login page
	@LogExecution
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@LogExecution
	@PostMapping("/login") // NOTE: --------- ADMIN REPORT PAGE --------------
	public String login(@ModelAttribute UserDto.UserLoginRequest loginRequest, Model model, HttpServletResponse response) {
		UserDto.UserResponse userResponse = userService.loginUser(loginRequest);
		UserEntity user = userService.getUserByEmail(userResponse.email());

		if (user != null) {
			// NOTE: 현재 구현된 것은 일반 사용자 로그인만 access token이 발급 됩니다.
			//       따라서 개발 단에서 간단히 admin에게도 token을 줘서 게시물 생성 가능하게 처리하려고
			//       아래와 같이 admin에게 토큰을 주게 되었습니다...

			// JWT 토큰 생성
			String token = tokenProvider.generateToken(user, Duration.ofHours(1));

			// 토큰을 쿠키에 저장
			Cookie jwtCookie = new Cookie("token", token);
			jwtCookie.setHttpOnly(true);
			jwtCookie.setPath("/");
			jwtCookie.setMaxAge(24 * 60 * 60); // 24시간
			response.addCookie(jwtCookie);

			return "redirect:/admin/stats";
		} else {
			return "redirect:/login?error=true";
		}
	}

	@LogExecution
	@GetMapping("/signup")
	public String signup() {
		return "signup";
	}

	@LogExecution
	@PostMapping("/user")
	public String signup(@ModelAttribute UserDto.UserCreateRequest addUserRequest) {
		userService.signupUser(addUserRequest); // 회원가입 메서드 호출
		return "redirect:/login"; // 회원 가입이 완료된 이후에 로그인 페이지로 이동
	}

	@LogExecution
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Cookie cookie = new Cookie("token", null);
		cookie.setHttpOnly(true);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);

		new SecurityContextLogoutHandler().logout(request, response,
			SecurityContextHolder.getContext().getAuthentication());
		return "redirect:/login";
	}

}
