package com.stupendousware.apibatch.configurations;

import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.stupendousware.apibatch.mappers.StagingOrderRowMapper;
import com.stupendousware.apibatch.models.StagingOrder;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class AppConfiguration {
    private Logger logger = Logger.getLogger(AppConfiguration.class.getName());

    @Autowired
    private DataSource dataSource;

    @Bean("transactionManager")
    public PlatformTransactionManager platformTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * @return
     */
    @Bean
    public JdbcCursorItemReader<StagingOrder> itemReader() {
        logger.info("registering JdbcCursorItemReader");
        return new JdbcCursorItemReaderBuilder<StagingOrder>()
                .dataSource(this.dataSource)
                .name("stagingOrderReader")
                .sql("SELECT id, code, created_datetime, item_id, user_id FROM STAGING_ORDER")
                .rowMapper(new StagingOrderRowMapper())
                .build();
    }
}
