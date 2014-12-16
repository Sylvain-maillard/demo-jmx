package fr.vsct.quicky.jmx.server.counter;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.google.common.collect.Maps;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Sylvain on 16/12/2014.
 */
@Aspect
@Component
public class CountingAspect {

    Map<String, CounterMBean> counterMBeanMap = Maps.newConcurrentMap();

    MetricRegistry metricRegistry = new MetricRegistry();

    @Autowired
    MBeanExporter mBeanExporter;

    @Around("@annotation(fr.vsct.quicky.jmx.server.counter.Counting)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        CounterMBean counterMBean = counterMBeanMap.computeIfAbsent(joinPoint.toLongString(), newKey -> {
            CounterMBean mBean = new CounterMBean(metricRegistry, joinPoint);
            mBeanExporter.setEnsureUniqueRuntimeObjectNames(false);
            mBeanExporter.registerManagedResource(mBean);
            return mBean;
        });

        Timer.Context context = counterMBean.onCall();
        try {
            return joinPoint.proceed();
        } finally {
            context.stop();
        }
    }
}
