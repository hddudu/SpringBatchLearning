package com.cml.learning.module.batjob;

import com.cml.learning.framework.annotation.BatchAnnotation;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Created by dudu on 2019/9/30.
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableBatchProcessing
public class BatchJobModule {

    public static void main(String[] args) {
        SpringApplication.run(BatchJobModule.class, args);
    }
}
