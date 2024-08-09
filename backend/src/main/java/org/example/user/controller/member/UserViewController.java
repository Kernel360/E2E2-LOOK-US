package org.example.user.controller.member;

import org.example.user.domain.dto.UserDto;
import org.example.user.service.member.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserViewController {

	private final UserService userService;

	@GetMapping("/")
	public String hello() {
		return "index";
	}

	@GetMapping("/login")
	public String login() {
		return "oauthLogin";
	}

	@GetMapping("/signup")
	public String signup() {
		return "signup";
	}

	@PostMapping("/user")
	public String signup(@RequestBody UserDto.UserCreateRequest addUserRequest) {
		userService.saveUser(addUserRequest); // 회원가입 메서드 호출
		return "redirect:/login"; // 회원 가입이 완료된 이후에 로그인 페이지로 이동
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		new SecurityContextLogoutHandler().logout(request, response,
			SecurityContextHolder.getContext().getAuthentication());
		return "redirect:/login";
	}

	@GetMapping("/articles")
	public String getArticles(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(authentication);
		System.out.println(SecurityContextHolder.getContext());
		return "articleList";
	}
}
