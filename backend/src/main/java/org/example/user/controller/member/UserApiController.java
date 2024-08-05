package org.example.user.controller.member;

import org.example.user.domain.dto.UserDto;
import org.example.user.domain.dto.request.member.AddUserRequest;
import org.example.user.service.member.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserApiController {

	private final UserService userService;

	@ResponseBody
	@PatchMapping("/api/v1/user-update")
	public ResponseEntity<UserDto.UserUpdateResponse> userUpdate(
		@RequestPart("updateRequest") UserDto.UserUpdateRequest updateRequest,
		@RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(userService.updateUser(updateRequest, name, profileImage));
	}

	@PostMapping("/user")
	public String signup(AddUserRequest addUserRequest) {
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

		return "articleList";
	}

}
