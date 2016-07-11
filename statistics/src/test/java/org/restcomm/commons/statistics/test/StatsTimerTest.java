package org.restcomm.commons.statistics.test;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import java.util.concurrent.TimeUnit;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restcomm.commons.statistics.reporter.RestcommStatsReporter;

/**
 * This class represents a unit test for Timer events on RestComm Stats Module. 
 * @author Ricardo Limonta
 * @since 2016-05-29
 */
public class StatsTimerTest {
    
    private static MetricRegistry metrics;

    @BeforeClass
    public static void setup() {
        metrics = new MetricRegistry();
    }

    @Test
    public void meterStats() throws Exception {
        //start reporter
        RestcommStatsReporter statsReporter = 
                             RestcommStatsReporter.forRegistry(metrics).build();
        
        //define periodicy
        statsReporter.start(1, TimeUnit.SECONDS);

        //define metric name
        Timer timer = metrics.timer("sip-requests");

        //start clock
        Timer.Context context = timer.time();
        
        //simulate method call
        Thread.sleep(1000);
        
        //stop clock
        context.stop();
        
        //simulate enviroment
        Thread.sleep(1000);
    }
}