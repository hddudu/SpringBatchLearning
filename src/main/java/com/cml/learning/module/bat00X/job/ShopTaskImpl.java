package com.cml.learning.module.bat00X.job;

import com.cml.learning.module.batjob.job.ShopTask;
import org.springframework.stereotype.Component;

/**
 * Created by dudu on 2019/9/30.
 */
@Component
public class ShopTaskImpl {

    public void doTask() {
        System.out.println("*********************执行定时任务--打印任务！*********************");
    }
}
