package com.stupendousware.apibatch.models;

import java.math.BigInteger;
import java.time.LocalDateTime;

public record Order(BigInteger id, String code, LocalDateTime createdDateTime, BigInteger itemId, BigInteger userId,
        String status) {
}
