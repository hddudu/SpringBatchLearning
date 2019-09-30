package com.cml.learning.framework.schedule;

import org.springframework.batch.core.Job;

/**
 * 批量 定时任务 接口
 * Created by dudu on 2019/9/30.
 */
public interface BatchScheduleTask {

    /**
     * 执行任务
     * @param job
     */
    void execute(Job job);
}
