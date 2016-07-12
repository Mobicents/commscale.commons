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
package org.restcomm.commons.statistics.sender;

import com.google.gson.Gson;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

/**
 *
 * @author Ricardo Limonta
 */
public class RestcommStatsSender {

    private static String remoteServer;
    private static ResteasyClient client;
    private static Gson gson = new Gson();
    private static final Logger LOGGER = Logger.getLogger("restcomm-stats");
    private static Properties properties;

    static {
        //retrieve path
        remoteServer = ResourceBundle.getBundle("config").getString("remote-server");
        //initialize client builder
        client = new ResteasyClientBuilder().build();
    }

    /**
     * Sends statistics to a remote server.
     * @param values Map containing the statistics values.
     * @param statsType Statistics type (Gauge, Counter, Histogram, Meter and Timer).
     */
    public static void sendStats(Map<String, Object> values, String statsType) {
        Response res = client.target(UriBuilder.fromPath(remoteServer.concat(statsType))).
                request("application/json").post(Entity.json(gson.toJson(values)));
        if (res.getStatus() != 200) {
            LOGGER.log(Level.SEVERE, "{0} - {1}", new Object[]{res.getStatus(), 
                                                  res.getStatusInfo().getReasonPhrase()});
        }

        //close response channel
        res.close();
    }
    
    /**
     * Sends statistics to a remote server.
     * @param values Map containing the statistics values.
     * @param statsType Statistics type (Gauge, Counter, Histogram, Meter and Timer).
     * @param serverAddress Remoter server adrress.
     */
    public static void sendStats(Map<String, Object> values, String statsType, String serverAddress) {
        remoteServer = serverAddress;
        sendStats(values, statsType);
    }
}
