package org.example.config.batch;

import java.time.LocalDateTime;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BatchScheduler {

	private final JobLauncher jobLauncher;
	private final Job postStatsJob;

	public BatchScheduler(JobLauncher jobLauncher, Job postStatsJob) {
		this.jobLauncher = jobLauncher;
		this.postStatsJob = postStatsJob;
	}

	@Scheduled(cron = "0 05 15 * * *") // 매일 24시에 실행
	public void runUpdatePostStatsJob() throws Exception {
		log.info("batch 시작");
		jobLauncher.run(postStatsJob, new JobParametersBuilder()
			.addString("timestamp", LocalDateTime.now().toString())
			.toJobParameters());
	}
}