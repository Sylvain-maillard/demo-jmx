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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * Created by Sylvain on 10/12/2014.
 */
@Aspect
@Component
public class CountingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountingAspect.class);

    @Autowired
    @Qualifier("mbeanExporter")
    MBeanExporter exporter;

    ConcurrentMap<String, CounterMBean> counterMBeanMap = Maps.newConcurrentMap();

    @Autowired
    private MetricRegistry metricRegistry;

//    public CountingAspect() {
//        metricRegistry = new MetricRegistry();
//    }

    @Around("@annotation(fr.vsct.quicky.jmx.server.counting.Counting)")
    public Object aroundAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {

        String key = joinPoint.getSignature().toLongString();

        CounterMBean counterMBean = counterMBeanMap.computeIfAbsent(key, newKey -> {
            CounterMBean newMbean = new CounterMBean(metricRegistry, joinPoint.getSignature().toShortString());
            exporter.setEnsureUniqueRuntimeObjectNames(false);
            exporter.registerManagedResource(newMbean);
            return newMbean;
        });

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
