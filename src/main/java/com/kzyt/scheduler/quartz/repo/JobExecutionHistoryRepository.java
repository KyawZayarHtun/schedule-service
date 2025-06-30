package com.kzyt.scheduler.quartz.repo;

import com.kzyt.scheduler.quartz.entity.JobExecutionHistory;
import com.kzyt.scheduler.quartz.io.JobExecutionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface JobExecutionHistoryRepository  extends JpaRepository<JobExecutionHistory, Long> {

    List<JobExecutionHistory> findByJobNameAndJobGroupOrderByCreatedAtDesc(String jobName, String jobGroup);

    List<JobExecutionHistory> findByExecutionStatusOrderByCreatedAtDesc(JobExecutionStatus status);

    List<JobExecutionHistory> findByIsMisfireTrue();

    @Query("SELECT h FROM JobExecutionHistory h WHERE h.createdAt BETWEEN :startDate AND :endDate ORDER BY h.createdAt DESC")
    List<JobExecutionHistory> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);

    @Query("SELECT h FROM JobExecutionHistory h WHERE h.jobName = :jobName AND h.jobGroup = :jobGroup AND h.executionStatus = 'FAILED' ORDER BY h.createdAt DESC")
    List<JobExecutionHistory> findFailedExecutions(@Param("jobName") String jobName, @Param("jobGroup") String jobGroup);

}
