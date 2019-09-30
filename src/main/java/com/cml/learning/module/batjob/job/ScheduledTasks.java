package com.cml.learning.module.batjob.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dudu on 2019/9/30.
 */
@Component
public class ScheduledTasks {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;

    @Scheduled(cron="0/30 * * * * * ")//0秒开始触发： 每隔30秒出发一次
    public void showCurrentTime() throws Exception{
        System.out.println("111111111111111111111111111111111111111111111111111");
        Map<String, JobParameter> param = new HashMap<String, JobParameter>();
        param.put("startTime", new JobParameter(new Date()));
        jobLauncher.run(job, new JobParameters(param));
        System.out.println("111111111111111111111111111111111111111111111111111");
    }


}
