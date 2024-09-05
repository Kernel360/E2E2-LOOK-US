package org.example.post.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.example.config.log.LogExecution;
import org.example.post.domain.entity.PostDailyStats;
import org.example.post.domain.entity.PostEntity;
import org.example.post.domain.entity.PostTotalStats;
import org.example.post.repository.PostDailyStatsRepository;
import org.example.post.repository.PostRepository;
import org.example.post.repository.PostTotalStatsRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostStatsService {

	private final PostDailyStatsRepository postDailyStatsRepository;
	private final PostTotalStatsRepository postTotalStatsRepository;
	private final PostRepository postRepository;

	@LogExecution
	public List<PostDailyStats> getDailyStatsByPost(PostEntity post) {
		return postDailyStatsRepository.findByPost(post);
	}

	@LogExecution
	public List<PostTotalStats> getTotalStatsByPost(PostEntity post) {
		return postTotalStatsRepository.findByPost(post);
	}

	public Map<String, Object> getPostStats() {
		List<PostEntity> posts = postRepository.findAll();

		Map<Long, List<String>> labelsByPostId = new HashMap<>();
		Map<Long, List<Integer>> hitsDataByPostId = new HashMap<>();
		Map<Long, List<Integer>> likeDataByPostId = new HashMap<>();
		Map<Long, List<Integer>> todayHitsByPostId = new HashMap<>();
		Map<Long, List<Integer>> todayLikesByPostId = new HashMap<>();

		for (PostEntity post : posts) {
			List<PostDailyStats> dailyStats = getDailyStatsByPost(post);
			List<PostTotalStats> totalStats = getTotalStatsByPost(post);

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

		// 필요한 데이터를 맵으로 반환
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("posts", posts);
		resultMap.put("labelsByPostId", labelsByPostId);
		resultMap.put("hitsDataByPostId", hitsDataByPostId);
		resultMap.put("likeDataByPostId", likeDataByPostId);
		resultMap.put("todayHitsByPostId", todayHitsByPostId);
		resultMap.put("todayLikesByPostId", todayLikesByPostId);

		return resultMap;
	}

}
