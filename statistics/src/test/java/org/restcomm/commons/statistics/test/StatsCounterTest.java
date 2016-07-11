package org.restcomm.commons.statistics.test;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import java.util.concurrent.TimeUnit;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restcomm.commons.statistics.reporter.RestcommStatsReporter;

/**
 * This class represents a unit test for Counter events on RestComm Stats Module. 
 * @author Ricardo Limonta
 * @since 2016-05-29
 */
public class StatsCounterTest {
    
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
        statsReporter.start(10, TimeUnit.SECONDS);

        //define metric name
        Counter counter = metrics.counter("sip-invites");

        //simulate metric sender
        for (int i = 0; i < 10; i++) {
            //increment request events
            counter.inc(i);
            //simulate interval
            Thread.sleep(100);
        }
        
        //simulate interval
        Thread.sleep(1000);
    }
}