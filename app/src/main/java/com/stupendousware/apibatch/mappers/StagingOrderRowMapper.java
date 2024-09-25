package com.stupendousware.apibatch.mappers;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.stupendousware.apibatch.models.StagingOrder;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class StagingOrderRowMapper implements RowMapper<StagingOrder> {
    private Logger logger = Logger.getLogger(StagingOrderRowMapper.class.getName());
    public static final String ID_COLUMN = "id";
    public static final String CODE_COLUMN = "code";
    public static final String CREATED_DATETIME_COLUMN = "created_datetime";
    public static final String ITEM_ID_COLUMN = "item_id";
    public static final String USER_ID_COLUMN = "user_id";

    @Override
    public StagingOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
        var stagingOrder = new StagingOrder(BigInteger.valueOf(rs.getInt(ID_COLUMN)),
                rs.getString(CODE_COLUMN),
                rs.getTimestamp(CREATED_DATETIME_COLUMN).toLocalDateTime(),
                BigInteger.valueOf(rs.getInt(ITEM_ID_COLUMN)),
                BigInteger.valueOf(rs.getInt(USER_ID_COLUMN)));
        logger.info("stagingOrder: " + stagingOrder);
        return stagingOrder;
    }

}
