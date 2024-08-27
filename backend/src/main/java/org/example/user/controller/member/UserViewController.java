package org.example.user.controller.member;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.example.config.jwt.TokenProvider;
import org.example.post.domain.entity.PostDailyStats;
import org.example.post.domain.entity.PostEntity;
import org.example.post.domain.entity.PostStats;
import org.example.post.domain.entity.PostTotalStats;
import org.example.post.repository.PostDailyStatsRepository;
import org.example.post.repository.PostRepository;
import org.example.post.repository.PostStatsRepository;
import org.example.post.repository.PostTotalStatsRepository;
import org.example.post.service.PostStatsService;
import org.example.user.domain.dto.UserDto;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.service.member.UserService;
import org.springframework.security.core.Authentication;
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

@Controller
@RequiredArgsConstructor
public class UserViewController {

	private final UserService userService;
	private final PostStatsService postStatsService;
	private final PostRepository postRepository;
	private final TokenProvider tokenProvider;

	@GetMapping("/")
	public String hello() {
		return "index";
	}

	// NOTE: admin login page
	@GetMapping("/login")
	public String login() {
		return "login";
	}

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

			// if ("ROLE_ADMIN".equals(userResponse.role())) {
			// 	return "redirect:/admin/stats";
			// } else {
			// 	return "redirect:/home";
			// }
			return "redirect:/admin/stats";
		} else {
			return "redirect:/login?error=true";
		}
	}


	@GetMapping("/admin/stats")
	public String getPostStats(Model model) {
		// 데이터 조회
		List<PostEntity> posts = postRepository.findAll();

		Map<Long, List<String>> labelsByPostId = new HashMap<>();
		Map<Long, List<Integer>> hitsDataByPostId = new HashMap<>();
		Map<Long, List<Integer>> likeDataByPostId = new HashMap<>();
		Map<Long, List<Integer>> todayHitsByPostId = new HashMap<>();
		Map<Long, List<Integer>> todayLikesByPostId = new HashMap<>();

		for (PostEntity post : posts) {
			List<PostDailyStats> dailyStats = postStatsService.getDailyStatsByPost(post);
			List<PostTotalStats> totalStats = postStatsService.getTotalStatsByPost(post);

			List<String> labels = dailyStats.stream()
				.map(stat -> stat.getRecordedAt().toString())
				.collect(Collectors.toList());

			List<Integer> todayHitsData = dailyStats.stream()
				.map(PostDailyStats::getTodayHits)
				.collect(Collectors.toList());

			List<Integer> todayLikesData = dailyStats.stream()
				.map(PostDailyStats::getTodayLikes)
				.collect(Collectors.toList());

			int totalHits = !totalStats.isEmpty() ? totalStats.get(0).getHits() : 0;
			int totalLikes = !totalStats.isEmpty() ? totalStats.get(0).getLikeCount() : 0;

			labelsByPostId.put(post.getPostId(), labels);
			todayHitsByPostId.put(post.getPostId(), todayHitsData);
			todayLikesByPostId.put(post.getPostId(), todayLikesData);
			hitsDataByPostId.put(post.getPostId(), List.of(totalHits)); // 전체 조회수
			likeDataByPostId.put(post.getPostId(), List.of(totalLikes)); // 전체 좋아요 수
		}

		// 모델에 데이터 추가
		model.addAttribute("labelsByPostId", labelsByPostId);
		model.addAttribute("hitsDataByPostId", hitsDataByPostId);
		model.addAttribute("likeDataByPostId", likeDataByPostId);
		model.addAttribute("todayHitsByPostId", todayHitsByPostId);
		model.addAttribute("todayLikesByPostId", todayLikesByPostId);

		return "adminStats"; // Thymeleaf 템플릿 이름
	}


	@GetMapping("/signup")
	public String signup() {
		return "signup";
	}


	@PostMapping("/user")
	public String signup(@ModelAttribute UserDto.UserCreateRequest addUserRequest) {
		userService.signupUser(addUserRequest); // 회원가입 메서드 호출
		return "redirect:/login"; // 회원 가입이 완료된 이후에 로그인 페이지로 이동
	}


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
