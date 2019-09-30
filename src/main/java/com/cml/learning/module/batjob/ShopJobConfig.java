package com.cml.learning.module.batjob;

import com.cml.learning.module.batjob.job.ShopTask;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by dudu on 2019/9/30.
 */

@Configuration
@EnableBatchProcessing
@ComponentScan(basePackageClasses = DefaultBatchConfigurer.class)
//此办法注解为@PostConstruct，表示DefaultBatchConfigurer构建和相关依赖完成注入后，调用该办法继续完成相关的初始化。
public class ShopJobConfig {
    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private ShopTask shopTask;

    @Bean
    public Job job(Step step1) {
        return jobs.get("shopJob").incrementer(new RunIdIncrementer()).start(step1).build();
    }

    @Bean
    public Step Step1() {
        //设置代理
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();
        adapter.setTargetObject(shopTask);//目标对象
        adapter.setTargetMethod("doTask");//目前方法
        return steps.get("shopJobStep1").tasklet(adapter).build();
    }

}
