/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
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
    }
    
    @Test
    public void meterStats() throws Exception {
        //start reporter
    	RestcommStatsReporter statsReporter = RestcommStatsReporter.getRestcommStatsReporter();
        metrics = RestcommStatsReporter.getMetricRegistry();
        
        //define remote server address (optionally)
        statsReporter.setRemoteServer("http://192.168.0.44/rest/");
        statsReporter.setProjectName("sipservlets");
        statsReporter.setProjectType("community");
        statsReporter.setVersion("3.0.0.GA");
        //define periodicy
        statsReporter.start(10, TimeUnit.SECONDS);

        //define metric name
        Counter counterCalls = metrics.counter("calls");
        Counter counterMinutes = metrics.counter("minutes");
        Counter counterMessages = metrics.counter("messages");

        
        //simulate metric sender
        for (int i = 1; i < 10; i++) {
            //increment request events
            counterCalls.inc(i);
            counterMinutes.inc(i);
            counterMessages.inc(i);
            System.out.println(counterCalls.getCount());
            //simulate interval
            Thread.sleep(1000);
        }
//        statsReporter.stop();
        //simulate interval
        Thread.sleep(5000);
    }
}