package fr.vsct.quicky.jmx.server.counter;

import com.codahale.metrics.Timer;
import com.google.common.collect.Maps;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Sylvain on 16/12/2014.
 */
@Aspect
@Component
public class CountingAspect {

    @Around("@annotation(fr.vsct.quicky.jmx.server.counter.Counting)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            System.out.println("it works.");
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            throw throwable;
        }
    }
}
