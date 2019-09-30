package com.cml.learning.module.bat00X.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dudu on 2019/9/30.
 */
@Component
@EnableScheduling//开启定时任务
public class ScheduledTasks {

    @Autowired
    private JobLauncher jobLauncher;

    //这个 job是哪个job呢?
    //这个是自动注入的： 所以应该是： 已经被定义好了任务
    @Autowired
    @Qualifier(value = "printJob")//针对 @Bean中的 name进行对应
    private Job job;

    @Scheduled(cron="0/15 * * * * * ")//0秒开始触发： 每隔30秒出发一次
    public void showCurrentTime() throws Exception{
        System.out.println("111111111111111111111111111111111111111111111111111");
        Map<String, JobParameter> param = new HashMap<String, JobParameter>();
        param.put("startTime", new JobParameter(new Date()));
        jobLauncher.run(job, new JobParameters(param));
        System.out.println("111111111111111111111111111111111111111111111111111");
    }


}
