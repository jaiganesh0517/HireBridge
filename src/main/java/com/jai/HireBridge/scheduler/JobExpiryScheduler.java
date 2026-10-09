package com.jai.HireBridge.scheduler;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jai.HireBridge.model.Status; // verify package
import com.jai.HireBridge.repositories.JobPostingRepository;

@Component
public class JobExpiryScheduler {

	private static final Logger log = LoggerFactory.getLogger(JobExpiryScheduler.class);

	private final JobPostingRepository jobPostingRepository;

	public JobExpiryScheduler(JobPostingRepository jobPostingRepository) {
		this.jobPostingRepository = jobPostingRepository;
	}


	//@Scheduled(fixedRate = 60000)
	 @Scheduled(cron = "0 0 * * * *")
	@Transactional
	public void closeExpiredJobs() {
		 int closed = jobPostingRepository.closeExpiredJobs(
			        Status.OPEN, Status.CLOSED, LocalDateTime.now(ZoneId.of("Asia/Kolkata")));
		if (closed > 0) {
			log.info("Auto-closed {} expired job posting(s)", closed);
		}
	}
}