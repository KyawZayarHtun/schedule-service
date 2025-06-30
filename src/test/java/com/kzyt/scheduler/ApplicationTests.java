package com.kzyt.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZoneId;

@Slf4j
class ApplicationTests {

	@Test
	void contextLoads() {

		ZoneId defaultZoneId = ZoneId.systemDefault();
		System.out.println("JVM Default Time Zone (java.time.ZoneId): " + defaultZoneId);

    }

}
