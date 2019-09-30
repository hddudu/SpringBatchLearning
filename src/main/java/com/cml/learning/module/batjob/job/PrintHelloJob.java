package com.cml.learning.module.batjob.job;

import com.cml.learning.framework.schedule.BatchScheduleTask;
import org.springframework.batch.core.Job;

/**
 * Created by dudu on 2019/9/30.
 */
//@Component//交给spring 容器 管理
//@EnableScheduling//启动定时任务
public class PrintHelloJob implements BatchScheduleTask {


    //
//    @Scheduled(cron = "0/5 ")
    @Override
    public void execute(Job job) {

    }
}
