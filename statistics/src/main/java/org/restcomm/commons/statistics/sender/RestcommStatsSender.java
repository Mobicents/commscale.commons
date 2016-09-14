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

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

import com.google.gson.Gson;

/**
 *
 * @author Ricardo Limonta
 */
public class RestcommStatsSender {

    private static String remoteServer;
//    private static HttpClient client;
    private static Gson gson = new Gson();
    private static final Logger LOGGER = Logger.getLogger("restcomm-stats");

    static {
        //retrieve path
        remoteServer = ResourceBundle.getBundle("config").getString("remote-server");
    }

    /**
     * Sends statistics to a remote server.
     * @param values Map containing the statistics values.
     * @param statsType Statistics type (Gauge, Counter, Histogram, Meter and Timer).
     * @param serverAddress Remoter server adrress.
     */
    public static boolean sendStats(Map<String, Object> values, String statsType, String serverAddress) {
        if (serverAddress != null) {
            remoteServer = serverAddress;
        }
        return sendStats(values, statsType);
    }
    
    /**
     * Sends statistics to a remote server.
     * @param values Map containing the statistics values.
     * @param statsType Statistics type (Gauge, Counter, Histogram, Meter and Timer).
     */
    public static boolean sendStats(Map<String, Object> values, String statsType) {
    	String jsonString = gson.toJson(values);
    	if(LOGGER.isLoggable(Level.FINE)) {
    		LOGGER.log(Level.FINE, "send Stats {0} to {1}", new Object[]{jsonString, remoteServer});
    	}
    	CloseableHttpClient client = null;
    	try {
	    	RequestConfig requestConfig = RequestConfig.custom()
	                .setConnectTimeout(10000)
	                .setConnectionRequestTimeout(10000)
	                .setSocketTimeout(10000)
	                .setCookieSpec(CookieSpecs.STANDARD).build();
	    	client = HttpClients.custom().
                    setSslcontext(new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy()
                    {
                        public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
                        {
                            return true;
                        }
                    }).build()).setDefaultRequestConfig(requestConfig).build();
		
    	
//	        Response res = client.target(UriBuilder.fromPath(remoteServer.concat(statsType))).
//	                                                     request("application/json").post(Entity.json(jsonString ));
    		
    		HttpPost post = new HttpPost(remoteServer.concat(statsType));
            StringEntity se = new StringEntity(jsonString);
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(se);
            HttpResponse response = client.execute(post);
    		
	        if (response.getStatusLine().getStatusCode() > 200) {
	            LOGGER.log(Level.SEVERE, "{0} - {1}", new Object[]{response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase()});
	            return false;
	        } else {
	        	if(LOGGER.isLoggable(Level.FINE)) {
	        		LOGGER.log(Level.FINE, "{0} - {1}", new Object[]{response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase()});
	        	}
	        	return true;
	        }
    	} catch(Exception e) {
    		if(LOGGER.isLoggable(Level.INFO)) {
    			if(e.getCause() != null) {
    				LOGGER.log(Level.INFO, "couldn't send stats data to " + remoteServer + " because of " + e.getMessage() + ", root cause: " + e.getCause().getMessage());
    			} else {
    				LOGGER.log(Level.INFO, "couldn't send stats data to " + remoteServer + " because of " + e.getMessage());
    			}
        	}
    		return false;
    	} finally {
    		try {
    			if(client != null)
    				client.close();
			} catch (IOException e) {
				if(LOGGER.isLoggable(Level.INFO)) {
					LOGGER.log(Level.INFO, "couldn't close the httpclient for " + remoteServer + " because of " + e.getMessage());
				}
			}
    	}
    }
}
