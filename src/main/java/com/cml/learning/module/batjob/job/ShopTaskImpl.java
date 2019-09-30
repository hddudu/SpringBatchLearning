package com.cml.learning.module.batjob.job;

import org.springframework.stereotype.Component;

/**
 * Created by dudu on 2019/9/30.
 */
@Component
public class ShopTaskImpl implements ShopTask {
    @Override
    public void doTask() {
        System.out.println("task execute");
    }
}
