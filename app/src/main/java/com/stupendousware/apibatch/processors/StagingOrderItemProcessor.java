package com.stupendousware.apibatch.processors;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.stupendousware.apibatch.mappers.StagingOrderRowMapper;
import com.stupendousware.apibatch.models.Order;
import com.stupendousware.apibatch.models.StagingOrder;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class StagingOrderItemProcessor implements ItemProcessor<StagingOrder, Order> {
    private Logger logger = Logger.getLogger(StagingOrderRowMapper.class.getName());

    @Override
    public Order process(StagingOrder item) throws Exception {
        var order = new Order(item.id(),
                item.code().toUpperCase() + LocalDateTime.now(),
                item.createdDate(),
                item.itemId(),
                item.userId(),
                "NEW");
        logger.info("StagingOrder processor order: " + order);
        return order;
    }

}
