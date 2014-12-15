package fr.vsct.quicky.jmx.server.counting;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Created by Sylvain on 10/12/2014.
 */
public class CounterMBean {

    private final Timer timer;

    public CounterMBean(MetricRegistry metricRegistry, ProceedingJoinPoint joinPoint) {
        String name = joinPoint.getSignature().getDeclaringType().getSimpleName() + "." + joinPoint.getSignature().getName();
        this.timer = metricRegistry.timer("app." + name + ".timer");
    }

    public Timer.Context onCall() {
        return timer.time();
    }

    public void onSuccess(Timer.Context time) {
        time.stop();
    }

    public void onFailed(Timer.Context time) {
        time.stop();
    }
}
