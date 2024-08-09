package org.example.user.controller.member;

import org.example.user.domain.dto.UserDto;
import org.example.user.service.member.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserApiController {

	private final UserService userService;

	@PatchMapping("/api/v1/user-update")
	public ResponseEntity<UserDto.UserUpdateResponse> userUpdate(
		@RequestPart("updateRequest") UserDto.UserUpdateRequest updateRequest,
		@RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return ResponseEntity.status(HttpStatus.OK)
			.body(userService.updateUser(updateRequest, email, profileImage));
	}

}
