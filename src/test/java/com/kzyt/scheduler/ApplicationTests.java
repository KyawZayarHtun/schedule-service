package com.kzyt.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class ApplicationTests {

	@Autowired
	private Scheduler scheduler;

	@Test
	void contextLoads() {

        try {
			boolean b = scheduler.checkExists(new JobKey("a", "b"));
			log.info(String.valueOf(b));
		} catch (SchedulerException e) {
            throw new RuntimeException(e);
        }

    }

}
