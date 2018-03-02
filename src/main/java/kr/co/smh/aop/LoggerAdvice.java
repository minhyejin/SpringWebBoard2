package kr.co.smh.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggerAdvice {

	Logger logger = LoggerFactory.getLogger(LoggerAdvice.class);

	//user 관련된 서비스가 실행 될 때의 포인트컷
	@Around("within(kr.co.smh.service.user..*)")//내가 실행해야될 메소드가 어디있는지 알려줌(프록시가 어디처리해야되는지 알려줌 ) 
	// @Around("execution(* kr.co.smh.service.user..*(..))")//원래 하려했던 : 타겟 
	public Object userLogger(ProceedingJoinPoint joinPoint) throws Throwable {//메게변수 
		String signature = joinPoint.getSignature().toShortString();//사용자가 원래 하려고 했던 메소드 
		logger.info("============c=="+signature + " 실행 시작합니다.=============");
		try {
			Object obj = joinPoint.proceed();//실행 
			return obj;//메소드의 리턴값 뭐든지 전부다 등장할 수 있음 
		} finally {
			logger.info("============="+signature + " 실행 종료합니다.=============");//log 마지막으로 찍어줌 
		}
	}
}