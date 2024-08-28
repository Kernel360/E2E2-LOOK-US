package org.example.log;

import java.lang.reflect.Method;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Aspect
@Component
public class LogAspect {

	// Log4j Logger 인스턴스 생성 (LogAspect 전용)
	private static final Logger defaultLogger = LogManager.getLogger(LogAspect.class);

	@Pointcut("@annotation(org.example.log.LogExecution)")
	public void logExecutionAnnotation() {
	}

	@Around("logExecutionAnnotation()")
	public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		try {
			Object result = joinPoint.proceed();
			return result;
		} finally {
			long finish = System.currentTimeMillis();
			long timeMs = finish - start;
			logToFile(joinPoint, timeMs);
		}
	}

	private void logToFile(JoinPoint joinPoint, long timeMs) {
		Logger logger = selectLogger(joinPoint);
		logInfo(logger, joinPoint, timeMs);
	}

	private Logger selectLogger(JoinPoint joinPoint) {
		String packageName = joinPoint.getSignature().getDeclaringType().getPackage().getName();

		 if (packageName.startsWith("org.example.config")) {
			return LogManager.getLogger("org.example.config");
		} else if (packageName.startsWith("org.example.follow")) {
			return LogManager.getLogger("org.example.follow");
		} else if (packageName.startsWith("org.example.image")) {
			return LogManager.getLogger("org.example.image");
		} else if (packageName.startsWith("org.example.post")) {
			return LogManager.getLogger("org.example.post");
		} else if (packageName.startsWith("org.example.scrap")) {
			return LogManager.getLogger("org.example.scrap");
		} else if (packageName.startsWith("org.example.user")) {
			return LogManager.getLogger("org.example.user");
		} else if (packageName.startsWith("org.example.util")) {
			return LogManager.getLogger("org.example.util");
		} else {
			return defaultLogger;
		}
	}

	private void logInfo(Logger logger, JoinPoint joinPoint, Long timeMs) {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		logger.info("method = {}", method.getName());

		if (timeMs != null) {
			logger.info("timeMs = {}", timeMs);
		}

		Object[] args = joinPoint.getArgs();
		for (Object arg : args) {
			if (arg != null) {
				logger.info("type = {}", arg.getClass().getSimpleName());
				logger.info("value = {}", arg);
			}
		}
	}
}
