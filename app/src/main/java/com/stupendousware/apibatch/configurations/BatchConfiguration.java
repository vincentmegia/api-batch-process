package com.stupendousware.apibatch.configurations;

import java.sql.Date;
import java.time.LocalDate;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.stupendousware.apibatch.TaskletOne;
import com.stupendousware.apibatch.models.Order;
import com.stupendousware.apibatch.models.StagingOrder;
import com.stupendousware.apibatch.processors.StagingOrderItemProcessor;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchConfiguration {
    private Logger logger = Logger.getLogger(BatchConfiguration.class.getName());

    @Autowired
    private DataSource dataSource;

    @Bean
    public JobLauncher jobLauncher(JobRepository jobRepository) throws Exception {
        var taskExecutorJobLauncher = new TaskExecutorJobLauncher();
        taskExecutorJobLauncher.setJobRepository(jobRepository);
        taskExecutorJobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        taskExecutorJobLauncher.afterPropertiesSet();
        return taskExecutorJobLauncher;
    }

    @Bean
    public Job importStagingOrderJob(JobRepository jobRepository, Step step,
            JobExecutionListener jobExecutionListener) {
        logger.info("------------------------------------jobRepo: " + jobRepository.getJobNames());
        logger.info("-----STEP:" + step);
        return new JobBuilder("importStagingOrderJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(step)
                .end()
                .listener(jobExecutionListener)
                .build();
    }

    /**
     * @param jobRepository
     * @param dataSourceTransactionManager
     * @param JdbcCursorItemReader
     * @param stagingOrderItemProcessor
     * @return
     */
    @Bean
    public Step importStagingOrderStep(JobRepository jobRepository,
            DataSourceTransactionManager dataSourceTransactionManager,
            JdbcCursorItemReader<StagingOrder> jdbcCursorItemReader,
            StagingOrderItemProcessor stagingOrderItemProcessor,
            /* JdbcBatchItemWriter<Order> jdbcBatchItemWriter, */
            TaskletOne taskletOne) throws Exception {
        logger.info("jdbcIreader: " + jdbcCursorItemReader);
        /* logger.info("setup job step writer: " + jdbcBatchItemWriter.toString()); */
        var step = new StepBuilder("importStagingOrderStep", jobRepository)
                .<StagingOrder, Order>chunk(3, dataSourceTransactionManager)
                .reader(jdbcCursorItemReader)
                .processor(stagingOrderItemProcessor)
                .writer(writer1())
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .allowStartIfComplete(true)
                .build();
        step.afterPropertiesSet();
        return step;
    }

    private JdbcBatchItemWriter<Order> writer1() {
        return new JdbcBatchItemWriterBuilder<Order>()
                .itemPreparedStatementSetter((item, ps) -> {
                    logger.info("batch-writer item: " + item);
                    ps.setString(1, item.code());
                    ps.setDate(2, Date.valueOf(LocalDate.now()));
                    ps.setInt(3, item.itemId().intValue());
                    ps.setInt(4, item.userId().intValue());
                    ps.setString(5, item.status());
                })
                .dataSource(this.dataSource)
                .sql("INSERT INTO ORDERS (code, created_datetime, item_id, user_id, status) VALUES (?, ?, ?, ?, ?);")
                .build();
    }
}
