package org.example.post.service;

import org.example.post.domain.entity.PostStats;
import org.example.post.repository.PostStatsRepository;
import org.springframework.stereotype.Service;
import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostStatsService {

	private final PostStatsRepository postStatsRepository;

	public List<PostStats> getPostStats() {
		return postStatsRepository.findAll(); // 날짜별로 정렬하여 가져오기
	}
}
