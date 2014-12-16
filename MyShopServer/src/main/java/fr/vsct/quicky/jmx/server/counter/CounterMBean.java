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
@ManagedResource
public class CounterMBean implements SelfNaming {
    private final Timer timer;
    private final String name;

    public CounterMBean(MetricRegistry registry, ProceedingJoinPoint jp) {
        this.name = jp.getSignature().getDeclaringType().getSimpleName() + "." + jp.getSignature().getName();
        this.timer = registry.timer(name + ".timer");
    }

    public Timer.Context onCall() {
        return timer.time();
    }

    @Override
    public ObjectName getObjectName() throws MalformedObjectNameException {
        return new ObjectName(this.getClass().getPackage().getName() + ":type=counter,name=" + ObjectName.quote(this.name));
    }

    @ManagedAttribute
    public double getMeanResponseTime() {
        return TimeUnit.NANOSECONDS.toMicros((long)timer.getSnapshot().getMean());
    }
}
