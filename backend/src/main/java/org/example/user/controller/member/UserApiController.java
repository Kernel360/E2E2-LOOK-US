package org.example.user.controller.member;

import java.util.List;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.user.ApiUserErrorSubCategory;
import org.example.exception.user.ApiUserException;
import org.example.user.domain.dto.UserDto;
import org.example.user.service.member.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserApiController {

	private final UserService userService;

	@PatchMapping("/update")
	public ResponseEntity<UserDto.UserUpdateResponse> userUpdate(
		@RequestPart(value = "updateRequest", required = false) UserDto.UserUpdateRequest updateRequest,
		@RequestPart(value = "profileImage", required = false) MultipartFile profileImage
	) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		if(updateRequest == null && profileImage.isEmpty()){
			throw ApiUserException.builder()
				.category(ApiErrorCategory.RESOURCE_INACCESSIBLE)
				.subCategory(ApiUserErrorSubCategory.USER_UPDATE_IMPOSSIBLE)
				.setErrorData(() -> "수정할 데이터가 입력되지 않았습니다.")
				.build();
		}

		return ResponseEntity.status(HttpStatus.OK)
			.body(userService.updateUser(updateRequest, email, profileImage));
	}

	@GetMapping("/me")
	public ResponseEntity<UserDto.UserGetInfoResponse> getMyPage(Authentication authentication) {

		return ResponseEntity.status(HttpStatus.OK)
							 .body(userService.getMyInfo(authentication.getName()));
	}

	@GetMapping("/me/posts")
	public ResponseEntity<List<UserDto.UserGetPostsResponse>> getMyPosts(Authentication authentication) {

		return ResponseEntity.status(HttpStatus.OK)
							 .body(userService.getMyPosts(authentication.getName()));
	}

	@PostMapping("/resign")
	public ResponseEntity<?> resignUser(Authentication authentication, HttpServletRequest request, HttpServletResponse response){
		userService.resignUser(authentication.getName(), request, response);

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logoutUser(Authentication authentication, HttpServletRequest request, HttpServletResponse response){
		userService.logoutUser(authentication.getName(), request, response);

		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
}
