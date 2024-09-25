package com.stupendousware.apibatch.models;

import java.math.BigInteger;
import java.time.LocalDateTime;

public record StagingOrder(BigInteger id, String code, LocalDateTime createdDate, BigInteger itemId,
        BigInteger userId) {
}
