package fr.vsct.quicky.jmx.server.counting;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.google.common.collect.Maps;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentMap;

/**
 * Created by Sylvain on 10/12/2014.
 */
@Aspect
@Component
public class CountingAspect {

    private ConcurrentMap<String, CounterMBean> counterMBeanMap = Maps.newConcurrentMap();

    @Autowired
    private MetricRegistry metricRegistry;

    @Around("@annotation(fr.vsct.quicky.jmx.server.counting.Counting)")
    public Object aroundAnnotation(final ProceedingJoinPoint joinPoint) throws Throwable {

        String key = joinPoint.getSignature().toLongString();

        CounterMBean counterMBean = counterMBeanMap.computeIfAbsent(key, (newKey) -> new CounterMBean(metricRegistry, joinPoint));

        Timer.Context time = counterMBean.onCall();
        try {
            Object result = joinPoint.proceed();
            counterMBean.onSuccess(time);
            return result;
        } catch (Throwable throwable) {
            counterMBean.onFailed(time);
            throw throwable;
        }
    }
}
