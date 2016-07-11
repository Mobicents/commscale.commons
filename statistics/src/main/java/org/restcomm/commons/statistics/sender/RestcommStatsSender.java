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

    private static final String PATH;
    private static ResteasyClient client;
    private static Gson gson = new Gson();
    private static final Logger LOGGER = Logger.getLogger("restcomm-stats");
    private static Properties properties;

    static {
        //retrieve path
        PATH = ResourceBundle.getBundle("config").getString("remote-server");
        //initialize client builder
        client = new ResteasyClientBuilder().build();
    }

    public static void sendStats(Map<String, Object> values, String statsType) {
        Response res = client.target(UriBuilder.fromPath(PATH.concat(statsType))).
                request("application/json").post(Entity.json(gson.toJson(values)));
        if (res.getStatus() != 200) {
            LOGGER.log(Level.SEVERE, "{0} - {1}", new Object[]{res.getStatus(), 
                                                  res.getStatusInfo().getReasonPhrase()});
        }

        //close response channel
        res.close();
    }
}
