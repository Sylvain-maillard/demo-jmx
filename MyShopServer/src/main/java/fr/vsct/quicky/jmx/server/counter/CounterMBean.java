package fr.vsct.quicky.jmx.server.counter;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.jmx.export.naming.SelfNaming;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sylvain on 16/12/2014.
 */
public class CounterMBean {
    private final Timer timer;
    public CounterMBean(MetricRegistry registry, ProceedingJoinPoint jp) {
        String name = jp.getSignature().getDeclaringType().getSimpleName() + "." + jp.getSignature().getName();
        this.timer = registry.timer("app." + name + ".timer");
    }

    public Timer.Context onCall() {
        return timer.time();
    }
}
