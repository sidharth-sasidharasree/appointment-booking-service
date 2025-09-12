package com.booking.portal.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.booking.portal.annotation.LogExecutionTime;

@Aspect
@Component
public class LoggingAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
	
	@Around("@annotation(logExecutionTime)")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint, LogExecutionTime logExecutionTime) throws Throwable {
	    long start = System.currentTimeMillis();
	    Object proceed = joinPoint.proceed();
	    long executionTime = System.currentTimeMillis() - start;
	    // Get method name
	    String methodName = joinPoint.getSignature().getName();
	    logger.info("Method {} executed in {} ms", methodName, executionTime);
	    return proceed;
	}

}
