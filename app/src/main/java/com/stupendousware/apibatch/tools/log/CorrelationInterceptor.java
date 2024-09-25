package com.stupendousware.apibatch.tools.log;

import java.util.UUID;
import java.util.logging.Logger;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import io.micrometer.core.instrument.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CorrelationInterceptor implements HandlerInterceptor {
    private static final String CORRELATION_ID_HEADER_NAME = "X-Correlation-Id";
    private static final String CORRELATION_ID_LOG_VAR_NAME = "correlationId";
    private Logger logger = Logger.getLogger(CorrelationInterceptor.class.getName());

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        MDC.remove(CORRELATION_ID_LOG_VAR_NAME);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        var correlationId = getCorrelationId(request);
        MDC.put(CORRELATION_ID_LOG_VAR_NAME, correlationId);
        return true;
    }

    /**
     * @param request
     * @return
     */
    private String getCorrelationId(HttpServletRequest request) {
        var correlationId = request.getHeader(CORRELATION_ID_HEADER_NAME);
        if (StringUtils.isEmpty(correlationId)) {
            correlationId = UUID.randomUUID().toString();
        }
        logger.info("generated correlationId: " + correlationId);
        return correlationId;
    }
}
