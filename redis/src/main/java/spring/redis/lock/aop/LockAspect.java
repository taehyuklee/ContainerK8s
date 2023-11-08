package spring.redis.lock.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import lombok.extern.slf4j.Slf4j;
import spring.redis.lock.annotation.SpinLock;

@Slf4j
@Aspect
public class LockAspect {

    @Around("@annotation(spinLock)") //파라미터가 있을때는 Retry retry 파라미터 받은거에서 @annotation쪽으로 알아서 대체가 된다고 한다.
    public Object doRetry(ProceedingJoinPoint joinPoint, SpinLock spinLock) throws Throwable{
        log.info("locking start");

        Exception exceptionHolder = null;

        throw exceptionHolder;

    }
}