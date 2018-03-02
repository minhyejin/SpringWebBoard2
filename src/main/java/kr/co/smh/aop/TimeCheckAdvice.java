package kr.co.smh.aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.smh.domain.mongo.dto.LogDTO;
import kr.co.smh.mongo.log.MongoLogger;

@Component
@Aspect
public class TimeCheckAdvice {

	@Autowired
	private HttpSession session;

	@Autowired
	private MongoLogger mongoLogger;

	private static final Logger logger = LoggerFactory.getLogger(TimeCheckAdvice.class);

	// @Around("execution(* kr.co.smh.service..*(..))")포인트 컷 (kr.co.smh.service..*패키지 안 모든 메소드가 실행될 때 timelog를 찍어라  )
	@Around("within(kr.co.smh.service..*)")
	public Object timeLog(ProceedingJoinPoint joinPoint) throws Throwable {//timeLog가 advice 
		String signature = joinPoint.getSignature().toShortString();//joinPoint는 메소드가 활약할 수 있는 포인트 즉 메소드 
		logger.info(signature + " is start");
		long st = System.currentTimeMillis();

		try {
			logger.info(signature + " Running");
			Object obj = joinPoint.proceed();//타겟 =객체, joinpoint는 객체 안쪽에 있는 메소드 (실제 실행되어야 하는 메소드),proceed는 그 메소드를 실제 실행시켜주는 역할 
			return obj;
		} finally {
			long et = System.currentTimeMillis();
			LogDTO logDTO = new LogDTO();
			logDTO.setSignature(signature);
			logDTO.setCurrentTime(et - st);
			mongoLogger.insertLog(logDTO);
			logger.info(signature + " is finished");
			logger.info(signature + " 경과시간 : " + (et - st));
		}
	}
}