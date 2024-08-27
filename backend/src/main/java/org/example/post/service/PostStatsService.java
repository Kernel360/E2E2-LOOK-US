package org.example.post.service;

import java.util.List;

import org.example.post.domain.entity.PostDailyStats;
import org.example.post.domain.entity.PostEntity;
import org.example.post.domain.entity.PostTotalStats;
import org.example.post.repository.PostDailyStatsRepository;
import org.example.post.repository.PostTotalStatsRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostStatsService {


	private final PostDailyStatsRepository postDailyStatsRepository;
	private final PostTotalStatsRepository postTotalStatsRepository;

	public List<PostDailyStats> getDailyStatsByPost(PostEntity post) {
		return postDailyStatsRepository.findByPost(post);
	}

	public List<PostTotalStats> getTotalStatsByPost(PostEntity post) {
		return postTotalStatsRepository.findByPost(post);
	}// 날짜별로 정렬하여 가져오기
}
