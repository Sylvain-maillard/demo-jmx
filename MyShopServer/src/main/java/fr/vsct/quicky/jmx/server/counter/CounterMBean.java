package fr.vsct.quicky.jmx.server.counter;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Created by Sylvain on 16/12/2014.
 */
public class CounterMBean {
    private final Timer timer;

    public CounterMBean(MetricRegistry registry, ProceedingJoinPoint jp) {
        this.timer = registry.timer(jp.getSignature().getDeclaringType().getSimpleName() + "." + jp.getSignature().getName() + ".timer");
    }

    public Timer.Context onCall() {
        return timer.time();
    }
}
