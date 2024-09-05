package org.example.user.controller.member;

import java.util.Map;

import org.example.config.log.LogExecution;
import org.example.post.service.PostStatsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdminStatsController {

	private final PostStatsService postStatsService;

	@LogExecution
	@GetMapping("/admin/stats")
	public String getPostStats(Model model) {
		// 서비스에서 데이터 조회
		Map<String, Object> statsData = postStatsService.getPostStats();

		// 모델에 데이터 추가
		model.addAttribute("posts", statsData.get("posts"));
		model.addAttribute("labelsByPostId", statsData.get("labelsByPostId"));
		model.addAttribute("hitsDataByPostId", statsData.get("hitsDataByPostId"));
		model.addAttribute("likeDataByPostId", statsData.get("likeDataByPostId"));
		model.addAttribute("todayHitsByPostId", statsData.get("todayHitsByPostId"));
		model.addAttribute("todayLikesByPostId", statsData.get("todayLikesByPostId"));

		return "adminStats"; // Thymeleaf 템플릿 이름
	}
}
