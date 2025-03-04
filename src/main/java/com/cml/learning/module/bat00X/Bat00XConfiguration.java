package com.cml.learning.module.bat00X;

import com.cml.learning.module.bat00X.job.ShopTaskImpl;
import com.cml.learning.module.batjob.job.ShopTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.cml.learning.module.bat00X.beans.Person;
import com.cml.learning.module.bat00X.listener.JobCompletionNotificationListener;
import com.cml.learning.module.bat00X.processor.PersonItemProcessor;
import com.cml.learning.module.bat00X.reader.PersonReader;
import com.cml.learning.module.bat00X.writer.PersonWriter;

@Configuration
@EnableBatchProcessing
public class Bat00XConfiguration {

	private static final Logger log = LoggerFactory.getLogger(Bat00XConfiguration.class);

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	private PersonWriter personWriter;

	@Autowired
	private ShopTaskImpl shopTaskImpl;

	// tag::readerwriterprocessor[]
	@Bean
	@Scope("prototype")
	public ItemReader<Person> reader() {
		return new PersonReader();
	}

	@Bean
	public PersonItemProcessor processor() {
		return new PersonItemProcessor();
	}

	// end::readerwriterprocessor[]

	// tag::jobstep[]

	@Bean
	public Job importUserJob(JobCompletionNotificationListener listener) {
		return jobBuilderFactory.get("一次处理任务——导入用户任务").incrementer(new RunIdIncrementer()).listener(listener).flow(step1()).end().build();
	}
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1").<Person, Person> chunk(2).reader(reader()).processor(processor()).writer(personWriter).build();
	}

//	@Bean
//	public Job secondJob(JobCompletionNotificationListener listener) {
//		return jobBuilderFactory.get("importUserJob2").incrementer(new RunIdIncrementer()).listener(listener).flow(step2()).end().build();
//	}

	@Bean
	public Step step2() {
		return stepBuilderFactory.get("step2").<Person, Person> chunk(2).reader(reader()).processor(processor()).writer(personWriter).build();
	}


	// end::jobstep[]
	//************* 定义定时任务 *************//
	@Bean(name = "printJob")
	public Job printJob(Step step1) {
		return jobBuilderFactory.get("定时任务——定时打印").incrementer(new RunIdIncrementer()).start(Step1()).build();
	}



//	@Bean
//	public Step stepPrint() {
////		shopTaskImpl.doTask();//先执行打印---然后返回step
//		return stepBuilderFactory.get("stepPrint打印步骤").chunk(1).build();
//	}
	//暂时不用
	@Bean
	public Step Step1() {
		MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();
		adapter.setTargetObject(shopTaskImpl);
		adapter.setTargetMethod("doTask");
		return stepBuilderFactory.get("shopJobStep1").tasklet(adapter).build();
	}
}