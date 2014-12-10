package fr.vsct.quicky.jmx.server.counting;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.jmx.export.naming.SelfNaming;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Sylvain on 10/12/2014.
 */
@ManagedResource
public class CounterMBean implements SelfNaming {

    private final String name;
    private final Timer timer;
    private final Counter failedCounter;

    public CounterMBean(MetricRegistry metricRegistry, String name) {
        this.name = name;
        this.timer = metricRegistry.timer(name);
        this.failedCounter = metricRegistry.counter(name+"_failed");
    }

    public Timer.Context onCall() {
        return timer.time();
    }

    public void onSuccess(Timer.Context time) {
        time.stop();
    }

    public void onFailed(Timer.Context time) {
        time.stop();
        failedCounter.inc();
    }

    @Override
    public ObjectName getObjectName() throws MalformedObjectNameException {
        return new ObjectName("fr.vsct.quicky.jmx.server.counters:type=Counter,name=" + name);
    }

    @ManagedAttribute
    public long getFailedCount() {
        return failedCounter.getCount();
    }

    @ManagedAttribute
    public long getMeanResponseTime() {
        return TimeUnit.NANOSECONDS.toMillis((long) timer.getSnapshot().getMean());
    }

    @ManagedAttribute
    public long getMeanResponseTimeInNanos() {
        return (long) timer.getSnapshot().getMean();
    }
}
