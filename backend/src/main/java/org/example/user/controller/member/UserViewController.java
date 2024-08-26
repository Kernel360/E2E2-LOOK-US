package org.example.user.controller.member;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.example.config.jwt.TokenProvider;
import org.example.post.domain.entity.PostStats;
import org.example.post.repository.PostStatsRepository;
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
	private final PostStatsRepository postStatsRepository;
	private final TokenProvider tokenProvider;

	@GetMapping("/")
	public String hello() {
		return "index";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@PostMapping("/login")
	public String login(@ModelAttribute UserDto.UserLoginRequest loginRequest, Model model, HttpServletResponse response) {
		UserDto.UserResponse userResponse = userService.loginUser(loginRequest);
		UserEntity user = userService.getUserByEmail(userResponse.email());
		if (user != null) {
			// JWT 토큰 생성
			String token = tokenProvider.generateToken(user, Duration.ofHours(1));

			// 토큰을 쿠키에 저장
			Cookie jwtCookie = new Cookie("token", token);
			jwtCookie.setHttpOnly(true);
			jwtCookie.setPath("/");
			jwtCookie.setMaxAge(24 * 60 * 60); // 24시간
			response.addCookie(jwtCookie);

			if ("ROLE_ADMIN".equals(userResponse.role())) {
				return "redirect:/admin/stats";
			} else {
				return "redirect:/home";
			}
		} else {
			return "redirect:/login?error=true";
		}
	}


	@GetMapping("/admin/stats")
	public String getPostStats(Model model) {
		// 데이터 조회
		List<PostStats> stats = postStatsRepository.findAll();

		// 게시글별로 데이터를 그룹화
		Map<Long, List<PostStats>> statsByPostId = stats.stream()
			.collect(Collectors.groupingBy(stat -> stat.getPost().getPostId()));

		// 각 게시글에 대한 레이블과 데이터를 미리 가공하여 모델에 추가
		Map<Long, List<String>> labelsByPostId = new HashMap<>();
		Map<Long, List<Integer>> hitsDataByPostId = new HashMap<>();
		Map<Long, List<Integer>> likeDataByPostId = new HashMap<>();
		Map<Long, List<Integer>> todayHitsByPostId = new HashMap<>();
		Map<Long, List<Integer>> todayLikesByPostId = new HashMap<>();

		for (Map.Entry<Long, List<PostStats>> entry : statsByPostId.entrySet()) {
			List<PostStats> postStatsList = entry.getValue();
			List<String> labels = postStatsList.stream()
				.map(stat -> stat.getRecordedAt().toString())
				.collect(Collectors.toList());
			List<Integer> hitsData = postStatsList.stream()
				.map(PostStats::getHits)
				.collect(Collectors.toList());
			List<Integer> likeData = postStatsList.stream()
				.map(PostStats::getLikeCount)
				.collect(Collectors.toList());
			List<Integer> todayHitsData = postStatsList.stream()
				.map(PostStats::getTodayHits)
				.collect(Collectors.toList());
			List<Integer> todayLikesData = postStatsList.stream()
				.map(PostStats::getTodayLikes)
				.collect(Collectors.toList());

			labelsByPostId.put(entry.getKey(), labels);
			hitsDataByPostId.put(entry.getKey(), hitsData);
			likeDataByPostId.put(entry.getKey(), likeData);
			todayHitsByPostId.put(entry.getKey(), todayHitsData);
			todayLikesByPostId.put(entry.getKey(), todayLikesData);
		}

		// 모델에 데이터 추가
		model.addAttribute("statsByPostId", statsByPostId);
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
