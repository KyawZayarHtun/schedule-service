CREATE TABLE job_execution_history (
                                       id BIGSERIAL PRIMARY KEY,
                                       job_name VARCHAR(200) NOT NULL,
                                       job_group VARCHAR(200) NOT NULL,
                                       trigger_name VARCHAR(200) NOT NULL,
                                       trigger_group VARCHAR(200) NOT NULL,
                                       scheduled_fire_time TIMESTAMP,
                                       actual_fire_time TIMESTAMP,
                                       job_start_time TIMESTAMP,
                                       job_end_time TIMESTAMP,
                                       execution_duration_ms BIGINT,
                                       execution_status VARCHAR(20) NOT NULL,
                                       error_message TEXT,
                                       is_misfire BOOLEAN DEFAULT FALSE,
                                       next_fire_time TIMESTAMP,
                                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better query performance
CREATE INDEX idx_job_execution_history_job_name_group ON job_execution_history(job_name, job_group);
CREATE INDEX idx_job_execution_history_status ON job_execution_history(execution_status);
CREATE INDEX idx_job_execution_history_misfire ON job_execution_history(is_misfire);
CREATE INDEX idx_job_execution_history_created_at ON job_execution_history(created_at);
CREATE INDEX idx_job_execution_history_scheduled_fire_time ON job_execution_history(scheduled_fire_time);